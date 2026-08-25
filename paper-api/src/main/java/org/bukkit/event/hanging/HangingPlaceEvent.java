package org.bukkit.event.hanging;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Triggered when a hanging entity is created in the world
 */
public interface HangingPlaceEvent extends HangingEvent, BlockEvent, Cancellable {

    /**
     * {@return the player placing the hanging entity}
     */
    @Nullable Player getPlayer();

    /**
     * {@return the block that the hanging entity was placed on}
     */
    @Override
    Block getBlock();

    /**
     * {@return the face of the block that the hanging entity was placed on}
     */
    BlockFace getBlockFace();

    /**
     * Returns the hand that was used to place the hanging entity, or {@code null}
     * if a player did not place the hanging entity.
     *
     * @return the hand
     */
    @Nullable EquipmentSlot getHand();

    /**
     * {@return the item from which the hanging entity originated}
     */
    @Nullable ItemStack getItemStack();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
