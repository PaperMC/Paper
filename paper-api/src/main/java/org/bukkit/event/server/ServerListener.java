
package org.bukkit.event.server;

import org.bukkit.event.Listener;

/**
 * Handles all miscellaneous server events
 */
public class ServerListener implements Listener {
    /**
     * Called when a plugin is enabled
     *
     * @param event Relevant event details
     */
    public void onPluginEnable(PluginEnableEvent event) {
    }

    /**
     * Called when a plugin is disabled
     *
     * @param event Relevant event details
     */
    public void onPluginDisable(PluginDisableEvent event) {
    }

    /**
     * Called when a server command is used
     *
     * @param event Relevant event details
     */
    public void onServerCommand(ServerCommandEvent event) {
    }
}
