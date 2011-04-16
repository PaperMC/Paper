package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;

import net.minecraft.server.CraftingManager;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

public class CraftShapelessRecipe extends ShapelessRecipe implements CraftRecipe {
    public CraftShapelessRecipe(ItemStack result) {
        super(result);
    }

    public static CraftShapelessRecipe fromBukkitRecipe(ShapelessRecipe recipe) {
        if (recipe instanceof CraftShapelessRecipe) {
            return (CraftShapelessRecipe) recipe;
        }
        CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getResult());
        for (MaterialData ingred : recipe.getIngredientList()) {
            ret.addIngredient(ingred);
        }
        return ret;
    }

    public void addToCraftingManager() {
        ArrayList<MaterialData> ingred = this.getIngredientList();
        Object[] data = new Object[ingred.size()];
        int i = 0;
        for (MaterialData mdata : ingred) {
            int id = mdata.getItemTypeId();
            byte dmg = mdata.getData();
            data[i] = new net.minecraft.server.ItemStack(id, 1, dmg);
            i++;
        }
        int id = this.getResult().getTypeId();
        int amount = this.getResult().getAmount();
        short durability = this.getResult().getDurability();
        CraftingManager.a().b(new net.minecraft.server.ItemStack(id, amount, durability), data);
    }
}
