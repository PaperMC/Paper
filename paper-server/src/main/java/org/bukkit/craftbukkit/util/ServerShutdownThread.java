package org.bukkit.craftbukkit.util;

import ca.spottedleaf.moonrise.common.util.TickThread;
import io.papermc.paper.log.LoggerShutdown;
import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends TickThread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        super("Server Shutdown Thread");
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Paper start - try to shutdown on main
            server.halt(false, false);

            // Give the server at least a second to realize that it needs to stop, if it takes longer that means it could be stuck.
            // If the server hasn't finished starting up yet, give it as much time as it needs to start up and close on its own. This is
            // assuming that as long as the server thread is running, it will eventually successfully init & isn't deadlocked.
            for (int i = 1000; i > 0 && !server.hasStopped() || (!server.hasStarted && server.getRunningThread().isAlive()); i -= 100) {
                Thread.sleep(100);
            }
            if (server.hasStopped()) {
                while (!server.hasFullyShutdown) Thread.sleep(1000);
                return;
            }
            // Looks stalled, close async
            server.forceTicks = true;
            this.server.close();
            while (!server.hasFullyShutdown) Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Paper end
        } finally {
            LoggerShutdown.shutdownLogging();
        }
    }
}
