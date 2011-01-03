package org.bukkit.fillr;

import org.bukkit.*;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.event.*;

import java.io.File;
import org.bukkit.event.player.PlayerListener;

public class Fillr extends JavaPlugin {
    private FillrListener listener;
    public static String name = "Fillr";
    public static String version = "1.0";
    public static String directory = "plugins";

    public Fillr(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, plugin, cLoader);
        registerEvents();
    }

    public void onDisable() {}
    public void onEnable() {}

    private void registerEvents() {
        listener = new FillrListener();
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, listener, Event.Priority.Normal, this);
    }

    private class FillrListener extends PlayerListener {
        @Override
        public void onPlayerCommand(PlayerChatEvent event) {
            if (!event.isCancelled()) {
                String[] split = event.getMessage().split(" ", 2);

                if (split[0].equalsIgnoreCase("/get")) {
                    if (split.length == 2) {
                        Getter getter = new Getter(getServer());
                        getter.get(split[1], event.getPlayer());
                    } else {
                        event.getPlayer().sendMessage("Usage: /get (Plugin Name)");
                    }

                    event.setCancelled(true);
                }
            }
        }
    }
}
