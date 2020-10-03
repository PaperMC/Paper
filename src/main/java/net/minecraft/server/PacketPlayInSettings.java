package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInSettings implements Packet<PacketListenerPlayIn> {

    public String locale;
    public int viewDistance;
    private EnumChatVisibility c;
    private boolean d;
    private int e;
    private EnumMainHand f;

    public PacketPlayInSettings() {}

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.locale = packetdataserializer.e(16);
        this.viewDistance = packetdataserializer.readByte();
        this.c = (EnumChatVisibility) packetdataserializer.a(EnumChatVisibility.class);
        this.d = packetdataserializer.readBoolean();
        this.e = packetdataserializer.readUnsignedByte();
        this.f = (EnumMainHand) packetdataserializer.a(EnumMainHand.class);
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.locale);
        packetdataserializer.writeByte(this.viewDistance);
        packetdataserializer.a((Enum) this.c);
        packetdataserializer.writeBoolean(this.d);
        packetdataserializer.writeByte(this.e);
        packetdataserializer.a((Enum) this.f);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public EnumChatVisibility d() {
        return this.c;
    }

    public boolean e() {
        return this.d;
    }

    public int f() {
        return this.e;
    }

    public EnumMainHand getMainHand() {
        return this.f;
    }
}
