package net.minecraft.server;

import java.io.IOException;
import javax.annotation.Nullable;

public class PacketPlayOutTitle implements Packet<PacketListenerPlayOut> {

    private PacketPlayOutTitle.EnumTitleAction a;
    private IChatBaseComponent b;
    private int c;
    private int d;
    private int e;

    public PacketPlayOutTitle() {}

    public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction, IChatBaseComponent ichatbasecomponent) {
        this(packetplayouttitle_enumtitleaction, ichatbasecomponent, -1, -1, -1);
    }

    public PacketPlayOutTitle(int i, int j, int k) {
        this(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent) null, i, j, k);
    }

    public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction packetplayouttitle_enumtitleaction, @Nullable IChatBaseComponent ichatbasecomponent, int i, int j, int k) {
        this.a = packetplayouttitle_enumtitleaction;
        this.b = ichatbasecomponent;
        this.c = i;
        this.d = j;
        this.e = k;
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = (PacketPlayOutTitle.EnumTitleAction) packetdataserializer.a(PacketPlayOutTitle.EnumTitleAction.class);
        if (this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE || this.a == PacketPlayOutTitle.EnumTitleAction.ACTIONBAR) {
            this.b = packetdataserializer.h();
        }

        if (this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
            this.c = packetdataserializer.readInt();
            this.d = packetdataserializer.readInt();
            this.e = packetdataserializer.readInt();
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a((Enum) this.a);
        if (this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE || this.a == PacketPlayOutTitle.EnumTitleAction.ACTIONBAR) {
            packetdataserializer.a(this.b);
        }

        if (this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
            packetdataserializer.writeInt(this.c);
            packetdataserializer.writeInt(this.d);
            packetdataserializer.writeInt(this.e);
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    public static enum EnumTitleAction {

        TITLE, SUBTITLE, ACTIONBAR, TIMES, CLEAR, RESET;

        private EnumTitleAction() {}
    }
}
