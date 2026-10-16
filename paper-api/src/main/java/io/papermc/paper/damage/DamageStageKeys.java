package io.papermc.paper.damage;

import net.kyori.adventure.key.Key;

/**
 * Identifiers for the vanilla damage stages.
 * Not all stages are applicable to every damage event, check {@link DamageCalculation#stage(Key)} before using one.
 */
public final class DamageStageKeys {

    /** Reduces damage during the victim's damage cooldown. */
    public static final Key INVULNERABILITY_REDUCTION = Key.key("paper", "invulnerability_reduction");
    /** Increases damage for entities especially vulnerable to freezing. */
    public static final Key FREEZING = Key.key("paper", "freezing");
    /** Reduces damage from certain falling blocks when the victim wears a helmet. */
    public static final Key HELMET = Key.key("paper", "helmet");
    /** Reduces damage blocked by a shield or another blocking item. */
    public static final Key BLOCKING = Key.key("paper", "blocking");
    /** Reduces damage through the victim's armor and armor toughness. */
    public static final Key ARMOR = Key.key("paper", "armor");
    /** Reduces damage through the Resistance effect. */
    public static final Key RESISTANCE = Key.key("paper", "resistance");
    /** Reduces damage through protection enchantments and similar effects. */
    public static final Key MAGIC_PROTECTION = Key.key("paper", "magic_protection");
    /** Reduces damage by consuming absorption hearts. */
    public static final Key ABSORPTION = Key.key("paper", "absorption");

    private DamageStageKeys() {
    }
}
