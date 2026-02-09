package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a block is placed by a player.
 * <p>
 * If this event is cancelled, the block will not be placed.
 */
public interface BlockPlaceEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the block that this block was placed against
     *
     * @return Block the block that the new block was placed against
     */
    Block getBlockAgainst();

    /**
     * Gets the item in the player's hand when they placed the block.
     *
     * @return The ItemStack for the item in the player's hand when they
     *     placed the block
     */
    ItemStack getItemInHand();

    /**
     * Gets the player who placed the block involved in this event.
     *
     * @return The Player who placed the block involved in this event
     */
    Player getPlayer();

    /**
     * Clarity method for getting the placed block. Not really needed except
     * for reasons of clarity.
     *
     * @return The Block that was placed
     */
    Block getBlockPlaced();

    /**
     * Gets the BlockState for the block which was replaced. Material type air
     * mostly.
     *
     * @return The BlockState for the block which was replaced.
     */
    BlockState getBlockReplacedState();

    /**
     * Gets the value whether the player would be allowed to build here.
     * Defaults to {@code false} if the server was going to stop them (such as, the
     * player is in Spawn). Note that this is an entirely different check
     * than BLOCK_CANBUILD, as this refers to a player, not universe-physics
     * rule like cactus on dirt.
     *
     * @return boolean whether the server would allow a player to build here
     */
    boolean canBuild();

    /**
     * Sets the canBuild state of this event. Set to {@code true} if you want the
     * player to be able to build.
     *
     * @param canBuild {@code true} if you want the player to be able to build
     */
    void setBuild(boolean canBuild);

    /**
     * Gets the hand which placed the block
     *
     * @return Main or off-hand, depending on which hand was used to place the block
     */
    EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
