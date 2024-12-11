package io.papermc.paper.registry.event.type;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEvent;

public final class RegistryLifecycleEventType<T, E extends RegistryEvent<T>> extends PrioritizableLifecycleEventType.Simple<BootstrapContext, E> {

    public RegistryLifecycleEventType(final RegistryKey<T> registryKey, final String eventName) {
        super(registryKey + " / " + eventName, BootstrapContext.class);
    }

    @Override
    public boolean blocksReloading(final BootstrapContext eventOwner) {
        return false; // only runs once
    }
}
