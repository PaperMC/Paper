package com.destroystokyo.paper.entity.ai;

import net.minecraft.server.PathfinderGoal;

import org.bukkit.entity.Mob;

/**
 * Wraps api in vanilla
 */
public class PaperCustomGoal<T extends Mob> extends PathfinderGoal {

    private final Goal<T> handle;

    public PaperCustomGoal(Goal<T> handle) {
        this.handle = handle;

        this.setTypes(MobGoalHelper.paperToVanilla(handle.getTypes()));
        if (this.getGoalTypes().size() == 0) {
            this.getGoalTypes().addUnchecked(Type.UNKNOWN_BEHAVIOR);
        }
    }

    @Override
    public boolean shouldActivate() {
        return handle.shouldActivate();
    }

    @Override
    public boolean shouldStayActive() {
        return handle.shouldStayActive();
    }

    @Override
    public void start() {
        handle.start();
    }

    @Override
    public void onTaskReset() {
        handle.stop();
    }

    @Override
    public void tick() {
        handle.tick();
    }

    public Goal<T> getHandle() {
        return handle;
    }

    public GoalKey<T> getKey() {
        return handle.getKey();
    }
}
