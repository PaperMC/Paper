package org.bukkit.craftbukkit.generator;

import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends net.minecraft.world.level.chunk.ChunkGenerator {

    public InternalChunkGenerator(BiomeSource biomeSource, Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter) {
        super(biomeSource, generationSettingsGetter);
    }
}
