package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.generator.structure.Structure;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#STRUCTURE}.
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
public final class StructureTagKeys {
    /**
     * {@code #minecraft:cats_spawn_as_black}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> CATS_SPAWN_AS_BLACK = create(key("cats_spawn_as_black"));

    /**
     * {@code #minecraft:cats_spawn_in}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> CATS_SPAWN_IN = create(key("cats_spawn_in"));

    /**
     * {@code #minecraft:dolphin_located}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> DOLPHIN_LOCATED = create(key("dolphin_located"));

    /**
     * {@code #minecraft:eye_of_ender_located}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> EYE_OF_ENDER_LOCATED = create(key("eye_of_ender_located"));

    /**
     * {@code #minecraft:mineshaft}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> MINESHAFT = create(key("mineshaft"));

    /**
     * {@code #minecraft:ocean_ruin}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> OCEAN_RUIN = create(key("ocean_ruin"));

    /**
     * {@code #minecraft:on_desert_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_DESERT_VILLAGE_MAPS = create(key("on_desert_village_maps"));

    /**
     * {@code #minecraft:on_jungle_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_JUNGLE_EXPLORER_MAPS = create(key("on_jungle_explorer_maps"));

    /**
     * {@code #minecraft:on_ocean_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_OCEAN_EXPLORER_MAPS = create(key("on_ocean_explorer_maps"));

    /**
     * {@code #minecraft:on_plains_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_PLAINS_VILLAGE_MAPS = create(key("on_plains_village_maps"));

    /**
     * {@code #minecraft:on_savanna_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_SAVANNA_VILLAGE_MAPS = create(key("on_savanna_village_maps"));

    /**
     * {@code #minecraft:on_snowy_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_SNOWY_VILLAGE_MAPS = create(key("on_snowy_village_maps"));

    /**
     * {@code #minecraft:on_swamp_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_SWAMP_EXPLORER_MAPS = create(key("on_swamp_explorer_maps"));

    /**
     * {@code #minecraft:on_taiga_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_TAIGA_VILLAGE_MAPS = create(key("on_taiga_village_maps"));

    /**
     * {@code #minecraft:on_treasure_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_TREASURE_MAPS = create(key("on_treasure_maps"));

    /**
     * {@code #minecraft:on_trial_chambers_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_TRIAL_CHAMBERS_MAPS = create(key("on_trial_chambers_maps"));

    /**
     * {@code #minecraft:on_woodland_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> ON_WOODLAND_EXPLORER_MAPS = create(key("on_woodland_explorer_maps"));

    /**
     * {@code #minecraft:ruined_portal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> RUINED_PORTAL = create(key("ruined_portal"));

    /**
     * {@code #minecraft:shipwreck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> SHIPWRECK = create(key("shipwreck"));

    /**
     * {@code #minecraft:village}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Structure> VILLAGE = create(key("village"));

    private StructureTagKeys() {
    }

    /**
     * Creates a tag key for {@link Structure} in the registry {@code minecraft:worldgen/structure}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<Structure> create(final Key key) {
        return TagKey.create(RegistryKey.STRUCTURE, key);
    }
}
