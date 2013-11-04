package net.minecraft.server;

import java.io.IOException;

import net.minecraft.util.com.google.common.collect.BiMap;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Packet {

    private static final Logger a = LogManager.getLogger();
    public final long timestamp = System.currentTimeMillis(); // CraftBukkit

    public Packet() {}

    public static Packet a(BiMap bimap, int i) {
        try {
            Class oclass = (Class) bimap.get(Integer.valueOf(i));

            return oclass == null ? null : (Packet) oclass.newInstance();
        } catch (Exception exception) {
            a.error("Couldn\'t create packet " + i, exception);
            return null;
        }
    }

    public static void a(ByteBuf bytebuf, byte[] abyte) {
        bytebuf.writeShort(abyte.length);
        bytebuf.writeBytes(abyte);
    }

    public static byte[] a(ByteBuf bytebuf) throws IOException { // CraftBukkit - added throws
        short short1 = bytebuf.readShort();

        if (short1 < 0) {
            throw new IOException("Key was smaller than nothing!  Weird key!");
        } else {
            byte[] abyte = new byte[short1];

            bytebuf.readBytes(abyte);
            return abyte;
        }
    }

    public abstract void a(PacketDataSerializer packetdataserializer) throws IOException; // CraftBukkit - added throws

    public abstract void b(PacketDataSerializer packetdataserializer) throws IOException; // CraftBukkit - added throws

    public abstract void handle(PacketListener packetlistener);

    public boolean a() {
        return false;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String b() {
        return "";
    }
}
