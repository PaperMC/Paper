
package org.bukkit.craftbukkit;

import java.lang.ref.WeakReference;
import net.minecraft.server.ChunkPosition;

import net.minecraft.server.WorldServer;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.util.ConcurrentSoftMap;

public class CraftChunk implements Chunk {
    private WeakReference<net.minecraft.server.Chunk> weakChunk;
    private final ConcurrentSoftMap<Integer, Block> cache = new ConcurrentSoftMap<Integer, Block>();
    private WorldServer worldServer;
    private int x;
    private int z;

    public CraftChunk(net.minecraft.server.Chunk chunk) {
        this.weakChunk = new WeakReference<net.minecraft.server.Chunk>(chunk);
        worldServer = (WorldServer) getHandle().world;
        x = getHandle().x;
        z = getHandle().z;
    }

    public World getWorld() {
        return worldServer.getWorld();
    }

    public net.minecraft.server.Chunk getHandle() {
        net.minecraft.server.Chunk c = weakChunk.get();
        if (c == null) {
            c = worldServer.getChunkAt(x,z);
            weakChunk = new WeakReference<net.minecraft.server.Chunk>(c);
        }
        return c;
    }

    void breakLink() {
        weakChunk.clear();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    public Block getBlock(int x, int y, int z) {
        int pos = (x & 0xF) << 11 | (z & 0xF) << 7 | (y & 0x7F);
        Block block = this.cache.get(pos);
        if (block == null) {
            Block newBlock = new CraftBlock(this, (getX() << 4) | (x & 0xF), y & 0x7F, (getZ() << 4) | (z & 0xF));
            Block oldBlock = this.cache.put(pos, newBlock);
            if(oldBlock == null) {
                block = newBlock;
            } else {
                block = oldBlock;
            }
        }
        return block;
    }

    public Entity[] getEntities() {
        int count = 0, index = 0;
        net.minecraft.server.Chunk chunk = getHandle();
        for (int i = 0; i < 8; i++) {
            count += chunk.entitySlices[i].size();
        }

        Entity[] entities = new Entity[count];
        for (int i = 0; i < 8; i++) {
            for (Object obj: chunk.entitySlices[i].toArray()) {
                if (!(obj instanceof net.minecraft.server.Entity)) continue;
                entities[index++] = ((net.minecraft.server.Entity) obj).getBukkitEntity();
            }
        }
        return entities;
    }

    public BlockState[] getTileEntities() {
        int index = 0;
        net.minecraft.server.Chunk chunk = getHandle();
        BlockState[] entities = new BlockState[chunk.tileEntities.size()];
        for (Object obj : chunk.tileEntities.keySet().toArray()) {
            if (!(obj instanceof ChunkPosition)) continue;
            ChunkPosition position = (ChunkPosition) obj;
            entities[index++] = worldServer.getWorld().getBlockAt(position.x + (chunk.x << 4), position.y, position.z + (chunk.z << 4)).getState();
        }
        return entities;
    }
}

