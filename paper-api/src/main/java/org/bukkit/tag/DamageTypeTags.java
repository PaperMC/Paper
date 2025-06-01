package org.bukkit.tag;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Vanilla {@link DamageType} {@link Tag tags}.
 */
@ApiStatus.Experimental
public final class DamageTypeTags {

    // Start generate - DamageTypeTags
    // @GeneratedFrom 1.21.6-pre1
    public static final Tag<DamageType> ALWAYS_HURTS_ENDER_DRAGONS = getTag("always_hurts_ender_dragons");

    public static final Tag<DamageType> ALWAYS_KILLS_ARMOR_STANDS = getTag("always_kills_armor_stands");

    public static final Tag<DamageType> ALWAYS_MOST_SIGNIFICANT_FALL = getTag("always_most_significant_fall");

    public static final Tag<DamageType> ALWAYS_TRIGGERS_SILVERFISH = getTag("always_triggers_silverfish");

    public static final Tag<DamageType> AVOIDS_GUARDIAN_THORNS = getTag("avoids_guardian_thorns");

    public static final Tag<DamageType> BURN_FROM_STEPPING = getTag("burn_from_stepping");

    public static final Tag<DamageType> BURNS_ARMOR_STANDS = getTag("burns_armor_stands");

    public static final Tag<DamageType> BYPASSES_ARMOR = getTag("bypasses_armor");

    public static final Tag<DamageType> BYPASSES_EFFECTS = getTag("bypasses_effects");

    public static final Tag<DamageType> BYPASSES_ENCHANTMENTS = getTag("bypasses_enchantments");

    public static final Tag<DamageType> BYPASSES_INVULNERABILITY = getTag("bypasses_invulnerability");

    public static final Tag<DamageType> BYPASSES_RESISTANCE = getTag("bypasses_resistance");

    public static final Tag<DamageType> BYPASSES_SHIELD = getTag("bypasses_shield");

    public static final Tag<DamageType> BYPASSES_WOLF_ARMOR = getTag("bypasses_wolf_armor");

    public static final Tag<DamageType> CAN_BREAK_ARMOR_STAND = getTag("can_break_armor_stand");

    public static final Tag<DamageType> DAMAGES_HELMET = getTag("damages_helmet");

    public static final Tag<DamageType> IGNITES_ARMOR_STANDS = getTag("ignites_armor_stands");

    public static final Tag<DamageType> IS_DROWNING = getTag("is_drowning");

    public static final Tag<DamageType> IS_EXPLOSION = getTag("is_explosion");

    public static final Tag<DamageType> IS_FALL = getTag("is_fall");

    public static final Tag<DamageType> IS_FIRE = getTag("is_fire");

    public static final Tag<DamageType> IS_FREEZING = getTag("is_freezing");

    public static final Tag<DamageType> IS_LIGHTNING = getTag("is_lightning");

    public static final Tag<DamageType> IS_PLAYER_ATTACK = getTag("is_player_attack");

    public static final Tag<DamageType> IS_PROJECTILE = getTag("is_projectile");

    public static final Tag<DamageType> MACE_SMASH = getTag("mace_smash");

    public static final Tag<DamageType> NO_ANGER = getTag("no_anger");

    public static final Tag<DamageType> NO_IMPACT = getTag("no_impact");

    public static final Tag<DamageType> NO_KNOCKBACK = getTag("no_knockback");

    public static final Tag<DamageType> PANIC_CAUSES = getTag("panic_causes");

    public static final Tag<DamageType> PANIC_ENVIRONMENTAL_CAUSES = getTag("panic_environmental_causes");

    public static final Tag<DamageType> WITCH_RESISTANT_TO = getTag("witch_resistant_to");

    public static final Tag<DamageType> WITHER_IMMUNE_TO = getTag("wither_immune_to");
    // End generate - DamageTypeTags
    /**
     * Vanilla tag representing damage types which originate from mace smashes.
     *
     * @deprecated use {@link #MACE_SMASH}
     */
    @Deprecated(since = "1.21.4", forRemoval = true)
    public static final Tag<DamageType> IS_MACE_SMASH = MACE_SMASH;
    /**
     * Internal use only.
     */
    @ApiStatus.Internal
    public static final String REGISTRY_DAMAGE_TYPES = "damage_types";

    @Nullable
    private static Tag<DamageType> getTag(String key) {
        return Bukkit.getTag(REGISTRY_DAMAGE_TYPES, NamespacedKey.minecraft(key), DamageType.class);
    }

    private DamageTypeTags() {
    }
}
