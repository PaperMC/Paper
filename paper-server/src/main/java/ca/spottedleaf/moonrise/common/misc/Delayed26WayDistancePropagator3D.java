package ca.spottedleaf.moonrise.common.misc;

import ca.spottedleaf.moonrise.common.util.CoordinateUtils;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;

public final class Delayed26WayDistancePropagator3D {

    // this map is considered "stale" unless updates are propagated.
    protected final Delayed8WayDistancePropagator2D.LevelMap levels = new Delayed8WayDistancePropagator2D.LevelMap(8192*2, 0.6f);

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

    public Delayed26WayDistancePropagator3D() {
        this(null);
    }

    public Delayed26WayDistancePropagator3D(final LevelChangeCallback changeCallback) {
        this.changeCallback = changeCallback;
    }

    public int getLevel(final long pos) {
        return this.levels.get(pos);
    }

    public int getLevel(final int x, final int y, final int z) {
        return this.levels.get(CoordinateUtils.getChunkSectionKey(x, y, z));
    }

    public void setSource(final int x, final int y, final int z, final int level) {
        this.setSource(CoordinateUtils.getChunkSectionKey(x, y, z), level);
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

    public void removeSource(final int x, final int y, final int z) {
        this.removeSource(CoordinateUtils.getChunkSectionKey(x, y, z));
    }

    public void removeSource(final long coordinate) {
        if (this.sources.remove(coordinate) != 0) {
            this.updatedSources.add(coordinate);
        }
    }

    // queues used for BFS propagating levels
    protected final Delayed8WayDistancePropagator2D.WorkQueue[] levelIncreaseWorkQueues = new Delayed8WayDistancePropagator2D.WorkQueue[64];
    {
        for (int i = 0; i < this.levelIncreaseWorkQueues.length; ++i) {
            this.levelIncreaseWorkQueues[i] = new Delayed8WayDistancePropagator2D.WorkQueue();
        }
    }
    protected final Delayed8WayDistancePropagator2D.WorkQueue[] levelRemoveWorkQueues = new Delayed8WayDistancePropagator2D.WorkQueue[64];
    {
        for (int i = 0; i < this.levelRemoveWorkQueues.length; ++i) {
            this.levelRemoveWorkQueues[i] = new Delayed8WayDistancePropagator2D.WorkQueue();
        }
    }
    protected long levelIncreaseWorkQueueBitset;
    protected long levelRemoveWorkQueueBitset;

    protected final void addToIncreaseWorkQueue(final long coordinate, final byte level) {
        final Delayed8WayDistancePropagator2D.WorkQueue queue = this.levelIncreaseWorkQueues[level];
        queue.queuedCoordinates.enqueue(coordinate);
        queue.queuedLevels.enqueue(level);

        this.levelIncreaseWorkQueueBitset |= (1L << level);
    }

    protected final void addToIncreaseWorkQueue(final long coordinate, final byte index, final byte level) {
        final Delayed8WayDistancePropagator2D.WorkQueue queue = this.levelIncreaseWorkQueues[index];
        queue.queuedCoordinates.enqueue(coordinate);
        queue.queuedLevels.enqueue(level);

        this.levelIncreaseWorkQueueBitset |= (1L << index);
    }

    protected final void addToRemoveWorkQueue(final long coordinate, final byte level) {
        final Delayed8WayDistancePropagator2D.WorkQueue queue = this.levelRemoveWorkQueues[level];
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

            final Delayed8WayDistancePropagator2D.WorkQueue queue = this.levelIncreaseWorkQueues[queueIndex];
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
                final int x = CoordinateUtils.getChunkSectionX(coordinate);
                final int y = CoordinateUtils.getChunkSectionY(coordinate);
                final int z = CoordinateUtils.getChunkSectionZ(coordinate);

                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        for (int dx = -1; dx <= 1; ++dx) {
                            if ((dy | dz | dx) == 0) {
                                // already propagated to coordinate
                                continue;
                            }

                            // sure we can check the neighbour level in the map right now and avoid a propagation,
                            // but then we would still have to recheck it when popping the value off of the queue!
                            // so just avoid the double lookup
                            final long neighbourCoordinate = CoordinateUtils.getChunkSectionKey(dx + x, dy + y, dz + z);
                            this.addToIncreaseWorkQueue(neighbourCoordinate, neighbourLevel);
                        }
                    }
                }
            }
        }
    }

    protected void propagateDecreases() {
        for (int queueIndex = 63 ^ Long.numberOfLeadingZeros(this.levelRemoveWorkQueueBitset);
             this.levelRemoveWorkQueueBitset != 0L;
             this.levelRemoveWorkQueueBitset ^= (1L << queueIndex), queueIndex = 63 ^ Long.numberOfLeadingZeros(this.levelRemoveWorkQueueBitset)) {

            final Delayed8WayDistancePropagator2D.WorkQueue queue = this.levelRemoveWorkQueues[queueIndex];
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
                final int x = CoordinateUtils.getChunkSectionX(coordinate);
                final int y = CoordinateUtils.getChunkSectionY(coordinate);
                final int z = CoordinateUtils.getChunkSectionZ(coordinate);

                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        for (int dx = -1; dx <= 1; ++dx) {
                            if ((dy | dz | dx) == 0) {
                                // already propagated to coordinate
                                continue;
                            }

                            // sure we can check the neighbour level in the map right now and avoid a propagation,
                            // but then we would still have to recheck it when popping the value off of the queue!
                            // so just avoid the double lookup
                            final long neighbourCoordinate = CoordinateUtils.getChunkSectionKey(dx + x, dy + y, dz + z);
                            this.addToRemoveWorkQueue(neighbourCoordinate, neighbourLevel);
                        }
                    }
                }
            }
        }

        // propagate sources we clobbered in the process
        this.propagateIncreases();
    }
}
