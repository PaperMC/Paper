package io.papermc.paper.entity.poi;

import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the result of searching for a point of interest.
 *
 * @see World#locateAllPoiInRange(Location, Predicate, int)
 * @see World#locateAllPoiInRange(Location, Predicate, int, PoiType.Occupancy)
 */
@NullMarked
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
