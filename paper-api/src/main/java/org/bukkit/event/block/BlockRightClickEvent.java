package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

/**
 * Not implemented yet
 */
public class BlockRightClickEvent extends BlockEvent  {
    protected Block clickedBlock;
    protected BlockFace direction;
    protected ItemStack itemInHand;
    protected Player player;

    public BlockRightClickEvent(Type type, Block placedAgainst, BlockFace direction, ItemStack itemInHand, Player thePlayer) {
        super(type, placedAgainst);
        this.clickedBlock = placedAgainst;
        this.direction = direction;
        this.itemInHand = itemInHand;
        this.player = thePlayer;
    }

    /**
     * Gets the player who placed this block
     *
     * @return Player who placed the block
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return BlockFace the direction this block was clicked
     */
    public BlockFace getDirection() {
        return direction;
    }

    /**
     * Returns the item in your hand when you placed the block
     *
     * @return ItemStack the item in your hand when placing the block
     */
    public ItemStack getItemInHand() {
        return itemInHand;
    }
}
