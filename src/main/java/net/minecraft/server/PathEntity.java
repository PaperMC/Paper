package net.minecraft.server;

import java.util.List;
import javax.annotation.Nullable;

public class PathEntity {

    private final List<PathPoint> a;
    private PathPoint[] b = new PathPoint[0];
    private PathPoint[] c = new PathPoint[0];
    private int e;
    private final BlockPosition f;
    private final float g;
    private final boolean h;

    public PathEntity(List<PathPoint> list, BlockPosition blockposition, boolean flag) {
        this.a = list;
        this.f = blockposition;
        this.g = list.isEmpty() ? Float.MAX_VALUE : ((PathPoint) this.a.get(this.a.size() - 1)).c(this.f);
        this.h = flag;
    }

    public void a() {
        ++this.e;
    }

    public boolean b() {
        return this.e <= 0;
    }

    public boolean c() {
        return this.e >= this.a.size();
    }

    @Nullable
    public PathPoint d() {
        return !this.a.isEmpty() ? (PathPoint) this.a.get(this.a.size() - 1) : null;
    }

    public PathPoint a(int i) {
        return (PathPoint) this.a.get(i);
    }

    public void b(int i) {
        if (this.a.size() > i) {
            this.a.subList(i, this.a.size()).clear();
        }

    }

    public void a(int i, PathPoint pathpoint) {
        this.a.set(i, pathpoint);
    }

    public int e() {
        return this.a.size();
    }

    public int f() {
        return this.e;
    }

    public void c(int i) {
        this.e = i;
    }

    public Vec3D a(Entity entity, int i) {
        PathPoint pathpoint = (PathPoint) this.a.get(i);
        double d0 = (double) pathpoint.a + (double) ((int) (entity.getWidth() + 1.0F)) * 0.5D;
        double d1 = (double) pathpoint.b;
        double d2 = (double) pathpoint.c + (double) ((int) (entity.getWidth() + 1.0F)) * 0.5D;

        return new Vec3D(d0, d1, d2);
    }

    public BlockPosition d(int i) {
        return ((PathPoint) this.a.get(i)).a();
    }

    public Vec3D a(Entity entity) {
        return this.a(entity, this.e);
    }

    public BlockPosition g() {
        return ((PathPoint) this.a.get(this.e)).a();
    }

    public PathPoint h() {
        return (PathPoint) this.a.get(this.e);
    }

    @Nullable
    public PathPoint i() {
        return this.e > 0 ? (PathPoint) this.a.get(this.e - 1) : null;
    }

    public boolean a(@Nullable PathEntity pathentity) {
        if (pathentity == null) {
            return false;
        } else if (pathentity.a.size() != this.a.size()) {
            return false;
        } else {
            for (int i = 0; i < this.a.size(); ++i) {
                PathPoint pathpoint = (PathPoint) this.a.get(i);
                PathPoint pathpoint1 = (PathPoint) pathentity.a.get(i);

                if (pathpoint.a != pathpoint1.a || pathpoint.b != pathpoint1.b || pathpoint.c != pathpoint1.c) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean j() {
        return this.h;
    }

    public String toString() {
        return "Path(length=" + this.a.size() + ")";
    }

    public BlockPosition m() {
        return this.f;
    }

    public float n() {
        return this.g;
    }
}
