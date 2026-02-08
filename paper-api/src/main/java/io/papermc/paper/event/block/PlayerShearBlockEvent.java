package io.papermc.paper.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player uses shears on a block.
 * <p>
 * This event is <b>not</b> called when a player breaks blocks with shears, but rather when a
 * player uses the shears on a block to collect drops from it and/or modify its state.
 * <p>
 * Examples include shearing a pumpkin to turn it into a carved pumpkin or shearing a beehive to get honeycomb.
 */
public interface PlayerShearBlockEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the block being sheared in this event.
     *
     * @return The {@link Block} which block is being sheared in this event.
     */
    Block getBlock();

    /**
     * Gets the item used to shear the block.
     *
     * @return The {@link ItemStack} of the shears.
     */
    ItemStack getItem();

    /**
     * Gets the hand used to shear the block.
     *
     * @return Either {@link EquipmentSlot#HAND} OR {@link EquipmentSlot#OFF_HAND}.
     */
    EquipmentSlot getHand();

    /**
     * Gets the resulting drops of this event.
     *
     * @return A mutable {@link List list} of {@link ItemStack items} that will be dropped as result of this event.
     */
    List<ItemStack> getDrops();

    /**
     * Gets whether the shearing of the block should be cancelled or not.
     *
     * @return Whether the shearing of the block should be cancelled or not.
     */
    @Override
    boolean isCancelled();

    /**
     * Sets whether the shearing of the block should be cancelled or not.
     *
     * @param cancel whether the shearing of the block should be cancelled or not.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
