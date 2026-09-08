package org.bukkit.craftbukkit.event.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftBlockCookEvent extends CraftBlockEvent implements BlockCookEvent {

    private final ItemStack source;
    private ItemStack result;
    private final @Nullable CookingRecipe<?> recipe;

    private boolean cancelled;

    public CraftBlockCookEvent(final Block block, final ItemStack source, final ItemStack result, final @Nullable CookingRecipe<?> recipe) {
        super(block);
        this.source = source;
        this.result = result;
        this.recipe = recipe;
    }

    public CraftBlockCookEvent(
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.item.ItemStack source,
        final net.minecraft.world.item.ItemStack result,
        final Optional<RecipeHolder<CampfireCookingRecipe>> recipe
    ) {
        this(
            CraftBlock.at(level, pos),
            CraftItemStack.asCraftMirror(source),
            CraftItemStack.asBukkitCopy(result),
            (CookingRecipe<?>) recipe.map(RecipeHolder::toBukkitRecipe).orElse(null)
        );
    }

    @Override
    public ItemStack getSource() {
        return this.source;
    }

    @Override
    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public void setResult(final ItemStack result) {
        this.result = result;
    }

    @Override
    public @Nullable CookingRecipe<?> getRecipe() {
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
        return BlockCookEvent.getHandlerList();
    }
}
