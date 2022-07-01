package org.bukkit.craftbukkit.util;

import org.bukkit.Location;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

public class CraftStructureSearchResult implements StructureSearchResult {

    private final Structure structure;
    private final Location location;

    public CraftStructureSearchResult(Structure structure, Location location) {
        this.structure = structure;
        this.location = location;
    }

    public Structure getStructure() {
        return structure;
    }

    public Location getLocation() {
        return location;
    }
}
