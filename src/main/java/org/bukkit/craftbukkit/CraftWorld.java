package org.bukkit.craftbukkit;

import com.google.common.collect.MapMaker;
import java.io.File;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.*;

import org.bukkit.entity.Arrow;
import org.bukkit.Effect;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.Chunk;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.Difficulty;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

public class CraftWorld implements World {
    private final WorldServer world;
    private Environment environment;
    private final CraftServer server = (CraftServer)Bukkit.getServer();
    private ConcurrentMap<Integer, CraftChunk> unloadedChunks = new MapMaker().weakValues().makeMap();
    private final ChunkGenerator generator;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();

    private static final Random rand = new Random();

    public CraftWorld(WorldServer world, ChunkGenerator gen, Environment env) {
        this.world = world;
        this.generator = gen;

        environment = env;
    }

    public void preserveChunk(CraftChunk chunk) {
        chunk.breakLink();
        unloadedChunks.put((chunk.getX() << 16) + chunk.getZ(), chunk);
    }

    public Chunk popPreservedChunk(int x, int z) {
        return unloadedChunks.remove((x << 16) + z);
    }

    public Block getBlockAt(int x, int y, int z) {
        return getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y & 0x7F, z & 0xF);
    }

    public int getBlockTypeIdAt(int x, int y, int z) {
        return world.getTypeId(x, y, z);
    }

    public int getHighestBlockYAt(int x, int z) {
        return world.getHighestBlockYAt(x, z);
    }

    public Location getSpawnLocation() {
        ChunkCoordinates spawn = world.getSpawn();
        return new Location(this, spawn.x, spawn.y, spawn.z);
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = getSpawnLocation();
            world.worldData.setSpawn(x, y, z);

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Chunk getChunkAt(int x, int z) {
        return this.world.chunkProviderServer.getChunkAt(x, z).bukkitChunk;
    }

    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    public boolean isChunkLoaded(int x, int z) {
        return world.chunkProviderServer.isChunkLoaded(x, z);
    }

    public Chunk[] getLoadedChunks() {
        Object[] chunks = world.chunkProviderServer.chunks.values().toArray();
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

        world.chunkProviderServer.queueUnload(x, z);

        return true;
    }

    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        if (safe && isChunkInUse(x, z)) {
            return false;
        }

        net.minecraft.server.Chunk chunk = world.chunkProviderServer.getOrCreateChunk(x, z);

        if (save && !(chunk instanceof EmptyChunk)) {
            chunk.removeEntities();
            world.chunkProviderServer.saveChunk(chunk);
            world.chunkProviderServer.saveChunkNOP(chunk);
        }

        preserveChunk((CraftChunk) chunk.bukkitChunk);
        world.chunkProviderServer.unloadQueue.remove(x, z);
        world.chunkProviderServer.chunks.remove(x, z);
        world.chunkProviderServer.chunkList.remove(chunk);

        return true;
    }

    public boolean regenerateChunk(int x, int z) {
        unloadChunk(x, z, false, false);

        world.chunkProviderServer.unloadQueue.remove(x, z);

        net.minecraft.server.Chunk chunk = null;

        if (world.chunkProviderServer.chunkProvider == null) {
            chunk = world.chunkProviderServer.emptyChunk;
        } else {
            chunk = world.chunkProviderServer.chunkProvider.getOrCreateChunk(x, z);
        }

        chunkLoadPostProcess(chunk, x, z);

        refreshChunk(x, z);

        return chunk != null;
    }

    public boolean refreshChunk(int x, int z) {
        if (!isChunkLoaded(x, z)) {
            return false;
        }

        int px = x << 4;
        int pz = z << 4;

        // If there are more than 10 updates to a chunk at once, it carries out the update as a cuboid
        // This flags 16 blocks in a line along the bottom for update and then flags a block at the opposite corner at the top
        // The cuboid that contains these 17 blocks covers the entire chunk
        // The server will compress the chunk and send it to all clients

        for (int xx = px; xx < (px + 16); xx++) {
            world.notify(xx, 0, pz);
        }
        world.notify(px, 127, pz + 15);

        return true;
    }


    public boolean isChunkInUse(int x, int z) {
        Player[] players = server.getOnlinePlayers();

        for (Player player : players) {
            Location loc = player.getLocation();
            if (loc.getWorld() != world.chunkProviderServer.world.getWorld()) {
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
            return world.chunkProviderServer.getChunkAt(x, z) != null;
        }

        world.chunkProviderServer.unloadQueue.remove(x, z);
        net.minecraft.server.Chunk chunk = (net.minecraft.server.Chunk) world.chunkProviderServer.chunks.get(x, z);

        if (chunk == null) {
            chunk = world.chunkProviderServer.loadChunk(x, z);

            chunkLoadPostProcess(chunk, x, z);
        }
        return chunk != null;
    }

    @SuppressWarnings("unchecked")
    private void chunkLoadPostProcess(net.minecraft.server.Chunk chunk, int x, int z) {
        if (chunk != null) {
            world.chunkProviderServer.chunks.put(x, z, chunk);
            world.chunkProviderServer.chunkList.add(chunk);

            chunk.loadNOP();
            chunk.addEntities();

            if (!chunk.done && world.chunkProviderServer.isChunkLoaded(x + 1, z + 1) && world.chunkProviderServer.isChunkLoaded(x, z + 1) && world.chunkProviderServer.isChunkLoaded(x + 1, z)) {
                world.chunkProviderServer.getChunkAt(world.chunkProviderServer, x, z);
            }

            if (world.chunkProviderServer.isChunkLoaded(x - 1, z) && !world.chunkProviderServer.getOrCreateChunk(x - 1, z).done && world.chunkProviderServer.isChunkLoaded(x - 1, z + 1) && world.chunkProviderServer.isChunkLoaded(x, z + 1) && world.chunkProviderServer.isChunkLoaded(x - 1, z)) {
                world.chunkProviderServer.getChunkAt(world.chunkProviderServer, x - 1, z);
            }

            if (world.chunkProviderServer.isChunkLoaded(x, z - 1) && !world.chunkProviderServer.getOrCreateChunk(x, z - 1).done && world.chunkProviderServer.isChunkLoaded(x + 1, z - 1) && world.chunkProviderServer.isChunkLoaded(x, z - 1) && world.chunkProviderServer.isChunkLoaded(x + 1, z)) {
                world.chunkProviderServer.getChunkAt(world.chunkProviderServer, x, z - 1);
            }

            if (world.chunkProviderServer.isChunkLoaded(x - 1, z - 1) && !world.chunkProviderServer.getOrCreateChunk(x - 1, z - 1).done && world.chunkProviderServer.isChunkLoaded(x - 1, z - 1) && world.chunkProviderServer.isChunkLoaded(x, z - 1) && world.chunkProviderServer.isChunkLoaded(x - 1, z)) {
                world.chunkProviderServer.getChunkAt(world.chunkProviderServer, x - 1, z - 1);
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
        CraftItemStack clone = new CraftItemStack(item);
        EntityItem entity = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), clone.getHandle());
        entity.pickupDelay = 10;
        world.addEntity(entity);
        // TODO this is inconsistent with how Entity.getBukkitEntity() works.
        // However, this entity is not at the moment backed by a server entity class so it may be left.
        return new CraftItem(world.getServer(), entity);
    }

    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        double xs = world.random.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        double ys = world.random.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        double zs = world.random.nextFloat() * 0.7F + (1.0F - 0.7F) * 0.5D;
        loc = loc.clone();
        loc.setX(loc.getX() + xs);
        loc.setY(loc.getY() + ys);
        loc.setZ(loc.getZ() + zs);
        return dropItem(loc, item);
    }

    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        EntityArrow arrow = new EntityArrow(world);
        arrow.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        world.addEntity(arrow);
        arrow.a(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        return (Arrow) arrow.getBukkitEntity();
    }

    public LivingEntity spawnCreature(Location loc, CreatureType creatureType) {
        LivingEntity creature;
        try {
            EntityLiving entityCreature = (EntityLiving) EntityTypes.a(creatureType.getName(), world);
            entityCreature.setPosition(loc.getX(), loc.getY(), loc.getZ());
            creature = (LivingEntity) CraftEntity.getEntity(server, entityCreature);
            world.addEntity(entityCreature, SpawnReason.CUSTOM);
        } catch (Exception e) {
            // if we fail, for any reason, return null.
            creature = null;
        }
        return creature;
    }

    public LightningStrike strikeLightning(Location loc) {
        EntityWeatherStorm lightning = new EntityWeatherStorm(world, loc.getX(), loc.getY(), loc.getZ());
        world.strikeLightning(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public LightningStrike strikeLightningEffect(Location loc) {
        EntityWeatherStorm lightning = new EntityWeatherStorm(world, loc.getX(), loc.getY(), loc.getZ(), true);
        world.strikeLightning(lightning);
        return new CraftLightningStrike(server, lightning);
    }

    public boolean generateTree(Location loc, TreeType type) {
        return generateTree(loc, type, world);
    }

    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        switch (type) {
            case BIG_TREE:
                return new WorldGenBigTree(false).generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case BIRCH:
                return new WorldGenForest(false).generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case REDWOOD:
                return new WorldGenTaiga2(false).generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case TALL_REDWOOD:
                return new WorldGenTaiga1().generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            case TREE:
            default:
                return new WorldGenTrees(false).generate(delegate, rand, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }
    }

    public TileEntity getTileEntityAt(final int x, final int y, final int z) {
        return world.getTileEntity(x, y, z);
    }

    public String getName() {
        return world.worldData.name;
    }

    @Deprecated
    public long getId() {
        return world.worldData.getSeed();
    }

    public UUID getUID() {
        return world.getUUID();
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
        return world.getTime();
    }

    public void setFullTime(long time) {
        world.setTime(time);

        // Forces the client to update to the new time immediately
        for (Player p: getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            cp.getHandle().netServerHandler.sendPacket(new Packet4UpdateTime(cp.getHandle().getPlayerTime()));
        }
    }

    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false);
    }

    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return world.createExplosion(null, x, y, z, power, setFire).wasCanceled ? false : true;
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
            world.worldProvider = WorldProvider.byDimension(environment.getId());
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
        BiomeBase base = getHandle().getWorldChunkManager().getBiome(x, z);

        return CraftBlock.biomeBaseToBiome(base);
    }

    public double getTemperature(int x, int z) {
        throw new UnsupportedOperationException("Not compatible with 1.8");
    }

    public double getHumidity(int x, int z) {
        throw new UnsupportedOperationException("Not compatible with 1.8");
    }

    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object o: world.entityList) {
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

        for (Object o: world.entityList) {
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

    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>();

        for (Object o : world.entityList) {
            if (o instanceof net.minecraft.server.Entity) {
                net.minecraft.server.Entity mcEnt = (net.minecraft.server.Entity) o;
                Entity bukkitEntity = mcEnt.getBukkitEntity();

                if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                    list.add((Player) bukkitEntity);
                }
            }
        }

        return list;
    }

    public void save() {
        boolean oldSave = world.savingDisabled;

        world.savingDisabled = false;
        world.save(true, null);

        world.savingDisabled = oldSave;
    }

    public boolean isAutoSave() {
        return !world.savingDisabled;
    }

    public void setAutoSave(boolean value) {
        world.savingDisabled = !value;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().difficulty = difficulty.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().difficulty);
    }

    public boolean hasStorm() {
        return world.worldData.hasStorm();
    }

    public void setStorm(boolean hasStorm) {
        CraftServer server = world.getServer();

        WeatherChangeEvent weather = new WeatherChangeEvent((org.bukkit.World) this, hasStorm);
        server.getPluginManager().callEvent(weather);
        if (!weather.isCancelled()) {
            world.worldData.setStorm(hasStorm);

            // These numbers are from Minecraft
            if (hasStorm) {
                setWeatherDuration(rand.nextInt(12000) + 12000);
            } else {
                setWeatherDuration(rand.nextInt(168000) + 12000);
            }
        }
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
        CraftServer server = world.getServer();

        ThunderChangeEvent thunder = new ThunderChangeEvent((org.bukkit.World) this, thundering);
        server.getPluginManager().callEvent(thunder);
        if (!thunder.isCancelled()) {
            world.worldData.setThundering(thundering);

            // These numbers are from Minecraft
            if (thundering) {
                setThunderDuration(rand.nextInt(12000) + 3600);
            } else {
                setThunderDuration(rand.nextInt(168000) + 12000);
            }
        }
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

    public void playEffect(Location location, Effect effect, int data, int radius) {
        int packetData = effect.getId();
        Packet61 packet = new Packet61(packetData, location.getBlockX(), location.getBlockY(), location.getBlockZ(), data);
        int distance;
        for (Player player : getPlayers()) {
            distance = (int) player.getLocation().distance(location);
            if (distance <= radius) {
                ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(packet);
            }
        }
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, SpawnReason.CUSTOM);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T spawn(Location location, Class<T> clazz, SpawnReason reason) throws IllegalArgumentException {
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
        } else if (FallingSand.class.isAssignableFrom(clazz)) {
            entity = new EntityFallingSand(world, x, y, z, 0, 0);
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new EntitySnowball(world, x, y, z);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EntityEgg(world, x, y, z);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderPearl(world, x, y, z);
            } else if (Arrow.class.isAssignableFrom(clazz)) {
                entity = new EntityArrow(world);
                entity.setPositionRotation(x, y, z, 0, 0);
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = new EntitySmallFireball(world);
                } else {
                    entity = new EntityFireball(world);
                }
                ((EntityFireball) entity).setPositionRotation(x, y, z, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((EntityFireball) entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecart(world, x, y, z, CraftMinecart.Type.PoweredMinecart.getId());
            } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecart(world, x, y, z, CraftMinecart.Type.StorageMinecart.getId());
            } else {
                entity = new EntityMinecart(world, x, y, z, CraftMinecart.Type.Minecart.getId());
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
            } else if (Snowman.class.isAssignableFrom(clazz)) {
                entity = new EntitySnowman(world);
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
            } else if (Skeleton.class.isAssignableFrom(clazz)) {
                entity = new EntitySkeleton(world);
            } else if (Slime.class.isAssignableFrom(clazz)) {
                entity = new EntitySlime(world);
            } else if (Spider.class.isAssignableFrom(clazz)) {
                if (CaveSpider.class.isAssignableFrom(clazz)) {
                    entity = new EntityCaveSpider(world);
                } else {
                    entity = new EntitySpider(world);
                }
            } else if (Squid.class.isAssignableFrom(clazz)) {
                entity = new EntitySquid(world);
            } else if (Wolf.class.isAssignableFrom(clazz)) {
                entity = new EntityWolf(world);
            } else if (PigZombie.class.isAssignableFrom(clazz)) {
                entity = new EntityPigZombie(world);
            } else if (Zombie.class.isAssignableFrom(clazz)) {
                entity = new EntityZombie(world);
            } else if (Silverfish.class.isAssignableFrom(clazz)) {
                entity = new EntitySilverfish(world);
            } else if (Enderman.class.isAssignableFrom(clazz)) {
                entity = new EntityEnderman(world);
            } else if (Blaze.class.isAssignableFrom(clazz)) {
                entity = new EntityBlaze(world);
            } else if (Villager.class.isAssignableFrom(clazz)) {
                entity = new EntityVillager(world);
            } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                if (EnderDragon.class.isAssignableFrom(clazz)) {
                    entity = new EntityEnderDragon(world);
                }
            }

            if (entity != null) {
                entity.setLocation(x, y, z, pitch, yaw);
            }
        } else if (Painting.class.isAssignableFrom(clazz)) {
            Block block = getBlockAt(location);
            BlockFace face = BlockFace.SELF;
            if (block.getRelative(BlockFace.EAST).getTypeId() == 0) {
                face = BlockFace.EAST;
            } else if (block.getRelative(BlockFace.NORTH).getTypeId() == 0) {
                face = BlockFace.NORTH;
            } else if (block.getRelative(BlockFace.WEST).getTypeId() == 0) {
                face = BlockFace.WEST;
            } else if (block.getRelative(BlockFace.SOUTH).getTypeId() == 0) {
                face = BlockFace.SOUTH;
            }
            int dir;
            switch(face) {
                case EAST:
                default:
                    dir = 0;
                    break;
                case NORTH:
                    dir = 1;
                    break;
                case WEST:
                    dir = 2;
                    break;
                case SOUTH:
                    dir = 3;;
                    break;
            }
            entity = new EntityPainting(world, (int) x, (int) y, (int) z, dir);
            if (!((EntityPainting) entity).j()) {
                entity = null;
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new EntityTNTPrimed(world, x, y, z);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new EntityExperienceOrb(world, x, y, z, 0);
        } else if (Weather.class.isAssignableFrom(clazz)) {
            // not sure what this can do
            entity = new EntityWeatherStorm(world, x, y, z);
        } else if (LightningStrike.class.isAssignableFrom(clazz)) {
            // what is this, I don't even
        } else if (Fish.class.isAssignableFrom(clazz)) {
            // this is not a fish, it's a bobber, and it's probably useless
            entity = new EntityFishingHook(world);
            entity.setLocation(x, y, z, pitch, yaw);
        }

        if (entity != null) {
            world.addEntity(entity, reason);
            return (T) entity.getBukkitEntity();
        }

        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
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
        return world.height;
    }

    public int getSeaLevel() {
        return world.seaLevel;
    }

    public boolean getKeepSpawnInMemory() {
        return world.keepSpawnInMemory;
    }

    public void setKeepSpawnInMemory(boolean keepLoaded) {
        world.keepSpawnInMemory = keepLoaded;
        // Grab the worlds spawn chunk
        ChunkCoordinates chunkcoordinates = this.world.getSpawn();
        int chunkCoordX = chunkcoordinates.x >> 4;
        int chunkCoordZ = chunkcoordinates.z >> 4;
        //  Cycle through the 25x25 Chunks around it to load/unload the chunks.
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                if (keepLoaded) {
                    loadChunk(chunkCoordX + x, chunkCoordZ + z);
                } else {
                    if (isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
                        if (this.getHandle().getChunkAt(chunkCoordX + x, chunkCoordZ + z) instanceof EmptyChunk) {
                            unloadChunk(chunkCoordX + x, chunkCoordZ + z, false);
                        } else {
                            unloadChunk(chunkCoordX + x, chunkCoordZ + z);
                        }
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
        return ((PlayerNBTManager)world.q()).a();
    }
}
