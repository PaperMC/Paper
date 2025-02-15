package io.papermc.paper.util;

import io.papermc.paper.entity.PoiType;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the result of searching for a point of interest.
 *
 * @see World#locateNearestPoi(Location, PoiType, int, PoiType.Occupancy)
 */
@NullMarked
public interface PoiSearchResult {

    /**
     * Returns the {@link PoiType}
     *
     * @return the {@link PoiType}
     */
    PoiType getPoiType();

    /**
     * Return the location of the {@link PoiType}.
     *
     * @return the location the {@link PoiType} was found at
     */
    Location getLocation();
}
