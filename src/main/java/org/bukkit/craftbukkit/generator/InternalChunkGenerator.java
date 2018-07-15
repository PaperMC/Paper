package org.bukkit.craftbukkit.generator;

import net.minecraft.server.GeneratorSettings;
import org.bukkit.generator.ChunkGenerator;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator<C extends GeneratorSettings> extends ChunkGenerator implements net.minecraft.server.ChunkGenerator<C> {
}
