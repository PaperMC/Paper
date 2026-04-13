package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jspecify.annotations.Nullable;

public interface CraftRecipe extends Recipe {

    void addToRecipeManager();

    static Optional<Ingredient> toPossibleIngredient(@Nullable RecipeChoice bukkit, boolean requireNotEmpty) {
        return (bukkit == null || bukkit == RecipeChoice.empty()) ? Optional.empty() : Optional.of(toIngredient(bukkit, requireNotEmpty)); // Paper - support "empty" choices
    }

    static Ingredient toIngredient(RecipeChoice bukkit, boolean requireNotEmpty) {
        Ingredient stack;

        if (bukkit == null) {
            stack = Ingredient.of();
        } else if (bukkit instanceof final RecipeChoice.ItemTypeChoice itemTypeChoice) {
            stack = Ingredient.of(PaperRegistrySets.convertToNms(Registries.ITEM, Conversions.global().lookup(), itemTypeChoice.itemTypes()));
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            stack = Ingredient.of(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map(CraftItemType::bukkitToMinecraft));
        } else if (bukkit instanceof RecipeChoice.ExactChoice) {
            stack = Ingredient.ofStacks(((RecipeChoice.ExactChoice) bukkit).getChoices().stream().map(CraftItemStack::asNMSCopy).toList());
            // Paper start - support "empty" choices - legacy method that spigot might incorrectly call
            // Their impl of Ingredient.of() will error, ingredients need at least one entry.
            // Callers running into this exception may have passed an incorrect empty() recipe choice to a non-empty slot or
            // spigot calls this method in a wrong place.
        } else if (bukkit == RecipeChoice.empty()) {
            throw new IllegalArgumentException("This ingredient cannot be empty");
            // Paper end - support "empty" choices
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }

        if (requireNotEmpty) {
            Preconditions.checkArgument(!stack.isEmpty(), "Recipe requires at least one non-air choice");
        }

        return stack;
    }

    static RecipeChoice toChoice(Optional<Ingredient> ingredient) {
        return ingredient.map(CraftRecipe::toChoice).orElse(RecipeChoice.empty()); // Paper - fix issue with recipe API
    }

    static RecipeChoice toChoice(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            return RecipeChoice.empty(); // Paper - null breaks API contracts
        }

        if (ingredient.isExact()) {
            List<org.bukkit.inventory.ItemStack> choices = new ArrayList<>(ingredient.itemStacks().size());
            for (net.minecraft.world.item.ItemStack i : ingredient.itemStacks()) {
                choices.add(CraftItemStack.asBukkitCopy(i));
            }

            return new RecipeChoice.ExactChoice(choices);
        } else {
            final RegistryKeySet<ItemType> itemTypes = PaperRegistrySets.convertToApi(RegistryKey.ITEM, ingredient.values);
            return RecipeChoice.itemType(itemTypes);
        }
    }

    static net.minecraft.world.item.crafting.CraftingBookCategory getCategory(CraftingBookCategory bukkit) {
        return net.minecraft.world.item.crafting.CraftingBookCategory.valueOf(bukkit.name());
    }

    static CraftingBookCategory getCategory(net.minecraft.world.item.crafting.CraftingBookCategory internal) {
        return CraftingBookCategory.valueOf(internal.name());
    }

    static net.minecraft.world.item.crafting.CookingBookCategory getCategory(CookingBookCategory bukkit) {
        return net.minecraft.world.item.crafting.CookingBookCategory.valueOf(bukkit.name());
    }

    static CookingBookCategory getCategory(net.minecraft.world.item.crafting.CookingBookCategory internal) {
        return CookingBookCategory.valueOf(internal.name());
    }
}
