package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Shelf;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Fired when a Shelf is about to pull/swap a specific hotbar slot from a player
 * (including when the Shelf is part of a redstone-powered side-chain).
 *
 * <p>Cancelling this event prevents only this slot transfer; the rest of the
 * Shelf interaction/chain continues unaffected.</p>
 */
@NullMarked
public class PlayerShelfHotbarSwapEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private final int hotbarSlot;
    private ItemStack itemStack;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerShelfHotbarSwapEvent(final Player player, final Block block, final int hotbarSlot, final ItemStack itemStack) {
        super(player);
        this.block = block;
        this.hotbarSlot = hotbarSlot;
        this.itemStack = itemStack;
    }

    /**
     * Gets the block of the shelf involved in this event.
     *
     * @return the shelf block
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * Returns the shelf block state involved in this event.
     * This constructs a new snapshot {@link BlockState} and validates its type.
     *
     * @return a shelf state snapshot
     * @throws IllegalStateException if the block is no longer a shelf
     */
    public Shelf getShelf() {
        final BlockState state = this.block.getState();
        Preconditions.checkState(state instanceof Shelf, "Block state is no longer a Shelf block state!");
        return (Shelf) state;
    }

    /**
     * Gets the hotbar slot being pulled.
     * Index is 0–8 inclusive.
     *
     * @return the hotbar slot index
     */
    public int getHotbarSlot() {
        return this.hotbarSlot;
    }

    /**
     * Gets the {@link ItemStack} the shelf is about to pull from the player's hotbar.
     *
     * @return the item being pulled
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Sets the {@link ItemStack} that will be inserted into the shelf for this transfer.
     * If {@code null} is provided, the ItemStack will become air and the transfer will insert nothing.
     * The original item will be consumed from the player's slot.
     *
     * @param itemStack the replacement item
     */
    public void setItemStack(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack == null ? ItemStack.empty() : itemStack;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
