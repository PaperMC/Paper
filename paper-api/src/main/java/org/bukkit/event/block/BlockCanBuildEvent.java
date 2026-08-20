package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

/**
 * Called when we try to place a block, to see if we can build it here or not.
 * <p>
 * Note:
 * <ul>
 * <li>The Block returned by getBlock() is the block we are trying to place
 *     on, not the block we are trying to place.
 * <li>If you want to figure out what is being placed, use {@link
 *     #getMaterial()} instead.
 * </ul>
 */
public interface BlockCanBuildEvent extends BlockEventNew {

    /**
     * Gets the player who placed the block involved in this event.
     * <br>
     * May be {@code null} for legacy calls of the event.
     *
     * @return The Player who placed the block involved in this event
     */
    @Nullable Player getPlayer();

    /**
     * Gets the Material that we are trying to place.
     *
     * @return The Material that we are trying to place
     */
    Material getMaterial();

    /**
     * Gets the BlockData that we are trying to place.
     *
     * @return The BlockData that we are trying to place
     */
    BlockData getBlockData();

    /**
     * Gets the hand the player will use to place the block
     *
     * @return the {@link EquipmentSlot} representing the players hand.
     */
    EquipmentSlot getHand();

    /**
     * Gets whether the block can be built here.
     * <p>
     * By default, returns Minecraft's answer on whether the block can be
     * built here or not.
     *
     * @return boolean whether the block can be built
     */
    boolean isBuildable();

    /**
     * Sets whether the block can be built here or not.
     *
     * @param buildable {@code true} if you want to allow the block to be built here
     *     despite Minecraft's default behaviour
     */
    void setBuildable(boolean buildable);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
