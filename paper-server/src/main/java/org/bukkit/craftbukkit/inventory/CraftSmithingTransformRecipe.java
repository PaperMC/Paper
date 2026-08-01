package org.bukkit.craftbukkit.inventory;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;

public class CraftSmithingTransformRecipe extends SmithingTransformRecipe implements CraftRecipe {
    public CraftSmithingTransformRecipe(NamespacedKey key, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        super(key, result, template, base, addition);
    }
    // Paper start - Option to prevent data components copy
    public CraftSmithingTransformRecipe(NamespacedKey key, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition, boolean copyDataComponents) {
        super(key, result, template, base, addition, copyDataComponents);
    }
    // Paper end - Option to prevent data components copy

    public static CraftSmithingTransformRecipe fromBukkitRecipe(SmithingTransformRecipe recipe) {
        if (recipe instanceof CraftSmithingTransformRecipe) {
            return (CraftSmithingTransformRecipe) recipe;
        }
        return new CraftSmithingTransformRecipe(recipe.getKey(), recipe.getResult(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition(), recipe.willCopyDataComponents());
    }

    @Override
    public void addToRecipeManager() {
        final net.minecraft.world.item.crafting.SmithingTransformRecipe recipe = new net.minecraft.world.item.crafting.SmithingTransformRecipe(
            new net.minecraft.world.item.crafting.Recipe.CommonInfo(true),
            CraftRecipe.toPossibleIngredient(this.getTemplate(), false),
            CraftRecipe.toIngredient(this.getBase(), false),
            CraftRecipe.toPossibleIngredient(this.getAddition(), false),
            CraftItemStack.asTemplate(this.getResult()),
            this.willCopyDataComponents()
        );
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toResourceKey(Registries.RECIPE, this.getKey()), recipe)); // Paper - Option to prevent data components copy
    }
}
