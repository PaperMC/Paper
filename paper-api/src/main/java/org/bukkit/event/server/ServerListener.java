package org.bukkit.event.server;

import org.bukkit.event.Listener;

/**
 * Handles all miscellaneous server events
 */
@Deprecated
public class ServerListener implements Listener {

    /**
     * Called when a plugin is enabled
     *
     * @param event Relevant event details
     */
    public void onPluginEnable(PluginEnableEvent event) {}

    /**
     * Called when a plugin is disabled
     *
     * @param event Relevant event details
     */
    public void onPluginDisable(PluginDisableEvent event) {}

    /**
     * Called when a server command is used
     *
     * @param event Relevant event details
     */
    public void onServerCommand(ServerCommandEvent event) {}

    /**
     * Called when a map item is initialized (created or loaded into memory)
     *
     * @param event Relevant event details
     */
    public void onMapInitialize(MapInitializeEvent event) {}

    /**
     * Called when a server list ping has come in.
     *
     * @param event Relevant event details
     */
    public void onServerListPing(ServerListPingEvent event) {}
}
