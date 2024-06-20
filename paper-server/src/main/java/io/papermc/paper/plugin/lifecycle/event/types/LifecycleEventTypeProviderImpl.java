package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class LifecycleEventTypeProviderImpl implements LifecycleEventTypeProvider {

    public static LifecycleEventTypeProviderImpl instance() {
        return (LifecycleEventTypeProviderImpl) LifecycleEventTypeProvider.provider();
    }

    private final PaperTagEventTypeProvider provider = new PaperTagEventTypeProvider();

    @Override
    public <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Monitorable<O, E> monitor(final String name, final Class<? extends O> ownerType) {
        return new MonitorableLifecycleEventType<>(name, ownerType);
    }

    @Override
    public <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Prioritizable<O, E> prioritized(final String name, final Class<? extends O> ownerType) {
        return new PrioritizableLifecycleEventType.Simple<>(name, ownerType);
    }

    @Override
    public PaperTagEventTypeProvider tagProvider() {
        return this.provider;
    }
}
