package org.bukkit.event.inventory;

import org.bukkit.event.block.BlockCookEvent;

/**
 * Called when an ItemStack is successfully smelted in a furnace-like block
 * such as a {@link org.bukkit.block.Furnace}, {@link org.bukkit.block.Smoker},
 * or {@link org.bukkit.block.BlastFurnace}.
 */
public interface FurnaceSmeltEvent extends BlockCookEvent {
}
