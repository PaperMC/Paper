package org.bukkit.potion;

/**
 * Represents a category of {@link PotionEffectType} and its effect on an entity.
 */
public enum PotionEffectTypeCategory {

    /**
     * Beneficial effects that positively impact an entity, such as Regeneration,
     * Absorption, or Fire Resistance.
     */
    BENEFICIAL,
    /**
     * Harmful effects that negatively impact an entity, such as Blindness, Wither,
     * or Levitation.
     */
    HARMFUL,
    /**
     * Neutral effects that have neither a positive nor negative effect on an
     * entity, such as Glowing or Bad Omen.
     */
    NEUTRAL;

}
