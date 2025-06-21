package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.EntityTypeTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#ENTITY_TYPE}.
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
public final class EntityTypeTags {
    /**
     * {@code #minecraft:aquatic}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> AQUATIC = fetch(EntityTypeTagKeys.AQUATIC);

    /**
     * {@code #minecraft:arrows}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> ARROWS = fetch(EntityTypeTagKeys.ARROWS);

    /**
     * {@code #minecraft:arthropod}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> ARTHROPOD = fetch(EntityTypeTagKeys.ARTHROPOD);

    /**
     * {@code #minecraft:axolotl_always_hostiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> AXOLOTL_ALWAYS_HOSTILES = fetch(EntityTypeTagKeys.AXOLOTL_ALWAYS_HOSTILES);

    /**
     * {@code #minecraft:axolotl_hunt_targets}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> AXOLOTL_HUNT_TARGETS = fetch(EntityTypeTagKeys.AXOLOTL_HUNT_TARGETS);

    /**
     * {@code #minecraft:beehive_inhabitors}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> BEEHIVE_INHABITORS = fetch(EntityTypeTagKeys.BEEHIVE_INHABITORS);

    /**
     * {@code #minecraft:boat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> BOAT = fetch(EntityTypeTagKeys.BOAT);

    /**
     * {@code #minecraft:can_breathe_under_water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> CAN_BREATHE_UNDER_WATER = fetch(EntityTypeTagKeys.CAN_BREATHE_UNDER_WATER);

    /**
     * {@code #minecraft:can_equip_harness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> CAN_EQUIP_HARNESS = fetch(EntityTypeTagKeys.CAN_EQUIP_HARNESS);

    /**
     * {@code #minecraft:can_equip_saddle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> CAN_EQUIP_SADDLE = fetch(EntityTypeTagKeys.CAN_EQUIP_SADDLE);

    /**
     * {@code #minecraft:can_turn_in_boats}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> CAN_TURN_IN_BOATS = fetch(EntityTypeTagKeys.CAN_TURN_IN_BOATS);

    /**
     * {@code #minecraft:can_wear_horse_armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> CAN_WEAR_HORSE_ARMOR = fetch(EntityTypeTagKeys.CAN_WEAR_HORSE_ARMOR);

    /**
     * {@code #minecraft:deflects_projectiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> DEFLECTS_PROJECTILES = fetch(EntityTypeTagKeys.DEFLECTS_PROJECTILES);

    /**
     * {@code #minecraft:dismounts_underwater}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> DISMOUNTS_UNDERWATER = fetch(EntityTypeTagKeys.DISMOUNTS_UNDERWATER);

    /**
     * {@code #minecraft:fall_damage_immune}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> FALL_DAMAGE_IMMUNE = fetch(EntityTypeTagKeys.FALL_DAMAGE_IMMUNE);

    /**
     * {@code #minecraft:followable_friendly_mobs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> FOLLOWABLE_FRIENDLY_MOBS = fetch(EntityTypeTagKeys.FOLLOWABLE_FRIENDLY_MOBS);

    /**
     * {@code #minecraft:freeze_hurts_extra_types}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> FREEZE_HURTS_EXTRA_TYPES = fetch(EntityTypeTagKeys.FREEZE_HURTS_EXTRA_TYPES);

    /**
     * {@code #minecraft:freeze_immune_entity_types}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> FREEZE_IMMUNE_ENTITY_TYPES = fetch(EntityTypeTagKeys.FREEZE_IMMUNE_ENTITY_TYPES);

    /**
     * {@code #minecraft:frog_food}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> FROG_FOOD = fetch(EntityTypeTagKeys.FROG_FOOD);

    /**
     * {@code #minecraft:ignores_poison_and_regen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> IGNORES_POISON_AND_REGEN = fetch(EntityTypeTagKeys.IGNORES_POISON_AND_REGEN);

    /**
     * {@code #minecraft:illager}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> ILLAGER = fetch(EntityTypeTagKeys.ILLAGER);

    /**
     * {@code #minecraft:illager_friends}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> ILLAGER_FRIENDS = fetch(EntityTypeTagKeys.ILLAGER_FRIENDS);

    /**
     * {@code #minecraft:immune_to_infested}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> IMMUNE_TO_INFESTED = fetch(EntityTypeTagKeys.IMMUNE_TO_INFESTED);

    /**
     * {@code #minecraft:immune_to_oozing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> IMMUNE_TO_OOZING = fetch(EntityTypeTagKeys.IMMUNE_TO_OOZING);

    /**
     * {@code #minecraft:impact_projectiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> IMPACT_PROJECTILES = fetch(EntityTypeTagKeys.IMPACT_PROJECTILES);

    /**
     * {@code #minecraft:inverted_healing_and_harm}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> INVERTED_HEALING_AND_HARM = fetch(EntityTypeTagKeys.INVERTED_HEALING_AND_HARM);

    /**
     * {@code #minecraft:no_anger_from_wind_charge}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> NO_ANGER_FROM_WIND_CHARGE = fetch(EntityTypeTagKeys.NO_ANGER_FROM_WIND_CHARGE);

    /**
     * {@code #minecraft:non_controlling_rider}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> NON_CONTROLLING_RIDER = fetch(EntityTypeTagKeys.NON_CONTROLLING_RIDER);

    /**
     * {@code #minecraft:not_scary_for_pufferfish}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> NOT_SCARY_FOR_PUFFERFISH = fetch(EntityTypeTagKeys.NOT_SCARY_FOR_PUFFERFISH);

    /**
     * {@code #minecraft:powder_snow_walkable_mobs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> POWDER_SNOW_WALKABLE_MOBS = fetch(EntityTypeTagKeys.POWDER_SNOW_WALKABLE_MOBS);

    /**
     * {@code #minecraft:raiders}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> RAIDERS = fetch(EntityTypeTagKeys.RAIDERS);

    /**
     * {@code #minecraft:redirectable_projectile}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> REDIRECTABLE_PROJECTILE = fetch(EntityTypeTagKeys.REDIRECTABLE_PROJECTILE);

    /**
     * {@code #minecraft:sensitive_to_bane_of_arthropods}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> SENSITIVE_TO_BANE_OF_ARTHROPODS = fetch(EntityTypeTagKeys.SENSITIVE_TO_BANE_OF_ARTHROPODS);

    /**
     * {@code #minecraft:sensitive_to_impaling}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> SENSITIVE_TO_IMPALING = fetch(EntityTypeTagKeys.SENSITIVE_TO_IMPALING);

    /**
     * {@code #minecraft:sensitive_to_smite}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> SENSITIVE_TO_SMITE = fetch(EntityTypeTagKeys.SENSITIVE_TO_SMITE);

    /**
     * {@code #minecraft:skeletons}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> SKELETONS = fetch(EntityTypeTagKeys.SKELETONS);

    /**
     * {@code #minecraft:undead}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> UNDEAD = fetch(EntityTypeTagKeys.UNDEAD);

    /**
     * {@code #minecraft:wither_friends}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> WITHER_FRIENDS = fetch(EntityTypeTagKeys.WITHER_FRIENDS);

    /**
     * {@code #minecraft:zombies}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<EntityType> ZOMBIES = fetch(EntityTypeTagKeys.ZOMBIES);

    private EntityTypeTags() {
    }

    private static Tag<EntityType> fetch(final TagKey<EntityType> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENTITY_TYPE).getTag(tagKey);
    }
}
