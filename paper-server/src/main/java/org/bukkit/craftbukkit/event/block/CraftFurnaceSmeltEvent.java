package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftFurnaceSmeltEvent extends CraftBlockCookEvent implements FurnaceSmeltEvent {

    public CraftFurnaceSmeltEvent(final Block furnace, final ItemStack source, final ItemStack result, final @Nullable CookingRecipe<?> recipe) {
        super(furnace, source, result, recipe);
    }

    public CraftFurnaceSmeltEvent(
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.item.ItemStack source,
        final net.minecraft.world.item.ItemStack result,
        final RecipeHolder<? extends AbstractCookingRecipe> recipe
    ) {
        this(
            CraftBlock.at(level, pos),
            CraftItemStack.asCraftMirror(source),
            CraftItemStack.asBukkitCopy(result),
            (CookingRecipe<?>) recipe.toBukkitRecipe()
        );
    }
}
