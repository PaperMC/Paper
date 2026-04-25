package org.bukkit.craftbukkit.inventory;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTrimRecipe;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class CraftSmithingTrimRecipe extends SmithingTrimRecipe implements CraftRecipe {

    public CraftSmithingTrimRecipe(NamespacedKey key, RecipeChoice template, RecipeChoice base, RecipeChoice addition, TrimPattern pattern) {
        super(key, template, base, addition, pattern);
    }
    // Paper start - Option to prevent data components copy
    public CraftSmithingTrimRecipe(NamespacedKey key, RecipeChoice template, RecipeChoice base, RecipeChoice addition, TrimPattern pattern, boolean copyDataComponents) {
        super(key, template, base, addition, pattern, copyDataComponents);
    }
    // Paper end - Option to prevent data components copy

    public static CraftSmithingTrimRecipe fromBukkitRecipe(SmithingTrimRecipe recipe) {
        if (recipe instanceof CraftSmithingTrimRecipe smithingTrimRecipe) {
            return smithingTrimRecipe;
        }
        return new CraftSmithingTrimRecipe(recipe.getKey(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition(), recipe.getTrimPattern(), recipe.willCopyDataComponents());
    }

    @Override
    public void addToRecipeManager() {
        final net.minecraft.world.item.crafting.SmithingTrimRecipe recipe = new net.minecraft.world.item.crafting.SmithingTrimRecipe(
            new net.minecraft.world.item.crafting.Recipe.CommonInfo(true),
            CraftRecipe.toIngredient(this.getTemplate(), false),
            CraftRecipe.toIngredient(this.getBase(), false),
            CraftRecipe.toIngredient(this.getAddition(), false),
            CraftTrimPattern.bukkitToMinecraftHolder(this.getTrimPattern()),
            this.willCopyDataComponents()
        );
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toResourceKey(Registries.RECIPE, this.getKey()), recipe));
    }
}
