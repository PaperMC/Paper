package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import java.io.IOException; // CraftBukkit

public class Packet24MobSpawn extends Packet {

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public byte f;
    public byte g;
    public byte h;
    private DataWatcher i;
    private List q;

    public Packet24MobSpawn() {}

    public Packet24MobSpawn(EntityLiving entityliving) {
        this.a = entityliving.id;
        this.b = (byte) EntityTypes.a((Entity) entityliving);
        this.c = MathHelper.floor(entityliving.locX * 32.0D);
        this.d = MathHelper.floor(entityliving.locY * 32.0D);
        this.e = MathHelper.floor(entityliving.locZ * 32.0D);
        this.f = (byte) ((int) (entityliving.yaw * 256.0F / 360.0F));
        this.g = (byte) ((int) (entityliving.pitch * 256.0F / 360.0F));
        this.h = (byte) ((int) (entityliving.X * 256.0F / 360.0F));
        this.i = entityliving.getDataWatcher();
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOException
        this.a = datainputstream.readInt();
        this.b = datainputstream.readByte() & 255;
        this.c = datainputstream.readInt();
        this.d = datainputstream.readInt();
        this.e = datainputstream.readInt();
        this.f = datainputstream.readByte();
        this.g = datainputstream.readByte();
        this.h = datainputstream.readByte();
        this.q = DataWatcher.a(datainputstream);
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeByte(this.b & 255);
        dataoutputstream.writeInt(this.c);
        dataoutputstream.writeInt(this.d);
        dataoutputstream.writeInt(this.e);
        dataoutputstream.writeByte(this.f);
        dataoutputstream.writeByte(this.g);
        dataoutputstream.writeByte(this.h);
        this.i.a(dataoutputstream);
    }

    public void handle(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 20;
    }
}
