package io.papermc.paper.plugin.lifecycle.event;

import com.google.common.base.Preconditions;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.AbstractLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.LifecycleEventHandlerConfiguration;
import java.util.function.BooleanSupplier;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class PaperLifecycleEventManager<O extends LifecycleEventOwner> implements LifecycleEventManager<O> {

    private final O owner;
    public final BooleanSupplier registrationCheck;

    public PaperLifecycleEventManager(final O owner, final BooleanSupplier registrationCheck) {
        this.owner = owner;
        this.registrationCheck = registrationCheck;
    }

    @Override
    public void registerEventHandler(final LifecycleEventHandlerConfiguration<? super O> handlerConfiguration) {
        Preconditions.checkState(this.registrationCheck.getAsBoolean(), "Cannot register lifecycle event handlers");
        ((AbstractLifecycleEventHandlerConfiguration<? super O, ?>) handlerConfiguration).registerFrom(this.owner);
    }
}
