package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spigotmc.AsyncCatcher; // Paper

public abstract class ChunkMapDistance {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int b = 33 + ChunkStatus.a(ChunkStatus.FULL) - 2;
    private final Long2ObjectMap<ObjectSet<EntityPlayer>> c = new Long2ObjectOpenHashMap();
    public final Long2ObjectOpenHashMap<ArraySetSorted<Ticket<?>>> tickets = new Long2ObjectOpenHashMap();
    private final ChunkMapDistance.a ticketLevelTracker = new ChunkMapDistance.a();
    public static final int MOB_SPAWN_RANGE = 8; // private final ChunkMapDistance.b f = new ChunkMapDistance.b(8); // Paper - no longer used
    private final ChunkMapDistance.c g = new ChunkMapDistance.c(33);
    // Paper start use a queue, but still keep unique requirement
    public final java.util.Queue<PlayerChunk> pendingChunkUpdates = new java.util.ArrayDeque<PlayerChunk>() {
        @Override
        public boolean add(PlayerChunk o) {
            if (o.isUpdateQueued) return true;
            o.isUpdateQueued = true;
            return super.add(o);
        }
    };
    // Paper end
    private final ChunkTaskQueueSorter i;
    private final Mailbox<ChunkTaskQueueSorter.a<Runnable>> j;
    private final Mailbox<ChunkTaskQueueSorter.b> k;
    private final LongSet l = new LongOpenHashSet(); public final LongSet getOnPlayerTicketAddQueue() { return l; } // Paper - OBFHELPER
    private final Executor m;
    private long currentTick;

    PlayerChunkMap chunkMap; // Paper

    protected ChunkMapDistance(Executor executor, Executor executor1) {
        executor1.getClass();
        Mailbox<Runnable> mailbox = Mailbox.a("player ticket throttler", executor1::execute);
        ChunkTaskQueueSorter chunktaskqueuesorter = new ChunkTaskQueueSorter(ImmutableList.of(mailbox), executor, 4);

        this.i = chunktaskqueuesorter;
        this.j = chunktaskqueuesorter.a(mailbox, true);
        this.k = chunktaskqueuesorter.a(mailbox);
        this.m = executor1;
    }

    protected void purgeTickets() {
        ++this.currentTick;
        ObjectIterator objectiterator = this.tickets.long2ObjectEntrySet().fastIterator();

        while (objectiterator.hasNext()) {
            Entry<ArraySetSorted<Ticket<?>>> entry = (Entry) objectiterator.next();

            if ((entry.getValue()).removeIf((ticket) -> { // CraftBukkit - decompile error
                return ticket.b(this.currentTick);
            })) {
                this.ticketLevelTracker.update(entry.getLongKey(), getLowestTicketLevel((ArraySetSorted) entry.getValue()), false);
            }

            if (((ArraySetSorted) entry.getValue()).isEmpty()) {
                objectiterator.remove();
            }
        }

    }

    private static int getLowestTicketLevel(ArraySetSorted<Ticket<?>> arraysetsorted) {
        AsyncCatcher.catchOp("ChunkMapDistance::getLowestTicketLevel"); // Paper
        return !arraysetsorted.isEmpty() ? ((Ticket) arraysetsorted.b()).b() : PlayerChunkMap.GOLDEN_TICKET + 1;
    }

    protected abstract boolean a(long i);

    @Nullable
    protected abstract PlayerChunk b(long i);

    @Nullable
    protected abstract PlayerChunk a(long i, int j, @Nullable PlayerChunk playerchunk, int k);

    public boolean a(PlayerChunkMap playerchunkmap) {
        //this.f.a(); // Paper - no longer used
        AsyncCatcher.catchOp("DistanceManagerTick"); // Paper
        this.g.a();
        int i = Integer.MAX_VALUE - this.ticketLevelTracker.a(Integer.MAX_VALUE);
        boolean flag = i != 0;

        if (flag) {
            ;
        }

        // Paper start
        if (!this.pendingChunkUpdates.isEmpty()) {
            this.pollingPendingChunkUpdates = true; try {
            while(!this.pendingChunkUpdates.isEmpty()) {
                PlayerChunk remove = this.pendingChunkUpdates.remove();
                remove.isUpdateQueued = false;
                remove.a(playerchunkmap);
            }
            } finally { this.pollingPendingChunkUpdates = false; }
            // Paper end
            return true;
        } else {
            if (!this.l.isEmpty()) {
                LongIterator longiterator = this.l.iterator();

                while (longiterator.hasNext()) {
                    long j = longiterator.nextLong();

                    if (this.e(j).stream().anyMatch((ticket) -> {
                        return ticket.getTicketType() == TicketType.PLAYER;
                    })) {
                        PlayerChunk playerchunk = playerchunkmap.getUpdatingChunk(j);

                        if (playerchunk == null) {
                            throw new IllegalStateException();
                        }

                        CompletableFuture<Either<Chunk, PlayerChunk.Failure>> completablefuture = playerchunk.b();

                        completablefuture.thenAccept((either) -> {
                            this.m.execute(() -> {
                                this.k.a(ChunkTaskQueueSorter.a(() -> {
                                }, j, false));
                            });
                        });
                    }
                }

                this.l.clear();
            }

            return flag;
        }
    }
    boolean pollingPendingChunkUpdates = false; // Paper

    private boolean addTicket(long i, Ticket<?> ticket) { // CraftBukkit - void -> boolean
        AsyncCatcher.catchOp("ChunkMapDistance::addTicket"); // Paper
        ArraySetSorted<Ticket<?>> arraysetsorted = this.e(i);
        int j = getLowestTicketLevel(arraysetsorted);
        Ticket<?> ticket1 = (Ticket) arraysetsorted.a(ticket); // CraftBukkit - decompile error

        ticket1.a(this.currentTick);
        if (ticket.b() < j) {
            this.ticketLevelTracker.update(i, ticket.b(), true);
        }

        return ticket == ticket1; // CraftBukkit
    }

    private boolean removeTicket(long i, Ticket<?> ticket) { // CraftBukkit - void -> boolean
        AsyncCatcher.catchOp("ChunkMapDistance::removeTicket"); // Paper
        ArraySetSorted<Ticket<?>> arraysetsorted = this.e(i);
        int oldLevel = getLowestTicketLevel(arraysetsorted); // Paper

        boolean removed = false; // CraftBukkit
        if (arraysetsorted.remove(ticket)) {
            removed = true; // CraftBukkit
            // Paper start - delay chunk unloads for player tickets
            long delayChunkUnloadsBy = chunkMap.world.paperConfig.delayChunkUnloadsBy;
            if (ticket.getTicketType() == TicketType.PLAYER && delayChunkUnloadsBy > 0) {
                boolean hasPlayer = false;
                for (Ticket<?> ticket1 : arraysetsorted) {
                    if (ticket1.getTicketType() == TicketType.PLAYER) {
                        hasPlayer = true;
                        break;
                    }
                }
                PlayerChunk playerChunk = chunkMap.getUpdatingChunk(i);
                if (!hasPlayer && playerChunk != null && playerChunk.isFullChunkReady()) {
                    Ticket<Long> delayUnload = new Ticket<Long>(TicketType.DELAY_UNLOAD, 33, i);
                    delayUnload.delayUnloadBy = delayChunkUnloadsBy;
                    delayUnload.setCurrentTick(this.currentTick);
                    arraysetsorted.remove(delayUnload);
                    // refresh ticket
                    arraysetsorted.add(delayUnload);
                }
            }
            // Paper end
        }

        if (arraysetsorted.isEmpty()) {
            this.tickets.remove(i);
        }

        int newLevel = getLowestTicketLevel(arraysetsorted); // Paper
        if (newLevel > oldLevel) this.ticketLevelTracker.update(i, newLevel, false); // Paper
        return removed; // CraftBukkit
    }

    public <T> void a(TicketType<T> tickettype, ChunkCoordIntPair chunkcoordintpair, int i, T t0) {
        // CraftBukkit start
        this.addTicketAtLevel(tickettype, chunkcoordintpair, i, t0);
    }

    // Paper start
    public static final int PRIORITY_TICKET_LEVEL = PlayerChunkMap.GOLDEN_TICKET;
    public static final int URGENT_PRIORITY = 29;
    public boolean delayDistanceManagerTick = false;
    public boolean markUrgent(ChunkCoordIntPair coords) {
        return addPriorityTicket(coords, TicketType.URGENT, URGENT_PRIORITY);
    }
    public boolean markHighPriority(ChunkCoordIntPair coords, int priority) {
        priority = Math.min(URGENT_PRIORITY - 1, Math.max(1, priority));
        return addPriorityTicket(coords, TicketType.PRIORITY, priority);
    }

    public void markAreaHighPriority(ChunkCoordIntPair center, int priority, int radius) {
        delayDistanceManagerTick = true;
        priority = Math.min(URGENT_PRIORITY - 1, Math.max(1, priority));
        int finalPriority = priority;
        MCUtil.getSpiralOutChunks(center.asPosition(), radius).forEach(coords -> {
            addPriorityTicket(coords, TicketType.PRIORITY, finalPriority);
        });
        delayDistanceManagerTick = false;
        chunkMap.world.getChunkProvider().tickDistanceManager();
    }

    public void clearAreaPriorityTickets(ChunkCoordIntPair center, int radius) {
        delayDistanceManagerTick = true;
        MCUtil.getSpiralOutChunks(center.asPosition(), radius).forEach(coords -> {
            this.removeTicket(coords.pair(), new Ticket<ChunkCoordIntPair>(TicketType.PRIORITY, PRIORITY_TICKET_LEVEL, coords));
        });
        delayDistanceManagerTick = false;
        chunkMap.world.getChunkProvider().tickDistanceManager();
    }

    private boolean hasPlayerTicket(ChunkCoordIntPair coords, int level) {
        ArraySetSorted<Ticket<?>> tickets = this.tickets.get(coords.pair());
        if (tickets == null || tickets.isEmpty()) {
            return false;
        }
        for (Ticket<?> ticket : tickets) {
            if (ticket.getTicketType() == TicketType.PLAYER && ticket.getTicketLevel() == level) {
                return true;
            }
        }

        return false;
    }

    private boolean addPriorityTicket(ChunkCoordIntPair coords, TicketType<ChunkCoordIntPair> ticketType, int priority) {
        AsyncCatcher.catchOp("ChunkMapDistance::addPriorityTicket");
        long pair = coords.pair();
        PlayerChunk chunk = chunkMap.getUpdatingChunk(pair);
        boolean needsTicket = chunkMap.playerViewDistanceNoTickMap.getObjectsInRange(pair) != null && !hasPlayerTicket(coords, 33);

        if (needsTicket) {
            Ticket<?> ticket = new Ticket<>(TicketType.PLAYER, 33, coords);
            getOnPlayerTicketAddQueue().add(pair);
            addTicket(pair, ticket);
        }
        if ((chunk != null && chunk.isFullChunkReady())) {
            if (needsTicket) {
                chunkMap.world.getChunkProvider().tickDistanceManager();
            }
            return needsTicket;
        }

        boolean success;
        if (!(success = updatePriorityTicket(coords, ticketType, priority))) {
            Ticket<ChunkCoordIntPair> ticket = new Ticket<ChunkCoordIntPair>(ticketType, PRIORITY_TICKET_LEVEL, coords);
            ticket.priority = priority;
            success = this.addTicket(pair, ticket);
        } else {
            if (chunk == null) {
                chunk = chunkMap.getUpdatingChunk(pair);
            }
            chunkMap.queueHolderUpdate(chunk);
        }

        //chunkMap.world.getWorld().spawnParticle(priority <= 15 ? org.bukkit.Particle.EXPLOSION_HUGE : org.bukkit.Particle.EXPLOSION_NORMAL, chunkMap.world.getWorld().getPlayers(), null, coords.x << 4, 70, coords.z << 4, 2, 0, 0, 0, 1, null, true);

        chunkMap.world.getChunkProvider().tickDistanceManager();

        return success;
    }

    private boolean updatePriorityTicket(ChunkCoordIntPair coords, TicketType<ChunkCoordIntPair> type, int priority) {
        ArraySetSorted<Ticket<?>> tickets = this.tickets.get(coords.pair());
        if (tickets == null) {
            return false;
        }
        for (Ticket<?> ticket : tickets) {
            if (ticket.getTicketType() == type) {
                // We only support increasing, not decreasing, too complicated
                ticket.setCurrentTick(this.currentTick);
                ticket.priority = Math.max(ticket.priority, priority);
                return true;
            }
        }

        return false;
    }

    public int getChunkPriority(ChunkCoordIntPair coords) {
        AsyncCatcher.catchOp("ChunkMapDistance::getChunkPriority");
        ArraySetSorted<Ticket<?>> tickets = this.tickets.get(coords.pair());
        if (tickets == null) {
            return 0;
        }
        for (Ticket<?> ticket : tickets) {
            if (ticket.getTicketType() == TicketType.URGENT) {
                return URGENT_PRIORITY;
            }
        }
        for (Ticket<?> ticket : tickets) {
            if (ticket.getTicketType() == TicketType.PRIORITY && ticket.priority > 0) {
                return ticket.priority;
            }
        }
        return 0;
    }

    public void clearPriorityTickets(ChunkCoordIntPair coords) {
        AsyncCatcher.catchOp("ChunkMapDistance::clearPriority");
        this.removeTicket(coords.pair(), new Ticket<ChunkCoordIntPair>(TicketType.PRIORITY, PRIORITY_TICKET_LEVEL, coords));
    }

    public void clearUrgent(ChunkCoordIntPair coords) {
        AsyncCatcher.catchOp("ChunkMapDistance::clearUrgent");
        this.removeTicket(coords.pair(), new Ticket<ChunkCoordIntPair>(TicketType.URGENT, PRIORITY_TICKET_LEVEL, coords));
    }
    // Paper end
    public <T> boolean addTicketAtLevel(TicketType<T> ticketType, ChunkCoordIntPair chunkcoordintpair, int level, T identifier) {
        return this.addTicket(chunkcoordintpair.pair(), new Ticket<>(ticketType, level, identifier));
        // CraftBukkit end
    }

    public <T> void b(TicketType<T> tickettype, ChunkCoordIntPair chunkcoordintpair, int i, T t0) {
        // CraftBukkit start
        this.removeTicketAtLevel(tickettype, chunkcoordintpair, i, t0);
    }

    public <T> boolean removeTicketAtLevel(TicketType<T> ticketType, ChunkCoordIntPair chunkcoordintpair, int level, T identifier) {
        Ticket<T> ticket = new Ticket<>(ticketType, level, identifier);

        return this.removeTicket(chunkcoordintpair.pair(), ticket);
        // CraftBukkit end
    }

    public <T> void addTicket(TicketType<T> tickettype, ChunkCoordIntPair chunkcoordintpair, int i, T t0) {
        this.addTicket(chunkcoordintpair.pair(), new Ticket<>(tickettype, 33 - i, t0));
    }

    public <T> void removeTicket(TicketType<T> tickettype, ChunkCoordIntPair chunkcoordintpair, int i, T t0) {
        Ticket<T> ticket = new Ticket<>(tickettype, 33 - i, t0);

        this.removeTicket(chunkcoordintpair.pair(), ticket);
    }

    private ArraySetSorted<Ticket<?>> e(long i) {
        return (ArraySetSorted) this.tickets.computeIfAbsent(i, (j) -> {
            return ArraySetSorted.a(4);
        });
    }

    protected void a(ChunkCoordIntPair chunkcoordintpair, boolean flag) {
        Ticket<ChunkCoordIntPair> ticket = new Ticket<>(TicketType.FORCED, 31, chunkcoordintpair);

        if (flag) {
            this.addTicket(chunkcoordintpair.pair(), ticket);
        } else {
            this.removeTicket(chunkcoordintpair.pair(), ticket);
        }

    }

    public void a(SectionPosition sectionposition, EntityPlayer entityplayer) {
        long i = sectionposition.r().pair();

        ((ObjectSet) this.c.computeIfAbsent(i, (j) -> {
            return new ObjectOpenHashSet();
        })).add(entityplayer);
        //this.f.update(i, 0, true); // Paper - no longer used
        this.g.update(i, 0, true);
    }

    public void b(SectionPosition sectionposition, EntityPlayer entityplayer) {
        long i = sectionposition.r().pair();
        ObjectSet<EntityPlayer> objectset = (ObjectSet) this.c.get(i);

        if (objectset != null) objectset.remove(entityplayer); // Paper - some state corruption happens here, don't crash, clean up gracefully.
        if (objectset == null || objectset.isEmpty()) { // Paper
            this.c.remove(i);
            //this.f.update(i, Integer.MAX_VALUE, false); // Paper - no longer used
            this.g.update(i, Integer.MAX_VALUE, false);
        }

    }

    protected String c(long i) {
        ArraySetSorted<Ticket<?>> arraysetsorted = (ArraySetSorted) this.tickets.get(i);
        String s;

        if (arraysetsorted != null && !arraysetsorted.isEmpty()) {
            s = ((Ticket) arraysetsorted.b()).toString();
        } else {
            s = "no_ticket";
        }

        return s;
    }

    protected void setNoTickViewDistance(int i) { // Paper - force abi breakage on usage change
        this.g.a(i);
    }

    public int b() {
        // Paper start - use distance map to implement
        // note: this is the spawn chunk count
        return this.chunkMap.playerChunkTickRangeMap.size();
        // Paper end - use distance map to implement
    }

    public boolean d(long i) {
        // Paper start - use distance map to implement
        // note: this is the is spawn chunk method
        return this.chunkMap.playerChunkTickRangeMap.getObjectsInRange(i) != null;
        // Paper end - use distance map to implement
    }

    public String c() {
        return this.i.a();
    }

    // CraftBukkit start
    public <T> void removeAllTicketsFor(TicketType<T> ticketType, int ticketLevel, T ticketIdentifier) {
        Ticket<T> target = new Ticket<>(ticketType, ticketLevel, ticketIdentifier);

        for (java.util.Iterator<Entry<ArraySetSorted<Ticket<?>>>> iterator = this.tickets.long2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
            Entry<ArraySetSorted<Ticket<?>>> entry = iterator.next();
            ArraySetSorted<Ticket<?>> tickets = entry.getValue();
            if (tickets.remove(target)) {
                // copied from removeTicket
                this.ticketLevelTracker.update(entry.getLongKey(), getLowestTicketLevel(tickets), false);

                // can't use entry after it's removed
                if (tickets.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }
    // CraftBukkit end

    class a extends ChunkMap {

        public a() {
            super(PlayerChunkMap.GOLDEN_TICKET + 2, 16, 256);
        }

        @Override
        protected int b(long i) {
            ArraySetSorted<Ticket<?>> arraysetsorted = (ArraySetSorted) ChunkMapDistance.this.tickets.get(i);

            return arraysetsorted == null ? Integer.MAX_VALUE : (arraysetsorted.isEmpty() ? Integer.MAX_VALUE : ((Ticket) arraysetsorted.b()).b());
        }

        @Override
        protected int c(long i) {
            if (!ChunkMapDistance.this.a(i)) {
                PlayerChunk playerchunk = ChunkMapDistance.this.b(i);

                if (playerchunk != null) {
                    return playerchunk.getTicketLevel();
                }
            }

            return PlayerChunkMap.GOLDEN_TICKET + 1;
        }

        @Override
        protected void a(long i, int j) {
            PlayerChunk playerchunk = ChunkMapDistance.this.b(i);
            int k = playerchunk == null ? PlayerChunkMap.GOLDEN_TICKET + 1 : playerchunk.getTicketLevel();

            if (k != j) {
                playerchunk = ChunkMapDistance.this.a(i, j, playerchunk, k);
                if (playerchunk != null) {
                    ChunkMapDistance.this.pendingChunkUpdates.add(playerchunk);
                }

            }
        }

        public int a(int i) {
            return this.b(i);
        }
    }

    class c extends ChunkMapDistance.b {

        private int e = 0;
        private final Long2IntMap f = Long2IntMaps.synchronize(new Long2IntOpenHashMap());
        private final LongSet g = new LongOpenHashSet();

        protected c(int i) {
            super(i);
            this.f.defaultReturnValue(i + 2);
        }

        @Override
        protected void a(long i, int j, int k) {
            this.g.add(i);
        }

        public void a(int i) {
            ObjectIterator objectiterator = this.a.long2ByteEntrySet().iterator();

            while (objectiterator.hasNext()) {
                Long2ByteMap.Entry it_unimi_dsi_fastutil_longs_long2bytemap_entry = (Long2ByteMap.Entry) objectiterator.next(); // Paper - decompile fix
                byte b0 = it_unimi_dsi_fastutil_longs_long2bytemap_entry.getByteValue();
                long j = it_unimi_dsi_fastutil_longs_long2bytemap_entry.getLongKey();

                this.a(j, b0, this.c(b0), b0 <= i - 2);
            }

            this.e = i;
        }

        private void a(long i, int j, boolean flag, boolean flag1) {
            if (flag != flag1) {
                ChunkCoordIntPair coords = new ChunkCoordIntPair(i); // Paper
                Ticket<?> ticket = new Ticket<>(TicketType.PLAYER, 33, coords); // Paper - no-tick view distance

                if (flag1) {
                    scheduleChunkLoad(i, MinecraftServer.currentTick, j, (priority) -> { // Paper - smarter ticket delay based on frustum and distance
                    // Paper start - recheck its still valid if not cancel
                    if (!isChunkInRange(i)) {
                        ChunkMapDistance.this.k.a(ChunkTaskQueueSorter.a(() -> {
                            ChunkMapDistance.this.m.execute(() -> {
                                ChunkMapDistance.this.removeTicket(i, ticket);
                                ChunkMapDistance.this.clearPriorityTickets(coords);
                            });
                        }, i, false));
                        return;
                    }
                    // abort early if we got a ticket already
                    if (hasPlayerTicket(coords, 33)) return;
                    // skip player ticket throttle for near chunks
                    if (priority <= 3) {
                        ChunkMapDistance.this.addTicket(i, ticket);
                        ChunkMapDistance.this.l.add(i);
                        return;
                    }
                    // Paper end
                    ChunkMapDistance.this.j.a(ChunkTaskQueueSorter.a(() -> { // CraftBukkit - decompile error
                        ChunkMapDistance.this.m.execute(() -> {
                            if (isChunkInRange(i)) { if (!hasPlayerTicket(coords, 33)) { // Paper - high priority might of already added it
                                ChunkMapDistance.this.addTicket(i, ticket);
                                ChunkMapDistance.this.l.add(i);
                            }} else { // Paper
                                ChunkMapDistance.this.k.a(ChunkTaskQueueSorter.a(() -> { // CraftBukkit - decompile error
                                }, i, false));
                            }

                        });
                    }, i, () -> {
                        return Math.min(PlayerChunkMap.GOLDEN_TICKET, priority); // Paper
                    }));
                    }); // Paper
                } else {
                    ChunkMapDistance.this.k.a(ChunkTaskQueueSorter.a(() -> {
                        ChunkMapDistance.this.m.execute(() -> {
                            ChunkMapDistance.this.removeTicket(i, ticket);
                            ChunkMapDistance.this.clearPriorityTickets(coords); // Paper
                        });
                    }, i, true));
                }
            }

        }

        // Paper start - smart scheduling of player tickets
        private boolean isChunkInRange(long i) {
            return this.isLoadedChunkLevel(this.getChunkLevel(i));
        }
        public void scheduleChunkLoad(long i, long startTick, int initialDistance, java.util.function.Consumer<Integer> task) {
            long elapsed = MinecraftServer.currentTick - startTick;
            ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(i);
            PlayerChunk updatingChunk = chunkMap.getUpdatingChunk(i);
            if ((updatingChunk != null && updatingChunk.isFullChunkReady()) || !isChunkInRange(i) || getChunkPriority(chunkPos) > 0) { // Copied from above
                // no longer needed
                task.accept(1);
                return;
            }

            int desireDelay = 0;
            double minDist = Double.MAX_VALUE;
            com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> players = chunkMap.playerViewDistanceNoTickMap.getObjectsInRange(i);
            if (elapsed == 0 && initialDistance <= 4) {
                // Aim for no delay on initial 6 chunk radius tickets save on performance of the below code to only > 6
                minDist = initialDistance;
            } else if (players != null) {
                Object[] backingSet = players.getBackingSet();

                BlockPosition blockPos = chunkPos.asPosition();

                boolean isFront = false;
                BlockPosition.MutableBlockPosition pos = new BlockPosition.MutableBlockPosition();
                for (int index = 0, len = backingSet.length; index < len; ++index) {
                    if (!(backingSet[index] instanceof EntityPlayer)) {
                        continue;
                    }
                    EntityPlayer player = (EntityPlayer) backingSet[index];

                    ChunkCoordIntPair pointInFront = player.getChunkInFront(5);
                    pos.setValues(pointInFront.x << 4, 0, pointInFront.z << 4);
                    double frontDist = MCUtil.distanceSq(pos, blockPos);

                    pos.setValues(player.locX(), 0, player.locZ());
                    double center = MCUtil.distanceSq(pos, blockPos);

                    double dist = Math.min(frontDist, center);
                    if (!isFront) {
                        ChunkCoordIntPair pointInBack = player.getChunkInFront(-7);
                        pos.setValues(pointInBack.x << 4, 0, pointInBack.z << 4);
                        double backDist = MCUtil.distanceSq(pos, blockPos);
                        if (frontDist < backDist) {
                            isFront = true;
                        }
                    }
                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
                if (minDist == Double.MAX_VALUE) {
                    minDist = 15;
                } else {
                    minDist = Math.sqrt(minDist) / 16;
                }
                if (minDist > 4) {
                    int desiredTimeDelayMax = isFront ?
                        (minDist < 10 ? 7 : 15) : // Front
                        (minDist < 10 ? 15 : 45); // Back
                    desireDelay += (desiredTimeDelayMax * 20) * (minDist / 32);
                }
            } else {
                minDist = initialDistance;
                desireDelay = 1;
            }
            long delay = desireDelay - elapsed;
            if (delay <= 0 && minDist > 4 && minDist < Double.MAX_VALUE) {
                boolean hasAnyNeighbor = false;
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && z == 0) continue;
                        long pair = ChunkCoordIntPair.pair(chunkPos.x + x, chunkPos.z + z);
                        PlayerChunk neighbor = chunkMap.getUpdatingChunk(pair);
                        ChunkStatus current = neighbor != null ? neighbor.getChunkHolderStatus() : null;
                        if (current != null && current.isAtLeastStatus(ChunkStatus.LIGHT)) {
                            hasAnyNeighbor = true;
                        }
                    }
                }
                if (!hasAnyNeighbor) {
                    delay += 20;
                }
            }
            if (delay <= 0) {
                task.accept((int) minDist);
            } else {
                int taskDelay = (int) Math.min(delay, minDist >= 10 ? 40 : (minDist < 6 ? 5 : 20));
                MCUtil.scheduleTask(taskDelay, () -> scheduleChunkLoad(i, startTick, initialDistance, task), "Player Ticket Delayer");
            }
        }
        // Paper end

        @Override
        public void a() {
            super.a();
            if (!this.g.isEmpty()) {
                LongIterator longiterator = this.g.iterator();

                while (longiterator.hasNext()) {
                    long i = longiterator.nextLong();
                    int j = this.f.get(i);
                    int k = this.c(i);

                    if (j != k) {
                        ChunkMapDistance.this.i.a(new ChunkCoordIntPair(i), () -> {
                            return this.f.get(i);
                        }, k, (l) -> {
                            if (l >= this.f.defaultReturnValue()) {
                                this.f.remove(i);
                            } else {
                                this.f.put(i, l);
                            }

                        });
                        this.a(i, k, this.c(j), this.c(k));
                    }
                }

                this.g.clear();
            }

        }

        private boolean isLoadedChunkLevel(int i) { return c(i); } // Paper - OBFHELPER
        private boolean c(int i) {
            return i <= this.e - 2;
        }
    }

    class b extends ChunkMap {

        protected final Long2ByteMap a = new Long2ByteOpenHashMap();
        protected final int b;

        protected b(int i) {
            super(i + 2, 16, 256);
            this.b = i;
            this.a.defaultReturnValue((byte) (i + 2));
        }

        protected final int getChunkLevel(long i) { return c(i); } // Paper - OBFHELPER
        @Override
        protected int c(long i) {
            return this.a.get(i);
        }

        @Override
        protected void a(long i, int j) {
            byte b0;

            if (j > this.b) {
                b0 = this.a.remove(i);
            } else {
                b0 = this.a.put(i, (byte) j);
            }

            this.a(i, b0, j);
        }

        protected void a(long i, int j, int k) {}

        @Override
        protected int b(long i) {
            return this.d(i) ? 0 : Integer.MAX_VALUE;
        }

        private boolean d(long i) {
            ObjectSet<EntityPlayer> objectset = (ObjectSet) ChunkMapDistance.this.c.get(i);

            return objectset != null && !objectset.isEmpty();
        }

        public void a() {
            this.b(Integer.MAX_VALUE);
        }
    }
}
