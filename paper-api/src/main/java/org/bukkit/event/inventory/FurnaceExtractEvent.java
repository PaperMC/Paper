package org.bukkit.event.inventory;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a player takes items out of a furnace-like block such as a
 * {@link org.bukkit.block.Furnace}, {@link org.bukkit.block.Smoker}, or
 * {@link org.bukkit.block.BlastFurnace}.
 */
public class FurnaceExtractEvent extends BlockExpEvent {

    private final Player player;
    private final ItemStack itemStack;
    private final int itemAmount;

    @ApiStatus.Internal
    public FurnaceExtractEvent(@NotNull Player player, @NotNull Block block, @NotNull ItemStack itemStack, int itemAmount, int exp) {
        super(block, exp);
        this.player = player;
        this.itemStack = itemStack;
        this.itemAmount = itemAmount;
    }

    /**
     * Get the player that triggered the event
     *
     * @return the relevant player
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the Material of the item being retrieved
     *
     * @return the material of the item
     */
    @NotNull
    public Material getItemType() {
        return this.itemStack.getType();
    }

    /**
     * Get the ItemStack of the item triggering the event
     *
     * @return the item stack
     */
    @NotNull
    public ItemStack getItemStack() {
        return this.itemStack.clone();
    }

    /**
     * Get the item count being retrieved
     *
     * @return the amount of the item
     */
    public int getItemAmount() {
        return this.itemAmount;
    }
}
