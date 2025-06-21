package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.StructureTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.generator.structure.Structure;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#STRUCTURE}.
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
public final class StructureTags {
    /**
     * {@code #minecraft:cats_spawn_as_black}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> CATS_SPAWN_AS_BLACK = fetch(StructureTagKeys.CATS_SPAWN_AS_BLACK);

    /**
     * {@code #minecraft:cats_spawn_in}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> CATS_SPAWN_IN = fetch(StructureTagKeys.CATS_SPAWN_IN);

    /**
     * {@code #minecraft:dolphin_located}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> DOLPHIN_LOCATED = fetch(StructureTagKeys.DOLPHIN_LOCATED);

    /**
     * {@code #minecraft:eye_of_ender_located}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> EYE_OF_ENDER_LOCATED = fetch(StructureTagKeys.EYE_OF_ENDER_LOCATED);

    /**
     * {@code #minecraft:mineshaft}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> MINESHAFT = fetch(StructureTagKeys.MINESHAFT);

    /**
     * {@code #minecraft:ocean_ruin}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> OCEAN_RUIN = fetch(StructureTagKeys.OCEAN_RUIN);

    /**
     * {@code #minecraft:on_desert_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_DESERT_VILLAGE_MAPS = fetch(StructureTagKeys.ON_DESERT_VILLAGE_MAPS);

    /**
     * {@code #minecraft:on_jungle_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_JUNGLE_EXPLORER_MAPS = fetch(StructureTagKeys.ON_JUNGLE_EXPLORER_MAPS);

    /**
     * {@code #minecraft:on_ocean_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_OCEAN_EXPLORER_MAPS = fetch(StructureTagKeys.ON_OCEAN_EXPLORER_MAPS);

    /**
     * {@code #minecraft:on_plains_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_PLAINS_VILLAGE_MAPS = fetch(StructureTagKeys.ON_PLAINS_VILLAGE_MAPS);

    /**
     * {@code #minecraft:on_savanna_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_SAVANNA_VILLAGE_MAPS = fetch(StructureTagKeys.ON_SAVANNA_VILLAGE_MAPS);

    /**
     * {@code #minecraft:on_snowy_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_SNOWY_VILLAGE_MAPS = fetch(StructureTagKeys.ON_SNOWY_VILLAGE_MAPS);

    /**
     * {@code #minecraft:on_swamp_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_SWAMP_EXPLORER_MAPS = fetch(StructureTagKeys.ON_SWAMP_EXPLORER_MAPS);

    /**
     * {@code #minecraft:on_taiga_village_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_TAIGA_VILLAGE_MAPS = fetch(StructureTagKeys.ON_TAIGA_VILLAGE_MAPS);

    /**
     * {@code #minecraft:on_treasure_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_TREASURE_MAPS = fetch(StructureTagKeys.ON_TREASURE_MAPS);

    /**
     * {@code #minecraft:on_trial_chambers_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_TRIAL_CHAMBERS_MAPS = fetch(StructureTagKeys.ON_TRIAL_CHAMBERS_MAPS);

    /**
     * {@code #minecraft:on_woodland_explorer_maps}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> ON_WOODLAND_EXPLORER_MAPS = fetch(StructureTagKeys.ON_WOODLAND_EXPLORER_MAPS);

    /**
     * {@code #minecraft:ruined_portal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> RUINED_PORTAL = fetch(StructureTagKeys.RUINED_PORTAL);

    /**
     * {@code #minecraft:shipwreck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> SHIPWRECK = fetch(StructureTagKeys.SHIPWRECK);

    /**
     * {@code #minecraft:village}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Structure> VILLAGE = fetch(StructureTagKeys.VILLAGE);

    private StructureTags() {
    }

    private static Tag<Structure> fetch(final TagKey<Structure> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getTag(tagKey);
    }
}
