package org.bukkit.tag;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Vanilla {@link DamageType} {@link Tag tags}.
 */
@ApiStatus.Experimental
public final class DamageTypeTags {

    /**
     * Vanilla tag representing damage types which damage helmets.
     */
    public static final Tag<DamageType> DAMAGES_HELMET = getTag("damages_helmet");
    /**
     * Vanilla tag representing damage types which bypass armor.
     */
    public static final Tag<DamageType> BYPASSES_ARMOR = getTag("bypasses_armor");
    /**
     * Vanilla tag representing damage types which bypass shields.
     */
    public static final Tag<DamageType> BYPASSES_SHIELD = getTag("bypasses_shield");
    /**
     * Vanilla tag representing damage types which bypass invulnerability.
     */
    public static final Tag<DamageType> BYPASSES_INVULNERABILITY = getTag("bypasses_invulnerability");
    /**
     * Vanilla tag representing damage types which bypass cooldowns.
     */
    public static final Tag<DamageType> BYPASSES_COOLDOWN = getTag("bypasses_cooldown");
    /**
     * Vanilla tag representing damage types which bypass effects.
     */
    public static final Tag<DamageType> BYPASSES_EFFECTS = getTag("bypasses_effects");
    /**
     * Vanilla tag representing damage types which bypass resistance.
     */
    public static final Tag<DamageType> BYPASSES_RESISTANCE = getTag("bypasses_resistance");
    /**
     * Vanilla tag representing damage types which bypass enchantments.
     */
    public static final Tag<DamageType> BYPASSES_ENCHANTMENTS = getTag("bypasses_enchantments");
    /**
     * Vanilla tag representing all fire damage types.
     */
    public static final Tag<DamageType> IS_FIRE = getTag("is_fire");
    /**
     * Vanilla tag representing damage types which originate from projectiles.
     */
    public static final Tag<DamageType> IS_PROJECTILE = getTag("is_projectile");
    /**
     * Vanilla tag representing damage types which witches are resistant to.
     */
    public static final Tag<DamageType> WITCH_RESISTANT_TO = getTag("witch_resistant_to");
    /**
     * Vanilla tag representing all explosion damage types.
     */
    public static final Tag<DamageType> IS_EXPLOSION = getTag("is_explosion");
    /**
     * Vanilla tag representing all fall damage types.
     */
    public static final Tag<DamageType> IS_FALL = getTag("is_fall");
    /**
     * Vanilla tag representing all drowning damage types.
     */
    public static final Tag<DamageType> IS_DROWNING = getTag("is_drowning");
    /**
     * Vanilla tag representing all freezing damage types.
     */
    public static final Tag<DamageType> IS_FREEZING = getTag("is_freezing");
    /**
     * Vanilla tag representing all lightning damage types.
     */
    public static final Tag<DamageType> IS_LIGHTNING = getTag("is_lightning");
    /**
     * Vanilla tag representing damage types which do not cause entities to
     * anger.
     */
    public static final Tag<DamageType> NO_ANGER = getTag("no_anger");
    /**
     * Vanilla tag representing damage types which do not cause an impact.
     */
    public static final Tag<DamageType> NO_IMPACT = getTag("no_impact");
    /**
     * Vanilla tag representing damage types which cause maximum fall damage.
     */
    public static final Tag<DamageType> ALWAYS_MOST_SIGNIFICANT_FALL = getTag("always_most_significant_fall");
    /**
     * Vanilla tag representing damage types which withers are immune to.
     */
    public static final Tag<DamageType> WITHER_IMMUNE_TO = getTag("wither_immune_to");
    /**
     * Vanilla tag representing damage types which ignite armor stands.
     */
    public static final Tag<DamageType> IGNITES_ARMOR_STANDS = getTag("ignites_armor_stands");
    /**
     * Vanilla tag representing damage types which burn armor stands.
     */
    public static final Tag<DamageType> BURNS_ARMOR_STANDS = getTag("burns_armor_stands");
    /**
     * Vanilla tag representing damage types which avoid guardian thorn damage.
     */
    public static final Tag<DamageType> AVOIDS_GUARDIAN_THORNS = getTag("avoids_guardian_thorns");
    /**
     * Vanilla tag representing damage types which always trigger silverfish.
     */
    public static final Tag<DamageType> ALWAYS_TRIGGERS_SILVERFISH = getTag("always_triggers_silverfish");
    /**
     * Vanilla tag representing damage types which always hurt enderdragons.
     */
    public static final Tag<DamageType> ALWAYS_HURTS_ENDER_DRAGONS = getTag("always_hurts_ender_dragons");
    /**
     * Vanilla tag representing damage types which do not cause knockback.
     */
    public static final Tag<DamageType> NO_KNOCKBACK = getTag("no_knockback");
    /**
     * Vanilla tag representing damage types which always kill armor stands.
     */
    public static final Tag<DamageType> ALWAYS_KILLS_ARMOR_STANDS = getTag("always_kills_armor_stands");
    /**
     * Vanilla tag representing damage types which can break armor stands.
     */
    public static final Tag<DamageType> CAN_BREAK_ARMOR_STAND = getTag("can_break_armor_stand");
    /**
     * Vanilla tag representing damage types which bypass wolf armor.
     */
    public static final Tag<DamageType> BYPASSES_WOLF_ARMOR = getTag("bypasses_wolf_armor");
    /**
     * Vanilla tag representing damage types which are from player attacks.
     */
    public static final Tag<DamageType> IS_PLAYER_ATTACK = getTag("is_player_attack");
    /**
     * Vanilla tag representing damage types which originate from hot blocks.
     */
    public static final Tag<DamageType> BURN_FROM_STEPPING = getTag("burn_from_stepping");
    /**
     * Vanilla tag representing damage types which cause entities to panic.
     */
    public static final Tag<DamageType> PANIC_CAUSES = getTag("panic_causes");
    /**
     * Vanilla tag representing environmental damage types which cause entities
     * to panic.
     */
    public static final Tag<DamageType> PANIC_ENVIRONMENTAL_CAUSES = getTag("panic_environmental_causes");
    /**
     * Vanilla tag representing damage types which originate from mace smashes.
     */
    public static final Tag<DamageType> IS_MACE_SMASH = getTag("mace_smash");
    /**
     * Internal use only.
     */
    @ApiStatus.Internal
    public static final String REGISTRY_DAMAGE_TYPES = "damage_types";

    @NotNull
    private static Tag<DamageType> getTag(String key) {
        return Objects.requireNonNull(Bukkit.getTag(REGISTRY_DAMAGE_TYPES, NamespacedKey.minecraft(key), DamageType.class));
    }

    private DamageTypeTags() {
    }
}
