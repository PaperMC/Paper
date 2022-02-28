package org.bukkit.craftbukkit.generator;

import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.levelgen.structure.StructureSet;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends net.minecraft.world.level.chunk.ChunkGenerator {

    public InternalChunkGenerator(IRegistry<StructureSet> iregistry, Optional<HolderSet<StructureSet>> optional, WorldChunkManager worldchunkmanager) {
        super(iregistry, optional, worldchunkmanager);
    }
}
