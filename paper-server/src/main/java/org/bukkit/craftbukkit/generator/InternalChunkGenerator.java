package org.bukkit.craftbukkit.generator;

import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.levelgen.StructureSettings;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends net.minecraft.world.level.chunk.ChunkGenerator {

    public InternalChunkGenerator(WorldChunkManager worldchunkmanager, StructureSettings structuresettings) {
        super(worldchunkmanager, structuresettings);
    }
}
