package net.minecraft.server;

import java.util.EnumSet;
import javax.annotation.Nullable;

public class PathfinderGoalWrapped extends PathfinderGoal {

    private final PathfinderGoal a;
    private final int b;
    private boolean c;

    public PathfinderGoalWrapped(int i, PathfinderGoal pathfindergoal) {
        this.b = i;
        this.a = pathfindergoal;
    }

    public boolean a(PathfinderGoalWrapped pathfindergoalwrapped) {
        return this.C_() && pathfindergoalwrapped.h() < this.h();
    }

    @Override
    public boolean a() {
        return this.a.a();
    }

    @Override
    public boolean b() {
        return this.a.b();
    }

    @Override
    public boolean C_() {
        return this.a.C_();
    }

    @Override
    public void c() {
        if (!this.c) {
            this.c = true;
            this.a.c();
        }
    }

    @Override
    public void d() {
        if (this.c) {
            this.c = false;
            this.a.d();
        }
    }

    @Override
    public void e() {
        this.a.e();
    }

    @Override
    public void a(EnumSet<PathfinderGoal.Type> enumset) {
        this.a.a(enumset);
    }

    @Override
    public EnumSet<PathfinderGoal.Type> i() {
        return this.a.i();
    }

    public boolean g() {
        return this.c;
    }

    public int h() {
        return this.b;
    }

    public PathfinderGoal j() {
        return this.a;
    }

    public boolean equals(@Nullable Object object) {
        return this == object ? true : (object != null && this.getClass() == object.getClass() ? this.a.equals(((PathfinderGoalWrapped) object).a) : false);
    }

    public int hashCode() {
        return this.a.hashCode();
    }
}
