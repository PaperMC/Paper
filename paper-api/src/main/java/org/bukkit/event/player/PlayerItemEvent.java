package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * 
 * @author durron597
 *
 */
public class PlayerItemEvent extends PlayerEvent implements Cancellable {
    protected ItemStack item;
    protected boolean cancel;
    protected Block blockClicked;
    protected BlockFace blockFace;
    
    public PlayerItemEvent(Type type, Player who, ItemStack item, Block blockClicked, BlockFace blockFace) {
        super(type, who);
        this.item = item;
        cancel = false;
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you
     * want to prevent buckets from placing water and so forth
     * 
     * @return boolean cancellation state
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * Cancelling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    /**
     * Returns the item in hand represented by this event
     * 
     * @return ItemStack the item used
     */
    public ItemStack getItem() {
        return this.item;
    }
    
    /**
     * Convenience method. Returns the material of the item represented by this
     * event
     * 
     * @return Material the material of the item used
     */
    public Material getMaterial() {
        if (this.item == null) return Material.AIR;
        
        return item.getType();
    }
    
    /**
     * Convenience method to inform the user whether this was a block placement
     * event.
     * 
     * @return boolean true if the item in hand was a block
     */
    public boolean isBlock() {
        if (item == null) return false;
        
        return item.getType().isBlock();
    }
    
    /**
     * Returns the clicked block
     * 
     * @return Block returns the block clicked with this item.
     */
    public Block getBlockClicked() {
        return blockClicked;
    }
    
    /**
     * Returns the face of the block that was clicked
     * 
     * @return BlockFace returns the face of the block that was clicked
     */
    public BlockFace getBlockFace() {
        return blockFace;
    }
}
