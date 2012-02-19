package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a block is damaged by a player.
 * <p />
 * If a Block Damage event is cancelled, the block will not be damaged.
 */
public class BlockDamageEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean instaBreak;
    private boolean cancel;
    private final ItemStack itemstack;

    public BlockDamageEvent(final Player player, final Block block, final ItemStack itemInHand, final boolean instaBreak) {
        super(block);
        this.instaBreak = instaBreak;
        this.player = player;
        this.itemstack = itemInHand;
        this.cancel = false;
    }

    /**
     * Gets the player damaging the block involved in this event.
     *
     * @return The player damaging the block involved in this event
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets if the block is set to instantly break when damaged by the player.
     *
     * @return true if the block should instantly break when damaged by the player
     */
    public boolean getInstaBreak() {
        return instaBreak;
    }

    /**
     * Sets if the block should instantly break when damaged by the player.
     *
     * @param bool true if you want the block to instantly break when damaged by the player
     */
    public void setInstaBreak(boolean bool) {
        this.instaBreak = bool;
    }

    /**
     * Gets the ItemStack for the item currently in the player's hand.
     *
     * @return The ItemStack for the item currently in the player's hand
     */
    public ItemStack getItemInHand() {
        return itemstack;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
