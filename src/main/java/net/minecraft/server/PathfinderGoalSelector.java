package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;

public class PathfinderGoalSelector {

    private ArrayList a = new ArrayList();
    private ArrayList b = new ArrayList();

    public PathfinderGoalSelector() {}

    public void a(int i, PathfinderGoal pathfindergoal) {
        this.a.add(new PathfinderGoalSelectorItem(this, i, pathfindergoal));
    }

    public void a() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem = (PathfinderGoalSelectorItem) iterator.next();
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

        boolean flag1 = false;

        if (flag1 && arraylist.size() > 0) {
            System.out.println("Starting: ");
        }

        Iterator iterator1;
        PathfinderGoalSelectorItem pathfindergoalselectoritem1;

        for (iterator1 = arraylist.iterator(); iterator1.hasNext(); pathfindergoalselectoritem1.a.c()) {
            pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) iterator1.next();
            if (flag1) {
                System.out.println(pathfindergoalselectoritem1.a.toString() + ", ");
            }
        }

        if (flag1 && this.b.size() > 0) {
            System.out.println("Running: ");
        }

        for (iterator1 = this.b.iterator(); iterator1.hasNext(); pathfindergoalselectoritem1.a.e()) {
            pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) iterator1.next();
            if (flag1) {
                System.out.println(pathfindergoalselectoritem1.a.toString());
            }
        }
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem) {
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            PathfinderGoalSelectorItem pathfindergoalselectoritem1 = (PathfinderGoalSelectorItem) iterator.next();

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

        return true;
    }

    private boolean a(PathfinderGoalSelectorItem pathfindergoalselectoritem, PathfinderGoalSelectorItem pathfindergoalselectoritem1) {
        return (pathfindergoalselectoritem.a.h() & pathfindergoalselectoritem1.a.h()) == 0;
    }
}
