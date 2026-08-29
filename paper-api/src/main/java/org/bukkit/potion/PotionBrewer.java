package org.bukkit.potion;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.inventory.BrewingRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Used to manage custom {@link io.papermc.paper.potion.PotionMix}s.
 * @deprecated since mojang introduced data driven brewing recipes, this type no longer offers anything that isnt
 * covered by existing recipe and potion type api.
 */
@Deprecated(since = "26.3", forRemoval = true)
public interface PotionBrewer {

    // Paper start
    /**
     * Adds a new potion mix recipe.
     *
     * @param potionMix the potion mix to add
     * @deprecated fully replaced by {@link org.bukkit.inventory.BrewingRecipe}
     */
    @Deprecated(since = "26.3", forRemoval = true)
    default void addPotionMix(@NotNull io.papermc.paper.potion.PotionMix potionMix) {
        Bukkit.getServer().addRecipe(new BrewingRecipe(
            potionMix.getKey(),
            potionMix.getResult(),
            potionMix.getInput(),
            potionMix.getIngredient()
        ));
    }

    /**
     * Removes a potion mix recipe.
     *
     * @param key the key of the mix to remove
     * @deprecated fully replaced by {@link org.bukkit.inventory.BrewingRecipe}
     */
    @Deprecated(since = "26.3", forRemoval = true)
    default void removePotionMix(@NotNull org.bukkit.NamespacedKey key) {
        Bukkit.getServer().removeRecipe(key);
    }

    /**
     * Resets potion mixes to their default, removing all custom ones.
     * @deprecated fully replaced by {@link org.bukkit.inventory.BrewingRecipe}
     */
    @Deprecated(since = "26.3", forRemoval = true)
    void resetPotionMixes();
    // Paper end

    /**
     * Creates a {@link PotionEffect} from the given {@link PotionEffectType},
     * applying duration modifiers and checks.
     *
     * @param potion The type of potion
     * @param duration The duration in ticks
     * @param amplifier The amplifier of the effect
     * @return The resulting potion effect
     * @deprecated use {@link PotionEffectType#createEffect(int, int)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.20.5") // Paper
    @NotNull
    // Paper start - make default
    default PotionEffect createEffect(@NotNull PotionEffectType potion, int duration, int amplifier) {
        return potion.createEffect(duration, amplifier);
    }
    // Paper end

    /**
     * Returns a collection of {@link PotionEffect} that would be applied from
     * a potion with the given data value.
     *
     * @param damage The data value of the potion
     * @return The list of effects
     * @deprecated Non-Functional
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
    @NotNull
    // Paper start - make default
    default Collection<PotionEffect> getEffectsFromDamage(final int damage) {
        return new java.util.ArrayList<>();
    }
    // Paper end

    /**
     * Returns a collection of {@link PotionEffect} that would be applied from
     * a potion with the given type.
     *
     * @param type The type of the potion
     * @param upgraded Whether the potion is upgraded
     * @param extended Whether the potion is extended
     * @return The list of effects
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link PotionType#getPotionEffects()} instead
     */
    @NotNull
    @Deprecated(since = "1.20.2", forRemoval = true) // Paper
    public Collection<PotionEffect> getEffects(@NotNull PotionType type, boolean upgraded, boolean extended);
}
