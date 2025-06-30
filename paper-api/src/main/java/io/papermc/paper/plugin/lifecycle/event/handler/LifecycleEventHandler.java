package io.papermc.paper.plugin.lifecycle.event.handler;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;

/**
 * A handler for a specific event. Can be implemented
 * in a concrete class or as a lambda.
 *
 * @param <E> the event
 */
@FunctionalInterface
public interface LifecycleEventHandler<E extends LifecycleEvent> {

    void run(E event);
}
