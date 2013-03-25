package org.bukkit.craftbukkit.chunkio;

import net.minecraft.server.ChunkProviderServer;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

class QueuedChunk {
    final long coords;
    final ChunkRegionLoader loader;
    final World world;
    final ChunkProviderServer provider;
    NBTTagCompound compound;

    public QueuedChunk(long coords, ChunkRegionLoader loader, World world, ChunkProviderServer provider) {
        this.coords = coords;
        this.loader = loader;
        this.world = world;
        this.provider = provider;
    }

    @Override
    public int hashCode() {
        return (int) coords ^ world.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof QueuedChunk) {
            QueuedChunk other = (QueuedChunk) object;
            return coords == other.coords && world == other.world;
        }

        return false;
    }
}
