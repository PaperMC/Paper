package io.papermc.paper.potion;

import com.google.common.base.Preconditions;
import java.util.Collection;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.BrewingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperPotionBrewer implements PotionBrewer {

    private final MinecraftServer server;

    public PaperPotionBrewer(final MinecraftServer server) {
        this.server = server;
    }

    @Override
    @Deprecated(forRemoval = true)
    public Collection<PotionEffect> getEffects(final PotionType type, final boolean upgraded, final boolean extended) {
        final org.bukkit.NamespacedKey key = type.getKey();

        Preconditions.checkArgument(!key.getKey().startsWith("strong_"), "Strong potion type cannot be used directly, got %s", key);
        Preconditions.checkArgument(!key.getKey().startsWith("long_"), "Extended potion type cannot be used directly, got %s", key);

        org.bukkit.NamespacedKey effectiveKey = key;
        if (upgraded) {
            effectiveKey = new org.bukkit.NamespacedKey(key.namespace(), "strong_" + key.key());
        } else if (extended) {
            effectiveKey = new org.bukkit.NamespacedKey(key.namespace(), "long_" + key.key());
        }

        final org.bukkit.potion.PotionType effectivePotionType = org.bukkit.Registry.POTION.get(effectiveKey);
        Preconditions.checkNotNull(effectivePotionType, "Unknown potion type from data " + effectiveKey.asMinimalString()); // Legacy error message in 1.20.4
        return effectivePotionType.getPotionEffects();
    }

    @Override
    public void resetPotionMixes() {
        final RecipeManager recipeManager = this.server.getRecipeManager();
        final RegistryAccess registryAccess = this.server.registryAccess();

        final Collection<RecipeHolder<BrewingRecipe>> brewingRecipes = recipeManager.recipes.byType(RecipeType.BREWING);

        // Completely clear brewing recipes.
        brewingRecipes.forEach(recipe -> recipeManager.recipes.byKey.remove(recipe.id()));
        brewingRecipes.clear();

        // Restore recipe manager state for brewing from registries.
        registryAccess.get(Registries.RECIPE)
            .map(Holder.Reference::value)
            .stream()
            .flatMap(HolderLookup::listElements)
            .filter(recipe -> recipe.value().getType() == RecipeType.BREWING)
            .forEach(recipe -> recipeManager.recipes.addRecipe(new RecipeHolder<>(recipe.key(), recipe.value())));
        recipeManager.finalizeRecipeLoading();
    }
}
