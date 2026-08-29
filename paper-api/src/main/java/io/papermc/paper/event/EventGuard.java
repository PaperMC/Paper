package io.papermc.paper.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class EventGuard {
    private EventGuard() {}

    /**
     * {@return false if there are definitely no listeners associated with this event}
     * The implementation of this method is conservative: it might return true even when no listeners are registered.
     * @param eventClass the type of the event.
     */
    public static boolean hasListeners(Class<? extends Event> eventClass) {
        throw new UnsupportedOperationException("should be replaced");
    }
}
