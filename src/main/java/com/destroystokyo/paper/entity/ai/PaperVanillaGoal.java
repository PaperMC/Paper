package com.destroystokyo.paper.entity.ai;

import net.minecraft.server.PathfinderGoal;

import java.util.EnumSet;

import org.bukkit.entity.Mob;

/**
 * Wraps vanilla in api
 */
public class PaperVanillaGoal<T extends Mob> implements VanillaGoal<T> {

    private final PathfinderGoal handle;
    private final GoalKey<T> key;

    private final EnumSet<GoalType> types;

    public PaperVanillaGoal(PathfinderGoal handle) {
        this.handle = handle;
        this.key = MobGoalHelper.getKey(handle.getClass());
        this.types = MobGoalHelper.vanillaToPaper(handle.getGoalTypes());
    }

    public PathfinderGoal getHandle() {
        return handle;
    }

    @Override
    public boolean shouldActivate() {
        return handle.shouldActivate2();
    }

    @Override
    public boolean shouldStayActive() {
        return handle.shouldStayActive2();
    }

    @Override
    public void start() {
        handle.start();
    }

    @Override
    public void stop() {
        handle.onTaskReset();
    }

    @Override
    public void tick() {
        handle.tick();
    }

    @Override
    public GoalKey<T> getKey() {
        return key;
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return types;
    }
}
