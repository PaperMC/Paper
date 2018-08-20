package com.destroystokyo.paper.antixray;

import net.minecraft.server.Chunk;
import net.minecraft.server.IBlockData;
import net.minecraft.server.PacketPlayOutMapChunk;

public final class ChunkPacketInfoAntiXray extends ChunkPacketInfo<IBlockData> implements Runnable {

    private Chunk[] nearbyChunks;
    private final ChunkPacketBlockControllerAntiXray chunkPacketBlockControllerAntiXray;

    public ChunkPacketInfoAntiXray(PacketPlayOutMapChunk packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector,
                                   ChunkPacketBlockControllerAntiXray chunkPacketBlockControllerAntiXray) {
        super(packetPlayOutMapChunk, chunk, chunkSectionSelector);
        this.chunkPacketBlockControllerAntiXray = chunkPacketBlockControllerAntiXray;
    }

    public Chunk[] getNearbyChunks() {
        return nearbyChunks;
    }

    public void setNearbyChunks(Chunk... nearbyChunks) {
        this.nearbyChunks = nearbyChunks;
    }

    @Override
    public void run() {
        chunkPacketBlockControllerAntiXray.obfuscate(this);
    }
}
