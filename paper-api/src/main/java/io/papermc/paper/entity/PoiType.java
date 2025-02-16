package io.papermc.paper.entity;

import io.papermc.paper.InternalAPIBridge;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a point of interest (poi)
 */
@NullMarked
public interface PoiType extends Keyed {

    /**
     * An armorer's point of interests.
     */
    PoiType ARMORER = get("armorer");
    /**
     * A butcher's point of interests.
     */
    PoiType BUTCHER = get("butcher");
    /**
     * A cartographer's point of interests.
     */
    PoiType CARTOGRAPHER = get("cartographer");
    /**
     * A cleric's point of interests.
     */
    PoiType CLERIC = get("cleric");
    /**
     * A farmer's point of interests.
     */
    PoiType FARMER = get("farmer");
    /**
     * A fisherman's point of interests.
     */
    PoiType FISHERMAN = get("fisherman");
    /**
     * A fletcher's point of interests.
     */
    PoiType FLETCHER = get("fletcher");
    /**
     * A leatherworker's point of interests.
     */
    PoiType LEATHERWORKER = get("leatherworker");
    /**
     * A librarian's point of interests.
     */
    PoiType LIBRARIAN = get("librarian");
    /**
     * A mason's point of interests.
     */
    PoiType MASON = get("mason");
    /**
     * A shepherd's point of interests.
     */
    PoiType SHEPHERD = get("shepherd");
    /**
     * A toolsmithr's point of interests.
     */
    PoiType TOOLSMITH = get("toolsmith");
    /**
     * A weaponsmith's point of interests.
     */
    PoiType WEAPONSMITH = get("weaponsmith");
    /**
     * A home point of interest.
     */
    PoiType HOME = get("home");
    /**
     * A meeting point of interest.
     */
    PoiType MEETING = get("meeting");
    /**
     * A beehive point of interests.
     */
    PoiType BEEHIVE = get("beehive");
    /**
     * A bee nest point of interests.
     */
    PoiType BEE_NEST = get("bee_nest");
    /**
     * A nether portal point of interest.
     */
    PoiType NETHER_PORTAL = get("nether_portal");
    /**
     * A lodestone point of interest.
     */
    PoiType LODESTONE = get("lodestone");
    /**
     * A lightning rod point of interest.
     */
    PoiType LIGHTNING_ROD = get("lightning_rod");

    /**
     * Determines whether or not the provided BlockState is relevant to this
     * point of interest.
     *
     * @param data the BlockData to check
     * @return true if the BlockState is relevant, otherwise false
     */
    boolean is(@NotNull final BlockData data);

    private static PoiType get(String key) {
        return Registry.POINT_OF_INTEREST_TYPE.getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * Determines the type of occupancy the point of interest has.
     * <p>
     * A PoiType occupancy is defined by how much "space" a point of interest
     * has.
     * <p>
     * With context to a Beehive, if a beehive can have 3 bees inside of it
     * and only 2 bees are inside the occupancy of the Beehive would be
     * considered {@link Occupancy#HAS_SPACE}. However, if all 3 bees are
     * inside of a Beehive occupancy would be {@link Occupancy#IS_OCCUPIED}.
     * <p>
     * With context to a Villager workstation if a villager is currently using
     * a workstation as a source of its profession the occupancy would be
     * {@link Occupancy#IS_OCCUPIED}, however, if no villager was attached to
     * a workstation its occupancy would be {@link Occupancy#HAS_SPACE}.
     */
    interface Occupancy {

        /**
         * The poi has space
         */
        Occupancy HAS_SPACE = occupancy("HAS_SPACE");
        /**
         * The poi is occupied
         */
        Occupancy IS_OCCUPIED = occupancy("IS_OCCUPIED");
        /**
         * The poi is either occupied or has space
         */
        Occupancy ANY = occupancy("ANY");

        private static Occupancy occupancy(String enumEntryName) {
            return InternalAPIBridge.get().createOccupancy(enumEntryName);
        }
    }

}
