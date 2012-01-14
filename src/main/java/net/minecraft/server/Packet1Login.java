package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException; // CraftBukkit

public class Packet1Login extends Packet {

    public int a;
    public String name;
    public long c;
    public WorldType d;
    public int e;
    public byte f;
    public byte g;
    public byte h;
    public byte i;

    public Packet1Login() {}

    public Packet1Login(String s, int i, long j, WorldType worldtype, int k, byte b0, byte b1, byte b2, byte b3) {
        this.name = s;
        this.a = i;
        this.c = j;
        this.d = worldtype;
        this.f = b0;
        this.g = b1;
        this.e = k;
        this.h = b2;
        this.i = b3;
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit
        this.a = datainputstream.readInt();
        this.name = a(datainputstream, 16);
        if (this.a < 23) return; // CraftBukkit
        this.c = datainputstream.readLong();
        String s = a(datainputstream, 16);

        this.d = WorldType.a(s);
        if (this.d == null) {
            this.d = WorldType.NORMAL;
        }

        this.e = datainputstream.readInt();
        this.f = datainputstream.readByte();
        this.g = datainputstream.readByte();
        this.h = datainputstream.readByte();
        this.i = datainputstream.readByte();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit
        dataoutputstream.writeInt(this.a);
        a(this.name, dataoutputstream);
        dataoutputstream.writeLong(this.c);
        if (this.d == null) {
            a("", dataoutputstream);
        } else {
            a(this.d.name(), dataoutputstream);
        }

        dataoutputstream.writeInt(this.e);
        dataoutputstream.writeByte(this.f);
        dataoutputstream.writeByte(this.g);
        dataoutputstream.writeByte(this.h);
        dataoutputstream.writeByte(this.i);
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        int i = 0;

        if (this.d != null) {
            i = this.d.name().length();
        }

        return 4 + this.name.length() + 4 + 7 + 4 + i;
    }
}
