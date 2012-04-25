package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.craftbukkit.util.UnsafeList; // CraftBukkit

public class PathfinderGoalSelector {

    // CraftBukkit start - use UnsafeList
    private UnsafeList a = new UnsafeList(16);
    private UnsafeList b = new UnsafeList(16);
    // CraftBukkit end

    public PathfinderGoalSelector() {}

    public void a(int i, PathfinderGoal pathfindergoal) {
        this.a.add(new PathfinderGoalSelectorItem(this, i, pathfindergoal));
    }

    public void a() {
        //ArrayList arraylist = new ArrayList(); // CraftBukkit - removed usage

        // CraftBukkit start - don't use iterators for private fields
        for (int i = 0; i < this.a.size(); i++) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem = (PathfinderGoalSelectorItem) this.a.unsafeGet(i); // CraftBukkit - use unsafeGet
            boolean flag = this.b.contains(pathfindergoalselectoritem);

            if (flag) {
                if (this.a(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.b()) {
                    continue;
                }

                pathfindergoalselectoritem.a.d();
                this.b.remove(pathfindergoalselectoritem);
            }

            if (this.a(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.a()) {
                // CraftBukkit start - call method now
                // arraylist.add(pathfindergoalselectoritem);
                pathfindergoalselectoritem.a.c();
                // CraftBukkit end
                this.b.add(pathfindergoalselectoritem);
            }
        }
        // CraftBukkit end

        boolean flag1 = false;

        // CraftBukkit start - removed usage of arraylist
        /*if (flag1 && arraylist.size() > 0) {
            System.out.println("Starting: ");
        }*/
        // CraftBukkit end

        Iterator iterator1;
        PathfinderGoalSelectorItem pathfindergoalselectoritem1;

        // CraftBukkit start - removed usage of arraylist
        /*for (iterator1 = arraylist.iterator(); iterator1.hasNext(); pathfindergoalselectoritem1.a.c()) {
            pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) iterator1.next();
            if (flag1) {
                System.out.println(pathfindergoalselectoritem1.a.toString() + ", ");
            }
        }*/
        // CraftBukkit end

        if (flag1 && this.b.size() > 0) {
            System.out.println("Running: ");
        }

        // CraftBukkit start - don't use iterators for private fields
        for (int i = 0; i < this.b.size(); i++) {
            pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) this.b.unsafeGet(i); // CraftBukkit - use unsafeGet
            pathfindergoalselectoritem1.a.e();
            if (flag1) {
                System.out.println(pathfindergoalselectoritem1.a.toString());
            }
        }
        // CraftBukkit end
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem) {
        // CraftBukkit start - don't use iterators for private fields
        for (int i = 0; i < this.a.size(); i++) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) this.a.unsafeGet(i); // CraftBukkit - use unsafeGet

            if (pathfindergoalselectoritem1 != pathfindergoalselectoritem) {
                if (pathfindergoalselectoritem.b >= pathfindergoalselectoritem1.b) {
                    if (!this.a(pathfindergoalselectoritem, pathfindergoalselectoritem1) && this.b.contains(pathfindergoalselectoritem1)) { // CraftBukkit - switch order
                        return false;
                    }
                } else if (!pathfindergoalselectoritem1.a.g() && this.b.contains(pathfindergoalselectoritem1)) { // CraftBukkit - switch order
                    return false;
                }
            }
        }
        // CraftBukkit end

        return true;
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem, PathfinderGoalSelectorItem pathfindergoalselectoritem1) {
        return (pathfindergoalselectoritem.a.h() & pathfindergoalselectoritem1.a.h()) == 0;
    }
}
