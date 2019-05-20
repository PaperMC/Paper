package org.bukkit.potion;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a brewer that can create {@link PotionEffect}s.
 */
public interface PotionBrewer {

    /**
     * Creates a {@link PotionEffect} from the given {@link PotionEffectType},
     * applying duration modifiers and checks.
     *
     * @param potion The type of potion
     * @param duration The duration in ticks
     * @param amplifier The amplifier of the effect
     * @return The resulting potion effect
     */
    @NotNull
    public PotionEffect createEffect(@NotNull PotionEffectType potion, int duration, int amplifier);

    /**
     * Returns a collection of {@link PotionEffect} that would be applied from
     * a potion with the given data value.
     *
     * @param damage The data value of the potion
     * @return The list of effects
     * @deprecated Non-Functional
     */
    @Deprecated
    @NotNull
    public Collection<PotionEffect> getEffectsFromDamage(int damage);

    /**
     * Returns a collection of {@link PotionEffect} that would be applied from
     * a potion with the given type.
     *
     * @param type The type of the potion
     * @param upgraded Whether the potion is upgraded
     * @param extended Whether the potion is extended
     * @return The list of effects
     */
    @NotNull
    public Collection<PotionEffect> getEffects(@NotNull PotionType type, boolean upgraded, boolean extended);
}
