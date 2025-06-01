package org.bukkit.generator.structure;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * Represent a StructureType of a {@link Structure}.
 *
 * Listed structure types are present in the default server. Depending on the
 * server there might be additional structure types present (for example
 * structure types added by data packs), which can be received via
 * {@link Registry#STRUCTURE_TYPE}.
 */
public abstract class StructureType implements Keyed {

    // Start generate - StructureType
    // @GeneratedFrom 1.21.6-pre1
    public static final StructureType BURIED_TREASURE = getStructureType("buried_treasure");

    public static final StructureType DESERT_PYRAMID = getStructureType("desert_pyramid");

    public static final StructureType END_CITY = getStructureType("end_city");

    public static final StructureType FORTRESS = getStructureType("fortress");

    public static final StructureType IGLOO = getStructureType("igloo");

    public static final StructureType JIGSAW = getStructureType("jigsaw");

    public static final StructureType JUNGLE_TEMPLE = getStructureType("jungle_temple");

    public static final StructureType MINESHAFT = getStructureType("mineshaft");

    public static final StructureType NETHER_FOSSIL = getStructureType("nether_fossil");

    public static final StructureType OCEAN_MONUMENT = getStructureType("ocean_monument");

    public static final StructureType OCEAN_RUIN = getStructureType("ocean_ruin");

    public static final StructureType RUINED_PORTAL = getStructureType("ruined_portal");

    public static final StructureType SHIPWRECK = getStructureType("shipwreck");

    public static final StructureType STRONGHOLD = getStructureType("stronghold");

    public static final StructureType SWAMP_HUT = getStructureType("swamp_hut");

    public static final StructureType WOODLAND_MANSION = getStructureType("woodland_mansion");
    // End generate - StructureType

    @NotNull
    private static StructureType getStructureType(@NotNull String name) {
        return Registry.STRUCTURE_TYPE.getOrThrow(NamespacedKey.minecraft(name));
    }
}
