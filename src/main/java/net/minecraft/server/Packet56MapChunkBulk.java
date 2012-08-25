package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet56MapChunkBulk extends Packet {

    private int[] c;
    private int[] d;
    public int[] a;
    public int[] b;
    // CraftBukkit start - private -> public
    public byte[] buffer;
    private byte[][] inflatedBuffers;
    public int size;
    public byte[] buildBuffer = new byte[0]; // - static
    // CraftBukkit end

    public Packet56MapChunkBulk() {}

    public Packet56MapChunkBulk(List list) {
        int i = list.size();

        this.c = new int[i];
        this.d = new int[i];
        this.a = new int[i];
        this.b = new int[i];
        this.inflatedBuffers = new byte[i][];
        int j = 0;

        for (int k = 0; k < i; ++k) {
            Chunk chunk = (Chunk) list.get(k);
            ChunkMap chunkmap = Packet51MapChunk.a(chunk, true, '\uffff');

            if (buildBuffer.length < j + chunkmap.a.length) {
                byte[] abyte = new byte[j + chunkmap.a.length];

                System.arraycopy(buildBuffer, 0, abyte, 0, buildBuffer.length);
                buildBuffer = abyte;
            }

            System.arraycopy(chunkmap.a, 0, buildBuffer, j, chunkmap.a.length);
            j += chunkmap.a.length;
            this.c[k] = chunk.x;
            this.d[k] = chunk.z;
            this.a[k] = chunkmap.b;
            this.b[k] = chunkmap.c;
            this.inflatedBuffers[k] = chunkmap.a;
        }

        /* CraftBukkit start
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
        this.lowPriority = true;
        // CraftBukkit end
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOException
        short short1 = datainputstream.readShort();

        this.size = datainputstream.readInt();
        this.c = new int[short1];
        this.d = new int[short1];
        this.a = new int[short1];
        this.b = new int[short1];
        this.inflatedBuffers = new byte[short1][];
        if (buildBuffer.length < this.size) {
            buildBuffer = new byte[this.size];
        }

        datainputstream.readFully(buildBuffer, 0, this.size);
        byte[] abyte = new byte[196864 * short1];
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
            this.c[j] = datainputstream.readInt();
            this.d[j] = datainputstream.readInt();
            this.a[j] = datainputstream.readShort();
            this.b[j] = datainputstream.readShort();
            int k = 0;

            int l;

            for (l = 0; l < 16; ++l) {
                k += this.a[j] >> l & 1;
            }

            l = 2048 * 5 * k + 256;
            this.inflatedBuffers[j] = new byte[l];
            System.arraycopy(abyte, i, this.inflatedBuffers[j], 0, l);
            i += l;
        }
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeShort(this.c.length);
        dataoutputstream.writeInt(this.size);
        dataoutputstream.write(this.buffer, 0, this.size);

        for (int i = 0; i < this.c.length; ++i) {
            dataoutputstream.writeInt(this.c[i]);
            dataoutputstream.writeInt(this.d[i]);
            dataoutputstream.writeShort((short) (this.a[i] & '\uffff'));
            dataoutputstream.writeShort((short) (this.b[i] & '\uffff'));
        }
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 6 + this.size + 12 * this.d();
    }

    public int d() {
        return this.c.length;
    }
}
