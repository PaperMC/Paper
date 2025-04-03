package ca.spottedleaf.moonrise.common.misc;

public abstract class SingleUserAreaMap<T> {

    public static final int NOT_SET = Integer.MIN_VALUE;

    private final T parameter;
    private int lastChunkX = NOT_SET;
    private int lastChunkZ = NOT_SET;
    private int distance = NOT_SET;

    public SingleUserAreaMap(final T parameter) {
        this.parameter = parameter;
    }

    public final T getParameter() {
        return this.parameter;
    }

    public final int getLastChunkX() {
        return this.lastChunkX;
    }

    public final int getLastChunkZ() {
        return this.lastChunkZ;
    }

    public final int getLastDistance() {
        return this.distance;
    }

    /* math sign function except 0 returns 1 */
    protected static int sign(int val) {
        return 1 | (val >> (Integer.SIZE - 1));
    }

    protected abstract void addCallback(final T parameter, final int chunkX, final int chunkZ);

    protected abstract void removeCallback(final T parameter, final int chunkX, final int chunkZ);

    private void addToNew(final T parameter, final int chunkX, final int chunkZ, final int distance) {
        final int maxX = chunkX + distance;
        final int maxZ = chunkZ + distance;

        for (int cx = chunkX - distance; cx <= maxX; ++cx) {
            for (int cz = chunkZ - distance; cz <= maxZ; ++cz) {
                this.addCallback(parameter, cx, cz);
            }
        }
    }

    private void removeFromOld(final T parameter, final int chunkX, final int chunkZ, final int distance) {
        final int maxX = chunkX + distance;
        final int maxZ = chunkZ + distance;

        for (int cx = chunkX - distance; cx <= maxX; ++cx) {
            for (int cz = chunkZ - distance; cz <= maxZ; ++cz) {
                this.removeCallback(parameter, cx, cz);
            }
        }
    }

    public final boolean add(final int chunkX, final int chunkZ, final int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(Integer.toString(distance));
        }
        if (this.lastChunkX != NOT_SET) {
            return false;
        }
        this.lastChunkX = chunkX;
        this.lastChunkZ = chunkZ;
        this.distance = distance;

        this.addToNew(this.parameter, chunkX, chunkZ, distance);

        return true;
    }

    public final boolean update(final int toX, final int toZ, final int newViewDistance) {
        if (newViewDistance < 0) {
            throw new IllegalArgumentException(Integer.toString(newViewDistance));
        }
        final int fromX = this.lastChunkX;
        final int fromZ = this.lastChunkZ;
        final int oldViewDistance = this.distance;
        if (fromX == NOT_SET) {
            return false;
        }

        this.lastChunkX = toX;
        this.lastChunkZ = toZ;
        this.distance = newViewDistance;

        final T parameter = this.parameter;


        final int dx = toX - fromX;
        final int dz = toZ - fromZ;

        final int totalX = Math.abs(fromX - toX);
        final int totalZ = Math.abs(fromZ - toZ);

        if (Math.max(totalX, totalZ) > (2 * Math.max(newViewDistance, oldViewDistance))) {
            // teleported
            this.removeFromOld(parameter, fromX, fromZ, oldViewDistance);
            this.addToNew(parameter, toX, toZ, newViewDistance);
            return true;
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
                    if (Math.max(Math.abs(currX - toX), Math.abs(currZ - toZ)) > newViewDistance) {
                        this.removeCallback(parameter, currX, currZ);
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
                    if (Math.max(Math.abs(currX - fromX), Math.abs(currZ - fromZ)) > oldViewDistance) {
                        this.addCallback(parameter, currX, currZ);
                    }
                }
            }

            return true;
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
                    this.addCallback(parameter, currX, currZ);
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
                    this.addCallback(parameter, currX, currZ);
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
                    this.removeCallback(parameter, currX, currZ);
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
                    this.removeCallback(parameter, currX, currZ);
                }
            }
        }

        return true;
    }

    public final boolean remove() {
        final int chunkX = this.lastChunkX;
        final int chunkZ = this.lastChunkZ;
        final int distance = this.distance;
        if (chunkX == NOT_SET) {
            return false;
        }

        this.lastChunkX = this.lastChunkZ = this.distance = NOT_SET;

        this.removeFromOld(this.parameter, chunkX, chunkZ, distance);

        return true;
    }
}
