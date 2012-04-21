package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;

public class PathfinderGoalSelector {

    private ArrayList a = new ArrayList();
    private ArrayList b = new ArrayList();
    private ArrayList arraylist = new ArrayList(); // CraftBukkit - create list for object reuse

    public PathfinderGoalSelector() {}

    public void a(int i, PathfinderGoal pathfindergoal) {
        this.a.add(new PathfinderGoalSelectorItem(this, i, pathfindergoal));
    }

    public void a() {
        //ArrayList arraylist = new ArrayList(); // CraftBukkit
        arraylist.clear(); // CraftBukkit - prepare reused list

        // CraftBukkit start - don't use iterators for private fields
        for (int i = 0; i < this.a.size(); i++) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem = (PathfinderGoalSelectorItem) this.a.get(i);
            boolean flag = this.b.contains(pathfindergoalselectoritem);

            if (flag) {
                if (this.a(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.b()) {
                    continue;
                }

                pathfindergoalselectoritem.a.d();
                this.b.remove(pathfindergoalselectoritem);
            }

            if (this.a(pathfindergoalselectoritem) && pathfindergoalselectoritem.a.a()) {
                arraylist.add(pathfindergoalselectoritem);
                this.b.add(pathfindergoalselectoritem);
            }
        }
        // CraftBukkit end

        boolean flag1 = false;

        if (flag1 && arraylist.size() > 0) {
            System.out.println("Starting: ");
        }

        Iterator iterator1;
        PathfinderGoalSelectorItem pathfindergoalselectoritem1;

        //CraftBukkit start - don't use iterators for private fields
        for (int i = 0; i < arraylist.size(); i++) {
            pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) arraylist.get(i);
            pathfindergoalselectoritem1.a.c();
            if (flag1) {
                System.out.println(pathfindergoalselectoritem1.a.toString() + ", ");
            }
        }

        if (flag1 && this.b.size() > 0) {
            System.out.println("Running: ");
        }

        for (int i = 0; i < this.b.size(); i++) {
            pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) this.b.get(i);
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
            PathfinderGoalSelectorItem pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) this.a.get(i);

            if (pathfindergoalselectoritem1 != pathfindergoalselectoritem) {
                if (pathfindergoalselectoritem.b >= pathfindergoalselectoritem1.b) {
                    if (this.b.contains(pathfindergoalselectoritem1) && !this.a(pathfindergoalselectoritem, pathfindergoalselectoritem1)) {
                        return false;
                    }
                } else if (this.b.contains(pathfindergoalselectoritem1) && !pathfindergoalselectoritem1.a.g()) {
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
