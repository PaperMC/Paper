package org.bukkit.craftbukkit.inventory;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.BrewingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class CraftBrewingRecipe extends BrewingRecipe implements CraftRecipe {

    public CraftBrewingRecipe(final NamespacedKey key, final ItemStack result, final RecipeChoice input, final RecipeChoice ingredient) {
        super(key, result, input, ingredient);
    }

    public static CraftBrewingRecipe fromBukkitRecipe(final BrewingRecipe recipe) {
        if (recipe instanceof final CraftBrewingRecipe craftBrewingRecipe) {
            return craftBrewingRecipe;
        }

        return new CraftBrewingRecipe(recipe.getKey(), recipe.getResult(), recipe.getInput(), recipe.getIngredient());
    }

    @Override
    public void addToRecipeManager() {
        final net.minecraft.world.item.crafting.BrewingRecipe internalRecipe = new net.minecraft.world.item.crafting.BrewingRecipe(
            new PotionIngredient(CraftRecipe.toIngredient(this.getInput(), false), Optional.empty()),
            new PotionIngredient(CraftRecipe.toIngredient(this.getIngredient(), false), Optional.empty()),
            CraftItemStack.asTemplate(this.getResult())
        );
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toResourceKey(Registries.RECIPE, this.getKey()), internalRecipe));
    }
}
