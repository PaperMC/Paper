package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntitySound;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateTime;
import net.minecraft.network.protocol.game.PacketPlayOutWorldEvent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.ChunkMapDistance;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunk;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.util.ArraySetSorted;
import net.minecraft.util.Unit;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.EntityLightning;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.raid.PersistentRaid;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunkExtension;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.storage.SavedFile;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FeatureFlag;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Raid;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.boss.CraftDragonBattle;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.generator.structure.CraftGeneratedStructure;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.util.CraftBiomeSearchResult;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.CraftRayTraceResult;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.craftbukkit.util.CraftStructureSearchResult;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BiomeSearchResult;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StructureSearchResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftWorld extends CraftRegionAccessor implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    private final WorldServer world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    private final ChunkGenerator generator;
    private final BiomeProvider biomeProvider;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private final Object2IntOpenHashMap<SpawnCategory> spawnCategoryLimit = new Object2IntOpenHashMap<>();
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

    private static final Random rand = new Random();

    public CraftWorld(WorldServer world, ChunkGenerator gen, BiomeProvider biomeProvider, Environment env) {
        this.world = world;
        this.generator = gen;
        this.biomeProvider = biomeProvider;

        environment = env;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return CraftBlock.at(world, new BlockPosition(x, y, z));
    }

    @Override
    public Location getSpawnLocation() {
        BlockPosition spawn = world.getSharedSpawnPos();
        float yaw = world.getSharedSpawnAngle();
        return CraftLocation.toBukkit(spawn, this, yaw, 0);
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return equals(location.getWorld()) ? setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw()) : false;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {
        try {
            Location previousLocation = getSpawnLocation();
            world.levelData.setSpawn(new BlockPosition(x, y, z), angle);

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            server.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return setSpawnLocation(x, y, z, 0.0F);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        net.minecraft.world.level.chunk.Chunk chunk = (net.minecraft.world.level.chunk.Chunk) this.world.getChunk(x, z, ChunkStatus.FULL, true);
        return new CraftChunk(chunk);
    }

    @NotNull
    @Override
    public Chunk getChunkAt(int x, int z, boolean generate) {
        if (generate) {
            return getChunkAt(x, z);
        }

        return new CraftChunk(getHandle(), x, z);
    }

    @Override
    public Chunk getChunkAt(Block block) {
        Preconditions.checkArgument(block != null, "null block");

        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return world.getChunkSource().isChunkLoaded(x, z);
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        try {
            return isChunkLoaded(x, z) || world.getChunkSource().chunkMap.read(new ChunkCoordIntPair(x, z)).get().isPresent();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Chunk[] getLoadedChunks() {
        Long2ObjectLinkedOpenHashMap<PlayerChunk> chunks = world.getChunkSource().chunkMap.visibleChunkMap;
        return chunks.values().stream().map(PlayerChunk::getFullChunkNow).filter(Objects::nonNull).map(CraftChunk::new).toArray(Chunk[]::new);
    }

    @Override
    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return unloadChunk0(x, z, save);
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        if (isChunkLoaded(x, z)) {
            world.getChunkSource().removeRegionTicket(TicketType.PLUGIN, new ChunkCoordIntPair(x, z), 1, Unit.INSTANCE);
        }

        return true;
    }

    private boolean unloadChunk0(int x, int z, boolean save) {
        if (!isChunkLoaded(x, z)) {
            return true;
        }
        net.minecraft.world.level.chunk.Chunk chunk = world.getChunk(x, z);

        chunk.setUnsaved(!save); // Use method call to account for persistentDataContainer
        unloadChunkRequest(x, z);

        world.getChunkSource().purgeUnload();
        return !isChunkLoaded(x, z);
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version! Unless you can fix it, this is not a bug :)");
        /*
        if (!unloadChunk0(x, z, false)) {
            return false;
        }

        final long chunkKey = ChunkCoordIntPair.pair(x, z);
        world.getChunkProvider().unloadQueue.remove(chunkKey);

        net.minecraft.server.Chunk chunk = world.getChunkProvider().generateChunk(x, z);
        PlayerChunk playerChunk = world.getPlayerChunkMap().getChunk(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = chunk;
        }

        if (chunk != null) {
            refreshChunk(x, z);
        }

        return chunk != null;
        */
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        PlayerChunk playerChunk = world.getChunkSource().chunkMap.visibleChunkMap.get(ChunkCoordIntPair.asLong(x, z));
        if (playerChunk == null) return false;

        playerChunk.getTickingChunkFuture().thenAccept(either -> {
            either.left().ifPresent(chunk -> {
                List<EntityPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);
                if (playersInRange.isEmpty()) return;

                ClientboundLevelChunkWithLightPacket refreshPacket = new ClientboundLevelChunkWithLightPacket(chunk, world.getLightEngine(), null, null);
                for (EntityPlayer player : playersInRange) {
                    if (player.connection == null) continue;

                    player.connection.send(refreshPacket);
                }
            });
        });

        return true;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return isChunkLoaded(x, z);
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        IChunkAccess chunk = world.getChunkSource().getChunk(x, z, generate ? ChunkStatus.FULL : ChunkStatus.EMPTY, true);

        // If generate = false, but the chunk already exists, we will get this back.
        if (chunk instanceof ProtoChunkExtension) {
            // We then cycle through again to get the full chunk immediately, rather than after the ticket addition
            chunk = world.getChunkSource().getChunk(x, z, ChunkStatus.FULL, true);
        }

        if (chunk instanceof net.minecraft.world.level.chunk.Chunk) {
            world.getChunkSource().addRegionTicket(TicketType.PLUGIN, new ChunkCoordIntPair(x, z), 1, Unit.INSTANCE);
            return true;
        }

        return false;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        return isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    @Override
    public void loadChunk(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        loadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "null plugin");
        Preconditions.checkArgument(plugin.isEnabled(), "plugin is not enabled");

        ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;

        if (chunkDistanceManager.addRegionTicketAtDistance(TicketType.PLUGIN_TICKET, new ChunkCoordIntPair(x, z), 2, plugin)) { // keep in-line with force loading, add at level 31
            this.getChunkAt(x, z); // ensure loaded
            return true;
        }

        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        return chunkDistanceManager.removeRegionTicketAtDistance(TicketType.PLUGIN_TICKET, new ChunkCoordIntPair(x, z), 2, plugin); // keep in-line with force loading, remove at level 31
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        chunkDistanceManager.removeAllTicketsFor(TicketType.PLUGIN_TICKET, 31, plugin); // keep in-line with force loading, remove at level 31
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        ArraySetSorted<Ticket<?>> tickets = chunkDistanceManager.tickets.get(ChunkCoordIntPair.asLong(x, z));

        if (tickets == null) {
            return Collections.emptyList();
        }

        ImmutableList.Builder<Plugin> ret = ImmutableList.builder();
        for (Ticket<?> ticket : tickets) {
            if (ticket.getType() == TicketType.PLUGIN_TICKET) {
                ret.add((Plugin) ticket.key);
            }
        }

        return ret.build();
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        Map<Plugin, ImmutableList.Builder<Chunk>> ret = new HashMap<>();
        ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;

        for (Long2ObjectMap.Entry<ArraySetSorted<Ticket<?>>> chunkTickets : chunkDistanceManager.tickets.long2ObjectEntrySet()) {
            long chunkKey = chunkTickets.getLongKey();
            ArraySetSorted<Ticket<?>> tickets = chunkTickets.getValue();

            Chunk chunk = null;
            for (Ticket<?> ticket : tickets) {
                if (ticket.getType() != TicketType.PLUGIN_TICKET) {
                    continue;
                }

                if (chunk == null) {
                    chunk = this.getChunkAt(ChunkCoordIntPair.getX(chunkKey), ChunkCoordIntPair.getZ(chunkKey));
                }

                ret.computeIfAbsent((Plugin) ticket.key, (key) -> ImmutableList.builder()).add(chunk);
            }
        }

        return ret.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (entry) -> entry.getValue().build()));
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return getHandle().getForcedChunks().contains(ChunkCoordIntPair.asLong(x, z));
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        getHandle().setChunkForced(x, z, forced);
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        Set<Chunk> chunks = new HashSet<>();

        for (long coord : getHandle().getForcedChunks()) {
            chunks.add(getChunkAt(ChunkCoordIntPair.getX(coord), ChunkCoordIntPair.getZ(coord)));
        }

        return Collections.unmodifiableCollection(chunks);
    }

    public WorldServer getHandle() {
        return world;
    }

    @Override
    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item) {
        return dropItem(loc, item, null);
    }

    @Override
    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item, Consumer<? super org.bukkit.entity.Item> function) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        EntityItem entity = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        org.bukkit.entity.Item itemEntity = (org.bukkit.entity.Item) entity.getBukkitEntity();
        entity.pickupDelay = 10;
        if (function != null) {
            function.accept(itemEntity);
        }
        world.addFreshEntity(entity, SpawnReason.CUSTOM);
        return itemEntity;
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        return dropItemNaturally(loc, item, null);
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item, Consumer<? super org.bukkit.entity.Item> function) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        double xs = (world.random.nextFloat() * 0.5F) + 0.25D;
        double ys = (world.random.nextFloat() * 0.5F) + 0.25D;
        double zs = (world.random.nextFloat() * 0.5F) + 0.25D;
        loc = loc.clone().add(xs, ys, zs);
        return dropItem(loc, item, function);
    }

    @Override
    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        return spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location loc, Vector velocity, float speed, float spread, Class<T> clazz) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(velocity != null, "Vector cannot be null");
        Preconditions.checkArgument(clazz != null, "clazz Entity for the arrow cannot be null");

        EntityArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = EntityTypes.ARROW.create(world);
            ((Arrow) arrow.getBukkitEntity()).setBasePotionType(PotionType.WATER);
        } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = EntityTypes.SPECTRAL_ARROW.create(world);
        } else if (Trident.class.isAssignableFrom(clazz)) {
            arrow = EntityTypes.TRIDENT.create(world);
        } else {
            arrow = EntityTypes.ARROW.create(world);
        }

        arrow.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        world.addFreshEntity(arrow);
        return (T) arrow.getBukkitEntity();
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        return strikeLightning0(loc, false);
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        return strikeLightning0(loc, true);
    }

    private LightningStrike strikeLightning0(Location loc, boolean isVisual) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        EntityLightning lightning = EntityTypes.LIGHTNING_BOLT.create(world);
        lightning.moveTo(loc.getX(), loc.getY(), loc.getZ());
        lightning.setVisualOnly(isVisual);
        world.strikeLightning(lightning, LightningStrikeEvent.Cause.CUSTOM);
        return (LightningStrike) lightning.getBukkitEntity();
    }

    @Override
    public boolean generateTree(Location loc, TreeType type) {
        return generateTree(loc, rand, type);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        world.captureTreeGeneration = true;
        world.captureBlockStates = true;
        boolean grownTree = generateTree(loc, type);
        world.captureBlockStates = false;
        world.captureTreeGeneration = false;
        if (grownTree) { // Copy block data to delegate
            for (BlockState blockstate : world.capturedBlockStates.values()) {
                BlockPosition position = ((CraftBlockState) blockstate).getPosition();
                net.minecraft.world.level.block.state.IBlockData oldBlock = world.getBlockState(position);
                int flag = ((CraftBlockState) blockstate).getFlag();
                delegate.setBlockData(blockstate.getX(), blockstate.getY(), blockstate.getZ(), blockstate.getBlockData());
                net.minecraft.world.level.block.state.IBlockData newBlock = world.getBlockState(position);
                world.notifyAndUpdatePhysics(position, null, oldBlock, newBlock, newBlock, flag, 512);
            }
            world.capturedBlockStates.clear();
            return true;
        } else {
            world.capturedBlockStates.clear();
            return false;
        }
    }

    @Override
    public String getName() {
        return world.serverLevelData.getLevelName();
    }

    @Override
    public UUID getUID() {
        return world.uuid;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(world.dimension().location());
    }

    @Override
    public String toString() {
        return "CraftWorld{name=" + getName() + '}';
    }

    @Override
    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) time += 24000;
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) margin += 24000;
        setFullTime(getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return world.getDayTime();
    }

    @Override
    public void setFullTime(long time) {
        // Notify anyone who's listening
        TimeSkipEvent event = new TimeSkipEvent(this, TimeSkipEvent.SkipReason.CUSTOM, time - world.getDayTime());
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        world.setDayTime(world.getDayTime() + event.getSkipAmount());

        // Forces the client to update to the new time immediately
        for (Player p : getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().connection == null) continue;

            cp.getHandle().connection.send(new PacketPlayOutUpdateTime(cp.getHandle().level().getGameTime(), cp.getHandle().getPlayerTime(), cp.getHandle().level().getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        }
    }

    @Override
    public long getGameTime() {
        return world.levelData.getGameTime();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source) {
        return !world.explode(source == null ? null : ((CraftEntity) source).getHandle(), x, y, z, power, setFire, breakBlocks ? net.minecraft.world.level.World.a.MOB : net.minecraft.world.level.World.a.NONE).wasCanceled;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return createExplosion(loc, power, setFire, true);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(loc, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        Preconditions.checkArgument(loc != null, "Location is null");
        Preconditions.checkArgument(this.equals(loc.getWorld()), "Location not in world");

        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source);
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    @Override
    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return biomeProvider;
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    @NotNull
    @Override
    public <T extends LivingEntity> T spawn(@NotNull Location location, @NotNull Class<T> clazz, @NotNull SpawnReason spawnReason, boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException {
        Preconditions.checkArgument(spawnReason != null, "Spawn reason cannot be null");
        return spawn(location, clazz, function, spawnReason, randomizeData);
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, org.bukkit.HeightMap heightMap) {
        // Transient load for this tick
        return world.getChunk(x >> 4, z >> 4).getHeight(CraftHeightMap.toNMS(heightMap), x, z);
    }

    @Override
    public Block getHighestBlockAt(int x, int z, org.bukkit.HeightMap heightMap) {
        return getBlockAt(x, getHighestBlockYAt(x, z, heightMap), z);
    }

    @Override
    public Block getHighestBlockAt(Location location, org.bukkit.HeightMap heightMap) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public Biome getBiome(int x, int z) {
        return getBiome(x, 0, z);
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        for (int y = getMinHeight(); y < getMaxHeight(); y++) {
            setBiome(x, y, z, bio);
        }
    }

    @Override
    public void setBiome(int x, int y, int z, Holder<BiomeBase> bb) {
        BlockPosition pos = new BlockPosition(x, 0, z);
        if (this.world.hasChunkAt(pos)) {
            net.minecraft.world.level.chunk.Chunk chunk = this.world.getChunkAt(pos);

            if (chunk != null) {
                chunk.setBiome(x >> 2, y >> 2, z >> 2, bb);

                chunk.setUnsaved(true); // SPIGOT-2890
            }
        }
    }

    @Override
    public double getTemperature(int x, int z) {
        return getTemperature(x, 0, z);
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        BlockPosition pos = new BlockPosition(x, y, z);
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).value().getTemperature(pos);
    }

    @Override
    public double getHumidity(int x, int z) {
        return getHumidity(x, 0, z);
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).value().climateSettings.downfall();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>) getEntitiesByClasses(classes);
    }

    @Override
    public Iterable<net.minecraft.world.entity.Entity> getNMSEntities() {
        return getHandle().getEntities().getAll();
    }

    @Override
    public void addEntityToWorld(net.minecraft.world.entity.Entity entity, SpawnReason reason) {
        getHandle().addFreshEntity(entity, reason);
    }

    @Override
    public void addEntityWithPassengers(net.minecraft.world.entity.Entity entity, SpawnReason reason) {
        getHandle().tryAddFreshEntityWithPassengers(entity, reason);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        return this.getNearbyEntities(location, x, y, z, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<? super Entity> filter) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(this.equals(location.getWorld()), "Location cannot be in a different world");

        BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return this.getNearbyEntities(aabb, filter);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return this.getNearbyEntities(boundingBox, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<? super Entity> filter) {
        Preconditions.checkArgument(boundingBox != null, "BoundingBox cannot be null");

        AxisAlignedBB bb = new AxisAlignedBB(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
        List<net.minecraft.world.entity.Entity> entityList = getHandle().getEntities((net.minecraft.world.entity.Entity) null, bb, Predicates.alwaysTrue());
        List<Entity> bukkitEntityList = new ArrayList<org.bukkit.entity.Entity>(entityList.size());

        for (net.minecraft.world.entity.Entity entity : entityList) {
            Entity bukkitEntity = entity.getBukkitEntity();
            if (filter == null || filter.test(bukkitEntity)) {
                bukkitEntityList.add(bukkitEntity);
            }
        }

        return bukkitEntityList;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        return this.rayTraceEntities(start, direction, maxDistance, null);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        return this.rayTraceEntities(start, direction, maxDistance, raySize, null);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<? super Entity> filter) {
        return this.rayTraceEntities(start, direction, maxDistance, 0.0D, filter);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize, Predicate<? super Entity> filter) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(this.equals(start.getWorld()), "Location start cannot be in a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();

        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) need to be greater than 0", direction.lengthSquared());

        if (maxDistance < 0.0D) {
            return null;
        }

        Vector startPos = start.toVector();
        Vector dir = direction.clone().normalize().multiply(maxDistance);
        BoundingBox aabb = BoundingBox.of(startPos, startPos).expandDirectional(dir).expand(raySize);
        Collection<Entity> entities = this.getNearbyEntities(aabb, filter);

        Entity nearestHitEntity = null;
        RayTraceResult nearestHitResult = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : entities) {
            BoundingBox boundingBox = entity.getBoundingBox().expand(raySize);
            RayTraceResult hitResult = boundingBox.rayTrace(startPos, direction, maxDistance);

            if (hitResult != null) {
                double distanceSq = startPos.distanceSquared(hitResult.getHitPosition());

                if (distanceSq < nearestDistanceSq) {
                    nearestHitEntity = entity;
                    nearestHitResult = hitResult;
                    nearestDistanceSq = distanceSq;
                }
            }
        }

        return (nearestHitEntity == null) ? null : new RayTraceResult(nearestHitResult.getHitPosition(), nearestHitEntity, nearestHitResult.getHitBlockFace());
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        return this.rayTraceBlocks(start, direction, maxDistance, FluidCollisionMode.NEVER, false);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(this.equals(start.getWorld()), "Location start cannot be in a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();

        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) need to be greater than 0", direction.lengthSquared());
        Preconditions.checkArgument(fluidCollisionMode != null, "FluidCollisionMode cannot be null");

        if (maxDistance < 0.0D) {
            return null;
        }

        Vector dir = direction.clone().normalize().multiply(maxDistance);
        Vec3D startPos = CraftLocation.toVec3D(start);
        Vec3D endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());
        MovingObjectPosition nmsHitResult = this.getHandle().clip(new RayTrace(startPos, endPos, ignorePassableBlocks ? RayTrace.BlockCollisionOption.COLLIDER : RayTrace.BlockCollisionOption.OUTLINE, CraftFluidCollisionMode.toNMS(fluidCollisionMode), VoxelShapeCollision.empty()));

        return CraftRayTraceResult.fromNMS(this, nmsHitResult);
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<? super Entity> filter) {
        RayTraceResult blockHit = this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks);
        Vector startVec = null;
        double blockHitDistance = maxDistance;

        // limiting the entity search range if we found a block hit:
        if (blockHit != null) {
            startVec = start.toVector();
            blockHitDistance = startVec.distance(blockHit.getHitPosition());
        }

        RayTraceResult entityHit = this.rayTraceEntities(start, direction, blockHitDistance, raySize, filter);
        if (blockHit == null) {
            return entityHit;
        }

        if (entityHit == null) {
            return blockHit;
        }

        // Cannot be null as blockHit == null returns above
        double entityHitDistanceSquared = startVec.distanceSquared(entityHit.getHitPosition());
        if (entityHitDistanceSquared < (blockHitDistance * blockHitDistance)) {
            return entityHit;
        }

        return blockHit;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(world.players().size());

        for (EntityHuman human : world.players()) {
            HumanEntity bukkitEntity = human.getBukkitEntity();

            if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    @Override
    public void save() {
        this.server.checkSaveState();
        boolean oldSave = world.noSave;

        world.noSave = false;
        world.save(null, false, false);

        world.noSave = oldSave;
    }

    @Override
    public boolean isAutoSave() {
        return !world.noSave;
    }

    @Override
    public void setAutoSave(boolean value) {
        world.noSave = !value;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().serverLevelData.setDifficulty(EnumDifficulty.byId(difficulty.getValue()));
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    @Override
    public int getViewDistance() {
        return world.getChunkSource().chunkMap.serverViewDistance;
    }

    @Override
    public int getSimulationDistance() {
        return world.getChunkSource().chunkMap.getDistanceManager().simulationDistance;
    }

    public BlockMetadataStore getBlockMetadata() {
        return blockMetadata;
    }

    @Override
    public boolean hasStorm() {
        return world.levelData.isRaining();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        world.levelData.setRaining(hasStorm);
        setWeatherDuration(0); // Reset weather duration (legacy behaviour)
        setClearWeatherDuration(0); // Reset clear weather duration (reset "/weather clear" commands)
    }

    @Override
    public int getWeatherDuration() {
        return world.serverLevelData.getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        world.serverLevelData.setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return world.levelData.isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        world.serverLevelData.setThundering(thundering);
        setThunderDuration(0); // Reset weather duration (legacy behaviour)
        setClearWeatherDuration(0); // Reset clear weather duration (reset "/weather clear" commands)
    }

    @Override
    public int getThunderDuration() {
        return world.serverLevelData.getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        world.serverLevelData.setThunderTime(duration);
    }

    @Override
    public boolean isClearWeather() {
        return !this.hasStorm() && !this.isThundering();
    }

    @Override
    public void setClearWeatherDuration(int duration) {
        world.serverLevelData.setClearWeatherTime(duration);
    }

    @Override
    public int getClearWeatherDuration() {
        return world.serverLevelData.getClearWeatherTime();
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public boolean getPVP() {
        return world.pvpMode;
    }

    @Override
    public void setPVP(boolean pvp) {
        world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        playEffect(player.getLocation(), effect, data, 0);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        playEffect(location, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        playEffect(loc, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Preconditions.checkArgument(effect.getData() != null, "Effect.%s does not have a valid Data", effect);
            Preconditions.checkArgument(effect.getData().isAssignableFrom(data.getClass()), "%s data cannot be used for the %s effect", data.getClass().getName(), effect);
        } else {
            // Special case: the axis is optional for ELECTRIC_SPARK
            Preconditions.checkArgument(effect.getData() == null || effect == Effect.ELECTRIC_SPARK, "Wrong kind of data for the %s effect", effect);
        }

        int datavalue = CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue, radius);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "World of Location cannot be null");
        int packetData = effect.getId();
        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(packetData, CraftLocation.toBlockPosition(location), data, false);
        int distance;
        radius *= radius;

        for (Player player : getPlayers()) {
            if (((CraftPlayer) player).getHandle().connection == null) continue;
            if (!location.getWorld().equals(player.getWorld())) continue;

            distance = (int) player.getLocation().distanceSquared(location);
            if (distance <= radius) {
                ((CraftPlayer) player).getHandle().connection.send(packet);
            }
        }
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        Preconditions.checkArgument(data != null, "MaterialData cannot be null");
        return spawnFallingBlock(location, data.getItemType(), data.getData());
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isBlock(), "Material.%s must be a block", material);

        EntityFallingBlock entity = EntityFallingBlock.fall(world, BlockPosition.containing(location.getX(), location.getY(), location.getZ()), CraftBlockType.bukkitToMinecraft(material).defaultBlockState(), SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(data != null, "BlockData cannot be null");

        EntityFallingBlock entity = EntityFallingBlock.fall(world, BlockPosition.containing(location.getX(), location.getY(), location.getZ()), ((CraftBlockData) data).getState(), SpawnReason.CUSTOM);
        return (FallingBlock) entity.getBukkitEntity();
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setSpawnSettings(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return world.getChunkSource().spawnFriendlies;
    }

    @Override
    public boolean getAllowMonsters() {
        return world.getChunkSource().spawnEnemies;
    }

    @Override
    public int getMinHeight() {
        return world.getMinBuildHeight();
    }

    @Override
    public int getMaxHeight() {
        return world.getMaxBuildHeight();
    }

    @Override
    public int getLogicalHeight() {
        return world.dimensionType().logicalHeight();
    }

    @Override
    public boolean isNatural() {
        return world.dimensionType().natural();
    }

    @Override
    public boolean isBedWorks() {
        return world.dimensionType().bedWorks();
    }

    @Override
    public boolean hasSkyLight() {
        return world.dimensionType().hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return world.dimensionType().hasCeiling();
    }

    @Override
    public boolean isPiglinSafe() {
        return world.dimensionType().piglinSafe();
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        return world.dimensionType().respawnAnchorWorks();
    }

    @Override
    public boolean hasRaids() {
        return world.dimensionType().hasRaids();
    }

    @Override
    public boolean isUltraWarm() {
        return world.dimensionType().ultraWarm();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return world.keepSpawnInMemory;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        world.keepSpawnInMemory = keepLoaded;
        // Grab the worlds spawn chunk
        BlockPosition chunkcoordinates = this.world.getSharedSpawnPos();
        if (keepLoaded) {
            world.getChunkSource().addRegionTicket(TicketType.START, new ChunkCoordIntPair(chunkcoordinates), 11, Unit.INSTANCE);
        } else {
            // TODO: doesn't work well if spawn changed....
            world.getChunkSource().removeRegionTicket(TicketType.START, new ChunkCoordIntPair(chunkcoordinates), 11, Unit.INSTANCE);
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

    @Override
    public File getWorldFolder() {
        return world.convertable.getLevelPath(SavedFile.ROOT).toFile().getParentFile();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        for (Player player : getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Override
    public org.bukkit.WorldType getWorldType() {
        return world.isFlat() ? org.bukkit.WorldType.FLAT : org.bukkit.WorldType.NORMAL;
    }

    @Override
    public boolean canGenerateStructures() {
        return world.serverLevelData.worldGenOptions().generateStructures();
    }

    @Override
    public boolean isHardcore() {
        return world.getLevelData().isHardcore();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        world.serverLevelData.settings.hardcore = hardcore;
    }

    @Override
    @Deprecated
    public long getTicksPerAnimalSpawns() {
        return getTicksPerSpawns(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        setTicksPerSpawns(SpawnCategory.ANIMAL, ticksPerAnimalSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerMonsterSpawns() {
        return getTicksPerSpawns(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        setTicksPerSpawns(SpawnCategory.MONSTER, ticksPerMonsterSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterSpawns() {
        return getTicksPerSpawns(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        setTicksPerSpawns(SpawnCategory.WATER_ANIMAL, ticksPerWaterSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterAmbientSpawns() {
        return getTicksPerSpawns(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterAmbientSpawns(int ticksPerWaterAmbientSpawns) {
        setTicksPerSpawns(SpawnCategory.WATER_AMBIENT, ticksPerWaterAmbientSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        return getTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {
        setTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE, ticksPerWaterUndergroundCreatureSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerAmbientSpawns() {
        return getTicksPerSpawns(SpawnCategory.AMBIENT);
    }

    @Override
    @Deprecated
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        setTicksPerSpawns(SpawnCategory.AMBIENT, ticksPerAmbientSpawns);
    }

    @Override
    public void setTicksPerSpawns(SpawnCategory spawnCategory, int ticksPerCategorySpawn) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        world.ticksPerSpawnCategory.put(spawnCategory, (long) ticksPerCategorySpawn);
    }

    @Override
    public long getTicksPerSpawns(SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        return world.ticksPerSpawnCategory.getLong(spawnCategory);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    @Deprecated
    public int getMonsterSpawnLimit() {
        return getSpawnLimit(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public void setMonsterSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.MONSTER, limit);
    }

    @Override
    @Deprecated
    public int getAnimalSpawnLimit() {
        return getSpawnLimit(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public void setAnimalSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.ANIMAL, limit);
    }

    @Override
    @Deprecated
    public int getWaterAnimalSpawnLimit() {
        return getSpawnLimit(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public void setWaterAnimalSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.WATER_ANIMAL, limit);
    }

    @Override
    @Deprecated
    public int getWaterAmbientSpawnLimit() {
        return getSpawnLimit(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public void setWaterAmbientSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.WATER_AMBIENT, limit);
    }

    @Override
    @Deprecated
    public int getWaterUndergroundCreatureSpawnLimit() {
        return getSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public void setWaterUndergroundCreatureSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE, limit);
    }

    @Override
    @Deprecated
    public int getAmbientSpawnLimit() {
        return getSpawnLimit(SpawnCategory.AMBIENT);
    }

    @Override
    @Deprecated
    public void setAmbientSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.AMBIENT, limit);
    }

    @Override
    public int getSpawnLimit(SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        int limit = spawnCategoryLimit.getOrDefault(spawnCategory, -1);
        if (limit < 0) {
            limit = server.getSpawnLimit(spawnCategory);
        }
        return limit;
    }

    @Override
    public void setSpawnLimit(SpawnCategory spawnCategory, int limit) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        spawnCategoryLimit.put(spawnCategory, limit);
    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        playSound(loc, instrument.getSound(), org.bukkit.SoundCategory.RECORDS, 3f, note.getPitch());
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        playSound(loc, sound, category, volume, pitch, getHandle().random.nextLong());;
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        playSound(loc, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        getHandle().playSeededSound(null, x, y, z, CraftSound.bukkitToMinecraft(sound), SoundCategory.valueOf(category.name()), volume, pitch, seed);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(Holder.direct(SoundEffect.createVariableRangeEvent(new MinecraftKey(sound))), SoundCategory.valueOf(category.name()), x, y, z, volume, pitch, seed);
        world.getServer().getPlayerList().broadcast(null, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.world.dimension(), packet);
    }

    @Override
    public void playSound(Entity entity, Sound sound, float volume, float pitch) {
        playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Entity entity, String sound, float volume, float pitch) {
        playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Entity entity, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        playSound(entity, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        playSound(entity, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(Entity entity, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof CraftEntity craftEntity) || entity.getWorld() != this || sound == null || category == null) return;

        PacketPlayOutEntitySound packet = new PacketPlayOutEntitySound(CraftSound.bukkitToMinecraftHolder(sound), net.minecraft.sounds.SoundCategory.valueOf(category.name()), craftEntity.getHandle(), volume, pitch, seed);
        PlayerChunkMap.EntityTracker entityTracker = getHandle().getChunkSource().chunkMap.entityMap.get(entity.getEntityId());
        if (entityTracker != null) {
            entityTracker.broadcastAndSend(packet);
        }
    }

    @Override
    public void playSound(Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof CraftEntity craftEntity) || entity.getWorld() != this || sound == null || category == null) return;

        PacketPlayOutEntitySound packet = new PacketPlayOutEntitySound(Holder.direct(SoundEffect.createVariableRangeEvent(new MinecraftKey(sound))), net.minecraft.sounds.SoundCategory.valueOf(category.name()), craftEntity.getHandle(), volume, pitch, seed);
        PlayerChunkMap.EntityTracker entityTracker = getHandle().getChunkSource().chunkMap.entityMap.get(entity.getEntityId());
        if (entityTracker != null) {
            entityTracker.broadcastAndSend(packet);
        }
    }

    private static Map<String, GameRules.GameRuleKey<?>> gamerules;
    public static synchronized Map<String, GameRules.GameRuleKey<?>> getGameRulesNMS() {
        if (gamerules != null) {
            return gamerules;
        }

        Map<String, GameRules.GameRuleKey<?>> gamerules = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleVisitor() {
            @Override
            public <T extends GameRules.GameRuleValue<T>> void visit(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
                gamerules.put(gamerules_gamerulekey.getId(), gamerules_gamerulekey);
            }
        });

        return CraftWorld.gamerules = gamerules;
    }

    private static Map<String, GameRules.GameRuleDefinition<?>> gameruleDefinitions;
    public static synchronized Map<String, GameRules.GameRuleDefinition<?>> getGameRuleDefinitions() {
        if (gameruleDefinitions != null) {
            return gameruleDefinitions;
        }

        Map<String, GameRules.GameRuleDefinition<?>> gameruleDefinitions = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleVisitor() {
            @Override
            public <T extends GameRules.GameRuleValue<T>> void visit(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
                gameruleDefinitions.put(gamerules_gamerulekey.getId(), gamerules_gameruledefinition);
            }
        });

        return CraftWorld.gameruleDefinitions = gameruleDefinitions;
    }

    @Override
    public String getGameRuleValue(String rule) {
        // In method contract for some reason
        if (rule == null) {
            return null;
        }

        GameRules.GameRuleValue<?> value = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule));
        return value != null ? value.toString() : "";
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!isGameRule(rule)) return false;

        GameRules.GameRuleValue<?> handle = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule));
        handle.deserialize(value);
        handle.onChanged(getHandle());
        return true;
    }

    @Override
    public String[] getGameRules() {
        return getGameRulesNMS().keySet().toArray(new String[getGameRulesNMS().size()]);
    }

    @Override
    public boolean isGameRule(String rule) {
        Preconditions.checkArgument(rule != null, "String rule cannot be null");
        Preconditions.checkArgument(!rule.isEmpty(), "String rule cannot be empty");
        return getGameRulesNMS().containsKey(rule);
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        return convert(rule, getHandle().getGameRules().getRule(getGameRulesNMS().get(rule.getName())));
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        return convert(rule, getGameRuleDefinitions().get(rule.getName()).createRule());
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        Preconditions.checkArgument(newValue != null, "GameRule value cannot be null");

        if (!isGameRule(rule.getName())) return false;

        GameRules.GameRuleValue<?> handle = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule.getName()));
        handle.deserialize(newValue.toString());
        handle.onChanged(getHandle());
        return true;
    }

    private <T> T convert(GameRule<T> rule, GameRules.GameRuleValue<?> value) {
        if (value == null) {
            return null;
        }

        if (value instanceof GameRules.GameRuleBoolean) {
            return rule.getType().cast(((GameRules.GameRuleBoolean) value).get());
        } else if (value instanceof GameRules.GameRuleInt) {
            return rule.getType().cast(value.getCommandResult());
        } else {
            throw new IllegalArgumentException("Invalid GameRule type (" + value + ") for GameRule " + rule.getName());
        }
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
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        particle = CraftParticle.convertLegacy(particle);
        data = CraftParticle.convertLegacy(data);
        if (data != null) {
            Preconditions.checkArgument(particle.getDataType().isInstance(data), "data (%s) should be %s", data.getClass(), particle.getDataType());
        }
        getHandle().sendParticles(
                null, // Sender
                CraftParticle.createParticleParam(particle, data), // Particle
                x, y, z, // Position
                count,  // Count
                offsetX, offsetY, offsetZ, // Random offset
                extra, // Speed?
                force
        );

    }

    @Deprecated
    @Override
    public Location locateNearestStructure(Location origin, org.bukkit.StructureType structureType, int radius, boolean findUnexplored) {
        StructureSearchResult result = null;

        // Manually map the mess of the old StructureType to the new StructureType and normal Structure
        if (org.bukkit.StructureType.MINESHAFT == structureType) {
            result = locateNearestStructure(origin, StructureType.MINESHAFT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.VILLAGE == structureType) {
            result = locateNearestStructure(origin, List.of(Structure.VILLAGE_DESERT, Structure.VILLAGE_PLAINS, Structure.VILLAGE_SAVANNA, Structure.VILLAGE_SNOWY, Structure.VILLAGE_TAIGA), radius, findUnexplored);
        } else if (org.bukkit.StructureType.NETHER_FORTRESS == structureType) {
            result = locateNearestStructure(origin, StructureType.FORTRESS, radius, findUnexplored);
        } else if (org.bukkit.StructureType.STRONGHOLD == structureType) {
            result = locateNearestStructure(origin, StructureType.STRONGHOLD, radius, findUnexplored);
        } else if (org.bukkit.StructureType.JUNGLE_PYRAMID == structureType) {
            result = locateNearestStructure(origin, StructureType.JUNGLE_TEMPLE, radius, findUnexplored);
        } else if (org.bukkit.StructureType.OCEAN_RUIN == structureType) {
            result = locateNearestStructure(origin, StructureType.OCEAN_RUIN, radius, findUnexplored);
        } else if (org.bukkit.StructureType.DESERT_PYRAMID == structureType) {
            result = locateNearestStructure(origin, StructureType.DESERT_PYRAMID, radius, findUnexplored);
        } else if (org.bukkit.StructureType.IGLOO == structureType) {
            result = locateNearestStructure(origin, StructureType.IGLOO, radius, findUnexplored);
        } else if (org.bukkit.StructureType.SWAMP_HUT == structureType) {
            result = locateNearestStructure(origin, StructureType.SWAMP_HUT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.OCEAN_MONUMENT == structureType) {
            result = locateNearestStructure(origin, StructureType.OCEAN_MONUMENT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.END_CITY == structureType) {
            result = locateNearestStructure(origin, StructureType.END_CITY, radius, findUnexplored);
        } else if (org.bukkit.StructureType.WOODLAND_MANSION == structureType) {
            result = locateNearestStructure(origin, StructureType.WOODLAND_MANSION, radius, findUnexplored);
        } else if (org.bukkit.StructureType.BURIED_TREASURE == structureType) {
            result = locateNearestStructure(origin, StructureType.BURIED_TREASURE, radius, findUnexplored);
        } else if (org.bukkit.StructureType.SHIPWRECK == structureType) {
            result = locateNearestStructure(origin, StructureType.SHIPWRECK, radius, findUnexplored);
        } else if (org.bukkit.StructureType.PILLAGER_OUTPOST == structureType) {
            result = locateNearestStructure(origin, Structure.PILLAGER_OUTPOST, radius, findUnexplored);
        } else if (org.bukkit.StructureType.NETHER_FOSSIL == structureType) {
            result = locateNearestStructure(origin, StructureType.NETHER_FOSSIL, radius, findUnexplored);
        } else if (org.bukkit.StructureType.RUINED_PORTAL == structureType) {
            result = locateNearestStructure(origin, StructureType.RUINED_PORTAL, radius, findUnexplored);
        } else if (org.bukkit.StructureType.BASTION_REMNANT == structureType) {
            result = locateNearestStructure(origin, Structure.BASTION_REMNANT, radius, findUnexplored);
        }

        return (result == null) ? null : result.getLocation();
    }

    @Override
    public StructureSearchResult locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored) {
        List<Structure> structures = new ArrayList<>();
        for (Structure structure : Registry.STRUCTURE) {
            if (structure.getStructureType() == structureType) {
                structures.add(structure);
            }
        }

        return locateNearestStructure(origin, structures, radius, findUnexplored);
    }

    @Override
    public StructureSearchResult locateNearestStructure(Location origin, Structure structure, int radius, boolean findUnexplored) {
        return locateNearestStructure(origin, List.of(structure), radius, findUnexplored);
    }

    public StructureSearchResult locateNearestStructure(Location origin, List<Structure> structures, int radius, boolean findUnexplored) {
        BlockPosition originPos = BlockPosition.containing(origin.getX(), origin.getY(), origin.getZ());
        List<Holder<net.minecraft.world.level.levelgen.structure.Structure>> holders = new ArrayList<>();

        for (Structure structure : structures) {
            holders.add(Holder.direct(CraftStructure.bukkitToMinecraft(structure)));
        }

        Pair<BlockPosition, Holder<net.minecraft.world.level.levelgen.structure.Structure>> found = getHandle().getChunkSource().getGenerator().findNearestMapStructure(getHandle(), HolderSet.direct(holders), originPos, radius, findUnexplored);
        if (found == null) {
            return null;
        }

        return new CraftStructureSearchResult(CraftStructure.minecraftToBukkit(found.getSecond().value()), CraftLocation.toBukkit(found.getFirst(), this));
    }

    @Override
    public BiomeSearchResult locateNearestBiome(Location origin, int radius, Biome... biomes) {
        return locateNearestBiome(origin, radius, 32, 64, biomes);
    }

    @Override
    public BiomeSearchResult locateNearestBiome(Location origin, int radius, int horizontalInterval, int verticalInterval, Biome... biomes) {
        BlockPosition originPos = BlockPosition.containing(origin.getX(), origin.getY(), origin.getZ());
        Set<Holder<BiomeBase>> holders = new HashSet<>();

        for (Biome biome : biomes) {
            holders.add(CraftBiome.bukkitToMinecraftHolder(biome));
        }

        Climate.Sampler sampler = getHandle().getChunkSource().randomState().sampler();
        // The given predicate is evaluated once at the start of the search, so performance isn't a large concern.
        Pair<BlockPosition, Holder<BiomeBase>> found = getHandle().getChunkSource().getGenerator().getBiomeSource().findClosestBiome3d(originPos, radius, horizontalInterval, verticalInterval, holders::contains, sampler, getHandle());
        if (found == null) {
            return null;
        }

        return new CraftBiomeSearchResult(CraftBiome.minecraftHolderToBukkit(found.getSecond()), new Location(this, found.getFirst().getX(), found.getFirst().getY(), found.getFirst().getZ()));
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(radius >= 0, "Radius value (%s) cannot be negative", radius);

        PersistentRaid persistentRaid = world.getRaids();
        net.minecraft.world.entity.raid.Raid raid = persistentRaid.getNearbyRaid(CraftLocation.toBlockPosition(location), radius * radius);
        return (raid == null) ? null : new CraftRaid(raid);
    }

    @Override
    public List<Raid> getRaids() {
        PersistentRaid persistentRaid = world.getRaids();
        return persistentRaid.raidMap.values().stream().map(CraftRaid::new).collect(Collectors.toList());
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        return (getHandle().getDragonFight() == null) ? null : new CraftDragonBattle(getHandle().getDragonFight());
    }

    @Override
    public Collection<GeneratedStructure> getStructures(int x, int z) {
        return getStructures(x, z, struct -> true);
    }

    @Override
    public Collection<GeneratedStructure> getStructures(int x, int z, Structure structure) {
        Preconditions.checkArgument(structure != null, "Structure cannot be null");

        IRegistry<net.minecraft.world.level.levelgen.structure.Structure> registry = CraftRegistry.getMinecraftRegistry(Registries.STRUCTURE);
        MinecraftKey key = registry.getKey(CraftStructure.bukkitToMinecraft(structure));

        return getStructures(x, z, struct -> registry.getKey(struct).equals(key));
    }

    private List<GeneratedStructure> getStructures(int x, int z, Predicate<net.minecraft.world.level.levelgen.structure.Structure> predicate) {
        List<GeneratedStructure> structures = new ArrayList<>();
        for (StructureStart start : getHandle().structureManager().startsForStructure(new ChunkCoordIntPair(x, z), predicate)) {
            structures.add(new CraftGeneratedStructure(start));
        }

        return structures;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @Override
    public Set<FeatureFlag> getFeatureFlags() {
        return CraftFeatureFlag.getFromNMS(this.getHandle().enabledFeatures()).stream().map(FeatureFlag.class::cast).collect(Collectors.toUnmodifiableSet());
    }

    public void storeBukkitValues(NBTTagCompound c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(NBTBase c) {
        if (c instanceof NBTTagCompound) {
            this.persistentDataContainer.putAll((NBTTagCompound) c);
        }
    }
}
