package net.minecraft.server;

import java.util.EnumSet;

public abstract class PathfinderGoal {

    private final EnumSet<PathfinderGoal.Type> a = EnumSet.noneOf(PathfinderGoal.Type.class);

    public PathfinderGoal() {}

    public abstract boolean a();

    public boolean b() {
        return this.a();
    }

    public boolean C_() {
        return true;
    }

    public void c() {}

    public void d() {}

    public void e() {}

    public void a(EnumSet<PathfinderGoal.Type> enumset) {
        this.a.clear();
        this.a.addAll(enumset);
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public EnumSet<PathfinderGoal.Type> i() {
        return this.a;
    }

    public static enum Type {

        MOVE, LOOK, JUMP, TARGET;

        private Type() {}
    }
}
