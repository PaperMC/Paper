package org.bukkit.craftbukkit;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.Deflater;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet51MapChunk;
import net.minecraft.server.Packet56MapChunkBulk;

public final class ChunkCompressionThread implements Runnable {

    private static final ChunkCompressionThread instance = new ChunkCompressionThread();
    private static boolean isRunning = false;

    private final int QUEUE_CAPACITY = 1024 * 10;
    private final HashMap<EntityPlayer, Integer> queueSizePerPlayer = new HashMap<EntityPlayer, Integer>();
    private final BlockingQueue<QueuedPacket> packetQueue = new LinkedBlockingQueue<QueuedPacket>(QUEUE_CAPACITY);

    private final int CHUNK_SIZE = 16 * 256 * 16 * 5 / 2;
    private final int REDUCED_DEFLATE_THRESHOLD = CHUNK_SIZE / 4;
    private final int DEFLATE_LEVEL_CHUNKS = 6;
    private final int DEFLATE_LEVEL_PARTS = 1;

    private final Deflater deflater = new Deflater();
    private byte[] deflateBuffer = new byte[CHUNK_SIZE + 100];

    public static void startThread() {
        if (!isRunning) {
            isRunning = true;
            new Thread(instance).start();
        }
    }

    public void run() {
        while (true) {
            try {
                handleQueuedPacket(packetQueue.take());
            } catch (InterruptedException ie) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleQueuedPacket(QueuedPacket queuedPacket) {
        addToPlayerQueueSize(queuedPacket.player, -1);

        // Compress the packet if necessary
        if (queuedPacket.compress == 1) {
            handleMapChunk((Packet51MapChunk) queuedPacket.packet);
        } else if (queuedPacket.compress == 2) {
            handleMapChunkBulk((Packet56MapChunkBulk) queuedPacket.packet);
        }

        sendToNetworkQueue(queuedPacket);
    }

    private void handleMapChunkBulk(Packet56MapChunkBulk packet) {
        if (packet.buffer != null) {
            return;
        }

        int dataSize = packet.buildBuffer.length;
        if (deflateBuffer.length < dataSize + 100) {
            deflateBuffer = new byte[dataSize + 100];
        }

        deflater.reset();
        deflater.setLevel(dataSize < REDUCED_DEFLATE_THRESHOLD ? DEFLATE_LEVEL_PARTS : DEFLATE_LEVEL_CHUNKS);
        deflater.setInput(packet.buildBuffer);
        deflater.finish();
        int size = deflater.deflate(deflateBuffer);
        if (size == 0) {
            size = deflater.deflate(deflateBuffer);
        }

        // copy compressed data to packet
        packet.buffer = new byte[size];
        packet.size = size;
        System.arraycopy(deflateBuffer, 0, packet.buffer, 0, size);
    }

    private void handleMapChunk(Packet51MapChunk packet) {
        // If 'packet.buffer' is set then this packet has already been compressed.
        if (packet.buffer != null) {
            return;
        }

        int dataSize = packet.inflatedBuffer.length;
        if (deflateBuffer.length < dataSize + 100) {
            deflateBuffer = new byte[dataSize + 100];
        }

        deflater.reset();
        deflater.setLevel(dataSize < REDUCED_DEFLATE_THRESHOLD ? DEFLATE_LEVEL_PARTS : DEFLATE_LEVEL_CHUNKS);
        deflater.setInput(packet.inflatedBuffer);
        deflater.finish();
        int size = deflater.deflate(deflateBuffer);
        if (size == 0) {
            size = deflater.deflate(deflateBuffer);
        }

        // copy compressed data to packet
        packet.buffer = new byte[size];
        packet.size = size;
        System.arraycopy(deflateBuffer, 0, packet.buffer, 0, size);
    }

    private void sendToNetworkQueue(QueuedPacket queuedPacket) {
        queuedPacket.player.netServerHandler.networkManager.queue(queuedPacket.packet);
    }

    public static void sendPacket(EntityPlayer player, Packet packet) {
        int compressType = 0;

        if (packet instanceof Packet51MapChunk) {
            compressType = 1;
        } else if (packet instanceof Packet56MapChunkBulk) {
            compressType = 2;
        }

        instance.addQueuedPacket(new QueuedPacket(player, packet, compressType));
    }

    private void addToPlayerQueueSize(EntityPlayer player, int amount) {
        synchronized (queueSizePerPlayer) {
            Integer count = queueSizePerPlayer.get(player);
            amount += (count == null) ? 0 : count;
            if (amount == 0) {
                queueSizePerPlayer.remove(player);
            } else {
                queueSizePerPlayer.put(player, amount);
            }
        }
    }

    public static int getPlayerQueueSize(EntityPlayer player) {
        synchronized (instance.queueSizePerPlayer) {
            Integer count = instance.queueSizePerPlayer.get(player);
            return count == null ? 0 : count;
        }
    }

    private void addQueuedPacket(QueuedPacket task) {
        addToPlayerQueueSize(task.player, +1);

        while (true) {
            try {
                packetQueue.put(task);
                return;
            } catch (InterruptedException e) {
            }
        }
    }

    private static class QueuedPacket {
        final EntityPlayer player;
        final Packet packet;
        final int compress;

        QueuedPacket(EntityPlayer player, Packet packet, int compress) {
            this.player = player;
            this.packet = packet;
            this.compress = compress;
        }
    }
}