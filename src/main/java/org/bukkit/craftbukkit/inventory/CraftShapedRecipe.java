package org.bukkit.craftbukkit.inventory;

import java.util.HashMap;

import net.minecraft.server.CraftingManager;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

public class CraftShapedRecipe extends ShapedRecipe implements CraftRecipe {
    public CraftShapedRecipe(ItemStack result) {
        super(result);
    }

    public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe) {
        if (recipe instanceof CraftShapedRecipe) {
            return (CraftShapedRecipe) recipe;
        }
        CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getResult());
        String[] shape = recipe.getShape();
        ret.shape(shape);
        for (char c : recipe.getIngredientMap().keySet()) {
            ret.setIngredient(c, recipe.getIngredientMap().get(c));
        }
        return ret;
    }

    public void addToCraftingManager() {
        Object[] data;
        String[] shape = this.getShape();
        HashMap<Character, MaterialData> ingred = this.getIngredientMap();
        int datalen = shape.length;
        datalen += ingred.size() * 2;
        int i = 0;
        data = new Object[datalen];
        for (; i < shape.length; i++) {
            data[i] = shape[i];
        }
        for (char c : ingred.keySet()) {
            data[i] = c;
            i++;
            MaterialData mdata = ingred.get(c);
            int id = mdata.getItemTypeId();
            byte dmg = mdata.getData();
            data[i] = new net.minecraft.server.ItemStack(id, 1, dmg);
            i++;
        }
        CraftingManager.getInstance().registerShapedRecipe(CraftItemStack.createNMSItemStack(this.getResult()), data);
    }
}
