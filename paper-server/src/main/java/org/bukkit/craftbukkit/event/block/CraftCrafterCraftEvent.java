package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftCrafterCraftEvent extends CraftBlockEvent implements CrafterCraftEvent {

    private final CraftingRecipe recipe;
    private ItemStack result;

    private boolean cancelled;

    public CraftCrafterCraftEvent(final Block crafter, final CraftingRecipe recipe, final ItemStack result) {
        super(crafter);
        this.result = result;
        this.recipe = recipe;
    }

    public CraftCrafterCraftEvent(final Level level, final BlockPos pos, final RecipeHolder<?> recipe, final net.minecraft.world.item.ItemStack result) {
        this(CraftBlock.at(level, pos), (CraftingRecipe) recipe.toBukkitRecipe(), CraftItemStack.asCraftMirror(result));
    }

    @Override
    public ItemStack getResult() {
        return this.result.clone();
    }

    @Override
    public void setResult(final ItemStack result) {
        this.result = result.clone();
    }

    @Override
    public CraftingRecipe getRecipe() {
        return this.recipe;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return CrafterCraftEvent.getHandlerList();
    }
}
