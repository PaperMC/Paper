
package org.bukkit.craftbukkit;

import net.minecraft.server.MinecraftServer;

public class Main {
    public static void main(String[] args) {
        // Todo: Installation script

        try {
            MinecraftServer.main(args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
