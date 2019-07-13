package com.destroystokyo.paper.io;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.RegionFile;
import net.minecraft.server.WorldServer;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Prioritized singleton thread responsible for all chunk IO that occurs in a minecraft server.
 *
 * <p>
 *    Singleton access: {@link Holder#INSTANCE}
 * </p>
 *
 * <p>
 *     All functions provided are MT-Safe, however certain ordering constraints are (but not enforced):
 *     <li>
 *         Chunk saves may not occur for unloaded chunks.
 *     </li>
 *     <li>
 *         Tasks must be scheduled on the main thread.
 *     </li>
 * </p>
 *
 * @see Holder#INSTANCE
 * @see #scheduleSave(WorldServer, int, int, NBTTagCompound, NBTTagCompound, int)
 * @see #loadChunkDataAsync(WorldServer, int, int, int, Consumer, boolean, boolean, boolean)
 */
public final class PaperFileIOThread extends QueueExecutorThread {

    public static final Logger LOGGER = MinecraftServer.LOGGER;
    public static final NBTTagCompound FAILURE_VALUE = new NBTTagCompound();

    public static final class Holder {

        public static final PaperFileIOThread INSTANCE = new PaperFileIOThread();

        static {
            INSTANCE.start();
        }
    }

    private final AtomicLong writeCounter = new AtomicLong();

    private PaperFileIOThread() {
        super(new PrioritizedTaskQueue<>(), (int)(1.0e6)); // 1.0ms spinwait time
        this.setName("Paper RegionFile IO Thread");
        this.setPriority(Thread.NORM_PRIORITY - 1); // we keep priority close to normal because threads can wait on us
        this.setUncaughtExceptionHandler((final Thread unused, final Throwable thr) -> {
            LOGGER.fatal("Uncaught exception thrown from IO thread, report this!", thr);
        });
    }

    /* run() is implemented by superclass */

    /*
     *
     * IO thread will perform reads before writes
     *
     * How reads/writes are scheduled:
     *
     * If read in progress while scheduling write, ignore read and schedule write
     * If read in progress while scheduling read (no write in progress), chain the read task
     *
     *
     * If write in progress while scheduling read, use the pending write data and ret immediately
     * If write in progress while scheduling write (ignore read in progress), overwrite the write in progress data
     *
     * This allows the reads and writes to act as if they occur synchronously to the thread scheduling them, however
     * it fails to properly propagate write failures. When writes fail the data is kept so future reads will actually
     * read the failed write data. This should hopefully act as a way to prevent data loss for spurious fails for writing data.
     *
     */

    /**
     * Attempts to bump the priority of all IO tasks for the given chunk coordinates. This has no effect if no tasks are queued.
     * @param world Chunk's world
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @param priority Priority level to try to bump to
     */
    public void bumpPriority(final WorldServer world, final int chunkX, final int chunkZ, final int priority) {
        if (!PrioritizedTaskQueue.validPriority(priority)) {
            throw new IllegalArgumentException("Invalid priority: " + priority);
        }

        final Long key = Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ));

        final ChunkDataTask poiTask = world.poiDataController.tasks.get(key);
        final ChunkDataTask chunkTask = world.chunkDataController.tasks.get(key);

        if (poiTask != null) {
            poiTask.raisePriority(priority);
        }
        if (chunkTask != null) {
            chunkTask.raisePriority(priority);
        }
    }

    public NBTTagCompound getPendingWrite(final WorldServer world, final int chunkX, final int chunkZ, final boolean poiData) {
        final ChunkDataController taskController = poiData ? world.poiDataController : world.chunkDataController;

        final ChunkDataTask dataTask = taskController.tasks.get(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)));

        if (dataTask == null) {
            return null;
        }

        final ChunkDataController.InProgressWrite write = dataTask.inProgressWrite;

        if (write == null) {
            return null;
        }

        return write.data;
    }

    /**
     * Sets the priority of all IO tasks for the given chunk coordinates. This has no effect if no tasks are queued.
     * @param world Chunk's world
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @param priority Priority level to set to
     */
    public void setPriority(final WorldServer world, final int chunkX, final int chunkZ, final int priority) {
        if (!PrioritizedTaskQueue.validPriority(priority)) {
            throw new IllegalArgumentException("Invalid priority: " + priority);
        }

        final Long key = Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ));

        final ChunkDataTask poiTask = world.poiDataController.tasks.get(key);
        final ChunkDataTask chunkTask = world.chunkDataController.tasks.get(key);

        if (poiTask != null) {
            poiTask.updatePriority(priority);
        }
        if (chunkTask != null) {
            chunkTask.updatePriority(priority);
        }
    }

    /**
     * Schedules the chunk data to be written asynchronously.
     * <p>
     *     Impl notes:
     * </p>
     * <li>
     *     This function presumes a chunk load for the coordinates is not called during this function (anytime after is OK). This means
     *     saves must be scheduled before a chunk is unloaded.
     * </li>
     * <li>
     *     Writes may be called concurrently, although only the "later" write will go through.
     * </li>
     * @param world Chunk's world
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @param poiData Chunk point of interest data. If {@code null}, then no poi data is saved.
     * @param chunkData Chunk data. If {@code null}, then no chunk data is saved.
     * @param priority Priority level for this task. See {@link PrioritizedTaskQueue}
     * @throws IllegalArgumentException If both {@code poiData} and {@code chunkData} are {@code null}.
     * @throws IllegalStateException If the file io thread has shutdown.
     */
    public void scheduleSave(final WorldServer world, final int chunkX, final int chunkZ,
                             final NBTTagCompound poiData, final NBTTagCompound chunkData,
                             final int priority) throws IllegalArgumentException {
        if (!PrioritizedTaskQueue.validPriority(priority)) {
            throw new IllegalArgumentException("Invalid priority: " + priority);
        }

        final long writeCounter = this.writeCounter.getAndIncrement();

        if (poiData != null) {
            this.scheduleWrite(world.poiDataController, world, chunkX, chunkZ, poiData, priority, writeCounter);
        }
        if (chunkData != null) {
            this.scheduleWrite(world.chunkDataController, world, chunkX, chunkZ, chunkData, priority, writeCounter);
        }
    }

    private void scheduleWrite(final ChunkDataController dataController, final WorldServer world,
                               final int chunkX, final int chunkZ, final NBTTagCompound data, final int priority, final long writeCounter) {
        dataController.tasks.compute(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)), (final Long keyInMap, final ChunkDataTask taskRunning) -> {
            if (taskRunning == null) {
                // no task is scheduled

                // create task
                final ChunkDataTask newTask = new ChunkDataTask(priority, world, chunkX, chunkZ, dataController);
                newTask.inProgressWrite = new ChunkDataController.InProgressWrite();
                newTask.inProgressWrite.writeCounter = writeCounter;
                newTask.inProgressWrite.data = data;

                PaperFileIOThread.this.queueTask(newTask); // schedule
                return newTask;
            }

            taskRunning.raisePriority(priority);

            if (taskRunning.inProgressWrite == null) {
                taskRunning.inProgressWrite = new ChunkDataController.InProgressWrite();
            }

            boolean reschedule = taskRunning.inProgressWrite.writeCounter == -1L;

            // synchronize for readers
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (taskRunning) {
                taskRunning.inProgressWrite.data = data;
                taskRunning.inProgressWrite.writeCounter = writeCounter;
            }

            if (reschedule) {
                // We need to reschedule this task since the previous one is not currently scheduled since it failed
                taskRunning.reschedule(priority);
            }

            return taskRunning;
        });
    }

    /**
     * Same as {@link #loadChunkDataAsync(WorldServer, int, int, int, Consumer, boolean, boolean, boolean)}, except this function returns
     * a {@link CompletableFuture} which is potentially completed <b>ASYNCHRONOUSLY ON THE FILE IO THREAD</b> when the load task
     * has completed.
     * <p>
     *     Note that if the chunk fails to load the returned future is completed with {@code null}.
     * </p>
     */
    public CompletableFuture<ChunkData> loadChunkDataAsyncFuture(final WorldServer world, final int chunkX, final int chunkZ,
                                                                 final int priority, final boolean readPoiData, final boolean readChunkData,
                                                                 final boolean intendingToBlock) {
        final CompletableFuture<ChunkData> future = new CompletableFuture<>();
        this.loadChunkDataAsync(world, chunkX, chunkZ, priority, future::complete, readPoiData, readChunkData, intendingToBlock);
        return future;
    }

    /**
     * Schedules a load to be executed asynchronously.
     * <p>
     *     Impl notes:
     * </p>
     * <li>
     *     If a chunk fails to load, the {@code onComplete} parameter is completed with {@code null}.
     * </li>
     * <li>
     *     It is possible for the {@code onComplete} parameter to be given {@link ChunkData} containing data
     *     this call did not request.
     * </li>
     * <li>
     *     The {@code onComplete} parameter may be completed during the execution of this function synchronously or it may
     *     be completed asynchronously on this file io thread. Interacting with the file IO thread in the completion of
     *     data is undefined behaviour, and can cause deadlock.
     * </li>
     * @param world Chunk's world
     * @param chunkX Chunk's x coordinate
     * @param chunkZ Chunk's z coordinate
     * @param priority Priority level for this task. See {@link PrioritizedTaskQueue}
     * @param onComplete Consumer to execute once this task has completed
     * @param readPoiData Whether to read point of interest data. If {@code false}, the {@code NBTTagCompound} will be {@code null}.
     * @param readChunkData Whether to read chunk data. If {@code false}, the {@code NBTTagCompound} will be {@code null}.
     * @return The {@link PrioritizedTaskQueue.PrioritizedTask} associated with this task. Note that this task does not support
     *                                                          cancellation.
     */
    public void loadChunkDataAsync(final WorldServer world, final int chunkX, final int chunkZ,
                                   final int priority, final Consumer<ChunkData> onComplete,
                                   final boolean readPoiData, final boolean readChunkData,
                                   final boolean intendingToBlock) {
        if (!PrioritizedTaskQueue.validPriority(priority)) {
            throw new IllegalArgumentException("Invalid priority: " + priority);
        }

        if (!(readPoiData | readChunkData)) {
            throw new IllegalArgumentException("Must read chunk data or poi data");
        }

        final ChunkData complete = new ChunkData();
        final boolean[] requireCompletion = new boolean[] { readPoiData, readChunkData };

        if (readPoiData) {
            this.scheduleRead(world.poiDataController, world, chunkX, chunkZ, (final NBTTagCompound poiData) -> {
                complete.poiData = poiData;

                final boolean finished;

                // avoid a race condition where the file io thread completes and we complete synchronously
                // Note: Synchronization can be elided if both of the accesses are volatile
                synchronized (requireCompletion) {
                    requireCompletion[0] = false; // 0 -> poi data
                    finished = !requireCompletion[1]; // 1 -> chunk data
                }

                if (finished) {
                    onComplete.accept(complete);
                }
            }, priority, intendingToBlock);
        }

        if (readChunkData) {
            this.scheduleRead(world.chunkDataController, world, chunkX, chunkZ, (final NBTTagCompound chunkData) -> {
                complete.chunkData = chunkData;

                final boolean finished;

                // avoid a race condition where the file io thread completes and we complete synchronously
                // Note: Synchronization can be elided if both of the accesses are volatile
                synchronized (requireCompletion) {
                    requireCompletion[1] = false; // 1 -> chunk data
                    finished = !requireCompletion[0]; // 0 -> poi data
                }

                if (finished) {
                    onComplete.accept(complete);
                }
            }, priority, intendingToBlock);
        }

    }

    // Note: the onComplete may be called asynchronously or synchronously here.
    private void scheduleRead(final ChunkDataController dataController, final WorldServer world,
                              final int chunkX, final int chunkZ, final Consumer<NBTTagCompound> onComplete, final int priority,
                              final boolean intendingToBlock) {

        Function<RegionFile, Boolean> tryLoadFunction = (final RegionFile file) -> {
            if (file == null) {
                return Boolean.TRUE;
            }
            return Boolean.valueOf(file.chunkExists(new ChunkCoordIntPair(chunkX, chunkZ)));
        };

        dataController.tasks.compute(Long.valueOf(IOUtil.getCoordinateKey(chunkX, chunkZ)), (final Long keyInMap, final ChunkDataTask running) -> {
            if (running == null) {
                // not scheduled

                final Boolean shouldSchedule = intendingToBlock ? dataController.computeForRegionFile(chunkX, chunkZ, tryLoadFunction) :
                    dataController.computeForRegionFileIfLoaded(chunkX, chunkZ, tryLoadFunction);

                if (shouldSchedule == Boolean.FALSE) {
                    // not on disk
                    onComplete.accept(null);
                    return null;
                }

                // set up task
                final ChunkDataTask newTask = new ChunkDataTask(priority, world, chunkX, chunkZ, dataController);
                newTask.inProgressRead = new ChunkDataController.InProgressRead();
                newTask.inProgressRead.readFuture.thenAccept(onComplete);

                PaperFileIOThread.this.queueTask(newTask); // schedule task
                return newTask;
            }

            running.raisePriority(priority);

            if (running.inProgressWrite == null) {
                // chain to the read future
                running.inProgressRead.readFuture.thenAccept(onComplete);
                return running;
            }

            // at this stage we have to use the in progress write's data to avoid an order issue
            // we don't synchronize since all writes to data occur in the compute() call
            onComplete.accept(running.inProgressWrite.data);
            return running;
        });
    }

    /**
     * Same as {@link #loadChunkDataAsync(WorldServer, int, int, int, Consumer, boolean, boolean, boolean)}, except this function returns
     * the {@link ChunkData} associated with the specified chunk when the task is complete.
     * @return The chunk data, or {@code null} if the chunk failed to load.
     */
    public ChunkData loadChunkData(final WorldServer world, final int chunkX, final int chunkZ, final int priority,
                                   final boolean readPoiData, final boolean readChunkData) {
        return this.loadChunkDataAsyncFuture(world, chunkX, chunkZ, priority, readPoiData, readChunkData, true).join();
    }

    /**
     * Schedules the given task at the specified priority to be executed on the IO thread.
     * <p>
     *     Internal api. Do not use.
     * </p>
     */
    public void runTask(final int priority, final Runnable runnable) {
        this.queueTask(new GeneralTask(priority, runnable));
    }

    static final class GeneralTask extends PrioritizedTaskQueue.PrioritizedTask implements Runnable {

        private final Runnable run;

        public GeneralTask(final int priority, final Runnable run) {
            super(priority);
            this.run = IOUtil.notNull(run, "Task may not be null");
        }

        @Override
        public void run() {
            try {
                this.run.run();
            } catch (final Throwable throwable) {
                if (throwable instanceof ThreadDeath) {
                    throw (ThreadDeath)throwable;
                }
                LOGGER.fatal("Failed to execute general task on IO thread " + IOUtil.genericToString(this.run), throwable);
            }
        }
    }

    public static final class ChunkData {

        public NBTTagCompound poiData;
        public NBTTagCompound chunkData;

        public ChunkData() {}

        public ChunkData(final NBTTagCompound poiData, final NBTTagCompound chunkData) {
            this.poiData = poiData;
            this.chunkData = chunkData;
        }
    }

    public static abstract class ChunkDataController {

        // ConcurrentHashMap synchronizes per chain, so reduce the chance of task's hashes colliding.
        public final ConcurrentHashMap<Long, ChunkDataTask> tasks = new ConcurrentHashMap<>(64, 0.5f);

        public abstract void writeData(final int x, final int z, final NBTTagCompound compound) throws IOException;
        public abstract NBTTagCompound readData(final int x, final int z) throws IOException;

        public abstract <T> T computeForRegionFile(final int chunkX, final int chunkZ, final Function<RegionFile, T> function);
        public abstract <T> T computeForRegionFileIfLoaded(final int chunkX, final int chunkZ, final Function<RegionFile, T> function);

        public static final class InProgressWrite {
            public long writeCounter;
            public NBTTagCompound data;
        }

        public static final class InProgressRead {
            public final CompletableFuture<NBTTagCompound> readFuture = new CompletableFuture<>();
        }
    }

    public static final class ChunkDataTask extends PrioritizedTaskQueue.PrioritizedTask implements Runnable {

        public ChunkDataController.InProgressWrite inProgressWrite;
        public ChunkDataController.InProgressRead inProgressRead;

        private final WorldServer world;
        private final int x;
        private final int z;
        private final ChunkDataController taskController;

        public ChunkDataTask(final int priority, final WorldServer world, final int x, final int z, final ChunkDataController taskController) {
            super(priority);
            this.world = world;
            this.x = x;
            this.z = z;
            this.taskController = taskController;
        }

        @Override
        public String toString() {
            return "Task for world: '" + this.world.getWorld().getName() + "' at " + this.x + "," + this.z +
                " poi: " + (this.taskController == this.world.poiDataController) + ", hash: " + this.hashCode();
        }

        /*
         *
         * IO thread will perform reads before writes
         *
         * How reads/writes are scheduled:
         *
         * If read in progress while scheduling write, ignore read and schedule write
         * If read in progress while scheduling read (no write in progress), chain the read task
         *
         *
         * If write in progress while scheduling read, use the pending write data and ret immediately
         * If write in progress while scheduling write (ignore read in progress), overwrite the write in progress data
         *
         * This allows the reads and writes to act as if they occur synchronously to the thread scheduling them, however
         * it fails to properly propagate write failures
         *
         */

        void reschedule(final int priority) {
            // priority is checked before this stage // TODO what
            this.queue.lazySet(null);
            this.priority.lazySet(priority);
            PaperFileIOThread.Holder.INSTANCE.queueTask(this);
        }

        @Override
        public void run() {
            ChunkDataController.InProgressRead read = this.inProgressRead;
            if (read != null) {
                NBTTagCompound compound = PaperFileIOThread.FAILURE_VALUE;
                try {
                    compound = this.taskController.readData(this.x, this.z);
                } catch (final Throwable thr) {
                    if (thr instanceof ThreadDeath) {
                        throw (ThreadDeath)thr;
                    }
                    LOGGER.fatal("Failed to read chunk data for task: " + this.toString(), thr);
                    // fall through to complete with null data
                }
                read.readFuture.complete(compound);
            }

            final Long chunkKey = Long.valueOf(IOUtil.getCoordinateKey(this.x, this.z));

            ChunkDataController.InProgressWrite write = this.inProgressWrite;

            if (write == null) {
                // IntelliJ warns this is invalid, however it does not consider that writes to the task map & the inProgress field can occur concurrently.
                ChunkDataTask inMap = this.taskController.tasks.compute(chunkKey, (final Long keyInMap, final ChunkDataTask valueInMap) -> {
                    if (valueInMap == null) {
                        throw new IllegalStateException("Write completed concurrently, expected this task: " + ChunkDataTask.this.toString() + ", report this!");
                    }
                    if (valueInMap != ChunkDataTask.this) {
                        throw new IllegalStateException("Chunk task mismatch, expected this task: " + ChunkDataTask.this.toString() + ", got: " + valueInMap.toString() + ", report this!");
                    }
                    return valueInMap.inProgressWrite == null ? null : valueInMap;
                });

                if (inMap == null) {
                    return; // set the task value to null, indicating we're done
                }

                // not null, which means there was a concurrent write
                write = this.inProgressWrite;
            }

            // check if another process is writing
            /*try { TODO: Can we restore this?
                ((WorldServer)this.world).checkSession();
            } catch (final Exception ex) {
                LOGGER.fatal("Couldn't save chunk; already in use by another instance of Minecraft?", ex);
                // we don't need to set the write counter to -1 as we know at this stage there's no point in re-scheduling
                // writes since they'll fail anyways.
                return;
            }
*/
            for (;;) {
                final long writeCounter;
                final NBTTagCompound data;

                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (write) {
                    writeCounter = write.writeCounter;
                    data = write.data;
                }

                boolean failedWrite = false;

                try {
                    this.taskController.writeData(this.x, this.z, data);
                } catch (final Throwable thr) {
                    if (thr instanceof ThreadDeath) {
                        throw (ThreadDeath)thr;
                    }
                    LOGGER.fatal("Failed to write chunk data for task: " + this.toString(), thr);
                    failedWrite = true;
                }

                boolean finalFailWrite = failedWrite;

                ChunkDataTask inMap = this.taskController.tasks.compute(chunkKey, (final Long keyInMap, final ChunkDataTask valueInMap) -> {
                    if (valueInMap == null) {
                        throw new IllegalStateException("Write completed concurrently, expected this task: " + ChunkDataTask.this.toString() + ", report this!");
                    }
                    if (valueInMap != ChunkDataTask.this) {
                        throw new IllegalStateException("Chunk task mismatch, expected this task: " + ChunkDataTask.this.toString() + ", got: " + valueInMap.toString() + ", report this!");
                    }
                    if (valueInMap.inProgressWrite.writeCounter == writeCounter) {
                        if (finalFailWrite) {
                            valueInMap.inProgressWrite.writeCounter = -1L;
                        }

                        return null;
                    }
                    return valueInMap;
                    // Hack end
                });

                if (inMap == null) {
                    // write counter matched, so we wrote the most up-to-date pending data, we're done here
                    // or we failed to write and successfully set the write counter to -1
                    return; // we're done here
                }

                // fetch & write new data
                continue;
            }
        }
    }
}
