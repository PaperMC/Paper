package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTrimRecipe;

public class CraftSmithingTrimRecipe extends SmithingTrimRecipe implements CraftRecipe {

    public CraftSmithingTrimRecipe(NamespacedKey key, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        super(key, template, base, addition);
    }

    public static CraftSmithingTrimRecipe fromBukkitRecipe(SmithingTrimRecipe recipe) {
        if (recipe instanceof CraftSmithingTrimRecipe) {
            return (CraftSmithingTrimRecipe) recipe;
        }
        CraftSmithingTrimRecipe ret = new CraftSmithingTrimRecipe(recipe.getKey(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition());
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftRecipe.toMinecraft(this.getKey()), new net.minecraft.world.item.crafting.SmithingTrimRecipe(this.toNMSOptional(this.getTemplate(), false), this.toNMSOptional(this.getBase(), false), this.toNMSOptional(this.getAddition(), false))));
    }
}
