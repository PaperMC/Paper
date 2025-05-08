package io.papermc.paper.world.biome;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;

public class PaperBiomeClimate implements BiomeClimate {

    private final Holder<Biome> biome;

    public PaperBiomeClimate(Holder<Biome> biome) {
        this.biome = biome;
    }

    @Override
    public boolean hasPrecipitation() {
        return this.biome.value().climateSettings.hasPrecipitation();
    }

    @Override
    public float temperature() {
        return this.biome.value().climateSettings.temperature();
    }

    @Override
    public float downfall() {
        return this.biome.value().climateSettings.downfall();
    }

    @Override
    public float adjustedTemperature(final Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Cannot get biome climate for a location with a null world");
        return this.biome.value().getHeightAdjustedTemperature(CraftLocation.toBlockPosition(location), location.getWorld().getSeaLevel());
    }
}
