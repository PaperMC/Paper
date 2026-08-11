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
    public double getMaxTemperature() {
        return this.sampler.temperature().range().max();
    }

    @Override
    public double getMinTemperature() {
        return this.sampler.temperature().range().min();
    }

    @Override
    public double getHumidity() {
        return this.humidity;
    }

    @Override
    public double getMaxHumidity() {
        return this.sampler.humidity().range().max();
    }

    @Override
    public double getMinHumidity() {
        return this.sampler.humidity().range().min();
    }

    @Override
    public double getContinentalness() {
        return this.continentalness;
    }

    @Override
    public double getMaxContinentalness() {
        return this.sampler.continentalness().range().max();
    }

    @Override
    public double getMinContinentalness() {
        return this.sampler.continentalness().range().min();
    }

    @Override
    public double getErosion() {
        return this.erosion;
    }

    @Override
    public double getMaxErosion() {
        return this.sampler.erosion().range().max();
    }

    @Override
    public double getMinErosion() {
        return this.sampler.erosion().range().min();
    }

    @Override
    public double getDepth() {
        return this.depth;
    }

    @Override
    public double getMaxDepth() {
        return this.sampler.depth().range().max();
    }

    @Override
    public double getMinDepth() {
        return this.sampler.depth().range().min();
    }

    @Override
    public double getWeirdness() {
        return this.weirdness;
    }

    @Override
    public double getMaxWeirdness() {
        return this.sampler.weirdness().range().max();
    }

    @Override
    public double getMinWeirdness() {
        return this.sampler.weirdness().range().min();
    }
}
