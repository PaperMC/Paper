package io.papermc.paper.plugin.lifecycle.event.registrar;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A lifecycle event that exposes a {@link Registrar} that is
 * reloadable.
 *
 * @param <R> the registrar type
 * @see RegistrarEvent
 */
@ApiStatus.NonExtendable
public interface ReloadableRegistrarEvent<R extends Registrar> extends RegistrarEvent<R> {

    /**
     * Get the cause of this reload.
     *
     * @return the cause
     */
    @Contract(pure = true)
    Cause cause();

    enum Cause {
        /**
         * The initial load of the server.
         */
        INITIAL,
        /**
         * A reload, triggered via one of the various mechanisms like
         * the bukkit or minecraft reload commands.
         */
        RELOAD
    }
}
