package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class CraftPluginEnableEvent extends CraftPluginEvent implements PluginEnableEvent {

    public CraftPluginEnableEvent(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return PluginEnableEvent.getHandlerList();
    }
}
