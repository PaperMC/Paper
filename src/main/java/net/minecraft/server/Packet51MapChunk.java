package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet51MapChunk extends Packet {

    public int a;
    public int b;
    public int c;
    public int d;
    public byte[] buffer;
    public boolean f;
    public int size; // CraftBukkit - private -> public
    private int h;
    public byte[] rawData = new byte[0]; // CraftBukkit

    public Packet51MapChunk() {
        this.lowPriority = true;
    }

    // CraftBukkit start
    public Packet51MapChunk(Chunk chunk, boolean flag, int i) {
        this.lowPriority = true;
        this.a = chunk.x;
        this.b = chunk.z;
        this.f = flag;
        if (flag) {
            i = '\uffff';
            chunk.seenByPlayer = true;
        }

        ChunkSection[] achunksection = chunk.h();
        int j = 0;
        int k = 0;

        int l;

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].a()) && (i & 1 << l) != 0) {
                this.c |= 1 << l;
                ++j;
                if (achunksection[l].h() != null) {
                    this.d |= 1 << l;
                    ++k;
                }
            }
        }

        l = 2048 * (5 * j + k);
        if (flag) {
            l += 256;
        }

        if (rawData.length < l) {
            rawData = new byte[l];
        }

        byte[] abyte = rawData;
        int i1 = 0;

        int j1;

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].a()) && (i & 1 << j1) != 0) {
                byte[] abyte1 = achunksection[j1].g();

                System.arraycopy(abyte1, 0, abyte, i1, abyte1.length);
                i1 += abyte1.length;
            }
        }

        NibbleArray nibblearray;

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].a()) && (i & 1 << j1) != 0) {
                nibblearray = achunksection[j1].i();
                System.arraycopy(nibblearray.a, 0, abyte, i1, nibblearray.a.length);
                i1 += nibblearray.a.length;
            }
        }

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].a()) && (i & 1 << j1) != 0) {
                nibblearray = achunksection[j1].j();
                System.arraycopy(nibblearray.a, 0, abyte, i1, nibblearray.a.length);
                i1 += nibblearray.a.length;
            }
        }

        for (j1 = 0; j1 < achunksection.length; ++j1) {
            if (achunksection[j1] != null && (!flag || !achunksection[j1].a()) && (i & 1 << j1) != 0) {
                nibblearray = achunksection[j1].k();
                System.arraycopy(nibblearray.a, 0, abyte, i1, nibblearray.a.length);
                i1 += nibblearray.a.length;
            }
        }

        if (k > 0) {
            for (j1 = 0; j1 < achunksection.length; ++j1) {
                if (achunksection[j1] != null && (!flag || !achunksection[j1].a()) && achunksection[j1].h() != null && (i & 1 << j1) != 0) {
                    nibblearray = achunksection[j1].h();
                    System.arraycopy(nibblearray.a, 0, abyte, i1, nibblearray.a.length);
                    i1 += nibblearray.a.length;
                }
            }
        }

        if (flag) {
            byte[] abyte2 = chunk.l();

            System.arraycopy(abyte2, 0, abyte, i1, abyte2.length);
            i1 += abyte2.length;
        }

        /* CraftBukkit start - Moved compression into its own method.
        byte[] abyte = data; // CraftBukkit - uses data from above constructor
        Deflater deflater = new Deflater(-1);

        try {
            deflater.setInput(abyte, 0, i1);
            deflater.finish();
            this.buffer = new byte[i1];
            this.size = deflater.deflate(this.buffer);
        } finally {
            deflater.end();
        } */
        this.rawData = abyte;
        // CraftBukkit end
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOEXception
        this.a = datainputstream.readInt();
        this.b = datainputstream.readInt();
        this.f = datainputstream.readBoolean();
        this.c = datainputstream.readShort();
        this.d = datainputstream.readShort();
        this.size = datainputstream.readInt();
        this.h = datainputstream.readInt();
        if (rawData.length < this.size) {
            rawData = new byte[this.size];
        }

        datainputstream.readFully(rawData, 0, this.size);
        int i = 0;

        int j;

        for (j = 0; j < 16; ++j) {
            i += this.c >> j & 1;
        }

        j = 12288 * i;
        if (this.f) {
            j += 256;
        }

        this.buffer = new byte[j];
        Inflater inflater = new Inflater();

        inflater.setInput(rawData, 0, this.size);

        try {
            inflater.inflate(this.buffer);
        } catch (DataFormatException dataformatexception) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeInt(this.b);
        dataoutputstream.writeBoolean(this.f);
        dataoutputstream.writeShort((short) (this.c & '\uffff'));
        dataoutputstream.writeShort((short) (this.d & '\uffff'));
        dataoutputstream.writeInt(this.size);
        dataoutputstream.writeInt(this.h);
        dataoutputstream.write(this.buffer, 0, this.size);
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 17 + this.size;
    }
}
