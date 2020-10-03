package net.minecraft.server;

import java.io.IOException;

public class PacketPlayOutWorldBorder implements Packet<PacketListenerPlayOut> {

    private PacketPlayOutWorldBorder.EnumWorldBorderAction a;
    private int b;
    private double c;
    private double d;
    private double e;
    private double f;
    private long g;
    private int h;
    private int i;

    public PacketPlayOutWorldBorder() {}

    public PacketPlayOutWorldBorder(WorldBorder worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction packetplayoutworldborder_enumworldborderaction) {
        this.a = packetplayoutworldborder_enumworldborderaction;
        this.c = worldborder.getCenterX();
        this.d = worldborder.getCenterZ();
        this.f = worldborder.getSize();
        this.e = worldborder.k();
        this.g = worldborder.j();
        this.b = worldborder.m();
        this.i = worldborder.getWarningDistance();
        this.h = worldborder.getWarningTime();
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = (PacketPlayOutWorldBorder.EnumWorldBorderAction) packetdataserializer.a(PacketPlayOutWorldBorder.EnumWorldBorderAction.class);
        switch (this.a) {
            case SET_SIZE:
                this.e = packetdataserializer.readDouble();
                break;
            case LERP_SIZE:
                this.f = packetdataserializer.readDouble();
                this.e = packetdataserializer.readDouble();
                this.g = packetdataserializer.j();
                break;
            case SET_CENTER:
                this.c = packetdataserializer.readDouble();
                this.d = packetdataserializer.readDouble();
                break;
            case SET_WARNING_BLOCKS:
                this.i = packetdataserializer.i();
                break;
            case SET_WARNING_TIME:
                this.h = packetdataserializer.i();
                break;
            case INITIALIZE:
                this.c = packetdataserializer.readDouble();
                this.d = packetdataserializer.readDouble();
                this.f = packetdataserializer.readDouble();
                this.e = packetdataserializer.readDouble();
                this.g = packetdataserializer.j();
                this.b = packetdataserializer.i();
                this.i = packetdataserializer.i();
                this.h = packetdataserializer.i();
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a((Enum) this.a);
        switch (this.a) {
            case SET_SIZE:
                packetdataserializer.writeDouble(this.e);
                break;
            case LERP_SIZE:
                packetdataserializer.writeDouble(this.f);
                packetdataserializer.writeDouble(this.e);
                packetdataserializer.b(this.g);
                break;
            case SET_CENTER:
                packetdataserializer.writeDouble(this.c);
                packetdataserializer.writeDouble(this.d);
                break;
            case SET_WARNING_BLOCKS:
                packetdataserializer.d(this.i);
                break;
            case SET_WARNING_TIME:
                packetdataserializer.d(this.h);
                break;
            case INITIALIZE:
                packetdataserializer.writeDouble(this.c);
                packetdataserializer.writeDouble(this.d);
                packetdataserializer.writeDouble(this.f);
                packetdataserializer.writeDouble(this.e);
                packetdataserializer.b(this.g);
                packetdataserializer.d(this.b);
                packetdataserializer.d(this.i);
                packetdataserializer.d(this.h);
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    public static enum EnumWorldBorderAction {

        SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS;

        private EnumWorldBorderAction() {}
    }
}
