package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.FurnaceRecipes;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class CraftFurnaceRecipe extends FurnaceRecipe implements CraftRecipe {
    public CraftFurnaceRecipe(ItemStack result, Material source) {
        super(result, source);
    }

    public CraftFurnaceRecipe(ItemStack result, MaterialData source) {
        super(result, source);
    }

    public static CraftFurnaceRecipe fromBukkitRecipe(FurnaceRecipe recipe) {
        if (recipe instanceof CraftFurnaceRecipe) {
            return (CraftFurnaceRecipe) recipe;
        }
        return new CraftFurnaceRecipe(recipe.getResult(), recipe.getInput());
    }

    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        MaterialData input = this.getInput();
        int id = result.getTypeId();
        int amount = result.getAmount();
        int dmg = result.getDurability();
        FurnaceRecipes.a().a(input.getItemTypeId(), new net.minecraft.server.ItemStack(id, amount, dmg));
    }
}
