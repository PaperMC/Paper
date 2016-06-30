package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.server.*;

import org.apache.commons.lang.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

public class CraftWorld implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;

    private final WorldServer world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    private final ChunkGenerator generator;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int ambientSpawn = -1;
    private int chunkLoadCount = 0;
    private int chunkGCTickCount;

    private static final Random rand = new Random();

    public CraftWorld(WorldServer world, ChunkGenerator gen, Environment env) {
        this.world = world;
        this.generator = gen;

        environment = env;

        if (server.chunkGCPeriod > 0) {
            chunkGCTickCount = rand.nextInt(server.chunkGCPeriod);
        }
    }

    public Block getBlockAt(int x, int y, int z) {
        return getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y, z & 0xF);
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return CraftMagicNumbers.getId(world.getType(new BlockPosition(x, y, z)).getBlock());
    }

    public int getHighestBlockYAt(int x, int z) {
        if (!isChunkLoaded(x >> 4, z >> 4)) {
            loadChunk(x >> 4, z >> 4);
        }

        return world.getHighestBlockYAt(new BlockPosition(x, 0, z)).getY();
    }

    public Location getSpawnLocation() {
        BlockPosition spawn = world.getSpawn();
        return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = getSpawnLocation();
            world.worldData.setSpawn(new BlockPosition(x, y, z));

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Chunk getChunkAt(int x, int z) {
        return this.world.getChunkProviderServer().getChunkAt(x, z).bukkitChunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(int x, int z) {
        return world.getChunkProviderServer().isLoaded(x, z);
    }

    public Chunk[] getLoadedChunks() {
        Object[] chunks = world.getChunkProviderServer().chunks.values().toArray();
        org.bukkit.Chunk[] craftChunks = new CraftChunk[chunks.length];

        for (int i = 0; i < chunks.length; i++) {
            net.minecraft.server.Chunk chunk = (net.minecraft.server.Chunk) chunks[i];
            craftChunks[i] = chunk.bukkitChunk;
        }

        return craftChunks;
    }

    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
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

        net.minecraft.server.Chunk chunk = world.getChunkProviderServer().getLoadedChunkAt(x, z);
        if (chunk != null) {
            world.getChunkProviderServer().unload(chunk);
        }

        return true;
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        if (isChunkInUse(x, z)) {
            return false;
        }

        return unloadChunk0(x, z, save);
    }

    private boolean unloadChunk0(int x, int z, boolean save) {
        net.minecraft.server.Chunk chunk = world.getChunkProviderServer().getChunkIfLoaded(x, z);
        if (chunk == null) {
            return true;
        }

        // If chunk had previously been queued to save, must do save to avoid loss of that data
        return world.getChunkProviderServer().unloadChunk(chunk, chunk.mustSave || save);
    }

    public boolean regenerateChunk(int x, int z) {
        if (!unloadChunk0(x, z, false)) {
            return false;
        }

        final long chunkKey = ChunkCoordIntPair.a(x, z);
        world.getChunkProviderServer().unloadQueue.remove(chunkKey);

        net.minecraft.server.Chunk chunk = null;

        chunk = world.getChunkProviderServer().chunkGenerator.getOrCreateChunk(x, z);
        PlayerChunk playerChunk = world.getPlayerChunkMap().getChunk(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = chunk;
        }

        if (chunk != null) {
            world.getChunkProviderServer().chunks.put(chunkKey, chunk);

            chunk.addEntities();
            chunk.loadNearby(world.getChunkProviderServer(), world.getChunkProviderServer().chunkGenerator, true);

            refreshChunk(x, z);
        }

        return chunk != null;
    }

    public boolean refreshChunk(int x, int z) {
        if (!isChunkLoaded(x, z)) {
            return false;
        }

        int px = x << 4;
        int pz = z << 4;

        // If there are more than 64 updates to a chunk at once, it will update all 'touched' sections within the chunk
        // And will include biome data if all sections have been 'touched'
        // This flags 65 blocks distributed across all the sections of the chunk, so that everything is sent, including biomes
        int height = getMaxHeight() / 16;
        for (int idx = 0; idx < 64; idx++) {
            world.notify(new BlockPosition(px + (idx / height), ((idx % height) * 16), pz), Blocks.AIR.getBlockData(), Blocks.STONE.getBlockData(), 3);
        }
        world.notify(new BlockPosition(px + 15, (height * 16) - 1, pz + 15), Blocks.AIR.getBlockData(), Blocks.STONE.getBlockData(), 3);

        return true;
    }

    public boolean isChunkInUse(int x, int z) {
        return world.getPlayerChunkMap().isChunkInUse(x, z);
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        chunkLoadCount++;
        if (generate) {
            // Use the default variant of loadChunk when generate == true.
            return world.getChunkProviderServer().getChunkAt(x, z) != null;
        }

        return world.getChunkProviderServer().getOrLoadChunkAt(x, z) != null;
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
        Validate.notNull(item, "Cannot drop a Null item.");
        Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
        EntityItem entity = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        entity.pickupDelay = 10;
        world.addEntity(entity, SpawnReason.CUSTOM);
        // TODO this is inconsistent with how Entity.getBukkitEntity() works.
        // However, this entity is not at the moment backed by a server entity class so it may be left.
        return new CraftItem(world.getServer(), entity);
    }

    private static void randomLocationWithinBlock(Location loc, double xs, double ys, double zs) {
        double prevX = loc.getX();
        double prevY = loc.getY();
        double prevZ = loc.getZ();
        loc.add(xs, ys, zs);
        if (loc.getX() < Math.floor(prevX)) {
            loc.setX(Math.floor(prevX));
        }
        if (loc.getX() >= Math.ceil(prevX)) {
            loc.setX(Math.ceil(prevX - 0.01));
        }
        if (loc.getY() < Math.floor(prevY)) {
            loc.setY(Math.floor(prevY));
        }
        if (loc.getY() >= Math.ceil(prevY)) {
            loc.setY(Math.ceil(prevY - 0.01));
        }
        if (loc.getZ() < Math.floor(prevZ)) {
            loc.setZ(Math.floor(prevZ));
        }
        if (loc.getZ() >= Math.ceil(prevZ)) {
            loc.setZ(Math.ceil(prevZ - 0.01));
        }
    }

    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        double xs = world.random.nextFloat() * 0.7F - 0.35D;
        double ys = world.random.nextFloat() * 0.7F - 0.35D;
        double zs = world.random.nextFloat() * 0.7F - 0.35D;
        loc = loc.clone();
        // Makes sure the new item is created within the block the location points to.
        // This prevents item spill in 1-block wide farms.
        randomLocationWithinBlock(loc, xs, ys, zs);
        return dropItem(loc, item);
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        return spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }

    public <T extends Arrow> T spawnArrow(Location loc, Vector velocity, float speed, float spread, Class<T> clazz) {
        Validate.notNull(loc, "Can not spawn arrow with a null location");
        Validate.notNull(velocity, "Can not spawn arrow with a null velocity");
        Validate.notNull(clazz, "Can not spawn an arrow with no class");

        EntityArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = new EntityTippedArrow(world);
            ((EntityTippedArrow) arrow).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
        } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = new EntitySpectralArrow(world);
        } else {
            arrow = new EntityTippedArrow(world);
        }

        arrow.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        world.addEntity(arrow);
        return (T) arrow.getBukkitEntity();
    }

    public Entity spawnEntity(Location loc, EntityType entityType) {
        return spawn(loc, entityType.getEntityClass());
    }

    public LightningStrike strikeLightning(Location loc) {
        EntityLightning lightning = new EntityLightning(world, loc.getX(), loc.getY(), loc.getZ(), false);
        world.strikeLightning(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public LightningStrike strikeLightningEffect(Location loc) {
        EntityLightning lightning = new EntityLightning(world, loc.getX(), loc.getY(), loc.getZ(), true);
        world.strikeLightning(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public boolean generateTree(Location loc, TreeType type) {
        BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        net.minecraft.server.WorldGenerator gen;
        switch (type) {
        case BIG_TREE:
            gen = new WorldGenBigTree(true);
            break;
        case BIRCH:
            gen = new WorldGenForest(true, false);
            break;
        case REDWOOD:
            gen = new WorldGenTaiga2(true);
            break;
        case TALL_REDWOOD:
            gen = new WorldGenTaiga1();
            break;
        case JUNGLE:
            IBlockData iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenJungleTree(true, 10, 20, iblockdata1, iblockdata2); // Magic values as in BlockSapling
            break;
        case SMALL_JUNGLE:
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, false);
            break;
        case COCOA_TREE:
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, true);
            break;
        case JUNGLE_BUSH:
            iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
            iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
            gen = new WorldGenGroundBush(iblockdata1, iblockdata2);
            break;
        case RED_MUSHROOM:
            gen = new WorldGenHugeMushroom(Blocks.RED_MUSHROOM_BLOCK);
            break;
        case BROWN_MUSHROOM:
            gen = new WorldGenHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
            break;
        case SWAMP:
            gen = new WorldGenSwampTree();
            break;
        case ACACIA:
            gen = new WorldGenAcaciaTree(true);
            break;
        case DARK_OAK:
            gen = new WorldGenForestTree(true);
            break;
        case MEGA_REDWOOD:
            gen = new WorldGenMegaTree(false, rand.nextBoolean());
            break;
        case TALL_BIRCH:
            gen = new WorldGenForest(true, true);
            break;
        case CHORUS_PLANT:
            BlockChorusFlower.a(world, pos, rand, 8);
            return true;
        case TREE:
        default:
            gen = new WorldGenTrees(true);
            break;
        }

        return gen.generate(world, rand, pos);
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        world.captureTreeGeneration = true;
        world.captureBlockStates = true;
        boolean grownTree = generateTree(loc, type);
        world.captureBlockStates = false;
        world.captureTreeGeneration = false;
        if (grownTree) { // Copy block data to delegate
            for (BlockState blockstate : world.capturedBlockStates) {
                int x = blockstate.getX();
                int y = blockstate.getY();
                int z = blockstate.getZ();
                BlockPosition position = new BlockPosition(x, y, z);
                net.minecraft.server.IBlockData oldBlock = world.getType(position);
                int typeId = blockstate.getTypeId();
                int data = blockstate.getRawData();
                int flag = ((CraftBlockState)blockstate).getFlag();
                delegate.setTypeIdAndData(x, y, z, typeId, data);
                net.minecraft.server.IBlockData newBlock = world.getType(position);
                world.notifyAndUpdatePhysics(position, null, oldBlock, newBlock, flag);
            }
            world.capturedBlockStates.clear();
            return true;
        } else {
            world.capturedBlockStates.clear();
            return false;
        }
    }

    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.getTileEntity(new BlockPosition(x, y, z));
    }

    public String getName() {
        return world.worldData.getName();
    }

    @Deprecated
    public long getId() {
        return world.worldData.getSeed();
    }

    public UUID getUID() {
        return world.getDataManager().getUUID();
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
        return world.getDayTime();
    }

    public void setFullTime(long time) {
        world.setDayTime(time);

        // Forces the client to update to the new time immediately
        for (Player p : getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().playerConnection == null) continue;

            cp.getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateTime(cp.getHandle().world.getTime(), cp.getHandle().getPlayerTime(), cp.getHandle().world.getGameRules().getBoolean("doDaylightCycle")));
        }
    }

    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return !world.createExplosion(null, x, y, z, power, setFire, breakBlocks).wasCanceled;
    }

    public boolean createExplosion(Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment env) {
        if (environment != env) {
            environment = env;
            switch (env) {
                case NORMAL:
                    world.worldProvider = new WorldProviderNormal();
                    break;
                case NETHER:
                    world.worldProvider = new WorldProviderHell();
                    break;
                case THE_END:
                    world.worldProvider = new WorldProviderTheEnd();
                    break;
            }
        }
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

    public ChunkGenerator getGenerator() {
        return generator;
    }

    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    public Biome getBiome(int x, int z) {
        return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPosition(x, 0, z)));
    }

    public void setBiome(int x, int z, Biome bio) {
        BiomeBase bb = CraftBlock.biomeToBiomeBase(bio);
        if (this.world.isLoaded(new BlockPosition(x, 0, z))) {
            net.minecraft.server.Chunk chunk = this.world.getChunkAtWorldCoords(new BlockPosition(x, 0, z));

            if (chunk != null) {
                byte[] biomevals = chunk.getBiomeIndex();
                biomevals[((z & 0xF) << 4) | (x & 0xF)] = (byte) BiomeBase.REGISTRY_ID.a(bb);
            }
        }
    }

    public double getTemperature(int x, int z) {
        return this.world.getBiome(new BlockPosition(x, 0, z)).getTemperature();
    }

    public double getHumidity(int x, int z) {
        return this.world.getBiome(new BlockPosition(x, 0, z)).getHumidity();
    }

    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object o : world.entityList) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt = (net.minecraft.server.Entity) o;
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

        for (Object o : world.entityList) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt = (net.minecraft.server.Entity) o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkitEntity != null && bukkitEntity instanceof LivingEntity) {
                    list.add((LivingEntity) bukkitEntity);
                }
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>)getEntitiesByClasses(classes);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        for (Object entity: world.entityList) {
            if (entity instanceof net.minecraft.server.Entity) {
                Entity bukkitEntity = ((net.minecraft.server.Entity) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                if (clazz.isAssignableFrom(bukkitClass)) {
                    list.add((T) bukkitEntity);
                }
            }
        }

        return list;
    }

    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<Entity>();

        for (Object entity: world.entityList) {
            if (entity instanceof net.minecraft.server.Entity) {
                Entity bukkitEntity = ((net.minecraft.server.Entity) entity).getBukkitEntity();

                if (bukkitEntity == null) {
                    continue;
                }

                Class<?> bukkitClass = bukkitEntity.getClass();

                for (Class<?> clazz : classes) {
                    if (clazz.isAssignableFrom(bukkitClass)) {
                        list.add(bukkitEntity);
                        break;
                    }
                }
            }
        }

        return list;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        if (location == null || !location.getWorld().equals(this)) {
            return Collections.emptyList();
        }

        AxisAlignedBB bb = new AxisAlignedBB(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
        List<net.minecraft.server.Entity> entityList = getHandle().getEntities((net.minecraft.server.Entity) null, bb, null);
        List<Entity> bukkitEntityList = new ArrayList<org.bukkit.entity.Entity>(entityList.size());
        for (Object entity : entityList) {
            bukkitEntityList.add(((net.minecraft.server.Entity) entity).getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(world.players.size());

        for (EntityHuman human : world.players) {
            HumanEntity bukkitEntity = human.getBukkitEntity();

            if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    public void save() {
        this.server.checkSaveState();
        try {
            boolean oldSave = world.savingDisabled;

            world.savingDisabled = false;
            world.save(true, null);

            world.savingDisabled = oldSave;
        } catch (ExceptionWorldConflict ex) {
            ex.printStackTrace();
        }
    }

    public boolean isAutoSave() {
        return !world.savingDisabled;
    }

    public void setAutoSave(boolean value) {
        world.savingDisabled = !value;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().worldData.setDifficulty(EnumDifficulty.getById(difficulty.getValue()));
    }

    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    public BlockMetadataStore getBlockMetadata() {
        return blockMetadata;
    }

    public boolean hasStorm() {
        return world.worldData.hasStorm();
    }

    public void setStorm(boolean hasStorm) {
        world.worldData.setStorm(hasStorm);
    }

    public int getWeatherDuration() {
        return world.worldData.getWeatherDuration();
    }

    public void setWeatherDuration(int duration) {
        world.worldData.setWeatherDuration(duration);
    }

    public boolean isThundering() {
        return world.worldData.isThundering();
    }

    public void setThundering(boolean thundering) {
        world.worldData.setThundering(thundering);
    }

    public int getThunderDuration() {
        return world.worldData.getThunderDuration();
    }

    public void setThunderDuration(int duration) {
        world.worldData.setThunderDuration(duration);
    }

    public long getSeed() {
        return world.worldData.getSeed();
    }

    public boolean getPVP() {
        return world.pvpMode;
    }

    public void setPVP(boolean pvp) {
        world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        playEffect(player.getLocation(), effect, data, 0);
    }

    public void playEffect(Location location, Effect effect, int data) {
        playEffect(location, effect, data, 64);
    }

    public <T> void playEffect(Location loc, Effect effect, T data) {
        playEffect(loc, effect, data, 64);
    }

    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue, radius);
    }

    public void playEffect(Location location, Effect effect, int data, int radius) {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(effect, "Effect cannot be null");
        Validate.notNull(location.getWorld(), "World cannot be null");
        int packetData = effect.getId();
        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(packetData, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), data, false);
        int distance;
        radius *= radius;

        for (Player player : getPlayers()) {
            if (((CraftPlayer) player).getHandle().playerConnection == null) continue;
            if (!location.getWorld().equals(player.getWorld())) continue;

            distance = (int) player.getLocation().distanceSquared(location);
            if (distance <= radius) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, SpawnReason.CUSTOM);
    }

    public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data) throws IllegalArgumentException {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(material.isBlock(), "Material must be a block");

        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY() + 0.5;
        double z = location.getBlockZ() + 0.5;

        EntityFallingBlock entity = new EntityFallingBlock(world, x, y, z, net.minecraft.server.Block.getById(material.getId()).fromLegacyData(data));
        entity.ticksLived = 1;

        world.addEntity(entity, SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData) throws IllegalArgumentException {
        return spawnFallingBlock(location, org.bukkit.Material.getMaterial(blockId), blockData);
    }

    @SuppressWarnings("unchecked")
    public net.minecraft.server.Entity createEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }

        net.minecraft.server.Entity entity = null;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        // order is important for some of these
        if (Boat.class.isAssignableFrom(clazz)) {
            entity = new EntityBoat(world, x, y, z);
        } else if (FallingBlock.class.isAssignableFrom(clazz)) {
            x = location.getBlockX();
            y = location.getBlockY();
            z = location.getBlockZ();
            IBlockData blockData = world.getType(new BlockPosition(x, y, z));
            int type = CraftMagicNumbers.getId(blockData.getBlock());
            int data = blockData.getBlock().toLegacyData(blockData);

            entity = new EntityFallingBlock(world, x + 0.5, y + 0.5, z + 0.5, net.minecraft.server.Block.getById(type).fromLegacyData(data));
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new EntitySnowball(world, x, y, z);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EntityEgg(world, x, y, z);
            } else if (Arrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = new EntityTippedArrow(world);
                    ((EntityTippedArrow) entity).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
                    entity = new EntitySpectralArrow(world);
                } else {
                    entity = new EntityTippedArrow(world);
                }
                entity.setPositionRotation(x, y, z, 0, 0);
            } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = new EntityThrownExpBottle(world);
                entity.setPositionRotation(x, y, z, 0, 0);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderPearl(world);
                entity.setPositionRotation(x, y, z, 0, 0);
            } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                if (LingeringPotion.class.isAssignableFrom(clazz)) {
                    entity = new EntityPotion(world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
                } else {
                    entity = new EntityPotion(world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
                }
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntitySmallFireball(world);
                } else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = new EntityWitherSkull(world);
                } else if (DragonFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntityDragonFireball(world);
                } else {
                    entity = new EntityLargeFireball(world);
                }
                entity.setPositionRotation(x, y, z, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((EntityFireball) entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            } else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = new EntityShulkerBullet(world);
                entity.setPositionRotation(x, y, z, yaw, pitch);
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartFurnace(world, x, y, z);
            } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartChest(world, x, y, z);
            } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartTNT(world, x, y, z);
            } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartHopper(world, x, y, z);
            } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartMobSpawner(world, x, y, z);
            } else if (CommandMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartCommandBlock(world, x, y, z);
            } else { // Default to rideable minecart for pre-rideable compatibility
                entity = new EntityMinecartRideable(world, x, y, z);
            }
        } else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderSignal(world, x, y, z);
        } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderCrystal(world);
            entity.setPositionRotation(x, y, z, 0, 0);
        } else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = new EntityChicken(world);
            } else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = new EntityMushroomCow(world);
                } else {
                    entity = new EntityCow(world);
                }
            } else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = new EntitySnowman(world);
                } else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = new EntityIronGolem(world);
                } else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = new EntityShulker(world);
                }
            } else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = new EntityCreeper(world);
            } else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = new EntityGhast(world);
            } else if (Pig.class.isAssignableFrom(clazz)) {
                entity = new EntityPig(world);
            } else if (Player.class.isAssignableFrom(clazz)) {
                // need a net server handler for this one
            } else if (Sheep.class.isAssignableFrom(clazz)) {
                entity = new EntitySheep(world);
            } else if (Horse.class.isAssignableFrom(clazz)) {
                entity = new EntityHorse(world);
            } else if (Skeleton.class.isAssignableFrom(clazz)) {
                entity = new EntitySkeleton(world);
            } else if (Slime.class.isAssignableFrom(clazz)) {
                if (MagmaCube.class.isAssignableFrom(clazz)) {
                    entity = new EntityMagmaCube(world);
                } else {
                    entity = new EntitySlime(world);
                }
            } else if (Spider.class.isAssignableFrom(clazz)) {
                if (CaveSpider.class.isAssignableFrom(clazz)) {
                    entity = new EntityCaveSpider(world);
                } else {
                    entity = new EntitySpider(world);
                }
            } else if (Squid.class.isAssignableFrom(clazz)) {
                entity = new EntitySquid(world);
            } else if (Tameable.class.isAssignableFrom(clazz)) {
                if (Wolf.class.isAssignableFrom(clazz)) {
                    entity = new EntityWolf(world);
                } else if (Ocelot.class.isAssignableFrom(clazz)) {
                    entity = new EntityOcelot(world);
                }
            } else if (PigZombie.class.isAssignableFrom(clazz)) {
                entity = new EntityPigZombie(world);
            } else if (Zombie.class.isAssignableFrom(clazz)) {
                entity = new EntityZombie(world);
            } else if (Giant.class.isAssignableFrom(clazz)) {
                entity = new EntityGiantZombie(world);
            } else if (Silverfish.class.isAssignableFrom(clazz)) {
                entity = new EntitySilverfish(world);
            } else if (Enderman.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderman(world);
            } else if (Blaze.class.isAssignableFrom(clazz)) {
                entity = new EntityBlaze(world);
            } else if (Villager.class.isAssignableFrom(clazz)) {
                entity = new EntityVillager(world);
            } else if (Witch.class.isAssignableFrom(clazz)) {
                entity = new EntityWitch(world);
            } else if (Wither.class.isAssignableFrom(clazz)) {
                entity = new EntityWither(world);
            } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                if (EnderDragon.class.isAssignableFrom(clazz)) {
                    entity = new EntityEnderDragon(world);
                }
            } else if (Ambient.class.isAssignableFrom(clazz)) {
                if (Bat.class.isAssignableFrom(clazz)) {
                    entity = new EntityBat(world);
                }
            } else if (Rabbit.class.isAssignableFrom(clazz)) {
                entity = new EntityRabbit(world);
            } else if (Endermite.class.isAssignableFrom(clazz)) {
                entity = new EntityEndermite(world);
            } else if (Guardian.class.isAssignableFrom(clazz)) {
                entity = new EntityGuardian(world);
            } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                entity = new EntityArmorStand(world, x, y, z);
            } else if (PolarBear.class.isAssignableFrom(clazz)) {
                entity = new EntityPolarBear(world);
            }

            if (entity != null) {
                entity.setLocation(x, y, z, yaw, pitch);
            }
        } else if (Hanging.class.isAssignableFrom(clazz)) {
            Block block = getBlockAt(location);
            BlockFace face = BlockFace.SELF;

            int width = 16; // 1 full block, also painting smallest size.
            int height = 16; // 1 full block, also painting smallest size.

            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
            } else if (LeashHitch.class.isAssignableFrom(clazz)) {
                width = 9;
                height = 9;
            }

            BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
            final BlockPosition pos = new BlockPosition((int) x, (int) y, (int) z);
            for (BlockFace dir : faces) {
                net.minecraft.server.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
                if (nmsBlock.getBlockData().getMaterial().isBuildable() || BlockDiodeAbstract.isDiode(nmsBlock.getBlockData())) {
                    boolean taken = false;
                    AxisAlignedBB bb = EntityHanging.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).opposite(), width, height);
                    List<net.minecraft.server.Entity> list = (List<net.minecraft.server.Entity>) world.getEntities(null, bb);
                    for (Iterator<net.minecraft.server.Entity> it = list.iterator(); !taken && it.hasNext();) {
                        net.minecraft.server.Entity e = it.next();
                        if (e instanceof EntityHanging) {
                            taken = true; // Hanging entities do not like hanging entities which intersect them.
                        }
                    }

                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }

            if (LeashHitch.class.isAssignableFrom(clazz)) {
                entity = new EntityLeash(world, new BlockPosition((int) x, (int) y, (int) z));
                entity.attachedToPlayer = true;
            } else {
                EnumDirection dir = CraftBlock.blockFaceToNotch(face).opposite();
                if (Painting.class.isAssignableFrom(clazz)) {
                    entity = new EntityPainting(world, new BlockPosition((int) x, (int) y, (int) z), dir);
                } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    entity = new EntityItemFrame(world, new BlockPosition((int) x, (int) y, (int) z), dir);
                }
            }

            if (entity != null && !((EntityHanging) entity).survives()) {
                throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new EntityTNTPrimed(world, x, y, z, null);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new EntityExperienceOrb(world, x, y, z, 0);
        } else if (Weather.class.isAssignableFrom(clazz)) {
            // not sure what this can do
            if (LightningStrike.class.isAssignableFrom(clazz)) {
                entity = new EntityLightning(world, x, y, z, false);
                // what is this, I don't even
            }
        } else if (Firework.class.isAssignableFrom(clazz)) {
            entity = new EntityFireworks(world, x, y, z, null);
        } else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = new EntityAreaEffectCloud(world, x, y, z);
        }

        if (entity != null) {
            return entity;
        }

        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.server.Entity entity, SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        if (entity instanceof EntityInsentient) {
            ((EntityInsentient) entity).prepare(getHandle().D(new BlockPosition(entity)), (GroupDataEntity) null);
        }

        world.addEntity(entity, reason);
        return (T) entity.getBukkitEntity();
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, SpawnReason reason) throws IllegalArgumentException {
        net.minecraft.server.Entity entity = createEntity(location, clazz);

        return addEntity(entity, reason);
    }

    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setSpawnFlags(allowMonsters, allowAnimals);
    }

    public boolean getAllowAnimals() {
        return world.allowAnimals;
    }

    public boolean getAllowMonsters() {
        return world.allowMonsters;
    }

    public int getMaxHeight() {
        return world.getHeight();
    }

    public int getSeaLevel() {
        return 64;
    }

    public boolean getKeepSpawnInMemory() {
        return world.keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepLoaded) {
        world.keepSpawnInMemory = keepLoaded;
        // Grab the worlds spawn chunk
        BlockPosition chunkcoordinates = this.world.getSpawn();
        int chunkCoordX = chunkcoordinates.getX() >> 4;
        int chunkCoordZ = chunkcoordinates.getZ() >> 4;
        // Cycle through the 25x25 Chunks around it to load/unload the chunks.
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                if (keepLoaded) {
                    loadChunk(chunkCoordX + x, chunkCoordZ + z);
                } else {
                    if (isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                        unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return getUID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CraftWorld other = (CraftWorld) obj;

        return this.getUID() == other.getUID();
    }

    public File getWorldFolder() {
        return ((WorldNBTStorage) world.getDataManager()).getDirectory();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        for (Player player : getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    public org.bukkit.WorldType getWorldType() {
        return org.bukkit.WorldType.getByName(world.getWorldData().getType().name());
    }

    public boolean canGenerateStructures() {
        return world.getWorldData().shouldGenerateMapFeatures();
    }

    public long getTicksPerAnimalSpawns() {
        return world.ticksPerAnimalSpawns;
    }

    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
    }

    public long getTicksPerMonsterSpawns() {
        return world.ticksPerMonsterSpawns;
    }

    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public int getMonsterSpawnLimit() {
        if (monsterSpawn < 0) {
            return server.getMonsterSpawnLimit();
        }

        return monsterSpawn;
    }

    public void setMonsterSpawnLimit(int limit) {
        monsterSpawn = limit;
    }

    public int getAnimalSpawnLimit() {
        if (animalSpawn < 0) {
            return server.getAnimalSpawnLimit();
        }

        return animalSpawn;
    }

    public void setAnimalSpawnLimit(int limit) {
        animalSpawn = limit;
    }

    public int getWaterAnimalSpawnLimit() {
        if (waterAnimalSpawn < 0) {
            return server.getWaterAnimalSpawnLimit();
        }

        return waterAnimalSpawn;
    }

    public void setWaterAnimalSpawnLimit(int limit) {
        waterAnimalSpawn = limit;
    }

    public int getAmbientSpawnLimit() {
        if (ambientSpawn < 0) {
            return server.getAmbientSpawnLimit();
        }

        return ambientSpawn;
    }

    public void setAmbientSpawnLimit(int limit) {
        ambientSpawn = limit;
    }


    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (loc == null || sound == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        getHandle().a(null, x, y, z, CraftSound.getSoundEffect(CraftSound.getSound(sound)), SoundCategory.MASTER, volume, pitch); // PAIL: rename
    }

    public void playSound(Location loc, String sound, float volume, float pitch) {
        if (loc == null || sound == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(sound, SoundCategory.MASTER, x, y, z, volume, pitch);
        world.getMinecraftServer().getPlayerList().sendPacketNearby(null, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.world.dimension, packet);
    }

    public String getGameRuleValue(String rule) {
        return getHandle().getGameRules().get(rule);
    }

    public boolean setGameRuleValue(String rule, String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!isGameRule(rule)) return false;

        getHandle().getGameRules().set(rule, value);
        return true;
    }

    public String[] getGameRules() {
        return getHandle().getGameRules().getGameRules();
    }

    public boolean isGameRule(String rule) {
        return getHandle().getGameRules().contains(rule);
    }

    @Override
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new CraftWorldBorder(this);
        }

        return this.worldBorder;
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        getHandle().sendParticles(
                null, // Sender
                CraftParticle.toNMS(particle), // Particle
                true, // Extended range
                x, y, z, // Position
                count,  // Count
                offsetX, offsetY, offsetZ, // Random offset
                extra, // Speed?
                CraftParticle.toData(particle, data)

        );

    }

    public void processChunkGC() {
        chunkGCTickCount++;

        if (chunkLoadCount >= server.chunkGCLoadThresh && server.chunkGCLoadThresh > 0) {
            chunkLoadCount = 0;
        } else if (chunkGCTickCount >= server.chunkGCPeriod && server.chunkGCPeriod > 0) {
            chunkGCTickCount = 0;
        } else {
            return;
        }

        ChunkProviderServer cps = world.getChunkProviderServer();
        for (net.minecraft.server.Chunk chunk : cps.chunks.values()) {
            // If in use, skip it
            if (isChunkInUse(chunk.locX, chunk.locZ)) {
                continue;
            }

            // Already unloading?
            if (cps.unloadQueue.contains(ChunkCoordIntPair.a(chunk.locX, chunk.locZ))) {
                continue;
            }

            // Add unload request
            cps.unload(chunk);
        }
    }
}
