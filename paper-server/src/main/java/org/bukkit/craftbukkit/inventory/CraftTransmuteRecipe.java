package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.TransmuteRecipe;

public class CraftTransmuteRecipe extends TransmuteRecipe implements CraftRecipe {

    public CraftTransmuteRecipe(NamespacedKey key, Material result, RecipeChoice input, RecipeChoice material) {
        super(key, result, input, material);
    }

    public static CraftTransmuteRecipe fromBukkitRecipe(TransmuteRecipe recipe) {
        if (recipe instanceof CraftTransmuteRecipe) {
            return (CraftTransmuteRecipe) recipe;
        }
        CraftTransmuteRecipe ret = new CraftTransmuteRecipe(recipe.getKey(), recipe.getResult().getType(), recipe.getInput(), recipe.getMaterial());
        ret.setGroup(recipe.getGroup());
        ret.setCategory(recipe.getCategory());
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getRecipeManager().addRecipe(
            new RecipeHolder<>(CraftRecipe.toMinecraft(this.getKey()),
                new net.minecraft.world.item.crafting.TransmuteRecipe(
                    new net.minecraft.world.item.crafting.Recipe.CommonInfo(true),
                    new net.minecraft.world.item.crafting.CraftingRecipe.CraftingBookInfo(CraftRecipe.getCategory(this.getCategory()), this.getGroup()),
                    this.toNMS(this.getInput(), true),
                    this.toNMS(this.getMaterial(), true),
                    net.minecraft.world.item.crafting.TransmuteRecipe.DEFAULT_MATERIAL_COUNT,
                    CraftItemStack.asTemplate(this.getResult()),
                    false
                )
            )
        );
    }
}
