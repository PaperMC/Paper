package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a block is damaged by a player
 */
public class BlockDamageEvent extends BlockEvent implements Cancellable {
    private Player player;
    private boolean instaBreak;
    private boolean cancel;
    private ItemStack itemstack;

    public BlockDamageEvent(Player player, Block block, ItemStack itemInHand, boolean instaBreak) {
        super(Type.BLOCK_DAMAGE, block);
        this.instaBreak = instaBreak;
        this.player = player;
        this.itemstack = itemInHand;
        this.cancel = false;
    }

    /**
     * Returns the player doing the damage
     *
     * @return the player damaging the block
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns if the block is set to instantly break
     *
     * @return true If the block should instantly break
     */
    public boolean getInstaBreak() {
        return instaBreak;
    }

    /**
     * Set if the block should instantly break
     *
     * @param bool If true, the block will instantly break
     */
    public void setInstaBreak(boolean bool) {
        this.instaBreak = bool;
    }

    /**
     * Returns the ItemStack in hand
     *
     * @return the ItemStack for the item currently in hand
     */
    public ItemStack getItemInHand() {
        return itemstack;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a block damage event is cancelled, the block will not be damaged
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a block damage event is cancelled, the block will not be damaged
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
