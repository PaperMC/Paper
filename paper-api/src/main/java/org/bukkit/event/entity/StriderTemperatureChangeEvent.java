package org.bukkit.event.entity;

import org.bukkit.entity.Strider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Strider}'s temperature has changed as a result of
 * entering or exiting blocks it considers warm.
 *
 * @since 1.16.1
 */
public class StriderTemperatureChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final boolean shivering;
    private boolean cancelled;

    public StriderTemperatureChangeEvent(@NotNull Strider what, boolean shivering) {
        super(what);
        this.shivering = shivering;
    }

    @NotNull
    @Override
    public Strider getEntity() {
        return (Strider) entity;
    }

    /**
     * Get the Strider's new shivering state.
     *
     * @return the new shivering state
     */
    public boolean isShivering() {
        return shivering;
    }

    /**
     * @since 1.17.1
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @since 1.17.1
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
