package org.bukkit.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.jetbrains.annotations.NotNull;

/**
 * Holds the result of searching for a structure.
 *
 * @see World#locateNearestStructure(Location, Structure, int, boolean)
 * @see World#locateNearestStructure(Location, StructureType, int, boolean)
 */
public interface StructureSearchResult {

    /**
     * Return the structure which was found.
     *
     * @return the found structure.
     */
    @NotNull
    Structure getStructure();

    /**
     * Return the location of the structure.
     *
     * @return the location the structure was found.
     */
    @NotNull
    Location getLocation();
}
