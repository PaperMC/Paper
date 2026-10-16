package org.bukkit.event.entity;

import org.bukkit.entity.Strider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Strider}'s temperature has changed as a result of
 * entering or exiting blocks it considers warm.
 */
public class StriderTemperatureChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean shivering;
    private boolean cancelled;

    @ApiStatus.Internal
    public StriderTemperatureChangeEvent(@NotNull Strider strider, boolean shivering) {
        super(strider);
        this.shivering = shivering;
    }

    @NotNull
    @Override
    public Strider getEntity() {
        return (Strider) this.entity;
    }

    /**
     * Get the Strider's new shivering state.
     *
     * @return the new shivering state
     */
    public boolean isShivering() {
        return this.shivering;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
