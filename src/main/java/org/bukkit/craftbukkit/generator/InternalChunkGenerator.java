package org.bukkit.craftbukkit.generator;

import net.minecraft.server.GeneratorAccess;
import net.minecraft.server.GeneratorSettingsDefault;
import net.minecraft.server.WorldChunkManager;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator<C extends GeneratorSettingsDefault> extends net.minecraft.server.ChunkGenerator<C> {

    public InternalChunkGenerator(GeneratorAccess generatorAccess, WorldChunkManager worldChunkManager, C c0) {
        super(generatorAccess, worldChunkManager, c0);
    }
}
