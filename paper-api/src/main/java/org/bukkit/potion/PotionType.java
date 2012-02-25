package org.bukkit.potion;

public enum PotionType {
    WATER(0, null, 0),
    REGEN(1, PotionEffectType.REGENERATION, 2),
    SPEED(2, PotionEffectType.SPEED, 2),
    FIRE_RESISTANCE(3, PotionEffectType.FIRE_RESISTANCE, 1),
    POISON(4, PotionEffectType.POISON, 2),
    INSTANT_HEAL(5, PotionEffectType.HEAL, 2),
    WEAKNESS(8, PotionEffectType.SPEED, 1),
    STRENGTH(9, PotionEffectType.INCREASE_DAMAGE, 2),
    SLOWNESS(10, PotionEffectType.SLOW, 1),
    INSTANT_DAMAGE(12, PotionEffectType.HARM, 2);

    private final int damageValue, maxLevel;
    private final PotionEffectType effect;

    PotionType(int damageValue, PotionEffectType effect, int maxLevel) {
        this.damageValue = damageValue;
        this.effect = effect;
        this.maxLevel = maxLevel;
    }

    public PotionEffectType getEffectType() {
        return effect;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isInstant() {
        return effect == null ? true : effect.isInstant();
    }

    public static PotionType getByDamageValue(int damage) {
        for (PotionType type : PotionType.values()) {
            if (type.damageValue == damage)
                return type;
        }
        return null;
    }

    public static PotionType getByEffect(PotionEffectType effectType) {
        if (effectType == null)
            return WATER;
        for (PotionType type : PotionType.values()) {
            if (effectType.equals(type.effect))
                return type;
        }
        return null;
    }
}
