package net.minecraft.server;

import co.aikar.timings.Timing; // Paper
import com.destroystokyo.paper.PaperWorldConfig; // Paper
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ComparisonChain; // Paper
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap; // Paper
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap; // Paper
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map; // Paper
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.UUID; // Paper
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet; // Paper
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player; // CraftBukkit
import org.spigotmc.AsyncCatcher;

public class PlayerChunkMap extends IChunkLoader implements PlayerChunk.d {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final int GOLDEN_TICKET = 33 + ChunkStatus.b();
    // Paper start - faster copying
    public final Long2ObjectLinkedOpenHashMap<PlayerChunk> updatingChunks = new com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<>(); // Paper - faster copying
    public final Long2ObjectLinkedOpenHashMap<PlayerChunk> visibleChunks = new ProtectedVisibleChunksMap(); // Paper - faster copying

    private class ProtectedVisibleChunksMap extends com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<PlayerChunk> {
        @Override
        public PlayerChunk put(long k, PlayerChunk playerChunk) {
            throw new UnsupportedOperationException("Updating visible Chunks");
        }

        @Override
        public PlayerChunk remove(long k) {
            throw new UnsupportedOperationException("Removing visible Chunks");
        }

        @Override
        public PlayerChunk get(long k) {
            return PlayerChunkMap.this.getVisibleChunk(k);
        }

        public PlayerChunk safeGet(long k) {
            return super.get(k);
        }
    }
    // Paper end
    public final com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<PlayerChunk> pendingVisibleChunks = new com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<PlayerChunk>(); // Paper - this is used if the visible chunks is updated while iterating only
    public transient com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<PlayerChunk> visibleChunksClone; // Paper - used for async access of visible chunks, clone and cache only when needed
    private final Long2ObjectLinkedOpenHashMap<PlayerChunk> pendingUnload;
    final LongSet loadedChunks; // Paper - private -> package
    public final WorldServer world;
    private final LightEngineThreaded lightEngine;
    private final IAsyncTaskHandler<Runnable> executor;
    final java.util.concurrent.Executor mainInvokingExecutor; // Paper
    public final ChunkGenerator chunkGenerator;
    private final Supplier<WorldPersistentData> l; public final Supplier<WorldPersistentData> getWorldPersistentDataSupplier() { return this.l; } // Paper - OBFHELPER
    private final VillagePlace m;
    public final LongSet unloadQueue;
    private boolean updatingChunksModified;
    private final ChunkTaskQueueSorter p;
    private final Mailbox<ChunkTaskQueueSorter.a<Runnable>> mailboxWorldGen;
    final Mailbox<ChunkTaskQueueSorter.a<Runnable>> mailboxMain; // Paper - private -> package private
    // Paper start
    final Mailbox<ChunkTaskQueueSorter.a<Runnable>> mailboxLight;
    public void addLightTask(PlayerChunk playerchunk, Runnable run) {
        this.mailboxLight.a(ChunkTaskQueueSorter.a(playerchunk, run));
    }
    // Paper end
    public final WorldLoadListener worldLoadListener;
    public final PlayerChunkMap.a chunkDistanceManager;
    private final AtomicInteger u;
    public final DefinedStructureManager definedStructureManager; // Paper - private -> public
    private final File w;
    private final PlayerMap playerMap;
    public final Int2ObjectMap<PlayerChunkMap.EntityTracker> trackedEntities;
    private final Long2ByteMap z;
    private final Queue<Runnable> A; private final Queue<Runnable> getUnloadQueueTasks() { return this.A; } // Paper - OBFHELPER
    int viewDistance; // Paper - private -> package private
    public final com.destroystokyo.paper.util.PlayerMobDistanceMap playerMobDistanceMap; // Paper

    // CraftBukkit start - recursion-safe executor for Chunk loadCallback() and unloadCallback()
    public final CallbackExecutor callbackExecutor = new CallbackExecutor();
    public static final class CallbackExecutor implements java.util.concurrent.Executor, Runnable {

        // Paper start - replace impl with recursive safe multi entry queue
        // it's possible to schedule multiple tasks currently, so it's vital we change this impl
        // If we recurse into the executor again, we will append to another queue, ensuring task order consistency
        private java.util.ArrayDeque<Runnable> queued = new java.util.ArrayDeque<>();

        @Override
        public void execute(Runnable runnable) {
            AsyncCatcher.catchOp("Callback Executor execute");
            if (queued == null) {
                queued = new java.util.ArrayDeque<>();
            }
            queued.add(runnable);
        }

        @Override
        public void run() {
            AsyncCatcher.catchOp("Callback Executor run");
            if (queued == null) {
                return;
            }
            java.util.ArrayDeque<Runnable> queue = queued;
            queued = null;
            Runnable task;
            while ((task = queue.pollFirst()) != null) {
                task.run();
            }
        }
        // Paper end
    };
    // CraftBukkit end

    final CallbackExecutor chunkLoadConversionCallbackExecutor = new CallbackExecutor(); // Paper

    // Paper start - distance maps
    private final com.destroystokyo.paper.util.misc.PooledLinkedHashSets<EntityPlayer> pooledLinkedPlayerHashSets = new com.destroystokyo.paper.util.misc.PooledLinkedHashSets<>();
    // Paper start - use distance map to optimise tracker
    public static boolean isLegacyTrackingEntity(Entity entity) {
        return entity.isLegacyTrackingEntity;
    }

    // inlined EnumMap, TrackingRange.TrackingRangeType
    static final org.spigotmc.TrackingRange.TrackingRangeType[] TRACKING_RANGE_TYPES = org.spigotmc.TrackingRange.TrackingRangeType.values();
    final com.destroystokyo.paper.util.misc.PlayerAreaMap[] playerEntityTrackerTrackMaps;
    final int[] entityTrackerTrackRanges;

    private int convertSpigotRangeToVanilla(final int vanilla) {
        return MinecraftServer.getServer().applyTrackingRangeScale(vanilla);
    }
    // Paper end - use distance map to optimise tracker
    // Paper start - optimise PlayerChunkMap#isOutsideRange
    // A note about the naming used here:
    // Previously, mojang used a "spawn range" of 8 for controlling both ticking and
    // mob spawn range. However, spigot makes the spawn range configurable by
    // checking if the chunk is in the tick range (8) and the spawn range
    // obviously this means a spawn range > 8 cannot be implemented

    // these maps are named after spigot's uses
    public final com.destroystokyo.paper.util.misc.PlayerAreaMap playerMobSpawnMap; // this map is absent from updateMaps since it's controlled at the start of the chunkproviderserver tick
    public final com.destroystokyo.paper.util.misc.PlayerAreaMap playerChunkTickRangeMap;
    // Paper end - optimise PlayerChunkMap#isOutsideRange
    // Paper start - no-tick view distance
    int noTickViewDistance;
    public final int getRawNoTickViewDistance() {
        return this.noTickViewDistance;
    }
    public final int getEffectiveNoTickViewDistance() {
        return this.noTickViewDistance == -1 ? this.getEffectiveViewDistance() : this.noTickViewDistance;
    }
    public final int getLoadViewDistance() {
        return Math.max(this.getEffectiveViewDistance(), this.getEffectiveNoTickViewDistance());
    }

    public final com.destroystokyo.paper.util.misc.PlayerAreaMap playerViewDistanceBroadcastMap;
    public final com.destroystokyo.paper.util.misc.PlayerAreaMap playerViewDistanceTickMap;
    public final com.destroystokyo.paper.util.misc.PlayerAreaMap playerViewDistanceNoTickMap;
    // Paper end - no-tick view distance

    void addPlayerToDistanceMaps(EntityPlayer player) {
        int chunkX = MCUtil.getChunkCoordinate(player.locX());
        int chunkZ = MCUtil.getChunkCoordinate(player.locZ());
        // Note: players need to be explicitly added to distance maps before they can be updated
        // Paper start - use distance map to optimise entity tracker
        for (int i = 0, len = TRACKING_RANGE_TYPES.length; i < len; ++i) {
            com.destroystokyo.paper.util.misc.PlayerAreaMap trackMap = this.playerEntityTrackerTrackMaps[i];
            int trackRange = this.entityTrackerTrackRanges[i];

            trackMap.add(player, chunkX, chunkZ, Math.min(trackRange, this.getEffectiveViewDistance()));
        }
        // Paper end - use distance map to optimise entity tracker
        // Paper start - optimise PlayerChunkMap#isOutsideRange
        this.playerChunkTickRangeMap.add(player, chunkX, chunkZ, ChunkMapDistance.MOB_SPAWN_RANGE);
        // Paper end - optimise PlayerChunkMap#isOutsideRange
        // Paper start - no-tick view distance
        int effectiveTickViewDistance = this.getEffectiveViewDistance();
        int effectiveNoTickViewDistance = Math.max(this.getEffectiveNoTickViewDistance(), effectiveTickViewDistance);

        if (!this.cannotLoadChunks(player)) {
            this.playerViewDistanceTickMap.add(player, chunkX, chunkZ, effectiveTickViewDistance);
            this.playerViewDistanceNoTickMap.add(player, chunkX, chunkZ, effectiveNoTickViewDistance + 2); // clients need chunk 1 neighbour, and we need another 1 for sending those extra neighbours (as we require neighbours to send)
        }

        player.needsChunkCenterUpdate = true;
        this.playerViewDistanceBroadcastMap.add(player, chunkX, chunkZ, effectiveNoTickViewDistance + 1); // clients need an extra neighbour to render the full view distance configured
        player.needsChunkCenterUpdate = false;
        // Paper end - no-tick view distance
    }

    void removePlayerFromDistanceMaps(EntityPlayer player) {
        // Paper start - use distance map to optimise tracker
        for (int i = 0, len = TRACKING_RANGE_TYPES.length; i < len; ++i) {
            this.playerEntityTrackerTrackMaps[i].remove(player);
        }
        // Paper end - use distance map to optimise tracker
        // Paper start - optimise PlayerChunkMap#isOutsideRange
        this.playerMobSpawnMap.remove(player);
        this.playerChunkTickRangeMap.remove(player);
        // Paper end - optimise PlayerChunkMap#isOutsideRange
        // Paper start - no-tick view distance
        this.playerViewDistanceBroadcastMap.remove(player);
        this.playerViewDistanceTickMap.remove(player);
        this.playerViewDistanceNoTickMap.remove(player);
        // Paper end - no-tick view distance
    }

    void updateMaps(EntityPlayer player) {
        int chunkX = MCUtil.getChunkCoordinate(player.locX());
        int chunkZ = MCUtil.getChunkCoordinate(player.locZ());
        // Note: players need to be explicitly added to distance maps before they can be updated
        // Paper start - use distance map to optimise entity tracker
        for (int i = 0, len = TRACKING_RANGE_TYPES.length; i < len; ++i) {
            com.destroystokyo.paper.util.misc.PlayerAreaMap trackMap = this.playerEntityTrackerTrackMaps[i];
            int trackRange = this.entityTrackerTrackRanges[i];

            trackMap.update(player, chunkX, chunkZ, Math.min(trackRange, this.getEffectiveViewDistance()));
        }
        // Paper end - use distance map to optimise entity tracker
        // Paper start - optimise PlayerChunkMap#isOutsideRange
        this.playerChunkTickRangeMap.update(player, chunkX, chunkZ, ChunkMapDistance.MOB_SPAWN_RANGE);
        // Paper end - optimise PlayerChunkMap#isOutsideRange
        // Paper start - no-tick view distance
        int effectiveTickViewDistance = this.getEffectiveViewDistance();
        int effectiveNoTickViewDistance = Math.max(this.getEffectiveNoTickViewDistance(), effectiveTickViewDistance);

        if (!this.cannotLoadChunks(player)) {
            this.playerViewDistanceTickMap.update(player, chunkX, chunkZ, effectiveTickViewDistance);
            this.playerViewDistanceNoTickMap.update(player, chunkX, chunkZ, effectiveNoTickViewDistance + 2); // clients need chunk 1 neighbour, and we need another 1 for sending those extra neighbours (as we require neighbours to send)
        }

        player.needsChunkCenterUpdate = true;
        this.playerViewDistanceBroadcastMap.update(player, chunkX, chunkZ, effectiveNoTickViewDistance + 1); // clients need an extra neighbour to render the full view distance configured
        player.needsChunkCenterUpdate = false;
        // Paper end - no-tick view distance
    }
    // Paper end

    public PlayerChunkMap(WorldServer worldserver, Convertable.ConversionSession convertable_conversionsession, DataFixer datafixer, DefinedStructureManager definedstructuremanager, Executor executor, IAsyncTaskHandler<Runnable> iasynctaskhandler, ILightAccess ilightaccess, ChunkGenerator chunkgenerator, WorldLoadListener worldloadlistener, Supplier<WorldPersistentData> supplier, int i, boolean flag) {
        super(new File(convertable_conversionsession.a(worldserver.getDimensionKey()), "region"), datafixer, flag);
        //this.visibleChunks = this.updatingChunks.clone(); // Paper - no more cloning
        this.pendingUnload = new Long2ObjectLinkedOpenHashMap();
        this.loadedChunks = new LongOpenHashSet();
        this.unloadQueue = new LongOpenHashSet();
        this.u = new AtomicInteger();
        this.playerMap = new PlayerMap();
        this.trackedEntities = new Int2ObjectOpenHashMap();
        this.z = new Long2ByteOpenHashMap();
        this.A = new com.destroystokyo.paper.utils.CachedSizeConcurrentLinkedQueue<>(); // Paper - need constant-time size()
        this.definedStructureManager = definedstructuremanager;
        this.w = convertable_conversionsession.a(worldserver.getDimensionKey());
        this.world = worldserver;
        this.chunkGenerator = chunkgenerator;
        this.executor = iasynctaskhandler;
        // Paper start
        this.mainInvokingExecutor = (run) -> {
            if (MCUtil.isMainThread()) {
                run.run();
            } else {
                iasynctaskhandler.execute(run);
            }
        };
        // Paper end
        ThreadedMailbox<Runnable> threadedmailbox = ThreadedMailbox.a(executor, "worldgen");

        iasynctaskhandler.getClass();
        Mailbox<Runnable> mailbox = Mailbox.a("main", iasynctaskhandler::a);

        this.worldLoadListener = worldloadlistener;
        ThreadedMailbox<Runnable> lightthreaded; ThreadedMailbox<Runnable> threadedmailbox1 = lightthreaded = ThreadedMailbox.a(executor, "light"); // Paper

        this.p = new ChunkTaskQueueSorter(ImmutableList.of(threadedmailbox, mailbox, threadedmailbox1), executor, Integer.MAX_VALUE);
        this.mailboxWorldGen = this.p.a(threadedmailbox, false);
        this.mailboxMain = this.p.a(mailbox, false);
        this.mailboxLight = this.p.a(lightthreaded, false);// Paper
        this.lightEngine = new LightEngineThreaded(ilightaccess, this, this.world.getDimensionManager().hasSkyLight(), threadedmailbox1, this.p.a(threadedmailbox1, false));
        this.chunkDistanceManager = new PlayerChunkMap.a(executor, iasynctaskhandler); this.chunkDistanceManager.chunkMap = this; // Paper
        this.l = supplier;
        this.m = new VillagePlace(new File(this.w, "poi"), datafixer, flag, this.world); // Paper
        this.setViewDistance(i);
        this.playerMobDistanceMap = this.world.paperConfig.perPlayerMobSpawns ? new com.destroystokyo.paper.util.PlayerMobDistanceMap() : null; // Paper
        // Paper start - use distance map to optimise entity tracker
        this.playerEntityTrackerTrackMaps = new com.destroystokyo.paper.util.misc.PlayerAreaMap[TRACKING_RANGE_TYPES.length];
        this.entityTrackerTrackRanges = new int[TRACKING_RANGE_TYPES.length];

        org.spigotmc.SpigotWorldConfig spigotWorldConfig = this.world.spigotConfig;

        for (int ordinal = 0, len = TRACKING_RANGE_TYPES.length; ordinal < len; ++ordinal) {
            org.spigotmc.TrackingRange.TrackingRangeType trackingRangeType = TRACKING_RANGE_TYPES[ordinal];
            int configuredSpigotValue;
            switch (trackingRangeType) {
                case PLAYER:
                    configuredSpigotValue = spigotWorldConfig.playerTrackingRange;
                    break;
                case ANIMAL:
                    configuredSpigotValue = spigotWorldConfig.animalTrackingRange;
                    break;
                case MONSTER:
                    configuredSpigotValue = spigotWorldConfig.monsterTrackingRange;
                    break;
                case MISC:
                    configuredSpigotValue = spigotWorldConfig.miscTrackingRange;
                    break;
                case OTHER:
                    configuredSpigotValue = spigotWorldConfig.otherTrackingRange;
                    break;
                case ENDERDRAGON:
                    configuredSpigotValue = EntityTypes.ENDER_DRAGON.getChunkRange() * 16;
                    break;
                default:
                    throw new IllegalStateException("Missing case for enum " + trackingRangeType);
            }
            configuredSpigotValue = convertSpigotRangeToVanilla(configuredSpigotValue);

            int trackRange = (configuredSpigotValue >>> 4) + ((configuredSpigotValue & 15) != 0 ? 1 : 0);
            this.entityTrackerTrackRanges[ordinal] = trackRange;

            this.playerEntityTrackerTrackMaps[ordinal] = new com.destroystokyo.paper.util.misc.PlayerAreaMap(this.pooledLinkedPlayerHashSets);
        }
        // Paper end - use distance map to optimise entity tracker
        // Paper start - optimise PlayerChunkMap#isOutsideRange
        this.playerChunkTickRangeMap = new com.destroystokyo.paper.util.misc.PlayerAreaMap(this.pooledLinkedPlayerHashSets,
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                PlayerChunk playerChunk = PlayerChunkMap.this.getUpdatingChunk(MCUtil.getCoordinateKey(rangeX, rangeZ));
                if (playerChunk != null) {
                    playerChunk.playersInChunkTickRange = newState;
                }
            },
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                PlayerChunk playerChunk = PlayerChunkMap.this.getUpdatingChunk(MCUtil.getCoordinateKey(rangeX, rangeZ));
                if (playerChunk != null) {
                    playerChunk.playersInChunkTickRange = newState;
                }
            });
        this.playerMobSpawnMap = new com.destroystokyo.paper.util.misc.PlayerAreaMap(this.pooledLinkedPlayerHashSets,
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                PlayerChunk playerChunk = PlayerChunkMap.this.getUpdatingChunk(MCUtil.getCoordinateKey(rangeX, rangeZ));
                if (playerChunk != null) {
                    playerChunk.playersInMobSpawnRange = newState;
                }
            },
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                PlayerChunk playerChunk = PlayerChunkMap.this.getUpdatingChunk(MCUtil.getCoordinateKey(rangeX, rangeZ));
                if (playerChunk != null) {
                    playerChunk.playersInMobSpawnRange = newState;
                }
            });
        // Paper end - optimise PlayerChunkMap#isOutsideRange
        // Paper start - no-tick view distance
        this.setNoTickViewDistance(this.world.paperConfig.noTickViewDistance);
        this.playerViewDistanceTickMap = new com.destroystokyo.paper.util.misc.PlayerAreaMap(this.pooledLinkedPlayerHashSets,
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                checkHighPriorityChunks(player);
                if (newState.size() != 1) {
                    return;
                }
                Chunk chunk = PlayerChunkMap.this.world.getChunkProvider().getChunkAtIfLoadedMainThreadNoCache(rangeX, rangeZ);
                if (chunk == null || !chunk.areNeighboursLoaded(2)) {
                    return;
                }

                ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(rangeX, rangeZ);
                PlayerChunkMap.this.world.getChunkProvider().addTicketAtLevel(TicketType.PLAYER, chunkPos, 31, chunkPos); // entity ticking level, TODO check on update
            },
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                if (newState != null) {
                    return;
                }
                ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(rangeX, rangeZ);
                PlayerChunkMap.this.world.getChunkProvider().removeTicketAtLevel(TicketType.PLAYER, chunkPos, 31, chunkPos); // entity ticking level, TODO check on update
                PlayerChunkMap.this.world.getChunkProvider().clearPriorityTickets(chunkPos);
            }, (player, prevPos, newPos) -> {
            player.lastHighPriorityChecked = -1; // reset and recheck
            checkHighPriorityChunks(player);
        });
        this.playerViewDistanceNoTickMap = new com.destroystokyo.paper.util.misc.PlayerAreaMap(this.pooledLinkedPlayerHashSets);
        this.playerViewDistanceBroadcastMap = new com.destroystokyo.paper.util.misc.PlayerAreaMap(this.pooledLinkedPlayerHashSets,
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                if (player.needsChunkCenterUpdate) {
                    player.needsChunkCenterUpdate = false;
                    player.playerConnection.sendPacket(new PacketPlayOutViewCentre(currPosX, currPosZ));
                }
                PlayerChunkMap.this.sendChunk(player, new ChunkCoordIntPair(rangeX, rangeZ), new Packet[2], false, true); // unloaded, loaded
            },
            (EntityPlayer player, int rangeX, int rangeZ, int currPosX, int currPosZ, int prevPosX, int prevPosZ,
             com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newState) -> {
                PlayerChunkMap.this.sendChunk(player, new ChunkCoordIntPair(rangeX, rangeZ), null, true, false); // unloaded, loaded
            });
        // Paper end - no-tick view distance
    }
    // Paper start - Chunk Prioritization
    public void queueHolderUpdate(PlayerChunk playerchunk) {
        Runnable runnable = () -> {
            if (isUnloading(playerchunk)) {
                return; // unloaded
            }
            chunkDistanceManager.pendingChunkUpdates.add(playerchunk);
            if (!chunkDistanceManager.pollingPendingChunkUpdates) {
                world.getChunkProvider().tickDistanceManager();
            }
        };
        if (MCUtil.isMainThread()) {
            // We can't use executor here because it will not execute tasks if its currently in the middle of executing tasks...
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }

    private boolean isUnloading(PlayerChunk playerchunk) {
        return playerchunk == null || unloadQueue.contains(playerchunk.location.pair());
    }

    private void updateChunkPriorityMap(Long2IntOpenHashMap map, long chunk, int level) {
        int prev = map.getOrDefault(chunk, -1);
        if (level > prev) {
            map.put(chunk, level);
        }
    }

    public void checkHighPriorityChunks(EntityPlayer player) {
        int currentTick = MinecraftServer.currentTick;
        if (currentTick - player.lastHighPriorityChecked < 20 || !player.isRealPlayer) { // weed out fake players
            return;
        }
        player.lastHighPriorityChecked = currentTick;
        Long2IntOpenHashMap priorities = new Long2IntOpenHashMap();

        int viewDistance = getEffectiveNoTickViewDistance();
        BlockPosition.MutableBlockPosition pos = new BlockPosition.MutableBlockPosition();

        // Prioritize circular near
        double playerChunkX = MathHelper.floor(player.locX()) >> 4;
        double playerChunkZ = MathHelper.floor(player.locZ()) >> 4;
        pos.setValues(player.locX(), 0, player.locZ());
        double twoThirdModifier = 2D / 3D;
        MCUtil.getSpiralOutChunks(pos, Math.min(6, viewDistance)).forEach(coord -> {
            if (shouldSkipPrioritization(coord)) return;

            double dist = MCUtil.distance(playerChunkX, 0, playerChunkZ, coord.x, 0, coord.z);
            // Prioritize immediate
            if (dist <= 4 * 4) {
                updateChunkPriorityMap(priorities, coord.pair(), (int) (27 - Math.sqrt(dist)));
                return;
            }

            // Prioritize nearby chunks
            updateChunkPriorityMap(priorities, coord.pair(), (int) (20 - Math.sqrt(dist) * twoThirdModifier));
        });

        // Prioritize Frustum near 3
        ChunkCoordIntPair front3 = player.getChunkInFront(3);
        pos.setValues(front3.x << 4, 0, front3.z << 4);
        MCUtil.getSpiralOutChunks(pos, Math.min(5, viewDistance)).forEach(coord -> {
            if (shouldSkipPrioritization(coord)) return;

            double dist = MCUtil.distance(playerChunkX, 0, playerChunkZ, coord.x, 0, coord.z);
            updateChunkPriorityMap(priorities, coord.pair(), (int) (25 - Math.sqrt(dist) * twoThirdModifier));
        });

        // Prioritize Frustum near 5
        if (viewDistance > 4) {
            ChunkCoordIntPair front5 = player.getChunkInFront(5);
            pos.setValues(front5.x << 4, 0, front5.z << 4);
            MCUtil.getSpiralOutChunks(pos, 4).forEach(coord -> {
                if (shouldSkipPrioritization(coord)) return;

                double dist = MCUtil.distance(playerChunkX, 0, playerChunkZ, coord.x, 0, coord.z);
                updateChunkPriorityMap(priorities, coord.pair(), (int) (25 - Math.sqrt(dist) * twoThirdModifier));
            });
        }

        // Prioritize Frustum far 7
        if (viewDistance > 6) {
            ChunkCoordIntPair front7 = player.getChunkInFront(7);
            pos.setValues(front7.x << 4, 0, front7.z << 4);
            MCUtil.getSpiralOutChunks(pos, 3).forEach(coord -> {
                if (shouldSkipPrioritization(coord)) {
                    return;
                }
                double dist = MCUtil.distance(playerChunkX, 0, playerChunkZ, coord.x, 0, coord.z);
                updateChunkPriorityMap(priorities, coord.pair(), (int) (25 - Math.sqrt(dist) * twoThirdModifier));
            });
        }

        if (priorities.isEmpty()) return;
        chunkDistanceManager.delayDistanceManagerTick = true;
        priorities.long2IntEntrySet().fastForEach(entry -> chunkDistanceManager.markHighPriority(new ChunkCoordIntPair(entry.getLongKey()), entry.getIntValue()));
        chunkDistanceManager.delayDistanceManagerTick = false;
        world.getChunkProvider().tickDistanceManager();

    }

    private boolean shouldSkipPrioritization(ChunkCoordIntPair coord) {
        if (playerViewDistanceNoTickMap.getObjectsInRange(coord.pair()) == null) return true;
        PlayerChunk chunk = getUpdatingChunk(coord.pair());
        return chunk != null && (chunk.isFullChunkReady());
    }
    // Paper end

    public void updatePlayerMobTypeMap(Entity entity) {
        if (!this.world.paperConfig.perPlayerMobSpawns) {
            return;
        }
        int chunkX = (int)Math.floor(entity.locX()) >> 4;
        int chunkZ = (int)Math.floor(entity.locZ()) >> 4;
        int index = entity.getEntityType().getEnumCreatureType().ordinal();

        for (EntityPlayer player : this.playerMobDistanceMap.getPlayersInRange(chunkX, chunkZ)) {
            ++player.mobCounts[index];
        }
    }

    public int getMobCountNear(EntityPlayer entityPlayer, EnumCreatureType enumCreatureType) {
        return entityPlayer.mobCounts[enumCreatureType.ordinal()];
    }

    private static double getDistanceSquaredFromChunk(ChunkCoordIntPair chunkPos, Entity entity) { return a(chunkPos, entity); } // Paper - OBFHELPER
    private static double a(ChunkCoordIntPair chunkcoordintpair, Entity entity) {
        double d0 = (double) (chunkcoordintpair.x * 16 + 8);
        double d1 = (double) (chunkcoordintpair.z * 16 + 8);
        double d2 = d0 - entity.locX();
        double d3 = d1 - entity.locZ();

        return d2 * d2 + d3 * d3;
    }

    private static int b(ChunkCoordIntPair chunkcoordintpair, EntityPlayer entityplayer, boolean flag) {
        int i;
        int j;

        if (flag) {
            SectionPosition sectionposition = entityplayer.O();

            i = sectionposition.a();
            j = sectionposition.c();
        } else {
            i = MathHelper.floor(entityplayer.locX() / 16.0D);
            j = MathHelper.floor(entityplayer.locZ() / 16.0D);
        }

        return a(chunkcoordintpair, i, j);
    }

    private static int a(ChunkCoordIntPair chunkcoordintpair, int i, int j) {
        int k = chunkcoordintpair.x - i;
        int l = chunkcoordintpair.z - j;

        return Math.max(Math.abs(k), Math.abs(l));
    }

    protected LightEngineThreaded a() {
        return this.lightEngine;
    }

    @Nullable
    public PlayerChunk getUpdatingChunk(long i) { // Paper
        return (PlayerChunk) this.updatingChunks.get(i);
    }

    // Paper start - remove cloning of visible chunks unless accessed as a collection async
    private static final boolean DEBUG_ASYNC_VISIBLE_CHUNKS = Boolean.getBoolean("paper.debug-async-visible-chunks");
    private boolean isIterating = false;
    private boolean hasPendingVisibleUpdate = false;
    public void forEachVisibleChunk(java.util.function.Consumer<PlayerChunk> consumer) {
        org.spigotmc.AsyncCatcher.catchOp("forEachVisibleChunk");
        boolean prev = isIterating;
        isIterating = true;
        try {
            for (PlayerChunk value : this.visibleChunks.values()) {
                consumer.accept(value);
            }
        } finally {
            this.isIterating = prev;
            if (!this.isIterating && this.hasPendingVisibleUpdate) {
                ((ProtectedVisibleChunksMap)this.visibleChunks).copyFrom(this.pendingVisibleChunks);
                this.pendingVisibleChunks.clear();
                this.hasPendingVisibleUpdate = false;
            }
        }
    }
    public Long2ObjectLinkedOpenHashMap<PlayerChunk> getVisibleChunks() {
        if (Thread.currentThread() == this.world.serverThread) {
            return this.visibleChunks;
        } else {
            synchronized (this.visibleChunks) {
                if (DEBUG_ASYNC_VISIBLE_CHUNKS) new Throwable("Async getVisibleChunks").printStackTrace();
                if (this.visibleChunksClone == null) {
                    this.visibleChunksClone = this.hasPendingVisibleUpdate ? this.pendingVisibleChunks.clone() : ((ProtectedVisibleChunksMap)this.visibleChunks).clone();
                }
                return this.visibleChunksClone;
            }
        }
    }
    // Paper end

    @Nullable
    public PlayerChunk getVisibleChunk(long i) { // Paper - protected -> public
        // Paper start - mt safe get
        if (Thread.currentThread() != this.world.serverThread) {
            synchronized (this.visibleChunks) {
                return (PlayerChunk) (this.hasPendingVisibleUpdate ? this.pendingVisibleChunks.get(i) : ((ProtectedVisibleChunksMap)this.visibleChunks).safeGet(i));
            }
        }
        return (PlayerChunk) (this.hasPendingVisibleUpdate ? this.pendingVisibleChunks.get(i) : ((ProtectedVisibleChunksMap)this.visibleChunks).safeGet(i));
        // Paper end
    }

    protected IntSupplier c(long i) {
        return () -> {
            PlayerChunk playerchunk = this.getVisibleChunk(i);

            return playerchunk == null ? ChunkTaskQueue.a - 1 : Math.min(playerchunk.k(), ChunkTaskQueue.a - 1);
        };
    }

    // Paper start
    public final int getEffectiveViewDistance() {
        // TODO this needs to be checked on update
        // Mojang currently sets it to +1 of the configured view distance. So subtract one to get the one we really want.
        return this.viewDistance - 1;
    }
    // Paper end

    private CompletableFuture<Either<List<IChunkAccess>, PlayerChunk.Failure>> a(ChunkCoordIntPair chunkcoordintpair, int i, IntFunction<ChunkStatus> intfunction) {
        List<CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>>> list = Lists.newArrayList();
        int j = chunkcoordintpair.x;
        int k = chunkcoordintpair.z;
        PlayerChunk requestingNeighbor = getUpdatingChunk(chunkcoordintpair.pair()); // Paper

        for (int l = -i; l <= i; ++l) {
            for (int i1 = -i; i1 <= i; ++i1) {
                int j1 = Math.max(Math.abs(i1), Math.abs(l));
                final ChunkCoordIntPair chunkcoordintpair1 = new ChunkCoordIntPair(j + i1, k + l);
                long k1 = chunkcoordintpair1.pair();
                PlayerChunk playerchunk = this.getUpdatingChunk(k1);

                if (playerchunk == null) {
                    return CompletableFuture.completedFuture(Either.right(new PlayerChunk.Failure() {
                        public String toString() {
                            return "Unloaded " + chunkcoordintpair1.toString();
                        }
                    }));
                }

                ChunkStatus chunkstatus = (ChunkStatus) intfunction.apply(j1);
                CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> completablefuture = playerchunk.a(chunkstatus, this);
                // Paper start
                if (requestingNeighbor != null && requestingNeighbor != playerchunk && !completablefuture.isDone()) {
                    requestingNeighbor.onNeighborRequest(playerchunk, chunkstatus);
                    completablefuture.thenAccept(either -> {
                        requestingNeighbor.onNeighborDone(playerchunk, chunkstatus, either.left().orElse(null));
                    });
                }
                // Paper end

                list.add(completablefuture);
            }
        }

        CompletableFuture<List<Either<IChunkAccess, PlayerChunk.Failure>>> completablefuture1 = SystemUtils.b((List) list);

        return completablefuture1.thenApply((list1) -> {
            List<IChunkAccess> list2 = Lists.newArrayList();
            // CraftBukkit start - decompile error
            int cnt = 0;

            for (Iterator iterator = list1.iterator(); iterator.hasNext(); ++cnt) {
                final int l1 = cnt;
                // CraftBukkit end
                final Either<IChunkAccess, PlayerChunk.Failure> either = (Either) iterator.next();
                Optional<IChunkAccess> optional = either.left();

                if (!optional.isPresent()) {
                    return Either.right(new PlayerChunk.Failure() {
                        public String toString() {
                            return "Unloaded " + new ChunkCoordIntPair(j + l1 % (i * 2 + 1), k + l1 / (i * 2 + 1)) + " " + ((PlayerChunk.Failure) either.right().get()).toString();
                        }
                    });
                }

                list2.add(optional.get());
            }

            return Either.left(list2);
        });
    }

    public CompletableFuture<Either<Chunk, PlayerChunk.Failure>> b(ChunkCoordIntPair chunkcoordintpair) {
        return this.a(chunkcoordintpair, 2, (i) -> {
            return ChunkStatus.FULL;
        }).thenApplyAsync((either) -> {
            return either.mapLeft((list) -> {
                return (Chunk) list.get(list.size() / 2);
            });
        }, this.mainInvokingExecutor); // Paper
    }

    @Nullable
    private PlayerChunk a(long i, int j, @Nullable PlayerChunk playerchunk, int k) {
        if (k > PlayerChunkMap.GOLDEN_TICKET && j > PlayerChunkMap.GOLDEN_TICKET) {
            return playerchunk;
        } else {
            if (playerchunk != null) {
                playerchunk.a(j);
                playerchunk.updateRanges(); // Paper - optimise isOutsideOfRange
            }

            if (playerchunk != null) {
                if (j > PlayerChunkMap.GOLDEN_TICKET) {
                    this.unloadQueue.add(i);
                } else {
                    this.unloadQueue.remove(i);
                }
            }

            if (j <= PlayerChunkMap.GOLDEN_TICKET && playerchunk == null) {
                playerchunk = (PlayerChunk) this.pendingUnload.remove(i);
                if (playerchunk != null) {
                    playerchunk.a(j);
                } else {
                    playerchunk = new PlayerChunk(new ChunkCoordIntPair(i), j, this.lightEngine, this.p, this);
                }

                this.updatingChunks.put(i, playerchunk);
                this.updatingChunksModified = true;
            }

            return playerchunk;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.p.close();
            this.world.asyncChunkTaskManager.close(true); // Paper - Required since we're closing regionfiles in the next line
            this.m.close();
        } finally {
            super.close();
        }

    }

    // Paper start - incremental autosave
    final ObjectRBTreeSet<PlayerChunk> autoSaveQueue = new ObjectRBTreeSet<>((playerchunk1, playerchunk2) -> {
        int timeCompare =  Long.compare(playerchunk1.lastAutoSaveTime, playerchunk2.lastAutoSaveTime);
        if (timeCompare != 0) {
            return timeCompare;
        }

        return Long.compare(MCUtil.getCoordinateKey(playerchunk1.location), MCUtil.getCoordinateKey(playerchunk2.location));
    });

    protected void saveIncrementally() {
        int savedThisTick = 0;
        // optimized since we search far less chunks to hit ones that need to be saved
        List<PlayerChunk> reschedule = new java.util.ArrayList<>(this.world.paperConfig.maxAutoSaveChunksPerTick);
        long currentTick = this.world.getTime();
        long maxSaveTime = currentTick - this.world.paperConfig.autoSavePeriod;

        for (Iterator<PlayerChunk> iterator = this.autoSaveQueue.iterator(); iterator.hasNext();) {
            PlayerChunk playerchunk = iterator.next();
            if (playerchunk.lastAutoSaveTime > maxSaveTime) {
                break;
            }

            iterator.remove();

            IChunkAccess ichunkaccess = playerchunk.getChunkSave().getNow(null);
            if (ichunkaccess instanceof Chunk) {
                boolean shouldSave = ((Chunk)ichunkaccess).lastSaved <= maxSaveTime;

                if (shouldSave && this.saveChunk(ichunkaccess)) {
                    ++savedThisTick;

                    if (!playerchunk.setHasBeenLoaded()) {
                        // do not fall through to reschedule logic
                        playerchunk.inactiveTimeStart = currentTick;
                        if (savedThisTick >= this.world.paperConfig.maxAutoSaveChunksPerTick) {
                            break;
                        }
                        continue;
                    }
                }
            }

            reschedule.add(playerchunk);

            if (savedThisTick >= this.world.paperConfig.maxAutoSaveChunksPerTick) {
                break;
            }
        }

        for (int i = 0, len = reschedule.size(); i < len; ++i) {
            PlayerChunk playerchunk = reschedule.get(i);
            playerchunk.lastAutoSaveTime = this.world.getTime();
            this.autoSaveQueue.add(playerchunk);
        }
    }
    // Paper end

    protected void save(boolean flag) {
        Long2ObjectLinkedOpenHashMap<PlayerChunk> visibleChunks = this.getVisibleChunks(); // Paper remove clone of visible Chunks unless saving off main thread (watchdog kill)
        if (flag) {
            List<PlayerChunk> list = (List) visibleChunks.values().stream().filter(PlayerChunk::hasBeenLoaded).peek(PlayerChunk::m).collect(Collectors.toList()); // Paper - remove cloning of visible chunks
            MutableBoolean mutableboolean = new MutableBoolean();

            do {
                boolean isShuttingDown = world.getMinecraftServer().hasStopped(); // Paper
                mutableboolean.setFalse();
                list.stream().map((playerchunk) -> {
                    CompletableFuture completablefuture;

                    do {
                        completablefuture = playerchunk.getChunkSave();
                        this.executor.awaitTasks(completablefuture::isDone);
                    } while (completablefuture != playerchunk.getChunkSave());

                    return (IChunkAccess) completablefuture.join();
                }).filter((ichunkaccess) -> {
                    return ichunkaccess instanceof ProtoChunkExtension || ichunkaccess instanceof Chunk;
                }).filter(this::saveChunk).forEach((ichunkaccess) -> {
                    mutableboolean.setTrue();
                });
            } while (mutableboolean.isTrue());

            this.b(() -> {
                return true;
            });
            this.world.asyncChunkTaskManager.flush(); // Paper - flush to preserve behavior compat with pre-async behaviour
//            this.i(); // Paper - nuke IOWorker
            PlayerChunkMap.LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.w.getName());
        } else {
            visibleChunks.values().stream().filter(PlayerChunk::hasBeenLoaded).forEach((playerchunk) -> {
                IChunkAccess ichunkaccess = (IChunkAccess) playerchunk.getChunkSave().getNow(null); // CraftBukkit - decompile error

                if (ichunkaccess instanceof ProtoChunkExtension || ichunkaccess instanceof Chunk) {
                    this.saveChunk(ichunkaccess);
                    playerchunk.m();
                }

            });
        }

    }

    private static final double UNLOAD_QUEUE_RESIZE_FACTOR = 0.90; // Spigot // Paper - unload more

    protected void unloadChunks(BooleanSupplier booleansupplier) {
        GameProfilerFiller gameprofilerfiller = this.world.getMethodProfiler();

        try (Timing ignored = this.world.timings.poiUnload.startTiming()) { // Paper
        gameprofilerfiller.enter("poi");
        this.m.a(booleansupplier);
        } // Paper
        gameprofilerfiller.exitEnter("chunk_unload");
        if (!this.world.isSavingDisabled()) {
            try (Timing ignored = this.world.timings.chunkUnload.startTiming()) { // Paper
            this.b(booleansupplier);
            }// Paper
        }

        gameprofilerfiller.exit();
    }

    private void b(BooleanSupplier booleansupplier) {
        LongIterator longiterator = this.unloadQueue.iterator();
        // Spigot start
        org.spigotmc.SlackActivityAccountant activityAccountant = this.world.getMinecraftServer().slackActivityAccountant;
        activityAccountant.startActivity(0.5);
        int targetSize = Math.min(this.unloadQueue.size() - 100,  (int) (this.unloadQueue.size() * UNLOAD_QUEUE_RESIZE_FACTOR)); // Paper - Make more aggressive
        // Spigot end
        while (longiterator.hasNext()) { // Spigot
            long j = longiterator.nextLong();
            longiterator.remove(); // Spigot
            PlayerChunk playerchunk = (PlayerChunk) this.updatingChunks.remove(j);

            if (playerchunk != null) {
                this.pendingUnload.put(j, playerchunk);
                this.updatingChunksModified = true;
                this.a(j, playerchunk); // Paper - Move up - don't leak chunks
                // Spigot start
                if (!booleansupplier.getAsBoolean() && this.unloadQueue.size() <= targetSize && activityAccountant.activityTimeIsExhausted()) {
                    break;
                }
                // Spigot end
                //this.a(j, playerchunk); // Paper - move up because spigot did a dumb
            }
        }
        activityAccountant.endActivity(); // Spigot

        Runnable runnable;

        int queueTarget = Math.min(this.getUnloadQueueTasks().size() - 100, (int) (this.getUnloadQueueTasks().size() * UNLOAD_QUEUE_RESIZE_FACTOR)); // Paper - Target this queue as well
        while ((booleansupplier.getAsBoolean() || this.getUnloadQueueTasks().size() > queueTarget) && (runnable = (Runnable)this.getUnloadQueueTasks().poll()) != null) { // Paper - Target this queue as well
            runnable.run();
        }

    }

    // Paper start - async chunk save for unload
    // Note: This is very unsafe to call if the chunk is still in use.
    // This is also modeled after PlayerChunkMap#saveChunk(IChunkAccess, boolean), with the intentional difference being
    // serializing the chunk is left to a worker thread.
    private void asyncSave(IChunkAccess chunk) {
        ChunkCoordIntPair chunkPos = chunk.getPos();
        NBTTagCompound poiData;
        try (Timing ignored = this.world.timings.chunkUnloadPOISerialization.startTiming()) {
            poiData = this.getVillagePlace().getData(chunk.getPos());
        }

        com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE.scheduleSave(this.world, chunkPos.x, chunkPos.z,
            poiData, null, com.destroystokyo.paper.io.PrioritizedTaskQueue.LOW_PRIORITY);

        if (!chunk.isNeedsSaving()) {
            return;
        }

        ChunkStatus chunkstatus = chunk.getChunkStatus();

        // Copied from PlayerChunkMap#saveChunk(IChunkAccess, boolean)
        if (chunkstatus.getType() != ChunkStatus.Type.LEVELCHUNK) {
            try (co.aikar.timings.Timing ignored1 = this.world.timings.chunkSaveOverwriteCheck.startTiming()) { // Paper
                // Paper start - Optimize save by using status cache
                try {
                    ChunkStatus statusOnDisk = this.getChunkStatusOnDisk(chunkPos);
                    if (statusOnDisk != null && statusOnDisk.getType() == ChunkStatus.Type.LEVELCHUNK) {
                        // Paper end
                        return;
                    }

                    if (chunkstatus == ChunkStatus.EMPTY && chunk.h().values().stream().noneMatch(StructureStart::e)) {
                        return;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }

        ChunkRegionLoader.AsyncSaveData asyncSaveData;
        try (Timing ignored = this.world.timings.chunkUnloadPrepareSave.startTiming()) {
            asyncSaveData = ChunkRegionLoader.getAsyncSaveData(this.world, chunk);
        }

        this.world.asyncChunkTaskManager.scheduleChunkSave(chunkPos.x, chunkPos.z, com.destroystokyo.paper.io.PrioritizedTaskQueue.LOW_PRIORITY,
            asyncSaveData, chunk);

        chunk.setLastSaved(this.world.getTime());
        chunk.setNeedsSaving(false);
    }
    // Paper end

    private void a(long i, PlayerChunk playerchunk) {
        CompletableFuture<IChunkAccess> completablefuture = playerchunk.getChunkSave();
        Consumer<IChunkAccess> consumer = (ichunkaccess) -> { // CraftBukkit - decompile error
            CompletableFuture<IChunkAccess> completablefuture1 = playerchunk.getChunkSave();

            if (completablefuture1 != completablefuture) {
                this.a(i, playerchunk);
            } else {
                if (this.pendingUnload.remove(i, playerchunk) && ichunkaccess != null) {
                    if (ichunkaccess instanceof Chunk) {
                        ((Chunk) ichunkaccess).setLoaded(false);
                    }

                    //this.saveChunk(ichunkaccess);// Paper - delay
                    if (this.loadedChunks.remove(i) && ichunkaccess instanceof Chunk) {
                        Chunk chunk = (Chunk) ichunkaccess;

                        this.world.unloadChunk(chunk);
                    }
                    this.autoSaveQueue.remove(playerchunk); // Paper

                    try {
                        this.asyncSave(ichunkaccess); // Paper - async chunk saving
                    } catch (Throwable ex) {
                        LOGGER.fatal("Failed to prepare async save, attempting synchronous save", ex);
                        this.saveChunk(ichunkaccess);
                    }

                    this.lightEngine.a(ichunkaccess.getPos());
                    this.lightEngine.queueUpdate();
                    this.worldLoadListener.a(ichunkaccess.getPos(), (ChunkStatus) null);
                }

            }
        };
        Queue queue = this.A;

        this.A.getClass();
        completablefuture.thenAcceptAsync(consumer, queue::add).whenComplete((ovoid, throwable) -> {
            if (throwable != null) {
                PlayerChunkMap.LOGGER.error("Failed to save chunk " + playerchunk.i(), throwable);
            }

        });
    }

    protected boolean b() {
        if (!this.updatingChunksModified) {
            return false;
        } else {
            // Paper start - stop cloning visibleChunks
            synchronized (this.visibleChunks) {
                if (isIterating) {
                    hasPendingVisibleUpdate = true;
                    this.pendingVisibleChunks.copyFrom((com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<PlayerChunk>)this.updatingChunks);
                } else {
                    hasPendingVisibleUpdate = false;
                    this.pendingVisibleChunks.clear();
                    ((ProtectedVisibleChunksMap)this.visibleChunks).copyFrom((com.destroystokyo.paper.util.map.Long2ObjectLinkedOpenHashMapFastCopy<PlayerChunk>)this.updatingChunks);
                    this.visibleChunksClone = null;
                }
            }
            // Paper end

            this.updatingChunksModified = false;
            return true;
        }
    }

    public CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> a(PlayerChunk playerchunk, ChunkStatus chunkstatus) {
        ChunkCoordIntPair chunkcoordintpair = playerchunk.i();

        if (chunkstatus == ChunkStatus.EMPTY) {
            return this.f(chunkcoordintpair);
        } else {
            CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> completablefuture = playerchunk.a(chunkstatus.e(), this);

            return completablefuture.thenComposeAsync((either) -> {
                Optional<IChunkAccess> optional = either.left();

                if (!optional.isPresent()) {
                    return CompletableFuture.completedFuture(either);
                } else {
                    if (chunkstatus == ChunkStatus.LIGHT) {
                        this.chunkDistanceManager.a(TicketType.LIGHT, chunkcoordintpair, 33 + ChunkStatus.a(ChunkStatus.FEATURES), chunkcoordintpair);
                    }

                    IChunkAccess ichunkaccess = (IChunkAccess) optional.get();

                    if (ichunkaccess.getChunkStatus().b(chunkstatus)) {
                        CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> completablefuture1; // Paper

                        if (chunkstatus == ChunkStatus.LIGHT) {
                            completablefuture1 = this.b(playerchunk, chunkstatus);
                        } else {
                            completablefuture1 = chunkstatus.a(this.world, this.definedStructureManager, this.lightEngine, (ichunkaccess1) -> {
                                return this.c(playerchunk);
                            }, ichunkaccess);
                        }

                        this.worldLoadListener.a(chunkcoordintpair, chunkstatus);
                        return completablefuture1;
                    } else {
                        return this.b(playerchunk, chunkstatus);
                    }
                }
            }, this.mainInvokingExecutor).thenComposeAsync(CompletableFuture::completedFuture, this.mainInvokingExecutor); // Paper - optimize chunk status progression without jumping through thread pool - ensure main
        }
    }

    private CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> f(ChunkCoordIntPair chunkcoordintpair) {
        // Paper start - Async chunk io
        final java.util.function.BiFunction<ChunkRegionLoader.InProgressChunkHolder, Throwable, Either<IChunkAccess, PlayerChunk.Failure>> syncLoadComplete = (chunkHolder, ioThrowable) -> {
            try (Timing ignored = this.world.timings.chunkLoad.startTimingIfSync()) { // Paper
                this.world.getMethodProfiler().c("chunkLoad");
                // Paper start
                if (ioThrowable != null) {
                    com.destroystokyo.paper.util.SneakyThrow.sneaky(ioThrowable);
                }

                this.getVillagePlace().loadInData(chunkcoordintpair, chunkHolder.poiData);
                chunkHolder.tasks.forEach(Runnable::run);
                // Paper end

                if (chunkHolder.protoChunk != null) {try (Timing ignored2 = this.world.timings.chunkLoadLevelTimer.startTimingIfSync()) { // Paper start - timings // Paper - chunk is created async

                    if (true) {
                        ProtoChunk protochunk = chunkHolder.protoChunk;

                        protochunk.setLastSaved(this.world.getTime());
                        this.a(chunkcoordintpair, protochunk.getChunkStatus().getType());
                        return Either.left(protochunk);
                    }

                    PlayerChunkMap.LOGGER.error("Chunk file at {} is missing level data, skipping", chunkcoordintpair);
                }} // Paper
            } catch (ReportedException reportedexception) {
                Throwable throwable = reportedexception.getCause();

                if (!(throwable instanceof IOException)) {
                    this.g(chunkcoordintpair);
                    throw reportedexception;
                }

                PlayerChunkMap.LOGGER.error("Couldn't load chunk {}", chunkcoordintpair, throwable);
            } catch (Exception exception) {
                PlayerChunkMap.LOGGER.error("Couldn't load chunk {}", chunkcoordintpair, exception);
            }

            this.g(chunkcoordintpair);
            return Either.left(new ProtoChunk(chunkcoordintpair, ChunkConverter.a, this.world)); // Paper - Anti-Xray - Add parameter
            // Paper start - Async chunk io
        };
        CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> ret = new CompletableFuture<>();

        Consumer<ChunkRegionLoader.InProgressChunkHolder> chunkHolderConsumer = (ChunkRegionLoader.InProgressChunkHolder holder) -> {
            // Go into the chunk load queue and not server task queue so we can be popped out even faster.
            com.destroystokyo.paper.io.chunk.ChunkTaskManager.queueChunkWaitTask(() -> {
                try {
                    ret.complete(syncLoadComplete.apply(holder, null));
                } catch (Exception e) {
                    ret.completeExceptionally(e);
                }
            });
        };

        CompletableFuture<NBTTagCompound> chunkSaveFuture = this.world.asyncChunkTaskManager.getChunkSaveFuture(chunkcoordintpair.x, chunkcoordintpair.z);
        PlayerChunk playerChunk = getUpdatingChunk(chunkcoordintpair.pair());
        int chunkPriority = playerChunk != null ? playerChunk.getCurrentPriority() : 33;
        int priority = com.destroystokyo.paper.io.PrioritizedTaskQueue.NORMAL_PRIORITY;

        if (chunkPriority <= 10) {
            priority = com.destroystokyo.paper.io.PrioritizedTaskQueue.HIGHEST_PRIORITY;
        } else if (chunkPriority <= 20) {
            priority = com.destroystokyo.paper.io.PrioritizedTaskQueue.HIGH_PRIORITY;
        }
        boolean isHighestPriority = priority == com.destroystokyo.paper.io.PrioritizedTaskQueue.HIGHEST_PRIORITY;
        if (chunkSaveFuture != null) {
            this.world.asyncChunkTaskManager.scheduleChunkLoad(chunkcoordintpair.x, chunkcoordintpair.z, priority, chunkHolderConsumer, isHighestPriority, chunkSaveFuture);
        } else {
            this.world.asyncChunkTaskManager.scheduleChunkLoad(chunkcoordintpair.x, chunkcoordintpair.z, priority, chunkHolderConsumer, isHighestPriority);
        }
        this.world.asyncChunkTaskManager.raisePriority(chunkcoordintpair.x, chunkcoordintpair.z, priority);
        return ret;
        // Paper end
    }

    private void g(ChunkCoordIntPair chunkcoordintpair) {
        this.z.put(chunkcoordintpair.pair(), (byte) -1);
    }

    private byte a(ChunkCoordIntPair chunkcoordintpair, ChunkStatus.Type chunkstatus_type) {
        return this.z.put(chunkcoordintpair.pair(), (byte) (chunkstatus_type == ChunkStatus.Type.PROTOCHUNK ? -1 : 1));
    }

    private CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> b(PlayerChunk playerchunk, ChunkStatus chunkstatus) {
        ChunkCoordIntPair chunkcoordintpair = playerchunk.i();
        CompletableFuture<Either<List<IChunkAccess>, PlayerChunk.Failure>> completablefuture = this.a(chunkcoordintpair, chunkstatus.f(), (i) -> {
            return this.a(chunkstatus, i);
        });

        this.world.getMethodProfiler().c(() -> {
            return "chunkGenerate " + chunkstatus.d();
        });
        return completablefuture.thenComposeAsync((either) -> {
            return either.map((list) -> { // Paper - Shut up.
                try {
                    CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> completablefuture1 = chunkstatus.a(this.world, this.chunkGenerator, this.definedStructureManager, this.lightEngine, (ichunkaccess) -> {
                        return this.c(playerchunk);
                    }, list);

                    this.worldLoadListener.a(chunkcoordintpair, chunkstatus);
                    return completablefuture1;
                } catch (Exception exception) {
                    CrashReport crashreport = CrashReport.a(exception, "Exception generating new chunk");
                    CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");

                    crashreportsystemdetails.a("Location", (Object) String.format("%d,%d", chunkcoordintpair.x, chunkcoordintpair.z));
                    crashreportsystemdetails.a("Position hash", (Object) ChunkCoordIntPair.pair(chunkcoordintpair.x, chunkcoordintpair.z));
                    crashreportsystemdetails.a("Generator", (Object) this.chunkGenerator);
                    throw new ReportedException(crashreport);
                }
            }, (playerchunk_failure) -> {
                this.c(chunkcoordintpair);
                return CompletableFuture.completedFuture(Either.right(playerchunk_failure));
            });
        }, (runnable) -> {
            // Paper start - optimize chunk status progression without jumping through thread pool
            if (playerchunk.canAdvanceStatus()) {
                this.mainInvokingExecutor.execute(runnable);
                return;
            }
            // Paper end
            this.mailboxWorldGen.a(ChunkTaskQueueSorter.a(playerchunk, runnable));
        });
    }

    protected void c(ChunkCoordIntPair chunkcoordintpair) {
        this.executor.a(SystemUtils.a(() -> {
            this.chunkDistanceManager.b(TicketType.LIGHT, chunkcoordintpair, 33 + ChunkStatus.a(ChunkStatus.FEATURES), chunkcoordintpair);
        }, () -> {
            return "release light ticket " + chunkcoordintpair;
        }));
    }

    private ChunkStatus a(ChunkStatus chunkstatus, int i) {
        ChunkStatus chunkstatus1;

        if (i == 0) {
            chunkstatus1 = chunkstatus.e();
        } else {
            chunkstatus1 = ChunkStatus.a(ChunkStatus.a(chunkstatus) + i);
        }

        return chunkstatus1;
    }

    private CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> c(PlayerChunk playerchunk) {
        CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> completablefuture = playerchunk.getStatusFutureUnchecked(ChunkStatus.FULL.e());

        return completablefuture.thenApplyAsync((either) -> {
            ChunkStatus chunkstatus = PlayerChunk.getChunkStatus(playerchunk.getTicketLevel());

            return !chunkstatus.b(ChunkStatus.FULL) ? PlayerChunk.UNLOADED_CHUNK_ACCESS : either.mapLeft((ichunkaccess) -> {
            try (Timing ignored = world.timings.chunkPostLoad.startTimingIfSync()) { // Paper
                ChunkCoordIntPair chunkcoordintpair = playerchunk.i();
                Chunk chunk;

                if (ichunkaccess instanceof ProtoChunkExtension) {
                    chunk = ((ProtoChunkExtension) ichunkaccess).u();
                } else {
                    chunk = new Chunk(this.world, (ProtoChunk) ichunkaccess);
                    playerchunk.a(new ProtoChunkExtension(chunk));
                }

                chunk.setLastSaved(this.world.getTime() - 1); // Paper - avoid autosaving newly generated/loaded chunks

                chunk.a(() -> {
                    return PlayerChunk.getChunkState(playerchunk.getTicketLevel());
                });
                chunk.addEntities();
                if (this.loadedChunks.add(chunkcoordintpair.pair())) {
                    chunk.setLoaded(true);
                    this.world.a(chunk.getTileEntities().values());
                    List<Entity> list = null;
                    List<Entity>[] aentityslice = chunk.getEntitySlices(); // Spigot
                    int i = aentityslice.length;

                    for (int j = 0; j < i; ++j) {
                        List<Entity> entityslice = aentityslice[j]; // Spigot
                        Iterator iterator = entityslice.iterator();

                        while (iterator.hasNext()) {
                            Entity entity = (Entity) iterator.next();
                            // CraftBukkit start - these are spawned serialized (DefinedStructure) and we don't call an add event below at the moment due to ordering complexities
                            boolean needsRemoval = false;
                            if (chunk.needsDecoration && !this.world.getServer().getServer().getSpawnNPCs() && entity instanceof NPC) {
                                entity.dead = true; // Paper
                                needsRemoval = true;
                            }
                            // CraftBukkit end
                            checkDupeUUID(entity); // Paper
                            if (!(entity instanceof EntityHuman) && (entity.dead || !this.world.addEntityChunk(entity))) { // Paper
                                if (list == null) {
                                    list = Lists.newArrayList(new Entity[]{entity});
                                } else {
                                    list.add(entity);
                                }
                            }
                        }
                    }

                    if (list != null) {
                        list.forEach(chunk::b);
                    }
                }

                return chunk;
                } // Paper
            });
        }, (runnable) -> {
            Mailbox mailbox = this.mailboxMain;
            long i = playerchunk.i().pair();

            playerchunk.getClass();
            mailbox.a(ChunkTaskQueueSorter.a(runnable, i, () -> 1)); // Paper - final loads are always urgent!
        });
    }

    // Paper start
    private void checkDupeUUID(Entity entity) {
        PaperWorldConfig.DuplicateUUIDMode mode = world.paperConfig.duplicateUUIDMode;
        if (mode != PaperWorldConfig.DuplicateUUIDMode.WARN
            && mode != PaperWorldConfig.DuplicateUUIDMode.DELETE
            && mode != PaperWorldConfig.DuplicateUUIDMode.SAFE_REGEN) {
            return;
        }
        Entity other = world.getEntity(entity.uniqueID);

        if (mode == PaperWorldConfig.DuplicateUUIDMode.SAFE_REGEN && other != null && !other.dead
                && Objects.equals(other.getSaveID(), entity.getSaveID())
                && entity.getBukkitEntity().getLocation().distance(other.getBukkitEntity().getLocation()) < world.paperConfig.duplicateUUIDDeleteRange
        ) {
            if (World.DEBUG_ENTITIES) LOGGER.warn("[DUPE-UUID] Duplicate UUID found used by " + other + ", deleted entity " + entity + " because it was near the duplicate and likely an actual duplicate. See https://github.com/PaperMC/Paper/issues/1223 for discussion on what this is about.");
            entity.dead = true;
            return;
        }
        if (other != null && !other.dead) {
            switch (mode) {
                case SAFE_REGEN: {
                    entity.setUUID(UUID.randomUUID());
                    if (World.DEBUG_ENTITIES) LOGGER.warn("[DUPE-UUID] Duplicate UUID found used by " + other + ", regenerated UUID for " + entity + ". See https://github.com/PaperMC/Paper/issues/1223 for discussion on what this is about.");
                    break;
                }
                case DELETE: {
                    if (World.DEBUG_ENTITIES) LOGGER.warn("[DUPE-UUID] Duplicate UUID found used by " + other + ", deleted entity " + entity + ". See https://github.com/PaperMC/Paper/issues/1223 for discussion on what this is about.");
                    entity.dead = true;
                    break;
                }
                default:
                    if (World.DEBUG_ENTITIES) LOGGER.warn("[DUPE-UUID] Duplicate UUID found used by " + other + ", doing nothing to " + entity + ". See https://github.com/PaperMC/Paper/issues/1223 for discussion on what this is about.");
                    break;
            }
        }
    }
    // Paper end

    public CompletableFuture<Either<Chunk, PlayerChunk.Failure>> a(PlayerChunk playerchunk) {
        ChunkCoordIntPair chunkcoordintpair = playerchunk.i();
        CompletableFuture<Either<List<IChunkAccess>, PlayerChunk.Failure>> completablefuture = this.a(chunkcoordintpair, 1, (i) -> {
            return ChunkStatus.FULL;
        });
        CompletableFuture<Either<Chunk, PlayerChunk.Failure>> completablefuture1 = completablefuture.thenApplyAsync((either) -> {
            return either.flatMap((list) -> {
                Chunk chunk = (Chunk) list.get(list.size() / 2);

                chunk.A();
                return Either.left(chunk);
            });
        }, (runnable) -> {
            this.mailboxMain.a(ChunkTaskQueueSorter.a(playerchunk, () -> PlayerChunkMap.this.chunkLoadConversionCallbackExecutor.execute(runnable))); // Paper - delay running Chunk post processing until outside of the sorter to prevent a deadlock scenario when post processing causes another chunk request.
        });

        completablefuture1.thenAcceptAsync((either) -> {
            either.mapLeft((chunk) -> {
                this.u.getAndIncrement();
                // Paper - no-tick view distance - moved to Chunk neighbour update
                return Either.left(chunk);
            });
        }, (runnable) -> {
            this.mailboxMain.a(ChunkTaskQueueSorter.a(playerchunk, runnable)); // Paper - diff on change, this is the scheduling method copied in Chunk used to schedule chunk broadcasts (on change it needs to be copied again)
        });
        return completablefuture1;
    }

    public CompletableFuture<Either<Chunk, PlayerChunk.Failure>> b(PlayerChunk playerchunk) {
        return playerchunk.a(ChunkStatus.FULL, this).thenApplyAsync((either) -> {
            return either.mapLeft((ichunkaccess) -> {
                Chunk chunk = (Chunk) ichunkaccess;

                chunk.B();
                return chunk;
            });
        }, (runnable) -> {
            this.mailboxMain.a(ChunkTaskQueueSorter.a(playerchunk, runnable));
        });
    }

    public int c() {
        return this.u.get();
    }

    public boolean saveChunk(IChunkAccess ichunkaccess) {
        try (co.aikar.timings.Timing ignored = this.world.timings.chunkSave.startTiming()) { // Paper
        this.m.a(ichunkaccess.getPos());
        if (!ichunkaccess.isNeedsSaving()) {
            return false;
        } else {
            ichunkaccess.setLastSaved(this.world.getTime());
            ichunkaccess.setNeedsSaving(false);
            ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();

            try {
                ChunkStatus chunkstatus = ichunkaccess.getChunkStatus();

                if (chunkstatus.getType() != ChunkStatus.Type.LEVELCHUNK) {
                    try (co.aikar.timings.Timing ignored1 = this.world.timings.chunkSaveOverwriteCheck.startTiming()) { // Paper
                    if (this.h(chunkcoordintpair)) {
                        return false;
                    }

                    if (chunkstatus == ChunkStatus.EMPTY && ichunkaccess.h().values().stream().noneMatch(StructureStart::e)) {
                        return false;
                    }
                    } // Paper
                }

                this.world.getMethodProfiler().c("chunkSave");
                NBTTagCompound nbttagcompound;
                try (co.aikar.timings.Timing ignored1 = this.world.timings.chunkSaveDataSerialization.startTiming()) { // Paper
                    nbttagcompound = ChunkRegionLoader.saveChunk(this.world, ichunkaccess);
                } // Paper


                // Paper start - async chunk io
                com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE.scheduleSave(this.world, chunkcoordintpair.x, chunkcoordintpair.z,
                    null, nbttagcompound, com.destroystokyo.paper.io.PrioritizedTaskQueue.NORMAL_PRIORITY);
                // Paper end - async chunk io
                this.a(chunkcoordintpair, chunkstatus.getType());
                return true;
            } catch (Exception exception) {
                PlayerChunkMap.LOGGER.error("Failed to save chunk {},{}", chunkcoordintpair.x, chunkcoordintpair.z, exception);
                com.destroystokyo.paper.exception.ServerInternalException.reportInternalException(exception); // Paper
                return false;
            }
        }
        } // Paper
    }

    private boolean h(ChunkCoordIntPair chunkcoordintpair) {
        byte b0 = this.z.get(chunkcoordintpair.pair());

        if (b0 != 0) {
            return b0 == 1;
        } else {
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = this.readChunkData(chunkcoordintpair);
                if (nbttagcompound == null) {
                    this.g(chunkcoordintpair);
                    return false;
                }
            } catch (Exception exception) {
                PlayerChunkMap.LOGGER.error("Failed to read chunk {}", chunkcoordintpair, exception);
                this.g(chunkcoordintpair);
                return false;
            }

            ChunkStatus.Type chunkstatus_type = ChunkRegionLoader.a(nbttagcompound);

            return this.a(chunkcoordintpair, chunkstatus_type) == 1;
        }
    }

    public void setViewDistance(int i) { // Paper - public
        int j = MathHelper.clamp(i + 1, 3, 33); // Paper - diff on change, these make the lower view distance limit 2 and the upper 32

        if (j != this.viewDistance) {
            int k = this.viewDistance;

            this.viewDistance = j;
            this.setNoTickViewDistance(this.getRawNoTickViewDistance()); //Paper - no-tick view distance - propagate changes to no-tick, which does the actual chunk loading/sending
        }

    }

    // Paper start - no-tick view distance
    public final void setNoTickViewDistance(int viewDistance) {
        viewDistance = viewDistance == -1 ? -1 : MathHelper.clamp(viewDistance, 2, 32);

        this.noTickViewDistance = viewDistance;
        int loadViewDistance = this.getLoadViewDistance();
        this.chunkDistanceManager.setNoTickViewDistance(loadViewDistance + 2 + 2); // add 2 to account for the change to 31 -> 33 tickets // see notes in the distance map updating for the other + 2

        if (this.world != null && this.world.players != null) { // this can be called from constructor, where these aren't set
            for (EntityPlayer player : this.world.players) {
                PlayerConnection connection = player.playerConnection;
                if (connection != null) {
                    // moved in from PlayerList
                    connection.sendPacket(new PacketPlayOutViewDistance(loadViewDistance));
                }
                this.updateMaps(player);
            }
        }
    }
    // Paper end - no-tick view distance

    protected void sendChunk(EntityPlayer entityplayer, ChunkCoordIntPair chunkcoordintpair, Packet<?>[] apacket, boolean flag, boolean flag1) {
        if (entityplayer.world == this.world) {
            if (flag1 && !flag) {
                PlayerChunk playerchunk = this.getVisibleChunk(chunkcoordintpair.pair());

                if (playerchunk != null) {
                    Chunk chunk = playerchunk.getSendingChunk(); // Paper - no-tick view distance

                    if (chunk != null) {
                        this.a(entityplayer, apacket, chunk);
                    }

                    PacketDebug.a(this.world, chunkcoordintpair);
                }
            }

            if (!flag1 && flag) {
                entityplayer.a(chunkcoordintpair);
            }

        }
    }

    public int d() {
        return this.visibleChunks.size();
    }

    protected PlayerChunkMap.a e() {
        return this.chunkDistanceManager;
    }

    protected Iterable<PlayerChunk> f() {
        return Iterables.unmodifiableIterable(this.getVisibleChunks().values()); // Paper
    }

    void a(Writer writer) throws IOException {
        CSVWriter csvwriter = CSVWriter.a().a("x").a("z").a("level").a("in_memory").a("status").a("full_status").a("accessible_ready").a("ticking_ready").a("entity_ticking_ready").a("ticket").a("spawning").a("entity_count").a("block_entity_count").a(writer);
        ObjectBidirectionalIterator objectbidirectionaliterator = this.getVisibleChunks().long2ObjectEntrySet().iterator(); // Paper

        while (objectbidirectionaliterator.hasNext()) {
            Entry<PlayerChunk> entry = (Entry) objectbidirectionaliterator.next();
            ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(entry.getLongKey());
            PlayerChunk playerchunk = (PlayerChunk) entry.getValue();
            Optional<IChunkAccess> optional = Optional.ofNullable(playerchunk.f());
            Optional<Chunk> optional1 = optional.flatMap((ichunkaccess) -> {
                return ichunkaccess instanceof Chunk ? Optional.of((Chunk) ichunkaccess) : Optional.empty();
            });

            // CraftBukkit - decompile error
            csvwriter.a(chunkcoordintpair.x, chunkcoordintpair.z, playerchunk.getTicketLevel(), optional.isPresent(), optional.map(IChunkAccess::getChunkStatus).orElse(null), optional1.map(Chunk::getState).orElse(null), a(playerchunk.c()), a(playerchunk.a()), a(playerchunk.b()), this.chunkDistanceManager.c(entry.getLongKey()), !this.isOutsideOfRange(chunkcoordintpair), optional1.map((chunk) -> {
                return Stream.of(chunk.getEntitySlices()).mapToInt(List::size).sum(); // Spigot
            }).orElse(0), optional1.map((chunk) -> {
                return chunk.getTileEntities().size();
            }).orElse(0));
        }

    }

    private static String a(CompletableFuture<Either<Chunk, PlayerChunk.Failure>> completablefuture) {
        try {
            Either<Chunk, PlayerChunk.Failure> either = (Either) completablefuture.getNow(null); // CraftBukkit - decompile error

            return either != null ? (String) either.map((chunk) -> {
                return "done";
            }, (playerchunk_failure) -> {
                return "unloaded";
            }) : "not completed";
        } catch (CompletionException completionexception) {
            return "failed " + completionexception.getCause().getMessage();
        } catch (CancellationException cancellationexception) {
            return "cancelled";
        }
    }

    // Paper start - Asynchronous chunk io
    @Nullable
    @Override
    public NBTTagCompound read(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        if (Thread.currentThread() != com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE) {
            NBTTagCompound ret = com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE
                .loadChunkDataAsyncFuture(this.world, chunkcoordintpair.x, chunkcoordintpair.z, com.destroystokyo.paper.io.IOUtil.getPriorityForCurrentThread(),
                    false, true, true).join().chunkData;

            if (ret == com.destroystokyo.paper.io.PaperFileIOThread.FAILURE_VALUE) {
                throw new IOException("See logs for further detail");
            }
            return ret;
        }
        return super.read(chunkcoordintpair);
    }

    @Override
    public void write(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) throws IOException {
        if (Thread.currentThread() != com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE) {
            com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE.scheduleSave(
                this.world, chunkcoordintpair.x, chunkcoordintpair.z, null, nbttagcompound,
                com.destroystokyo.paper.io.IOUtil.getPriorityForCurrentThread());
            return;
        }
        super.write(chunkcoordintpair, nbttagcompound);
    }
    // Paper end

    @Nullable
    public NBTTagCompound readChunkData(ChunkCoordIntPair chunkcoordintpair) throws IOException { // Paper - private -> public
        NBTTagCompound nbttagcompound = this.read(chunkcoordintpair);
        // Paper start - Cache chunk status on disk
        if (nbttagcompound == null) {
            return null;
        }

        nbttagcompound = this.getChunkData(this.world.getTypeKey(), this.l, nbttagcompound, chunkcoordintpair, world); // CraftBukkit
        if (nbttagcompound == null) {
            return null;
        }

        this.updateChunkStatusOnDisk(chunkcoordintpair, nbttagcompound);

        return nbttagcompound;
        // Paper end
    }

    // Paper start - chunk status cache "api"
    public ChunkStatus getChunkStatusOnDiskIfCached(ChunkCoordIntPair chunkPos) {
        synchronized (this) { // Paper
        RegionFile regionFile = this.regionFileCache.getRegionFileIfLoaded(chunkPos);

        return regionFile == null ? null : regionFile.getStatusIfCached(chunkPos.x, chunkPos.z);
        } // Paper
    }

    public ChunkStatus getChunkStatusOnDisk(ChunkCoordIntPair chunkPos) throws IOException {
        // Paper start - async chunk save for unload
        IChunkAccess unloadingChunk = this.world.asyncChunkTaskManager.getChunkInSaveProgress(chunkPos.x, chunkPos.z);
        if (unloadingChunk != null) {
            return unloadingChunk.getChunkStatus();
        }
        // Paper end
        // Paper start - async io
        NBTTagCompound inProgressWrite = com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE
                                             .getPendingWrite(this.world, chunkPos.x, chunkPos.z, false);

        if (inProgressWrite != null) {
            return ChunkRegionLoader.getStatus(inProgressWrite);
        }
        // Paper end
        synchronized (this) { // Paper - async io
            RegionFile regionFile = this.regionFileCache.getFile(chunkPos, true);

            if (regionFile == null || !regionFile.chunkExists(chunkPos)) {
                return null;
            }

            ChunkStatus status = regionFile.getStatusIfCached(chunkPos.x, chunkPos.z);

            if (status != null) {
                return status;
            }
            // Paper start - async io
        }

        NBTTagCompound compound = this.readChunkData(chunkPos);

        return ChunkRegionLoader.getStatus(compound);
        // Paper end
    }

    public void updateChunkStatusOnDisk(ChunkCoordIntPair chunkPos, @Nullable NBTTagCompound compound) throws IOException {
        synchronized (this) {
            RegionFile regionFile = this.regionFileCache.getFile(chunkPos, false);

            regionFile.setStatus(chunkPos.x, chunkPos.z, ChunkRegionLoader.getStatus(compound));
        }
    }

    public IChunkAccess getUnloadingChunk(int chunkX, int chunkZ) {
        PlayerChunk chunkHolder = this.pendingUnload.get(ChunkCoordIntPair.pair(chunkX, chunkZ));
        return chunkHolder == null ? null : chunkHolder.getAvailableChunkNow();
    }
    // Paper end


    // Paper start - async io
    // this function will not load chunk data off disk to check for status
    // ret null for unknown, empty for empty status on disk or absent from disk
    public ChunkStatus getStatusOnDiskNoLoad(int x, int z) {
        // Paper start - async chunk save for unload
        IChunkAccess unloadingChunk = this.world.asyncChunkTaskManager.getChunkInSaveProgress(x, z);
        if (unloadingChunk != null) {
            return unloadingChunk.getChunkStatus();
        }
        // Paper end
        // Paper start - async io
        net.minecraft.server.NBTTagCompound inProgressWrite = com.destroystokyo.paper.io.PaperFileIOThread.Holder.INSTANCE
            .getPendingWrite(this.world, x, z, false);

        if (inProgressWrite != null) {
            return net.minecraft.server.ChunkRegionLoader.getStatus(inProgressWrite);
        }
        // Paper end
        // variant of PlayerChunkMap#getChunkStatusOnDisk that does not load data off disk, but loads the region file
        ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(x, z);
        synchronized (world.getChunkProvider().playerChunkMap) {
            net.minecraft.server.RegionFile file;
            try {
                file = world.getChunkProvider().playerChunkMap.regionFileCache.getFile(chunkPos, false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            return !file.chunkExists(chunkPos) ? ChunkStatus.EMPTY : file.getStatusIfCached(x, z);
        }
    }

    boolean isOutsideOfRange(ChunkCoordIntPair chunkcoordintpair) {
        // Spigot start
        return isOutsideOfRange(chunkcoordintpair, false);
    }

    // Paper start - optimise isOutsideOfRange
    final boolean isOutsideOfRange(ChunkCoordIntPair chunkcoordintpair, boolean reducedRange) {
        return this.isOutsideOfRange(this.getUpdatingChunk(chunkcoordintpair.pair()), chunkcoordintpair, reducedRange);
    }

    final boolean isOutsideOfRange(PlayerChunk playerchunk, ChunkCoordIntPair chunkcoordintpair, boolean reducedRange) {
        // this function is so hot that removing the map lookup call can have an order of magnitude impact on its performance
        // tested and confirmed via System.nanoTime()
        com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> playersInRange = reducedRange ? playerchunk.playersInMobSpawnRange : playerchunk.playersInChunkTickRange;

        if (playersInRange == null) {
            return true;
        }

        Object[] backingSet = playersInRange.getBackingSet();

        if (reducedRange) {
            for (int i = 0, len = backingSet.length; i < len; ++i) {
                Object raw = backingSet[i];
                if (!(raw instanceof EntityPlayer)) {
                    continue;
                }
                EntityPlayer player = (EntityPlayer) raw;
                // don't check spectator and whatnot, already handled by mob spawn map update
                if (player.lastEntitySpawnRadiusSquared > getDistanceSquaredFromChunk(chunkcoordintpair, player)) {
                    return false; // in range
                }
            }
        } else {
            final double range = (ChunkMapDistance.MOB_SPAWN_RANGE * 16) * (ChunkMapDistance.MOB_SPAWN_RANGE * 16);
            // before spigot, mob spawn range was actually mob spawn range + tick range, but it was split
            for (int i = 0, len = backingSet.length; i < len; ++i) {
                Object raw = backingSet[i];
                if (!(raw instanceof EntityPlayer)) {
                    continue;
                }
                EntityPlayer player = (EntityPlayer) raw;
                // don't check spectator and whatnot, already handled by mob spawn map update
                if (range > getDistanceSquaredFromChunk(chunkcoordintpair, player)) {
                    return false; // in range
                }
            }
        }
        // no players in range
        return true;
    }
    // Paper end - optimise isOutsideOfRange

    private boolean cannotLoadChunks(EntityPlayer entityplayer) { return this.b(entityplayer); } // Paper - OBFHELPER
    private boolean b(EntityPlayer entityplayer) {
        return entityplayer.isSpectator() && !this.world.getGameRules().getBoolean(GameRules.SPECTATORS_GENERATE_CHUNKS);
    }

    void a(EntityPlayer entityplayer, boolean flag) {
        boolean flag1 = this.b(entityplayer);
        boolean flag2 = this.playerMap.c(entityplayer);
        int i = MathHelper.floor(entityplayer.locX()) >> 4;
        int j = MathHelper.floor(entityplayer.locZ()) >> 4;

        if (flag) {
            this.playerMap.a(ChunkCoordIntPair.pair(i, j), entityplayer, flag1);
            this.c(entityplayer);
            if (!flag1) {
                this.chunkDistanceManager.a(SectionPosition.a((Entity) entityplayer), entityplayer);
            }
            this.addPlayerToDistanceMaps(entityplayer); // Paper - distance maps
        } else {
            SectionPosition sectionposition = entityplayer.O();

            this.playerMap.a(sectionposition.r().pair(), entityplayer);
            if (!flag2) {
                this.chunkDistanceManager.b(sectionposition, entityplayer);
            }
            this.removePlayerFromDistanceMaps(entityplayer); // Paper - distance maps
        }

        // Paper - broadcast view distance map handles this (see remove/add calls above)

    }

    private SectionPosition c(EntityPlayer entityplayer) {
        SectionPosition sectionposition = SectionPosition.a((Entity) entityplayer);

        entityplayer.a(sectionposition);
        // Paper - distance map handles this now
        return sectionposition;
    }

    public void movePlayer(EntityPlayer entityplayer) {
        // Paper - delay this logic for the entity tracker tick, no need to duplicate it

        int i = MathHelper.floor(entityplayer.locX()) >> 4;
        int j = MathHelper.floor(entityplayer.locZ()) >> 4;
        SectionPosition sectionposition = entityplayer.O();
        SectionPosition sectionposition1 = SectionPosition.a((Entity) entityplayer);
        long k = sectionposition.r().pair();
        long l = sectionposition1.r().pair();
        boolean flag = this.playerMap.d(entityplayer);
        boolean flag1 = this.b(entityplayer);
        boolean flag2 = sectionposition.s() != sectionposition1.s();

        if (flag2 || flag != flag1) {
            this.c(entityplayer);
            if (!flag) {
                this.chunkDistanceManager.b(sectionposition, entityplayer);
            }

            if (!flag1) {
                this.chunkDistanceManager.a(sectionposition1, entityplayer);
            }

            if (!flag && flag1) {
                this.playerMap.a(entityplayer);
            }

            if (flag && !flag1) {
                this.playerMap.b(entityplayer);
            }

            if (k != l) {
                this.playerMap.a(k, l, entityplayer);
            }
        }

        int i1 = sectionposition.a();
        int j1 = sectionposition.c();
        int k1;
        int l1;

        /* // Paper start - replaced by distance map
        if (Math.abs(i1 - i) <= this.viewDistance * 2 && Math.abs(j1 - j) <= this.viewDistance * 2) {
            k1 = Math.min(i, i1) - this.viewDistance;
            l1 = Math.min(j, j1) - this.viewDistance;
            int i2 = Math.max(i, i1) + this.viewDistance;
            int j2 = Math.max(j, j1) + this.viewDistance;

            for (int k2 = k1; k2 <= i2; ++k2) {
                for (int l2 = l1; l2 <= j2; ++l2) {
                    ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(k2, l2);
                    boolean flag3 = a(chunkcoordintpair, i1, j1) <= this.viewDistance;
                    boolean flag4 = a(chunkcoordintpair, i, j) <= this.viewDistance;

                    this.sendChunk(entityplayer, chunkcoordintpair, new Packet[2], flag3, flag4);
                }
            }
        } else {
            ChunkCoordIntPair chunkcoordintpair1;
            boolean flag5;
            boolean flag6;

            for (k1 = i1 - this.viewDistance; k1 <= i1 + this.viewDistance; ++k1) {
                for (l1 = j1 - this.viewDistance; l1 <= j1 + this.viewDistance; ++l1) {
                    chunkcoordintpair1 = new ChunkCoordIntPair(k1, l1);
                    flag5 = true;
                    flag6 = false;
                    this.sendChunk(entityplayer, chunkcoordintpair1, new Packet[2], true, false);
                }
            }

            for (k1 = i - this.viewDistance; k1 <= i + this.viewDistance; ++k1) {
                for (l1 = j - this.viewDistance; l1 <= j + this.viewDistance; ++l1) {
                    chunkcoordintpair1 = new ChunkCoordIntPair(k1, l1);
                    flag5 = false;
                    flag6 = true;
                    this.sendChunk(entityplayer, chunkcoordintpair1, new Packet[2], false, true);
                }
            }
        }*/ // Paper end - replaced by distance map

        this.updateMaps(entityplayer); // Paper - distance maps

    }

    @Override
    public Stream<EntityPlayer> a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        // Paper start - per player view distance
        // there can be potential desync with player's last mapped section and the view distance map, so use the
        // view distance map here.
        com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> inRange = this.playerViewDistanceBroadcastMap.getObjectsInRange(chunkcoordintpair);

        if (inRange == null) {
            return Stream.empty();
        }
        // all current cases are inlined so we wont hit this code, it's just in case plugins or future updates use it
        List<EntityPlayer> players = new java.util.ArrayList<>();
        Object[] backingSet = inRange.getBackingSet();

        if (flag) { // flag -> border only
            for (int i = 0, len = backingSet.length; i < len; ++i) {
                Object temp = backingSet[i];
                if (!(temp instanceof EntityPlayer)) {
                    continue;
                }
                EntityPlayer player = (EntityPlayer)temp;
                int viewDistance = this.playerViewDistanceBroadcastMap.getLastViewDistance(player);
                long lastPosition = this.playerViewDistanceBroadcastMap.getLastCoordinate(player);

                int distX = Math.abs(MCUtil.getCoordinateX(lastPosition) - chunkcoordintpair.x);
                int distZ = Math.abs(MCUtil.getCoordinateZ(lastPosition) - chunkcoordintpair.z);
                if (Math.max(distX, distZ) == viewDistance) {
                    players.add(player);
                }
            }
        } else {
            for (int i = 0, len = backingSet.length; i < len; ++i) {
                Object temp = backingSet[i];
                if (!(temp instanceof EntityPlayer)) {
                    continue;
                }
                EntityPlayer player = (EntityPlayer)temp;
                players.add(player);
            }
        }
        return players.stream();
        // Paper end - per player view distance
    }

    protected void addEntity(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp("entity track"); // Spigot
        // Paper start - ignore and warn about illegal addEntity calls instead of crashing server
        if (!entity.valid || entity.world != this.world || this.trackedEntities.containsKey(entity.getId())) {
            new Throwable("[ERROR] Illegal PlayerChunkMap::addEntity for world " + this.world.getWorld().getName()
                + ": " + entity  + (this.trackedEntities.containsKey(entity.getId()) ? " ALREADY CONTAINED (This would have crashed your server)" : ""))
                .printStackTrace();
            return;
        }
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).supressTrackerForLogin) return; // Delay adding to tracker until after list packets
        // Paper end
        if (!(entity instanceof EntityComplexPart)) {
            EntityTypes<?> entitytypes = entity.getEntityType();
            int i = entitytypes.getChunkRange() * 16;
            i = org.spigotmc.TrackingRange.getEntityTrackingRange(entity, i); // Spigot
            int j = entitytypes.getUpdateInterval();

            if (this.trackedEntities.containsKey(entity.getId())) {
                throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Entity is already tracked!")));
            } else {
                PlayerChunkMap.EntityTracker playerchunkmap_entitytracker = new PlayerChunkMap.EntityTracker(entity, i, j, entitytypes.isDeltaTracking());

                entity.tracker = playerchunkmap_entitytracker; // Paper - Fast access to tracker
                this.trackedEntities.put(entity.getId(), playerchunkmap_entitytracker);
                playerchunkmap_entitytracker.updatePlayers(entity.getPlayersInTrackRange()); // Paper - don't search all players
                if (entity instanceof EntityPlayer) {
                    EntityPlayer entityplayer = (EntityPlayer) entity;

                    this.a(entityplayer, true);
                    ObjectIterator objectiterator = this.trackedEntities.values().iterator();

                    while (objectiterator.hasNext()) {
                        PlayerChunkMap.EntityTracker playerchunkmap_entitytracker1 = (PlayerChunkMap.EntityTracker) objectiterator.next();

                        if (playerchunkmap_entitytracker1.tracker != entityplayer) {
                            playerchunkmap_entitytracker1.updatePlayer(entityplayer);
                        }
                    }
                }

            }
        }
    }

    protected void removeEntity(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp("entity untrack"); // Spigot
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entity;

            this.a(entityplayer, false);
            ObjectIterator objectiterator = this.trackedEntities.values().iterator();

            while (objectiterator.hasNext()) {
                PlayerChunkMap.EntityTracker playerchunkmap_entitytracker = (PlayerChunkMap.EntityTracker) objectiterator.next();

                playerchunkmap_entitytracker.clear(entityplayer);
            }
        }

        PlayerChunkMap.EntityTracker playerchunkmap_entitytracker1 = (PlayerChunkMap.EntityTracker) this.trackedEntities.remove(entity.getId());

        if (playerchunkmap_entitytracker1 != null) {
            playerchunkmap_entitytracker1.a();
        }
        entity.tracker = null; // Paper - We're no longer tracked
    }

    // Paper start - optimised tracker
    private final void processTrackQueue() {
        this.world.timings.tracker1.startTiming();
        try {
            for (EntityTracker tracker : this.trackedEntities.values()) {
                // update tracker entry
                tracker.updatePlayers(tracker.tracker.getPlayersInTrackRange());
            }
        } finally {
            this.world.timings.tracker1.stopTiming();
        }


        this.world.timings.tracker2.startTiming();
        try {
            for (EntityTracker tracker : this.trackedEntities.values()) {
                tracker.trackerEntry.tick();
            }
        } finally {
            this.world.timings.tracker2.stopTiming();
        }
    }
    // Paper end - optimised tracker

    protected void g() {
        // Paper start - optimized tracker
        if (true) {
            this.processTrackQueue();
            return;
        }
        // Paper end - optimized tracker
        List<EntityPlayer> list = Lists.newArrayList();
        List<EntityPlayer> list1 = this.world.getPlayers();

        PlayerChunkMap.EntityTracker playerchunkmap_entitytracker;
        ObjectIterator objectiterator;
        world.timings.tracker1.startTiming(); // Paper

        for (objectiterator = this.trackedEntities.values().iterator(); objectiterator.hasNext(); playerchunkmap_entitytracker.trackerEntry.a()) {
            playerchunkmap_entitytracker = (PlayerChunkMap.EntityTracker) objectiterator.next();
            SectionPosition sectionposition = playerchunkmap_entitytracker.e;
            SectionPosition sectionposition1 = SectionPosition.a(playerchunkmap_entitytracker.tracker);

            if (!Objects.equals(sectionposition, sectionposition1)) {
                playerchunkmap_entitytracker.track(list1);
                Entity entity = playerchunkmap_entitytracker.tracker;

                if (entity instanceof EntityPlayer) {
                    list.add((EntityPlayer) entity);
                }

                playerchunkmap_entitytracker.e = sectionposition1;
            }
        }
        world.timings.tracker1.stopTiming(); // Paper

        if (!list.isEmpty()) {
            objectiterator = this.trackedEntities.values().iterator();

            world.timings.tracker2.startTiming(); // Paper
            while (objectiterator.hasNext()) {
                playerchunkmap_entitytracker = (PlayerChunkMap.EntityTracker) objectiterator.next();
                playerchunkmap_entitytracker.track(list);
            }
            world.timings.tracker2.stopTiming(); // Paper
        }


    }

    protected void broadcast(Entity entity, Packet<?> packet) {
        PlayerChunkMap.EntityTracker playerchunkmap_entitytracker = (PlayerChunkMap.EntityTracker) this.trackedEntities.get(entity.getId());

        if (playerchunkmap_entitytracker != null) {
            playerchunkmap_entitytracker.broadcast(packet);
        }

    }

    protected void broadcastIncludingSelf(Entity entity, Packet<?> packet) {
        PlayerChunkMap.EntityTracker playerchunkmap_entitytracker = (PlayerChunkMap.EntityTracker) this.trackedEntities.get(entity.getId());

        if (playerchunkmap_entitytracker != null) {
            playerchunkmap_entitytracker.broadcastIncludingSelf(packet);
        }

    }

    // Paper start
    private static int getLightMask(final Chunk chunk) {
        final ChunkSection[] chunkSections = chunk.getSections();
        int mask = 0;

        for (int i = 0; i < chunkSections.length; ++i) {
            /*


Lightmasks have 18 bits, from the -1 (void) section until the 17th (air) section.
Sections go from 0..16. Now whenever a section is not empty, it can potentially change lighting for the section itself, the section below and the section above, hence the bitmask 111b, which is 7d.

             */
            mask |= (ChunkSection.isEmpty(chunkSections[i]) ? 0 : 7) << i;
        }

        return mask;
    }

    private static int getCeilingLightMask(final Chunk chunk) {
        int mask = getLightMask(chunk);

        /*
         It is similar to get highest bit, it would turn an 001010 into an 001111 so basically the highest bit and all below.
         We then invert this, so we'd have 110000 and compare that to the "main" chunk.
         This is because the bug only appears when the current chunks lightmaps are higher than those of the neighbors, thus we can omit sending neighbors which are lower than the current chunks lights.

         so TLDR is that getCeilingLightMask returns a light mask with all bits set below the highest affected section. We could also count the number of leading zeros and invert them, somehow.
         @TODO: Implement Leafs suggestion
         either use Integer#numberOfLeadingZeros or document what this bithack is supposed to be doing then
         */
        mask |= mask >> 1;
        mask |= mask >> 2;
        mask |= mask >> 4;
        mask |= mask >> 8;
        mask |= mask >> 16;

        return mask;
    }
    // Paper end

    final void sendChunk(EntityPlayer entityplayer, Packet<?>[] apacket, Chunk chunk) { this.a(entityplayer, apacket, chunk); } // Paper - OBFHELPER
    private void a(EntityPlayer entityplayer, Packet<?>[] apacket, Chunk chunk) {
        if (apacket[0] == null) {
            // Paper start - add 8 for light fix workaround
            if (apacket.length != 10) { // in case Plugins call sendChunk, resize
                apacket = new Packet[10];
            }
            // Paper end
            apacket[0] = new PacketPlayOutMapChunk(chunk, 65535);
            apacket[1] = new PacketPlayOutLightUpdate(chunk.getPos(), this.lightEngine, true);

            // Paper start - Fix MC-162253
            final int lightMask = getLightMask(chunk);
            int i = 1;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0) {
                        continue;
                    }

                    ++i;

                    if (!chunk.isNeighbourLoaded(x, z)) {
                        continue;
                    }

                    final Chunk neighbor = chunk.getRelativeNeighbourIfLoaded(x, z);
                    final int updateLightMask = lightMask & ~getCeilingLightMask(neighbor);

                    if (updateLightMask == 0) {
                        continue;
                    }

                    apacket[i] = new PacketPlayOutLightUpdate(new ChunkCoordIntPair(chunk.getPos().x + x, chunk.getPos().z + z), lightEngine, updateLightMask, 0, true);
                }
            }
        }

        final int viewDistance = playerViewDistanceBroadcastMap.getLastViewDistance(entityplayer);
        final long lastPosition = playerViewDistanceBroadcastMap.getLastCoordinate(entityplayer);

        int j = 1;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                ++j;

                Packet<?> packet = apacket[j];
                if (packet == null) {
                    continue;
                }

                final int distX = Math.abs(MCUtil.getCoordinateX(lastPosition) - (chunk.getPos().x + x));
                final int distZ = Math.abs(MCUtil.getCoordinateZ(lastPosition) - (chunk.getPos().z + z));

                if (Math.max(distX, distZ) > viewDistance) {
                    continue;
                }
                entityplayer.playerConnection.sendPacket(packet);
            }
        }
        // Paper end - Fix MC-162253

        entityplayer.a(chunk.getPos(), apacket[0], apacket[1]);
        PacketDebug.a(this.world, chunk.getPos());
        List<Entity> list = Lists.newArrayList();
        List<Entity> list1 = Lists.newArrayList();
        // Paper start - optimise entity tracker
        // use the chunk entity list, not the whole trackedEntities map...
        Entity[] entities = chunk.entities.getRawData();
        for (int i = 0, size = chunk.entities.size(); i < size; ++i) {
            Entity entity = entities[i];
            if (entity == entityplayer) {
                continue;
            }
            PlayerChunkMap.EntityTracker tracker = this.trackedEntities.get(entity.getId());
            if (tracker != null) { // dumb plugins... move on...
                tracker.updatePlayer(entityplayer);
            }

            // keep the vanilla logic here - this is REQUIRED or else passengers and their vehicles disappear!
            // (and god knows what the leash thing is)

            if (entity instanceof EntityInsentient && ((EntityInsentient)entity).getLeashHolder() != null) {
                list.add(entity);
            }

            if (!entity.getPassengers().isEmpty()) {
                list1.add(entity);
            }
        }
        // Paper end - optimise entity tracker

        Iterator iterator;
        Entity entity1;

        if (!list.isEmpty()) {
            iterator = list.iterator();

            while (iterator.hasNext()) {
                entity1 = (Entity) iterator.next();
                entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(entity1, ((EntityInsentient) entity1).getLeashHolder()));
            }
        }

        if (!list1.isEmpty()) {
            iterator = list1.iterator();

            while (iterator.hasNext()) {
                entity1 = (Entity) iterator.next();
                entityplayer.playerConnection.sendPacket(new PacketPlayOutMount(entity1));
            }
        }

    }

    public VillagePlace getVillagePlace() { return this.h(); } // Paper - OBFHELPER
    protected VillagePlace h() {
        return this.m;
    }

    public CompletableFuture<Void> a(Chunk chunk) {
        return this.executor.f(() -> {
            chunk.a(this.world);
        });
    }

    public class EntityTracker {

        final EntityTrackerEntry trackerEntry; // Paper - private -> package private
        private final Entity tracker;
        private final int trackingDistance;
        private SectionPosition e;
        // Paper start
        // Replace trackedPlayers Set with a Map. The value is true until the player receives
        // their first update (which is forced to have absolute coordinates), false afterward.
        public java.util.Map<EntityPlayer, Boolean> trackedPlayerMap = new java.util.HashMap<>();
        public Set<EntityPlayer> trackedPlayers = trackedPlayerMap.keySet();

        public EntityTracker(Entity entity, int i, int j, boolean flag) {
            this.trackerEntry = new EntityTrackerEntry(PlayerChunkMap.this.world, entity, j, flag, this::broadcast, trackedPlayerMap); // CraftBukkit // Paper
            this.tracker = entity;
            this.trackingDistance = i;
            this.e = SectionPosition.a(entity);
        }

        // Paper start - use distance map to optimise tracker
        com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> lastTrackerCandidates;

        final void updatePlayers(com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> newTrackerCandidates) {
            com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> oldTrackerCandidates = this.lastTrackerCandidates;
            this.lastTrackerCandidates = newTrackerCandidates;

            if (newTrackerCandidates != null) {
                Object[] rawData = newTrackerCandidates.getBackingSet();
                for (int i = 0, len = rawData.length; i < len; ++i) {
                    Object raw = rawData[i];
                    if (!(raw instanceof EntityPlayer)) {
                        continue;
                    }
                    EntityPlayer player = (EntityPlayer)raw;
                    this.updatePlayer(player);
                }
            }

            if (oldTrackerCandidates == newTrackerCandidates) {
                // this is likely the case.
                // means there has been no range changes, so we can just use the above for tracking.
                return;
            }

            // stuff could have been removed, so we need to check the trackedPlayers set
            // for players that were removed

            for (EntityPlayer player : this.trackedPlayers.toArray(new EntityPlayer[0])) { // avoid CME
                if (newTrackerCandidates == null || !newTrackerCandidates.contains(player)) {
                    this.updatePlayer(player);
                }
            }
        }
        // Paper end - use distance map to optimise tracker

        public boolean equals(Object object) {
            return object instanceof PlayerChunkMap.EntityTracker ? ((PlayerChunkMap.EntityTracker) object).tracker.getId() == this.tracker.getId() : false;
        }

        public int hashCode() {
            return this.tracker.getId();
        }

        public void broadcast(Packet<?> packet) {
            Iterator iterator = this.trackedPlayers.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                entityplayer.playerConnection.sendPacket(packet);
            }

        }

        public void broadcastIncludingSelf(Packet<?> packet) {
            this.broadcast(packet);
            if (this.tracker instanceof EntityPlayer) {
                ((EntityPlayer) this.tracker).playerConnection.sendPacket(packet);
            }

        }

        public void a() {
            Iterator iterator = this.trackedPlayers.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                this.trackerEntry.a(entityplayer);
            }

        }

        public void clear(EntityPlayer entityplayer) {
            org.spigotmc.AsyncCatcher.catchOp("player tracker clear"); // Spigot
            if (this.trackedPlayers.remove(entityplayer)) {
                this.trackerEntry.a(entityplayer);
            }

        }

        public void updatePlayer(EntityPlayer entityplayer) {
            org.spigotmc.AsyncCatcher.catchOp("player tracker update"); // Spigot
            if (entityplayer != this.tracker) {
                // Paper start - remove allocation of Vec3D here
                //Vec3D vec3d = entityplayer.getPositionVector().d(this.tracker.getPositionVector()); // MC-155077, SPIGOT-5113
                double vec3d_dx = entityplayer.locX() - this.tracker.locX();
                double vec3d_dy = entityplayer.locY() - this.tracker.locY();
                double vec3d_dz = entityplayer.locZ() - this.tracker.locZ();
                // Paper end - remove allocation of Vec3D here
                int i = Math.min(this.b(), (PlayerChunkMap.this.viewDistance - 1) * 16);
                boolean flag = vec3d_dx >= (double) (-i) && vec3d_dx <= (double) i && vec3d_dz >= (double) (-i) && vec3d_dz <= (double) i && this.tracker.a(entityplayer); // Paper - remove allocation of Vec3D here

                if (flag) {
                    boolean flag1 = this.tracker.attachedToPlayer;

                    if (!flag1) {
                        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(this.tracker.chunkX, this.tracker.chunkZ);
                        PlayerChunk playerchunk = PlayerChunkMap.this.getVisibleChunk(chunkcoordintpair.pair());

                        if (playerchunk != null && playerchunk.getSendingChunk() != null) { // Paper - no-tick view distance
                            flag1 = PlayerChunkMap.b(chunkcoordintpair, entityplayer, false) <= PlayerChunkMap.this.viewDistance;
                        }
                    }

                    // CraftBukkit start - respect vanish API
                    if (this.tracker instanceof EntityPlayer) {
                        Player player = ((EntityPlayer) this.tracker).getBukkitEntity();
                        if (!entityplayer.getBukkitEntity().canSee(player)) {
                            flag1 = false;
                        }
                    }

                    entityplayer.removeQueue.remove(Integer.valueOf(this.tracker.getId()));
                    // CraftBukkit end

                    if (flag1 && this.trackedPlayerMap.putIfAbsent(entityplayer, true) == null) { // Paper
                        this.trackerEntry.b(entityplayer);
                    }
                } else if (this.trackedPlayers.remove(entityplayer)) {
                    this.trackerEntry.a(entityplayer);
                }

            }
        }

        private int a(int i) {
            return PlayerChunkMap.this.world.getMinecraftServer().b(i);
        }

        private int b() {
            Collection<Entity> collection = this.tracker.getAllPassengers();
            int i = this.trackingDistance;
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();
                int j = entity.getEntityType().getChunkRange() * 16;
                j = org.spigotmc.TrackingRange.getEntityTrackingRange(entity, j); // Paper

                if (j < i) { // Paper - we need the lowest range thanks to the fact that our tracker doesn't account for passenger logic
                    i = j;
                }
            }

            return this.a(i);
        }

        public void track(List<EntityPlayer> list) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                this.updatePlayer(entityplayer);
            }

        }
    }

    class a extends ChunkMapDistance {

        protected a(Executor executor, Executor executor1) {
            super(executor, executor1);
        }

        @Override
        protected boolean a(long i) {
            return PlayerChunkMap.this.unloadQueue.contains(i);
        }

        @Nullable
        @Override
        protected PlayerChunk b(long i) {
            return PlayerChunkMap.this.getUpdatingChunk(i);
        }

        @Nullable
        @Override
        protected PlayerChunk a(long i, int j, @Nullable PlayerChunk playerchunk, int k) {
            return PlayerChunkMap.this.a(i, j, playerchunk, k);
        }
    }
}
