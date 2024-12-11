package io.papermc.paper.registry.event.type;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfigurationImpl;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEntryAddEvent;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public class RegistryEntryAddHandlerConfiguration<T, B extends RegistryBuilder<T>> extends PrioritizedLifecycleEventHandlerConfigurationImpl<BootstrapContext, RegistryEntryAddEvent<T, B>> implements RegistryEntryAddConfiguration<T> {

    private @Nullable Predicate<TypedKey<T>> filter;

    public RegistryEntryAddHandlerConfiguration(final LifecycleEventHandler<? super RegistryEntryAddEvent<T, B>> handler, final AbstractLifecycleEventType<BootstrapContext, RegistryEntryAddEvent<T, B>, ?> eventType) {
        super(handler, eventType);
    }

    @Contract(pure = true)
    public @Nullable Predicate<TypedKey<T>> filter() {
        return this.filter;
    }

    @Override
    public RegistryEntryAddConfiguration<T> filter(final Predicate<TypedKey<T>> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public RegistryEntryAddConfiguration<T> priority(final int priority) {
        return (RegistryEntryAddConfiguration<T>) super.priority(priority);
    }

    @Override
    public RegistryEntryAddConfiguration<T> monitor() {
        return (RegistryEntryAddConfiguration<T>) super.monitor();
    }
}
