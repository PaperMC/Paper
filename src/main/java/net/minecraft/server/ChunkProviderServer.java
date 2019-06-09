package net.minecraft.server;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.destroystokyo.paper.exception.ServerInternalException;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap; // Paper
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer extends IChunkProvider {

    private static final List<ChunkStatus> b = ChunkStatus.a(); static final List<ChunkStatus> getPossibleChunkStatuses() { return ChunkProviderServer.b; } // Paper - OBFHELPER
    private final ChunkMapDistance chunkMapDistance;
    public final ChunkGenerator chunkGenerator;
    private final WorldServer world;
    public final Thread serverThread; // Paper - private -> public
    private final LightEngineThreaded lightEngine;
    public final ChunkProviderServer.a serverThreadQueue; // Paper private -> public
    public final PlayerChunkMap playerChunkMap;
    private final WorldPersistentData worldPersistentData;
    private long lastTickTime;
    public boolean allowMonsters = true;
    public boolean allowAnimals = true;
    private final long[] cachePos = new long[4];
    private final ChunkStatus[] cacheStatus = new ChunkStatus[4];
    private final IChunkAccess[] cacheChunk = new IChunkAccess[4];
    @Nullable
    private SpawnerCreature.d p;
    // Paper start
    final com.destroystokyo.paper.util.concurrent.WeakSeqLock loadedChunkMapSeqLock = new com.destroystokyo.paper.util.concurrent.WeakSeqLock();
    final Long2ObjectOpenHashMap<Chunk> loadedChunkMap = new Long2ObjectOpenHashMap<>(8192, 0.5f);

    private final Chunk[] lastLoadedChunks = new Chunk[4 * 4];
    private final long[] lastLoadedChunkKeys = new long[4 * 4];

    {
        java.util.Arrays.fill(this.lastLoadedChunkKeys, MCUtil.INVALID_CHUNK_KEY);
    }

    private static int getCacheKey(int x, int z) {
        return x & 3 | ((z & 3) << 2);
    }

    void addLoadedChunk(Chunk chunk) {
        this.loadedChunkMapSeqLock.acquireWrite();
        try {
            this.loadedChunkMap.put(chunk.coordinateKey, chunk);
        } finally {
            this.loadedChunkMapSeqLock.releaseWrite();
        }

        // rewrite cache if we have to
        // we do this since we also cache null chunks
        int cacheKey = getCacheKey(chunk.getPos().x, chunk.getPos().z);

        long cachedKey = this.lastLoadedChunkKeys[cacheKey];
        if (cachedKey == chunk.coordinateKey) {
            this.lastLoadedChunks[cacheKey] = chunk;
        }
    }

    void removeLoadedChunk(Chunk chunk) {
        this.loadedChunkMapSeqLock.acquireWrite();
        try {
            this.loadedChunkMap.remove(chunk.coordinateKey);
        } finally {
            this.loadedChunkMapSeqLock.releaseWrite();
        }

        // rewrite cache if we have to
        // we do this since we also cache null chunks
        int cacheKey = getCacheKey(chunk.getPos().x, chunk.getPos().z);

        long cachedKey = this.lastLoadedChunkKeys[cacheKey];
        if (cachedKey == chunk.coordinateKey) {
            this.lastLoadedChunks[cacheKey] = null;
        }
    }

    public Chunk getChunkAtIfLoadedMainThread(int x, int z) {
        int cacheKey = getCacheKey(x, z);
        long chunkKey = MCUtil.getCoordinateKey(x, z);

        long cachedKey = this.lastLoadedChunkKeys[cacheKey];
        if (cachedKey == chunkKey) {
            return this.lastLoadedChunks[cacheKey];
        }

        Chunk ret = this.loadedChunkMap.get(chunkKey);

        this.lastLoadedChunkKeys[cacheKey] = chunkKey;
        this.lastLoadedChunks[cacheKey] = ret;

        return ret;
    }

    public Chunk getChunkAtIfLoadedMainThreadNoCache(int x, int z) {
        return this.loadedChunkMap.get(MCUtil.getCoordinateKey(x, z));
    }

    public Chunk getChunkAtMainThread(int x, int z) {
        Chunk ret = this.getChunkAtIfLoadedMainThread(x, z);
        if (ret != null) {
            return ret;
        }
        return (Chunk)this.getChunkAt(x, z, ChunkStatus.FULL, true);
    }

    private long chunkFutureAwaitCounter;

    public void getEntityTickingChunkAsync(int x, int z, java.util.function.Consumer<Chunk> onLoad) {
        if (Thread.currentThread() != this.serverThread) {
            this.serverThreadQueue.execute(() -> {
                ChunkProviderServer.this.getEntityTickingChunkAsync(x, z, onLoad);
            });
            return;
        }
        this.getChunkFutureAsynchronously(x, z, 31, PlayerChunk::getEntityTickingFuture, onLoad);
    }

    public void getTickingChunkAsync(int x, int z, java.util.function.Consumer<Chunk> onLoad) {
        if (Thread.currentThread() != this.serverThread) {
            this.serverThreadQueue.execute(() -> {
                ChunkProviderServer.this.getTickingChunkAsync(x, z, onLoad);
            });
            return;
        }
        this.getChunkFutureAsynchronously(x, z, 32, PlayerChunk::getTickingFuture, onLoad);
    }

    public void getFullChunkAsync(int x, int z, java.util.function.Consumer<Chunk> onLoad) {
        if (Thread.currentThread() != this.serverThread) {
            this.serverThreadQueue.execute(() -> {
                ChunkProviderServer.this.getFullChunkAsync(x, z, onLoad);
            });
            return;
        }
        this.getChunkFutureAsynchronously(x, z, 33, PlayerChunk::getFullChunkFuture, onLoad);
    }

    private void getChunkFutureAsynchronously(int x, int z, int ticketLevel, Function<PlayerChunk, CompletableFuture<Either<Chunk, PlayerChunk.Failure>>> futureGet, java.util.function.Consumer<Chunk> onLoad) {
        if (Thread.currentThread() != this.serverThread) {
            throw new IllegalStateException();
        }
        ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(x, z);
        Long identifier = Long.valueOf(this.chunkFutureAwaitCounter++);
        this.chunkMapDistance.addTicketAtLevel(TicketType.FUTURE_AWAIT, chunkPos, ticketLevel, identifier);
        this.tickDistanceManager();

        PlayerChunk chunk = this.playerChunkMap.getUpdatingChunk(chunkPos.pair());

        if (chunk == null) {
            throw new IllegalStateException("Expected playerchunk " + chunkPos + " in world '" + this.world.getWorld().getName() + "'");
        }

        CompletableFuture<Either<Chunk, PlayerChunk.Failure>> future = futureGet.apply(chunk);

        future.whenCompleteAsync((either, throwable) -> {
            try {
                if (throwable != null) {
                    if (throwable instanceof ThreadDeath) {
                        throw (ThreadDeath)throwable;
                    }
                    MinecraftServer.LOGGER.fatal("Failed to complete future await for chunk " + chunkPos.toString() + " in world '" + ChunkProviderServer.this.world.getWorld().getName() + "'", throwable);
                } else if (either.right().isPresent()) {
                    MinecraftServer.LOGGER.fatal("Failed to complete future await for chunk " + chunkPos.toString() + " in world '" + ChunkProviderServer.this.world.getWorld().getName() + "': " + either.right().get().toString());
                }

                try {
                    if (onLoad != null) {
                        playerChunkMap.callbackExecutor.execute(() -> {
                            onLoad.accept(either == null ? null : either.left().orElse(null)); // indicate failure to the callback.
                        });
                    }
                } catch (Throwable thr) {
                    if (thr instanceof ThreadDeath) {
                        throw (ThreadDeath)thr;
                    }
                    MinecraftServer.LOGGER.fatal("Load callback for future await failed " + chunkPos.toString() + " in world '" + ChunkProviderServer.this.world.getWorld().getName() + "'", thr);
                    return;
                }
            } finally {
                // due to odd behaviour with CB unload implementation we need to have these AFTER the load callback.
                ChunkProviderServer.this.chunkMapDistance.addTicketAtLevel(TicketType.UNKNOWN, chunkPos, ticketLevel, chunkPos);
                ChunkProviderServer.this.chunkMapDistance.removeTicketAtLevel(TicketType.FUTURE_AWAIT, chunkPos, ticketLevel, identifier);
            }
        }, this.serverThreadQueue);
    }
    // Paper end

    public ChunkProviderServer(WorldServer worldserver, Convertable.ConversionSession convertable_conversionsession, DataFixer datafixer, DefinedStructureManager definedstructuremanager, Executor executor, ChunkGenerator chunkgenerator, int i, boolean flag, WorldLoadListener worldloadlistener, Supplier<WorldPersistentData> supplier) {
        this.world = worldserver;
        this.serverThreadQueue = new ChunkProviderServer.a(worldserver);
        this.chunkGenerator = chunkgenerator;
        this.serverThread = Thread.currentThread();
        File file = convertable_conversionsession.a(worldserver.getDimensionKey());
        File file1 = new File(file, "data");

        file1.mkdirs();
        this.worldPersistentData = new WorldPersistentData(file1, datafixer);
        this.playerChunkMap = new PlayerChunkMap(worldserver, convertable_conversionsession, datafixer, definedstructuremanager, executor, this.serverThreadQueue, this, this.getChunkGenerator(), worldloadlistener, supplier, i, flag);
        this.lightEngine = this.playerChunkMap.a();
        this.chunkMapDistance = this.playerChunkMap.e();
        this.clearCache();
    }

    // CraftBukkit start - properly implement isChunkLoaded
    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        PlayerChunk chunk = this.playerChunkMap.getUpdatingChunk(ChunkCoordIntPair.pair(chunkX, chunkZ));
        if (chunk == null) {
            return false;
        }
        return chunk.getFullChunk() != null;
    }
    // CraftBukkit end

    @Override
    public LightEngineThreaded getLightEngine() {
        return this.lightEngine;
    }

    @Nullable
    private PlayerChunk getChunk(long i) {
        return this.playerChunkMap.getVisibleChunk(i);
    }

    public int b() {
        return this.playerChunkMap.c();
    }

    private void a(long i, IChunkAccess ichunkaccess, ChunkStatus chunkstatus) {
        for (int j = 3; j > 0; --j) {
            this.cachePos[j] = this.cachePos[j - 1];
            this.cacheStatus[j] = this.cacheStatus[j - 1];
            this.cacheChunk[j] = this.cacheChunk[j - 1];
        }

        this.cachePos[0] = i;
        this.cacheStatus[0] = chunkstatus;
        this.cacheChunk[0] = ichunkaccess;
    }

    // Paper start - "real" get chunk if loaded
    // Note: Partially copied from the getChunkAt method below
    @Nullable
    public Chunk getChunkAtIfCachedImmediately(int x, int z) {
        long k = ChunkCoordIntPair.pair(x, z);

        // Note: Bypass cache since we need to check ticket level, and to make this MT-Safe

        PlayerChunk playerChunk = this.getChunk(k);
        if (playerChunk == null) {
            return null;
        }

        return playerChunk.getFullChunkIfCached();
    }

    @Nullable
    public Chunk getChunkAtIfLoadedImmediately(int x, int z) {
        long k = ChunkCoordIntPair.pair(x, z);

        if (Thread.currentThread() == this.serverThread) {
            return this.getChunkAtIfLoadedMainThread(x, z);
        }

        Chunk ret = null;
        long readlock;
        do {
            readlock = this.loadedChunkMapSeqLock.acquireRead();
            try {
                ret = this.loadedChunkMap.get(k);
            } catch (Throwable thr) {
                if (thr instanceof ThreadDeath) {
                    throw (ThreadDeath)thr;
                }
                // re-try, this means a CME occurred...
                continue;
            }
        } while (!this.loadedChunkMapSeqLock.tryReleaseRead(readlock));

        return ret;
    }

    @Nullable
    public IChunkAccess getChunkAtImmediately(int x, int z) {
        long k = ChunkCoordIntPair.pair(x, z);

        // Note: Bypass cache to make this MT-Safe

        PlayerChunk playerChunk = this.getChunk(k);
        if (playerChunk == null) {
            return null;
        }

        return playerChunk.getAvailableChunkNow();

    }
    // Paper end

    @Nullable
    @Override
    public IChunkAccess getChunkAt(int i, int j, ChunkStatus chunkstatus, boolean flag) {
        if (Thread.currentThread() != this.serverThread) {
            return (IChunkAccess) CompletableFuture.supplyAsync(() -> {
                return this.getChunkAt(i, j, chunkstatus, flag);
            }, this.serverThreadQueue).join();
        } else {
            GameProfilerFiller gameprofilerfiller = this.world.getMethodProfiler();

            gameprofilerfiller.c("getChunk");
            long k = ChunkCoordIntPair.pair(i, j);

            IChunkAccess ichunkaccess;

            for (int l = 0; l < 4; ++l) {
                if (k == this.cachePos[l] && chunkstatus == this.cacheStatus[l]) {
                    ichunkaccess = this.cacheChunk[l];
                    if (ichunkaccess != null) { // CraftBukkit - the chunk can become accessible in the meantime TODO for non-null chunks it might also make sense to check that the chunk's state hasn't changed in the meantime
                        return ichunkaccess;
                    }
                }
            }

            gameprofilerfiller.c("getChunkCacheMiss");
            CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> completablefuture = this.getChunkFutureMainThread(i, j, chunkstatus, flag);

            if (!completablefuture.isDone()) { // Paper
                this.world.timings.syncChunkLoad.startTiming(); // Paper
            this.serverThreadQueue.awaitTasks(completablefuture::isDone);
                this.world.timings.syncChunkLoad.stopTiming(); // Paper
            } // Paper
            ichunkaccess = (IChunkAccess) ((Either) completablefuture.join()).map((ichunkaccess1) -> {
                return ichunkaccess1;
            }, (playerchunk_failure) -> {
                if (flag) {
                    throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Chunk not there when requested: " + playerchunk_failure)));
                } else {
                    return null;
                }
            });
            this.a(k, ichunkaccess, chunkstatus);
            return ichunkaccess;
        }
    }

    @Nullable
    @Override
    public Chunk a(int i, int j) {
        if (Thread.currentThread() != this.serverThread) {
            return null;
        } else {
            this.world.getMethodProfiler().c("getChunkNow");
            long k = ChunkCoordIntPair.pair(i, j);

            for (int l = 0; l < 4; ++l) {
                if (k == this.cachePos[l] && this.cacheStatus[l] == ChunkStatus.FULL) {
                    IChunkAccess ichunkaccess = this.cacheChunk[l];

                    return ichunkaccess instanceof Chunk ? (Chunk) ichunkaccess : null;
                }
            }

            PlayerChunk playerchunk = this.getChunk(k);

            if (playerchunk == null) {
                return null;
            } else {
                Either<IChunkAccess, PlayerChunk.Failure> either = (Either) playerchunk.b(ChunkStatus.FULL).getNow(null); // CraftBukkit - decompile error

                if (either == null) {
                    return null;
                } else {
                    IChunkAccess ichunkaccess1 = (IChunkAccess) either.left().orElse(null); // CraftBukkit - decompile error

                    if (ichunkaccess1 != null) {
                        this.a(k, ichunkaccess1, ChunkStatus.FULL);
                        if (ichunkaccess1 instanceof Chunk) {
                            return (Chunk) ichunkaccess1;
                        }
                    }

                    return null;
                }
            }
        }
    }

    private void clearCache() {
        Arrays.fill(this.cachePos, ChunkCoordIntPair.a);
        Arrays.fill(this.cacheStatus, (Object) null);
        Arrays.fill(this.cacheChunk, (Object) null);
    }

    private CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> getChunkFutureMainThread(int i, int j, ChunkStatus chunkstatus, boolean flag) {
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
        long k = chunkcoordintpair.pair();
        int l = 33 + ChunkStatus.a(chunkstatus);
        PlayerChunk playerchunk = this.getChunk(k);

        // CraftBukkit start - don't add new ticket for currently unloading chunk
        boolean currentlyUnloading = false;
        if (playerchunk != null) {
            PlayerChunk.State oldChunkState = PlayerChunk.getChunkState(playerchunk.oldTicketLevel);
            PlayerChunk.State currentChunkState = PlayerChunk.getChunkState(playerchunk.getTicketLevel());
            currentlyUnloading = (oldChunkState.isAtLeast(PlayerChunk.State.BORDER) && !currentChunkState.isAtLeast(PlayerChunk.State.BORDER));
        }
        if (flag && !currentlyUnloading) {
            // CraftBukkit end
            this.chunkMapDistance.a(TicketType.UNKNOWN, chunkcoordintpair, l, chunkcoordintpair);
            if (this.a(playerchunk, l)) {
                GameProfilerFiller gameprofilerfiller = this.world.getMethodProfiler();

                gameprofilerfiller.enter("chunkLoad");
                this.tickDistanceManager();
                playerchunk = this.getChunk(k);
                gameprofilerfiller.exit();
                if (this.a(playerchunk, l)) {
                    throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("No chunk holder after ticket has been added")));
                }
            }
        }

        return this.a(playerchunk, l) ? PlayerChunk.UNLOADED_CHUNK_ACCESS_FUTURE : playerchunk.a(chunkstatus, this.playerChunkMap);
    }

    private boolean a(@Nullable PlayerChunk playerchunk, int i) {
        return playerchunk == null || playerchunk.oldTicketLevel > i; // CraftBukkit using oldTicketLevel for isLoaded checks
    }

    public boolean isLoaded(int i, int j) {
        PlayerChunk playerchunk = this.getChunk((new ChunkCoordIntPair(i, j)).pair());
        int k = 33 + ChunkStatus.a(ChunkStatus.FULL);

        return !this.a(playerchunk, k);
    }

    @Override
    public IBlockAccess c(int i, int j) {
        long k = ChunkCoordIntPair.pair(i, j);
        PlayerChunk playerchunk = this.getChunk(k);

        if (playerchunk == null) {
            return null;
        } else {
            int l = ChunkProviderServer.b.size() - 1;

            while (true) {
                ChunkStatus chunkstatus = (ChunkStatus) ChunkProviderServer.b.get(l);
                Optional<IChunkAccess> optional = ((Either) playerchunk.getStatusFutureUnchecked(chunkstatus).getNow(PlayerChunk.UNLOADED_CHUNK_ACCESS)).left();

                if (optional.isPresent()) {
                    return (IBlockAccess) optional.get();
                }

                if (chunkstatus == ChunkStatus.LIGHT.e()) {
                    return null;
                }

                --l;
            }
        }
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    public boolean runTasks() {
        return this.serverThreadQueue.executeNext();
    }

    private boolean tickDistanceManager() {
        boolean flag = this.chunkMapDistance.a(this.playerChunkMap);
        boolean flag1 = this.playerChunkMap.b();

        if (!flag && !flag1) {
            return false;
        } else {
            this.clearCache();
            return true;
        }
    }

    public final boolean isInEntityTickingChunk(Entity entity) { return this.a(entity); } // Paper - OBFHELPER
    @Override public boolean a(Entity entity) {
        long i = ChunkCoordIntPair.pair(MathHelper.floor(entity.locX()) >> 4, MathHelper.floor(entity.locZ()) >> 4);

        return this.a(i, (Function<PlayerChunk, CompletableFuture<Either<Chunk, PlayerChunk.Failure>>>) PlayerChunk::b); // CraftBukkit - decompile error
    }

    public final boolean isEntityTickingChunk(ChunkCoordIntPair chunkcoordintpair) { return this.a(chunkcoordintpair); } // Paper - OBFHELPER
    @Override public boolean a(ChunkCoordIntPair chunkcoordintpair) {
        return this.a(chunkcoordintpair.pair(), (Function<PlayerChunk, CompletableFuture<Either<Chunk, PlayerChunk.Failure>>>) PlayerChunk::b); // CraftBukkit - decompile error
    }

    @Override
    public boolean a(BlockPosition blockposition) {
        long i = ChunkCoordIntPair.pair(blockposition.getX() >> 4, blockposition.getZ() >> 4);

        return this.a(i, (Function<PlayerChunk, CompletableFuture<Either<Chunk, PlayerChunk.Failure>>>) PlayerChunk::a); // CraftBukkit - decompile error
    }

    private boolean a(long i, Function<PlayerChunk, CompletableFuture<Either<Chunk, PlayerChunk.Failure>>> function) {
        PlayerChunk playerchunk = this.getChunk(i);

        if (playerchunk == null) {
            return false;
        } else {
            Either<Chunk, PlayerChunk.Failure> either = (Either) ((CompletableFuture) function.apply(playerchunk)).getNow(PlayerChunk.UNLOADED_CHUNK);

            return either.left().isPresent();
        }
    }

    public void save(boolean flag) {
        this.tickDistanceManager();
        try (co.aikar.timings.Timing timed = world.timings.chunkSaveData.startTiming()) { // Paper - Timings
        this.playerChunkMap.save(flag);
        } // Paper - Timings
    }

    // Paper start - duplicate save, but call incremental
    public void saveIncrementally() {
        this.tickDistanceManager();
        try (co.aikar.timings.Timing timed = world.timings.chunkSaveData.startTiming()) { // Paper - Timings
            this.playerChunkMap.saveIncrementally();
        } // Paper - Timings
    }
    // Paper end

    @Override
    public void close() throws IOException {
        // CraftBukkit start
        close(true);
    }

    public void close(boolean save) throws IOException {
        if (save) {
            this.save(true);
        }
        // CraftBukkit end
        this.lightEngine.close();
        this.playerChunkMap.close();
    }

    // CraftBukkit start - modelled on below
    public void purgeUnload() {
        this.world.getMethodProfiler().enter("purge");
        this.chunkMapDistance.purgeTickets();
        this.tickDistanceManager();
        this.world.getMethodProfiler().exitEnter("unload");
        this.playerChunkMap.unloadChunks(() -> true);
        this.world.getMethodProfiler().exit();
        this.clearCache();
    }
    // CraftBukkit end

    public void tick(BooleanSupplier booleansupplier) {
        this.world.getMethodProfiler().enter("purge");
        this.world.timings.doChunkMap.startTiming(); // Spigot
        this.chunkMapDistance.purgeTickets();
        this.tickDistanceManager();
        this.world.timings.doChunkMap.stopTiming(); // Spigot
        this.world.getMethodProfiler().exitEnter("chunks");
        this.world.timings.chunks.startTiming(); // Paper - timings
        this.tickChunks();
        this.world.timings.chunks.stopTiming(); // Paper - timings
        this.world.timings.doChunkUnload.startTiming(); // Spigot
        this.world.getMethodProfiler().exitEnter("unload");
        this.playerChunkMap.unloadChunks(booleansupplier);
        this.world.timings.doChunkUnload.stopTiming(); // Spigot
        this.world.getMethodProfiler().exit();
        this.clearCache();
    }

    private void tickChunks() {
        long i = this.world.getTime();
        long j = i - this.lastTickTime;

        this.lastTickTime = i;
        WorldData worlddata = this.world.getWorldData();
        boolean flag = this.world.isDebugWorld();
        boolean flag1 = this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && !world.getPlayers().isEmpty(); // CraftBukkit

        if (!flag) {
            this.world.getMethodProfiler().enter("pollingChunks");
            int k = this.world.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED);
            boolean flag2 = world.ticksPerAnimalSpawns != 0L && worlddata.getTime() % world.ticksPerAnimalSpawns == 0L; // CraftBukkit

            this.world.getMethodProfiler().enter("naturalSpawnCount");
            this.world.timings.countNaturalMobs.startTiming(); // Paper - timings
            int l = this.chunkMapDistance.b();
            SpawnerCreature.d spawnercreature_d = SpawnerCreature.a(l, this.world.A(), this::a);
            this.world.timings.countNaturalMobs.stopTiming(); // Paper - timings

            this.p = spawnercreature_d;
            this.world.getMethodProfiler().exit();
            //List<PlayerChunk> list = Lists.newArrayList(this.playerChunkMap.f()); // Paper
            //Collections.shuffle(list); // Paper
            //Paper start - call player naturally spawn event
            int chunkRange = world.spigotConfig.mobSpawnRange;
            chunkRange = (chunkRange > world.spigotConfig.viewDistance) ? (byte) world.spigotConfig.viewDistance : chunkRange;
            chunkRange = Math.min(chunkRange, 8);
            for (EntityPlayer entityPlayer : this.world.getPlayers()) {
                entityPlayer.playerNaturallySpawnedEvent = new com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent(entityPlayer.getBukkitEntity(), (byte) chunkRange);
                entityPlayer.playerNaturallySpawnedEvent.callEvent();
            };
            // Paper end
            this.playerChunkMap.f().forEach((playerchunk) -> { // Paper - no... just no...
                Optional<Chunk> optional = ((Either) playerchunk.a().getNow(PlayerChunk.UNLOADED_CHUNK)).left();

                if (optional.isPresent()) {
                    this.world.getMethodProfiler().enter("broadcast");
                    this.world.timings.broadcastChunkUpdates.startTiming(); // Paper - timings
                    playerchunk.a((Chunk) optional.get());
                    this.world.timings.broadcastChunkUpdates.stopTiming(); // Paper - timings
                    this.world.getMethodProfiler().exit();
                    Optional<Chunk> optional1 = ((Either) playerchunk.b().getNow(PlayerChunk.UNLOADED_CHUNK)).left();

                    if (optional1.isPresent()) {
                        Chunk chunk = (Chunk) optional1.get();
                        ChunkCoordIntPair chunkcoordintpair = playerchunk.i();

                        if (!this.playerChunkMap.isOutsideOfRange(chunkcoordintpair)) {
                            chunk.setInhabitedTime(chunk.getInhabitedTime() + j);
                            if (flag1 && (this.allowMonsters || this.allowAnimals) && this.world.getWorldBorder().isInBounds(chunk.getPos()) && !this.playerChunkMap.isOutsideOfRange(chunkcoordintpair, true)) { // Spigot
                                SpawnerCreature.a(this.world, chunk, spawnercreature_d, this.allowAnimals, this.allowMonsters, flag2);
                            }

                            this.world.timings.chunkTicks.startTiming(); // Spigot // Paper
                            this.world.a(chunk, k);
                            this.world.timings.chunkTicks.stopTiming(); // Spigot // Paper
                        }
                    }
                }
            });
            this.world.getMethodProfiler().enter("customSpawners");
            if (flag1) {
                try (co.aikar.timings.Timing ignored = this.world.timings.miscMobSpawning.startTiming()) { // Paper - timings
                this.world.doMobSpawning(this.allowMonsters, this.allowAnimals);
                } // Paper - timings
            }

            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().exit();
        }

        this.playerChunkMap.g();
    }

    private void a(long i, Consumer<Chunk> consumer) {
        PlayerChunk playerchunk = this.getChunk(i);

        if (playerchunk != null) {
            ((Either) playerchunk.c().getNow(PlayerChunk.UNLOADED_CHUNK)).left().ifPresent(consumer);
        }

    }

    @Override
    public String getName() {
        return "ServerChunkCache: " + this.h();
    }

    @VisibleForTesting
    public int f() {
        return this.serverThreadQueue.bh();
    }

    public ChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }

    public int h() {
        return this.playerChunkMap.d();
    }

    public void flagDirty(BlockPosition blockposition) {
        int i = blockposition.getX() >> 4;
        int j = blockposition.getZ() >> 4;
        PlayerChunk playerchunk = this.getChunk(ChunkCoordIntPair.pair(i, j));

        if (playerchunk != null) {
            playerchunk.a(blockposition);
        }

    }

    @Override
    public void a(EnumSkyBlock enumskyblock, SectionPosition sectionposition) {
        this.serverThreadQueue.execute(() -> {
            PlayerChunk playerchunk = this.getChunk(sectionposition.r().pair());

            if (playerchunk != null) {
                playerchunk.a(enumskyblock, sectionposition.b());
            }

        });
    }

    public <T> void addTicket(TicketType<T> tickettype, ChunkCoordIntPair chunkcoordintpair, int i, T t0) {
        this.chunkMapDistance.addTicket(tickettype, chunkcoordintpair, i, t0);
    }

    public <T> void removeTicket(TicketType<T> tickettype, ChunkCoordIntPair chunkcoordintpair, int i, T t0) {
        this.chunkMapDistance.removeTicket(tickettype, chunkcoordintpair, i, t0);
    }

    @Override
    public void a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        this.chunkMapDistance.a(chunkcoordintpair, flag);
    }

    public void movePlayer(EntityPlayer entityplayer) {
        this.playerChunkMap.movePlayer(entityplayer);
    }

    public void removeEntity(Entity entity) {
        this.playerChunkMap.removeEntity(entity);
    }

    public void addEntity(Entity entity) {
        this.playerChunkMap.addEntity(entity);
    }

    public void broadcastIncludingSelf(Entity entity, Packet<?> packet) {
        this.playerChunkMap.broadcastIncludingSelf(entity, packet);
    }

    public void broadcast(Entity entity, Packet<?> packet) {
        this.playerChunkMap.broadcast(entity, packet);
    }

    public void setViewDistance(int i) {
        this.playerChunkMap.setViewDistance(i);
    }

    @Override
    public void a(boolean flag, boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }

    public WorldPersistentData getWorldPersistentData() {
        return this.worldPersistentData;
    }

    public VillagePlace j() {
        return this.playerChunkMap.h();
    }

    @Nullable
    public SpawnerCreature.d k() {
        return this.p;
    }

    final class a extends IAsyncTaskHandler<Runnable> {

        private a(World world) {
            super("Chunk source main thread executor for " + world.getDimensionKey().a());
        }

        @Override
        protected Runnable postToMainThread(Runnable runnable) {
            return runnable;
        }

        @Override
        protected boolean canExecute(Runnable runnable) {
            return true;
        }

        @Override
        protected boolean isNotMainThread() {
            return true;
        }

        @Override
        protected Thread getThread() {
            return ChunkProviderServer.this.serverThread;
        }

        @Override
        protected void executeTask(Runnable runnable) {
            ChunkProviderServer.this.world.getMethodProfiler().c("runTask");
            super.executeTask(runnable);
        }

        @Override
        protected boolean executeNext() {
        // CraftBukkit start - process pending Chunk loadCallback() and unloadCallback() after each run task
        try {
            if (ChunkProviderServer.this.tickDistanceManager()) {
                return true;
            } else {
                ChunkProviderServer.this.lightEngine.queueUpdate();
                return super.executeNext();
            }
        } finally {
            playerChunkMap.callbackExecutor.run();
        }
        // CraftBukkit end
        }
    }
}
