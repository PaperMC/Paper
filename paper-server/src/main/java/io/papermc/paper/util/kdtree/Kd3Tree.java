package io.papermc.paper.util.kdtree;

import net.minecraft.world.phys.Vec3;

public class Kd3Tree {
    private final Kd3Node root;

    public Kd3Tree(Vec3[] points) {
        if (points.length > 0) {
            this.root = Kd3Node.create(0, points, 0, points.length - 1);
        } else {
            this.root = null;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Vec3 nearest(Vec3 target) {
        if (root == null) {
            return null;
        }
        Kd3Node parent = parent(target);
        Vec3 result = parent.getPoint();
        double smallest = target.distanceTo(result);

        double[] best = new double[]{smallest};

        Vec3 nearest = root.nearest(target, best);
        if (nearest != null) {
            return nearest;
        }
        return result;
    }

    private Kd3Node parent(Vec3 value) {
        Kd3Node node = root;
        Kd3Node next;
        while (true) {
            if (node.isBelow(value)) {
                next = node.getBelow();
            } else {
                next = node.getAbove();
            }
            if (next == null) {
                break;
            } else {
                node = next;
            }
        }

        return node;
    }
}
