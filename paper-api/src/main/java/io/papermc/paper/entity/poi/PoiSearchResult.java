package io.papermc.paper.entity.poi;

import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Holds the result of searching for a point of interest.
 *
 * @see World#locateAllPoiInRange(Location, Predicate, int)
 * @see World#locateAllPoiInRange(Location, Predicate, int, PoiType.Occupancy)
 */
public interface PoiSearchResult {

    /**
     * Returns the {@link PoiType}.
     *
     * @return the {@link PoiType}
     */
    PoiType poiType();

    /**
     * Return the location of the {@link PoiType}.
     *
     * @return the location the {@link PoiType} was found at
     */
    Location location();
}
