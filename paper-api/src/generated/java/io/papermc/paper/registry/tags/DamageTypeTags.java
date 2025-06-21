package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.damage.DamageType;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#DAMAGE_TYPE}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedFrom("1.21.6")
public final class DamageTypeTags {
    /**
     * {@code #minecraft:always_hurts_ender_dragons}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> ALWAYS_HURTS_ENDER_DRAGONS = fetch(DamageTypeTagKeys.ALWAYS_HURTS_ENDER_DRAGONS);

    /**
     * {@code #minecraft:always_kills_armor_stands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> ALWAYS_KILLS_ARMOR_STANDS = fetch(DamageTypeTagKeys.ALWAYS_KILLS_ARMOR_STANDS);

    /**
     * {@code #minecraft:always_most_significant_fall}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> ALWAYS_MOST_SIGNIFICANT_FALL = fetch(DamageTypeTagKeys.ALWAYS_MOST_SIGNIFICANT_FALL);

    /**
     * {@code #minecraft:always_triggers_silverfish}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> ALWAYS_TRIGGERS_SILVERFISH = fetch(DamageTypeTagKeys.ALWAYS_TRIGGERS_SILVERFISH);

    /**
     * {@code #minecraft:avoids_guardian_thorns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> AVOIDS_GUARDIAN_THORNS = fetch(DamageTypeTagKeys.AVOIDS_GUARDIAN_THORNS);

    /**
     * {@code #minecraft:burn_from_stepping}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BURN_FROM_STEPPING = fetch(DamageTypeTagKeys.BURN_FROM_STEPPING);

    /**
     * {@code #minecraft:burns_armor_stands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BURNS_ARMOR_STANDS = fetch(DamageTypeTagKeys.BURNS_ARMOR_STANDS);

    /**
     * {@code #minecraft:bypasses_armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_ARMOR = fetch(DamageTypeTagKeys.BYPASSES_ARMOR);

    /**
     * {@code #minecraft:bypasses_effects}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_EFFECTS = fetch(DamageTypeTagKeys.BYPASSES_EFFECTS);

    /**
     * {@code #minecraft:bypasses_enchantments}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_ENCHANTMENTS = fetch(DamageTypeTagKeys.BYPASSES_ENCHANTMENTS);

    /**
     * {@code #minecraft:bypasses_invulnerability}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_INVULNERABILITY = fetch(DamageTypeTagKeys.BYPASSES_INVULNERABILITY);

    /**
     * {@code #minecraft:bypasses_resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_RESISTANCE = fetch(DamageTypeTagKeys.BYPASSES_RESISTANCE);

    /**
     * {@code #minecraft:bypasses_shield}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_SHIELD = fetch(DamageTypeTagKeys.BYPASSES_SHIELD);

    /**
     * {@code #minecraft:bypasses_wolf_armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> BYPASSES_WOLF_ARMOR = fetch(DamageTypeTagKeys.BYPASSES_WOLF_ARMOR);

    /**
     * {@code #minecraft:can_break_armor_stand}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> CAN_BREAK_ARMOR_STAND = fetch(DamageTypeTagKeys.CAN_BREAK_ARMOR_STAND);

    /**
     * {@code #minecraft:damages_helmet}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> DAMAGES_HELMET = fetch(DamageTypeTagKeys.DAMAGES_HELMET);

    /**
     * {@code #minecraft:ignites_armor_stands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IGNITES_ARMOR_STANDS = fetch(DamageTypeTagKeys.IGNITES_ARMOR_STANDS);

    /**
     * {@code #minecraft:is_drowning}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_DROWNING = fetch(DamageTypeTagKeys.IS_DROWNING);

    /**
     * {@code #minecraft:is_explosion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_EXPLOSION = fetch(DamageTypeTagKeys.IS_EXPLOSION);

    /**
     * {@code #minecraft:is_fall}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_FALL = fetch(DamageTypeTagKeys.IS_FALL);

    /**
     * {@code #minecraft:is_fire}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_FIRE = fetch(DamageTypeTagKeys.IS_FIRE);

    /**
     * {@code #minecraft:is_freezing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_FREEZING = fetch(DamageTypeTagKeys.IS_FREEZING);

    /**
     * {@code #minecraft:is_lightning}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_LIGHTNING = fetch(DamageTypeTagKeys.IS_LIGHTNING);

    /**
     * {@code #minecraft:is_player_attack}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_PLAYER_ATTACK = fetch(DamageTypeTagKeys.IS_PLAYER_ATTACK);

    /**
     * {@code #minecraft:is_projectile}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> IS_PROJECTILE = fetch(DamageTypeTagKeys.IS_PROJECTILE);

    /**
     * {@code #minecraft:mace_smash}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> MACE_SMASH = fetch(DamageTypeTagKeys.MACE_SMASH);

    /**
     * {@code #minecraft:no_anger}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> NO_ANGER = fetch(DamageTypeTagKeys.NO_ANGER);

    /**
     * {@code #minecraft:no_impact}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> NO_IMPACT = fetch(DamageTypeTagKeys.NO_IMPACT);

    /**
     * {@code #minecraft:no_knockback}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> NO_KNOCKBACK = fetch(DamageTypeTagKeys.NO_KNOCKBACK);

    /**
     * {@code #minecraft:panic_causes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> PANIC_CAUSES = fetch(DamageTypeTagKeys.PANIC_CAUSES);

    /**
     * {@code #minecraft:panic_environmental_causes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> PANIC_ENVIRONMENTAL_CAUSES = fetch(DamageTypeTagKeys.PANIC_ENVIRONMENTAL_CAUSES);

    /**
     * {@code #minecraft:witch_resistant_to}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> WITCH_RESISTANT_TO = fetch(DamageTypeTagKeys.WITCH_RESISTANT_TO);

    /**
     * {@code #minecraft:wither_immune_to}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<DamageType> WITHER_IMMUNE_TO = fetch(DamageTypeTagKeys.WITHER_IMMUNE_TO);

    private DamageTypeTags() {
    }

    private static Tag<DamageType> fetch(final TagKey<DamageType> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).getTag(tagKey);
    }
}
