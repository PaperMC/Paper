package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * This event is called when a service is registered.
 */
public class ServiceRegisterEvent extends ServiceEvent {
    private static final HandlerList handlers = new HandlerList();

    public ServiceRegisterEvent(RegisteredServiceProvider<?> registeredProvider) {
        super(registeredProvider);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
