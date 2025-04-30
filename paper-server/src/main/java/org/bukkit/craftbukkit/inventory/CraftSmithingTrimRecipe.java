package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
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
        if (recipe instanceof CraftSmithingTrimRecipe) {
            return (CraftSmithingTrimRecipe) recipe;
        }
        CraftSmithingTrimRecipe ret = new CraftSmithingTrimRecipe(recipe.getKey(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition(), recipe.getTrimPattern(), recipe.willCopyDataComponents()); // Paper - Option to prevent data components copy
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        final net.minecraft.world.item.crafting.SmithingTrimRecipe recipe = new net.minecraft.world.item.crafting.SmithingTrimRecipe(
            this.toNMS(this.getTemplate(), false),
            this.toNMS(this.getBase(), false),
            this.toNMS(this.getAddition(), false),
            CraftTrimPattern.bukkitToMinecraftHolder(this.getTrimPattern()),
            this.willCopyDataComponents()
        );
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftRecipe.toMinecraft(this.getKey()), recipe));
    }
}
