package io.papermc.paper.world.biome;

import io.papermc.paper.math.Position;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;

public class PaperBiomeClimate implements BiomeClimate {
    private final Biome.ClimateSettings climateSettings;

    public PaperBiomeClimate(Biome biome) {
        this.climateSettings = biome.climateSettings;
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
    public float adjustedTemperature(final Position position) {
        BlockPos pos = new BlockPos(position.blockX(), position.blockY(), position.blockZ());
        return climateSettings.temperatureModifier().modifyTemperature(pos, climateSettings.temperature());
    }
}
