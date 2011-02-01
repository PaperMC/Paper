
package org.bukkit.craftbukkit;

import java.util.HashMap;

import net.minecraft.server.WorldServer;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;

public class CraftChunk implements Chunk {
    private final net.minecraft.server.Chunk chunk;
    private final HashMap<Integer, Block> cache = new HashMap<Integer, Block>();

    public CraftChunk(net.minecraft.server.Chunk chunk) {
        this.chunk = chunk;
    }

    public World getWorld() {
        return ((WorldServer) chunk.d).getWorld();
    }

    public net.minecraft.server.Chunk getHandle() {
        return chunk;
    }

    public int getX() {
        return chunk.j;
    }

    public int getZ() {
        return chunk.k;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    public Block getBlock(int x, int y, int z) {
        int pos = (x & 0xF) << 11 | (z & 0xF) << 7 | (y & 0x7F);
        Block block = this.cache.get( pos );
        if (block == null) {
            block = new CraftBlock( this, (getX() << 4) | (x & 0xF), y & 0x7F, (getZ() << 4) | (z & 0xF) );
            this.cache.put( pos, block );
        }
        return block;
    }
}
