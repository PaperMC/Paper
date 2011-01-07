package org.bukkit.event.player;

import org.bukkit.Block;
import org.bukkit.BlockFace;
import org.bukkit.ItemStack;
import org.bukkit.Player;
import org.bukkit.event.Cancellable;

/**
 * Represents an event that a block was clicked with an item.
 * 
 * Note: while this is the event that is thrown on block placement, there is no
 * BlockPlaced associated with this event. This is because the event is thrown
 * before the block is written to the universe, so the returned block would not
 * be the new placed block. In hMod, BlockPlaced worked by UNDOING the block
 * placement; in Bukkit, we catch the event before it even gets written to the
 * universe, so the concept of a placed block is meaningless.
 * 
 * To get the type of block that's being placed, use the method getItem (for
 * the item in your hand).
 * 
 * @author durron597
 */
public class PlayerBlockItemEvent extends PlayerItemEvent implements Cancellable {
    protected Block blockClicked;
    protected BlockFace direction;
    protected boolean canBuild; 
    
    public PlayerBlockItemEvent(Type type, Player who, ItemStack item, Block blockClicked, BlockFace direction, boolean canBuild) {
        super(type, who, item);
        this.blockClicked = blockClicked;
        this.canBuild = canBuild;
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
        return direction;
    }
    
    /**
     * Gets the value whether the player would be allowed to build here.
     * Defaults to spawn if the server was going to stop them (such as, the
     * player is in Spawn). Note that this is an entirely different check
     * than BLOCK_CANBUILD, as this refers to a player, not universe-physics
     * rule like cactus on dirt.
     * 
     * @return boolean whether the server would allow a player to build here
     */
    public boolean canBuild() {
        return this.canBuild;
    }
    
    /**
     * Sets the canBuild state of this event. Set to true if you want the
     * player to be able to build.  
     */
    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }
}
