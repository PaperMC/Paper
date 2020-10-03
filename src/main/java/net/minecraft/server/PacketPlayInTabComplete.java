package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInTabComplete implements Packet<PacketListenerPlayIn> {

    private int a;
    private String b;

    public PacketPlayInTabComplete() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.i();
        this.b = packetdataserializer.e(32500);
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.d(this.a);
        packetdataserializer.a(this.b, 32500);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public int b() {
        return this.a;
    }

    public String c() {
        return this.b;
    }
}
