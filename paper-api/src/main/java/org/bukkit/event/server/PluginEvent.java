package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Used for plugin enable and disable events
 */
public abstract class PluginEvent extends ServerEvent {
    private final Plugin plugin;

    public PluginEvent(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the plugin involved in this event
     *
     * @return Plugin for this event
     */
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }
}
