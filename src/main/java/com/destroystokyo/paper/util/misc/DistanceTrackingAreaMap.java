package com.destroystokyo.paper.util.misc;

import com.destroystokyo.paper.util.math.IntegerUtil;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.MCUtil;

/** @author Spottedleaf */
public abstract class DistanceTrackingAreaMap<E> extends AreaMap<E> {

    // use this map only if you need distance tracking, the tracking here is obviously going to hit harder.

    protected final Long2IntOpenHashMap chunkToNearestDistance = new Long2IntOpenHashMap(1024, 0.7f);
    {
        this.chunkToNearestDistance.defaultReturnValue(-1);
    }

    protected final DistanceChangeCallback<E> distanceChangeCallback;

    public DistanceTrackingAreaMap() {
        this(new PooledLinkedHashSets<>());
    }

    // let users define a "global" or "shared" pooled sets if they wish
    public DistanceTrackingAreaMap(final PooledLinkedHashSets<E> pooledHashSets) {
        this(pooledHashSets, null, null, null);
    }

    public DistanceTrackingAreaMap(final PooledLinkedHashSets<E> pooledHashSets, final ChangeCallback<E> addCallback, final ChangeCallback<E> removeCallback,
                                   final DistanceChangeCallback<E> distanceChangeCallback) {
        super(pooledHashSets, addCallback, removeCallback);
        this.distanceChangeCallback = distanceChangeCallback;
    }

    // ret -1 if there is nothing mapped
    public final int getNearestObjectDistance(final long key) {
        return this.chunkToNearestDistance.get(key);
    }

    // ret -1 if there is nothing mapped
    public final int getNearestObjectDistance(final ChunkCoordIntPair chunkPos) {
        return this.chunkToNearestDistance.get(MCUtil.getCoordinateKey(chunkPos));
    }

    // ret -1 if there is nothing mapped
    public final int getNearestObjectDistance(final int chunkX, final int chunkZ) {
        return this.chunkToNearestDistance.get(MCUtil.getCoordinateKey(chunkX, chunkZ));
    }

    protected final void recalculateDistance(final int chunkX, final int chunkZ) {
        final long key = MCUtil.getCoordinateKey(chunkX, chunkZ);
        final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> state = this.areaMap.get(key);
        if (state == null) {
            final int oldDistance = this.chunkToNearestDistance.remove(key);
            // nothing here.
            if (oldDistance == -1) {
                // nothing was here previously
                return;
            }
            if (this.distanceChangeCallback != null) {
                this.distanceChangeCallback.accept(chunkX, chunkZ, oldDistance, -1, null);
            }
            return;
        }

        int newDistance = Integer.MAX_VALUE;

        final Object[] rawData = state.getBackingSet();
        for (int i = 0, len = rawData.length; i < len; ++i) {
            final Object raw = rawData[i];

            if (raw == null) {
                continue;
            }

            final E object = (E)raw;
            final long location = this.objectToLastCoordinate.getLong(object);

            final int distance = Math.max(IntegerUtil.branchlessAbs(chunkX - MCUtil.getCoordinateX(location)), IntegerUtil.branchlessAbs(chunkZ - MCUtil.getCoordinateZ(location)));

            if (distance < newDistance) {
                newDistance = distance;
            }
        }

        final int oldDistance = this.chunkToNearestDistance.put(key, newDistance);

        if (oldDistance != newDistance) {
            if (this.distanceChangeCallback != null) {
                this.distanceChangeCallback.accept(chunkX, chunkZ, oldDistance, newDistance, state);
            }
        }
    }

    @Override
    protected void addObjectCallback(final E object, final int chunkX, final int chunkZ, final int viewDistance) {
        final int maxX = chunkX + viewDistance;
        final int maxZ = chunkZ + viewDistance;
        final int minX = chunkX - viewDistance;
        final int minZ = chunkZ - viewDistance;
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                this.recalculateDistance(x, z);
            }
        }
    }

    @Override
    protected void removeObjectCallback(final E object, final int chunkX, final int chunkZ, final int viewDistance) {
        final int maxX = chunkX + viewDistance;
        final int maxZ = chunkZ + viewDistance;
        final int minX = chunkX - viewDistance;
        final int minZ = chunkZ - viewDistance;
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                this.recalculateDistance(x, z);
            }
        }
    }

    @Override
    protected void updateObjectCallback(final E object, final long oldPosition, final long newPosition, final int oldViewDistance, final int newViewDistance) {
        if (oldPosition == newPosition && newViewDistance == oldViewDistance) {
            return;
        }

        final int toX = MCUtil.getCoordinateX(newPosition);
        final int toZ = MCUtil.getCoordinateZ(newPosition);
        final int fromX = MCUtil.getCoordinateX(oldPosition);
        final int fromZ = MCUtil.getCoordinateZ(oldPosition);

        final int totalX = IntegerUtil.branchlessAbs(fromX - toX);
        final int totalZ = IntegerUtil.branchlessAbs(fromZ - toZ);

        if (Math.max(totalX, totalZ) > (2 * Math.max(newViewDistance, oldViewDistance))) {
            // teleported?
            this.removeObjectCallback(object, fromX, fromZ, oldViewDistance);
            this.addObjectCallback(object, toX, toZ, newViewDistance);
            return;
        }

        final int minX = Math.min(fromX - oldViewDistance, toX - newViewDistance);
        final int maxX = Math.max(fromX + oldViewDistance, toX + newViewDistance);
        final int minZ = Math.min(fromZ - oldViewDistance, toZ - newViewDistance);
        final int maxZ = Math.max(fromZ + oldViewDistance, toZ + newViewDistance);

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                final int distXOld = IntegerUtil.branchlessAbs(x - fromX);
                final int distZOld = IntegerUtil.branchlessAbs(z - fromZ);

                if (Math.max(distXOld, distZOld) <= oldViewDistance) {
                    this.recalculateDistance(x, z);
                    continue;
                }

                final int distXNew = IntegerUtil.branchlessAbs(x - toX);
                final int distZNew = IntegerUtil.branchlessAbs(z - toZ);

                if (Math.max(distXNew, distZNew) <= newViewDistance) {
                    this.recalculateDistance(x, z);
                    continue;
                }
            }
        }
    }

    @FunctionalInterface
    public static interface DistanceChangeCallback<E> {

        void accept(final int posX, final int posZ, final int oldNearestDistance, final int newNearestDistance,
                    final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> state);

    }
}
