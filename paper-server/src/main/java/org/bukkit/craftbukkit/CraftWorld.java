package org.bukkit.craftbukkit;

import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.*;
import org.bukkit.entity.Arrow;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.Chunk;
import org.bukkit.entity.ItemDrop;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.Location;
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
            block = new CraftBlock(this, x, y, z, world.getTypeId(x, y, z), (byte)world.getData(x, y, z));
            blockCache.put(loc, block);
        } else {
            block.update();
        }

        return block;
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return world.getTypeId(x, y, z);
    }

    public int getHighestBlockYAt(int x, int z) {
        return world.d(x, z);
    }
    
    public Location getSpawnLocation() {
        return new Location(this, world.spawnX, world.e(world.spawnX, world.spawnZ), world.spawnZ);
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
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return world.A.a(chunk.getX(), chunk.getZ());
    }

    public void loadChunk(Chunk chunk) {
         world.A.d(chunk.getX(), chunk.getZ());
    }


    public Block updateBlock(int x, int y, int z) {
        BlockCoordinate loc = new BlockCoordinate(x, y, z);
        CraftBlock block = (CraftBlock)blockCache.get(loc);
        final int type = world.getTypeId(x, y, z);
        final byte data = (byte)world.getData(x, y, z);

        if (block == null) {
            block = new CraftBlock(this, x, y, z, type, data);
            blockCache.put(loc, block);
        } else {
            block.update();
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
    
    public ItemDrop dropItem(Location loc, ItemStack item) {
        net.minecraft.server.ItemStack stack = new net.minecraft.server.ItemStack(
            item.getTypeId(),
            item.getAmount(),
            item.getDamage()
        );
        EntityItem entity = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), stack);
        entity.c = 10;
        world.a(entity);
        //TODO this is inconsistent with how Entity.getBukkitEntity() works.
        // However, this entity is not at the moment backed by a server entity class so it may be left.
        return new CraftItemDrop(world.getServer(), entity);
    }
    
    public ItemDrop dropItemNaturally(Location loc, ItemStack item) {
        double xs = world.l.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        double ys = world.l.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        double zs = world.l.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        loc = loc.clone();
        loc.setX(loc.getX() + xs);
        loc.setY(loc.getY() + ys);
        loc.setZ(loc.getZ() + zs);
        return dropItem(loc, item);
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        EntityArrow arrow = new EntityArrow(world);
        arrow.c(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        world.a(arrow);
        arrow.a(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        return (Arrow) arrow.getBukkitEntity();
    }
    
    public Minecart spawnMinecart(Location loc) {
        EntityMinecart minecart = new EntityMinecart(
            world,
            loc.getX(),
            loc.getY(),
            loc.getZ(),
            CraftMinecart.Type.Minecart.getId()
        );
        world.a(minecart);
        return (Minecart) minecart.getBukkitEntity();
    }
    
    public StorageMinecart spawnStorageMinecart(Location loc) {
        EntityMinecart minecart = new EntityMinecart(
            world,
            loc.getX(),
            loc.getY(),
            loc.getZ(),
            CraftMinecart.Type.StorageMinecart.getId()
        );
        world.a(minecart);
        return (StorageMinecart) minecart.getBukkitEntity();
    }
    
    public PoweredMinecart spawnPoweredMinecart(Location loc) {
        EntityMinecart minecart = new EntityMinecart(
            world,
            loc.getX(),
            loc.getY(),
            loc.getZ(),
            CraftMinecart.Type.PoweredMinecart.getId()
        );
        world.a(minecart);
        return (PoweredMinecart) minecart.getBukkitEntity();
    }
    
    public Boat spawnBoat(Location loc) {
        EntityBoat boat = new EntityBoat(world, loc.getX(), loc.getY(), loc.getZ());
        world.a(boat);
        return (Boat) boat.getBukkitEntity();
    }
    
    public boolean generateTree(Location loc) {
        WorldGenTrees treeGen = new WorldGenTrees();
        return treeGen.a(world, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
    
    public boolean generateBigTree(Location loc) {
        WorldGenBigTree treeGen = new WorldGenBigTree();
        return treeGen.a(world, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
    
    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.getTileEntity(x, y, z);
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

    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();
        
        for (Object o : world.b) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt
                         = (net.minecraft.server.Entity)o;
                
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null) {
                    list.add(bukkitEntity);
                }
            }
        }
        
        return list;
    }

    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        
        for (Object o : world.b) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt
                         = (net.minecraft.server.Entity)o;
                
                Entity bukkitEntity = mcEnt.getBukkitEntity();
                
                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity) {
                    list.add((LivingEntity)bukkitEntity);
                }
            }
        }
        
        return list;
    }
}
