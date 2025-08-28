package io.papermc.paper.entity.poi;

import io.papermc.paper.InternalAPIBridge;
import org.bukkit.Keyed;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a point of interest (POI).
 * <p>
 * Many PoiTypes act as a marker vs storing occupants examples of this are most
 * explicitly {@link PoiTypes#NETHER_PORTAL}. This state can be checked using the method
 * {@link #hasOccupants()} which will return {@code true} if the PoiType can have occupants.
 *
 * @see PoiTypes
 */
@NullMarked
public interface PoiType extends Keyed {

    /**
     * Determines whether the provided BlockState is relevant to this
     * point of interest.
     *
     * @param data the BlockData to check
     * @return {@code true} if the BlockState is relevant, otherwise {@code false}
     */
    boolean is(BlockData data);

    /**
     * Determines whether this PoiType can actually hold occupants.
     *
     * @return {@code true} if this PoiType can hold occupants
     */
    boolean hasOccupants();

    /**
     * Determines the type of occupancy the point of interest has.
     * <p>
     * A PoiType occupancy is defined by how much "space" a point of interest
     * has.
     * <p>
     * With context to a Villager workstation if a villager is currently using
     * a workstation as a source of its profession the occupancy would be
     * {@link Occupancy#IS_OCCUPIED}, however, if no villager was attached to
     * a workstation its occupancy would be {@link Occupancy#HAS_SPACE}.
     */
    interface Occupancy {

        /**
         * The poi has space.
         */
        Occupancy HAS_SPACE = occupancy("HAS_SPACE");
        /**
         * The poi is occupied.
         */
        Occupancy IS_OCCUPIED = occupancy("IS_OCCUPIED");
        /**
         * The poi is either occupied or has space.
         */
        Occupancy ANY = occupancy("ANY");

        private static Occupancy occupancy(final String enumEntryName) {
            return InternalAPIBridge.get().createOccupancy(enumEntryName);
        }
    }
}
