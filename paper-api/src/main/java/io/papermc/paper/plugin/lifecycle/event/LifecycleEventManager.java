package io.papermc.paper.plugin.lifecycle.event;

import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.LifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.jetbrains.annotations.ApiStatus;

/// Manages a plugin's lifecycle events. Can be obtained
/// from [org.bukkit.plugin.Plugin] or [io.papermc.paper.plugin.bootstrap.BootstrapContext].
///
/// @param <O> the owning type, [org.bukkit.plugin.Plugin] or [io.papermc.paper.plugin.bootstrap.BootstrapContext]
@ApiStatus.NonExtendable
public interface LifecycleEventManager<O extends LifecycleEventOwner> {

    /// Registers an event handler for a specific event type.
    ///
    /// This is shorthand for creating a new [LifecycleEventHandlerConfiguration] and
    /// just passing in the [LifecycleEventHandler].
    ///
    /// ```
    /// LifecycleEventHandler<RegistrarEvent<Commands>> handler = new Handler();
    /// manager.registerEventHandler(LifecycleEvents.COMMANDS, handler);
    /// ```
    ///
    /// is equivalent to
    /// ```
    /// LifecycleEventHandler<RegistrarEvent<Commands>> handler = new Handler();
    /// manager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler(handler));
    /// ```
    ///
    /// @param eventType the event type to listen to
    /// @param eventHandler the handler for that event
    /// @param <E> the type of the event object
    default <E extends LifecycleEvent> void registerEventHandler(final LifecycleEventType<? super O, ? extends E, ?> eventType, final LifecycleEventHandler<? super E> eventHandler) {
        this.registerEventHandler(eventType.newHandler(eventHandler));
    }

    /// Registers an event handler configuration.
    ///
    /// Configurations are created via [LifecycleEventType#newHandler(LifecycleEventHandler)].
    /// Event types may have different configurations options available on the builder-like object
    /// returned by [LifecycleEventType#newHandler(LifecycleEventHandler)].
    ///
    /// @param handlerConfiguration the handler configuration to register
    void registerEventHandler(LifecycleEventHandlerConfiguration<? super O> handlerConfiguration);
}
