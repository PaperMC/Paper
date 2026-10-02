package org.bukkit.craftbukkit.generator;

import net.minecraft.world.level.biome.Climate;
import org.bukkit.generator.BiomeParameterPoint;

public class CraftBiomeParameterPoint implements BiomeParameterPoint {

    private final double temperature;
    private final double humidity;
    private final double continentalness;
    private final double erosion;
    private final double depth;
    private final double weirdness;
    private final Climate.Sampler sampler;

    public static BiomeParameterPoint createBiomeParameterPoint(Climate.Sampler sampler, Climate.TargetPoint targetPoint) {
        return new CraftBiomeParameterPoint(sampler, Climate.unquantizeCoord(targetPoint.temperature()), Climate.unquantizeCoord(targetPoint.humidity()), Climate.unquantizeCoord(targetPoint.continentalness()), Climate.unquantizeCoord(targetPoint.erosion()), Climate.unquantizeCoord(targetPoint.depth()), Climate.unquantizeCoord(targetPoint.weirdness()));
    }

    private CraftBiomeParameterPoint(Climate.Sampler sampler, double temperature, double humidity, double continentalness, double erosion, double depth, double weirdness) {
        this.sampler = sampler;
        this.temperature = temperature;
        this.humidity = humidity;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.depth = depth;
        this.weirdness = weirdness;
    }

    @Override
    public double getTemperature() {
        return this.temperature;
    }

    @Override
    public double getHumidity() {
        return this.humidity;
    }

    @Override
    public double getContinentalness() {
        return this.continentalness;
    }

    @Override
    public double getErosion() {
        return this.erosion;
    }

    @Override
    public double getDepth() {
        return this.depth;
    }

    @Override
    public double getWeirdness() {
        return this.weirdness;
    }
}
