package org.bukkit.fillr;

import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

import java.io.File;

public class Fillr extends JavaPlugin {
    private FillrListener listener;
    public static final String NAME = "Fillr";
    public static final String VERSION = "1.0";
    public static final String DIRECTORY = "plugins";

    public Fillr(PluginLoader pluginLoader, Server instance,
            PluginDescriptionFile desc, File folder, File plugin,
            ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }

    public void onDisable() {
    }

    public void onEnable() {
        registerEvents();
    }

    private void registerEvents() {
        listener = new FillrListener(getServer());
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, listener, Event.Priority.Normal, this);
    }

    public void onCommand(Player player, String command, String[] args) {
        // TODO Auto-generated method stub
    }
}
