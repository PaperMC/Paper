package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Event called on dispense of an item from a block.
 */
public class BlockDispenseEvent extends BlockEvent implements Cancellable {

    private boolean cancelled = false;
    private ItemStack item;
    private Vector velocity;

    public BlockDispenseEvent(Block block, ItemStack dispensed, Vector velocity) {
        super(Type.BLOCK_DISPENSE, block);
        this.item = dispensed;
        this.velocity = velocity;
    }

    /**
     * Get the item that is being dispensed. Modifying the returned item
     * will have no effect.
     *
     * @return an ItemStack for the item being dispensed
     */
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Set the item being dispensed.
     *
     * @param item
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the velocity. Modifying the returned Vector will not
     * change the velocity.
     *
     * @return a Vector for the dispensed item's velocity
     */
    public Vector getVelocity() {
        return velocity.clone();
    }

    /**
     * Set the velocity.
     *
     * @param vel
     */
    public void setVelocity(Vector vel) {
        velocity = vel;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
