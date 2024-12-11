package io.papermc.paper.plugin.lifecycle.event.handler.configuration;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class MonitorLifecycleEventHandlerConfigurationImpl<O extends LifecycleEventOwner, E extends LifecycleEvent> extends AbstractLifecycleEventHandlerConfiguration<O, E> implements MonitorLifecycleEventHandlerConfiguration<O> {

    private boolean monitor = false;

    public MonitorLifecycleEventHandlerConfigurationImpl(final LifecycleEventHandler<? super E> handler, final AbstractLifecycleEventType<O, E, ?> eventType) {
        super(handler, eventType);
    }

    public boolean isMonitor() {
        return this.monitor;
    }

    @Override
    public MonitorLifecycleEventHandlerConfiguration<O> monitor() {
        this.monitor = true;
        return this;
    }
}
