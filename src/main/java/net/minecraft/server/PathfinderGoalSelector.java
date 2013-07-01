package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.util.UnsafeList; // CraftBukkit

public class PathfinderGoalSelector {

    // CraftBukkit start - ArrayList -> UnsafeList
    private List a = new UnsafeList();
    private List b = new UnsafeList();
    // CraftBukkit end
    private final MethodProfiler c;
    private int d;
    private int e = 3;

    public PathfinderGoalSelector(MethodProfiler methodprofiler) {
        this.c = methodprofiler;
    }

    public void a(int i, PathfinderGoal pathfindergoal) {
        this.a.add(new PathfinderGoalSelectorItem(this, i, pathfindergoal));
    }

    public void a(PathfinderGoal pathfindergoal) {
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            PathfinderGoal pathfindergoal1 = pathfindergoalselectoritem.a;

            if (pathfindergoal1 == pathfindergoal) {
                if (this.b.contains(pathfindergoalselectoritem)) {
                    pathfindergoal1.d();
                    this.b.remove(pathfindergoalselectoritem);
                }

                iterator.remove();
            }
        }
    }

    public void a() {
        // ArrayList arraylist = new ArrayList(); // CraftBukkit - remove usage
        Iterator iterator;
        PathfinderGoalSelectorItem pathfindergoalselectoritem;

        if (this.d++ % this.e == 0) {
            iterator = this.a.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
                boolean flag = this.b.contains(pathfindergoalselectoritem);

                if (flag) {
                    if (this.b(pathfindergoalselectoritem) && this.a(pathfindergoalselectoritem)) {
                        continue;
                    }

                    pathfindergoalselectoritem.a.d();
                    this.b.remove(pathfindergoalselectoritem);
                }

                if (this.b(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.a()) {
                    // CraftBukkit start - call method now instead of queueing
                    // arraylist.add(pathfindergoalselectoritem);
                    pathfindergoalselectoritem.a.c();
                    // CraftBukkit end
                    this.b.add(pathfindergoalselectoritem);
                }
            }
        } else {
            iterator = this.b.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
                if (!pathfindergoalselectoritem.a.b()) {
                    pathfindergoalselectoritem.a.d();
                    iterator.remove();
                }
            }
        }

        this.c.a("goalStart");
        // CraftBukkit start - removed usage of arraylist
        /*iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            this.c.a(pathfindergoalselectoritem.a.getClass().getSimpleName());
            pathfindergoalselectoritem.a.c();
            this.c.b();
        }*/
        // CraftBukkit end

        this.c.b();
        this.c.a("goalTick");
        iterator = this.b.iterator();

        while (iterator.hasNext()) {
            pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            pathfindergoalselectoritem.a.e();
        }

        this.c.b();
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem) {
        this.c.a("canContinue");
        boolean flag = pathfindergoalselectoritem.a.b();

        this.c.b();
        return flag;
    }

    private boolean b(PathfinderGoalSelectorItem pathfindergoalselectoritem) {
        this.c.a("canUse");
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) iterator.next();

            if (pathfindergoalselectoritem1 != pathfindergoalselectoritem) {
                if (pathfindergoalselectoritem.b >= pathfindergoalselectoritem1.b) {
                    // CraftBukkit - switch order
                    if (!this.a(pathfindergoalselectoritem, pathfindergoalselectoritem1) && this.b.contains(pathfindergoalselectoritem1)) {
                        this.c.b();
                        ((UnsafeList.Itr) iterator).valid = false; // CraftBukkit - mark iterator for reuse
                        return false;
                    }
                    // CraftBukkit - switch order
                } else if (!pathfindergoalselectoritem1.a.i() && this.b.contains(pathfindergoalselectoritem1)) {
                    this.c.b();
                    ((UnsafeList.Itr) iterator).valid = false; // CraftBukkit - mark iterator for reuse
                    return false;
                }
            }
        }

        this.c.b();
        return true;
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem, PathfinderGoalSelectorItem pathfindergoalselectoritem1) {
        return (pathfindergoalselectoritem.a.j() & pathfindergoalselectoritem1.a.j()) == 0;
    }
}
