package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.map.MapCursor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class handles the creation and storage of all structure types for
 * Bukkit. Structure Types are the different kinds of structures that can be
 * generated during world/chunk generation. These include Villages, Mineshafts,
 * Mansions, etc.
 * <br>
 * The registration of new {@link StructureType}s is case-sensitive.
 *
 * @deprecated This class does not represent the structures of a world well. Use
 * {@link org.bukkit.generator.structure.Structure} or
 * {@link org.bukkit.generator.structure.StructureType} instead.
 */
// Order is retrieved from WorldGenFactory
@Deprecated
public final class StructureType implements Keyed {

    private static final Map<String, StructureType> structureTypeMap = new HashMap<>();

    /**
     * Mineshafts are underground structures which consist of branching mining
     * tunnels with wooden supports and broken rails.
     * <br>
     * They are the only place where cave spider spawners and minecarts with
     * chests can be found naturally.
     */
    public static final StructureType MINESHAFT = register(new StructureType("mineshaft", MapCursor.Type.RED_X));

    /**
     * Villages are naturally generating structures that form above ground.
     * <br>
     * They are usually generated in desert, plains, taiga, and savanna biomes
     * and are a site for villager spawns, with whom the player can trade.
     */
    public static final StructureType VILLAGE = register(new StructureType("village", MapCursor.Type.MANSION));

    /**
     * Nether fortresses are very large complexes that mainly consist of
     * netherbricks.
     * <br>
     * They contain blaze spawners, nether wart farms, and loot chests. They are
     * only generated in the nether dimension.
     */
    public static final StructureType NETHER_FORTRESS = register(new StructureType("fortress", MapCursor.Type.RED_X));

    /**
     * Strongholds are underground structures that consist of many rooms,
     * libraries, and an end portal room.
     * <br>
     * They can be found using an {@link Material#ENDER_EYE}.
     */
    public static final StructureType STRONGHOLD = register(new StructureType("stronghold", MapCursor.Type.MANSION));

    /**
     * Jungle pyramids (also known as jungle temples) are found in jungles.
     * <br>
     * They are usually composed of cobblestone and mossy cobblestone. They
     * consist of three floors, with the bottom floor containing treasure
     * chests.
     */
    public static final StructureType JUNGLE_PYRAMID = register(new StructureType("jungle_pyramid", MapCursor.Type.RED_X));

    /**
     * Ocean ruins are clusters of many different blocks that generate
     * underwater in ocean biomes (as well as on the surface of beaches).
     * <br>
     * They come in my different variations. The cold variants consist primarily
     * of stone brick, and the warm variants consist of sandstone.
     */
    public static final StructureType OCEAN_RUIN = register(new StructureType("ocean_ruin", MapCursor.Type.TEMPLE));

    /**
     * Desert pyramids (also known as desert temples) are found in deserts.
     * <br>
     * They are usually composed of sandstone and stained terracotta.
     */
    public static final StructureType DESERT_PYRAMID = register(new StructureType("desert_pyramid", MapCursor.Type.RED_X));

    /**
     * Igloos are structures that generate in snowy biomes.
     * <br>
     * They consist of the house, as well as a basement.
     */
    public static final StructureType IGLOO = register(new StructureType("igloo", MapCursor.Type.RED_X));

    /**
     * Swamp huts (also known as witch huts) generate in swamp biomes and have
     * the ability to spawn witches.
     */
    public static final StructureType SWAMP_HUT = register(new StructureType("swamp_hut", MapCursor.Type.RED_X));

    /**
     * Ocean monuments are underwater structures.
     * <br>
     * They are usually composed on all three different prismarine types and sea
     * lanterns. They are the only place guardians and elder guardians spawn
     * naturally.
     */
    public static final StructureType OCEAN_MONUMENT = register(new StructureType("monument", MapCursor.Type.TEMPLE));

    /**
     * End Cities are tall castle-like structures that generate in the outer
     * island of the End dimension.
     * <br>
     * They consist primarily of end stone bricks, purpur blocks, and end rods.
     * They are the only place where shulkers can be found.
     */
    public static final StructureType END_CITY = register(new StructureType("end_city", MapCursor.Type.RED_X));

    /**
     * Mansions (also known as woodland mansions) are massive house structures
     * that generate in dark forests, containing a wide variety of rooms.
     * <br>
     * They are the only place where evokers, vindicators, and vexes spawn
     * naturally (but only once)
     */
    public static final StructureType WOODLAND_MANSION = register(new StructureType("mansion", MapCursor.Type.MANSION));

    /**
     * Buried treasure consists of a single chest buried in the beach sand or
     * gravel, with random loot in it.
     */
    public static final StructureType BURIED_TREASURE = register(new StructureType("buried_treasure", MapCursor.Type.RED_X));

    /**
     * Shipwrecks are structures that generate on the floor of oceans or
     * beaches.
     * <br>
     * They are made up of wood materials, and contain 1-3 loot chests. They can
     * generate sideways, upside-down, or upright.
     */
    public static final StructureType SHIPWRECK = register(new StructureType("shipwreck", MapCursor.Type.RED_X));

    /**
     * Pillager outposts may contain crossbows.
     */
    public static final StructureType PILLAGER_OUTPOST = register(new StructureType("pillager_outpost", MapCursor.Type.RED_X));

    /**
     * Nether fossils.
     */
    public static final StructureType NETHER_FOSSIL = register(new StructureType("nether_fossil", MapCursor.Type.RED_X));

    /**
     * Ruined portal.
     */
    public static final StructureType RUINED_PORTAL = register(new StructureType("ruined_portal", MapCursor.Type.RED_X));

    /**
     * Bastion remnant.
     */
    public static final StructureType BASTION_REMNANT = register(new StructureType("bastion_remnant", MapCursor.Type.RED_X));

    /* ****************
     *  STRUCTURE TYPES REGISTERED ABOVE THIS
     * ****************
     */
    private final NamespacedKey key;
    private final MapCursor.Type mapCursor;

    /**
     * Create a new StructureType with a given name and map cursor. To indicate
     * this structure should not be compatible with explorer maps, use null for
     * <i>mapIcon</i>.
     *
     * @param name the name of the structure, case-sensitive
     * @param mapIcon the {@link org.bukkit.map.MapCursor.Type} this structure type should use
     * when creating explorer maps. Use null to indicate this structure should
     * not be compatible with explorer maps.
     */
    private StructureType(@NotNull String name, @Nullable MapCursor.Type mapIcon) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Structure name cannot be empty");
        this.key = NamespacedKey.minecraft(name);
        this.mapCursor = mapIcon;
    }

    /**
     * Get the name of this structure. This is case-sensitive when used in
     * commands.
     *
     * @return the name of this structure
     */
    @NotNull
    public String getName() {
        return key.getKey();
    }

    /**
     * Get the {@link org.bukkit.map.MapCursor.Type} that this structure can use on maps. If
     * this is null, this structure will not appear on explorer maps.
     *
     * @return the {@link org.bukkit.map.MapCursor.Type} or null.
     */
    @Nullable
    public MapCursor.Type getMapIcon() {
        return mapCursor;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StructureType)) {
            return false;
        }
        StructureType that = (StructureType) other;
        return this.key.equals(that.key) && this.mapCursor == that.mapCursor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.key);
        hash = 71 * hash + Objects.hashCode(this.mapCursor);
        return hash;
    }

    @Override
    public String toString() {
        return "StructureType{key=" + this.key + ", cursor=" + this.mapCursor + "}";
    }

    @NotNull
    private static <T extends StructureType> T register(@NotNull T type) {
        Preconditions.checkNotNull(type, "Cannot register null StructureType.");
        Preconditions.checkArgument(!structureTypeMap.containsKey(type.getName()), "Cannot register same StructureType twice. %s", type.getName());
        StructureType.structureTypeMap.put(type.getName(), type);
        return type;
    }

    /**
     * Get all registered {@link StructureType}s.
     *
     * @return an immutable copy of registered structure types.
     */
    @NotNull
    public static Map<String, StructureType> getStructureTypes() {
        return ImmutableMap.copyOf(structureTypeMap);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
