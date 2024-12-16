package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher {

    public static boolean enabled = true;

    public static void catchOp(String reason) {
        if (AsyncCatcher.enabled && Thread.currentThread() != MinecraftServer.getServer().serverThread) {
            MinecraftServer.LOGGER.error("Thread {} failed main thread check: {}", Thread.currentThread().getName(), reason, new Throwable()); // Paper
            throw new IllegalStateException("Asynchronous " + reason + "!");
        }
    }
}
