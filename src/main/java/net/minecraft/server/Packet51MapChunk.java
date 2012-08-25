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
    // CraftBukkit start - private -> public
    public byte[] buffer;
    public byte[] inflatedBuffer;
    public boolean e;
    public int size;
    // CraftBukkit end
    private static byte[] buildBuffer = new byte[196864];

    public Packet51MapChunk() {
        this.lowPriority = true;
    }

    public Packet51MapChunk(Chunk chunk, boolean flag, int i) {
        this.lowPriority = true;
        this.a = chunk.x;
        this.b = chunk.z;
        this.e = flag;
        ChunkMap chunkmap = a(chunk, flag, i);
        // Deflater deflater = new Deflater(-1); // CraftBukkit

        this.d = chunkmap.c;
        this.c = chunkmap.b;

        /* CraftBukkit start - compression moved to new thread
        try {
            this.inflatedBuffer = chunkmap.a;
            deflater.setInput(chunkmap.a, 0, chunkmap.a.length);
            deflater.finish();
            this.buffer = new byte[chunkmap.a.length];
            this.size = deflater.deflate(this.buffer);
        } finally {
            deflater.end();
        }
        */
        this.inflatedBuffer = chunkmap.a;
        // CraftBukkit end
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.a = datainputstream.readInt();
        this.b = datainputstream.readInt();
        this.e = datainputstream.readBoolean();
        this.c = datainputstream.readShort();
        this.d = datainputstream.readShort();
        this.size = datainputstream.readInt();
        if (buildBuffer.length < this.size) {
            buildBuffer = new byte[this.size];
        }

        datainputstream.readFully(buildBuffer, 0, this.size);
        int i = 0;

        int j;

        for (j = 0; j < 16; ++j) {
            i += this.c >> j & 1;
        }

        j = 12288 * i;
        if (this.e) {
            j += 256;
        }

        this.inflatedBuffer = new byte[j];
        Inflater inflater = new Inflater();

        inflater.setInput(buildBuffer, 0, this.size);

        try {
            inflater.inflate(this.inflatedBuffer);
        } catch (DataFormatException dataformatexception) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeInt(this.b);
        dataoutputstream.writeBoolean(this.e);
        dataoutputstream.writeShort((short) (this.c & '\uffff'));
        dataoutputstream.writeShort((short) (this.d & '\uffff'));
        dataoutputstream.writeInt(this.size);
        dataoutputstream.write(this.buffer, 0, this.size);
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 17 + this.size;
    }

    public static ChunkMap a(Chunk chunk, boolean flag, int i) {
        int j = 0;
        ChunkSection[] achunksection = chunk.i();
        int k = 0;
        ChunkMap chunkmap = new ChunkMap();
        byte[] abyte = buildBuffer;

        if (flag) {
            chunk.seenByPlayer = true;
        }

        int l;

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].a()) && (i & 1 << l) != 0) {
                chunkmap.b |= 1 << l;
                if (achunksection[l].i() != null) {
                    chunkmap.c |= 1 << l;
                    ++k;
                }
            }
        }

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].a()) && (i & 1 << l) != 0) {
                byte[] abyte1 = achunksection[l].g();

                System.arraycopy(abyte1, 0, abyte, j, abyte1.length);
                j += abyte1.length;
            }
        }

        NibbleArray nibblearray;

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].a()) && (i & 1 << l) != 0) {
                nibblearray = achunksection[l].j();
                System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                j += nibblearray.a.length;
            }
        }

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].a()) && (i & 1 << l) != 0) {
                nibblearray = achunksection[l].k();
                System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                j += nibblearray.a.length;
            }
        }

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].a()) && (i & 1 << l) != 0) {
                nibblearray = achunksection[l].l();
                System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                j += nibblearray.a.length;
            }
        }

        if (k > 0) {
            for (l = 0; l < achunksection.length; ++l) {
                if (achunksection[l] != null && (!flag || !achunksection[l].a()) && achunksection[l].i() != null && (i & 1 << l) != 0) {
                    nibblearray = achunksection[l].i();
                    System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                    j += nibblearray.a.length;
                }
            }
        }

        // CraftBukkit start - Hackiest hack to have ever hacked.
        // First of all, check to see if we flagged it to send, and all data is "0"
        // This means that it's an "EmptyChunk," HOWEVER... It's not a physical EmptyChunk on the server, there is simply no data present
        if (flag && i == 0xffff && j == 0 && chunkmap.b == 0 && chunkmap.c == 0) {
            chunkmap.b = 1;
            j = 10240;
            java.util.Arrays.fill(abyte, 0, j, (byte) 0);
        }
        // CraftBukkit end

        if (flag) {
            byte[] abyte2 = chunk.m();

            System.arraycopy(abyte2, 0, abyte, j, abyte2.length);
            j += abyte2.length;
        }

        chunkmap.a = new byte[j];
        System.arraycopy(abyte, 0, chunkmap.a, 0, j);
        return chunkmap;
    }
}
