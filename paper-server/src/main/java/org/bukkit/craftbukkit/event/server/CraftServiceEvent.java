package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.server.ServiceEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public abstract class CraftServiceEvent extends CraftServerEvent implements ServiceEvent {

    private final RegisteredServiceProvider<?> provider;

    protected CraftServiceEvent(final RegisteredServiceProvider<?> provider) {
        this.provider = provider;
    }

    @Override
    public RegisteredServiceProvider<?> getProvider() {
        return this.provider;
    }
}
