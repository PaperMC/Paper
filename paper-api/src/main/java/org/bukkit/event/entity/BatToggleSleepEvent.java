package org.bukkit.event.entity;

import org.bukkit.entity.Bat;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a bat attempts to sleep or wake up from its slumber.
 * <p>
 * If this event is cancelled, the Bat will not toggle its sleep
 * state.
 */
public class BatToggleSleepEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean awake;

    private boolean cancelled;

    @ApiStatus.Internal
    public BatToggleSleepEvent(@NotNull Bat bat, boolean awake) {
        super(bat);
        this.awake = awake;
    }

    /**
     * Get whether the bat is attempting to awaken.
     *
     * @return {@code true} if trying to awaken, {@code false} otherwise
     */
    public boolean isAwake() {
        return this.awake;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
