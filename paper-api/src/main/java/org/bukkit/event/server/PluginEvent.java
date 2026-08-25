package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;

/**
 * Used for plugin enable and disable events
 */
public interface PluginEvent extends ServerEventNew {

    /**
     * Gets the plugin involved in this event
     *
     * @return Plugin for this event
     */
    Plugin getPlugin();
}
