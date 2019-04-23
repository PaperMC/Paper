package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully smelted in a furnace.
 */
public class FurnaceSmeltEvent extends BlockCookEvent {

    public FurnaceSmeltEvent(@NotNull final Block furnace, @NotNull final ItemStack source, @NotNull final ItemStack result) {
        super(furnace, source, result);
    }
}
