package org.bukkit.craftbukkit.util;

import org.bukkit.Location;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

public record CraftStructureSearchResult(Structure structure, Location location) implements StructureSearchResult {

    @Override
    public Structure getStructure() {
        return this.structure;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}
