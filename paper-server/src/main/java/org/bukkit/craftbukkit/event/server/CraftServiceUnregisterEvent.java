package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * This event is called when a service is unregistered.
 * <p>
 * Warning: The order in which register and unregister events are called
 * should not be relied upon.
 */
public class CraftServiceUnregisterEvent extends CraftServiceEvent implements ServiceUnregisterEvent {

    protected CraftServiceUnregisterEvent(final RegisteredServiceProvider<?> provider) {
        super(provider);
    }

    @Override
    public HandlerList getHandlers() {
        return ServiceUnregisterEvent.getHandlerList();
    }
}
