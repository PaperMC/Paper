package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an item is dispensed from a block.
 * <p>
 * If this event is cancelled, the block will not dispense the
 * item.
 */
public class BlockDispenseEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ItemStack item;
    private Vector velocity;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockDispenseEvent(@NotNull final Block block, @NotNull final ItemStack item, @NotNull final Vector velocity) {
        super(block);
        this.item = item;
        this.velocity = velocity;
    }

    /**
     * Gets the item that is being dispensed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setItem(ItemStack)} instead.
     *
     * @return An ItemStack for the item being dispensed
     */
    @NotNull
    public ItemStack getItem() {
        return this.item.clone();
    }

    /**
     * Sets the item being dispensed.
     *
     * @param item the item being dispensed
     */
    public void setItem(@NotNull ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the velocity in meters per tick.
     * <p>
     * Note: Modifying the returned Vector will not change the velocity, you
     * must use {@link #setVelocity(Vector)} instead.
     *
     * @return A Vector for the dispensed item's velocity
     */
    @NotNull
    public Vector getVelocity() {
        return this.velocity.clone();
    }

    /**
     * Sets the velocity of the item being dispensed in meters per tick.
     *
     * @param velocity the velocity of the item being dispensed
     */
    public void setVelocity(@NotNull Vector velocity) {
        this.velocity = velocity.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
