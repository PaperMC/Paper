package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class CraftPluginDisableEvent extends CraftPluginEvent implements PluginDisableEvent {

    public CraftPluginDisableEvent(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return PluginDisableEvent.getHandlerList();
    }
}
