package ca.spottedleaf.moonrise.common.misc;

import ca.spottedleaf.moonrise.common.util.CoordinateUtils;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceSet;

public final class PositionCountingAreaMap<T> {

    private final Reference2ReferenceOpenHashMap<T, PositionCounter> counters = new Reference2ReferenceOpenHashMap<>();
    private final Long2IntOpenHashMap positions = new Long2IntOpenHashMap();

    public ReferenceSet<T> getObjects() {
        return this.counters.keySet();
    }

    public LongSet getPositions() {
        return this.positions.keySet();
    }

    public int getTotalPositions() {
        return this.positions.size();
    }

    public boolean hasObjectsNear(final long pos) {
        return this.positions.containsKey(pos);
    }

    public boolean hasObjectsNear(final int toX, final int toZ) {
        return this.positions.containsKey(CoordinateUtils.getChunkKey(toX, toZ));
    }

    public int getObjectsNear(final int toX, final int toZ) {
        return this.positions.get(CoordinateUtils.getChunkKey(toX, toZ));
    }

    public boolean add(final T parameter, final int toX, final int toZ, final int distance) {
        final PositionCounter existing = this.counters.get(parameter);
        if (existing != null) {
            return false;
        }

        final PositionCounter counter = new PositionCounter(parameter);

        this.counters.put(parameter, counter);

        return counter.add(toX, toZ, distance);
    }

    public boolean addOrUpdate(final T parameter, final int toX, final int toZ, final int distance) {
        final PositionCounter existing = this.counters.get(parameter);
        if (existing != null) {
            return existing.update(toX, toZ, distance);
        }

        final PositionCounter counter = new PositionCounter(parameter);

        this.counters.put(parameter, counter);

        return counter.add(toX, toZ, distance);
    }

    public boolean remove(final T parameter) {
        final PositionCounter counter = this.counters.remove(parameter);
        if (counter == null) {
            return false;
        }

        counter.remove();

        return true;
    }

    public boolean update(final T parameter, final int toX, final int toZ, final int distance) {
        final PositionCounter counter = this.counters.get(parameter);
        if (counter == null) {
            return false;
        }

        return counter.update(toX, toZ, distance);
    }

    private final class PositionCounter extends SingleUserAreaMap<T> {

        public PositionCounter(final T parameter) {
            super(parameter);
        }

        @Override
        protected void addCallback(final T parameter, final int toX, final int toZ) {
            PositionCountingAreaMap.this.positions.addTo(CoordinateUtils.getChunkKey(toX, toZ), 1);
        }

        @Override
        protected void removeCallback(final T parameter, final int toX, final int toZ) {
            final long key = CoordinateUtils.getChunkKey(toX, toZ);
            if (PositionCountingAreaMap.this.positions.addTo(key, -1) == 1) {
                PositionCountingAreaMap.this.positions.remove(key);
            }
        }
    }
}
