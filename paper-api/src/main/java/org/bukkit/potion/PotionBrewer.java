package org.bukkit.potion;

import java.util.Collection;

/**
 * Represents a brewer that can create {@link PotionEffect}s.
 */
public interface PotionBrewer {
    /**
     * Creates a {@link PotionEffect} from the given {@link PotionEffectType},
     * applying duration modifiers and checks.
     *
     * @param potion
     *            The type of potion
     * @param duration
     *            The duration in ticks
     * @param amplifier
     *            The amplifier of the effect
     */
    public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier);

    /**
     * Returns a collection of {@link PotionEffect} that would be applied from a
     * potion with the given data value.
     *
     * @param damage
     *            The data value of the potion
     * @return
     */
    public Collection<PotionEffect> getEffectsFromDamage(int damage);
}
