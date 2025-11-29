package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.attribute.EnvironmentalAttributeType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#ENVIRONMENT_ATTRIBUTE}.
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
@GeneratedClass
public final class EnvironmentAttributeKeys {
    /**
     * {@code minecraft:audio/ambient_sounds}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> AUDIO_AMBIENT_SOUNDS = create(key("audio/ambient_sounds"));

    /**
     * {@code minecraft:audio/background_music}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> AUDIO_BACKGROUND_MUSIC = create(key("audio/background_music"));

    /**
     * {@code minecraft:audio/firefly_bush_sounds}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> AUDIO_FIREFLY_BUSH_SOUNDS = create(key("audio/firefly_bush_sounds"));

    /**
     * {@code minecraft:audio/music_volume}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> AUDIO_MUSIC_VOLUME = create(key("audio/music_volume"));

    /**
     * {@code minecraft:gameplay/baby_villager_activity}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_BABY_VILLAGER_ACTIVITY = create(key("gameplay/baby_villager_activity"));

    /**
     * {@code minecraft:gameplay/bed_rule}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_BED_RULE = create(key("gameplay/bed_rule"));

    /**
     * {@code minecraft:gameplay/bees_stay_in_hive}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_BEES_STAY_IN_HIVE = create(key("gameplay/bees_stay_in_hive"));

    /**
     * {@code minecraft:gameplay/can_pillager_patrol_spawn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_CAN_PILLAGER_PATROL_SPAWN = create(key("gameplay/can_pillager_patrol_spawn"));

    /**
     * {@code minecraft:gameplay/can_start_raid}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_CAN_START_RAID = create(key("gameplay/can_start_raid"));

    /**
     * {@code minecraft:gameplay/cat_waking_up_gift_chance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_CAT_WAKING_UP_GIFT_CHANCE = create(key("gameplay/cat_waking_up_gift_chance"));

    /**
     * {@code minecraft:gameplay/creaking_active}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_CREAKING_ACTIVE = create(key("gameplay/creaking_active"));

    /**
     * {@code minecraft:gameplay/eyeblossom_open}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_EYEBLOSSOM_OPEN = create(key("gameplay/eyeblossom_open"));

    /**
     * {@code minecraft:gameplay/fast_lava}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_FAST_LAVA = create(key("gameplay/fast_lava"));

    /**
     * {@code minecraft:gameplay/increased_fire_burnout}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_INCREASED_FIRE_BURNOUT = create(key("gameplay/increased_fire_burnout"));

    /**
     * {@code minecraft:gameplay/monsters_burn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_MONSTERS_BURN = create(key("gameplay/monsters_burn"));

    /**
     * {@code minecraft:gameplay/nether_portal_spawns_piglin}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN = create(key("gameplay/nether_portal_spawns_piglin"));

    /**
     * {@code minecraft:gameplay/piglins_zombify}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_PIGLINS_ZOMBIFY = create(key("gameplay/piglins_zombify"));

    /**
     * {@code minecraft:gameplay/respawn_anchor_works}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_RESPAWN_ANCHOR_WORKS = create(key("gameplay/respawn_anchor_works"));

    /**
     * {@code minecraft:gameplay/sky_light_level}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_SKY_LIGHT_LEVEL = create(key("gameplay/sky_light_level"));

    /**
     * {@code minecraft:gameplay/snow_golem_melts}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_SNOW_GOLEM_MELTS = create(key("gameplay/snow_golem_melts"));

    /**
     * {@code minecraft:gameplay/surface_slime_spawn_chance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_SURFACE_SLIME_SPAWN_CHANCE = create(key("gameplay/surface_slime_spawn_chance"));

    /**
     * {@code minecraft:gameplay/turtle_egg_hatch_chance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_TURTLE_EGG_HATCH_CHANCE = create(key("gameplay/turtle_egg_hatch_chance"));

    /**
     * {@code minecraft:gameplay/villager_activity}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_VILLAGER_ACTIVITY = create(key("gameplay/villager_activity"));

    /**
     * {@code minecraft:gameplay/water_evaporates}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> GAMEPLAY_WATER_EVAPORATES = create(key("gameplay/water_evaporates"));

    /**
     * {@code minecraft:visual/ambient_particles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_AMBIENT_PARTICLES = create(key("visual/ambient_particles"));

    /**
     * {@code minecraft:visual/cloud_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_CLOUD_COLOR = create(key("visual/cloud_color"));

    /**
     * {@code minecraft:visual/cloud_fog_end_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_CLOUD_FOG_END_DISTANCE = create(key("visual/cloud_fog_end_distance"));

    /**
     * {@code minecraft:visual/cloud_height}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_CLOUD_HEIGHT = create(key("visual/cloud_height"));

    /**
     * {@code minecraft:visual/default_dripstone_particle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_DEFAULT_DRIPSTONE_PARTICLE = create(key("visual/default_dripstone_particle"));

    /**
     * {@code minecraft:visual/fog_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_FOG_COLOR = create(key("visual/fog_color"));

    /**
     * {@code minecraft:visual/fog_end_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_FOG_END_DISTANCE = create(key("visual/fog_end_distance"));

    /**
     * {@code minecraft:visual/fog_start_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_FOG_START_DISTANCE = create(key("visual/fog_start_distance"));

    /**
     * {@code minecraft:visual/moon_angle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_MOON_ANGLE = create(key("visual/moon_angle"));

    /**
     * {@code minecraft:visual/moon_phase}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_MOON_PHASE = create(key("visual/moon_phase"));

    /**
     * {@code minecraft:visual/sky_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_SKY_COLOR = create(key("visual/sky_color"));

    /**
     * {@code minecraft:visual/sky_fog_end_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_SKY_FOG_END_DISTANCE = create(key("visual/sky_fog_end_distance"));

    /**
     * {@code minecraft:visual/sky_light_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_SKY_LIGHT_COLOR = create(key("visual/sky_light_color"));

    /**
     * {@code minecraft:visual/sky_light_factor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_SKY_LIGHT_FACTOR = create(key("visual/sky_light_factor"));

    /**
     * {@code minecraft:visual/star_angle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_STAR_ANGLE = create(key("visual/star_angle"));

    /**
     * {@code minecraft:visual/star_brightness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_STAR_BRIGHTNESS = create(key("visual/star_brightness"));

    /**
     * {@code minecraft:visual/sun_angle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_SUN_ANGLE = create(key("visual/sun_angle"));

    /**
     * {@code minecraft:visual/sunrise_sunset_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_SUNRISE_SUNSET_COLOR = create(key("visual/sunrise_sunset_color"));

    /**
     * {@code minecraft:visual/water_fog_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_WATER_FOG_COLOR = create(key("visual/water_fog_color"));

    /**
     * {@code minecraft:visual/water_fog_end_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_WATER_FOG_END_DISTANCE = create(key("visual/water_fog_end_distance"));

    /**
     * {@code minecraft:visual/water_fog_start_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<EnvironmentalAttributeType<?>> VISUAL_WATER_FOG_START_DISTANCE = create(key("visual/water_fog_start_distance"));

    private EnvironmentAttributeKeys() {
    }

    private static TypedKey<EnvironmentalAttributeType<?>> create(final Key key) {
        return TypedKey.create(RegistryKey.ENVIRONMENT_ATTRIBUTE, key);
    }
}
