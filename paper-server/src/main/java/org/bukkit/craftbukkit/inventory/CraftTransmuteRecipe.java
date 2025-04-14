package org.bukkit.craftbukkit.inventory;

import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.TransmuteResult;
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
        final ItemStack unwrappedInternalStack = CraftItemStack.unwrap(this.getResult());
        MinecraftServer.getServer().getRecipeManager().addRecipe(
            new RecipeHolder<>(CraftRecipe.toMinecraft(this.getKey()),
                new net.minecraft.world.item.crafting.TransmuteRecipe(this.getGroup(),
                    CraftRecipe.getCategory(this.getCategory()),
                    this.toNMS(this.getInput(), true),
                    this.toNMS(this.getMaterial(), true),
                    new TransmuteResult(unwrappedInternalStack.getItemHolder(), unwrappedInternalStack.getCount(), unwrappedInternalStack.getComponentsPatch())
                )
            )
        );
    }
}
