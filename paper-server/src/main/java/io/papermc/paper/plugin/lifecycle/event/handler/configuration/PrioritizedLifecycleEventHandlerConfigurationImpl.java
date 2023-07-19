package io.papermc.paper.plugin.lifecycle.event.handler.configuration;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import java.util.OptionalInt;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PrioritizedLifecycleEventHandlerConfigurationImpl<O extends LifecycleEventOwner, E extends LifecycleEvent>
    extends AbstractLifecycleEventHandlerConfiguration<O, E>
    implements PrioritizedLifecycleEventHandlerConfiguration<O> {

    private static final OptionalInt DEFAULT_PRIORITY = OptionalInt.of(0);
    private static final OptionalInt MONITOR_PRIORITY = OptionalInt.empty();

    private OptionalInt priority = DEFAULT_PRIORITY;

    public PrioritizedLifecycleEventHandlerConfigurationImpl(final LifecycleEventHandler<? super E> handler, final AbstractLifecycleEventType<O, E, ?> eventType) {
        super(handler, eventType);
    }

    public OptionalInt priority() {
        return this.priority;
    }

    @Override
    public PrioritizedLifecycleEventHandlerConfiguration<O> priority(final int priority) {
        this.priority = OptionalInt.of(priority);
        return this;
    }

    @Override
    public PrioritizedLifecycleEventHandlerConfiguration<O> monitor() {
        this.priority = MONITOR_PRIORITY;
        return this;
    }
}
