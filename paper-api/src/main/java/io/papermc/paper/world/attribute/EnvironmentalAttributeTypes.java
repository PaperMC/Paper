package io.papermc.paper.world.attribute;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EnvironmentalAttributeTypes {

    // ================
    //      AUDIO
    // ================

    // public static final EnvironmentalAttributeType<AmbientSounds> AMBIENT_SOUNDS = get("audio/ambient_sounds");

    // public static final EnvironmentalAttributeType<BackgroundMusic> BACKGROUND_MUSIC = get("audio/background_music");

    public static final EnvironmentalAttributeType<Boolean> FIREFLY_BUSH_SOUNDS = get("audio/firefly_bush_sounds");

    public static final EnvironmentalAttributeType<Float> MUSIC_VOLUME = get("audio/music_volume");

    // ================
    //     GAMEPLAY
    // ================

    // public static final EnvironmentalAttributeType<Activity> BABY_VILLAGER_ACTIVITY = get("gameplay/baby_villager_activity");

    // public static final EnvironmentalAttributeType<BedRule> BED_RULE = get("gameplay/bed_rule");

    public static final EnvironmentalAttributeType<Boolean> BEES_STAY_IN_HIVE = get("gameplay/bees_stay_in_hive");

    public static final EnvironmentalAttributeType<Boolean> CAN_PILLAGER_PATROL_SPAWN = get("gameplay/can_pillager_patrol_spawn");

    public static final EnvironmentalAttributeType<Boolean> CAN_START_RAID = get("gameplay/can_start_raid");

    public static final EnvironmentalAttributeType<Float> CAT_WAKING_UP_GIFT_CHANCE = get("gameplay/cat_waking_up_gift_chance");

    public static final EnvironmentalAttributeType<Boolean> CREAKING_ACTIVE = get("gameplay/creaking_active");

    public static final EnvironmentalAttributeType<TriState> EYEBLOSSOM_OPEN = get("gameplay/eyeblossom_open");

    public static final EnvironmentalAttributeType<Boolean> FAST_LAVA = get("gameplay/fast_lava");

    public static final EnvironmentalAttributeType<Boolean> INCREASED_FIRE_BURNOUT = get("gameplay/increased_fire_burnout");

    public static final EnvironmentalAttributeType<Boolean> MONSTERS_BURN = get("gameplay/monsters_burn");

    public static final EnvironmentalAttributeType<Boolean> NETHER_PORTAL_SPAWNS_PIGLIN = get("gameplay/nether_portal_spawns_piglin");

    public static final EnvironmentalAttributeType<Boolean> PIGLINS_ZOMBIFY = get("gameplay/piglins_zombify");

    public static final EnvironmentalAttributeType<Boolean> RESPAWN_ANCHOR_WORKS = get("gameplay/respawn_anchor_works");

    public static final EnvironmentalAttributeType<Float> SKY_LIGHT_LEVEL = get("gameplay/sky_light_level");

    public static final EnvironmentalAttributeType<Boolean> SNOW_GOLEM_MELTS = get("gameplay/snow_golem_melts");

    public static final EnvironmentalAttributeType<Float> SURFACE_SLIME_SPAWN_CHANCE = get("gameplay/surface_slime_spawn_chance");

    public static final EnvironmentalAttributeType<Float> TURTLE_EGG_HATCH_CHANCE = get("gameplay/turtle_egg_hatch_chance");

    // public static final EnvironmentalAttributeType<Activity> VILLAGER_ACTIVITY = get("gameplay/villager_activity");

    public static final EnvironmentalAttributeType<Boolean> WATER_EVAPORATES = get("gameplay/water_evaporates");

    // ================
    //      VISUAL
    // ================

    // public static final EnvironmentalAttributeType<List<AmbientParticle>> AMBIENT_PARTICLES = get("visual/ambient_particles");

    public static final EnvironmentalAttributeType<Integer> CLOUD_COLOR = get("visual/cloud_color");

    public static final EnvironmentalAttributeType<Float> CLOUD_FOG_END_DISTANCE = get("visual/cloud_fog_end_distance");

    public static final EnvironmentalAttributeType<Float> CLOUD_HEIGHT = get("visual/cloud_height");

    // public static final EnvironmentalAttributeType<ParticleOptions> DEFAULT_DRIPSTONE_PARTICLE = get("visual/default_dripstone_particle");

    public static final EnvironmentalAttributeType<Integer> FOG_COLOR = get("visual/fog_color");

    public static final EnvironmentalAttributeType<Float> FOG_END_DISTANCE = get("visual/fog_end_distance");

    public static final EnvironmentalAttributeType<Float> FOG_START_DISTANCE = get("visual/fog_start_distance");

    public static final EnvironmentalAttributeType<Float> MOON_ANGLE = get("visual/moon_angle");

    public static final EnvironmentalAttributeType<MoonPhase> MOON_PHASE = get("visual/moon_phase");

    public static final EnvironmentalAttributeType<Integer> SKY_COLOR = get("visual/sky_color");

    public static final EnvironmentalAttributeType<Float> SKY_FOG_END_DISTANCE = get("visual/sky_fog_end_distance");

    public static final EnvironmentalAttributeType<Integer> SKY_LIGHT_COLOR = get("visual/sky_light_color");

    public static final EnvironmentalAttributeType<Float> SKY_LIGHT_FACTOR = get("visual/sky_light_factor");

    public static final EnvironmentalAttributeType<Float> STAR_ANGLE = get("visual/star_angle");

    public static final EnvironmentalAttributeType<Float> STAR_BRIGHTNESS = get("visual/star_brightness");

    public static final EnvironmentalAttributeType<Float> SUN_ANGLE = get("visual/sun_angle");

    public static final EnvironmentalAttributeType<Integer> SUNRISE_SUNSET_COLOR = get("visual/sunrise_sunset_color");

    public static final EnvironmentalAttributeType<Integer> WATER_FOG_COLOR = get("visual/water_fog_color");

    public static final EnvironmentalAttributeType<Float> WATER_FOG_END_DISTANCE = get("visual/water_fog_end_distance");

    public static final EnvironmentalAttributeType<Float> WATER_FOG_START_DISTANCE = get("visual/water_fog_start_distance");

    private EnvironmentalAttributeTypes() {
    }

    @SuppressWarnings("unchecked")
    private static <T> EnvironmentalAttributeType<T> get(final String key) {
        return (EnvironmentalAttributeType<T>) RegistryAccess.registryAccess().getRegistry(RegistryKey.ENVIRONMENT_ATTRIBUTE).getOrThrow(Key.key(key));
    }
}
