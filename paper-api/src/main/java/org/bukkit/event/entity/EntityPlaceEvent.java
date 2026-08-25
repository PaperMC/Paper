package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

/**
 * Triggered when an entity is created in the world by a player "placing" an item
 * on a block.
 * <br>
 * Note that this event is currently only fired for four specific placements:
 * armor stands, boats, minecarts, and end crystals.
 */
public interface EntityPlaceEvent extends EntityEvent, BlockEvent, Cancellable {

    /**
     * {@return the player placing the entity}
     */
    @Nullable Player getPlayer();

    /**
     * {@return the block that the entity was placed on}
     */
    @Override
    Block getBlock();

    /**
     * {@return the face of the block that the entity was placed on}
     */
    BlockFace getBlockFace();

    /**
     * Get the hand used to place the entity.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
