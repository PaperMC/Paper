package org.bukkit.craftbukkit.util;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.util.BiomeSearchResult;

public record CraftBiomeSearchResult(Biome biome, Location location) implements BiomeSearchResult {

    @Override
    public Biome getBiome() {
        return this.biome;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}
