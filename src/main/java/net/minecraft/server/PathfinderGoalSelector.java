package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bukkit.craftbukkit.util.UnsafeList; // CraftBukkit

public class PathfinderGoalSelector {

    private static final Logger a = LogManager.getLogger();
    // CraftBukkit start - ArrayList -> UnsafeList
    private List b = new UnsafeList();
    private List c = new UnsafeList();
    // CraftBukkit end
    private final MethodProfiler d;
    private int e;
    private int f = 3;

    public PathfinderGoalSelector(MethodProfiler methodprofiler) {
        this.d = methodprofiler;
    }

    public void a(int i, PathfinderGoal pathfindergoal) {
        this.b.add(new PathfinderGoalSelectorItem(this, i, pathfindergoal));
    }

    public void a(PathfinderGoal pathfindergoal) {
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            PathfinderGoal pathfindergoal1 = pathfindergoalselectoritem.a;

            if (pathfindergoal1 == pathfindergoal) {
                if (this.c.contains(pathfindergoalselectoritem)) {
                    pathfindergoal1.d();
                    this.c.remove(pathfindergoalselectoritem);
                }

                iterator.remove();
            }
        }
    }

    public void a() {
        // ArrayList arraylist = new ArrayList(); // CraftBukkit - remove usage
        Iterator iterator;
        PathfinderGoalSelectorItem pathfindergoalselectoritem;

        if (this.e++ % this.f == 0) {
            iterator = this.b.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
                boolean flag = this.c.contains(pathfindergoalselectoritem);

                if (flag) {
                    if (this.b(pathfindergoalselectoritem) && this.a(pathfindergoalselectoritem)) {
                        continue;
                    }

                    pathfindergoalselectoritem.a.d();
                    this.c.remove(pathfindergoalselectoritem);
                }

                if (this.b(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.a()) {
                    // CraftBukkit start - call method now instead of queueing
                    // arraylist.add(pathfindergoalselectoritem);
                    pathfindergoalselectoritem.a.c();
                    // CraftBukkit end
                    this.c.add(pathfindergoalselectoritem);
                }
            }
        } else {
            iterator = this.c.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
                if (!pathfindergoalselectoritem.a.b()) {
                    pathfindergoalselectoritem.a.d();
                    iterator.remove();
                }
            }
        }

        this.d.a("goalStart");
        // CraftBukkit start - removed usage of arraylist
        /*iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            this.d.a(pathfindergoalselectoritem.a.getClass().getSimpleName());
            pathfindergoalselectoritem.a.c();
            this.d.b();
        }*/
        // CraftBukkit end

        this.d.b();
        this.d.a("goalTick");
        iterator = this.c.iterator();

        while (iterator.hasNext()) {
            pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
            pathfindergoalselectoritem.a.e();
        }

        this.d.b();
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem) {
        this.d.a("canContinue");
        boolean flag = pathfindergoalselectoritem.a.b();

        this.d.b();
        return flag;
    }

    private boolean b(PathfinderGoalSelectorItem pathfindergoalselectoritem) {
        this.d.a("canUse");
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) iterator.next();

            if (pathfindergoalselectoritem1 != pathfindergoalselectoritem) {
                if (pathfindergoalselectoritem.b >= pathfindergoalselectoritem1.b) {
                    // CraftBukkit - switch order
                    if (!this.a(pathfindergoalselectoritem, pathfindergoalselectoritem1) && this.c.contains(pathfindergoalselectoritem1)) {
                        this.d.b();
                        ((UnsafeList.Itr) iterator).valid = false; // CraftBukkit - mark iterator for reuse
                        return false;
                    }
                    // CraftBukkit - switch order
                } else if (!pathfindergoalselectoritem1.a.i() && this.c.contains(pathfindergoalselectoritem1)) {
                    this.d.b();
                    ((UnsafeList.Itr) iterator).valid = false; // CraftBukkit - mark iterator for reuse
                    return false;
                }
            }
        }

        this.d.b();
        return true;
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem, PathfinderGoalSelectorItem pathfindergoalselectoritem1) {
        return (pathfindergoalselectoritem.a.j() & pathfindergoalselectoritem1.a.j()) == 0;
    }
}
