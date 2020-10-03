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
    private final PathfinderAbstract c;
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
        Map<PathDestination, BlockPosition> map = (Map) set.stream().collect(Collectors.toMap((blockposition) -> {
            return this.c.a((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
        }, Function.identity()));
        PathEntity pathentity = this.a(pathpoint, map, f, i, f1);

        this.c.a();
        return pathentity;
    }

    @Nullable
    private PathEntity a(PathPoint pathpoint, Map<PathDestination, BlockPosition> map, float f, int i, float f1) {
        Set<PathDestination> set = map.keySet();

        pathpoint.e = 0.0F;
        pathpoint.f = this.a(pathpoint, set);
        pathpoint.g = pathpoint.f;
        this.d.a();
        this.d.a(pathpoint);
        Set<PathPoint> set1 = ImmutableSet.of();
        int j = 0;
        Set<PathDestination> set2 = Sets.newHashSetWithExpectedSize(set.size());
        int k = (int) ((float) this.b * f1);

        while (!this.d.e()) {
            ++j;
            if (j >= k) {
                break;
            }

            PathPoint pathpoint1 = this.d.c();

            pathpoint1.i = true;
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                PathDestination pathdestination = (PathDestination) iterator.next();

                if (pathpoint1.c((PathPoint) pathdestination) <= (float) i) {
                    pathdestination.e();
                    set2.add(pathdestination);
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
                        pathpoint2.f = this.a(pathpoint2, set) * 1.5F;
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

        Optional<PathEntity> optional = !set2.isEmpty() ? set2.stream().map((pathdestination1) -> {
            return this.a(pathdestination1.d(), (BlockPosition) map.get(pathdestination1), true);
        }).min(Comparator.comparingInt(PathEntity::e)) : set.stream().map((pathdestination1) -> {
            return this.a(pathdestination1.d(), (BlockPosition) map.get(pathdestination1), false);
        }).min(Comparator.comparingDouble(PathEntity::n).thenComparingInt(PathEntity::e));

        if (!optional.isPresent()) {
            return null;
        } else {
            PathEntity pathentity = (PathEntity) optional.get();

            return pathentity;
        }
    }

    private float a(PathPoint pathpoint, Set<PathDestination> set) {
        float f = Float.MAX_VALUE;

        float f1;

        for (Iterator iterator = set.iterator(); iterator.hasNext(); f = Math.min(f1, f)) {
            PathDestination pathdestination = (PathDestination) iterator.next();

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
