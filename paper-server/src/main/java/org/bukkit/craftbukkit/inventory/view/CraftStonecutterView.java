package org.bukkit.craftbukkit.inventory.view;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.view.StonecutterView;
import org.jetbrains.annotations.NotNull;

public class CraftStonecutterView extends CraftInventoryView<StonecutterMenu, StonecutterInventory> implements StonecutterView {

    public CraftStonecutterView(final HumanEntity player, final StonecutterInventory viewing, final StonecutterMenu container) {
        super(player, viewing, container);
    }

    @Override
    public int getSelectedRecipeIndex() {
        return this.container.getSelectedRecipeIndex();
    }

    @NotNull
    @Override
    public List<StonecuttingRecipe> getRecipes() {
        final List<StonecuttingRecipe> recipes = new ArrayList<>();
        for (final SelectableRecipe.SingleInputEntry<StonecutterRecipe> recipe : this.container.getVisibleRecipes().entries()) {
            recipe.recipe().recipe().map(RecipeHolder::toBukkitRecipe).ifPresent((bukkit) -> recipes.add((StonecuttingRecipe) bukkit));
        }
        return recipes;
    }

    @Override
    public int getRecipeAmount() {
        return this.container.getNumberOfVisibleRecipes();
    }
}
