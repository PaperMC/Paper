package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;

/**
 * Called when a plugin is enabled.
 */
@SuppressWarnings("serial")
public class PluginEnableEvent extends PluginEvent {
    public PluginEnableEvent(Plugin plugin) {
        super(Type.PLUGIN_ENABLE, plugin);
    }
}
