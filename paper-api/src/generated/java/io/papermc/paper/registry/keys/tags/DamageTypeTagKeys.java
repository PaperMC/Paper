package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#DAMAGE_TYPE}.
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
@GeneratedFrom("1.21.6-pre1")
@ApiStatus.Experimental
public final class DamageTypeTagKeys {
    /**
     * {@code #minecraft:always_hurts_ender_dragons}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> ALWAYS_HURTS_ENDER_DRAGONS = create(key("always_hurts_ender_dragons"));

    /**
     * {@code #minecraft:always_kills_armor_stands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> ALWAYS_KILLS_ARMOR_STANDS = create(key("always_kills_armor_stands"));

    /**
     * {@code #minecraft:always_most_significant_fall}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> ALWAYS_MOST_SIGNIFICANT_FALL = create(key("always_most_significant_fall"));

    /**
     * {@code #minecraft:always_triggers_silverfish}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> ALWAYS_TRIGGERS_SILVERFISH = create(key("always_triggers_silverfish"));

    /**
     * {@code #minecraft:avoids_guardian_thorns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> AVOIDS_GUARDIAN_THORNS = create(key("avoids_guardian_thorns"));

    /**
     * {@code #minecraft:burn_from_stepping}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BURN_FROM_STEPPING = create(key("burn_from_stepping"));

    /**
     * {@code #minecraft:burns_armor_stands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BURNS_ARMOR_STANDS = create(key("burns_armor_stands"));

    /**
     * {@code #minecraft:bypasses_armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_ARMOR = create(key("bypasses_armor"));

    /**
     * {@code #minecraft:bypasses_effects}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_EFFECTS = create(key("bypasses_effects"));

    /**
     * {@code #minecraft:bypasses_enchantments}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_ENCHANTMENTS = create(key("bypasses_enchantments"));

    /**
     * {@code #minecraft:bypasses_invulnerability}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_INVULNERABILITY = create(key("bypasses_invulnerability"));

    /**
     * {@code #minecraft:bypasses_resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_RESISTANCE = create(key("bypasses_resistance"));

    /**
     * {@code #minecraft:bypasses_shield}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_SHIELD = create(key("bypasses_shield"));

    /**
     * {@code #minecraft:bypasses_wolf_armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> BYPASSES_WOLF_ARMOR = create(key("bypasses_wolf_armor"));

    /**
     * {@code #minecraft:can_break_armor_stand}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> CAN_BREAK_ARMOR_STAND = create(key("can_break_armor_stand"));

    /**
     * {@code #minecraft:damages_helmet}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> DAMAGES_HELMET = create(key("damages_helmet"));

    /**
     * {@code #minecraft:ignites_armor_stands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IGNITES_ARMOR_STANDS = create(key("ignites_armor_stands"));

    /**
     * {@code #minecraft:is_drowning}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_DROWNING = create(key("is_drowning"));

    /**
     * {@code #minecraft:is_explosion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_EXPLOSION = create(key("is_explosion"));

    /**
     * {@code #minecraft:is_fall}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_FALL = create(key("is_fall"));

    /**
     * {@code #minecraft:is_fire}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_FIRE = create(key("is_fire"));

    /**
     * {@code #minecraft:is_freezing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_FREEZING = create(key("is_freezing"));

    /**
     * {@code #minecraft:is_lightning}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_LIGHTNING = create(key("is_lightning"));

    /**
     * {@code #minecraft:is_player_attack}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_PLAYER_ATTACK = create(key("is_player_attack"));

    /**
     * {@code #minecraft:is_projectile}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> IS_PROJECTILE = create(key("is_projectile"));

    /**
     * {@code #minecraft:mace_smash}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> MACE_SMASH = create(key("mace_smash"));

    /**
     * {@code #minecraft:no_anger}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> NO_ANGER = create(key("no_anger"));

    /**
     * {@code #minecraft:no_impact}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> NO_IMPACT = create(key("no_impact"));

    /**
     * {@code #minecraft:no_knockback}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> NO_KNOCKBACK = create(key("no_knockback"));

    /**
     * {@code #minecraft:panic_causes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> PANIC_CAUSES = create(key("panic_causes"));

    /**
     * {@code #minecraft:panic_environmental_causes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> PANIC_ENVIRONMENTAL_CAUSES = create(key("panic_environmental_causes"));

    /**
     * {@code #minecraft:witch_resistant_to}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> WITCH_RESISTANT_TO = create(key("witch_resistant_to"));

    /**
     * {@code #minecraft:wither_immune_to}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<DamageType> WITHER_IMMUNE_TO = create(key("wither_immune_to"));

    private DamageTypeTagKeys() {
    }

    /**
     * Creates a tag key for {@link DamageType} in the registry {@code minecraft:damage_type}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<DamageType> create(final Key key) {
        return TagKey.create(RegistryKey.DAMAGE_TYPE, key);
    }
}
