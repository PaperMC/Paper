package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.crafting.RecipeItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public interface CraftRecipe extends Recipe {

    void addToCraftingManager();

    default RecipeItemStack toNMS(RecipeChoice bukkit, boolean requireNotEmpty) {
        RecipeItemStack stack;

        if (bukkit == null) {
            stack = RecipeItemStack.EMPTY;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            stack = new RecipeItemStack(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.world.item.crafting.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else if (bukkit instanceof RecipeChoice.ExactChoice) {
            stack = new RecipeItemStack(((RecipeChoice.ExactChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.world.item.crafting.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(mat))));
            stack.exact = true;
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }

        stack.getItems();
        if (requireNotEmpty && stack.itemStacks.length == 0) {
            throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
        }

        return stack;
    }

    public static RecipeChoice toBukkit(RecipeItemStack list) {
        list.getItems();

        if (list.itemStacks.length == 0) {
            return null;
        }

        if (list.exact) {
            List<org.bukkit.inventory.ItemStack> choices = new ArrayList<>(list.itemStacks.length);
            for (net.minecraft.world.item.ItemStack i : list.itemStacks) {
                choices.add(CraftItemStack.asBukkitCopy(i));
            }

            return new RecipeChoice.ExactChoice(choices);
        } else {

            List<org.bukkit.Material> choices = new ArrayList<>(list.itemStacks.length);
            for (net.minecraft.world.item.ItemStack i : list.itemStacks) {
                choices.add(CraftMagicNumbers.getMaterial(i.getItem()));
            }

            return new RecipeChoice.MaterialChoice(choices);
        }
    }

    public static net.minecraft.world.item.crafting.CraftingBookCategory getCategory(CraftingBookCategory bukkit) {
        return net.minecraft.world.item.crafting.CraftingBookCategory.valueOf(bukkit.name());
    }

    public static CraftingBookCategory getCategory(net.minecraft.world.item.crafting.CraftingBookCategory nms) {
        return CraftingBookCategory.valueOf(nms.name());
    }

    public static net.minecraft.world.item.crafting.CookingBookCategory getCategory(CookingBookCategory bukkit) {
        return net.minecraft.world.item.crafting.CookingBookCategory.valueOf(bukkit.name());
    }

    public static CookingBookCategory getCategory(net.minecraft.world.item.crafting.CookingBookCategory nms) {
        return CookingBookCategory.valueOf(nms.name());
    }
}
