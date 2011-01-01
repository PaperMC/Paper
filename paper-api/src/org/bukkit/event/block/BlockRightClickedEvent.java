/**
 * 
 */
package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.BlockFace;
import org.bukkit.ItemStack;
import org.bukkit.Player;

/**
 * @author durron597
 */
public class BlockRightClickedEvent extends BlockEvent {
    protected Player clicker;
    protected BlockFace direction;
    protected ItemStack clickedWith;

    /**
     * @param type The type of event this is
     * @param theBlock The clicked block
     * @param direction The face we clicked from
     * @param clicker The player who clicked a block
     * @param clickedWith Item in player's hand
     */
    public BlockRightClickedEvent(Type type, Block theBlock, BlockFace direction, Player clicker, ItemStack clickedWith) {
        super(type, theBlock);
        this.direction = direction;
        this.clicker = clicker;
        this.clickedWith = clickedWith;
    }

    /**
     * @return the clicker
     */
    public Player getClicker() {
        return clicker;
    }

    /**
     * @return the direction
     */
    public BlockFace getDirection() {
        return direction;
    }

    /**
     * @return the clickedWith
     */
    public ItemStack getClickedWith() {
        return clickedWith;
    }
}
