
package org.bukkit.craftbukkit;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import net.minecraft.server.WorldServer;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.SoftMap;

public class CraftChunk implements Chunk {
    private WeakReference<net.minecraft.server.Chunk> weakChunk;
    private final SoftMap<Integer, Block> cache = new SoftMap<Integer, Block>();
    private WorldServer worldServer;
    private int x;
    private int z;
    
    public CraftChunk(net.minecraft.server.Chunk chunk) {
        this.weakChunk = new WeakReference<net.minecraft.server.Chunk>(chunk);
        worldServer = (WorldServer) getHandle().d;
        x = getHandle().j;
        z = getHandle().k;
    }

    public World getWorld() {
        return worldServer.getWorld();
    }

    public net.minecraft.server.Chunk getHandle() {
        net.minecraft.server.Chunk c = weakChunk.get();
        if (c == null) {
            weakChunk = new WeakReference<net.minecraft.server.Chunk>(worldServer.c(x,z));
            c = weakChunk.get();
        }
        return c;
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
        Block block = this.cache.get( pos );
        if (block == null) {
            block = new CraftBlock( this, (getX() << 4) | (x & 0xF), y & 0x7F, (getZ() << 4) | (z & 0xF) );
            this.cache.put( pos, block );
        }
        return block;
    }
}

