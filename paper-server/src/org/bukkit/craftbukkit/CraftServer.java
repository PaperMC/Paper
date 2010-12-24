
package org.bukkit.craftbukkit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.*;
import net.minecraft.server.*;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

public final class CraftServer implements Server {
    private final String serverName = "Craftbukkit";
    private final String serverVersion;
    private final HashMap<String, Player> playerCache = new HashMap<String, Player>();
    private final PluginManager pluginManager = new PluginManager(this);

    protected final MinecraftServer console;
    protected final hl server;

    public CraftServer(MinecraftServer instance, String ver) {
        serverVersion = ver;

        console = instance;
        server = console.f;

        pluginManager.RegisterInterface(JavaPluginLoader.class);
        
        try {
            pluginManager.loadPlugin(new File("sample.jar"));
        } catch (InvalidPluginException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "(Did you extract the lib folder?)", ex);
        }
    }

    public String getName() {
        return serverName;
    }

    public String getVersion() {
        return serverVersion;
    }

    public Player[] getOnlinePlayers() {
        List<fi> online = server.b;
        Player[] players = new Player[online.size()];

        for (int i = 0; i < players.length; i++) {
            String name = online.get(i).aw;
            
            if (playerCache.containsKey(name)) {
                players[i] = playerCache.get(name);
            } else {
                players[i] = new CraftPlayer(this, online.get(i));
                playerCache.put(name, players[i]);
            }
        }

        return players;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
