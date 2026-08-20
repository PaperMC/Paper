package org.bukkit.event.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This event is called when a player takes items out of a furnace-like block such as a
 * {@link org.bukkit.block.Furnace}, {@link org.bukkit.block.Smoker}, or
 * {@link org.bukkit.block.BlastFurnace}.
 */
public interface FurnaceExtractEvent extends BlockExpEvent {

    /**
     * Get the player that triggered the event
     *
     * @return the relevant player
     */
    Player getPlayer();

    /**
     * Get the ItemStack of the item triggering the event
     *
     * @return the item stack
     */
    ItemStack getItemStack();

    /**
     * Get the Material of the item being retrieved
     *
     * @return the material of the item
     * @deprecated use {@link #getItemStack()}
     */
    @Deprecated(since = "1.21.11")
    default Material getItemType() {
        return this.getItemStack().getType();
    }

    /**
     * Get the item count being retrieved
     *
     * @return the amount of the item
     * @deprecated use {@link #getItemStack()}
     */
    @Deprecated(since = "1.21.11") // todo check
    int getItemAmount();
}
