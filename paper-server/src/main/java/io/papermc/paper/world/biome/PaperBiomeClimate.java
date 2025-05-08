package io.papermc.paper.world.biome;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;

public class PaperBiomeClimate implements BiomeClimate {

    private final Biome.ClimateSettings climate;
    private final Biome biome;

    public PaperBiomeClimate(Biome.ClimateSettings climate, Biome biome) {
        this.climate = climate;
        this.biome = biome;
    }

    @Override
    public boolean hasPrecipitation() {
        return this.climate.hasPrecipitation();
    }

    @Override
    public float temperature() {
        return this.climate.temperature();
    }

    @Override
    public float downfall() {
        return this.climate.downfall();
    }

    @Override
    public float adjustedTemperature(final Location location) {
        Preconditions.checkArgument(location != null, "Cannot get biome climate for a null location");
        Preconditions.checkArgument(location.getWorld() != null, "Cannot get biome climate for a location with a null world");
        return this.biome.getHeightAdjustedTemperature(CraftLocation.toBlockPosition(location), location.getWorld().getSeaLevel());
    }
}
