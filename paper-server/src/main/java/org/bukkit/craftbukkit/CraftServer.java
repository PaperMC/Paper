package org.bukkit.craftbukkit;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerConfigurationManager;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

public final class CraftServer implements Server {
    private final String serverName = "Craftbukkit";
    private final String serverVersion = "1.1";
    private final PluginManager pluginManager = new SimplePluginManager(this);

    protected final MinecraftServer console;
    protected final ServerConfigurationManager server;

    public CraftServer(MinecraftServer console, ServerConfigurationManager server) {
        this.console = console;
        this.server = server;

        pluginManager.RegisterInterface(JavaPluginLoader.class);

        File pluginFolder = (File)console.options.valueOf("plugins");

        if (pluginFolder.exists()) {
            try {
                Plugin[] plugins = plugins = pluginManager.loadPlugins(pluginFolder);

                for (Plugin plugin : plugins) {
                    pluginManager.enablePlugin(plugin);
                }
            } catch (Throwable ex) {
                Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "(Is it up to date?)", ex);
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

    public Player getPlayer(final String name) {
        Player[] players = getOnlinePlayers();

        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
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
