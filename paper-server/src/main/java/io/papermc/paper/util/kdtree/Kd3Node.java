package io.papermc.paper.util.kdtree;

import net.minecraft.world.phys.Vec3;

public final class Kd3Node {
    private final int dimension;
    private final Vec3 point;
    private final double pointAtDimension;
    private final Kd3Node below;
    private final Kd3Node above;

    public Kd3Node(int dimension, Vec3 point, Kd3Node below, Kd3Node above) {
        this.dimension = dimension;
        this.point = point;
        this.pointAtDimension = getDimension(point, dimension);
        this.below = below;
        this.above = above;
    }

    public static Kd3Node create(int dimension, Vec3[] points, int left, int right) {
        if (right < left) {
            return null;
        } else if (right == left) {
            return new Kd3Node(dimension, points[left], null, null);
        }

        int m = 1 + (right - left) / 2;
        select(points, dimension, m, left, right);
        final int nextDimension = (dimension + 1) % 3;
        return new Kd3Node(
            dimension,
            points[left + m - 1],
            Kd3Node.create(nextDimension, points, left, left + m - 2),
            Kd3Node.create(nextDimension, points, left + m, right));
    }

    private double shorter(Vec3 target, double min) {
        double minsq = min * min;
        double maxV = minsq;

        for (int i = 0; i < 3; i++) {
            double d = getDimension(target, i) - getDimension(point, i);
            if ((maxV -= d * d) < 0) {
                return -1;
            }
        }
        return Math.sqrt(minsq - maxV);
    }

    public Vec3 nearest(Vec3 target, double[] min) {
        Vec3 result = null;

        double d = shorter(target, min[0]);
        if (d >= 0 && d < min[0]) {
            min[0] = d;
            result = point;
        }

        double dp = Math.abs(this.pointAtDimension - getDimension(target, dimension));
        Vec3 newResult = null;

        if (dp < min[0]) {
            if (above != null) {
                newResult = above.nearest(target, min);
                if (newResult != null) {
                    result = newResult;
                }
            }

            if (below != null) {
                newResult = below.nearest(target, min);
                if (newResult != null) {
                    result = newResult;
                }
            }
        } else {
            if (getDimension(target, dimension) < this.pointAtDimension) {
                if (below != null) {
                    newResult = below.nearest(target, min);
                }
            } else {
                if (above != null) {
                    newResult = above.nearest(target, min);
                }
            }

            if (newResult != null) {
                return newResult;
            }
        }
        return result;
    }

    public boolean isBelow(Vec3 point) {
        return getDimension(point, dimension) < this.pointAtDimension;
    }

    public Kd3Node getBelow() {
        return below;
    }

    public Kd3Node getAbove() {
        return above;
    }

    public Vec3 getPoint() {
        return point;
    }

    private static double getDimension(Vec3 point, int dimension) {
        if (dimension == 0) {
            return point.x();
        } else if (dimension == 1) {
            return point.z();
        } else if (dimension == 2) {
            return point.y();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static void swap(Vec3[] points, int pos1, int pos2) {
        if (pos1 == pos2) {
            return;
        }

        Vec3 tmp = points[pos1];
        points[pos1] = points[pos2];
        points[pos2] = tmp;
    }

    private static int partition(Vec3[] points, int dimension, int left, int right, int pivotIndex) {
        final Vec3 pivot = points[pivotIndex];
        swap(points, right, pivotIndex);

        int store = left;
        for (int i = left; i < right; i++) {
            if (getDimension(points[i], dimension) <= getDimension(pivot, dimension)) {
                swap(points, i, store);
                store++;
            }
        }

        swap(points, right, store);
        return store;
    }

    private static int selectPivotIndex(Vec3[] points, int dimension, int left, int right) {
        int midIndex = (left + right) / 2;

        int lowIndex = left;

        if (getDimension(points[lowIndex], dimension) >= getDimension(points[midIndex], dimension)) {
            lowIndex = midIndex;
            midIndex = left;
        }

        if (getDimension(points[right], dimension) <= getDimension(points[lowIndex], dimension)) {
            return lowIndex;
        } else if (getDimension(points[right], dimension) <= getDimension(points[midIndex], dimension)) {
            return midIndex;
        }

        return right;
    }

    private static void select(Vec3[] points, int dimension, int k, int left, int right) {
        while (true) {
            int idx = selectPivotIndex(points, dimension, left, right);

            int pivotIndex = partition(points, dimension, left, right, idx);
            if (left + k - 1 == pivotIndex) {
                return;
            }

            if (left + k - 1 < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                k -= (pivotIndex - left + 1);
                left = pivotIndex + 1;
            }
        }
    }
}
