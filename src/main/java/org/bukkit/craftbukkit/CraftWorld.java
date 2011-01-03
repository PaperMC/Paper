
package org.bukkit.craftbukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.WorldGenBigTree;
import net.minecraft.server.WorldGenTrees;
import net.minecraft.server.WorldServer;
import net.minecraft.server.EntityArrow;
import org.bukkit.ArrowEntity;
import org.bukkit.Block;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Vector;
import org.bukkit.World;

public class CraftWorld implements World {
    private final Map<ChunkCoordinate, Chunk> chunkCache = new HashMap<ChunkCoordinate, Chunk>();
    private final Map<BlockCoordinate, Block> blockCache = new HashMap<BlockCoordinate, Block>();
    private final WorldServer world;
    
    private static final Random rand = new Random();

    public CraftWorld(WorldServer world) {
        this.world = world;
    }

    public Block getBlockAt(int x, int y, int z) {
        BlockCoordinate loc = new BlockCoordinate(x, y, z);
        Block block = blockCache.get(loc);

        if (block == null) {
            block = new CraftBlock(this, x, y, z, world.a(x, y, z), (byte)world.b(x, y, z));
            blockCache.put(loc, block);
        }

        return block;
    }

    public Chunk getChunkAt(int x, int z) {
        ChunkCoordinate loc = new ChunkCoordinate(x, z);
        Chunk chunk = chunkCache.get(loc);

        if (chunk == null) {
            chunk = new CraftChunk(x, z);
            chunkCache.put(loc, chunk);
        }

        return chunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() << 4, block.getZ() << 4);
    }

    public boolean isChunkLoaded() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Block updateBlock(int x, int y, int z) {
        BlockCoordinate loc = new BlockCoordinate(x, y, z);
        CraftBlock block = (CraftBlock)blockCache.get(loc);
        final int type = world.a(x, y, z);
        final byte data = (byte)world.b(x, y, z);

        if (block == null) {
            block = new CraftBlock(this, x, y, z, type, data);
            blockCache.put(loc, block);
        } else {
            block.type = type;
            block.data = data;
        }

        return block;
    }

    public WorldServer getHandle() {
        return world;
    }

    public ArrowEntity spawnArrow(Location loc, Vector velocity, float speed,
            float spread) {
        EntityArrow arrow = new EntityArrow(world);
        arrow.c(loc.getX(), loc.getY(), loc.getZ());
        world.a(arrow);
        arrow.a(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        return new CraftArrowEntity(world.getServer(), arrow);
    }
    
    public boolean generateTree(Location loc) {
        WorldGenTrees treeGen = new WorldGenTrees();
        return treeGen.a(world, rand,
                loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
    
    public boolean generateBigTree(Location loc) {
        WorldGenBigTree treeGen = new WorldGenBigTree();
        return treeGen.a(world, rand,
                loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public String toString() {
        return "CraftWorld";
    }

    private final class ChunkCoordinate {
        public final int x;
        public final int z;

        public ChunkCoordinate(final int x, final int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ChunkCoordinate other = (ChunkCoordinate) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.z != other.z) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + this.x;
            hash = 53 * hash + this.z;
            return hash;
        }
    }

    private final class BlockCoordinate {
        public final int x;
        public final int y;
        public final int z;

        public BlockCoordinate(final int x, final int y, final int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final BlockCoordinate other = (BlockCoordinate) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            if (this.z != other.z) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.x;
            hash = 37 * hash + this.y;
            hash = 37 * hash + this.z;
            return hash;
        }
    }
}
