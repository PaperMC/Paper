package org.bukkit.generator.structure;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * Represent a Structure from the world.
 *
 * Listed structures are present in the default server. Depending on the server
 * there might be additional structures present (for example structures added by
 * data packs), which can be received via {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} and {@link io.papermc.paper.registry.RegistryKey#STRUCTURE}.
 */
public abstract class Structure implements Keyed {

    // Start generate - Structure
    // @GeneratedFrom 1.21.5
    public static final Structure ANCIENT_CITY = getStructure("ancient_city");

    public static final Structure BASTION_REMNANT = getStructure("bastion_remnant");

    public static final Structure BURIED_TREASURE = getStructure("buried_treasure");

    public static final Structure DESERT_PYRAMID = getStructure("desert_pyramid");

    public static final Structure END_CITY = getStructure("end_city");

    public static final Structure FORTRESS = getStructure("fortress");

    public static final Structure IGLOO = getStructure("igloo");

    public static final Structure JUNGLE_PYRAMID = getStructure("jungle_pyramid");

    public static final Structure MANSION = getStructure("mansion");

    public static final Structure MINESHAFT = getStructure("mineshaft");

    public static final Structure MINESHAFT_MESA = getStructure("mineshaft_mesa");

    public static final Structure MONUMENT = getStructure("monument");

    public static final Structure NETHER_FOSSIL = getStructure("nether_fossil");

    public static final Structure OCEAN_RUIN_COLD = getStructure("ocean_ruin_cold");

    public static final Structure OCEAN_RUIN_WARM = getStructure("ocean_ruin_warm");

    public static final Structure PILLAGER_OUTPOST = getStructure("pillager_outpost");

    public static final Structure RUINED_PORTAL = getStructure("ruined_portal");

    public static final Structure RUINED_PORTAL_DESERT = getStructure("ruined_portal_desert");

    public static final Structure RUINED_PORTAL_JUNGLE = getStructure("ruined_portal_jungle");

    public static final Structure RUINED_PORTAL_MOUNTAIN = getStructure("ruined_portal_mountain");

    public static final Structure RUINED_PORTAL_NETHER = getStructure("ruined_portal_nether");

    public static final Structure RUINED_PORTAL_OCEAN = getStructure("ruined_portal_ocean");

    public static final Structure RUINED_PORTAL_SWAMP = getStructure("ruined_portal_swamp");

    public static final Structure SHIPWRECK = getStructure("shipwreck");

    public static final Structure SHIPWRECK_BEACHED = getStructure("shipwreck_beached");

    public static final Structure STRONGHOLD = getStructure("stronghold");

    public static final Structure SWAMP_HUT = getStructure("swamp_hut");

    public static final Structure TRAIL_RUINS = getStructure("trail_ruins");

    public static final Structure TRIAL_CHAMBERS = getStructure("trial_chambers");

    public static final Structure VILLAGE_DESERT = getStructure("village_desert");

    public static final Structure VILLAGE_PLAINS = getStructure("village_plains");

    public static final Structure VILLAGE_SAVANNA = getStructure("village_savanna");

    public static final Structure VILLAGE_SNOWY = getStructure("village_snowy");

    public static final Structure VILLAGE_TAIGA = getStructure("village_taiga");
    // End generate - Structure

    @NotNull
    private static Structure getStructure(@NotNull String name) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getOrThrow(NamespacedKey.minecraft(name));
    }

    /**
     * Returns the type of the structure.
     *
     * @return the type of structure
     */
    @NotNull
    public abstract StructureType getStructureType();
    // Paper start - deprecate getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#STRUCTURE}. Structures can exist without a key.
     */
    @Override
    @Deprecated(since = "1.20.4", forRemoval = true)
    public abstract @NotNull NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#STRUCTURE}. Structures can exist without a key.
     */
    @Override
    @Deprecated(since = "1.20.4", forRemoval = true)
    public net.kyori.adventure.key.@org.jetbrains.annotations.NotNull Key key() {
        return org.bukkit.Keyed.super.key();
    }

    // Paper end - deprecate getKey
}
