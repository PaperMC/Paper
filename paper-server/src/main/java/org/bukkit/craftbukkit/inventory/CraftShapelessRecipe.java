package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

public class CraftShapelessRecipe extends ShapelessRecipe implements CraftRecipe {
    // TODO: Could eventually use this to add a matches() method or some such
    private net.minecraft.world.item.crafting.ShapelessRecipe recipe;

    public CraftShapelessRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftShapelessRecipe(NamespacedKey key, ItemStack result, net.minecraft.world.item.crafting.ShapelessRecipe recipe) {
        this(key, result);
        this.recipe = recipe;
    }

    public static CraftShapelessRecipe fromBukkitRecipe(ShapelessRecipe recipe) {
        if (recipe instanceof CraftShapelessRecipe) {
            return (CraftShapelessRecipe) recipe;
        }
        CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getKey(), recipe.getResult());
        ret.setGroup(recipe.getGroup());
        ret.setCategory(recipe.getCategory());
        for (RecipeChoice ingredient : recipe.getChoiceList()) {
            ret.addIngredient(ingredient);
        }
        return ret;
    }

    @Override
    public void addToRecipeManager() {
        List<org.bukkit.inventory.RecipeChoice> choices = this.getChoiceList();
        List<Ingredient> ingredients = new ArrayList<>(choices.size());
        for (org.bukkit.inventory.RecipeChoice choice : choices) {
            ingredients.add(CraftRecipe.toIngredient(choice, true));
        }

        net.minecraft.world.item.crafting.ShapelessRecipe recipe = new net.minecraft.world.item.crafting.ShapelessRecipe(
            new net.minecraft.world.item.crafting.Recipe.CommonInfo(true),
            new net.minecraft.world.item.crafting.CraftingRecipe.CraftingBookInfo(CraftRecipe.getCategory(this.getCategory()), this.getGroup()),
            CraftItemStack.asTemplate(this.getResult()),
            ingredients
        );
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toResourceKey(Registries.RECIPE, this.getKey()), recipe));
    }
}
