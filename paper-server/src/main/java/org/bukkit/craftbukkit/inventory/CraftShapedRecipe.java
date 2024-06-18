package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapedRecipes;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class CraftShapedRecipe extends ShapedRecipe implements CraftRecipe {
    // TODO: Could eventually use this to add a matches() method or some such
    private ShapedRecipes recipe;

    public CraftShapedRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftShapedRecipe(NamespacedKey key, ItemStack result, ShapedRecipes recipe) {
        this(key, result);
        this.recipe = recipe;
    }

    public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe) {
        if (recipe instanceof CraftShapedRecipe) {
            return (CraftShapedRecipe) recipe;
        }
        CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getKey(), recipe.getResult());
        ret.setGroup(recipe.getGroup());
        ret.setCategory(recipe.getCategory());
        String[] shape = recipe.getShape();
        ret.shape(shape);
        Map<Character, RecipeChoice> ingredientMap = recipe.getChoiceMap();
        for (char c : ingredientMap.keySet()) {
            RecipeChoice stack = ingredientMap.get(c);
            if (stack != null) {
                ret.setIngredient(c, stack);
            }
        }
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        Map<Character, org.bukkit.inventory.RecipeChoice> ingred = this.getChoiceMap();
        String[] shape = replaceUndefinedIngredientsWithEmpty(this.getShape(), ingred);
        ingred.values().removeIf(Objects::isNull);
        Map<Character, RecipeItemStack> data = Maps.transformValues(ingred, (bukkit) -> toNMS(bukkit, false));

        ShapedRecipePattern pattern = ShapedRecipePattern.of(data, shape);
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toMinecraft(this.getKey()), new ShapedRecipes(this.getGroup(), CraftRecipe.getCategory(this.getCategory()), pattern, CraftItemStack.asNMSCopy(this.getResult()))));
    }

    private static String[] replaceUndefinedIngredientsWithEmpty(String[] shape, Map<Character, org.bukkit.inventory.RecipeChoice> ingredients) {
        for (int i = 0; i < shape.length; i++) {
            String row = shape[i];
            StringBuilder filteredRow = new StringBuilder(row.length());

            for (char character : row.toCharArray()) {
                filteredRow.append(ingredients.get(character) == null ? ' ' : character);
            }

            shape[i] = filteredRow.toString();
        }

        return shape;
    }
}
