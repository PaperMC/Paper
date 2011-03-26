package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

/**
 * Not implemented yet
 */
public class BlockPlaceEvent extends BlockEvent implements Cancellable {
    protected boolean cancel;
    protected boolean canBuild;
    protected Block placedAgainst;
    protected BlockState replacedBlockState;
    protected ItemStack itemInHand;
    protected Player player;

    public BlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, Player thePlayer, boolean canBuild) {
        super(Type.BLOCK_PLACE, placedBlock);
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.player = thePlayer;
        this.replacedBlockState = replacedBlockState;
        this.canBuild = canBuild;
        cancel = false;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
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
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
     * Clarity method for getting the placed block. Not really needed
     * except for reasons of clarity
     *
     * @return Block the block that was placed
     */
    public Block getBlockPlaced() {
        return getBlock();
    }

    /**
     * Returns the state of the block which was replaced. Material type air mostly.
     *
     * @return BlockState of block which was replaced.
     */
    public BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }


    /**
     * Get the block that this block was placed against
     *
     * @return Block the block that the new block was placed against
     */
    public Block getBlockAgainst() {
        return placedAgainst;
    }

    /**
     * Returns the item in your hand when you placed the block
     *
     * @return ItemStack the item in your hand when placing the block
     */
    public ItemStack getItemInHand() {
        return itemInHand;
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
