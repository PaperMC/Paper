package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap; // Paper
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LightEngineThreaded extends LightEngine implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ThreadedMailbox<Runnable> b;
    // Paper start
    private static final int MAX_PRIORITIES = PlayerChunkMap.GOLDEN_TICKET + 2;

    private boolean isChunkLightStatus(long pair) {
        PlayerChunk playerChunk = playerChunkMap.getVisibleChunk(pair);
        if (playerChunk == null) {
            return false;
        }
        ChunkStatus status = PlayerChunk.getChunkStatus(playerChunk.getTicketLevel());
        return status != null && status.isAtLeastStatus(ChunkStatus.LIGHT);
    }

    static class ChunkLightQueue {
        public boolean shouldFastUpdate;
        java.util.ArrayDeque<Runnable> pre = new java.util.ArrayDeque<Runnable>();
        java.util.ArrayDeque<Runnable> post = new java.util.ArrayDeque<Runnable>();

        ChunkLightQueue(long chunk) {}
    }

    static class PendingLightTask {
        long chunkId;
        IntSupplier priority;
        Runnable pre;
        Runnable post;
        boolean fastUpdate;

        public PendingLightTask(long chunkId, IntSupplier priority, Runnable pre, Runnable post, boolean fastUpdate) {
            this.chunkId = chunkId;
            this.priority = priority;
            this.pre = pre;
            this.post = post;
            this.fastUpdate = fastUpdate;
        }
    }


    // Retain the chunks priority level for queued light tasks
    class LightQueue {
        private int size = 0;
        private final Long2ObjectLinkedOpenHashMap<ChunkLightQueue>[] buckets = new Long2ObjectLinkedOpenHashMap[MAX_PRIORITIES];
        private final java.util.concurrent.ConcurrentLinkedQueue<PendingLightTask> pendingTasks = new java.util.concurrent.ConcurrentLinkedQueue<>();
        private final java.util.concurrent.ConcurrentLinkedQueue<Runnable> priorityChanges = new java.util.concurrent.ConcurrentLinkedQueue<>();

        private LightQueue() {
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new Long2ObjectLinkedOpenHashMap<>();
            }
        }

        public void changePriority(long pair, int currentPriority, int priority) {
            this.priorityChanges.add(() -> {
                ChunkLightQueue remove = this.buckets[currentPriority].remove(pair);
                if (remove != null) {
                    ChunkLightQueue existing = this.buckets[priority].put(pair, remove);
                    if (existing != null) {
                        remove.pre.addAll(existing.pre);
                        remove.post.addAll(existing.post);
                    }
                }
            });
        }

        public final void addChunk(long chunkId, IntSupplier priority, Runnable pre, Runnable post) {
            pendingTasks.add(new PendingLightTask(chunkId, priority, pre, post, true));
            queueUpdate();
        }

        public final void add(long chunkId, IntSupplier priority, LightEngineThreaded.Update type, Runnable run) {
            pendingTasks.add(new PendingLightTask(chunkId, priority, type == Update.PRE_UPDATE ? run : null, type == Update.POST_UPDATE ? run : null, false));
        }
        public final void add(PendingLightTask update) {
            int priority = update.priority.getAsInt();
            ChunkLightQueue lightQueue = this.buckets[priority].computeIfAbsent(update.chunkId, ChunkLightQueue::new);

            if (update.pre != null) {
                this.size++;
                lightQueue.pre.add(update.pre);
            }
            if (update.post != null) {
                this.size++;
                lightQueue.post.add(update.post);
            }
            if (update.fastUpdate) {
                lightQueue.shouldFastUpdate = true;
            }
        }

        public final boolean isEmpty() {
            return this.size == 0 && this.pendingTasks.isEmpty();
        }

        public final int size() {
            return this.size;
        }

        public boolean poll(java.util.List<Runnable> pre, java.util.List<Runnable> post) {
            PendingLightTask pending;
            while ((pending = pendingTasks.poll()) != null) {
                add(pending);
            }
            Runnable run;
            while ((run = priorityChanges.poll()) != null) {
                run.run();
            }
            boolean hasWork = false;
            Long2ObjectLinkedOpenHashMap<ChunkLightQueue>[] buckets = this.buckets;
            int lowestPriority = 0;
            while (lowestPriority < MAX_PRIORITIES && !isEmpty()) {
                Long2ObjectLinkedOpenHashMap<ChunkLightQueue> bucket = buckets[lowestPriority];
                if (bucket.isEmpty()) {
                    lowestPriority++;
                    if (hasWork && lowestPriority <= 5) {
                        return true;
                    } else {
                        continue;
                    }
                }
                ChunkLightQueue queue = bucket.removeFirst();
                this.size -= queue.pre.size() + queue.post.size();
                pre.addAll(queue.pre);
                post.addAll(queue.post);
                queue.pre.clear();
                queue.post.clear();
                hasWork = true;
                if (queue.shouldFastUpdate) {
                    return true;
                }
            }
            return hasWork;
        }
    }

    final LightQueue queue = new LightQueue();
    // Paper end
    private final PlayerChunkMap d; private final PlayerChunkMap playerChunkMap; // Paper
    private final Mailbox<ChunkTaskQueueSorter.a<Runnable>> e;
    private volatile int f = 5;
    private final AtomicBoolean g = new AtomicBoolean();

    public LightEngineThreaded(ILightAccess ilightaccess, PlayerChunkMap playerchunkmap, boolean flag, ThreadedMailbox<Runnable> threadedmailbox, Mailbox<ChunkTaskQueueSorter.a<Runnable>> mailbox) {
        super(ilightaccess, true, flag);
        this.d = playerchunkmap; this.playerChunkMap = d; // Paper
        this.e = mailbox;
        this.b = threadedmailbox;
    }

    public void close() {}

    @Override
    public int a(int i, boolean flag, boolean flag1) {
        throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("Ran authomatically on a different thread!")));
    }

    @Override
    public void a(BlockPosition blockposition, int i) {
        throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("Ran authomatically on a different thread!")));
    }

    @Override
    public void a(BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.immutableCopy();

        this.a(blockposition.getX() >> 4, blockposition.getZ() >> 4, LightEngineThreaded.Update.POST_UPDATE, SystemUtils.a(() -> {
            super.a(blockposition1);
        }, () -> {
            return "checkBlock " + blockposition1;
        }));
    }

    protected void a(ChunkCoordIntPair chunkcoordintpair) {
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.b(chunkcoordintpair, false);
            super.a(chunkcoordintpair, false);

            int i;

            for (i = -1; i < 17; ++i) {
                super.a(EnumSkyBlock.BLOCK, SectionPosition.a(chunkcoordintpair, i), (NibbleArray) null, true);
                super.a(EnumSkyBlock.SKY, SectionPosition.a(chunkcoordintpair, i), (NibbleArray) null, true);
            }

            for (i = 0; i < 16; ++i) {
                super.a(SectionPosition.a(chunkcoordintpair, i), true);
            }

        }, () -> {
            return "updateChunkStatus " + chunkcoordintpair + " " + true;
        }));
    }

    @Override
    public void a(SectionPosition sectionposition, boolean flag) {
        this.a(sectionposition.a(), sectionposition.c(), () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(sectionposition, flag);
        }, () -> {
            return "updateSectionStatus " + sectionposition + " " + flag;
        }));
    }

    @Override
    public void a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(chunkcoordintpair, flag);
        }, () -> {
            return "enableLight " + chunkcoordintpair + " " + flag;
        }));
    }

    @Override
    public void a(EnumSkyBlock enumskyblock, SectionPosition sectionposition, @Nullable NibbleArray nibblearray, boolean flag) {
        this.a(sectionposition.a(), sectionposition.c(), () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.a(enumskyblock, sectionposition, nibblearray, flag);
        }, () -> {
            return "queueData " + sectionposition;
        }));
    }

    private void a(int i, int j, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) {
        this.a(i, j, this.d.c(ChunkCoordIntPair.pair(i, j)), lightenginethreaded_update, runnable);
    }

    private void a(int i, int j, IntSupplier intsupplier, LightEngineThreaded.Update lightenginethreaded_update, Runnable runnable) {
        // Paper start - replace method
        this.queue.add(ChunkCoordIntPair.pair(i, j), intsupplier, lightenginethreaded_update, runnable);
        // Paper end
    }

    @Override
    public void b(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        this.a(chunkcoordintpair.x, chunkcoordintpair.z, () -> {
            return 0;
        }, LightEngineThreaded.Update.PRE_UPDATE, SystemUtils.a(() -> {
            super.b(chunkcoordintpair, flag);
        }, () -> {
            return "retainData " + chunkcoordintpair;
        }));
    }

    public CompletableFuture<IChunkAccess> a(IChunkAccess ichunkaccess, boolean flag) {
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();

        // Paper start
        //ichunkaccess.b(false); // Don't need to disable this
        long pair = chunkcoordintpair.pair();
        CompletableFuture<IChunkAccess> future = new CompletableFuture<>();
        IntSupplier prioritySupplier = playerChunkMap.getPrioritySupplier(pair);
        boolean[] skippedPre = {false};
        this.queue.addChunk(pair, prioritySupplier, SystemUtils.a(() -> {
            if (!isChunkLightStatus(pair)) {
                future.complete(ichunkaccess);
                skippedPre[0] = true;
                return;
            }
            // Paper end
            ChunkSection[] achunksection = ichunkaccess.getSections();

            for (int i = 0; i < 16; ++i) {
                ChunkSection chunksection = achunksection[i];

                if (!ChunkSection.a(chunksection)) {
                    super.a(SectionPosition.a(chunkcoordintpair, i), false);
                }
            }

            super.a(chunkcoordintpair, true);
            if (!flag) {
                ichunkaccess.m().forEach((blockposition) -> {
                    super.a(blockposition, ichunkaccess.g(blockposition));
                });
            }

            // this.d.c(chunkcoordintpair); // Paper - move into post task below
        }, () -> {
            return "lightChunk " + chunkcoordintpair + " " + flag;
            // Paper start  - merge the 2 together
        }), () -> {
            this.d.c(chunkcoordintpair); // Paper - release light tickets as post task to ensure they stay loaded until fully done
            if (skippedPre[0]) return; // Paper - future's already complete
            ichunkaccess.b(true);
            super.b(chunkcoordintpair, false);
            // Paper start
            future.complete(ichunkaccess);
        });
        return future;
        // Paper end
    }

    public void queueUpdate() {
        if ((!this.queue.isEmpty() || super.a()) && this.g.compareAndSet(false, true)) { // Paper
            this.b.a((() -> { // Paper - decompile error
                this.b();
                this.g.set(false);
                queueUpdate(); // Paper - if we still have work to do, do it!
            }));
        }

    }

    // Paper start - replace impl
    private final java.util.List<Runnable> pre = new java.util.ArrayList<>();
    private final java.util.List<Runnable> post = new java.util.ArrayList<>();
    private void b() {
        if (queue.poll(pre, post)) {
            pre.forEach(Runnable::run);
            pre.clear();
            super.a(Integer.MAX_VALUE, true, true);
            post.forEach(Runnable::run);
            post.clear();
        } else {
            // might have level updates to go still
            super.a(Integer.MAX_VALUE, true, true);
        }
        // Paper end
    }

    public void a(int i) {
        this.f = i;
    }

    static enum Update {

        PRE_UPDATE, POST_UPDATE;

        private Update() {}
    }
}
