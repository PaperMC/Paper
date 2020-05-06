package net.minecraft.server;

import io.netty.channel.ChannelFuture; // Paper
import java.io.IOException;

public interface Packet<T extends PacketListener> {

    void a(PacketDataSerializer packetdataserializer) throws IOException;

    void b(PacketDataSerializer packetdataserializer) throws IOException;

    void a(T t0);

    // Paper start

    /**
     * @param player Null if not at PLAY stage yet
     */
    default void onPacketDispatch(@javax.annotation.Nullable EntityPlayer player) {}

    /**
     * @param player Null if not at PLAY stage yet
     * @param future Can be null if packet was cancelled
     */
    default void onPacketDispatchFinish(@javax.annotation.Nullable EntityPlayer player, @javax.annotation.Nullable ChannelFuture future) {}
    default boolean hasFinishListener() { return false; }
    default boolean isReady() { return true; }
    default java.util.List<Packet> getExtraPackets() { return null; }
    default boolean packetTooLarge(NetworkManager manager) {
        return false;
    }
    // Paper end

    default boolean a() {
        return false;
    }
}
