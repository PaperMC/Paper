package io.papermc.paper.plugin.lifecycle.event.registrar;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A lifecycle event that exposes a {@link Registrar} of some kind
 * to allow management of various things. Look at implementations of
 * {@link Registrar} for an idea of what uses this event.
 *
 * @param <R> registrar type
 * @see ReloadableRegistrarEvent
 */
@ApiStatus.NonExtendable
public interface RegistrarEvent<R extends Registrar> extends LifecycleEvent {

    /**
     * Get the registrar related to this event.
     *
     * @return the registrar
     */
    @Contract(pure = true)
    R registrar();
}
