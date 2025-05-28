package io.papermc.paper.plugin.lifecycle.event.handler.configuration;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Handler configuration that allows both "monitor" and prioritized handlers.
 * The default priority is 0.
 *
 * @param <O> the required owner type
 */
@ApiStatus.NonExtendable
public interface PrioritizedLifecycleEventHandlerConfiguration<O extends LifecycleEventOwner> extends LifecycleEventHandlerConfiguration<O> {

    /**
     * Sets the priority for this handler. Resets
     * all previous calls to {@link #monitor()}. A
     * lower numeric value correlates to the handler
     * being run earlier.
     *
     * @param priority the numerical priority
     * @return this configuration for chaining
     */
    @Contract("_ -> this")
    PrioritizedLifecycleEventHandlerConfiguration<O> priority(int priority);

    /**
     * Sets this handler configuration to be considered a "monitor".
     * These handlers will run last and should only be used by plugins
     * to observe any changes from previously ran handlers.
     *
     * @return this configuration for chaining
     */
    @Contract("-> this")
    PrioritizedLifecycleEventHandlerConfiguration<O> monitor();

}
