package net.minecraft.server;

import com.destroystokyo.paper.util.set.OptimizedSmallEnumSet; // Paper - remove streams from pathfindergoalselector
import com.google.common.collect.Sets;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator; // Paper - remove streams from pathfindergoalselector
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PathfinderGoalSelector {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final PathfinderGoalWrapped b = new PathfinderGoalWrapped(Integer.MAX_VALUE, new PathfinderGoal() {
        @Override
        public boolean a() {
            return false;
        }
    }) {
        @Override
        public boolean g() {
            return false;
        }
    };
    private final Map<PathfinderGoal.Type, PathfinderGoalWrapped> c = new EnumMap(PathfinderGoal.Type.class);
    private final Set<PathfinderGoalWrapped> d = Sets.newLinkedHashSet(); public final Set<PathfinderGoalWrapped> getTasks() { return d; }// Paper - OBFHELPER // Paper - private -> public
    private final Supplier<GameProfilerFiller> e;
    private final EnumSet<PathfinderGoal.Type> f = EnumSet.noneOf(PathfinderGoal.Type.class); // Paper unused, but dummy to prevent plugins from crashing as hard. Theyll need to support paper in a special case if this is super important, but really doesn't seem like it would be.
    private final OptimizedSmallEnumSet<PathfinderGoal.Type> goalTypes = new OptimizedSmallEnumSet<>(PathfinderGoal.Type.class); // Paper - remove streams from pathfindergoalselector
    private int g = 3;private int getTickRate() { return g; } // Paper - OBFHELPER
    private int curRate;private int getCurRate() { return curRate; } private void incRate() { this.curRate++; } // Paper TODO

    public PathfinderGoalSelector(Supplier<GameProfilerFiller> supplier) {
        this.e = supplier;
    }

    public void addGoal(int priority, PathfinderGoal goal) {a(priority, goal);} public void a(int i, PathfinderGoal pathfindergoal) { // Paper - OBFHELPER
        this.d.add(new PathfinderGoalWrapped(i, pathfindergoal));
    }

    // Paper start
    public boolean inactiveTick() {
        if (getCurRate() % getTickRate() != 0) {
            incRate();
            return false;
        } else {
            return true;
        }
    }
    public boolean hasTasks() {
        for (PathfinderGoalWrapped task : getTasks()) {
            if (task.isRunning()) {
                return true;
            }
        }
        return false;
    }
    // Paper end

    public void removeGoal(PathfinderGoal goal) {a(goal);} public void a(PathfinderGoal pathfindergoal) { // Paper - OBFHELPER
        // Paper start - remove streams from pathfindergoalselector
        for (Iterator<PathfinderGoalWrapped> iterator = this.d.iterator(); iterator.hasNext();) {
            PathfinderGoalWrapped goalWrapped = iterator.next();
            if (goalWrapped.j() != pathfindergoal) {
                continue;
            }
            if (goalWrapped.g()) {
                goalWrapped.d();
            }
            iterator.remove();
        }
        // Paper end - remove streams from pathfindergoalselector
    }

    private static final PathfinderGoal.Type[] PATHFINDER_GOAL_TYPES = PathfinderGoal.Type.values(); // Paper - remove streams from pathfindergoalselector

    public void doTick() {
        GameProfilerFiller gameprofilerfiller = (GameProfilerFiller) this.e.get();

        gameprofilerfiller.enter("goalCleanup");
        // Paper start - remove streams from pathfindergoalselector
        for (Iterator<PathfinderGoalWrapped> iterator = this.d.iterator(); iterator.hasNext();) {
            PathfinderGoalWrapped wrappedGoal = iterator.next();
            if (!wrappedGoal.g()) {
                continue;
            }
            if (!this.goalTypes.hasCommonElements(wrappedGoal.getGoalTypes()) && wrappedGoal.b()) {
                continue;
            }
            wrappedGoal.d();
        }
        // Paper end - remove streams from pathfindergoalselector
        this.c.forEach((pathfindergoal_type, pathfindergoalwrapped) -> {
            if (!pathfindergoalwrapped.g()) {
                this.c.remove(pathfindergoal_type);
            }

        });
        gameprofilerfiller.exit();
        gameprofilerfiller.enter("goalUpdate");
        // Paper start - remove streams from pathfindergoalselector
        goal_update_loop: for (Iterator<PathfinderGoalWrapped> iterator = this.d.iterator(); iterator.hasNext();) {
            PathfinderGoalWrapped wrappedGoal = iterator.next();
            if (wrappedGoal.g()) {
                continue;
            }

            OptimizedSmallEnumSet<PathfinderGoal.Type> wrappedGoalSet = wrappedGoal.getGoalTypes();

            if (this.goalTypes.hasCommonElements(wrappedGoalSet)) {
                continue;
            }

            long iterator1 = wrappedGoalSet.getBackingSet();
            int wrappedGoalSize = wrappedGoalSet.size();
            for (int i = 0; i < wrappedGoalSize; ++i) {
                PathfinderGoal.Type type = PATHFINDER_GOAL_TYPES[Long.numberOfTrailingZeros(iterator1)];
                iterator1 ^= com.destroystokyo.paper.util.math.IntegerUtil.getTrailingBit(iterator1);
                PathfinderGoalWrapped wrapped = this.c.getOrDefault(type, PathfinderGoalSelector.b);
                if (!wrapped.a(wrappedGoal)) {
                    continue goal_update_loop;
                }
            }

            if (!wrappedGoal.a()) {
                continue;
            }

            iterator1 = wrappedGoalSet.getBackingSet();
            wrappedGoalSize = wrappedGoalSet.size();
            for (int i = 0; i < wrappedGoalSize; ++i) {
                PathfinderGoal.Type type = PATHFINDER_GOAL_TYPES[Long.numberOfTrailingZeros(iterator1)];
                iterator1 ^= com.destroystokyo.paper.util.math.IntegerUtil.getTrailingBit(iterator1);
                PathfinderGoalWrapped wrapped = this.c.getOrDefault(type, PathfinderGoalSelector.b);

                wrapped.d();
                this.c.put(type, wrappedGoal);
            }

            wrappedGoal.c();
        }
        // Paper end - remove streams from pathfindergoalselector
        gameprofilerfiller.exit();
        gameprofilerfiller.enter("goalTick");
        // Paper start - remove streams from pathfindergoalselector
        for (Iterator<PathfinderGoalWrapped> iterator = this.d.iterator(); iterator.hasNext();) {
            PathfinderGoalWrapped wrappedGoal = iterator.next();
            if (wrappedGoal.g()) {
                wrappedGoal.e();
            }
        }
        // Paper end - remove streams from pathfindergoalselector
        gameprofilerfiller.exit();
    }

    public final Stream<PathfinderGoalWrapped> getExecutingGoals() { return d(); } // Paper - OBFHELPER
    public Stream<PathfinderGoalWrapped> d() {
        return this.d.stream().filter(PathfinderGoalWrapped::g);
    }

    public void a(PathfinderGoal.Type pathfindergoal_type) {
        this.goalTypes.addUnchecked(pathfindergoal_type); // Paper - remove streams from pathfindergoalselector
    }

    public void b(PathfinderGoal.Type pathfindergoal_type) {
        this.goalTypes.removeUnchecked(pathfindergoal_type); // Paper - remove streams from pathfindergoalselector
    }

    public void a(PathfinderGoal.Type pathfindergoal_type, boolean flag) {
        if (flag) {
            this.b(pathfindergoal_type);
        } else {
            this.a(pathfindergoal_type);
        }

    }
}
