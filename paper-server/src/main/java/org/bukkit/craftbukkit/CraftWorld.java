package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.FeatureHooks;
import io.papermc.paper.raytracing.RayTraceTarget;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.raytracing.PositionedRayTraceConfigurationBuilder;
import io.papermc.paper.raytracing.PositionedRayTraceConfigurationBuilderImpl;
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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Raid;
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
import org.bukkit.util.NumberConversions;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StructureSearchResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftWorld extends CraftRegionAccessor implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    private final ServerLevel world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final CraftServer server = (CraftServer) Bukkit.getServer();
    private final @Nullable ChunkGenerator generator;
    private final @Nullable BiomeProvider biomeProvider;
    private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private final Object2IntOpenHashMap<SpawnCategory> spawnCategoryLimit = new Object2IntOpenHashMap<>();
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(CraftWorld.DATA_TYPE_REGISTRY);
    private net.kyori.adventure.pointer.Pointers adventure$pointers; // Paper - implement pointers
    // Paper start - void damage configuration
    private boolean voidDamageEnabled;
    private float voidDamageAmount;
    private double voidDamageMinBuildHeightOffset;

    @Override
    public boolean isVoidDamageEnabled() {
        return this.voidDamageEnabled;
    }

    @Override
    public void setVoidDamageEnabled(final boolean enabled) {
        this.voidDamageEnabled = enabled;
    }

    @Override
    public float getVoidDamageAmount() {
        return this.voidDamageAmount;
    }

    @Override
    public void setVoidDamageAmount(float voidDamageAmount) {
        this.voidDamageAmount = voidDamageAmount;
    }

    @Override
    public double getVoidDamageMinBuildHeightOffset() {
        return this.voidDamageMinBuildHeightOffset;
    }

    @Override
    public void setVoidDamageMinBuildHeightOffset(double minBuildHeightOffset) {
        this.voidDamageMinBuildHeightOffset = minBuildHeightOffset;
    }
    // Paper end - void damage configuration

    // Paper start - Provide fast information methods
    @Override
    public int getEntityCount() {
        int ret = 0;
        for (net.minecraft.world.entity.Entity entity : this.world.getEntities().getAll()) {
            if (entity.getBukkitEntity().isValid()) {
                ++ret;
            }
        }
        return ret;
    }

    @Override
    public int getTileEntityCount() {
        // We don't use the full world block entity list, so we must iterate chunks
        int size = 0;
        for (ChunkHolder playerchunk : ca.spottedleaf.moonrise.common.PlatformHooks.get().getVisibleChunkHolders(this.world)) {
            net.minecraft.world.level.chunk.LevelChunk chunk = playerchunk.getTickingChunk();
            if (chunk == null) {
                continue;
            }
            size += chunk.blockEntities.size();
        }
        return size;
    }

    @Override
    public int getTickableTileEntityCount() {
        return world.blockEntityTickers.size();
    }

    @Override
    public int getChunkCount() {
        return this.world.getChunkSource().getFullChunksCount();
    }

    @Override
    public int getPlayerCount() {
        return world.players().size();
    }

    @Override
    public BiomeProvider vanillaBiomeProvider() {
        net.minecraft.server.level.ServerChunkCache serverCache = this.getHandle().chunkSource;

        final net.minecraft.world.level.chunk.ChunkGenerator gen = serverCache.getGenerator();
        net.minecraft.world.level.biome.BiomeSource biomeSource;
        if (gen instanceof org.bukkit.craftbukkit.generator.CustomChunkGenerator custom) {
            biomeSource = custom.getDelegate().getBiomeSource();
        } else {
            biomeSource = gen.getBiomeSource();
        }
        if (biomeSource instanceof org.bukkit.craftbukkit.generator.CustomWorldChunkManager customBiomeSource) {
            biomeSource = customBiomeSource.vanillaBiomeSource;
        }
        final net.minecraft.world.level.biome.BiomeSource finalBiomeSource = biomeSource;
        final net.minecraft.world.level.biome.Climate.Sampler sampler = serverCache.randomState().sampler();

        final List<Biome> possibleBiomes = finalBiomeSource.possibleBiomes().stream()
            .map(CraftBiome::minecraftHolderToBukkit)
            .toList();
        return new BiomeProvider() {
            @Override
            public Biome getBiome(final org.bukkit.generator.WorldInfo worldInfo, final int x, final int y, final int z) {
                return CraftBiome.minecraftHolderToBukkit(finalBiomeSource.getNoiseBiome(x >> 2, y >> 2, z >> 2, sampler));
            }

            @Override
            public List<Biome> getBiomes(final org.bukkit.generator.WorldInfo worldInfo) {
                return possibleBiomes;
            }
        };
    }
    // Paper end
    // Paper start - structure check API
    @Override
    public boolean hasStructureAt(final io.papermc.paper.math.Position position, final Structure structure) {
        return this.world.structureManager().getStructureWithPieceAt(
            io.papermc.paper.util.MCUtil.toBlockPos(position),
            CraftStructure.bukkitToMinecraft(structure)
        ).isValid();
    }
    // Paper end

    private static final Random rand = new Random();

    public CraftWorld(ServerLevel world, @Nullable ChunkGenerator generator, @Nullable BiomeProvider biomeProvider, Environment environment) {
        this.world = world;
        this.generator = generator;
        this.biomeProvider = biomeProvider;

        this.environment = environment;
        // Paper start - per world spawn limits
        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            if (CraftSpawnCategory.isValidForLimits(spawnCategory)) {
                setSpawnLimit(spawnCategory, this.world.paperConfig().entities.spawning.spawnLimits.getInt(CraftSpawnCategory.toNMS(spawnCategory)));
            }
        }
        // Paper end - per world spawn limits

        // Paper start - per world void damage height
        this.voidDamageEnabled = this.world.paperConfig().environment.voidDamageAmount.enabled();
        this.voidDamageMinBuildHeightOffset = this.world.paperConfig().environment.voidDamageMinBuildHeightOffset;
        this.voidDamageAmount = (float) this.world.paperConfig().environment.voidDamageAmount.or(0);
        // Paper end - per world void damage height
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return CraftBlock.at(this.world, new BlockPos(x, y, z));
    }

    @Override
    public Location getSpawnLocation() {
        BlockPos spawn = this.world.getSharedSpawnPos();
        float yaw = this.world.getSharedSpawnAngle();
        return CraftLocation.toBukkit(spawn, this, yaw, 0);
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return this.equals(location.getWorld()) ? this.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw()) : false;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {
        try {
            // Location previousLocation = this.getSpawnLocation(); // Paper - Call SpawnChangeEvent; moved to nms.ServerLevel
            this.world.setDefaultSpawnPos(new BlockPos(x, y, z), angle); // Paper - use ServerLevel#setDefaultSpawnPos

            // Paper start - Call SpawnChangeEvent; move to nms.ServerLevel
            // Notify anyone who's listening.
            // SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            // server.getPluginManager().callEvent(event);
            // Paper end - Call SpawnChangeEvent

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return this.setSpawnLocation(x, y, z, 0.0F);
    }
    // Paper start
    private static void warnUnsafeChunk(String reason, int x, int z) {
        // if any chunk coord is outside of 30 million blocks
        int max = (30_000_000 / 16) + 625;
        if (x > max || z > max || x < -max || z < -max) {
            Plugin plugin = io.papermc.paper.util.StackWalkerUtil.getFirstPluginCaller();
            if (plugin != null) {
                plugin.getLogger().warning("Plugin is %s at (%s, %s), this might cause issues.".formatted(reason, x, z));
            }
            if (net.minecraft.server.MinecraftServer.getServer().isDebugging()) {
                io.papermc.paper.util.TraceUtil.dumpTraceForThread("Dangerous chunk retrieval");
            }
        }
    }
    // Paper end

    @Override
    public Chunk getChunkAt(int x, int z) {
        warnUnsafeChunk("getting a faraway chunk", x, z); // Paper
        net.minecraft.world.level.chunk.LevelChunk chunk = (net.minecraft.world.level.chunk.LevelChunk) this.world.getChunk(x, z, ChunkStatus.FULL, true);
        return new CraftChunk(chunk);
    }

    @NotNull
    @Override
    public Chunk getChunkAt(int x, int z, boolean generate) {
        if (generate) {
            return this.getChunkAt(x, z);
        }

        return new CraftChunk(this.getHandle(), x, z);
    }

    @Override
    public Chunk getChunkAt(Block block) {
        Preconditions.checkArgument(block != null, "null block");

        return this.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return this.world.getChunkSource().isChunkLoaded(x, z);
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        // Paper start - Fix this method
        if (!Bukkit.isPrimaryThread()) {
            return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                return CraftWorld.this.isChunkGenerated(x, z);
            }, world.getChunkSource().mainThreadProcessor).join();
        }
        ChunkAccess chunk = world.getChunkSource().getChunkAtImmediately(x, z);
        if (chunk != null) {
            return chunk instanceof ImposterProtoChunk || chunk instanceof net.minecraft.world.level.chunk.LevelChunk;
        }
        final java.util.concurrent.CompletableFuture<ChunkAccess> future = new java.util.concurrent.CompletableFuture<>();
        ca.spottedleaf.moonrise.common.PlatformHooks.get().scheduleChunkLoad(
            this.world, x, z, false, ChunkStatus.EMPTY, true, ca.spottedleaf.concurrentutil.util.Priority.NORMAL, future::complete
        );
        world.getChunkSource().mainThreadProcessor.managedBlock(future::isDone);
        return future.thenApply(c -> {
            if (c != null) {
                return c.getPersistedStatus() == ChunkStatus.FULL;
            }
            return false;
        }).join();
        // Paper end - Fix this method
    }

    @Override
    public Chunk[] getLoadedChunks() {
        List<ChunkHolder> chunks = ca.spottedleaf.moonrise.common.PlatformHooks.get().getVisibleChunkHolders(this.world); // Paper
        return chunks.stream().map(ChunkHolder::getFullChunkNow).filter(Objects::nonNull).map(CraftChunk::new).toArray(Chunk[]::new);
    }

    @Override
    public void loadChunk(int x, int z) {
        this.loadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return this.unloadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return this.unloadChunk0(x, z, save);
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        org.spigotmc.AsyncCatcher.catchOp("chunk unload"); // Spigot
        if (this.isChunkLoaded(x, z)) {
            this.world.getChunkSource().removeTicketWithRadius(TicketType.PLUGIN, new ChunkPos(x, z), 1);
        }

        return true;
    }

    private boolean unloadChunk0(int x, int z, boolean save) {
        org.spigotmc.AsyncCatcher.catchOp("chunk unload"); // Spigot
        if (!this.isChunkLoaded(x, z)) {
            return true;
        }
        net.minecraft.world.level.chunk.LevelChunk chunk = this.world.getChunk(x, z);

        if (!save) {
            chunk.tryMarkSaved(); // Use method call to account for persistentDataContainer
        }
        this.unloadChunkRequest(x, z);

        this.world.getChunkSource().purgeUnload();
        return !this.isChunkLoaded(x, z);
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        ChunkHolder playerChunk = this.world.getChunkSource().chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(x, z));
        if (playerChunk == null) return false;

        // Paper start - chunk system
        net.minecraft.world.level.chunk.LevelChunk chunk = playerChunk.getChunkToSend();
        if (chunk == null) {
            return false;
        }
        // Paper end - chunk system
                List<ServerPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);
                if (playersInRange.isEmpty()) return true; // Paper - chunk system

                FeatureHooks.sendChunkRefreshPackets(playersInRange, chunk);
        // Paper - chunk system

        return true;
    }

    @Override
    public Collection<Player> getPlayersSeeingChunk(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "chunk cannot be null");

        return this.getPlayersSeeingChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public Collection<Player> getPlayersSeeingChunk(int x, int z) {
        if (!this.isChunkLoaded(x, z)) {
            return Collections.emptySet();
        }

        List<ServerPlayer> players = this.world.getChunkSource().chunkMap.getPlayers(new ChunkPos(x, z), false);

        if (players.isEmpty()) {
            return Collections.emptySet();
        }

        return players.stream()
                .filter(Objects::nonNull)
                .map(ServerPlayer::getBukkitEntity)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return this.isChunkLoaded(x, z);
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        org.spigotmc.AsyncCatcher.catchOp("chunk load"); // Spigot
        warnUnsafeChunk("loading a faraway chunk", x, z); // Paper
        ChunkAccess chunk = this.world.getChunkSource().getChunk(x, z, generate || isChunkGenerated(x, z) ? ChunkStatus.FULL : ChunkStatus.EMPTY, true); // Paper

        // If generate = false, but the chunk already exists, we will get this back.
        if (chunk instanceof ImposterProtoChunk) {
            // We then cycle through again to get the full chunk immediately, rather than after the ticket addition
            chunk = this.world.getChunkSource().getChunk(x, z, ChunkStatus.FULL, true);
        }

        if (chunk instanceof LevelChunk) {
            this.world.getChunkSource().addTicketWithRadius(TicketType.PLUGIN, new ChunkPos(x, z), 1);
            return true;
        }

        return false;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        return this.isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    @Override
    public void loadChunk(Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        this.loadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        warnUnsafeChunk("adding a faraway chunk ticket", x, z); // Paper
        Preconditions.checkArgument(plugin != null, "null plugin");
        Preconditions.checkArgument(plugin.isEnabled(), "plugin is not enabled");

        final DistanceManager distanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        if (distanceManager.ticketStorage.addPluginRegionTicket(new ChunkPos(x, z), plugin)) {
            this.getChunkAt(x, z); // ensure it's loaded
            return true;
        }

        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        final DistanceManager distanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        return distanceManager.ticketStorage.removePluginRegionTicket(new ChunkPos(x, z), plugin);
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        DistanceManager chunkDistanceManager = this.world.getChunkSource().chunkMap.distanceManager;
        chunkDistanceManager.ticketStorage.removeAllPluginRegionTickets(TicketType.PLUGIN_TICKET, ChunkMap.FORCED_TICKET_LEVEL, plugin);
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        return FeatureHooks.getPluginChunkTickets(this.world, x, z); // Paper - chunk system
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return FeatureHooks.getPluginChunkTickets(this.world); // Paper - chunk system
    }

    @NotNull
    @Override
    public Collection<Chunk> getIntersectingChunks(@NotNull BoundingBox boundingBox) {
        List<Chunk> chunks = new ArrayList<>();

        int minX = NumberConversions.floor(boundingBox.getMinX()) >> 4;
        int maxX = NumberConversions.floor(boundingBox.getMaxX()) >> 4;
        int minZ = NumberConversions.floor(boundingBox.getMinZ()) >> 4;
        int maxZ = NumberConversions.floor(boundingBox.getMaxZ()) >> 4;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(this.getChunkAt(x, z, false));
            }
        }

        return chunks;
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return this.getHandle().getForceLoadedChunks().contains(ChunkPos.asLong(x, z));
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        warnUnsafeChunk("forceloading a faraway chunk", x, z); // Paper
        this.getHandle().setChunkForced(x, z, forced);
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        Set<Chunk> chunks = new HashSet<>();

        for (long coord : this.getHandle().getForceLoadedChunks()) {
            chunks.add(this.getChunkAt(ChunkPos.getX(coord), ChunkPos.getZ(coord)));
        }

        return Collections.unmodifiableCollection(chunks);
    }

    public ServerLevel getHandle() {
        return this.world;
    }

    @Override
    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item) {
        return this.dropItem(loc, item, null);
    }

    @Override
    public org.bukkit.entity.Item dropItem(Location loc, ItemStack item, Consumer<? super org.bukkit.entity.Item> function) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        ItemEntity entity = new ItemEntity(this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
        org.bukkit.entity.Item itemEntity = (org.bukkit.entity.Item) entity.getBukkitEntity();
        entity.pickupDelay = 10;
        if (function != null) {
            function.accept(itemEntity);
        }
        this.world.addFreshEntity(entity, SpawnReason.CUSTOM);
        return itemEntity;
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item) {
        return this.dropItemNaturally(loc, item, null);
    }

    @Override
    public org.bukkit.entity.Item dropItemNaturally(Location loc, ItemStack item, Consumer<? super org.bukkit.entity.Item> function) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        double xs = Mth.nextDouble(this.world.random, -0.25D, 0.25D);
        double ys = Mth.nextDouble(this.world.random, -0.25D, 0.25D) - ((double) EntityType.ITEM.getHeight() / 2.0D);
        double zs = Mth.nextDouble(this.world.random, -0.25D, 0.25D);
        loc = loc.clone().add(xs, ys, zs);
        return this.dropItem(loc, item, function);
    }

    @Override
    public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread) {
        return this.spawnArrow(loc, velocity, speed, spread, Arrow.class);
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location loc, Vector velocity, float speed, float spread, Class<T> clazz) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(velocity != null, "Vector cannot be null");
        Preconditions.checkArgument(clazz != null, "clazz Entity for the arrow cannot be null");

        net.minecraft.world.entity.projectile.AbstractArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = EntityType.ARROW.create(this.world, EntitySpawnReason.COMMAND);
            ((Arrow) arrow.getBukkitEntity()).setBasePotionType(PotionType.WATER);
        } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = EntityType.SPECTRAL_ARROW.create(this.world, EntitySpawnReason.COMMAND);
        } else if (Trident.class.isAssignableFrom(clazz)) {
            arrow = EntityType.TRIDENT.create(this.world, EntitySpawnReason.COMMAND);
        } else {
            arrow = EntityType.ARROW.create(this.world, EntitySpawnReason.COMMAND);
        }

        arrow.snapTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
        this.world.addFreshEntity(arrow);
        return (T) arrow.getBukkitEntity();
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        return this.strikeLightning0(loc, false);
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        return this.strikeLightning0(loc, true);
    }

    private LightningStrike strikeLightning0(Location loc, boolean isVisual) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.world, EntitySpawnReason.COMMAND);
        lightning.snapTo(loc.getX(), loc.getY(), loc.getZ());
        lightning.isEffect = isVisual; // Paper - Properly handle lightning effects api
        this.world.strikeLightning(lightning, LightningStrikeEvent.Cause.CUSTOM);
        return (LightningStrike) lightning.getBukkitEntity();
    }

    // Paper start - Add methods to find targets for lightning strikes
    @Override
    public Location findLightningRod(Location location) {
        return this.world.findLightningRod(CraftLocation.toBlockPosition(location))
            .map(blockPos -> CraftLocation.toBukkit(blockPos, this.world)
                // get the actual rod pos
                .subtract(0, 1, 0))
            .orElse(null);
    }

    @Override
    public Location findLightningTarget(Location location) {
        final BlockPos pos = this.world.findLightningTargetAround(CraftLocation.toBlockPosition(location), true);
        return pos == null ? null : CraftLocation.toBukkit(pos, this.world);
    }
    // Paper end - Add methods to find targets for lightning strikes

    @Override
    public boolean generateTree(Location loc, TreeType type) {
        return this.generateTree(loc, CraftWorld.rand, type);
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        this.world.captureTreeGeneration = true;
        this.world.captureBlockStates = true;
        boolean grownTree = this.generateTree(loc, type);
        this.world.captureBlockStates = false;
        this.world.captureTreeGeneration = false;
        if (grownTree) { // Copy block data to delegate
            for (BlockState blockstate : this.world.capturedBlockStates.values()) {
                BlockPos position = ((CraftBlockState) blockstate).getPosition();
                net.minecraft.world.level.block.state.BlockState oldBlock = this.world.getBlockState(position);
                int flags = ((CraftBlockState) blockstate).getFlags();
                delegate.setBlockData(blockstate.getX(), blockstate.getY(), blockstate.getZ(), blockstate.getBlockData());
                net.minecraft.world.level.block.state.BlockState newBlock = this.world.getBlockState(position);
                this.world.notifyAndUpdatePhysics(position, null, oldBlock, newBlock, newBlock, flags, net.minecraft.world.level.block.Block.UPDATE_LIMIT);
            }
            this.world.capturedBlockStates.clear();
            return true;
        } else {
            this.world.capturedBlockStates.clear();
            return false;
        }
    }

    @Override
    public String getName() {
        return this.world.serverLevelData.getLevelName();
    }

    @Override
    public UUID getUID() {
        return this.world.uuid;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(this.world.dimension().location());
    }

    @Override
    public String toString() {
        return "CraftWorld{name=" + this.getName() + '}';
    }

    @Override
    public long getTime() {
        long time = this.getFullTime() % Level.TICKS_PER_DAY;
        if (time < 0) time += Level.TICKS_PER_DAY;
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - this.getFullTime()) % Level.TICKS_PER_DAY;
        if (margin < 0) margin += Level.TICKS_PER_DAY;
        this.setFullTime(this.getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return this.world.getDayTime();
    }

    @Override
    public void setFullTime(long time) {
        // Notify anyone who's listening
        TimeSkipEvent event = new TimeSkipEvent(this, TimeSkipEvent.SkipReason.CUSTOM, time - this.world.getDayTime());
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        this.world.setDayTime(this.world.getDayTime() + event.getSkipAmount());

        // Forces the client to update to the new time immediately
        for (Player p : this.getPlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().connection == null) continue;

            cp.getHandle().connection.send(new ClientboundSetTimePacket(cp.getHandle().level().getGameTime(), cp.getHandle().getPlayerTime(), cp.getHandle().relativeTime && cp.getHandle().level().getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        }
    }

    // Paper start
    @Override
    public boolean isDayTime() {
        return getHandle().isBrightOutside();
    }
    // Paper end

    @Override
    public long getGameTime() {
        return this.world.levelData.getGameTime();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return this.createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source) {
    // Paper start - expand explosion API
        return this.createExplosion(x, y, z, power, setFire, breakBlocks, source, null);
    }
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source, Consumer<net.minecraft.world.level.ServerExplosion> configurator) {
    // Paper end - expand explosion API
        net.minecraft.world.level.Level.ExplosionInteraction explosionType;
        if (!breakBlocks) {
            explosionType = net.minecraft.world.level.Level.ExplosionInteraction.NONE; // Don't break blocks
        } else if (source == null) {
            explosionType = net.minecraft.world.level.Level.ExplosionInteraction.STANDARD; // Break blocks, don't decay drops
        } else {
            explosionType = net.minecraft.world.level.Level.ExplosionInteraction.MOB; // Respect mobGriefing gamerule
        }

        net.minecraft.world.entity.Entity entity = (source == null) ? null : ((CraftEntity) source).getHandle();
        return !this.world.explode0(entity, Explosion.getDefaultDamageSource(this.world, entity), null, x, y, z, power, setFire, explosionType, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE, configurator).wasCanceled; // Paper - expand explosion API
    }
    // Paper start
    @Override
    public boolean createExplosion(Entity source, Location loc, float power, boolean setFire, boolean breakBlocks, boolean excludeSourceFromDamage) {
        return this.createExplosion(loc.x(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source, e -> e.excludeSourceFromDamage = excludeSourceFromDamage);
    }
    // Paper end

    @Override
    public boolean createExplosion(Location loc, float power) {
        return this.createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return this.createExplosion(loc, power, setFire, true);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        return this.createExplosion(loc, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        Preconditions.checkArgument(loc != null, "Location is null");
        Preconditions.checkArgument(this.equals(loc.getWorld()), "Location not in world");

        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source);
    }

    @Override
    public @NotNull Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public Block getBlockAt(Location location) {
        return this.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return this.getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    @Override
    public @Nullable ChunkGenerator getGenerator() {
        return this.generator;
    }

    @Override
    public @Nullable BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return this.populators;
    }

    @NotNull
    @Override
    public <T extends LivingEntity> T spawn(@NotNull Location location, @NotNull Class<T> clazz, @NotNull SpawnReason spawnReason, boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException {
        Preconditions.checkArgument(spawnReason != null, "Spawn reason cannot be null");
        return this.spawn(location, clazz, function, spawnReason, randomizeData);
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, org.bukkit.HeightMap heightMap) {
        warnUnsafeChunk("getting a faraway chunk", x >> 4, z >> 4); // Paper
        // Transient load for this tick
        return this.world.getChunk(x >> 4, z >> 4).getHeight(CraftHeightMap.toNMS(heightMap), x, z);
    }

    @Override
    public Block getHighestBlockAt(int x, int z, org.bukkit.HeightMap heightMap) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z, heightMap), z);
    }

    @Override
    public Block getHighestBlockAt(Location location, org.bukkit.HeightMap heightMap) {
        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public Biome getBiome(int x, int z) {
        return this.getBiome(x, 0, z);
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        for (int y = this.getMinHeight(); y < this.getMaxHeight(); y++) {
            this.setBiome(x, y, z, bio);
        }
    }

    @Override
    public void setBiome(int x, int y, int z, Holder<net.minecraft.world.level.biome.Biome> bb) {
        BlockPos pos = new BlockPos(x, 0, z);
        if (this.world.hasChunkAt(pos)) {
            net.minecraft.world.level.chunk.LevelChunk chunk = this.world.getChunkAt(pos);

            if (chunk != null) {
                chunk.setBiome(x >> 2, y >> 2, z >> 2, bb);

                chunk.markUnsaved(); // SPIGOT-2890
            }
        }
    }

    @Override
    public double getTemperature(int x, int z) {
        return this.getTemperature(x, 0, z);
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).value().getTemperature(pos, this.world.getSeaLevel());
    }

    @Override
    public double getHumidity(int x, int z) {
        return this.getHumidity(x, 0, z);
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).value().climateSettings.downfall();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>) this.getEntitiesByClasses(classes);
    }

    @Override
    public Iterable<net.minecraft.world.entity.Entity> getNMSEntities() {
        return this.getHandle().getEntities().getAll();
    }

    @Override
    public void addEntityToWorld(net.minecraft.world.entity.Entity entity, SpawnReason reason) {
        this.getHandle().addFreshEntity(entity, reason);
    }

    @Override
    public void addEntityWithPassengers(net.minecraft.world.entity.Entity entity, SpawnReason reason) {
        this.getHandle().tryAddFreshEntityWithPassengers(entity, reason);
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
        org.spigotmc.AsyncCatcher.catchOp("getNearbyEntities"); // Spigot
        Preconditions.checkArgument(boundingBox != null, "BoundingBox cannot be null");

        AABB bb = new AABB(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());
        List<net.minecraft.world.entity.Entity> entityList = this.getHandle().getEntities((net.minecraft.world.entity.Entity) null, bb, Predicates.alwaysTrue());
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
        // Paper start - Add predicate for blocks when raytracing
        return rayTraceEntities((io.papermc.paper.math.Position) start, direction, maxDistance, raySize, filter);
    }

    public RayTraceResult rayTraceEntities(io.papermc.paper.math.Position start, Vector direction, double maxDistance, double raySize, Predicate<? super Entity> filter) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(!(start instanceof Location location) || this.equals(location.getWorld()), "Location start cannot be in a different world");
        Preconditions.checkArgument(start.isFinite(), "Location start is not finite");
        // Paper end - Add predicate for blocks when raytracing

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
        // Paper start - Add predicate for blocks when raytracing
        return this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(io.papermc.paper.math.Position start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, Predicate<? super Block> canCollide) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(!(start instanceof Location location) || this.equals(location.getWorld()), "Location start cannot be in a different world");
        Preconditions.checkArgument(start.isFinite(), "Location start is not finite");
        // Paper end - Add predicate for blocks when raytracing

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();

        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) need to be greater than 0", direction.lengthSquared());
        Preconditions.checkArgument(fluidCollisionMode != null, "FluidCollisionMode cannot be null");

        if (maxDistance < 0.0D) {
            return null;
        }

        Vector dir = direction.clone().normalize().multiply(maxDistance);
        Vec3 startPos = io.papermc.paper.util.MCUtil.toVec3(start); // Paper - Add predicate for blocks when raytracing
        Vec3 endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());
        HitResult hitResult = this.getHandle().clip(new ClipContext(startPos, endPos, ignorePassableBlocks ? ClipContext.Block.COLLIDER : ClipContext.Block.OUTLINE, CraftFluidCollisionMode.toFluid(fluidCollisionMode), CollisionContext.empty()), canCollide); // Paper - Add predicate for blocks when raytracing

        return CraftRayTraceResult.convertFromInternal(this.getHandle(), hitResult);
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<? super Entity> filter) {
        // Paper start - Add predicate for blocks when raytracing
        return this.rayTrace(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, raySize, filter, null);
    }

    @Override
    public RayTraceResult rayTrace(io.papermc.paper.math.Position start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<? super Entity> filter, Predicate<? super Block> canCollide) {
        RayTraceResult blockHit = this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, canCollide);
        // Paper end - Add predicate for blocks when raytracing
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
    public RayTraceResult rayTrace(Consumer<PositionedRayTraceConfigurationBuilder> builderConsumer) {
        PositionedRayTraceConfigurationBuilderImpl builder = new PositionedRayTraceConfigurationBuilderImpl();

        builderConsumer.accept(builder);
        Preconditions.checkArgument(builder.start != null, "Start location cannot be null");
        Preconditions.checkArgument(builder.direction != null, "Direction vector cannot be null");
        Preconditions.checkArgument(builder.maxDistance.isPresent(), "Max distance must be set");
        Preconditions.checkArgument(!builder.targets.isEmpty(), "At least one target");

        final double maxDistance = builder.maxDistance.getAsDouble();
        if (builder.targets.contains(RayTraceTarget.ENTITY)) {
            if (builder.targets.contains(RayTraceTarget.BLOCK)) {
                return this.rayTrace(builder.start, builder.direction, maxDistance, builder.fluidCollisionMode, builder.ignorePassableBlocks, builder.raySize, builder.entityFilter, builder.blockFilter);
            }
            return this.rayTraceEntities(builder.start, builder.direction, maxDistance, builder.raySize, builder.entityFilter);
        }
        return this.rayTraceBlocks(builder.start, builder.direction, maxDistance, builder.fluidCollisionMode, builder.ignorePassableBlocks, builder.blockFilter);
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(this.world.players().size());

        for (net.minecraft.world.entity.player.Player human : this.world.players()) {
            HumanEntity bukkitEntity = human.getBukkitEntity();

            if ((bukkitEntity != null) && (bukkitEntity instanceof Player)) {
                list.add((Player) bukkitEntity);
            }
        }

        return list;
    }

    // Paper start - getEntity by UUID API
    @Override
    public Entity getEntity(UUID uuid) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        net.minecraft.world.entity.Entity entity = world.getEntity(uuid);
        return entity == null ? null : entity.getBukkitEntity();
    }
    // Paper end

    @Override
    public void save(boolean flush) {
        org.spigotmc.AsyncCatcher.catchOp("world save"); // Spigot
        this.server.checkSaveState();
        boolean oldSave = this.world.noSave;

        this.world.noSave = false;
        this.world.save(null, flush, false);

        this.world.noSave = oldSave;
    }

    @Override
    public boolean isAutoSave() {
        return !this.world.noSave;
    }

    @Override
    public void setAutoSave(boolean value) {
        this.world.noSave = !value;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.getHandle().getServer().setDifficulty(this.getHandle(), net.minecraft.world.Difficulty.byId(difficulty.getValue()), true); // Paper - per level difficulty; don't skip other difficulty-changing logic
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    @Override
    public int getViewDistance() {
        return FeatureHooks.getViewDistance(this.world); // Paper - chunk system
    }

    @Override
    public int getSimulationDistance() {
        return FeatureHooks.getSimulationDistance(this.world); // Paper - chunk system
    }

    public BlockMetadataStore getBlockMetadata() {
        return this.blockMetadata;
    }

    @Override
    public boolean hasStorm() {
        return this.world.levelData.isRaining();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        this.world.serverLevelData.setRaining(hasStorm, org.bukkit.event.weather.WeatherChangeEvent.Cause.PLUGIN); // Paper - Add cause to Weather/ThunderChangeEvents
        this.setWeatherDuration(0); // Reset weather duration (legacy behaviour)
        this.setClearWeatherDuration(0); // Reset clear weather duration (reset "/weather clear" commands)
    }

    @Override
    public int getWeatherDuration() {
        return this.world.serverLevelData.getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        this.world.serverLevelData.setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return this.world.levelData.isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        this.world.serverLevelData.setThundering(thundering, org.bukkit.event.weather.ThunderChangeEvent.Cause.PLUGIN); // Paper - Add cause to Weather/ThunderChangeEvents
        this.setThunderDuration(0); // Reset weather duration (legacy behaviour)
        this.setClearWeatherDuration(0); // Reset clear weather duration (reset "/weather clear" commands)
    }

    @Override
    public int getThunderDuration() {
        return this.world.serverLevelData.getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        this.world.serverLevelData.setThunderTime(duration);
    }

    @Override
    public boolean isClearWeather() {
        return !this.hasStorm() && !this.isThundering();
    }

    @Override
    public void setClearWeatherDuration(int duration) {
        this.world.serverLevelData.setClearWeatherTime(duration);
    }

    @Override
    public int getClearWeatherDuration() {
        return this.world.serverLevelData.getClearWeatherTime();
    }

    @Override
    public long getSeed() {
        return this.world.getSeed();
    }

    @Override
    public boolean getPVP() {
        return this.world.pvpMode;
    }

    @Override
    public void setPVP(boolean pvp) {
        this.world.pvpMode = pvp;
    }

    public void playEffect(Player player, Effect effect, int data) {
        this.playEffect(player.getLocation(), effect, data, 0);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        this.playEffect(location, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        this.playEffect(loc, effect, data, 64);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data, int radius) {
        if (data != null) {
            Preconditions.checkArgument(effect.getData() != null, "Effect.%s does not have a valid Data", effect);
            Preconditions.checkArgument(effect.isApplicable(data), "%s data cannot be used for the %s effect", data.getClass().getName(), effect); // Paper
        } else {
            // Special case: the axis is optional for ELECTRIC_SPARK
            Preconditions.checkArgument(effect.getData() == null || effect == Effect.ELECTRIC_SPARK, "Wrong kind of data for the %s effect", effect);
        }

        int datavalue = CraftEffect.getDataValue(effect, data);
        this.playEffect(loc, effect, datavalue, radius);
    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "World of Location cannot be null");
        int packetData = effect.getId();
        ClientboundLevelEventPacket packet = new ClientboundLevelEventPacket(packetData, CraftLocation.toBlockPosition(location), data, false);
        int distance;
        radius *= radius;

        for (Player player : this.getPlayers()) {
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
        return this.spawnFallingBlock(location, data.getItemType(), data.getData());
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isBlock(), "Material.%s must be a block", material);

        // Paper start - restore API behavior for spawning falling blocks
        FallingBlockEntity entity = new FallingBlockEntity(this.world, location.getX(), location.getY(), location.getZ(), CraftBlockType.bukkitToMinecraft(material).defaultBlockState()); // Paper
        entity.time = 1;

        this.world.addFreshEntity(entity, SpawnReason.CUSTOM);
        // Paper end - restore API behavior for spawning falling blocks
        return (FallingBlock) entity.getBukkitEntity();
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(data != null, "BlockData cannot be null");

        // Paper start - restore API behavior for spawning falling blocks
        FallingBlockEntity entity = new FallingBlockEntity(this.world, location.getX(), location.getY(), location.getZ(), ((CraftBlockData) data).getState());
        entity.time = 1;

        this.world.addFreshEntity(entity, SpawnReason.CUSTOM);
        // Paper end - restore API behavior for spawning falling blocks
        return (FallingBlock) entity.getBukkitEntity();
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
        return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        this.world.getChunkSource().setSpawnSettings(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return this.world.getChunkSource().spawnFriendlies;
    }

    @Override
    public boolean getAllowMonsters() {
        return this.world.getChunkSource().spawnEnemies;
    }

    @Override
    public int getMinHeight() {
        return this.world.getMinY();
    }

    @Override
    public int getMaxHeight() {
        return this.world.getMaxY() + 1;
    }

    public int getMaxY() {
        return this.world.getMaxY();
    }

    @Override
    public int getLogicalHeight() {
        return this.world.dimensionType().logicalHeight();
    }

    @Override
    public boolean isNatural() {
        return this.world.dimensionType().natural();
    }

    @Override
    public boolean isBedWorks() {
        return this.world.dimensionType().bedWorks();
    }

    @Override
    public boolean hasSkyLight() {
        return this.world.dimensionType().hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return this.world.dimensionType().hasCeiling();
    }

    @Override
    public boolean isPiglinSafe() {
        return this.world.dimensionType().piglinSafe();
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        return this.world.dimensionType().respawnAnchorWorks();
    }

    @Override
    public boolean hasRaids() {
        return this.world.dimensionType().hasRaids();
    }

    @Override
    public boolean isUltraWarm() {
        return this.world.dimensionType().ultraWarm();
    }

    @Override
    public int getSeaLevel() {
        return this.world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return this.getGameRuleValue(GameRule.SPAWN_CHUNK_RADIUS) > 0;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        if (keepLoaded) {
            this.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, this.getGameRuleDefault(GameRule.SPAWN_CHUNK_RADIUS));
        } else {
            this.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0);
        }
    }

    @Override
    public int hashCode() {
        return this.getUID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final CraftWorld other = (CraftWorld) obj;

        return this.getUID() == other.getUID();
    }

    @Override
    public File getWorldFolder() {
        return this.world.levelStorageAccess.getLevelPath(LevelResource.ROOT).toFile().getParentFile();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);

        for (Player player : this.getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : this.getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Override
    public org.bukkit.WorldType getWorldType() {
        return this.world.isFlat() ? org.bukkit.WorldType.FLAT : org.bukkit.WorldType.NORMAL;
    }

    @Override
    public boolean canGenerateStructures() {
        return this.world.serverLevelData.worldGenOptions().generateStructures();
    }

    @Override
    public boolean hasBonusChest() {
        return this.world.serverLevelData.worldGenOptions().generateBonusChest();
    }

    @Override
    public boolean isHardcore() {
        return this.world.getLevelData().isHardcore();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        this.world.serverLevelData.settings.hardcore = hardcore;
    }

    @Override
    @Deprecated
    public long getTicksPerAnimalSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        this.setTicksPerSpawns(SpawnCategory.ANIMAL, ticksPerAnimalSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerMonsterSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        this.setTicksPerSpawns(SpawnCategory.MONSTER, ticksPerMonsterSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        this.setTicksPerSpawns(SpawnCategory.WATER_ANIMAL, ticksPerWaterSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterAmbientSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterAmbientSpawns(int ticksPerWaterAmbientSpawns) {
        this.setTicksPerSpawns(SpawnCategory.WATER_AMBIENT, ticksPerWaterAmbientSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {
        this.setTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE, ticksPerWaterUndergroundCreatureSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerAmbientSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.AMBIENT);
    }

    @Override
    @Deprecated
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        this.setTicksPerSpawns(SpawnCategory.AMBIENT, ticksPerAmbientSpawns);
    }

    @Override
    public void setTicksPerSpawns(SpawnCategory spawnCategory, int ticksPerCategorySpawn) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        this.world.ticksPerSpawnCategory.put(spawnCategory, (long) ticksPerCategorySpawn);
    }

    @Override
    public long getTicksPerSpawns(SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        return this.world.ticksPerSpawnCategory.getLong(spawnCategory);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getWorldMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getWorldMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    @Deprecated
    public int getMonsterSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public void setMonsterSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.MONSTER, limit);
    }

    @Override
    @Deprecated
    public int getAnimalSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public void setAnimalSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.ANIMAL, limit);
    }

    @Override
    @Deprecated
    public int getWaterAnimalSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public void setWaterAnimalSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.WATER_ANIMAL, limit);
    }

    @Override
    @Deprecated
    public int getWaterAmbientSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public void setWaterAmbientSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.WATER_AMBIENT, limit);
    }

    @Override
    @Deprecated
    public int getWaterUndergroundCreatureSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public void setWaterUndergroundCreatureSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE, limit);
    }

    @Override
    @Deprecated
    public int getAmbientSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.AMBIENT);
    }

    @Override
    @Deprecated
    public void setAmbientSpawnLimit(int limit) {
        this.setSpawnLimit(SpawnCategory.AMBIENT, limit);
    }

    @Override
    public int getSpawnLimit(SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        // Paper start - Add mobcaps commands
        return this.getSpawnLimitUnsafe(spawnCategory);
    }
    public final int getSpawnLimitUnsafe(final SpawnCategory spawnCategory) {
        int limit = this.spawnCategoryLimit.getOrDefault(spawnCategory, -1);
        if (limit < 0) {
            limit = this.server.getSpawnLimitUnsafe(spawnCategory);
            // Paper end - Add mobcaps commands
        }
        return limit;
    }

    @Override
    public void setSpawnLimit(SpawnCategory spawnCategory, int limit) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);

        this.spawnCategoryLimit.put(spawnCategory, limit);
    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        this.playSound(loc, instrument.getSound(), org.bukkit.SoundCategory.RECORDS, 3f, note.getPitch());
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        this.playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        this.playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(loc, sound, category, volume, pitch, this.getHandle().random.nextLong());;
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(loc, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        this.getHandle().playSeededSound(null, x, y, z, CraftSound.bukkitToMinecraft(sound), SoundSource.valueOf(category.name()), volume, pitch, seed);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        if (loc == null || sound == null || category == null) return;

        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        ClientboundSoundPacket packet = new ClientboundSoundPacket(Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(sound))), SoundSource.valueOf(category.name()), x, y, z, volume, pitch, seed);
        this.world.getServer().getPlayerList().broadcast(null, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.world.dimension(), packet);
    }

    @Override
    public void playSound(Entity entity, Sound sound, float volume, float pitch) {
        this.playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Entity entity, String sound, float volume, float pitch) {
        this.playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Entity entity, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(entity, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(entity, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(Entity entity, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        if (!(entity instanceof CraftEntity craftEntity) || entity.getWorld() != this || sound == null || category == null) return;

        ClientboundSoundEntityPacket packet = new ClientboundSoundEntityPacket(CraftSound.bukkitToMinecraftHolder(sound), net.minecraft.sounds.SoundSource.valueOf(category.name()), craftEntity.getHandle(), volume, pitch, seed);
        ChunkMap.TrackedEntity entityTracker = this.getHandle().getChunkSource().chunkMap.entityMap.get(entity.getEntityId());
        if (entityTracker != null) {
            entityTracker.broadcastAndSend(packet);
        }
    }
    // Paper start - Adventure
    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        final long seed = sound.seed().orElseGet(this.world.getRandom()::nextLong);
        for (ServerPlayer player : this.getHandle().players()) {
            player.connection.send(io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, player.getX(), player.getY(), player.getZ(), seed, null));
        }
    }

    @Override
    public void playSound(Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        if (!(entity instanceof CraftEntity craftEntity) || entity.getWorld() != this || sound == null || category == null) return;

        ClientboundSoundEntityPacket packet = new ClientboundSoundEntityPacket(Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(sound))), net.minecraft.sounds.SoundSource.valueOf(category.name()), craftEntity.getHandle(), volume, pitch, seed);
        ChunkMap.TrackedEntity entityTracker = this.getHandle().getChunkSource().chunkMap.entityMap.get(entity.getEntityId());
        if (entityTracker != null) {
            entityTracker.broadcastAndSend(packet);
        }
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final double x, final double y, final double z) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, x, y, z, sound.seed().orElseGet(this.world.getRandom()::nextLong), this.playSound0(x, y, z));
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final net.kyori.adventure.sound.Sound.Emitter emitter) {
        org.spigotmc.AsyncCatcher.catchOp("play sound"); // Paper
        final long seed = sound.seed().orElseGet(this.getHandle().getRandom()::nextLong);
        if (emitter == net.kyori.adventure.sound.Sound.Emitter.self()) {
            for (ServerPlayer player : this.getHandle().players()) {
                player.connection.send(io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, player, seed, null));
            }
        } else if (emitter instanceof CraftEntity craftEntity) {
            final net.minecraft.world.entity.Entity entity = craftEntity.getHandle();
            io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, entity, seed, this.playSound0(entity.getX(), entity.getY(), entity.getZ()));
        } else {
            throw new IllegalArgumentException("Sound emitter must be an Entity or self(), but was: " + emitter);
        }
    }

    private java.util.function.BiConsumer<net.minecraft.network.protocol.Packet<?>, Float> playSound0(final double x, final double y, final double z) {
        return (packet, distance) -> this.world.getServer().getPlayerList().broadcast(null, x, y, z, distance, this.world.dimension(), packet);
    }
    // Paper end - Adventure

    private Map<String, GameRules.Key<?>> gamerules;
    public synchronized Map<String, GameRules.Key<?>> getGameRulesNMS() {
        if (this.gamerules != null) {
            return this.gamerules;
        }

        return this.gamerules = CraftWorld.getGameRulesNMS(this.getHandle().getGameRules());
    }

    public static Map<String, GameRules.Key<?>> getGameRulesNMS(GameRules gameRules) {
        Map<String, GameRules.Key<?>> gamerules = new HashMap<>();
        gameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            @Override
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                gamerules.put(key.getId(), key);
            }
        });
        return gamerules;
    }

    private Map<String, GameRules.Type<?>> gameruleDefinitions;
    public synchronized Map<String, GameRules.Type<?>> getGameRuleDefinitions() {
        if (this.gameruleDefinitions != null) {
            return this.gameruleDefinitions;
        }

        Map<String, GameRules.Type<?>> gameruleDefinitions = new HashMap<>();
        this.getHandle().getGameRules().visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            @Override
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                gameruleDefinitions.put(key.getId(), type);
            }
        });

        return this.gameruleDefinitions = gameruleDefinitions;
    }

    @Override
    public String getGameRuleValue(String rule) {
        // In method contract for some reason
        if (rule == null) {
            return null;
        }

        GameRules.Value<?> value = this.getHandle().getGameRules().getRule(this.getGameRulesNMS().get(rule));
        return value != null ? value.toString() : "";
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!this.isGameRule(rule)) return false;
        // Paper start - Add WorldGameRuleChangeEvent
        GameRule<?> gameRule = GameRule.getByName(rule);
        io.papermc.paper.event.world.WorldGameRuleChangeEvent event = new io.papermc.paper.event.world.WorldGameRuleChangeEvent(this, null, gameRule, value);
        if (!event.callEvent()) return false;
        // Paper end - Add WorldGameRuleChangeEvent

        GameRules.Value<?> handle = this.getHandle().getGameRules().getRule(this.getGameRulesNMS().get(rule));
        handle.deserialize(event.getValue()); // Paper - Add WorldGameRuleChangeEvent
        handle.onChanged(this.getHandle());
        return true;
    }

    @Override
    public String[] getGameRules() {
        return this.getGameRulesNMS().keySet().toArray(new String[this.getGameRulesNMS().size()]);
    }

    @Override
    public boolean isGameRule(String rule) {
        Preconditions.checkArgument(rule != null, "String rule cannot be null");
        Preconditions.checkArgument(!rule.isEmpty(), "String rule cannot be empty");
        return this.getGameRulesNMS().containsKey(rule);
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        GameRules.Key<? extends GameRules.Value<?>> key = this.getGameRulesNMS().get(rule.getName());
        Preconditions.checkArgument(key != null, "GameRule '%s' is not available", rule.getName());

        return this.getGameRuleResult(rule, this.getHandle().getGameRules().getRule(key));
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        GameRules.Type<?> type = this.getGameRuleDefinitions().get(rule.getName());
        Preconditions.checkArgument(type != null, "GameRule '%s' is not available", rule.getName());

        return this.getGameRuleResult(rule, type.createRule());
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        Preconditions.checkArgument(newValue != null, "GameRule value cannot be null");

        if (!this.isGameRule(rule.getName())) return false;
        // Paper start - Add WorldGameRuleChangeEvent
        io.papermc.paper.event.world.WorldGameRuleChangeEvent event = new io.papermc.paper.event.world.WorldGameRuleChangeEvent(this, null, rule, String.valueOf(newValue));
        if (!event.callEvent()) return false;
        // Paper end - Add WorldGameRuleChangeEvent

        GameRules.Value<?> handle = this.getHandle().getGameRules().getRule(this.getGameRulesNMS().get(rule.getName()));
        handle.deserialize(event.getValue()); // Paper - Add WorldGameRuleChangeEvent
        handle.onChanged(this.getHandle());
        return true;
    }

    private <T> T getGameRuleResult(GameRule<T> rule, GameRules.Value<?> value) {
        if (value == null) {
            return null;
        }

        if (value instanceof GameRules.BooleanValue) {
            return rule.getType().cast(((GameRules.BooleanValue) value).get());
        } else if (value instanceof GameRules.IntegerValue) {
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
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        this.spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        this.spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        // Paper start - Particle API
        this.spawnParticle(particle, null, null, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);
    }
    @Override
    public <T> void spawnParticle(Particle particle, List<Player> receivers, Player sender, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        // Paper end - Particle API
        data = CraftParticle.convertLegacy(data);
        if (data != null) {
            Preconditions.checkArgument(particle.getDataType().isInstance(data), "data (%s) should be %s", data.getClass(), particle.getDataType());
        }
        this.getHandle().sendParticlesSource(
                receivers == null ? this.getHandle().players() : receivers.stream().map(player -> ((CraftPlayer) player).getHandle()).collect(java.util.stream.Collectors.toList()), // Paper -  Particle API
                sender != null ? ((CraftPlayer) sender).getHandle() : null, // Sender // Paper - Particle API
                CraftParticle.createParticleParam(particle, data), // Particle
                force,
                false,
                x, y, z, // Position
                count,  // Count
                offsetX, offsetY, offsetZ, // Random offset
                extra // Speed?
        );

    }

    @Deprecated
    @Override
    public Location locateNearestStructure(Location origin, org.bukkit.StructureType structureType, int radius, boolean findUnexplored) {
        StructureSearchResult result = null;

        // Manually map the mess of the old StructureType to the new StructureType and normal Structure
        if (org.bukkit.StructureType.MINESHAFT == structureType) {
            result = this.locateNearestStructure(origin, StructureType.MINESHAFT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.VILLAGE == structureType) {
            result = this.locateNearestStructure(origin, List.of(Structure.VILLAGE_DESERT, Structure.VILLAGE_PLAINS, Structure.VILLAGE_SAVANNA, Structure.VILLAGE_SNOWY, Structure.VILLAGE_TAIGA), radius, findUnexplored);
        } else if (org.bukkit.StructureType.NETHER_FORTRESS == structureType) {
            result = this.locateNearestStructure(origin, StructureType.FORTRESS, radius, findUnexplored);
        } else if (org.bukkit.StructureType.STRONGHOLD == structureType) {
            result = this.locateNearestStructure(origin, StructureType.STRONGHOLD, radius, findUnexplored);
        } else if (org.bukkit.StructureType.JUNGLE_PYRAMID == structureType) {
            result = this.locateNearestStructure(origin, StructureType.JUNGLE_TEMPLE, radius, findUnexplored);
        } else if (org.bukkit.StructureType.OCEAN_RUIN == structureType) {
            result = this.locateNearestStructure(origin, StructureType.OCEAN_RUIN, radius, findUnexplored);
        } else if (org.bukkit.StructureType.DESERT_PYRAMID == structureType) {
            result = this.locateNearestStructure(origin, StructureType.DESERT_PYRAMID, radius, findUnexplored);
        } else if (org.bukkit.StructureType.IGLOO == structureType) {
            result = this.locateNearestStructure(origin, StructureType.IGLOO, radius, findUnexplored);
        } else if (org.bukkit.StructureType.SWAMP_HUT == structureType) {
            result = this.locateNearestStructure(origin, StructureType.SWAMP_HUT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.OCEAN_MONUMENT == structureType) {
            result = this.locateNearestStructure(origin, StructureType.OCEAN_MONUMENT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.END_CITY == structureType) {
            result = this.locateNearestStructure(origin, StructureType.END_CITY, radius, findUnexplored);
        } else if (org.bukkit.StructureType.WOODLAND_MANSION == structureType) {
            result = this.locateNearestStructure(origin, StructureType.WOODLAND_MANSION, radius, findUnexplored);
        } else if (org.bukkit.StructureType.BURIED_TREASURE == structureType) {
            result = this.locateNearestStructure(origin, StructureType.BURIED_TREASURE, radius, findUnexplored);
        } else if (org.bukkit.StructureType.SHIPWRECK == structureType) {
            result = this.locateNearestStructure(origin, StructureType.SHIPWRECK, radius, findUnexplored);
        } else if (org.bukkit.StructureType.PILLAGER_OUTPOST == structureType) {
            result = this.locateNearestStructure(origin, Structure.PILLAGER_OUTPOST, radius, findUnexplored);
        } else if (org.bukkit.StructureType.NETHER_FOSSIL == structureType) {
            result = this.locateNearestStructure(origin, StructureType.NETHER_FOSSIL, radius, findUnexplored);
        } else if (org.bukkit.StructureType.RUINED_PORTAL == structureType) {
            result = this.locateNearestStructure(origin, StructureType.RUINED_PORTAL, radius, findUnexplored);
        } else if (org.bukkit.StructureType.BASTION_REMNANT == structureType) {
            result = this.locateNearestStructure(origin, Structure.BASTION_REMNANT, radius, findUnexplored);
        }

        return (result == null) ? null : result.getLocation();
    }

    @Override
    public StructureSearchResult locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored) {
        List<Structure> structures = new ArrayList<>();
        for (Structure structure : RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE)) {
            if (structure.getStructureType() == structureType) {
                structures.add(structure);
            }
        }

        return this.locateNearestStructure(origin, structures, radius, findUnexplored);
    }

    @Override
    public StructureSearchResult locateNearestStructure(Location origin, Structure structure, int radius, boolean findUnexplored) {
        return this.locateNearestStructure(origin, List.of(structure), radius, findUnexplored);
    }

    public StructureSearchResult locateNearestStructure(Location origin, List<Structure> structures, int radius, boolean findUnexplored) {
        BlockPos originPos = BlockPos.containing(origin.getX(), origin.getY(), origin.getZ());
        List<Holder<net.minecraft.world.level.levelgen.structure.Structure>> holders = new ArrayList<>();

        for (Structure structure : structures) {
            holders.add(Holder.direct(CraftStructure.bukkitToMinecraft(structure)));
        }

        Pair<BlockPos, Holder<net.minecraft.world.level.levelgen.structure.Structure>> found = this.getHandle().getChunkSource().getGenerator().findNearestMapStructure(this.getHandle(), HolderSet.direct(holders), originPos, radius, findUnexplored);
        if (found == null) {
            return null;
        }

        return new CraftStructureSearchResult(CraftStructure.minecraftToBukkit(found.getSecond().value()), CraftLocation.toBukkit(found.getFirst(), this));
    }

    // Paper start
    @Override
    public double getCoordinateScale() {
        return getHandle().dimensionType().coordinateScale();
    }

    @Override
    public boolean isFixedTime() {
        return getHandle().dimensionType().hasFixedTime();
    }

    @Override
    public Collection<org.bukkit.Material> getInfiniburn() {
        return com.google.common.collect.Sets.newHashSet(com.google.common.collect.Iterators.transform(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getTagOrEmpty(this.getHandle().dimensionType().infiniburn()).iterator(), blockHolder -> CraftBlockType.minecraftToBukkit(blockHolder.value())));
    }

    @Override
    public void sendGameEvent(Entity sourceEntity, org.bukkit.GameEvent gameEvent, Vector position) {
        getHandle().gameEvent(sourceEntity != null ? ((CraftEntity) sourceEntity).getHandle(): null, net.minecraft.core.registries.BuiltInRegistries.GAME_EVENT.get(org.bukkit.craftbukkit.util.CraftNamespacedKey.toMinecraft(gameEvent.getKey())).orElseThrow(), org.bukkit.craftbukkit.util.CraftVector.toBlockPos(position));
    }
    // Paper end

    @Override
    public BiomeSearchResult locateNearestBiome(Location origin, int radius, Biome... biomes) {
        return this.locateNearestBiome(origin, radius, 32, 64, biomes);
    }

    @Override
    public BiomeSearchResult locateNearestBiome(Location origin, int radius, int horizontalInterval, int verticalInterval, Biome... biomes) {
        BlockPos originPos = BlockPos.containing(origin.getX(), origin.getY(), origin.getZ());
        Set<Holder<net.minecraft.world.level.biome.Biome>> holders = new HashSet<>();

        for (Biome biome : biomes) {
            holders.add(CraftBiome.bukkitToMinecraftHolder(biome));
        }

        Climate.Sampler sampler = this.getHandle().getChunkSource().randomState().sampler();
        // The given predicate is evaluated once at the start of the search, so performance isn't a large concern.
        Pair<BlockPos, Holder<net.minecraft.world.level.biome.Biome>> found = this.getHandle().getChunkSource().getGenerator().getBiomeSource().findClosestBiome3d(originPos, radius, horizontalInterval, verticalInterval, holders::contains, sampler, this.getHandle());
        if (found == null) {
            return null;
        }

        return new CraftBiomeSearchResult(CraftBiome.minecraftHolderToBukkit(found.getSecond()), CraftLocation.toBukkit(found.getFirst(), this));
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(radius >= 0, "Radius value (%s) cannot be negative", radius);

        Raids persistentRaid = this.world.getRaids();
        net.minecraft.world.entity.raid.Raid raid = persistentRaid.getNearbyRaid(CraftLocation.toBlockPosition(location), radius * radius);
        return (raid == null) ? null : new CraftRaid(raid, this.world);
    }

    // Paper start - more Raid API
    @Override
    public @Nullable Raid getRaid(final int id) {
        final net.minecraft.world.entity.raid.@Nullable Raid nmsRaid = this.world.getRaids().raidMap.get(id);
        return nmsRaid != null ? new CraftRaid(nmsRaid, this.world) : null;
    }
    // Paper end - more Raid API

    @Override
    public List<Raid> getRaids() {
        Raids persistentRaid = this.world.getRaids();
        return persistentRaid.raidMap.values().stream().map(raid -> new CraftRaid(raid, this.world)).collect(Collectors.toList());
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        return (this.getHandle().getDragonFight() == null) ? null : new CraftDragonBattle(this.getHandle().getDragonFight());
    }

    @Override
    public Collection<GeneratedStructure> getStructures(int x, int z) {
        return this.getStructures(x, z, struct -> true);
    }

    @Override
    public Collection<GeneratedStructure> getStructures(int x, int z, Structure structure) {
        Preconditions.checkArgument(structure != null, "Structure cannot be null");

        net.minecraft.core.Registry<net.minecraft.world.level.levelgen.structure.Structure> registry = CraftRegistry.getMinecraftRegistry(Registries.STRUCTURE);
        ResourceLocation key = registry.getKey(CraftStructure.bukkitToMinecraft(structure));

        return this.getStructures(x, z, struct -> registry.getKey(struct).equals(key));
    }

    private List<GeneratedStructure> getStructures(int x, int z, Predicate<net.minecraft.world.level.levelgen.structure.Structure> predicate) {
        List<GeneratedStructure> structures = new ArrayList<>();
        for (StructureStart start : this.getHandle().structureManager().startsForStructure(new ChunkPos(x, z), predicate)) {
            structures.add(new CraftGeneratedStructure(start));
        }

        return structures;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    // Paper - replace feature flag API

    public void storeBukkitValues(CompoundTag c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(Tag c) {
        if (c instanceof CompoundTag) {
            this.persistentDataContainer.putAll((CompoundTag) c);
        }
    }

    // Spigot start
    private final org.bukkit.World.Spigot spigot = new org.bukkit.World.Spigot() {

        @Override
        public LightningStrike strikeLightning(Location loc, boolean isSilent) {
            return CraftWorld.this.strikeLightning(loc);
        }

        @Override
        public LightningStrike strikeLightningEffect(Location loc, boolean isSilent) {
            return CraftWorld.this.strikeLightningEffect(loc);
        }
    };

    public org.bukkit.World.Spigot spigot() {
        return this.spigot;
    }
    // Spigot end
    // Paper start
    @Override
    public void getChunkAtAsync(int x, int z, boolean gen, boolean urgent, @NotNull Consumer<? super Chunk> cb) {
        warnUnsafeChunk("getting a faraway chunk async", x, z); // Paper
        ca.spottedleaf.moonrise.common.PlatformHooks.get().scheduleChunkLoad(
            this.getHandle(), x, z, gen, ChunkStatus.FULL, true,
            urgent ? ca.spottedleaf.concurrentutil.util.Priority.HIGHER : ca.spottedleaf.concurrentutil.util.Priority.NORMAL,
            (ChunkAccess chunk) -> {
                cb.accept(chunk == null ? null : new CraftChunk((net.minecraft.world.level.chunk.LevelChunk)chunk));
            }
        );

    }

    @Override
    public void getChunksAtAsync(int minX, int minZ, int maxX, int maxZ, boolean urgent, Runnable cb) {
        warnUnsafeChunk("getting a faraway chunk async", minX, minZ); // Paper
        warnUnsafeChunk("getting a faraway chunk async", maxX, maxZ); // Paper
        this.getHandle().loadChunks(
            minX, minZ, maxX, maxZ,
            urgent ? ca.spottedleaf.concurrentutil.util.Priority.HIGHER : ca.spottedleaf.concurrentutil.util.Priority.NORMAL,
            (List<ChunkAccess> chunks) -> {
                cb.run();
            }
        );
    }

    @Override
    public void setViewDistance(final int viewDistance) {
        FeatureHooks.setViewDistance(this.world, viewDistance); // Paper - chunk system
    }

    @Override
    public void setSimulationDistance(final int simulationDistance) {
        FeatureHooks.setSimulationDistance(this.world, simulationDistance); // Paper - chunk system
    }

    @Override
    public int getSendViewDistance() {
        return FeatureHooks.getSendViewDistance(this.world); // Paper - chunk system
    }

    @Override
    public void setSendViewDistance(final int viewDistance) {
        FeatureHooks.setSendViewDistance(this.world, viewDistance); // Paper - chunk system
    }

    // Paper start - implement pointers
    @Override
    public net.kyori.adventure.pointer.Pointers pointers() {
        if (this.adventure$pointers == null) {
            this.adventure$pointers = net.kyori.adventure.pointer.Pointers.builder()
                .withDynamic(net.kyori.adventure.identity.Identity.NAME, this::getName)
                .withDynamic(net.kyori.adventure.identity.Identity.UUID, this::getUID)
                .build();
        }

        return this.adventure$pointers;
    }
    // Paper end
}
