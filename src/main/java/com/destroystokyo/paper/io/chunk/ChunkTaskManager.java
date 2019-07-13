package com.destroystokyo.paper.io.chunk;

import com.destroystokyo.paper.io.PaperFileIOThread;
import com.destroystokyo.paper.io.IOUtil;
import com.destroystokyo.paper.io.PrioritizedTaskQueue;
import com.destroystokyo.paper.io.QueueExecutorThread;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.IAsyncTaskHandler;
import net.minecraft.server.IChunkAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PlayerChunk;
import net.minecraft.server.WorldServer;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.spigotmc.AsyncCatcher;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public final class ChunkTaskManager {

    private final QueueExecutorThread<ChunkTask>[] workers;
    private final WorldServer world;

    private final PrioritizedTaskQueue<ChunkTask> queue;
    private final boolean perWorldQueue;

    final ConcurrentHashMap<Long, ChunkLoadTask> chunkLoadTasks = new ConcurrentHashMap<>(64, 0.5f);
    final ConcurrentHashMap<Long, ChunkSaveTask> chunkSaveTasks = new ConcurrentHashMap<>(64, 0.5f);

    private final PrioritizedTaskQueue<ChunkTask> chunkTasks = new PrioritizedTaskQueue<>(); // used if async chunks are disabled in config

    protected static QueueExecutorThread<ChunkTask>[] globalWorkers;
    protected static QueueExecutorThread<ChunkTask> globalUrgentWorker;
    protected static PrioritizedTaskQueue<ChunkTask> globalQueue;
    protected static PrioritizedTaskQueue<ChunkTask> globalUrgentQueue;

    protected static final ConcurrentLinkedQueue<Runnable> CHUNK_WAIT_QUEUE = new ConcurrentLinkedQueue<>();

    public static final ArrayDeque<ChunkInfo> WAITING_CHUNKS = new ArrayDeque<>(); // stack

    private static final class ChunkInfo {

        public final int chunkX;
        public final int chunkZ;
        public final WorldServer world;

        public ChunkInfo(final int chunkX, final int chunkZ, final WorldServer world) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.world = world;
        }

        @Override
        public String toString() {
            return "[( " + this.chunkX + "," + this.chunkZ + ") in '" + this.world.getWorld().getName() + "']";
        }
    }

    public static void pushChunkWait(final WorldServer world, final int chunkX, final int chunkZ) {
        synchronized (WAITING_CHUNKS) {
            WAITING_CHUNKS.push(new ChunkInfo(chunkX, chunkZ, world));
        }
    }

    public static void popChunkWait() {
        synchronized (WAITING_CHUNKS) {
            WAITING_CHUNKS.pop();
        }
    }

    private static ChunkInfo[] getChunkInfos() {
        ChunkInfo[] chunks;
        synchronized (WAITING_CHUNKS) {
            chunks = WAITING_CHUNKS.toArray(new ChunkInfo[0]);
        }
        return chunks;
    }

    public static void dumpAllChunkLoadInfo() {
        ChunkInfo[] chunks = getChunkInfos();
        if (chunks.length > 0) {
            PaperFileIOThread.LOGGER.log(Level.ERROR, "Chunk wait task info below: ");

            for (final ChunkInfo chunkInfo : chunks) {
                final long key = IOUtil.getCoordinateKey(chunkInfo.chunkX, chunkInfo.chunkZ);
                final ChunkLoadTask loadTask = chunkInfo.world.asyncChunkTaskManager.chunkLoadTasks.get(key);
                final ChunkSaveTask saveTask = chunkInfo.world.asyncChunkTaskManager.chunkSaveTasks.get(key);

                PaperFileIOThread.LOGGER.log(Level.ERROR, chunkInfo.chunkX + "," + chunkInfo.chunkZ + " in '" + chunkInfo.world.getWorld().getName() + ":");
                PaperFileIOThread.LOGGER.log(Level.ERROR, "Load Task - " + (loadTask == null ? "none" : loadTask.toString()));
                PaperFileIOThread.LOGGER.log(Level.ERROR, "Save Task - " + (saveTask == null ? "none" : saveTask.toString()));
                // log current status of chunk to indicate whether we're waiting on generation or loading
                net.minecraft.server.PlayerChunk chunkHolder = chunkInfo.world.getChunkProvider().playerChunkMap.getVisibleChunk(key);

                dumpChunkInfo(new HashSet<>(), chunkHolder, chunkInfo.chunkX, chunkInfo.chunkZ);
            }
        }
    }

    static void dumpChunkInfo(Set<PlayerChunk> seenChunks, PlayerChunk chunkHolder, int x, int z) {
        dumpChunkInfo(seenChunks, chunkHolder, x, z, 0, 1);
    }

    static void dumpChunkInfo(Set<PlayerChunk> seenChunks, PlayerChunk chunkHolder, int x, int z, int indent, int maxDepth) {
        if (seenChunks.contains(chunkHolder)) {
            return;
        }
        if (indent > maxDepth) {
            return;
        }
        seenChunks.add(chunkHolder);
        String indentStr = StringUtils.repeat("  ", indent);
        if (chunkHolder == null) {
            PaperFileIOThread.LOGGER.log(Level.ERROR, indentStr + "Chunk Holder - null for (" + x +"," + z +")");
        } else {
            IChunkAccess chunk = chunkHolder.getAvailableChunkNow();
            net.minecraft.server.ChunkStatus holderStatus = chunkHolder.getChunkHolderStatus();
            PaperFileIOThread.LOGGER.log(Level.ERROR, indentStr + "Chunk Holder - non-null");
            PaperFileIOThread.LOGGER.log(Level.ERROR, indentStr + "Chunk Status - " + ((chunk == null) ? "null chunk" : chunk.getChunkStatus().toString()));
            PaperFileIOThread.LOGGER.log(Level.ERROR, indentStr + "Chunk Ticket Status - "  + PlayerChunk.getChunkStatus(chunkHolder.getTicketLevel()));
            PaperFileIOThread.LOGGER.log(Level.ERROR, indentStr + "Chunk Holder Status - " + ((holderStatus == null) ? "null" : holderStatus.toString()));
        }
    }

    public static void initGlobalLoadThreads(int threads) {
        if (threads <= 0 || globalWorkers != null) {
            return;
        }

        globalWorkers = new QueueExecutorThread[threads];
        globalQueue = new PrioritizedTaskQueue<>();
        globalUrgentQueue = new PrioritizedTaskQueue<>();

        for (int i = 0; i < threads; ++i) {
            globalWorkers[i] = new QueueExecutorThread<>(globalQueue, (long)0.10e6); //0.1ms
            globalWorkers[i].setName("Paper Async Chunk Task Thread #" + i);
            globalWorkers[i].setPriority(Thread.NORM_PRIORITY - 1);
            globalWorkers[i].setUncaughtExceptionHandler((final Thread thread, final Throwable throwable) -> {
                PaperFileIOThread.LOGGER.fatal("Thread '" + thread.getName() + "' threw an uncaught exception!", throwable);
            });

            globalWorkers[i].start();
        }

        globalUrgentWorker = new QueueExecutorThread<>(globalUrgentQueue, (long)0.10e6); //0.1ms
        globalUrgentWorker.setName("Paper Async Chunk Urgent Task Thread");
        globalUrgentWorker.setPriority(Thread.NORM_PRIORITY+1);
        globalUrgentWorker.setUncaughtExceptionHandler((final Thread thread, final Throwable throwable) -> {
            PaperFileIOThread.LOGGER.fatal("Thread '" + thread.getName() + "' threw an uncaught exception!", throwable);
        });

        globalUrgentWorker.start();
    }

    /**
     * Creates this chunk task manager to operate off the specified number of threads. If the specified number of threads is
     * less-than or equal to 0, then this chunk task manager will operate off of the world's chunk task queue.
     * @param world Specified world.
     * @param threads Specified number of threads.
     * @see net.minecraft.server.ChunkProviderServer#serverThreadQueue
     */
    public ChunkTaskManager(final WorldServer world, final int threads) {
        this.world = world;
        this.workers = threads <= 0 ? null : new QueueExecutorThread[threads];
        this.queue = new PrioritizedTaskQueue<>();
        this.perWorldQueue = true;

        for (int i = 0; i < threads; ++i) {
            this.workers[i] = new QueueExecutorThread<>(this.queue, (long)0.10e6); //0.1ms
            this.workers[i].setName("Async chunk loader thread #" + i +  " for world: " + world.getWorld().getName());
            this.workers[i].setPriority(Thread.NORM_PRIORITY - 1);
            this.workers[i].setUncaughtExceptionHandler((final Thread thread, final Throwable throwable) -> {
                PaperFileIOThread.LOGGER.fatal("Thread '" + thread.getName() + "' threw an uncaught exception!", throwable);
            });

            this.workers[i].start();
        }
    }

    /**
     * Creates the chunk task manager to work from the global workers. When {@link #close(boolean)} is invoked,
     * the global queue is not shutdown. If the global workers is configured to be disabled or use 0 threads, then
     * this chunk task manager will operate off of the world's chunk task queue.
     * @param world The world that this task manager is responsible for
     * @see net.minecraft.server.ChunkProviderServer#serverThreadQueue
     */
    public ChunkTaskManager(final WorldServer world) {
        this.world = world;
        this.workers = globalWorkers;
        this.queue = globalQueue;
        this.perWorldQueue = false;
    }

    public boolean pollNextChunkTask() {
        final ChunkTask task = this.chunkTasks.poll();

        if (task != null) {
            task.run();
            return true;
        }
        return false;
    }

    /**
     * Polls and runs the next available chunk wait queue task. This is to be used when the server is waiting on a chunk queue.
     * (per-world can cause issues if all the worker threads are blocked waiting for a response from the main thread)
     */
    public static boolean pollChunkWaitQueue() {
        final Runnable run = CHUNK_WAIT_QUEUE.poll();
        if (run != null) {
            run.run();
            return true;
        }
        return false;
    }

    /**
     * Queues a chunk wait task. Note that this will execute out of order with respect to tasks scheduled on a world's
     * chunk task queue, since this is the global chunk wait queue.
     */
    public static void queueChunkWaitTask(final Runnable runnable) {
        CHUNK_WAIT_QUEUE.add(runnable);
    }

    private static void drainChunkWaitQueue() {
        Runnable run;
        while ((run = CHUNK_WAIT_QUEUE.poll()) != null) {
            run.run();
        }
    }

    /**
     * The exact same as {@link #scheduleChunkLoad(int, int, int, Consumer, boolean)}, except that the chunk data is provided as
     * the {@code data} parameter.
     */
    public ChunkLoadTask scheduleChunkLoad(final int chunkX, final int chunkZ, final int priority,
                                           final Consumer<ChunkRegionLoader.InProgressChunkHolder> onComplete,
                                           final boolean intendingToBlock, final CompletableFuture<NBTTagCompound> dataFuture) {
        final WorldServer world = this.world;

        return this.chunkLoadTasks.compute(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)), (final Long keyInMap, final ChunkLoadTask valueInMap) -> {
            if (valueInMap != null) {
                if (!valueInMap.cancelled) {
                    throw new IllegalStateException("Double scheduling chunk load for task: " + valueInMap.toString());
                }
                valueInMap.cancelled = false;
                valueInMap.onComplete = onComplete;
                return valueInMap;
            }

            final ChunkLoadTask ret = new ChunkLoadTask(world, chunkX, chunkZ, priority, ChunkTaskManager.this, onComplete);

            dataFuture.thenAccept((final NBTTagCompound data) -> {
                final boolean failed = data == PaperFileIOThread.FAILURE_VALUE;
                PaperFileIOThread.Holder.INSTANCE.loadChunkDataAsync(world, chunkX, chunkZ, priority, (final PaperFileIOThread.ChunkData chunkData) -> {
                    ret.chunkData = chunkData;
                    if (!failed) {
                        chunkData.chunkData = data;
                    }
                    ChunkTaskManager.this.internalSchedule(ret); // only schedule to the worker threads here
                }, true, failed, intendingToBlock); // read data off disk if the future fails
            });

            return ret;
        });
    }

    public void cancelChunkLoad(final int chunkX, final int chunkZ) {
        this.chunkLoadTasks.compute(IOUtil.getCoordinateKey(chunkX, chunkZ), (final Long keyInMap, final ChunkLoadTask valueInMap) -> {
            if (valueInMap == null) {
                return null;
            }

            if (valueInMap.cancelled) {
                PaperFileIOThread.LOGGER.warn("Task " + valueInMap.toString() + " is already cancelled!");
            }
            valueInMap.cancelled = true;
            if (valueInMap.cancel()) {
                return null;
            }

            return valueInMap;
        });
    }

    /**
     * Schedules an asynchronous chunk load for the specified coordinates. The onComplete parameter may be invoked asynchronously
     * on a worker thread or on the world's chunk executor queue. As such the code that is executed for the parameter should be
     * carefully chosen.
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @param priority Priority for this task
     * @param onComplete The consumer to invoke with the {@link net.minecraft.server.ChunkRegionLoader.InProgressChunkHolder} object once this task is complete
     * @param intendingToBlock Whether the caller is intending to block on this task completing (this is a performance tune, and has no adverse side-effects)
     * @return The {@link ChunkLoadTask} associated with
     */
    public ChunkLoadTask scheduleChunkLoad(final int chunkX, final int chunkZ, final int priority,
                                           final Consumer<ChunkRegionLoader.InProgressChunkHolder> onComplete,
                                           final boolean intendingToBlock) {
        final WorldServer world = this.world;

        return this.chunkLoadTasks.compute(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)), (final Long keyInMap, final ChunkLoadTask valueInMap) -> {
            if (valueInMap != null) {
                if (!valueInMap.cancelled) {
                    throw new IllegalStateException("Double scheduling chunk load for task: " + valueInMap.toString());
                }
                valueInMap.cancelled = false;
                valueInMap.onComplete = onComplete;
                return valueInMap;
            }

            final ChunkLoadTask ret = new ChunkLoadTask(world, chunkX, chunkZ, priority, ChunkTaskManager.this, onComplete);

            PaperFileIOThread.Holder.INSTANCE.loadChunkDataAsync(world, chunkX, chunkZ, priority, (final PaperFileIOThread.ChunkData chunkData) -> {
                ret.chunkData = chunkData;
                ChunkTaskManager.this.internalSchedule(ret); // only schedule to the worker threads here
            }, true, true, intendingToBlock);

            return ret;
        });
    }

    /**
     * Schedules an async save for the specified chunk. The chunk, at the beginning of this call, must be completely unloaded
     * from the world.
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @param priority Priority for this task
     * @param asyncSaveData Async save data. See {@link ChunkRegionLoader#getAsyncSaveData(WorldServer, IChunkAccess)}
     * @param chunk Chunk to save
     * @return The {@link ChunkSaveTask} associated with the save task.
     */
    public ChunkSaveTask scheduleChunkSave(final int chunkX, final int chunkZ, final int priority,
                                           final ChunkRegionLoader.AsyncSaveData asyncSaveData,
                                           final IChunkAccess chunk) {
        AsyncCatcher.catchOp("chunk save schedule");

        final WorldServer world = this.world;

        return this.chunkSaveTasks.compute(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)), (final Long keyInMap, final ChunkSaveTask valueInMap) -> {
            if (valueInMap != null) {
                throw new IllegalStateException("Double scheduling chunk save for task: " + valueInMap.toString());
            }

            final ChunkSaveTask ret = new ChunkSaveTask(world, chunkX, chunkZ, priority, ChunkTaskManager.this, asyncSaveData, chunk);

            ChunkTaskManager.this.internalSchedule(ret);

            return ret;
        });
    }

    /**
     * Returns a completable future which will be completed with the <b>un-copied</b> chunk data for an in progress async save.
     * Returns {@code null} if no save is in progress.
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     */
    public CompletableFuture<NBTTagCompound> getChunkSaveFuture(final int chunkX, final int chunkZ) {
        final ChunkSaveTask chunkSaveTask = this.chunkSaveTasks.get(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)));
        if (chunkSaveTask == null) {
            return null;
        }
        return chunkSaveTask.onComplete;
    }

    /**
     * Returns the chunk object being used to serialize data async for an unloaded chunk. Note that modifying this chunk
     * is not safe to do as another thread is handling its save. The chunk is also not loaded into the world.
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @return Chunk object for an in-progress async save, or {@code null} if no save is in progress
     */
    public IChunkAccess getChunkInSaveProgress(final int chunkX, final int chunkZ) {
        final ChunkSaveTask chunkSaveTask = this.chunkSaveTasks.get(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)));
        if (chunkSaveTask == null) {
            return null;
        }
        return chunkSaveTask.chunk;
    }

    public void flush() {
        // flush here since we schedule tasks on the IO thread that can schedule tasks here
        drainChunkWaitQueue();
        PaperFileIOThread.Holder.INSTANCE.flush();
        drainChunkWaitQueue();

        if (this.workers == null) {
            if (Bukkit.isPrimaryThread() || MinecraftServer.getServer().hasStopped()) {
                ((IAsyncTaskHandler<Runnable>)this.world.getChunkProvider().serverThreadQueue).executeAll();
            } else {
                CompletableFuture<Void> wait = new CompletableFuture<>();
                MinecraftServer.getServer().scheduleOnMain(() -> {
                    ((IAsyncTaskHandler<Runnable>)this.world.getChunkProvider().serverThreadQueue).executeAll();
                });
                wait.join();
            }
        } else {
            for (final QueueExecutorThread<ChunkTask> worker : this.workers) {
                worker.flush();
            }
        }
        if (globalUrgentWorker != null) globalUrgentWorker.flush();

        // flush again since tasks we execute async saves
        drainChunkWaitQueue();
        PaperFileIOThread.Holder.INSTANCE.flush();
    }

    public void close(final boolean wait) {
        // flush here since we schedule tasks on the IO thread that can schedule tasks to this task manager
        // we do this regardless of the wait param since after we invoke close no tasks can be queued
        PaperFileIOThread.Holder.INSTANCE.flush();

        if (this.workers == null) {
            if (wait) {
                this.flush();
            }
            return;
        }

        if (this.workers != globalWorkers) {
            for (final QueueExecutorThread<ChunkTask> worker : this.workers) {
                worker.close(false, this.perWorldQueue);
            }
        }

        if (wait) {
            this.flush();
        }
    }

    public void raisePriority(final int chunkX, final int chunkZ, final int priority) {
        final Long chunkKey = Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ));

        ChunkTask chunkSaveTask = this.chunkSaveTasks.get(chunkKey);
        if (chunkSaveTask != null) {
            // don't bump save into urgent queue
            raiseTaskPriority(chunkSaveTask, priority != PrioritizedTaskQueue.HIGHEST_PRIORITY ? priority : PrioritizedTaskQueue.HIGH_PRIORITY);
        }

        ChunkLoadTask chunkLoadTask = this.chunkLoadTasks.get(chunkKey);
        if (chunkLoadTask != null) {
            raiseTaskPriority(chunkLoadTask, priority);
        }
    }

    private void raiseTaskPriority(ChunkTask task, int priority) {
        final boolean raised = task.raisePriority(priority);
        if (task.isScheduled() && raised && this.workers != null) {
            // only notify if we're in queue to be executed
            if (priority == PrioritizedTaskQueue.HIGHEST_PRIORITY) {
                // was in another queue but became urgent later, add to urgent queue and the previous
                // queue will just have to ignore this task if it has already been started.
                // Ultimately, we now have 2 potential queues that can pull it out whoever gets it first
                // but the urgent queue has dedicated thread(s) so it's likely to win....
                globalUrgentQueue.add(task);
                this.internalScheduleNotifyUrgent();
            } else {
                this.internalScheduleNotify();
            }
        }
    }

    protected void internalSchedule(final ChunkTask task) {
        if (this.workers == null) {
            this.chunkTasks.add(task);
            return;
        }

        // It's important we order the task to be executed before notifying. Avoid a race condition where the worker thread
        // wakes up and goes to sleep before we actually schedule (or it's just about to sleep)
        if (task.getPriority() == PrioritizedTaskQueue.HIGHEST_PRIORITY) {
            globalUrgentQueue.add(task);
            this.internalScheduleNotifyUrgent();
        } else {
            this.queue.add(task);
            this.internalScheduleNotify();
        }

    }

    protected void internalScheduleNotify() {
        if (this.workers == null) {
            return;
        }
        for (final QueueExecutorThread<ChunkTask> worker : this.workers) {
            if (worker.notifyTasks()) {
                // break here since we only want to wake up one worker for scheduling one task
                break;
            }
        }
    }


    protected void internalScheduleNotifyUrgent() {
        if (globalUrgentWorker == null) {
            return;
        }
        globalUrgentWorker.notifyTasks();
    }

}
