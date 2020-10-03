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

public class ChunkProviderServer extends IChunkProvider {

    private static final List<ChunkStatus> b = ChunkStatus.a();
    private final ChunkMapDistance chunkMapDistance;
    public final ChunkGenerator chunkGenerator;
    private final WorldServer world;
    private final Thread serverThread;
    private final LightEngineThreaded lightEngine;
    private final ChunkProviderServer.a serverThreadQueue;
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

            this.serverThreadQueue.awaitTasks(completablefuture::isDone);
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

    @Override
    public boolean a(Entity entity) {
        long i = ChunkCoordIntPair.pair(MathHelper.floor(entity.locX()) >> 4, MathHelper.floor(entity.locZ()) >> 4);

        return this.a(i, (Function<PlayerChunk, CompletableFuture<Either<Chunk, PlayerChunk.Failure>>>) PlayerChunk::b); // CraftBukkit - decompile error
    }

    @Override
    public boolean a(ChunkCoordIntPair chunkcoordintpair) {
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
        this.playerChunkMap.save(flag);
    }

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
        this.chunkMapDistance.purgeTickets();
        this.tickDistanceManager();
        this.world.getMethodProfiler().exitEnter("chunks");
        this.tickChunks();
        this.world.getMethodProfiler().exitEnter("unload");
        this.playerChunkMap.unloadChunks(booleansupplier);
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
            int l = this.chunkMapDistance.b();
            SpawnerCreature.d spawnercreature_d = SpawnerCreature.a(l, this.world.A(), this::a);

            this.p = spawnercreature_d;
            this.world.getMethodProfiler().exit();
            List<PlayerChunk> list = Lists.newArrayList(this.playerChunkMap.f());

            Collections.shuffle(list);
            list.forEach((playerchunk) -> {
                Optional<Chunk> optional = ((Either) playerchunk.a().getNow(PlayerChunk.UNLOADED_CHUNK)).left();

                if (optional.isPresent()) {
                    this.world.getMethodProfiler().enter("broadcast");
                    playerchunk.a((Chunk) optional.get());
                    this.world.getMethodProfiler().exit();
                    Optional<Chunk> optional1 = ((Either) playerchunk.b().getNow(PlayerChunk.UNLOADED_CHUNK)).left();

                    if (optional1.isPresent()) {
                        Chunk chunk = (Chunk) optional1.get();
                        ChunkCoordIntPair chunkcoordintpair = playerchunk.i();

                        if (!this.playerChunkMap.isOutsideOfRange(chunkcoordintpair)) {
                            chunk.setInhabitedTime(chunk.getInhabitedTime() + j);
                            if (flag1 && (this.allowMonsters || this.allowAnimals) && this.world.getWorldBorder().isInBounds(chunk.getPos())) {
                                SpawnerCreature.a(this.world, chunk, spawnercreature_d, this.allowAnimals, this.allowMonsters, flag2);
                            }

                            this.world.a(chunk, k);
                        }
                    }
                }
            });
            this.world.getMethodProfiler().enter("customSpawners");
            if (flag1) {
                this.world.doMobSpawning(this.allowMonsters, this.allowAnimals);
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
