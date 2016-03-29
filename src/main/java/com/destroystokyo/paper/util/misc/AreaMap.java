package com.destroystokyo.paper.util.misc;

import com.destroystokyo.paper.util.math.IntegerUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import javax.annotation.Nullable;
import java.util.Iterator;

/** @author Spottedleaf */
public abstract class AreaMap<E> {

    /* Tested via https://gist.github.com/Spottedleaf/520419c6f41ef348fe9926ce674b7217 */

    protected final Object2LongOpenHashMap<E> objectToLastCoordinate = new Object2LongOpenHashMap<>();
    protected final Object2IntOpenHashMap<E> objectToViewDistance = new Object2IntOpenHashMap<>();

    {
        this.objectToViewDistance.defaultReturnValue(-1);
        this.objectToLastCoordinate.defaultReturnValue(Long.MIN_VALUE);
    }

    // we use linked for better iteration.
    // map of: coordinate to set of objects in coordinate
    protected final Long2ObjectOpenHashMap<PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E>> areaMap = new Long2ObjectOpenHashMap<>(1024, 0.7f);
    protected final PooledLinkedHashSets<E> pooledHashSets;

    protected final ChangeCallback<E> addCallback;
    protected final ChangeCallback<E> removeCallback;
    protected final ChangeSourceCallback<E> changeSourceCallback;

    public AreaMap() {
        this(new PooledLinkedHashSets<>());
    }

    // let users define a "global" or "shared" pooled sets if they wish
    public AreaMap(final PooledLinkedHashSets<E> pooledHashSets) {
        this(pooledHashSets, null, null);
    }

    public AreaMap(final PooledLinkedHashSets<E> pooledHashSets, final ChangeCallback<E> addCallback, final ChangeCallback<E> removeCallback) {
        this(pooledHashSets, addCallback, removeCallback, null);
    }
    public AreaMap(final PooledLinkedHashSets<E> pooledHashSets, final ChangeCallback<E> addCallback, final ChangeCallback<E> removeCallback, final ChangeSourceCallback<E> changeSourceCallback) {
        this.pooledHashSets = pooledHashSets;
        this.addCallback = addCallback;
        this.removeCallback = removeCallback;
        this.changeSourceCallback = changeSourceCallback;
    }

    @Nullable
    public final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> getObjectsInRange(final long key) {
        return this.areaMap.get(key);
    }

    @Nullable
    public final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> getObjectsInRange(final ChunkCoordIntPair chunkPos) {
        return this.areaMap.get(MCUtil.getCoordinateKey(chunkPos));
    }

    @Nullable
    public final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> getObjectsInRange(final int chunkX, final int chunkZ) {
        return this.areaMap.get(MCUtil.getCoordinateKey(chunkX, chunkZ));
    }

    // Long.MIN_VALUE indicates the object is not mapped
    public final long getLastCoordinate(final E object) {
        return this.objectToLastCoordinate.getOrDefault(object, Long.MIN_VALUE);
    }

    // -1 indicates the object is not mapped
    public final int getLastViewDistance(final E object) {
        return this.objectToViewDistance.getOrDefault(object, -1);
    }

    // returns the total number of mapped chunks
    public final int size() {
        return this.areaMap.size();
    }

    public final void addOrUpdate(final E object, final int chunkX, final int chunkZ, final int viewDistance) {
        final int oldViewDistance = this.objectToViewDistance.put(object, viewDistance);
        final long newPos = MCUtil.getCoordinateKey(chunkX, chunkZ);
        final long oldPos = this.objectToLastCoordinate.put(object, newPos);

        if (oldViewDistance == -1) {
            this.addObject(object, chunkX, chunkZ, Integer.MIN_VALUE, Integer.MIN_VALUE, viewDistance);
            this.addObjectCallback(object, chunkX, chunkZ, viewDistance);
        } else {
            this.updateObject(object, oldPos, newPos, oldViewDistance, viewDistance);
            this.updateObjectCallback(object, oldPos, newPos, oldViewDistance, viewDistance);
        }
        //this.validate(object, viewDistance);
    }

    public final boolean update(final E object, final int chunkX, final int chunkZ, final int viewDistance) {
        final int oldViewDistance = this.objectToViewDistance.replace(object, viewDistance);
        if (oldViewDistance == -1) {
            return false;
        } else {
            final long newPos = MCUtil.getCoordinateKey(chunkX, chunkZ);
            final long oldPos = this.objectToLastCoordinate.put(object, newPos);
            this.updateObject(object, oldPos, newPos, oldViewDistance, viewDistance);
            this.updateObjectCallback(object, oldPos, newPos, oldViewDistance, viewDistance);
        }
        //this.validate(object, viewDistance);
        return true;
    }

    // called after the distance map updates
    protected void updateObjectCallback(final E Object, final long oldPosition, final long newPosition, final int oldViewDistance, final int newViewDistance) {
        if (newPosition != oldPosition && this.changeSourceCallback != null) {
            this.changeSourceCallback.accept(Object, oldPosition, newPosition);
        }
    }

    public final boolean add(final E object, final int chunkX, final int chunkZ, final int viewDistance) {
        final int oldViewDistance = this.objectToViewDistance.putIfAbsent(object, viewDistance);
        if (oldViewDistance != -1) {
            return false;
        }

        final long newPos = MCUtil.getCoordinateKey(chunkX, chunkZ);
        this.objectToLastCoordinate.put(object, newPos);
        this.addObject(object, chunkX, chunkZ, Integer.MIN_VALUE, Integer.MIN_VALUE, viewDistance);
        this.addObjectCallback(object, chunkX, chunkZ, viewDistance);

        //this.validate(object, viewDistance);

        return true;
    }

    // called after the distance map updates
    protected void addObjectCallback(final E object, final int chunkX, final int chunkZ, final int viewDistance) {}

    public final boolean remove(final E object) {
        final long position = this.objectToLastCoordinate.removeLong(object);
        final int viewDistance = this.objectToViewDistance.removeInt(object);

        if (viewDistance == -1) {
            return false;
        }

        final int currentX = MCUtil.getCoordinateX(position);
        final int currentZ = MCUtil.getCoordinateZ(position);

        this.removeObject(object, currentX, currentZ, currentX, currentZ, viewDistance);
        this.removeObjectCallback(object, currentX, currentZ, viewDistance);
        //this.validate(object, -1);
        return true;
    }

    // called after the distance map updates
    protected void removeObjectCallback(final E object, final int chunkX, final int chunkZ, final int viewDistance) {}

    protected abstract PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> getEmptySetFor(final E object);

    // expensive op, only for debug
    protected void validate(final E object, final int viewDistance) {
        int entiesGot = 0;
        int expectedEntries = (2 * viewDistance + 1);
        expectedEntries *= expectedEntries;
        if (viewDistance < 0) {
            expectedEntries = 0;
        }

        final long currPosition = this.objectToLastCoordinate.getLong(object);

        final int centerX = MCUtil.getCoordinateX(currPosition);
        final int centerZ = MCUtil.getCoordinateZ(currPosition);

        for (Iterator<Long2ObjectLinkedOpenHashMap.Entry<PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E>>> iterator = this.areaMap.long2ObjectEntrySet().fastIterator();
             iterator.hasNext();) {

            final Long2ObjectLinkedOpenHashMap.Entry<PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E>> entry = iterator.next();
            final long key = entry.getLongKey();
            final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> map = entry.getValue();

            if (map.referenceCount == 0) {
                throw new IllegalStateException("Invalid map");
            }

            if (map.contains(object)) {
                ++entiesGot;

                final int chunkX = MCUtil.getCoordinateX(key);
                final int chunkZ = MCUtil.getCoordinateZ(key);

                final int dist = Math.max(IntegerUtil.branchlessAbs(chunkX - centerX), IntegerUtil.branchlessAbs(chunkZ - centerZ));

                if (dist > viewDistance) {
                    throw new IllegalStateException("Expected view distance " + viewDistance + ", got " + dist);
                }
            }
        }

        if (entiesGot != expectedEntries) {
            throw new IllegalStateException("Expected " + expectedEntries + ", got " + entiesGot);
        }
    }

    private void addObjectTo(final E object, final int chunkX, final int chunkZ, final int currChunkX,
                             final int currChunkZ, final int prevChunkX, final int prevChunkZ) {
        final long key = MCUtil.getCoordinateKey(chunkX, chunkZ);

        PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> empty = this.getEmptySetFor(object);
        PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> current = this.areaMap.putIfAbsent(key, empty);

        if (current != null) {
            PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> next = this.pooledHashSets.findMapWith(current, object);
            if (next == current) {
                throw new IllegalStateException("Expected different map: got " + next.toString());
            }
            this.areaMap.put(key, next);

            current = next;
            // fall through to callback
        } else {
            current = empty;
        }

        if (this.addCallback != null) {
            try {
                this.addCallback.accept(object, chunkX, chunkZ, currChunkX, currChunkZ, prevChunkX, prevChunkZ, current);
            } catch (final Throwable ex) {
                if (ex instanceof ThreadDeath) {
                    throw (ThreadDeath)ex;
                }
                MinecraftServer.LOGGER.error("Add callback for map threw exception ", ex);
            }
        }
    }

    private void removeObjectFrom(final E object, final int chunkX, final int chunkZ, final int currChunkX,
                                  final int currChunkZ, final int prevChunkX, final int prevChunkZ) {
        final long key = MCUtil.getCoordinateKey(chunkX, chunkZ);

        PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> current = this.areaMap.get(key);

        if (current == null) {
            throw new IllegalStateException("Current map may not be null for " + object + ", (" + chunkX + "," + chunkZ + ")");
        }

        PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> next = this.pooledHashSets.findMapWithout(current, object);

        if (next == current) {
            throw new IllegalStateException("Current map [" + next.toString() + "] should have contained " + object + ", (" + chunkX + "," + chunkZ + ")");
        }

        if (next != null) {
            this.areaMap.put(key, next);
        } else {
            this.areaMap.remove(key);
        }

        if (this.removeCallback != null) {
            try {
                this.removeCallback.accept(object, chunkX, chunkZ, currChunkX, currChunkZ, prevChunkX, prevChunkZ, next);
            } catch (final Throwable ex) {
                if (ex instanceof ThreadDeath) {
                    throw (ThreadDeath)ex;
                }
                MinecraftServer.LOGGER.error("Remove callback for map threw exception ", ex);
            }
        }
    }

    private void addObject(final E object, final int chunkX, final int chunkZ, final int prevChunkX, final int prevChunkZ, final int viewDistance) {
        final int maxX = chunkX + viewDistance;
        final int maxZ = chunkZ + viewDistance;
        final int minX = chunkX - viewDistance;
        final int minZ = chunkZ - viewDistance;
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                this.addObjectTo(object, x, z, chunkX, chunkZ, prevChunkX, prevChunkZ);
            }
        }
    }

    private void removeObject(final E object, final int chunkX, final int chunkZ, final int currentChunkX, final int currentChunkZ, final int viewDistance) {
        final int maxX = chunkX + viewDistance;
        final int maxZ = chunkZ + viewDistance;
        final int minX = chunkX - viewDistance;
        final int minZ = chunkZ - viewDistance;
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                this.removeObjectFrom(object, x, z, currentChunkX, currentChunkZ, chunkX, chunkZ);
            }
        }
    }

    /* math sign function except 0 returns 1 */
    protected static int sign(int val) {
        return 1 | (val >> (Integer.SIZE - 1));
    }

    private void updateObject(final E object, final long oldPosition, final long newPosition, final int oldViewDistance, final int newViewDistance) {
        final int toX = MCUtil.getCoordinateX(newPosition);
        final int toZ = MCUtil.getCoordinateZ(newPosition);
        final int fromX = MCUtil.getCoordinateX(oldPosition);
        final int fromZ = MCUtil.getCoordinateZ(oldPosition);

        final int dx = toX - fromX;
        final int dz = toZ - fromZ;

        final int totalX = IntegerUtil.branchlessAbs(fromX - toX);
        final int totalZ = IntegerUtil.branchlessAbs(fromZ - toZ);

        if (Math.max(totalX, totalZ) > (2 * Math.max(newViewDistance, oldViewDistance))) {
            // teleported?
            this.removeObject(object, fromX, fromZ, fromX, fromZ, oldViewDistance);
            this.addObject(object, toX, toZ, fromX, fromZ, newViewDistance);
            return;
        }

        if (oldViewDistance != newViewDistance) {
            // remove loop

            final int oldMinX = fromX - oldViewDistance;
            final int oldMinZ = fromZ - oldViewDistance;
            final int oldMaxX = fromX + oldViewDistance;
            final int oldMaxZ = fromZ + oldViewDistance;
            for (int currX = oldMinX; currX <= oldMaxX; ++currX) {
                for (int currZ = oldMinZ; currZ <= oldMaxZ; ++currZ) {

                    // only remove if we're outside the new view distance...
                    if (Math.max(IntegerUtil.branchlessAbs(currX - toX), IntegerUtil.branchlessAbs(currZ - toZ)) > newViewDistance) {
                        this.removeObjectFrom(object, currX, currZ, toX, toZ, fromX, fromZ);
                    }
                }
            }

            // add loop

            final int newMinX = toX - newViewDistance;
            final int newMinZ = toZ - newViewDistance;
            final int newMaxX = toX + newViewDistance;
            final int newMaxZ = toZ + newViewDistance;
            for (int currX = newMinX; currX <= newMaxX; ++currX) {
                for (int currZ = newMinZ; currZ <= newMaxZ; ++currZ) {

                    // only add if we're outside the old view distance...
                    if (Math.max(IntegerUtil.branchlessAbs(currX - fromX), IntegerUtil.branchlessAbs(currZ - fromZ)) > oldViewDistance) {
                        this.addObjectTo(object, currX, currZ, toX, toZ, fromX, fromZ);
                    }
                }
            }

            return;
        }

        // x axis is width
        // z axis is height
        // right refers to the x axis of where we moved
        // top refers to the z axis of where we moved

        // same view distance

        // used for relative positioning
        final int up = sign(dz); // 1 if dz >= 0, -1 otherwise
        final int right = sign(dx); // 1 if dx >= 0, -1 otherwise

        // The area excluded by overlapping the two view distance squares creates four rectangles:
        // Two on the left, and two on the right. The ones on the left we consider the "removed" section
        // and on the right the "added" section.
        // https://i.imgur.com/MrnOBgI.png is a reference image. Note that the outside border is not actually
        // exclusive to the regions they surround.

        // 4 points of the rectangle
        int maxX; // exclusive
        int minX; // inclusive
        int maxZ; // exclusive
        int minZ; // inclusive

        if (dx != 0) {
            // handle right addition

            maxX = toX + (oldViewDistance * right) + right; // exclusive
            minX = fromX + (oldViewDistance * right) + right; // inclusive
            maxZ = fromZ + (oldViewDistance * up) + up; // exclusive
            minZ = toZ - (oldViewDistance * up); // inclusive

            for (int currX = minX; currX != maxX; currX += right) {
                for (int currZ = minZ; currZ != maxZ; currZ += up) {
                    this.addObjectTo(object, currX, currZ, toX, toZ, fromX, fromZ);
                }
            }
        }

        if (dz != 0) {
            // handle up addition

            maxX = toX + (oldViewDistance * right) + right; // exclusive
            minX = toX - (oldViewDistance * right); // inclusive
            maxZ = toZ + (oldViewDistance * up) + up; // exclusive
            minZ = fromZ + (oldViewDistance * up) + up; // inclusive

            for (int currX = minX; currX != maxX; currX += right) {
                for (int currZ = minZ; currZ != maxZ; currZ += up) {
                    this.addObjectTo(object, currX, currZ, toX, toZ, fromX, fromZ);
                }
            }
        }

        if (dx != 0) {
            // handle left removal

            maxX = toX - (oldViewDistance * right); // exclusive
            minX = fromX - (oldViewDistance * right); // inclusive
            maxZ = fromZ + (oldViewDistance * up) + up; // exclusive
            minZ = toZ - (oldViewDistance * up); // inclusive

            for (int currX = minX; currX != maxX; currX += right) {
                for (int currZ = minZ; currZ != maxZ; currZ += up) {
                    this.removeObjectFrom(object, currX, currZ, toX, toZ, fromX, fromZ);
                }
            }
        }

        if (dz != 0) {
            // handle down removal

            maxX = fromX + (oldViewDistance * right) + right; // exclusive
            minX = fromX - (oldViewDistance * right); // inclusive
            maxZ = toZ - (oldViewDistance * up); // exclusive
            minZ = fromZ - (oldViewDistance * up); // inclusive

            for (int currX = minX; currX != maxX; currX += right) {
                for (int currZ = minZ; currZ != maxZ; currZ += up) {
                    this.removeObjectFrom(object, currX, currZ, toX, toZ, fromX, fromZ);
                }
            }
        }
    }

    @FunctionalInterface
    public static interface ChangeCallback<E> {

        // if there is no previous position, then prevPos = Integer.MIN_VALUE
        void accept(final E object, final int rangeX, final int rangeZ, final int currPosX, final int currPosZ, final int prevPosX, final int prevPosZ,
                    final PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<E> newState);

    }

    @FunctionalInterface
    public static interface ChangeSourceCallback<E> {
        void accept(final E object, final long prevPos, final long newPos);
    }
}
