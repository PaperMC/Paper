package com.destroystokyo.paper.entity.ai;

import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalSelector;
import net.minecraft.server.PathfinderGoalWrapped;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.entity.Mob;

public class PaperMobGoals implements MobGoals {

    private final Map<PathfinderGoal, PaperVanillaGoal<?>> instanceCache = new HashMap<>();

    @Override
    public <T extends Mob> void addGoal(T mob, int priority, Goal<T> goal) {
        CraftMob craftMob = (CraftMob) mob;
        getHandle(craftMob, goal.getTypes()).addGoal(priority, new PaperCustomGoal<>(goal));
    }

    @Override
    public <T extends Mob> void removeGoal(T mob, Goal<T> goal) {
        CraftMob craftMob = (CraftMob) mob;
        if (goal instanceof PaperCustomGoal) {
            getHandle(craftMob, goal.getTypes()).removeGoal((PathfinderGoal) goal);
        } else if (goal instanceof PaperVanillaGoal) {
            getHandle(craftMob, goal.getTypes()).removeGoal(((PaperVanillaGoal<?>) goal).getHandle());
        } else {
            List<PathfinderGoal> toRemove = new LinkedList<>();
            for (PathfinderGoalWrapped item : getHandle(craftMob, goal.getTypes()).getTasks()) {
                if (item.getGoal() instanceof PaperCustomGoal) {
                    //noinspection unchecked
                    if (((PaperCustomGoal<T>) item.getGoal()).getHandle() == goal) {
                        toRemove.add(item.getGoal());
                    }
                }
            }

            for (PathfinderGoal g : toRemove) {
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
        for (PathfinderGoalWrapped item : getHandle(craftMob, type).getTasks()) {
            if (!item.getGoal().getGoalTypes().hasElement(MobGoalHelper.paperToVanilla(type))) {
                continue;
            }

            if (item.getGoal() instanceof PaperCustomGoal) {
                //noinspection unchecked
                goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
            } else {
                //noinspection unchecked
                goals.add((Goal<T>) instanceCache.computeIfAbsent(item.getGoal(), PaperVanillaGoal::new));
            }
        }
        return goals;
    }

    @Override
    public <T extends Mob> Collection<Goal<T>> getAllGoalsWithout(T mob, GoalType type) {
        CraftMob craftMob = (CraftMob) mob;
        Set<Goal<T>> goals = new HashSet<>();
        for (GoalType internalType : GoalType.values()) {
            if(internalType == type) {
                continue;
            }
            for (PathfinderGoalWrapped item : getHandle(craftMob, internalType).getTasks()) {
                if (item.getGoal().getGoalTypes().hasElement(MobGoalHelper.paperToVanilla(type))) {
                    continue;
                }

                if (item.getGoal() instanceof PaperCustomGoal) {
                    //noinspection unchecked
                    goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
                } else {
                    //noinspection unchecked
                    goals.add((Goal<T>) instanceCache.computeIfAbsent(item.getGoal(), PaperVanillaGoal::new));
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
        getHandle(craftMob, type).getExecutingGoals()
            .filter(item -> item.getGoal().getGoalTypes().hasElement(MobGoalHelper.paperToVanilla(type)))
            .forEach(item -> {
                if (item.getGoal() instanceof PaperCustomGoal) {
                    //noinspection unchecked
                    goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
                } else {
                    //noinspection unchecked
                    goals.add((Goal<T>) instanceCache.computeIfAbsent(item.getGoal(), PaperVanillaGoal::new));
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
            getHandle(craftMob, internalType).getExecutingGoals()
                .filter(item -> !item.getGoal().getGoalTypes().hasElement(MobGoalHelper.paperToVanilla(type)))
                .forEach(item -> {
                    if (item.getGoal() instanceof PaperCustomGoal) {
                        //noinspection unchecked
                        goals.add(((PaperCustomGoal<T>) item.getGoal()).getHandle());
                    } else {
                        //noinspection unchecked
                        goals.add((Goal<T>) instanceCache.computeIfAbsent(item.getGoal(), PaperVanillaGoal::new));
                    }
                });
        }
        return goals;
    }

    private PathfinderGoalSelector getHandle(CraftMob mob, EnumSet<GoalType> types) {
        if (types.contains(GoalType.TARGET)) {
            return mob.getHandle().targetSelector;
        } else {
            return mob.getHandle().goalSelector;
        }
    }

    private PathfinderGoalSelector getHandle(CraftMob mob, GoalType type) {
        if (type == GoalType.TARGET) {
            return mob.getHandle().targetSelector;
        } else {
            return mob.getHandle().goalSelector;
        }
    }
}
