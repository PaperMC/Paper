package ca.spottedleaf.moonrise.common.misc;

import ca.spottedleaf.moonrise.common.util.CoordinateUtils;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.ByteArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;

public final class Delayed8WayDistancePropagator2D {

    // Test
    /*
    protected static void test(int x, int z, com.destroystokyo.paper.util.misc.DistanceTrackingAreaMap<Ticket> reference, Delayed8WayDistancePropagator2D test) {
        int got = test.getLevel(x, z);

        int expect = 0;
        Object[] nearest = reference.getObjectsInRange(x, z) == null ? null : reference.getObjectsInRange(x, z).getBackingSet();
        if (nearest != null) {
            for (Object _obj : nearest) {
                if (_obj instanceof Ticket) {
                    Ticket ticket = (Ticket)_obj;
                    long ticketCoord = reference.getLastCoordinate(ticket);
                    int viewDistance = reference.getLastViewDistance(ticket);
                    int distance = Math.max(com.destroystokyo.paper.util.math.IntegerUtil.branchlessAbs(MCUtil.getCoordinateX(ticketCoord) - x),
                            com.destroystokyo.paper.util.math.IntegerUtil.branchlessAbs(MCUtil.getCoordinateZ(ticketCoord) - z));
                    int level = viewDistance - distance;
                    if (level > expect) {
                        expect = level;
                    }
                }
            }
        }

        if (expect != got) {
            throw new IllegalStateException("Expected " + expect + " at pos (" + x + "," + z + ") but got " + got);
        }
    }

    static class Ticket {

        int x;
        int z;

        final com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<Ticket> empty
                = new com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<>(this);

    }

    public static void main(final String[] args) {
        com.destroystokyo.paper.util.misc.DistanceTrackingAreaMap<Ticket> reference = new com.destroystokyo.paper.util.misc.DistanceTrackingAreaMap<Ticket>() {
            @Override
            protected com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<Ticket> getEmptySetFor(Ticket object) {
                return object.empty;
            }
        };
        Delayed8WayDistancePropagator2D test = new Delayed8WayDistancePropagator2D();

        final int maxDistance = 64;
        // test origin
        {
            Ticket originTicket = new Ticket();
            int originDistance = 31;
            // test single source
            reference.add(originTicket, 0, 0, originDistance);
            test.setSource(0, 0, originDistance); test.propagateUpdates(); // set and propagate
            for (int dx = -originDistance; dx <= originDistance; ++dx) {
                for (int dz = -originDistance; dz <= originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }
            // test single source decrease
            reference.update(originTicket, 0, 0, originDistance/2);
            test.setSource(0, 0, originDistance/2); test.propagateUpdates(); // set and propagate
            for (int dx = -originDistance; dx <= originDistance; ++dx) {
                for (int dz = -originDistance; dz <= originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }
            // test source increase
            originDistance = 2*originDistance;
            reference.update(originTicket, 0, 0, originDistance);
            test.setSource(0, 0, originDistance); test.propagateUpdates(); // set and propagate
            for (int dx = -4*originDistance; dx <= 4*originDistance; ++dx) {
                for (int dz = -4*originDistance; dz <= 4*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            reference.remove(originTicket);
            test.removeSource(0, 0); test.propagateUpdates();
        }

        // test multiple sources at origin
        {
            int originDistance = 31;
            java.util.List<Ticket> list = new java.util.ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                Ticket a = new Ticket();
                list.add(a);
                a.x = (i & 1) == 1 ? -i : i;
                a.z = (i & 1) == 1 ? -i : i;
            }
            for (Ticket ticket : list) {
                reference.add(ticket, ticket.x, ticket.z, originDistance);
                test.setSource(ticket.x, ticket.z, originDistance);
            }
            test.propagateUpdates();

            for (int dx = -8*originDistance; dx <= 8*originDistance; ++dx) {
                for (int dz = -8*originDistance; dz <= 8*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            // test ticket level decrease

            for (Ticket ticket : list) {
                reference.update(ticket, ticket.x, ticket.z, originDistance/2);
                test.setSource(ticket.x, ticket.z, originDistance/2);
            }
            test.propagateUpdates();

            for (int dx = -8*originDistance; dx <= 8*originDistance; ++dx) {
                for (int dz = -8*originDistance; dz <= 8*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            // test ticket level increase

            for (Ticket ticket : list) {
                reference.update(ticket, ticket.x, ticket.z, originDistance*2);
                test.setSource(ticket.x, ticket.z, originDistance*2);
            }
            test.propagateUpdates();

            for (int dx = -16*originDistance; dx <= 16*originDistance; ++dx) {
                for (int dz = -16*originDistance; dz <= 16*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            // test ticket remove
            for (int i = 0, len = list.size(); i < len; ++i) {
                if ((i & 3) != 0) {
                    continue;
                }
                Ticket ticket = list.get(i);
                reference.remove(ticket);
                test.removeSource(ticket.x, ticket.z);
            }
            test.propagateUpdates();

            for (int dx = -16*originDistance; dx <= 16*originDistance; ++dx) {
                for (int dz = -16*originDistance; dz <= 16*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }
        }

        // now test at coordinate offsets
        // test offset
        {
            Ticket originTicket = new Ticket();
            int originDistance = 31;
            int offX = 54432;
            int offZ = -134567;
            // test single source
            reference.add(originTicket, offX, offZ, originDistance);
            test.setSource(offX, offZ, originDistance); test.propagateUpdates(); // set and propagate
            for (int dx = -originDistance; dx <= originDistance; ++dx) {
                for (int dz = -originDistance; dz <= originDistance; ++dz) {
                    test(dx + offX, dz + offZ, reference, test);
                }
            }
            // test single source decrease
            reference.update(originTicket, offX, offZ, originDistance/2);
            test.setSource(offX, offZ, originDistance/2); test.propagateUpdates(); // set and propagate
            for (int dx = -originDistance; dx <= originDistance; ++dx) {
                for (int dz = -originDistance; dz <= originDistance; ++dz) {
                    test(dx + offX, dz + offZ, reference, test);
                }
            }
            // test source increase
            originDistance = 2*originDistance;
            reference.update(originTicket, offX, offZ, originDistance);
            test.setSource(offX, offZ, originDistance); test.propagateUpdates(); // set and propagate
            for (int dx = -4*originDistance; dx <= 4*originDistance; ++dx) {
                for (int dz = -4*originDistance; dz <= 4*originDistance; ++dz) {
                    test(dx + offX, dz + offZ, reference, test);
                }
            }

            reference.remove(originTicket);
            test.removeSource(offX, offZ); test.propagateUpdates();
        }

        // test multiple sources at origin
        {
            int originDistance = 31;
            int offX = 54432;
            int offZ = -134567;
            java.util.List<Ticket> list = new java.util.ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                Ticket a = new Ticket();
                list.add(a);
                a.x = offX + ((i & 1) == 1 ? -i : i);
                a.z = offZ + ((i & 1) == 1 ? -i : i);
            }
            for (Ticket ticket : list) {
                reference.add(ticket, ticket.x, ticket.z, originDistance);
                test.setSource(ticket.x, ticket.z, originDistance);
            }
            test.propagateUpdates();

            for (int dx = -8*originDistance; dx <= 8*originDistance; ++dx) {
                for (int dz = -8*originDistance; dz <= 8*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            // test ticket level decrease

            for (Ticket ticket : list) {
                reference.update(ticket, ticket.x, ticket.z, originDistance/2);
                test.setSource(ticket.x, ticket.z, originDistance/2);
            }
            test.propagateUpdates();

            for (int dx = -8*originDistance; dx <= 8*originDistance; ++dx) {
                for (int dz = -8*originDistance; dz <= 8*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            // test ticket level increase

            for (Ticket ticket : list) {
                reference.update(ticket, ticket.x, ticket.z, originDistance*2);
                test.setSource(ticket.x, ticket.z, originDistance*2);
            }
            test.propagateUpdates();

            for (int dx = -16*originDistance; dx <= 16*originDistance; ++dx) {
                for (int dz = -16*originDistance; dz <= 16*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }

            // test ticket remove
            for (int i = 0, len = list.size(); i < len; ++i) {
                if ((i & 3) != 0) {
                    continue;
                }
                Ticket ticket = list.get(i);
                reference.remove(ticket);
                test.removeSource(ticket.x, ticket.z);
            }
            test.propagateUpdates();

            for (int dx = -16*originDistance; dx <= 16*originDistance; ++dx) {
                for (int dz = -16*originDistance; dz <= 16*originDistance; ++dz) {
                    test(dx, dz, reference, test);
                }
            }
        }
    }
     */

    // this map is considered "stale" unless updates are propagated.
    protected final LevelMap levels = new LevelMap(8192*2, 0.6f);

    // this map is never stale
    protected final Long2ByteOpenHashMap sources = new Long2ByteOpenHashMap(4096, 0.6f);

    // Generally updates to positions are made close to other updates, so we link to decrease cache misses when
    // propagating updates
    protected final LongLinkedOpenHashSet updatedSources = new LongLinkedOpenHashSet();

    @FunctionalInterface
    public static interface LevelChangeCallback {

        /**
         * This can be called for intermediate updates. So do not rely on newLevel being close to or
         * the exact level that is expected after a full propagation has occured.
         */
        public void onLevelUpdate(final long coordinate, final byte oldLevel, final byte newLevel);

    }

    protected final LevelChangeCallback changeCallback;

    public Delayed8WayDistancePropagator2D() {
        this(null);
    }

    public Delayed8WayDistancePropagator2D(final LevelChangeCallback changeCallback) {
        this.changeCallback = changeCallback;
    }

    public int getLevel(final long pos) {
        return this.levels.get(pos);
    }

    public int getLevel(final int x, final int z) {
        return this.levels.get(CoordinateUtils.getChunkKey(x, z));
    }

    public void setSource(final int x, final int z, final int level) {
        this.setSource(CoordinateUtils.getChunkKey(x, z), level);
    }

    public void setSource(final long coordinate, final int level) {
        if ((level & 63) != level || level == 0) {
            throw new IllegalArgumentException("Level must be in (0, 63], not " + level);
        }

        final byte byteLevel = (byte)level;
        final byte oldLevel = this.sources.put(coordinate, byteLevel);

        if (oldLevel == byteLevel) {
            return; // nothing to do
        }

        // queue to update later
        this.updatedSources.add(coordinate);
    }

    public void removeSource(final int x, final int z) {
        this.removeSource(CoordinateUtils.getChunkKey(x, z));
    }

    public void removeSource(final long coordinate) {
        if (this.sources.remove(coordinate) != 0) {
            this.updatedSources.add(coordinate);
        }
    }

    // queues used for BFS propagating levels
    protected final WorkQueue[] levelIncreaseWorkQueues = new WorkQueue[64];
    {
        for (int i = 0; i < this.levelIncreaseWorkQueues.length; ++i) {
            this.levelIncreaseWorkQueues[i] = new WorkQueue();
        }
    }
    protected final WorkQueue[] levelRemoveWorkQueues = new WorkQueue[64];
    {
        for (int i = 0; i < this.levelRemoveWorkQueues.length; ++i) {
            this.levelRemoveWorkQueues[i] = new WorkQueue();
        }
    }
    protected long levelIncreaseWorkQueueBitset;
    protected long levelRemoveWorkQueueBitset;

    protected final void addToIncreaseWorkQueue(final long coordinate, final byte level) {
        final WorkQueue queue = this.levelIncreaseWorkQueues[level];
        queue.queuedCoordinates.enqueue(coordinate);
        queue.queuedLevels.enqueue(level);

        this.levelIncreaseWorkQueueBitset |= (1L << level);
    }

    protected final void addToIncreaseWorkQueue(final long coordinate, final byte index, final byte level) {
        final WorkQueue queue = this.levelIncreaseWorkQueues[index];
        queue.queuedCoordinates.enqueue(coordinate);
        queue.queuedLevels.enqueue(level);

        this.levelIncreaseWorkQueueBitset |= (1L << index);
    }

    protected final void addToRemoveWorkQueue(final long coordinate, final byte level) {
        final WorkQueue queue = this.levelRemoveWorkQueues[level];
        queue.queuedCoordinates.enqueue(coordinate);
        queue.queuedLevels.enqueue(level);

        this.levelRemoveWorkQueueBitset |= (1L << level);
    }

    public boolean propagateUpdates() {
        if (this.updatedSources.isEmpty()) {
            return false;
        }

        boolean ret = false;

        for (final LongIterator iterator = this.updatedSources.iterator(); iterator.hasNext();) {
            final long coordinate = iterator.nextLong();

            final byte currentLevel = this.levels.get(coordinate);
            final byte updatedSource = this.sources.get(coordinate);

            if (currentLevel == updatedSource) {
                continue;
            }
            ret = true;

            if (updatedSource > currentLevel) {
                // level increase
                this.addToIncreaseWorkQueue(coordinate, updatedSource);
            } else {
                // level decrease
                this.addToRemoveWorkQueue(coordinate, currentLevel);
                // if the current coordinate is a source, then the decrease propagation will detect that and queue
                // the source propagation
            }
        }

        this.updatedSources.clear();

        // propagate source level increases first for performance reasons (in crowded areas hopefully the additions
        // make the removes remove less)
        this.propagateIncreases();

        // now we propagate the decreases (which will then re-propagate clobbered sources)
        this.propagateDecreases();

        return ret;
    }

    protected void propagateIncreases() {
        for (int queueIndex = 63 ^ Long.numberOfLeadingZeros(this.levelIncreaseWorkQueueBitset);
             this.levelIncreaseWorkQueueBitset != 0L;
             this.levelIncreaseWorkQueueBitset ^= (1L << queueIndex), queueIndex = 63 ^ Long.numberOfLeadingZeros(this.levelIncreaseWorkQueueBitset)) {

            final WorkQueue queue = this.levelIncreaseWorkQueues[queueIndex];
            while (!queue.queuedLevels.isEmpty()) {
                final long coordinate = queue.queuedCoordinates.removeFirstLong();
                byte level = queue.queuedLevels.removeFirstByte();

                final boolean neighbourCheck = level < 0;

                final byte currentLevel;
                if (neighbourCheck) {
                    level = (byte)-level;
                    currentLevel = this.levels.get(coordinate);
                } else {
                    currentLevel = this.levels.putIfGreater(coordinate, level);
                }

                if (neighbourCheck) {
                    // used when propagating from decrease to indicate that this level needs to check its neighbours
                    // this means the level at coordinate could be equal, but would still need neighbours checked

                    if (currentLevel != level) {
                        // something caused the level to change, which means something propagated to it (which means
                        // us propagating here is redundant), or something removed the level (which means we
                        // cannot propagate further)
                        continue;
                    }
                } else if (currentLevel >= level) {
                    // something higher/equal propagated
                    continue;
                }
                if (this.changeCallback != null) {
                    this.changeCallback.onLevelUpdate(coordinate, currentLevel, level);
                }

                if (level == 1) {
                    // can't propagate 0 to neighbours
                    continue;
                }

                // propagate to neighbours
                final byte neighbourLevel = (byte)(level - 1);
                final int x = (int)coordinate;
                final int z = (int)(coordinate >>> 32);

                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        if ((dx | dz) == 0) {
                            // already propagated to coordinate
                            continue;
                        }

                        // sure we can check the neighbour level in the map right now and avoid a propagation,
                        // but then we would still have to recheck it when popping the value off of the queue!
                        // so just avoid the double lookup
                        final long neighbourCoordinate = CoordinateUtils.getChunkKey(x + dx, z + dz);
                        this.addToIncreaseWorkQueue(neighbourCoordinate, neighbourLevel);
                    }
                }
            }
        }
    }

    protected void propagateDecreases() {
        for (int queueIndex = 63 ^ Long.numberOfLeadingZeros(this.levelRemoveWorkQueueBitset);
             this.levelRemoveWorkQueueBitset != 0L;
             this.levelRemoveWorkQueueBitset ^= (1L << queueIndex), queueIndex = 63 ^ Long.numberOfLeadingZeros(this.levelRemoveWorkQueueBitset)) {

            final WorkQueue queue = this.levelRemoveWorkQueues[queueIndex];
            while (!queue.queuedLevels.isEmpty()) {
                final long coordinate = queue.queuedCoordinates.removeFirstLong();
                final byte level = queue.queuedLevels.removeFirstByte();

                final byte currentLevel = this.levels.removeIfGreaterOrEqual(coordinate, level);
                if (currentLevel == 0) {
                    // something else removed
                    continue;
                }

                if (currentLevel > level) {
                    // something higher propagated here or we hit the propagation of another source
                    // in the second case we need to re-propagate because we could have just clobbered another source's
                    // propagation
                    this.addToIncreaseWorkQueue(coordinate, currentLevel, (byte)-currentLevel); // indicate to the increase code that the level's neighbours need checking
                    continue;
                }

                if (this.changeCallback != null) {
                    this.changeCallback.onLevelUpdate(coordinate, currentLevel, (byte)0);
                }

                final byte source = this.sources.get(coordinate);
                if (source != 0) {
                    // must re-propagate source later
                    this.addToIncreaseWorkQueue(coordinate, source);
                }

                if (level == 0) {
                    // can't propagate -1 to neighbours
                    // we have to check neighbours for removing 1 just in case the neighbour is 2
                    continue;
                }

                // propagate to neighbours
                final byte neighbourLevel = (byte)(level - 1);
                final int x = (int)coordinate;
                final int z = (int)(coordinate >>> 32);

                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        if ((dx | dz) == 0) {
                            // already propagated to coordinate
                            continue;
                        }

                        // sure we can check the neighbour level in the map right now and avoid a propagation,
                        // but then we would still have to recheck it when popping the value off of the queue!
                        // so just avoid the double lookup
                        final long neighbourCoordinate = CoordinateUtils.getChunkKey(x + dx, z + dz);
                        this.addToRemoveWorkQueue(neighbourCoordinate, neighbourLevel);
                    }
                }
            }
        }

        // propagate sources we clobbered in the process
        this.propagateIncreases();
    }

    protected static final class LevelMap extends Long2ByteOpenHashMap {
        public LevelMap() {
            super();
        }

        public LevelMap(final int expected, final float loadFactor) {
            super(expected, loadFactor);
        }

        // copied from superclass
        private int find(final long k) {
            if (k == 0L) {
                return this.containsNullKey ? this.n : -(this.n + 1);
            } else {
                final long[] key = this.key;
                long curr;
                int pos;
                if ((curr = key[pos = (int)HashCommon.mix(k) & this.mask]) == 0L) {
                    return -(pos + 1);
                } else if (k == curr) {
                    return pos;
                } else {
                    while((curr = key[pos = pos + 1 & this.mask]) != 0L) {
                        if (k == curr) {
                            return pos;
                        }
                    }

                    return -(pos + 1);
                }
            }
        }

        // copied from superclass
        private void insert(final int pos, final long k, final byte v) {
            if (pos == this.n) {
                this.containsNullKey = true;
            }

            this.key[pos] = k;
            this.value[pos] = v;
            if (this.size++ >= this.maxFill) {
                this.rehash(HashCommon.arraySize(this.size + 1, this.f));
            }
        }

        // copied from superclass
        public byte putIfGreater(final long key, final byte value) {
            final int pos = this.find(key);
            if (pos < 0) {
                if (this.defRetValue < value) {
                    this.insert(-pos - 1, key, value);
                }
                return this.defRetValue;
            } else {
                final byte curr = this.value[pos];
                if (value > curr) {
                    this.value[pos] = value;
                    return curr;
                }
                return curr;
            }
        }

        // copied from superclass
        private void removeEntry(final int pos) {
            --this.size;
            this.shiftKeys(pos);
            if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
                this.rehash(this.n / 2);
            }
        }

        // copied from superclass
        private void removeNullEntry() {
            this.containsNullKey = false;
            --this.size;
            if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
                this.rehash(this.n / 2);
            }
        }

        // copied from superclass
        public byte removeIfGreaterOrEqual(final long key, final byte value) {
            if (key == 0L) {
                if (!this.containsNullKey) {
                    return this.defRetValue;
                }
                final byte current = this.value[this.n];
                if (value >= current) {
                    this.removeNullEntry();
                    return current;
                }
                return current;
            } else {
                long[] keys = this.key;
                byte[] values = this.value;
                long curr;
                int pos;
                if ((curr = keys[pos = (int)HashCommon.mix(key) & this.mask]) == 0L) {
                    return this.defRetValue;
                } else if (key == curr) {
                    final byte current = values[pos];
                    if (value >= current) {
                        this.removeEntry(pos);
                        return current;
                    }
                    return current;
                } else {
                    while((curr = keys[pos = pos + 1 & this.mask]) != 0L) {
                        if (key == curr) {
                            final byte current = values[pos];
                            if (value >= current) {
                                this.removeEntry(pos);
                                return current;
                            }
                            return current;
                        }
                    }

                    return this.defRetValue;
                }
            }
        }
    }

    protected static final class WorkQueue {

        public final NoResizeLongArrayFIFODeque queuedCoordinates = new NoResizeLongArrayFIFODeque();
        public final NoResizeByteArrayFIFODeque queuedLevels = new NoResizeByteArrayFIFODeque();

    }

    protected static final class NoResizeLongArrayFIFODeque extends LongArrayFIFOQueue {

        /**
         * Assumes non-empty. If empty, undefined behaviour.
         */
        public long removeFirstLong() {
            // copied from superclass
            long t = this.array[this.start];
            if (++this.start == this.length) {
                this.start = 0;
            }

            return t;
        }
    }

    protected static final class NoResizeByteArrayFIFODeque extends ByteArrayFIFOQueue {

        /**
         * Assumes non-empty. If empty, undefined behaviour.
         */
        public byte removeFirstByte() {
            // copied from superclass
            byte t = this.array[this.start];
            if (++this.start == this.length) {
                this.start = 0;
            }

            return t;
        }
    }
}
