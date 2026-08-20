package io.papermc.paper.event.entity;

import org.bukkit.entity.PufferFish;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Called just before a {@link PufferFish} inflates or deflates.
 */
public interface PufferFishStateChangeEvent extends EntityEventNew, Cancellable {

    @Override
    PufferFish getEntity();

    /**
     * Get the <strong>new</strong> puff state of the {@link PufferFish}.
     * <p>
     * This is what the {@link PufferFish}'s new puff state will be after this event if it isn't cancelled.<br>
     * Refer to {@link PufferFish#getPuffState()} to get the current puff state.
     *
     * @return The <strong>new</strong> puff state, 0 being not inflated, 1 being slightly inflated and 2 being fully inflated
     */
    int getNewPuffState();

    /**
     * Get if the {@link PufferFish} is going to inflate.
     *
     * @return If it's going to inflate
     */
    boolean isInflating();

    /**
     * Get if the {@link PufferFish} is going to deflate.
     *
     * @return If it's going to deflate
     */
    boolean isDeflating();

    /**
     * Set whether to cancel the {@link PufferFish} (in/de)flating.
     *
     * @param cancel {@code true} if you wish to cancel the (in/de)flation
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
