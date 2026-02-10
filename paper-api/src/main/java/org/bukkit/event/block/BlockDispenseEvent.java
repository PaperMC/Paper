package org.bukkit.event.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Called when an item is dispensed from a block.
 * <p>
 * If this event is cancelled, the block will not dispense the
 * item.
 */
public interface BlockDispenseEvent extends BlockEvent, Cancellable {

    /**
     * Gets the item that is being dispensed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setItem(ItemStack)} instead.
     *
     * @return An ItemStack for the item being dispensed
     */
    ItemStack getItem();

    /**
     * Sets the item being dispensed.
     *
     * @param item the item being dispensed
     */
    void setItem(ItemStack item);

    /**
     * Gets the velocity in meters per tick.
     * <p>
     * Note: Modifying the returned Vector will not change the velocity, you
     * must use {@link #setVelocity(Vector)} instead.
     *
     * @return A Vector for the dispensed item's velocity
     */
    Vector getVelocity();

    /**
     * Sets the velocity of the item being dispensed in meters per tick.
     *
     * @param velocity the velocity of the item being dispensed
     */
    void setVelocity(Vector velocity);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
