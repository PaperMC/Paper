package org.bukkit.craftbukkit.util;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.util.BiomeSearchResult;

public class CraftBiomeSearchResult implements BiomeSearchResult {

    private final Biome biome;
    private final Location location;

    public CraftBiomeSearchResult(Biome biome, Location location) {
        this.biome = biome;
        this.location = location;
    }

    public Biome getBiome() {
        return biome;
    }

    public Location getLocation() {
        return location;
    }
}
