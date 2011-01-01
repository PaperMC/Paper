
package org.bukkit.craftbukkit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigurationManager;
import net.minecraft.server.WorldServer;
import org.bukkit.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

public final class CraftServer implements Server {
    private final String serverName = "Craftbukkit";
    private final String serverVersion;
    private final PluginManager pluginManager = new SimplePluginManager(this);

    protected final MinecraftServer console;
    protected final ServerConfigurationManager server;

    public CraftServer(MinecraftServer instance, String ver) {
        serverVersion = ver;

        console = instance;
        server = console.f;

        pluginManager.RegisterInterface(JavaPluginLoader.class);

        File pluginFolder = new File("plugins");
        if (pluginFolder.exists()) {
            try {
                pluginManager.loadPlugins(pluginFolder);
            } catch (Throwable ex) {
                Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "(Did you extract the lib folder?)", ex);
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public String getName() {
        return serverName;
    }

    public String getVersion() {
        return serverVersion;
    }

    public Player[] getOnlinePlayers() {
        List<EntityPlayerMP> online = server.b;
        Player[] players = new Player[online.size()];

        for (int i = 0; i < players.length; i++) {
            players[i] = online.get(i).a.getPlayer();
        }

        return players;
    }

    public Player getPlayer(final EntityPlayerMP entity) {
        return entity.a.getPlayer();
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public World[] getWorlds() {
        return new World[] { console.e.getWorld() };
    }

    public ServerConfigurationManager getHandle() {
        return server;
    }
}
