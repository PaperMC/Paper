package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class CraftServiceRegisterEvent extends CraftServiceEvent implements ServiceRegisterEvent {

    public CraftServiceRegisterEvent(final RegisteredServiceProvider<?> provider) {
        super(provider);
    }

    @Override
    public HandlerList getHandlers() {
        return ServiceRegisterEvent.getHandlerList();
    }
}
