package io.papermc.paper.world.biome;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;

public class PaperBiomeClimate implements BiomeClimate {
    private final Biome.ClimateSettings climateSettings;
    private final Biome biome;

    public PaperBiomeClimate(Biome biome) {
        this.climateSettings = biome.climateSettings;
        this.biome = biome;
    }

    @Override
    public boolean hasPrecipitation() {
        return climateSettings.hasPrecipitation();
    }

    @Override
    public float temperature() {
        return climateSettings.temperature();
    }

    @Override
    public float downfall() {
        return climateSettings.downfall();
    }

    @Override
    public float adjustedTemperature(final Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Cannot get biome climate for a location with a null world");
        return biome.getHeightAdjustedTemperature(CraftLocation.toBlockPosition(location), location.getWorld().getSeaLevel());
    }
}
