package io.papermc.paper.world.biome;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Location;

public class PaperBiomeClimate implements BiomeClimate {
    private final Biome.ClimateSettings climateSettings;
    private final Biome biome;

    public PaperBiomeClimate(Biome biome) {
        this.climateSettings = biome.climateSettings;
        this.biome = biome;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPrecipitation() {
        return climateSettings.hasPrecipitation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float temperature() {
        return climateSettings.temperature();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float downfall() {
        return climateSettings.downfall();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float adjustedTemperature(final Location location) {
        Preconditions.checkNotNull(location.getWorld(), "Cannot get biome climate for a location with a null world");
        BlockPos pos = new BlockPos(location.blockX(), location.blockY(), location.blockZ());
        return biome.getHeightAdjustedTemperature(pos, location.getWorld().getSeaLevel());
    }
}
