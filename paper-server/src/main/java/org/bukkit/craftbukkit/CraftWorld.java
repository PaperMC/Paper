
package org.bukkit.craftbukkit;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.EntityMinecart;
import java.util.Random;

import net.minecraft.server.EntityBoat;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayerMP;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.TileEntity;
import net.minecraft.server.WorldGenBigTree;
import net.minecraft.server.WorldServer;
import net.minecraft.server.WorldGenTrees;
import org.bukkit.Arrow;
import org.bukkit.Block;
import org.bukkit.Boat;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Minecart;
import org.bukkit.PoweredMinecart;
import org.bukkit.StorageMinecart;
import org.bukkit.Vector;
import org.bukkit.World;

public class CraftWorld implements World {
    private final Map<ChunkCoordinate, CraftChunk> chunkCache = new HashMap<ChunkCoordinate, CraftChunk>();
    private final Map<BlockCoordinate, CraftBlock> blockCache = new HashMap<BlockCoordinate, CraftBlock>();
    private final WorldServer world;
    
    private static final Random rand = new Random();

    public CraftWorld(WorldServer world) {
        this.world = world;
    }

    public Block getBlockAt(int x, int y, int z) {
        BlockCoordinate loc = new BlockCoordinate(x, y, z);
        CraftBlock block = blockCache.get(loc);

        if (block == null) {
            block = new CraftBlock(this, x, y, z, world.a(x, y, z), (byte)world.b(x, y, z));
            blockCache.put(loc, block);
        }

        return block;
    }
    
    public int getHighestBlockYAt(int x, int z) {
    	return world.d(x, z);
    }

    public Chunk getChunkAt(int x, int z) {
        ChunkCoordinate loc = new ChunkCoordinate(x, z);
        CraftChunk chunk = chunkCache.get(loc);

        if (chunk == null) {
            chunk = new CraftChunk(this, x, z);
            chunkCache.put(loc, chunk);
        }

        return chunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() << 4, block.getZ() << 4);
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return world.A.a(chunk.getX(), chunk.getZ());
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

    public CraftChunk updateChunk(int x, int z) {
        ChunkCoordinate loc = new ChunkCoordinate(x, z);
        CraftChunk chunk = chunkCache.get(loc);

        if (chunk == null) {
            chunk = new CraftChunk(this, x, z);
            chunkCache.put(loc, chunk);
        } else {
            // TODO: Chunk stuff
        }

        return chunk;
    }

    public WorldServer getHandle() {
        return world;
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed,
            float spread) {
        EntityArrow arrow = new EntityArrow(world);
        arrow.c(loc.getX(), loc.getY(), loc.getZ());
        world.a(arrow);
        arrow.a(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        return new CraftArrow(world.getServer(), arrow);
    }
    
    public Minecart spawnMinecart(Location loc) {
        EntityMinecart minecart = new EntityMinecart(
                world,
                loc.getX(), loc.getY(), loc.getZ(),
                CraftMinecart.Type.Minecart.getID());
        world.a(minecart);
        return new CraftMinecart(world.getServer(), minecart);
    }
    
    public StorageMinecart spawnStorageMinecart(Location loc) {
        EntityMinecart minecart = new EntityMinecart(
                world,
                loc.getX(), loc.getY(), loc.getZ(),
                CraftMinecart.Type.StorageMinecart.getID());
        world.a(minecart);
        return new CraftStorageMinecart(world.getServer(), minecart);
    }
    
    public PoweredMinecart spawnPoweredMinecart(Location loc) {
        EntityMinecart minecart = new EntityMinecart(
                world,
                loc.getX(), loc.getY(), loc.getZ(),
                CraftMinecart.Type.PoweredMinecart.getID());
        world.a(minecart);
        return new CraftPoweredMinecart(world.getServer(), minecart);
    }
    
    public Boat spawnBoat(Location loc) {
        EntityBoat boat =
            new EntityBoat(world, loc.getX(), loc.getY(), loc.getZ());
        world.a(boat);
        return new CraftBoat(world.getServer(), boat);
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
    
    public CraftEntity toCraftEntity(net.minecraft.server.Entity entity) {
        if (entity instanceof CraftMappable) {
            return ((CraftMappable)entity).getCraftEntity();
        } else if (entity instanceof EntityArrow) {
            return new CraftArrow(world.getServer(), (EntityArrow)entity);
        } else if (entity instanceof EntityEgg) {
            return new CraftEgg(world.getServer(), (EntityEgg)entity);
        } else if (entity instanceof EntityPlayerMP) {
            return new CraftPlayer(world.getServer(), (EntityPlayerMP)entity);
        } else if (entity instanceof EntitySnowball) {
            return new CraftSnowball(world.getServer(), (EntitySnowball)entity);
        } else if (entity instanceof EntityPlayer) {
            return new CraftHumanEntity(world.getServer(), (EntityPlayer)entity);
        } else if (entity instanceof EntityLiving) {
            return new CraftLivingEntity(world.getServer(), (EntityLiving)entity);
        } else {
            return null;
        }
    }

    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.l(x, y, z);
    }

    public String getName() {
        return world.w;
    }

    public long getId() {
        return world.u;
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
