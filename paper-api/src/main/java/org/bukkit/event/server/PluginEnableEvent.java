package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Called when a plugin is enabled.
 */
@SuppressWarnings("serial")
public class PluginEnableEvent extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();
    public PluginEnableEvent(Plugin plugin) {
        super(Type.PLUGIN_ENABLE, plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
