package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a service is registered.
 * <p>
 * Warning: The order in which register and unregister events are called
 * should not be relied upon.
 *
 * @since 1.1.0 R5
 */
public class ServiceRegisterEvent extends ServiceEvent {
    private static final HandlerList handlers = new HandlerList();

    public ServiceRegisterEvent(@NotNull RegisteredServiceProvider<?> registeredProvider) {
        super(registeredProvider);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
