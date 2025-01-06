package com.destroystokyo.paper.entity.ai;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperMobGoals implements MobGoals {

    @Override
    public <T extends Mob> void addGoal(T mob, int priority, Goal<T> goal) {
        CraftMob craftMob = (CraftMob) mob;
        net.minecraft.world.entity.ai.goal.Goal mojangGoal;

        if (goal instanceof PaperGoal<?> paperGoal) {
            mojangGoal = paperGoal.getHandle();
        } else {
            mojangGoal = new PaperCustomGoal<>(goal);
        }

        getHandle(craftMob, goal.getTypes()).addGoal(priority, mojangGoal);
    }

    @Override
    public <T extends Mob> void removeGoal(T mob, Goal<T> goal) {
        CraftMob craftMob = (CraftMob) mob;
        if (goal instanceof PaperCustomGoal) {
            getHandle(craftMob, goal.getTypes()).removeGoal((net.minecraft.world.entity.ai.goal.Goal) goal);
        } else if (goal instanceof PaperGoal) {
            getHandle(craftMob, goal.getTypes()).removeGoal(((PaperGoal<?>) goal).getHandle());
        } else {
            List<net.minecraft.world.entity.ai.goal.Goal> toRemove = new LinkedList<>();
            for (WrappedGoal item : getHandle(craftMob, goal.getTypes()).getAvailableGoals()) {
                if (item.getGoal() instanceof PaperCustomGoal) {
                    //noinspection unchecked
                    if (((PaperCustomGoal<T>) item.getGoal()).getHandle() == goal) {
                        toRemove.add(item.getGoal());
                    }
                }
            }

            for (net.minecraft.world.entity.ai.goal.Goal g : toRemove) {
                getHandle(craftMob, goal.getTypes()).removeGoal(g);
            }
        }
    }

    @Override
    public <T extends Mob> void removeAllGoals(T mob) {
        for (GoalType type : GoalType.values()) {
            removeAllGoals(mob, type);
        }
    }

    @Override
    public <T extends Mob> void removeAllGoals(T mob, GoalType type) {
        for (Goal<T> goal : getAllGoals(mob, type)) {
            removeGoal(mob, goal);
        }
    }

    @Override
    public <T extends Mob> void removeGoal(T mob, GoalKey<T> key) {
        for (Goal<T> goal : getGoals(mob, key)) {
            removeGoal(mob, goal);
        }
    }

    @Override
    public <T extends Mob> boolean hasGoal(T mob, GoalKey<T> key) {
        for (Goal<T> g : getAllGoals(mob)) {
            if (g.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends Mob> Goal<T> getGoal(T mob, GoalKey<T> key) {
        for (Goal<T> g : getAllGoals(mob)) {
            if (g.getKey().equals(key)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getGoals(T mob, GoalKey<T> key) {
        Set<Goal<T>> goals = new HashSet<>();
        for (Goal<T> g : getAllGoals(mob)) {
            if (g.getKey().equals(key)) {
                goals.add(g);
            }
        }
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getAllGoals(T mob) {
        Set<Goal<T>> goals = new HashSet<>();
        for (GoalType type : GoalType.values()) {
            goals.addAll(getAllGoals(mob, type));
        }
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getAllGoals(T mob, GoalType type) {
        CraftMob craftMob = (CraftMob) mob;
        Set<Goal<T>> goals = new HashSet<>();
        for (WrappedGoal item : getHandle(craftMob, type).getAvailableGoals()) {
            if (!item.getGoal().hasFlag(MobGoalHelper.paperToVanilla(type))) {
                continue;
            }

            if (item.getGoal() instanceof PaperCustomGoal) {
                //noinspection unchecked
                goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
            } else {
                goals.add(item.getGoal().asPaperGoal());
            }
        }
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getAllGoalsWithout(T mob, GoalType type) {
        CraftMob craftMob = (CraftMob) mob;
        Set<Goal<T>> goals = new HashSet<>();
        for (GoalType internalType : GoalType.values()) {
            if (internalType == type) {
                continue;
            }
            for (WrappedGoal item : getHandle(craftMob, internalType).getAvailableGoals()) {
                if (item.getGoal().hasFlag(MobGoalHelper.paperToVanilla(type))) {
                    continue;
                }

                if (item.getGoal() instanceof PaperCustomGoal) {
                    //noinspection unchecked
                    goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
                } else {
                    goals.add(item.getGoal().asPaperGoal());
                }
            }
        }
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getRunningGoals(T mob) {
        Set<Goal<T>> goals = new HashSet<>();
        for (GoalType type : GoalType.values()) {
            goals.addAll(getRunningGoals(mob, type));
        }
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getRunningGoals(T mob, GoalType type) {
        CraftMob craftMob = (CraftMob) mob;
        Set<Goal<T>> goals = new HashSet<>();
        getHandle(craftMob, type).getAvailableGoals()
            .stream().filter(WrappedGoal::isRunning)
            .filter(item -> item.getGoal().hasFlag(MobGoalHelper.paperToVanilla(type)))
            .forEach(item -> {
                if (item.getGoal() instanceof PaperCustomGoal) {
                    //noinspection unchecked
                    goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
                } else {
                    goals.add(item.getGoal().asPaperGoal());
                }
            });
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getRunningGoalsWithout(T mob, GoalType type) {
        CraftMob craftMob = (CraftMob) mob;
        Set<Goal<T>> goals = new HashSet<>();
        for (GoalType internalType : GoalType.values()) {
            if (internalType == type) {
                continue;
            }
            getHandle(craftMob, internalType).getAvailableGoals()
                .stream()
                .filter(WrappedGoal::isRunning)
                .filter(item -> !item.getGoal().hasFlag(MobGoalHelper.paperToVanilla(type)))
                .forEach(item -> {
                    if (item.getGoal() instanceof PaperCustomGoal) {
                        //noinspection unchecked
                        goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
                    } else {
                        goals.add(item.getGoal().asPaperGoal());
                    }
                });
        }
        return goals;
    }

    private GoalSelector getHandle(CraftMob mob, EnumSet<GoalType> types) {
        if (types.contains(GoalType.TARGET)) {
            return mob.getHandle().targetSelector;
        } else {
            return mob.getHandle().goalSelector;
        }
    }

    private GoalSelector getHandle(CraftMob mob, GoalType type) {
        if (type == GoalType.TARGET) {
            return mob.getHandle().targetSelector;
        } else {
            return mob.getHandle().goalSelector;
        }
    }
}
