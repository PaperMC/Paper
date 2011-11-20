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
    public int e;
    public int f;
    public byte[] g;
    public int h; // CraftBukkit - private -> public
    public byte[] rawData; // CraftBukkit

    public Packet51MapChunk() {
        this.l = true;
    }

    // CraftBukkit start
    public Packet51MapChunk(int i, int j, int k, int l, int i1, int j1, World world) {
        this(i, j, k, l, i1, j1, world.getMultiChunkData(i, j, k, l, i1, j1));
    }

    public Packet51MapChunk(int i, int j, int k, int l, int i1, int j1, byte[] data) {
        // CraftBukkit end
        this.l = true;
        this.a = i;
        this.b = j;
        this.c = k;
        this.d = l;
        this.e = i1;
        this.f = j1;
        /* CraftBukkit - Moved compression into its own method.
        byte[] abyte = data; // CraftBukkit - uses data from above constructor
        Deflater deflater = new Deflater(-1);

        try {
            deflater.setInput(abyte);
            deflater.finish();
            this.g = new byte[l * i1 * j1 * 5 / 2];
            this.h = deflater.deflate(this.g);
        } finally {
            deflater.end();
        }*/
        this.rawData = data; // CraftBukkit
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOEXception
        this.a = datainputstream.readInt();
        this.b = datainputstream.readShort();
        this.c = datainputstream.readInt();
        this.d = datainputstream.read() + 1;
        this.e = datainputstream.read() + 1;
        this.f = datainputstream.read() + 1;
        this.h = datainputstream.readInt();
        byte[] abyte = new byte[this.h];

        datainputstream.readFully(abyte);
        this.g = new byte[this.d * this.e * this.f * 5 / 2];
        Inflater inflater = new Inflater();

        inflater.setInput(abyte);

        try {
            inflater.inflate(this.g);
        } catch (DataFormatException dataformatexception) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeShort(this.b);
        dataoutputstream.writeInt(this.c);
        dataoutputstream.write(this.d - 1);
        dataoutputstream.write(this.e - 1);
        dataoutputstream.write(this.f - 1);
        dataoutputstream.writeInt(this.h);
        dataoutputstream.write(this.g, 0, this.h);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 17 + this.h;
    }
}
