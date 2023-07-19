package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.AbstractLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.LifecycleEventHandlerConfiguration;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public abstract class AbstractLifecycleEventType<O extends LifecycleEventOwner, E extends LifecycleEvent, C extends LifecycleEventHandlerConfiguration<O>> implements LifecycleEventType<O, E, C> {

    private final String name;
    private final Class<? extends O> ownerType;

    protected AbstractLifecycleEventType(final String name, final Class<? extends O> ownerType) {
        this.name = name;
        this.ownerType = ownerType;
        LifecycleEventRunner.INSTANCE.addEventType(this);
    }

    @Override
    public String name() {
        return this.name;
    }

    private void verifyOwner(final O owner) {
        if (!this.ownerType.isInstance(owner)) {
            throw new IllegalArgumentException("You cannot register the lifecycle event '" + this.name + "' on " + owner);
        }
    }

    public boolean blocksReloading(final O eventOwner) {
        return eventOwner instanceof BootstrapContext;
    }

    public abstract boolean hasHandlers();

    public abstract void forEachHandler(E event, Consumer<RegisteredHandler<O, E>> consumer, Predicate<RegisteredHandler<O, E>> predicate);

    public abstract void removeMatching(Predicate<RegisteredHandler<O, E>> predicate);

    protected abstract void register(O owner, AbstractLifecycleEventHandlerConfiguration<O, E> config);

    public final void tryRegister(final O owner, final AbstractLifecycleEventHandlerConfiguration<O, E> config) {
        this.verifyOwner(owner);
        LifecycleEventRunner.INSTANCE.checkRegisteredHandler(owner, this);
        this.register(owner, config);
    }

    public record RegisteredHandler<O extends LifecycleEventOwner, E extends LifecycleEvent>(O owner, AbstractLifecycleEventHandlerConfiguration<O, E> config) {

        public LifecycleEventHandler<? super E> lifecycleEventHandler() {
            return this.config().handler();
        }
    }
}
