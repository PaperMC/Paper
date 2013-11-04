package net.minecraft.server;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class PacketPlayOutMapChunkBulk extends Packet {

    private int[] a;
    private int[] b;
    private int[] c;
    private int[] d;
    private byte[] buffer;
    private byte[][] inflatedBuffers;
    private int size;
    private boolean h;
    private byte[] buildBuffer = new byte[0]; // CraftBukkit - remove static
    // CraftBukkit start
    static final ThreadLocal<Deflater> localDeflater = new ThreadLocal<Deflater>() {
        @Override
        protected Deflater initialValue() {
            // Don't use higher compression level, slows things down too much
            return new Deflater(6);
        }
    };
    // CraftBukkit end

    public PacketPlayOutMapChunkBulk() {}

    public PacketPlayOutMapChunkBulk(List list) {
        int i = list.size();

        this.a = new int[i];
        this.b = new int[i];
        this.c = new int[i];
        this.d = new int[i];
        this.inflatedBuffers = new byte[i][];
        this.h = !list.isEmpty() && !((Chunk) list.get(0)).world.worldProvider.g;
        int j = 0;

        for (int k = 0; k < i; ++k) {
            Chunk chunk = (Chunk) list.get(k);
            ChunkMap chunkmap = PacketPlayOutMapChunk.a(chunk, true, '\uffff');

            if (buildBuffer.length < j + chunkmap.a.length) {
                byte[] abyte = new byte[j + chunkmap.a.length];

                System.arraycopy(buildBuffer, 0, abyte, 0, buildBuffer.length);
                buildBuffer = abyte;
            }

            System.arraycopy(chunkmap.a, 0, buildBuffer, j, chunkmap.a.length);
            j += chunkmap.a.length;
            this.a[k] = chunk.locX;
            this.b[k] = chunk.locZ;
            this.c[k] = chunkmap.b;
            this.d[k] = chunkmap.c;
            this.inflatedBuffers[k] = chunkmap.a;
        }

        /* CraftBukkit start - Moved to compress()
        Deflater deflater = new Deflater(-1);

        try {
            deflater.setInput(buildBuffer, 0, j);
            deflater.finish();
            this.buffer = new byte[j];
            this.size = deflater.deflate(this.buffer);
        } finally {
            deflater.end();
        }
        */
    }

    // Add compression method
    public void compress() {
        if (this.buffer != null) {
            return;
        }

        Deflater deflater = localDeflater.get();
        deflater.reset();
        deflater.setInput(this.buildBuffer);
        deflater.finish();

        this.buffer = new byte[this.buildBuffer.length + 100];
        this.size = deflater.deflate(this.buffer);
    }
    // CraftBukkit end

    public static int c() {
        return 5;
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - throws IOException
        short short1 = packetdataserializer.readShort();

        this.size = packetdataserializer.readInt();
        this.h = packetdataserializer.readBoolean();
        this.a = new int[short1];
        this.b = new int[short1];
        this.c = new int[short1];
        this.d = new int[short1];
        this.inflatedBuffers = new byte[short1][];
        if (buildBuffer.length < this.size) {
            buildBuffer = new byte[this.size];
        }

        packetdataserializer.readBytes(buildBuffer, 0, this.size);
        byte[] abyte = new byte[PacketPlayOutMapChunk.c() * short1];
        Inflater inflater = new Inflater();

        inflater.setInput(buildBuffer, 0, this.size);

        try {
            inflater.inflate(abyte);
        } catch (DataFormatException dataformatexception) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }

        int i = 0;

        for (int j = 0; j < short1; ++j) {
            this.a[j] = packetdataserializer.readInt();
            this.b[j] = packetdataserializer.readInt();
            this.c[j] = packetdataserializer.readShort();
            this.d[j] = packetdataserializer.readShort();
            int k = 0;
            int l = 0;

            int i1;

            for (i1 = 0; i1 < 16; ++i1) {
                k += this.c[j] >> i1 & 1;
                l += this.d[j] >> i1 & 1;
            }

            i1 = 2048 * 4 * k + 256;
            i1 += 2048 * l;
            if (this.h) {
                i1 += 2048 * k;
            }

            this.inflatedBuffers[j] = new byte[i1];
            System.arraycopy(abyte, i, this.inflatedBuffers[j], 0, i1);
            i += i1;
        }
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - throws IOException
        compress(); // CraftBukkit
        packetdataserializer.writeShort(this.a.length);
        packetdataserializer.writeInt(this.size);
        packetdataserializer.writeBoolean(this.h);
        packetdataserializer.writeBytes(this.buffer, 0, this.size);

        for (int i = 0; i < this.a.length; ++i) {
            packetdataserializer.writeInt(this.a[i]);
            packetdataserializer.writeInt(this.b[i]);
            packetdataserializer.writeShort((short) (this.c[i] & '\uffff'));
            packetdataserializer.writeShort((short) (this.d[i] & '\uffff'));
        }
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public String b() {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < this.a.length; ++i) {
            if (i > 0) {
                stringbuilder.append(", ");
            }

            stringbuilder.append(String.format("{x=%d, z=%d, sections=%d, adds=%d, data=%d}", new Object[] { Integer.valueOf(this.a[i]), Integer.valueOf(this.b[i]), Integer.valueOf(this.c[i]), Integer.valueOf(this.d[i]), Integer.valueOf(this.inflatedBuffers[i].length)}));
        }

        return String.format("size=%d, chunks=%d[%s]", new Object[] { Integer.valueOf(this.size), Integer.valueOf(this.a.length), stringbuilder});
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
