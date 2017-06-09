package org.bukkit.craftbukkit.util;

import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            org.spigotmc.AsyncCatcher.enabled = false; // Spigot
            this.server.close();
        } finally {
            try {
                net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // Paper - Use TerminalConsoleAppender
            } catch (Exception e) {
            }
        }
    }
}
