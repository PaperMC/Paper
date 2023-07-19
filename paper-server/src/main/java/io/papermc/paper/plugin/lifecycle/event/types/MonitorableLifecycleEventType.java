package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.AbstractLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.MonitorLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.MonitorLifecycleEventHandlerConfigurationImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class MonitorableLifecycleEventType<O extends LifecycleEventOwner, E extends LifecycleEvent> extends AbstractLifecycleEventType<O, E, MonitorLifecycleEventHandlerConfiguration<O>> implements LifecycleEventType.Monitorable<O, E> {

    final List<RegisteredHandler<O, E>> handlers = new ArrayList<>();
    int nonMonitorIdx = 0;

    public MonitorableLifecycleEventType(final String name, final Class<? extends O> ownerType) {
        super(name, ownerType);
    }

    @Override
    public boolean hasHandlers() {
        return !this.handlers.isEmpty();
    }

    @Override
    public MonitorLifecycleEventHandlerConfigurationImpl<O, E> newHandler(final LifecycleEventHandler<? super E> handler) {
        return new MonitorLifecycleEventHandlerConfigurationImpl<>(handler, this);
    }

    @Override
    protected void register(final O owner, final AbstractLifecycleEventHandlerConfiguration<O, E> config) {
        if (!(config instanceof final MonitorLifecycleEventHandlerConfigurationImpl<?,?> monitor)) {
            throw new IllegalArgumentException("Configuration must be a MonitorLifecycleEventHandlerConfiguration");
        }
        final RegisteredHandler<O, E> registeredHandler = new RegisteredHandler<>(owner, config);
        if (!monitor.isMonitor()) {
            this.handlers.add(this.nonMonitorIdx, registeredHandler);
            this.nonMonitorIdx++;
        } else {
            this.handlers.add(registeredHandler);
        }
    }

    @Override
    public void forEachHandler(final E event, final Consumer<RegisteredHandler<O, E>> consumer, final Predicate<RegisteredHandler<O, E>> predicate) {
        for (final RegisteredHandler<O, E> handler : this.handlers) {
            if (predicate.test(handler)) {
                consumer.accept(handler);
            }
        }
    }

    @Override
    public void removeMatching(final Predicate<RegisteredHandler<O, E>> predicate) {
        this.handlers.removeIf(predicate);
    }
}
