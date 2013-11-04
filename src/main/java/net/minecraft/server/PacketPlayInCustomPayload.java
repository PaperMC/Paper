package net.minecraft.server;

import java.io.IOException; // CraftBukkit

public class PacketPlayInCustomPayload extends Packet {

    private String tag;
    public int length; // CraftBukkit - private -> public
    private byte[] data;

    public PacketPlayInCustomPayload() {}

    public void a(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - added throws
        this.tag = packetdataserializer.c(20);
        this.length = packetdataserializer.readShort();
        if (this.length > 0 && this.length < 32767) {
            this.data = new byte[this.length];
            packetdataserializer.readBytes(this.data);
        }
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - added throws
        packetdataserializer.a(this.tag);
        packetdataserializer.writeShort((short) this.length);
        if (this.data != null) {
            packetdataserializer.writeBytes(this.data);
        }
    }

    public void a(PacketPlayInListener packetplayinlistener) {
        packetplayinlistener.a(this);
    }

    public String c() {
        return this.tag;
    }

    public byte[] e() {
        return this.data;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayInListener) packetlistener);
    }
}
