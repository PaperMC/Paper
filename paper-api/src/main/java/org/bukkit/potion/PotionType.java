package org.bukkit.potion;

import org.jetbrains.annotations.Nullable;

/**
 * This enum reflects and matches each potion state that can be obtained from
 * the Creative mode inventory
 */
public enum PotionType {
    UNCRAFTABLE(null, false, false),
    WATER(null, false, false),
    MUNDANE(null, false, false),
    THICK(null, false, false),
    AWKWARD(null, false, false),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION, false, true),
    INVISIBILITY(PotionEffectType.INVISIBILITY, false, true),
    JUMP(PotionEffectType.JUMP, true, true),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE, false, true),
    SPEED(PotionEffectType.SPEED, true, true),
    SLOWNESS(PotionEffectType.SLOW, true, true),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING, false, true),
    INSTANT_HEAL(PotionEffectType.HEAL, true, false),
    INSTANT_DAMAGE(PotionEffectType.HARM, true, false),
    POISON(PotionEffectType.POISON, true, true),
    REGEN(PotionEffectType.REGENERATION, true, true),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE, true, true),
    WEAKNESS(PotionEffectType.WEAKNESS, false, true),
    LUCK(PotionEffectType.LUCK, false, false),
    TURTLE_MASTER(PotionEffectType.SLOW, true, true), // TODO: multiple effects
    SLOW_FALLING(PotionEffectType.SLOW_FALLING, false, true),
    ;

    private final PotionEffectType effect;
    private final boolean upgradeable;
    private final boolean extendable;

    PotionType(/*@Nullable*/ PotionEffectType effect, boolean upgradeable, boolean extendable) {
        this.effect = effect;
        this.upgradeable = upgradeable;
        this.extendable = extendable;
    }

    @Nullable
    public PotionEffectType getEffectType() {
        return effect;
    }

    public boolean isInstant() {
        return effect != null && effect.isInstant();
    }

    /**
     * Checks if the potion type has an upgraded state.
     * This refers to whether or not the potion type can be Tier 2,
     * such as Potion of Fire Resistance II.
     *
     * @return true if the potion type can be upgraded;
     */
    public boolean isUpgradeable() {
        return upgradeable;
    }

    /**
     * Checks if the potion type has an extended state.
     * This refers to the extended duration potions
     *
     * @return true if the potion type can be extended
     */
    public boolean isExtendable() {
        return extendable;
    }

    public int getMaxLevel() {
        return upgradeable ? 2 : 1;
    }

    /**
     * @param effectType the effect to get by
     * @return the matching potion type
     * @deprecated Misleading
     */
    @Deprecated
    @Nullable
    public static PotionType getByEffect(@Nullable PotionEffectType effectType) {
        if (effectType == null)
            return WATER;
        for (PotionType type : PotionType.values()) {
            if (effectType.equals(type.effect))
                return type;
        }
        return null;
    }
}
