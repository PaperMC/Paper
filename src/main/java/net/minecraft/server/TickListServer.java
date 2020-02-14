package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class TickListServer<T> implements TickList<T> {

    protected final Predicate<T> a;
    private final Function<T, MinecraftKey> b;
    private final Set<NextTickListEntry<T>> nextTickListHash = Sets.newHashSet();
    private final TreeSet<NextTickListEntry<T>> nextTickList = Sets.newTreeSet(NextTickListEntry.a());
    private final WorldServer e;
    private final Queue<NextTickListEntry<T>> f = Queues.newArrayDeque();
    private final List<NextTickListEntry<T>> g = Lists.newArrayList();
    private final Consumer<NextTickListEntry<T>> h;

    public TickListServer(WorldServer worldserver, Predicate<T> predicate, Function<T, MinecraftKey> function, Consumer<NextTickListEntry<T>> consumer, String timingsType) { // Paper
        this.a = predicate;
        this.b = function;
        this.e = worldserver;
        this.h = consumer;
        this.timingCleanup = co.aikar.timings.WorldTimingsHandler.getTickList(worldserver, timingsType + " - Cleanup");
        this.timingTicking = co.aikar.timings.WorldTimingsHandler.getTickList(worldserver, timingsType + " - Ticking");
    }
    private final co.aikar.timings.Timing timingCleanup; // Paper
    private final co.aikar.timings.Timing timingTicking; // Paper
    // Paper end

    // Paper start
    protected void nextTick() {}
    // Paper end

    public void b() {
        // Paper start - allow overriding
        this.tick();
    }
    public void tick() {
        // Paper end
        int i = this.nextTickList.size();

        if (false) { // CraftBukkit
            throw new IllegalStateException("TickNextTick list out of synch");
        } else {
            if (i > 65536) {
                // CraftBukkit start - If the server has too much to process over time, try to alleviate that
                if (i > 20 * 65536) {
                    i = i / 20;
                } else {
                    i = 65536;
                }
                // CraftBukkit end
            }

            ChunkProviderServer chunkproviderserver = this.e.getChunkProvider();
            Iterator<NextTickListEntry<T>> iterator = this.nextTickList.iterator();

            this.e.getMethodProfiler().enter("cleaning");

            this.timingCleanup.startTiming(); // Paper
            NextTickListEntry nextticklistentry;

            while (i > 0 && iterator.hasNext()) {
                nextticklistentry = (NextTickListEntry) iterator.next();
                if (nextticklistentry.b > this.e.getTime()) {
                    break;
                }

                if (chunkproviderserver.a(nextticklistentry.a)) {
                    iterator.remove();
                    this.nextTickListHash.remove(nextticklistentry);
                    this.f.add(nextticklistentry);
                    --i;
                }
            }
            this.timingCleanup.stopTiming(); // Paper

            this.timingTicking.startTiming(); // Paper
            this.e.getMethodProfiler().exitEnter("ticking");

            while ((nextticklistentry = (NextTickListEntry) this.f.poll()) != null) {
                if (chunkproviderserver.a(nextticklistentry.a)) {
                    try {
                        this.g.add(nextticklistentry);
                        this.h.accept(nextticklistentry);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.a(throwable, "Exception while ticking");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being ticked");

                        CrashReportSystemDetails.a(crashreportsystemdetails, nextticklistentry.a, (IBlockData) null);
                        throw new ReportedException(crashreport);
                    }
                } else {
                    this.a(nextticklistentry.a, (T) nextticklistentry.b(), 0); // CraftBukkit - decompile error
                }
            }

            this.timingTicking.stopTiming(); // Paper
            this.e.getMethodProfiler().exit();
            this.g.clear();
            this.f.clear();
        }
    }

    @Override
    public boolean b(BlockPosition blockposition, T t0) {
        // Paper start - allow overriding
        return this.isPendingTickThisTick(blockposition, t0);
    }
    public boolean isPendingTickThisTick(BlockPosition blockposition, T t0) {
        // Paper end
        return this.f.contains(new NextTickListEntry<>(blockposition, t0));
    }

    public List<NextTickListEntry<T>> a(ChunkCoordIntPair chunkcoordintpair, boolean flag, boolean flag1) {
        // Paper start - allow overriding
        return this.getEntriesInChunk(chunkcoordintpair, flag, flag1);
    }
    public List<NextTickListEntry<T>> getEntriesInChunk(ChunkCoordIntPair chunkcoordintpair, boolean flag, boolean flag1) {
        // Paper end
        int i = (chunkcoordintpair.x << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.z << 4) - 2;
        int l = k + 16 + 2;

        return this.a(new StructureBoundingBox(i, 0, k, j, 256, l), flag, flag1);
    }

    public List<NextTickListEntry<T>> a(StructureBoundingBox structureboundingbox, boolean flag, boolean flag1) {
        // Paper start - allow overriding
        return this.getEntriesInBoundingBox(structureboundingbox, flag, flag1);
    }
    public List<NextTickListEntry<T>> getEntriesInBoundingBox(StructureBoundingBox structureboundingbox, boolean flag, boolean flag1) {
        // Paper end
        List<NextTickListEntry<T>> list = this.a((List) null, this.nextTickList, structureboundingbox, flag);

        if (flag && list != null) {
            this.nextTickListHash.removeAll(list);
        }

        list = this.a(list, this.f, structureboundingbox, flag);
        if (!flag1) {
            list = this.a(list, this.g, structureboundingbox, flag);
        }

        return list == null ? Collections.emptyList() : list;
    }

    @Nullable
    private List<NextTickListEntry<T>> a(@Nullable List<NextTickListEntry<T>> list, Collection<NextTickListEntry<T>> collection, StructureBoundingBox structureboundingbox, boolean flag) {
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            NextTickListEntry<T> nextticklistentry = (NextTickListEntry) iterator.next();
            BlockPosition blockposition = nextticklistentry.a;

            if (blockposition.getX() >= structureboundingbox.a && blockposition.getX() < structureboundingbox.d && blockposition.getZ() >= structureboundingbox.c && blockposition.getZ() < structureboundingbox.f) {
                if (flag) {
                    iterator.remove();
                }

                if (list == null) {
                    list = Lists.newArrayList();
                }

                ((List) list).add(nextticklistentry);
            }
        }

        return (List) list;
    }

    public void a(StructureBoundingBox structureboundingbox, BlockPosition blockposition) {
        // Paper start - allow overriding
        this.copy(structureboundingbox, blockposition);
    }
    public void copy(StructureBoundingBox structureboundingbox, BlockPosition blockposition) {
        // Paper end
        List<NextTickListEntry<T>> list = this.a(structureboundingbox, false, false);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            NextTickListEntry<T> nextticklistentry = (NextTickListEntry) iterator.next();

            if (structureboundingbox.b((BaseBlockPosition) nextticklistentry.a)) {
                BlockPosition blockposition1 = nextticklistentry.a.a((BaseBlockPosition) blockposition);
                T t0 = nextticklistentry.b();

                this.a(new NextTickListEntry<>(blockposition1, t0, nextticklistentry.b, nextticklistentry.c));
            }
        }

    }

    public NBTTagList a(ChunkCoordIntPair chunkcoordintpair) {
        // Paper start - allow overriding
        return this.serialize(chunkcoordintpair);
    }
    public NBTTagList serialize(ChunkCoordIntPair chunkcoordintpair) {
        // Paper end
        List<NextTickListEntry<T>> list = this.a(chunkcoordintpair, false, true);

        return a(this.b, list, this.e.getTime());
    }

    public static <T> NBTTagList serialize(Function<T, MinecraftKey> function, Iterable<NextTickListEntry<T>> iterable, long i) { return TickListServer.a(function, iterable, i); } // Paper - OBFHELPER
    private static <T> NBTTagList a(Function<T, MinecraftKey> function, Iterable<NextTickListEntry<T>> iterable, long i) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            NextTickListEntry<T> nextticklistentry = (NextTickListEntry) iterator.next();
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            nbttagcompound.setString("i", ((MinecraftKey) function.apply(nextticklistentry.b())).toString());
            nbttagcompound.setInt("x", nextticklistentry.a.getX());
            nbttagcompound.setInt("y", nextticklistentry.a.getY());
            nbttagcompound.setInt("z", nextticklistentry.a.getZ());
            nbttagcompound.setInt("t", (int) (nextticklistentry.b - i));
            nbttagcompound.setInt("p", nextticklistentry.c.a());
            nbttaglist.add(nbttagcompound);
        }

        return nbttaglist;
    }

    @Override
    public boolean a(BlockPosition blockposition, T t0) {
        // Paper start - allow overriding
        return this.isScheduledForTick(blockposition, t0);
    }
    public boolean isScheduledForTick(BlockPosition blockposition, T t0) {
        // Paper end
        return this.nextTickListHash.contains(new NextTickListEntry<>(blockposition, t0));
    }

    @Override
    public void a(BlockPosition blockposition, T t0, int i, TickListPriority ticklistpriority) {
        // Paper start - allow overriding
        this.schedule(blockposition, t0, i, ticklistpriority);
    }
    public void schedule(BlockPosition blockposition, T t0, int i, TickListPriority ticklistpriority) {
        // Paper end
        if (!this.a.test(t0)) {
            this.a(new NextTickListEntry<>(blockposition, t0, (long) i + this.e.getTime(), ticklistpriority));
        }

    }

    private void a(NextTickListEntry<T> nextticklistentry) {
        if (!this.nextTickListHash.contains(nextticklistentry)) {
            this.nextTickListHash.add(nextticklistentry);
            this.nextTickList.add(nextticklistentry);
        }

    }

    public int a() {
        // Paper start - allow overriding
        return this.getTotalScheduledEntries();
    }
    public int getTotalScheduledEntries() {
        // Paper end
        return this.nextTickListHash.size();
    }
}
