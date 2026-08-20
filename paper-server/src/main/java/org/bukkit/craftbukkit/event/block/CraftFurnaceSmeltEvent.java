package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftFurnaceSmeltEvent extends CraftBlockCookEvent implements FurnaceSmeltEvent {

    public CraftFurnaceSmeltEvent(final Block furnace, final ItemStack source, final ItemStack result, final @Nullable CookingRecipe<?> recipe) {
        super(furnace, source, result, recipe);
    }
}
