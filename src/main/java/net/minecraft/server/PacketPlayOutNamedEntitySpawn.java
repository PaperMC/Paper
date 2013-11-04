package net.minecraft.server;

import java.util.List;

import net.minecraft.util.com.mojang.authlib.GameProfile;

import java.io.IOException; // CraftBukkit

public class PacketPlayOutNamedEntitySpawn extends Packet {

    private int a;
    private GameProfile b;
    private int c;
    private int d;
    private int e;
    private byte f;
    private byte g;
    private int h;
    private DataWatcher i;
    private List j;

    public PacketPlayOutNamedEntitySpawn() {}

    public PacketPlayOutNamedEntitySpawn(EntityHuman entityhuman) {
        this.a = entityhuman.getId();
        this.b = entityhuman.getProfile();
        this.c = MathHelper.floor(entityhuman.locX * 32.0D);
        this.d = MathHelper.floor(entityhuman.locY * 32.0D);
        this.e = MathHelper.floor(entityhuman.locZ * 32.0D);
        this.f = (byte) ((int) (entityhuman.yaw * 256.0F / 360.0F));
        this.g = (byte) ((int) (entityhuman.pitch * 256.0F / 360.0F));
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        this.h = itemstack == null ? 0 : Item.b(itemstack.getItem());
        this.i = entityhuman.getDataWatcher();
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - added throws
        this.a = packetdataserializer.a();
        this.b = new GameProfile(packetdataserializer.c(36), packetdataserializer.c(16));
        this.c = packetdataserializer.readInt();
        this.d = packetdataserializer.readInt();
        this.e = packetdataserializer.readInt();
        this.f = packetdataserializer.readByte();
        this.g = packetdataserializer.readByte();
        this.h = packetdataserializer.readShort();
        this.j = DataWatcher.b(packetdataserializer);
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - added throws
        packetdataserializer.b(this.a);
        packetdataserializer.a(this.b.getId());
        packetdataserializer.a(this.b.getName().length() > 16 ? this.b.getName().substring(0, 16) : this.b.getName()); // CraftBukkit - Limit name length to 16 characters
        packetdataserializer.writeInt(this.c);
        packetdataserializer.writeInt(this.d);
        packetdataserializer.writeInt(this.e);
        packetdataserializer.writeByte(this.f);
        packetdataserializer.writeByte(this.g);
        packetdataserializer.writeShort(this.h);
        this.i.a(packetdataserializer);
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public String b() {
        return String.format("id=%d, gameProfile=\'%s\', x=%.2f, y=%.2f, z=%.2f, carried=%d", new Object[] { Integer.valueOf(this.a), this.b, Float.valueOf((float) this.c / 32.0F), Float.valueOf((float) this.d / 32.0F), Float.valueOf((float) this.e / 32.0F), Integer.valueOf(this.h)});
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
}
