package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.plugin.Plugin;

public abstract class CraftPluginEvent extends CraftServerEvent implements PluginEvent {

    private final Plugin plugin;

    protected CraftPluginEvent(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }
}
