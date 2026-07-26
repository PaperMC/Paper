package io.papermc.paper.plugin.lifecycle.event;

import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.LifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Manages a plugin's lifecycle events. Can be obtained
 * from {@link org.bukkit.plugin.Plugin} or {@link io.papermc.paper.plugin.bootstrap.BootstrapContext}.
 *
 * @param <O> the owning type, {@link org.bukkit.plugin.Plugin} or {@link io.papermc.paper.plugin.bootstrap.BootstrapContext}
 */
@ApiStatus.NonExtendable
public interface LifecycleEventManager<O extends LifecycleEventOwner> {

    /**
     * Registers an event handler for a specific event type.
     * <p>
     * This is shorthand for creating a new {@link LifecycleEventHandlerConfiguration} and
     * just passing in the {@link LifecycleEventHandler}.
     * <pre>{@code
     * LifecycleEventHandler<RegistrarEvent<Commands>> handler = new Handler();
     * manager.registerEventHandler(LifecycleEvents.COMMANDS, handler);
     * }</pre>
     * is equivalent to
     * <pre>{@code
     * LifecycleEventHandler<RegistrarEvent<Commands>> handler = new Handler();
     * manager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler(handler));
     * }</pre>
     *
     * @param eventType the event type to listen to
     * @param eventHandler the handler for that event
     * @param <E> the type of the event object
     */
    default <E extends LifecycleEvent> void registerEventHandler(final LifecycleEventType<? super O, ? extends E, ?> eventType, final LifecycleEventHandler<? super E> eventHandler) {
        this.registerEventHandler(eventType.newHandler(eventHandler));
    }

    /**
     * Registers an event handler configuration.
     * <p>
     * Configurations are created via {@link LifecycleEventType#newHandler(LifecycleEventHandler)}.
     * Event types may have different configurations options available on the builder-like object
     * returned by {@link LifecycleEventType#newHandler(LifecycleEventHandler)}.
     *
     * @param handlerConfiguration the handler configuration to register
     */
    void registerEventHandler(LifecycleEventHandlerConfiguration<? super O> handlerConfiguration);
}
