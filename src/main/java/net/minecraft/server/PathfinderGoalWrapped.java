package net.minecraft.server;

import java.util.EnumSet;
import javax.annotation.Nullable;

public class PathfinderGoalWrapped extends PathfinderGoal {

    private final PathfinderGoal a; public PathfinderGoal getGoal() {return a;} // Paper - OBFHELPER
    private final int b; public int getPriority() {return b;} // Paper - OBFHELPER
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

    // Paper start - remove streams from pathfindergoalselector
    public com.destroystokyo.paper.util.set.OptimizedSmallEnumSet<PathfinderGoal.Type> getGoalTypes() {
        return this.a.getGoalTypes();
        // Paper end - remove streams from pathfindergoalselector
    }

    public boolean isRunning() { return this.g(); } // Paper - OBFHELPER
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
