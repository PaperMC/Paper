package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.server.RecipeItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

public interface CraftRecipe extends Recipe {

    void addToCraftingManager();

    default net.minecraft.server.RecipeItemStack toNMS(RecipeChoice bukkit) {
        if (bukkit == null) {
            return RecipeItemStack.a;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            return new RecipeItemStack(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.server.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else if (bukkit instanceof RecipeChoice.ExactChoice) {
            RecipeItemStack stack = new RecipeItemStack(Stream.of(new net.minecraft.server.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(((RecipeChoice.ExactChoice) bukkit).getItemStack()))));
            stack.exact = true;

            return stack;
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }
    }

    public static RecipeChoice toBukkit(RecipeItemStack list) {
        list.buildChoices();

        if (list.choices.length == 0) {
            return null;
        }

        if (list.exact) {
            Preconditions.checkState(list.choices.length == 1, "Exact recipe must have 1 choice");
            return new RecipeChoice.ExactChoice(CraftItemStack.asBukkitCopy(list.choices[0]));
        }

        List<org.bukkit.Material> choices = new ArrayList<>(list.choices.length);
        for (net.minecraft.server.ItemStack i : list.choices) {
            choices.add(CraftMagicNumbers.getMaterial(i.getItem()));
        }

        return new RecipeChoice.MaterialChoice(choices);
    }
}
