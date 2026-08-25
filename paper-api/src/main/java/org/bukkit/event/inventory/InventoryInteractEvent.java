package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;

/**
 * A base class for events that describe an interaction between a
 * HumanEntity and the contents of an Inventory.
 */
public interface InventoryInteractEvent extends InventoryEvent, Cancellable {

    /**
     * Gets the player who performed the click.
     *
     * @return The clicking player.
     */
    default HumanEntity getWhoClicked() { // todo PlayerEvent?
        return this.getView().getPlayer();
    }

    /**
     * Sets the result of this event. This will change whether this
     * event is considered cancelled.
     *
     * @param newResult the new {@link org.bukkit.event.Event.Result} for this event
     * @see #isCancelled()
     */
    void setResult(Result newResult);

    /**
     * Gets the {@link org.bukkit.event.Event.Result} of this event. The Result describes the
     * behavior that will be applied to the inventory in relation to this
     * event.
     *
     * @return the Result of this event.
     */
    Result getResult();

    /**
     * Gets whether this event is cancelled. This is based off of the
     * Result value returned by {@link #getResult()}. {@link Result#ALLOW} and
     * {@link Result#DEFAULT} will result in a returned value of false, but
     * {@link Result#DENY} will result in a returned value of {@code true}.
     * <p>
     * {@inheritDoc}
     *
     * @return whether the event is cancelled
     */
    @Override
    default boolean isCancelled() {
        return this.getResult() == Result.DENY;
    }

    /**
     * Proxy method to {@link #setResult(org.bukkit.event.Event.Result)} for the Cancellable
     * interface. {@link #setResult(org.bukkit.event.Event.Result)} is preferred, as it allows
     * you to specify the Result beyond {@link Result#DENY} and {@link Result#ALLOW}.
     * <p>
     * {@inheritDoc}
     *
     * @param cancel result becomes {@link Result#DENY} if {@code true}, {@link Result#ALLOW} if {@code false}
     */
    @Override
    default void setCancelled(final boolean cancel) {
        this.setResult(cancel ? Result.DENY : Result.ALLOW);
    }
}
