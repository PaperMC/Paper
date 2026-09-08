package org.bukkit.craftbukkit.event.block;

import java.util.Optional;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.CampfireStartEvent;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftCampfireStartEvent extends CraftInventoryBlockStartEvent implements CampfireStartEvent {

    private final CampfireRecipe campfireRecipe;
    private int cookingTime;

    public CraftCampfireStartEvent(final Block campfire, final ItemStack source, final CampfireRecipe recipe) {
        super(campfire, source);
        this.cookingTime = recipe.getCookingTime();
        this.campfireRecipe = recipe;
    }

    public CraftCampfireStartEvent(
        final CampfireBlockEntity campfire, final net.minecraft.world.item.ItemStack source, final Optional<RecipeHolder<CampfireCookingRecipe>> recipe
    ) {
        this(
            CraftBlock.at(campfire.getLevel(),
                campfire.getBlockPos()),
            CraftItemStack.asCraftMirror(source),
            (CampfireRecipe) recipe.get().toBukkitRecipe()
        );
    }

    @Override
    public CampfireRecipe getRecipe() {
        return this.campfireRecipe;
    }

    @Override
    public int getTotalCookTime() {
        return this.cookingTime;
    }

    @Override
    public void setTotalCookTime(final int cookTime) {
        this.cookingTime = cookTime;
    }
}
