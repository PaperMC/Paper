package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.List;

import java.io.IOException; // CraftBukkit

public class Packet20NamedEntitySpawn extends Packet {

    public int a;
    public String b;
    public int c;
    public int d;
    public int e;
    public byte f;
    public byte g;
    public int h;
    private DataWatcher i;
    private List j;

    public Packet20NamedEntitySpawn() {}

    public Packet20NamedEntitySpawn(EntityHuman entityhuman) {
        this.a = entityhuman.id;

        // CraftBukkit start - Limit name length to 16 characters
        if (entityhuman.name.length() > 16) {
            this.b = entityhuman.name.substring(0, 16);
        } else {
            this.b = entityhuman.name;
        }
        // CraftBukkit end

        this.b = entityhuman.getName();
        this.c = MathHelper.floor(entityhuman.locX * 32.0D);
        this.d = MathHelper.floor(entityhuman.locY * 32.0D);
        this.e = MathHelper.floor(entityhuman.locZ * 32.0D);
        this.f = (byte) ((int) (entityhuman.yaw * 256.0F / 360.0F));
        this.g = (byte) ((int) (entityhuman.pitch * 256.0F / 360.0F));
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        this.h = itemstack == null ? 0 : itemstack.id;
        this.i = entityhuman.getDataWatcher();
    }

    public void a(DataInput datainput) throws IOException { // CraftBukkit
        this.a = datainput.readInt();
        this.b = a(datainput, 16);
        this.c = datainput.readInt();
        this.d = datainput.readInt();
        this.e = datainput.readInt();
        this.f = datainput.readByte();
        this.g = datainput.readByte();
        this.h = datainput.readShort();
        this.j = DataWatcher.a(datainput);
    }

    public void a(DataOutput dataoutput) throws IOException { // CraftBukkit
        dataoutput.writeInt(this.a);
        a(this.b, dataoutput);
        dataoutput.writeInt(this.c);
        dataoutput.writeInt(this.d);
        dataoutput.writeInt(this.e);
        dataoutput.writeByte(this.f);
        dataoutput.writeByte(this.g);
        dataoutput.writeShort(this.h);
        this.i.a(dataoutput);
    }

    public void handle(Connection connection) {
        connection.a(this);
    }

    public int a() {
        return 28;
    }
}
