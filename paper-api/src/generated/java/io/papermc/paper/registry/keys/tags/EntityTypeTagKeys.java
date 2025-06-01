package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#ENTITY_TYPE}.
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
public final class EntityTypeTagKeys {
    /**
     * {@code #minecraft:aquatic}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> AQUATIC = create(key("aquatic"));

    /**
     * {@code #minecraft:arrows}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> ARROWS = create(key("arrows"));

    /**
     * {@code #minecraft:arthropod}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> ARTHROPOD = create(key("arthropod"));

    /**
     * {@code #minecraft:axolotl_always_hostiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> AXOLOTL_ALWAYS_HOSTILES = create(key("axolotl_always_hostiles"));

    /**
     * {@code #minecraft:axolotl_hunt_targets}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> AXOLOTL_HUNT_TARGETS = create(key("axolotl_hunt_targets"));

    /**
     * {@code #minecraft:beehive_inhabitors}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> BEEHIVE_INHABITORS = create(key("beehive_inhabitors"));

    /**
     * {@code #minecraft:boat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> BOAT = create(key("boat"));

    /**
     * {@code #minecraft:can_breathe_under_water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> CAN_BREATHE_UNDER_WATER = create(key("can_breathe_under_water"));

    /**
     * {@code #minecraft:can_equip_harness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> CAN_EQUIP_HARNESS = create(key("can_equip_harness"));

    /**
     * {@code #minecraft:can_equip_saddle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> CAN_EQUIP_SADDLE = create(key("can_equip_saddle"));

    /**
     * {@code #minecraft:can_turn_in_boats}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> CAN_TURN_IN_BOATS = create(key("can_turn_in_boats"));

    /**
     * {@code #minecraft:can_wear_horse_armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> CAN_WEAR_HORSE_ARMOR = create(key("can_wear_horse_armor"));

    /**
     * {@code #minecraft:deflects_projectiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> DEFLECTS_PROJECTILES = create(key("deflects_projectiles"));

    /**
     * {@code #minecraft:dismounts_underwater}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> DISMOUNTS_UNDERWATER = create(key("dismounts_underwater"));

    /**
     * {@code #minecraft:fall_damage_immune}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> FALL_DAMAGE_IMMUNE = create(key("fall_damage_immune"));

    /**
     * {@code #minecraft:followable_friendly_mobs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> FOLLOWABLE_FRIENDLY_MOBS = create(key("followable_friendly_mobs"));

    /**
     * {@code #minecraft:freeze_hurts_extra_types}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> FREEZE_HURTS_EXTRA_TYPES = create(key("freeze_hurts_extra_types"));

    /**
     * {@code #minecraft:freeze_immune_entity_types}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> FREEZE_IMMUNE_ENTITY_TYPES = create(key("freeze_immune_entity_types"));

    /**
     * {@code #minecraft:frog_food}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> FROG_FOOD = create(key("frog_food"));

    /**
     * {@code #minecraft:ignores_poison_and_regen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> IGNORES_POISON_AND_REGEN = create(key("ignores_poison_and_regen"));

    /**
     * {@code #minecraft:illager}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> ILLAGER = create(key("illager"));

    /**
     * {@code #minecraft:illager_friends}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> ILLAGER_FRIENDS = create(key("illager_friends"));

    /**
     * {@code #minecraft:immune_to_infested}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> IMMUNE_TO_INFESTED = create(key("immune_to_infested"));

    /**
     * {@code #minecraft:immune_to_oozing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> IMMUNE_TO_OOZING = create(key("immune_to_oozing"));

    /**
     * {@code #minecraft:impact_projectiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> IMPACT_PROJECTILES = create(key("impact_projectiles"));

    /**
     * {@code #minecraft:inverted_healing_and_harm}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> INVERTED_HEALING_AND_HARM = create(key("inverted_healing_and_harm"));

    /**
     * {@code #minecraft:no_anger_from_wind_charge}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> NO_ANGER_FROM_WIND_CHARGE = create(key("no_anger_from_wind_charge"));

    /**
     * {@code #minecraft:non_controlling_rider}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> NON_CONTROLLING_RIDER = create(key("non_controlling_rider"));

    /**
     * {@code #minecraft:not_scary_for_pufferfish}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> NOT_SCARY_FOR_PUFFERFISH = create(key("not_scary_for_pufferfish"));

    /**
     * {@code #minecraft:powder_snow_walkable_mobs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> POWDER_SNOW_WALKABLE_MOBS = create(key("powder_snow_walkable_mobs"));

    /**
     * {@code #minecraft:raiders}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> RAIDERS = create(key("raiders"));

    /**
     * {@code #minecraft:redirectable_projectile}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> REDIRECTABLE_PROJECTILE = create(key("redirectable_projectile"));

    /**
     * {@code #minecraft:sensitive_to_bane_of_arthropods}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> SENSITIVE_TO_BANE_OF_ARTHROPODS = create(key("sensitive_to_bane_of_arthropods"));

    /**
     * {@code #minecraft:sensitive_to_impaling}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> SENSITIVE_TO_IMPALING = create(key("sensitive_to_impaling"));

    /**
     * {@code #minecraft:sensitive_to_smite}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> SENSITIVE_TO_SMITE = create(key("sensitive_to_smite"));

    /**
     * {@code #minecraft:skeletons}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> SKELETONS = create(key("skeletons"));

    /**
     * {@code #minecraft:undead}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> UNDEAD = create(key("undead"));

    /**
     * {@code #minecraft:wither_friends}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> WITHER_FRIENDS = create(key("wither_friends"));

    /**
     * {@code #minecraft:zombies}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<EntityType> ZOMBIES = create(key("zombies"));

    private EntityTypeTagKeys() {
    }

    /**
     * Creates a tag key for {@link EntityType} in the registry {@code minecraft:entity_type}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<EntityType> create(final Key key) {
        return TagKey.create(RegistryKey.ENTITY_TYPE, key);
    }
}
