package io.papermc.paper.plugin.lifecycle.event;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.ApiStatus;

/**
 * Base type for all Lifecycle Events.
 * <p>
 * Lifecycle events are generally fired when the older
 * event system is not available, like during early
 * server initialization.
 * @see LifecycleEvents
 * @since 1.20.4
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface LifecycleEvent {
}
