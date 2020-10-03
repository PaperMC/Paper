package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerConnectionUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    public static <T extends PacketListener> void ensureMainThread(Packet<T> packet, T t0, WorldServer worldserver) throws CancelledPacketHandleException {
        ensureMainThread(packet, t0, (IAsyncTaskHandler) worldserver.getMinecraftServer());
    }

    public static <T extends PacketListener> void ensureMainThread(Packet<T> packet, T t0, IAsyncTaskHandler<?> iasynctaskhandler) throws CancelledPacketHandleException {
        if (!iasynctaskhandler.isMainThread()) {
            iasynctaskhandler.execute(() -> {
                if (MinecraftServer.getServer().hasStopped() || (t0 instanceof PlayerConnection && ((PlayerConnection) t0).processedDisconnect)) return; // CraftBukkit, MC-142590
                if (t0.a().isConnected()) {
                    packet.a(t0);
                } else {
                    PlayerConnectionUtils.LOGGER.debug("Ignoring packet due to disconnection: " + packet);
                }

            });
            throw CancelledPacketHandleException.INSTANCE;
        }
        // CraftBukkit start - SPIGOT-5477, MC-142590
        else if (MinecraftServer.getServer().hasStopped() || (t0 instanceof PlayerConnection && ((PlayerConnection) t0).processedDisconnect)) {
            throw CancelledPacketHandleException.INSTANCE;
        }
        // CraftBukkit end
    }
}
