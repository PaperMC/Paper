package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a plugin is disabled.
 *
 * @since 1.0.0 R1
 */
public class PluginDisableEvent extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();

    public PluginDisableEvent(@NotNull final Plugin plugin) {
        super(plugin);
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
