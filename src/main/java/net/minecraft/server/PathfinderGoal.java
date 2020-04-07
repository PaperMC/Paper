package net.minecraft.server;

import com.destroystokyo.paper.util.set.OptimizedSmallEnumSet; // Paper - remove streams from pathfindergoalselector
import java.util.EnumSet;

public abstract class PathfinderGoal {

    private final EnumSet<PathfinderGoal.Type> a = EnumSet.noneOf(PathfinderGoal.Type.class); // Paper unused, but dummy to prevent plugins from crashing as hard. Theyll need to support paper in a special case if this is super important, but really doesn't seem like it would be.
    private final OptimizedSmallEnumSet<Type> goalTypes = new OptimizedSmallEnumSet<>(PathfinderGoal.Type.class); // Paper - remove streams from pathfindergoalselector

    public PathfinderGoal() {}

    public abstract boolean a();

    public boolean b() {
        return this.a();
    }

    public boolean C_() {
        return true;
    }

    public void c() {}

    public void d() {
        onTaskReset(); // Paper
    }
    public void onTaskReset() {} // Paper

    public void e() {}

    public void a(EnumSet<PathfinderGoal.Type> enumset) {
        // Paper start - remove streams from pathfindergoalselector
        this.goalTypes.clear();
        this.goalTypes.addAllUnchecked(enumset);
        // Paper end - remove streams from pathfindergoalselector
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    // Paper start - remove streams from pathfindergoalselector
    public com.destroystokyo.paper.util.set.OptimizedSmallEnumSet<PathfinderGoal.Type> getGoalTypes() {
        return this.goalTypes;
        // Paper end - remove streams from pathfindergoalselector
    }

    public static enum Type {

        MOVE, LOOK, JUMP, TARGET;

        private Type() {}
    }
}
