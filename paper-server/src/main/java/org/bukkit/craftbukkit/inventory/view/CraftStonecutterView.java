package org.bukkit.craftbukkit.inventory.view;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.inventory.ContainerStonecutter;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeStonecutting;
import net.minecraft.world.item.crafting.SelectableRecipe;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.view.StonecutterView;
import org.jetbrains.annotations.NotNull;

public class CraftStonecutterView extends CraftInventoryView<ContainerStonecutter, StonecutterInventory> implements StonecutterView {

    public CraftStonecutterView(final HumanEntity player, final StonecutterInventory viewing, final ContainerStonecutter container) {
        super(player, viewing, container);
    }

    @Override
    public int getSelectedRecipeIndex() {
        return container.getSelectedRecipeIndex();
    }

    @NotNull
    @Override
    public List<StonecuttingRecipe> getRecipes() {
        final List<StonecuttingRecipe> recipes = new ArrayList<>();
        for (final SelectableRecipe.a<RecipeStonecutting> recipe : container.getVisibleRecipes().entries()) {
            recipe.recipe().recipe().map(RecipeHolder::toBukkitRecipe).ifPresent((bukkit) -> recipes.add((StonecuttingRecipe) bukkit));
        }
        return recipes;
    }

    @Override
    public int getRecipeAmount() {
        return container.getNumberOfVisibleRecipes();
    }
}
