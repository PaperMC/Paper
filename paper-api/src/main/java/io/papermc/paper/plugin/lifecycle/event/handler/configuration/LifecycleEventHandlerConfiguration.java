package io.papermc.paper.plugin.lifecycle.event.handler.configuration;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Base type for constructing configured event handlers for
 * lifecycle events. Usually created via {@link io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType#newHandler(LifecycleEventHandler)}
 * from event types in {@link io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents}
 *
 * @param <O>
 */
@SuppressWarnings("unused")
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface LifecycleEventHandlerConfiguration<O extends LifecycleEventOwner> {
}
