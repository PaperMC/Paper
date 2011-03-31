
package org.bukkit.event.server;

import org.bukkit.event.Listener;
import org.bukkit.plugin.AuthorNagException;

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
        onPluginEnable((PluginEvent)event);
        throw new AuthorNagException("onPluginEnable has been replaced with a new signature, (PluginEnableEvent)");
    }

    /**
     * Called when a plugin is disabled
     *
     * @param event Relevant event details
     */
    public void onPluginDisable(PluginDisableEvent event) {
        onPluginDisable((PluginEvent)event);
        throw new AuthorNagException("onPluginDisable has been replaced with a new signature, (PluginDisableEvent)");
    }

    /**
     * Called when a server command is used
     *
     * @param event Relevant event details
     */
    public void onServerCommand(ServerCommandEvent event) {
    }

    //  TODO: Remove after RB
    @Deprecated public void onPluginDisable(PluginEvent event) {}
    @Deprecated public void onPluginEnable(PluginEvent event) {}
}
