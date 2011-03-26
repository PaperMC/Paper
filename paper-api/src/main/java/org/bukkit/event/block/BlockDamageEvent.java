package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

/**
 * @author tkelly
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
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns if the block is set to instantly break
     *
     * @return boolean If the block should instantly break
     */
    public boolean getInstaBreak() {
        return instaBreak;
    }
    
    /**
     * Set if the block should instantly break
     */
    public void setInstaBreak(boolean bool) {
        this.instaBreak = bool;
    }

    /**
     * Returns the ItemStack in hand
     * 
     * @return Currently wielding itemstack
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
}
