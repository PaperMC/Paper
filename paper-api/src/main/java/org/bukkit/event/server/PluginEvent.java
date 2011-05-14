package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;

/**
 * Used for plugin loaded and unloaded events
 */
public class PluginEvent extends ServerEvent {
    private final Plugin plugin;

    public PluginEvent(final Type type, final Plugin plugin) {
        super(type);

        this.plugin = plugin;
    }

    /**
     * Gets the plugin involved in this event
     *
     * @return Plugin for this event
     */
    public Plugin getPlugin() {
        return plugin;
    }
}
