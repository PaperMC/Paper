package net.minecraft.server;

import java.io.IOException;

public class PacketLoginOutCustomPayload implements Packet<PacketLoginOutListener> {

    private int a;
    private MinecraftKey b;
    private PacketDataSerializer c;

    public PacketLoginOutCustomPayload() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.i();
        this.b = packetdataserializer.p();
        int i = packetdataserializer.readableBytes();

        if (i >= 0 && i <= 1048576) {
            this.c = new PacketDataSerializer(packetdataserializer.readBytes(i));
        } else {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.d(this.a);
        packetdataserializer.a(this.b);
        packetdataserializer.writeBytes(this.c.copy());
    }

    public void a(PacketLoginOutListener packetloginoutlistener) {
        packetloginoutlistener.a(this);
    }
}
