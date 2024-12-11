package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

public class CustomWorldChunkManager extends BiomeSource {

    private final WorldInfo worldInfo;
    private final BiomeProvider biomeProvider;
    private final Registry<net.minecraft.world.level.biome.Biome> registry;

    private static List<Holder<net.minecraft.world.level.biome.Biome>> biomeListToBiomeBaseList(List<Biome> biomes, Registry<net.minecraft.world.level.biome.Biome> registry) {
        List<Holder<net.minecraft.world.level.biome.Biome>> biomeBases = new ArrayList<>();

        for (Biome biome : biomes) {
            Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot use the biome %s", biome);
            biomeBases.add(CraftBiome.bukkitToMinecraftHolder(biome));
        }

        return biomeBases;
    }

    public CustomWorldChunkManager(WorldInfo worldInfo, BiomeProvider biomeProvider, Registry<net.minecraft.world.level.biome.Biome> registry) {
        this.worldInfo = worldInfo;
        this.biomeProvider = biomeProvider;
        this.registry = registry;
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        throw new UnsupportedOperationException("Cannot serialize CustomWorldChunkManager");
    }

    @Override
    public Holder<net.minecraft.world.level.biome.Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler noise) {
        Biome biome = this.biomeProvider.getBiome(this.worldInfo, x << 2, y << 2, z << 2, CraftBiomeParameterPoint.createBiomeParameterPoint(noise, noise.sample(x, y, z)));
        Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot set the biome to %s", biome);

        return CraftBiome.bukkitToMinecraftHolder(biome);
    }

    @Override
    protected Stream<Holder<net.minecraft.world.level.biome.Biome>> collectPossibleBiomes() {
        return CustomWorldChunkManager.biomeListToBiomeBaseList(this.biomeProvider.getBiomes(this.worldInfo), this.registry).stream();
    }
}
