package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class Pathfinder {

    private final PathPoint[] a = new PathPoint[32];
    private final int b;
    private final PathfinderAbstract c; public PathfinderAbstract getPathfinder() { return this.c; }  // Paper - OBFHELPER
    private final Path d = new Path();

    public Pathfinder(PathfinderAbstract pathfinderabstract, int i) {
        this.c = pathfinderabstract;
        this.b = i;
    }

    @Nullable
    public PathEntity a(ChunkCache chunkcache, EntityInsentient entityinsentient, Set<BlockPosition> set, float f, int i, float f1) {
        this.d.a();
        this.c.a(chunkcache, entityinsentient);
        PathPoint pathpoint = this.c.b();
        // Paper start - remove streams - and optimize collection
        List<Map.Entry<PathDestination, BlockPosition>> map = Lists.newArrayList();
        for (BlockPosition blockposition : set) {
            map.add(new java.util.AbstractMap.SimpleEntry<>(this.c.a((double) blockposition.getX(), blockposition.getY(), blockposition.getZ()), blockposition));
        }
        // Paper end
        PathEntity pathentity = this.a(pathpoint, map, f, i, f1);

        this.c.a();
        return pathentity;
    }

    @Nullable
    private PathEntity a(PathPoint pathpoint, List<Map.Entry<PathDestination, BlockPosition>> list, float f, int i, float f1) { // Paper - optimize collection
        //Set<PathDestination> set = map.keySet(); // Paper

        pathpoint.e = 0.0F;
        pathpoint.f = this.a(pathpoint, list); // Paper - optimize collection
        pathpoint.g = pathpoint.f;
        this.d.a();
        this.d.a(pathpoint);
        Set<PathPoint> set1 = ImmutableSet.of();
        int j = 0;
        List<Map.Entry<PathDestination, BlockPosition>> set2 = Lists.newArrayListWithExpectedSize(list.size()); // Paper - optimize collection
        int k = (int) ((float) this.b * f1);

        while (!this.d.e()) {
            ++j;
            if (j >= k) {
                break;
            }

            PathPoint pathpoint1 = this.d.c();

            pathpoint1.i = true;
            // Paper start - optimize collection
            for (int i1 = 0; i1 < list.size(); i1++) {
                Map.Entry<PathDestination, BlockPosition> entry = list.get(i1);
                PathDestination pathdestination = entry.getKey();

                if (pathpoint1.c((PathPoint) pathdestination) <= (float) i) {
                    pathdestination.e();
                    set2.add(entry);
                    // Paper end
                }
            }

            if (!set2.isEmpty()) {
                break;
            }

            if (pathpoint1.a(pathpoint) < f) {
                int l = this.c.a(this.a, pathpoint1);

                for (int i1 = 0; i1 < l; ++i1) {
                    PathPoint pathpoint2 = this.a[i1];
                    float f2 = pathpoint1.a(pathpoint2);

                    pathpoint2.j = pathpoint1.j + f2;
                    float f3 = pathpoint1.e + f2 + pathpoint2.k;

                    if (pathpoint2.j < f && (!pathpoint2.c() || f3 < pathpoint2.e)) {
                        pathpoint2.h = pathpoint1;
                        pathpoint2.e = f3;
                        pathpoint2.f = this.a(pathpoint2, list) * 1.5F; // Paper - list instead of set
                        if (pathpoint2.c()) {
                            this.d.a(pathpoint2, pathpoint2.e + pathpoint2.f);
                        } else {
                            pathpoint2.g = pathpoint2.e + pathpoint2.f;
                            this.d.a(pathpoint2);
                        }
                    }
                }
            }
        }

        // Paper start - remove streams - and optimize collection
        PathEntity best = null;
        boolean useSet1 = set2.isEmpty();
        Comparator<PathEntity> comparator = useSet1 ? Comparator.comparingInt(PathEntity::e)
            : Comparator.comparingDouble(PathEntity::n).thenComparingInt(PathEntity::e);
        for (Map.Entry<PathDestination, BlockPosition> entry : useSet1 ? list : set2) {
            PathEntity pathEntity = this.a(entry.getKey().d(), entry.getValue(), !useSet1);
            if (best == null || comparator.compare(pathEntity, best) < 0)
                best = pathEntity;
        }
        return best;
        // Paper end
    }

    private float a(PathPoint pathpoint, List<Map.Entry<PathDestination, BlockPosition>> list) { // Paper - optimize collection
        float f = Float.MAX_VALUE;

        float f1;

        // Paper start - optimize collection
        for (int i = 0, listSize = list.size(); i < listSize; f = Math.min(f1, f), i++) { // Paper
            PathDestination pathdestination = list.get(i).getKey(); // Paper
            // Paper end

            f1 = pathpoint.a(pathdestination);
            pathdestination.a(f1, pathpoint);
        }

        return f;
    }

    private PathEntity a(PathPoint pathpoint, BlockPosition blockposition, boolean flag) {
        List<PathPoint> list = Lists.newArrayList();
        PathPoint pathpoint1 = pathpoint;

        list.add(0, pathpoint);

        while (pathpoint1.h != null) {
            pathpoint1 = pathpoint1.h;
            list.add(0, pathpoint1);
        }

        return new PathEntity(list, blockposition, flag);
    }
}
