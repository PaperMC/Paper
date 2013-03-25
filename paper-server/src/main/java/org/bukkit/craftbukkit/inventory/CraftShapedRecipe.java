package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.server.CraftingManager;
import net.minecraft.server.ShapedRecipes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftShapedRecipe extends ShapedRecipe implements CraftRecipe {
    // TODO: Could eventually use this to add a matches() method or some such
    private ShapedRecipes recipe;

    public CraftShapedRecipe(ItemStack result) {
        super(result);
    }

    public CraftShapedRecipe(ItemStack result, ShapedRecipes recipe) {
        this(result);
        this.recipe = recipe;
    }

    public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe) {
        if (recipe instanceof CraftShapedRecipe) {
            return (CraftShapedRecipe) recipe;
        }
        CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getResult());
        String[] shape = recipe.getShape();
        ret.shape(shape);
        Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        for (char c : ingredientMap.keySet()) {
            ItemStack stack = ingredientMap.get(c);
            if (stack != null) {
                ret.setIngredient(c, stack.getType(), stack.getDurability());
            }
        }
        return ret;
    }

    public void addToCraftingManager() {
        Object[] data;
        String[] shape = this.getShape();
        Map<Character, ItemStack> ingred = this.getIngredientMap();
        int datalen = shape.length;
        datalen += ingred.size() * 2;
        int i = 0;
        data = new Object[datalen];
        for (; i < shape.length; i++) {
            data[i] = shape[i];
        }
        for (char c : ingred.keySet()) {
            ItemStack mdata = ingred.get(c);
            if (mdata == null) continue;
            data[i] = c;
            i++;
            int id = mdata.getTypeId();
            short dmg = mdata.getDurability();
            data[i] = new net.minecraft.server.ItemStack(id, 1, dmg);
            i++;
        }
        CraftingManager.getInstance().registerShapedRecipe(CraftItemStack.asNMSCopy(this.getResult()), data);
    }
}
