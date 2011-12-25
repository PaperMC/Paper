package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;

/**
 * Called when a plugin is disabled.
 */
@SuppressWarnings("serial")
public class PluginDisableEvent extends PluginEvent {
    public PluginDisableEvent(Plugin plugin) {
        super(Type.PLUGIN_DISABLE, plugin);
    }
}
