package io.papermc.paper.event.entity;

import org.bukkit.entity.PufferFish;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called just before a {@link PufferFish} inflates or deflates.
 */
@NullMarked
public class PufferFishStateChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int newPuffState;
    private boolean cancelled;

    @ApiStatus.Internal
    public PufferFishStateChangeEvent(final PufferFish entity, final int newPuffState) {
        super(entity);
        this.newPuffState = newPuffState;
    }

    @Override
    public PufferFish getEntity() {
        return (PufferFish) super.getEntity();
    }

    /**
     * Get the <strong>new</strong> puff state of the {@link PufferFish}.
     * <p>
     * This is what the {@link PufferFish}'s new puff state will be after this event if it isn't cancelled.<br>
     * Refer to {@link PufferFish#getPuffState()} to get the current puff state.
     *
     * @return The <strong>new</strong> puff state, 0 being not inflated, 1 being slightly inflated and 2 being fully inflated
     */
    public int getNewPuffState() {
        return this.newPuffState;
    }

    /**
     * Get if the {@link PufferFish} is going to inflate.
     *
     * @return If it's going to inflate
     */
    public boolean isInflating() {
        return this.newPuffState > this.getEntity().getPuffState();
    }

    /**
     * Get if the {@link PufferFish} is going to deflate.
     *
     * @return If it's going to deflate
     */
    public boolean isDeflating() {
        return this.newPuffState < this.getEntity().getPuffState();
    }

    /**
     * Set whether to cancel the {@link PufferFish} (in/de)flating.
     *
     * @param cancel {@code true} if you wish to cancel the (in/de)flation
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
