package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException; // CraftBukkit

public class Packet2Handshake extends Packet {

    private int a;
    private String b;
    public String c; // CraftBukkit private -> public
    public int d; // CraftBukkit private -> public

    public Packet2Handshake() {}

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOException
        this.a = datainputstream.readByte();
        this.b = a(datainputstream, 16);
        this.c = a(datainputstream, 255);
        this.d = datainputstream.readInt();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeByte(this.a);
        a(this.b, dataoutputstream);
        a(this.c, dataoutputstream);
        dataoutputstream.writeInt(this.d);
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 3 + 2 * this.b.length();
    }

    public int d() {
        return this.a;
    }

    public String f() {
        return this.b;
    }
}
