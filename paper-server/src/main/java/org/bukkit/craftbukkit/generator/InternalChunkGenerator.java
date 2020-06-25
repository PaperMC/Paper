package org.bukkit.craftbukkit.generator;

import net.minecraft.server.StructureSettings;
import net.minecraft.server.WorldChunkManager;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends net.minecraft.server.ChunkGenerator {

    public InternalChunkGenerator(WorldChunkManager worldchunkmanager, StructureSettings structuresettings) {
        super(worldchunkmanager, structuresettings);
    }
}
