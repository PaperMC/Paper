package org.bukkit.craftbukkit;

import org.bukkit.craftbukkit.entity.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.minecraft.server.*;

import org.bukkit.entity.Arrow;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.Chunk;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;

public class CraftWorld implements World {
    private final WorldServer world;
    private final Environment environment;
    private final CraftServer server;
    private final ChunkProviderServer provider;
    private HashMap<Integer,CraftChunk> unloadedChunks = new HashMap<Integer, CraftChunk>();

    private static final Random rand = new Random();

    public CraftWorld(WorldServer world) {
        this.world = world;
        this.server = world.getServer();
        this.provider = world.u;

        if (world.m instanceof WorldProviderHell) {
            environment = Environment.NETHER;
        } else {
            environment = Environment.NORMAL;
        }

        server.addWorld(this);
    }

    public void preserveChunk( CraftChunk chunk ) {
        chunk.breakLink();
        unloadedChunks.put( (chunk.getX() << 16) + chunk.getZ(), chunk );
    }

    public CraftChunk popPreservedChunk( int x, int z ) {
        return unloadedChunks.remove( (x << 16) + z );
    }

    public Block getBlockAt(int x, int y, int z) {
        return getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y & 0x7F, z & 0xF);
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return world.getTypeId(x, y, z);
    }

    public int getHighestBlockYAt(int x, int z) {
        return world.d(x, z);
    }

    public Location getSpawnLocation() {
        ChunkCoordinates spawn = world.l();
        return new Location(this, spawn.a, spawn.b, spawn.c);
    }
    
    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            world.q.a(x, y, z);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Chunk getChunkAt(int x, int z) {
        return this.provider.d(x,z).bukkitChunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(int x, int z) {
        return provider.a( x, z );
    }

    public Chunk[] getLoadedChunks() {
        Object[] chunks = provider.e.values().toArray();
        org.bukkit.Chunk[] craftChunks = new CraftChunk[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            net.minecraft.server.Chunk chunk = (net.minecraft.server.Chunk)chunks[i];
            craftChunks[i] = chunk.bukkitChunk;
        }

        return craftChunks;
    }

    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk(x, z, save, false);
    }
    
    public boolean unloadChunkRequest(int x, int z) {
        return unloadChunkRequest(x, z, true);
    }
    
    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        if (safe && isChunkInUse(x, z)) {
            return false;
        }
        
        provider.c(x, z);

        return true;
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        if (safe && isChunkInUse(x, z)) {
            return false;
        }
        
        net.minecraft.server.Chunk chunk = provider.b(x, z);
        
        if (save) {
            chunk.e();
            provider.b(chunk);
            provider.a(chunk);
        }

        preserveChunk((CraftChunk)chunk.bukkitChunk);
        provider.a.remove(x, z);
        provider.e.remove(x, z);
        provider.f.remove(chunk);
        
        return true;
    }

    public boolean regenerateChunk(int x, int z) {
        unloadChunk(x, z, false, false);

        provider.a.remove(x, z);

        net.minecraft.server.Chunk chunk = null;

        if (provider.c == null) {
            chunk = provider.b;
        } else {
            chunk = provider.c.b(x, z);
        }

        chunkLoadPostProcess(chunk, x, z);

        refreshChunk(x, z);

        return chunk != null;
    }

    public boolean refreshChunk(int x, int z) {
        if (!isChunkLoaded(x, z)) {
            return false;
        }

        int px = x<<4;
        int pz = z<<4;

        // If there are more than 10 updates to a chunk at once, it carries out the update as a cuboid
        // This flags 16 blocks in a line along the bottom for update and then flags a block at the opposite corner at the top
        // The cuboid that contains these 17 blocks covers the entire chunk
        // The server will compress the chunk and send it to all clients

        for(int xx = px; xx < (px + 16); xx++) {
            world.g(xx, 0, pz);
        }
        world.g(px, 127, pz+15);

        return true;
    }


    public boolean isChunkInUse(int x, int z) {
        Player[] players = server.getOnlinePlayers();
        
        for (Player player : players) {
            Location loc = player.getLocation();
            if (loc.getWorld() != provider.g.getWorld()) {
                continue;
            }

            // If the chunk is within 256 blocks of a player, refuse to accept the unload request
            // This is larger than the distance of loaded chunks that actually surround a player
            // The player is the center of a 21x21 chunk grid, so the edge is 10 chunks (160 blocks) away from the player
            if (Math.abs(loc.getBlockX() - (x << 4)) <= 256 && Math.abs(loc.getBlockZ() - (z << 4)) <= 256) {
                return true;
            }
        }
        return false;
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        if (generate) {
            // Use the default variant of loadChunk when generate == true.
            return provider.d(x, z) != null;
        }

        provider.a.remove(x, z);
        net.minecraft.server.Chunk chunk = (net.minecraft.server.Chunk) provider.e.get(x, z);

        if (chunk == null) {
            chunk = provider.e(x, z);

            chunkLoadPostProcess(chunk, x, z);
        }
        return chunk != null;
    }

    private void chunkLoadPostProcess(net.minecraft.server.Chunk chunk, int x, int z) {
        if (chunk != null) {
            provider.e.put(x, z, chunk);
            provider.f.add(chunk);

            chunk.c();
            chunk.d();

            if (!chunk.n && provider.a(x + 1, z + 1) && provider.a(x, z + 1) && provider.a(x + 1, z)) {
                provider.a(provider, x, z);
            }

            if (provider.a(x - 1, z) && !provider.b(x - 1, z).n && provider.a(x - 1, z + 1) && provider.a(x, z + 1) && provider.a(x - 1, z)) {
                provider.a(provider, x - 1, z);
            }

            if (provider.a(x, z - 1) && !provider.b(x, z - 1).n && provider.a(x + 1, z - 1) && provider.a(x, z - 1) && provider.a(x + 1, z)) {
                provider.a(provider, x, z - 1);
            }

            if (provider.a(x - 1, z - 1) && !provider.b(x - 1, z - 1).n && provider.a(x - 1, z - 1) && provider.a(x, z - 1) && provider.a(x - 1, z)) {
                provider.a(provider, x - 1, z - 1);
            }
        }
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    public void loadChunk(Chunk chunk) {
        loadChunk(chunk.getX(), chunk.getZ());
        ((CraftChunk) getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
    }

    public WorldServer getHandle() {
        return world;
    }

    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item) {
        net.minecraft.server.ItemStack stack = new net.minecraft.server.ItemStack(
            item.getTypeId(),
            item.getAmount(),
            item.getDurability()
        );
        EntityItem entity = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), stack);
        entity.c = 10;
        world.a(entity);
        //TODO this is inconsistent with how Entity.getBukkitEntity() works.
        // However, this entity is not at the moment backed by a server entity class so it may be left.
        return new CraftItem(world.getServer(), entity);
    }

    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        double xs = world.k.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        double ys = world.k.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        double zs = world.k.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
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

    public LivingEntity spawnCreature(Location loc, CreatureType creatureType) {
        LivingEntity creature;
        try {
            EntityLiving entityCreature = (EntityLiving) EntityTypes.a(creatureType.getName(), world);
            entityCreature.a(loc.getX(), loc.getY(), loc.getZ());
            creature = (LivingEntity) CraftEntity.getEntity(server, entityCreature);
            world.a(entityCreature);
        } catch (Exception e) {
            // if we fail, for any reason, return null.
            creature = null;
        }
        return creature;
    }

    public boolean generateTree(Location loc, TreeType type) {
        return generateTree(loc, type, world);
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        switch (type) {
            case BIG_TREE:
                return new WorldGenBigTree().generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case BIRCH:
                return new WorldGenForest().generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case REDWOOD:
                return new WorldGenTaiga2().generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case TALL_REDWOOD:
                return new WorldGenTaiga1().generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case TREE:
            default:
                return new WorldGenTrees().generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }
    }

    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.getTileEntity(x, y, z);
    }

    public String getName() {
        return world.q.j;
    }

    public long getId() {
        return world.q.b();
    }

    @Override
    public String toString() {
        return "CraftWorld{name=" + getName() + '}';
    }

    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) time += 24000;
        return time;
    }

    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) margin += 24000;
        setFullTime(getFullTime() + margin);
    }

    public long getFullTime() {
        return world.k();
    }

    public void setFullTime(long time) {
        world.a(time);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getBlockTypeIdAt(Location location) {
        return getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
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

    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object o: world.b) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt = (net.minecraft.server.Entity)o;
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

        for (Object o: world.b) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt = (net.minecraft.server.Entity)o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity) {
                    list.add((LivingEntity)bukkitEntity);
                }
            }
        }

        return list;
    }

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>();

        for (Object o : world.b) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt = (net.minecraft.server.Entity)o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();
                
                if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                    list.add((Player)bukkitEntity);
                }
            }
        }

        return list;
    }

    public void save() {
        // Writes level.dat
        world.r();

        // Saves all chunks/regions
        world.o.a(true, null);
    }
}
