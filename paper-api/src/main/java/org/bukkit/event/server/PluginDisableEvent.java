package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;

public class PluginDisableEvent extends PluginEvent {
    public PluginDisableEvent(Plugin plugin) {
        super(Type.PLUGIN_DISABLE, plugin);
    }
}
