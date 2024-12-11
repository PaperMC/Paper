package io.papermc.paper.plugin.lifecycle.event.types;

import com.google.common.base.Preconditions;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.AbstractLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfigurationImpl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public abstract class PrioritizableLifecycleEventType<
    O extends LifecycleEventOwner,
    E extends LifecycleEvent,
    C extends PrioritizedLifecycleEventHandlerConfiguration<O>
> extends AbstractLifecycleEventType<O, E, C> {

    private static final Comparator<RegisteredHandler<?, ?>> COMPARATOR = Comparator.comparing(handler -> ((PrioritizedLifecycleEventHandlerConfigurationImpl<?, ?>) handler.config()).priority(), (o1, o2) -> {
        if (o1.equals(o2)) {
            return 0;
        } else if (o1.isEmpty()) {
            return 1;
        } else if (o2.isEmpty()) {
            return -1;
        } else {
            return Integer.compare(o1.getAsInt(), o2.getAsInt());
        }
    });

    private final List<RegisteredHandler<O, E>> handlers = new ArrayList<>();

    public PrioritizableLifecycleEventType(final String name, final Class<? extends O> ownerType) {
        super(name, ownerType);
    }

    @Override
    public boolean hasHandlers() {
        return !this.handlers.isEmpty();
    }

    @Override
    protected void register(final O owner, final AbstractLifecycleEventHandlerConfiguration<O, E> config) {
        Preconditions.checkArgument(config instanceof PrioritizedLifecycleEventHandlerConfigurationImpl<?, ?>, "Configuration must be a PrioritizedLifecycleEventHandlerConfiguration");
        this.handlers.add(new RegisteredHandler<>(owner, config));
        this.handlers.sort(COMPARATOR);
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

    public static class Simple<O extends LifecycleEventOwner, E extends LifecycleEvent> extends PrioritizableLifecycleEventType<O, E, PrioritizedLifecycleEventHandlerConfiguration<O>> implements LifecycleEventType.Prioritizable<O, E> {
        public Simple(final String name, final Class<? extends O> ownerType) {
            super(name, ownerType);
        }

        @Override
        public PrioritizedLifecycleEventHandlerConfiguration<O> newHandler(final LifecycleEventHandler<? super E> handler) {
            return new PrioritizedLifecycleEventHandlerConfigurationImpl<>(handler, this);
        }
    }
}
