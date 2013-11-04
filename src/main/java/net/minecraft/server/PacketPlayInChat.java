package net.minecraft.server;

import java.io.IOException; // CraftBukkit

public class PacketPlayInChat extends Packet {

    private String message;

    public PacketPlayInChat() {}

    public PacketPlayInChat(String s) {
        if (s.length() > 100) {
            s = s.substring(0, 100);
        }

        this.message = s;
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - added throws
        this.message = packetdataserializer.c(100);
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException { // CraftBukkit - added throws
        packetdataserializer.a(this.message);
    }

    public void a(PacketPlayInListener packetplayinlistener) {
        packetplayinlistener.a(this);
    }

    public String b() {
        return String.format("message=\'%s\'", new Object[] { this.message});
    }

    public String c() {
        return this.message;
    }

    // CraftBukkit start - make chat async
    @Override
    public boolean a() {
        return !this.message.startsWith("/");
    }
    // CraftBukkit end

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayInListener) packetlistener);
    }
}
