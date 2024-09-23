package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.entity.ai.SensorType;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

/**
 * Vanilla keys for {@link RegistryKey#SENSOR_TYPE}.
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
@GeneratedFrom("1.21.1")
@ApiStatus.Experimental
public final class SensorTypeKeys {
    /**
     * {@code minecraft:armadillo_scare_detected}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> ARMADILLO_SCARE_DETECTED = create(key("armadillo_scare_detected"));

    /**
     * {@code minecraft:armadillo_temptations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> ARMADILLO_TEMPTATIONS = create(key("armadillo_temptations"));

    /**
     * {@code minecraft:axolotl_attackables}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> AXOLOTL_ATTACKABLES = create(key("axolotl_attackables"));

    /**
     * {@code minecraft:axolotl_temptations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> AXOLOTL_TEMPTATIONS = create(key("axolotl_temptations"));

    /**
     * {@code minecraft:breeze_attack_entity_sensor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> BREEZE_ATTACK_ENTITY_SENSOR = create(key("breeze_attack_entity_sensor"));

    /**
     * {@code minecraft:camel_temptations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> CAMEL_TEMPTATIONS = create(key("camel_temptations"));

    /**
     * {@code minecraft:dummy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> DUMMY = create(key("dummy"));

    /**
     * {@code minecraft:frog_attackables}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> FROG_ATTACKABLES = create(key("frog_attackables"));

    /**
     * {@code minecraft:frog_temptations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> FROG_TEMPTATIONS = create(key("frog_temptations"));

    /**
     * {@code minecraft:goat_temptations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> GOAT_TEMPTATIONS = create(key("goat_temptations"));

    /**
     * {@code minecraft:golem_detected}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> GOLEM_DETECTED = create(key("golem_detected"));

    /**
     * {@code minecraft:hoglin_specific_sensor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> HOGLIN_SPECIFIC_SENSOR = create(key("hoglin_specific_sensor"));

    /**
     * {@code minecraft:hurt_by}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> HURT_BY = create(key("hurt_by"));

    /**
     * {@code minecraft:is_in_water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> IS_IN_WATER = create(key("is_in_water"));

    /**
     * {@code minecraft:nearest_adult}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> NEAREST_ADULT = create(key("nearest_adult"));

    /**
     * {@code minecraft:nearest_bed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> NEAREST_BED = create(key("nearest_bed"));

    /**
     * {@code minecraft:nearest_items}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> NEAREST_ITEMS = create(key("nearest_items"));

    /**
     * {@code minecraft:nearest_living_entities}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> NEAREST_LIVING_ENTITIES = create(key("nearest_living_entities"));

    /**
     * {@code minecraft:nearest_players}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> NEAREST_PLAYERS = create(key("nearest_players"));

    /**
     * {@code minecraft:piglin_brute_specific_sensor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> PIGLIN_BRUTE_SPECIFIC_SENSOR = create(key("piglin_brute_specific_sensor"));

    /**
     * {@code minecraft:piglin_specific_sensor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> PIGLIN_SPECIFIC_SENSOR = create(key("piglin_specific_sensor"));

    /**
     * {@code minecraft:secondary_pois}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> SECONDARY_POIS = create(key("secondary_pois"));

    /**
     * {@code minecraft:sniffer_temptations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> SNIFFER_TEMPTATIONS = create(key("sniffer_temptations"));

    /**
     * {@code minecraft:villager_babies}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> VILLAGER_BABIES = create(key("villager_babies"));

    /**
     * {@code minecraft:villager_hostiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> VILLAGER_HOSTILES = create(key("villager_hostiles"));

    /**
     * {@code minecraft:warden_entity_sensor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SensorType> WARDEN_ENTITY_SENSOR = create(key("warden_entity_sensor"));

    private SensorTypeKeys() {
    }

    private static @NonNull TypedKey<SensorType> create(final @NonNull Key key) {
        return TypedKey.create(RegistryKey.SENSOR_TYPE, key);
    }
}
