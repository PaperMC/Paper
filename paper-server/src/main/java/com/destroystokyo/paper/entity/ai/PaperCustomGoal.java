package com.destroystokyo.paper.entity.ai;

import org.bukkit.entity.Mob;

/**
 * Wraps api in vanilla
 */
public class PaperCustomGoal<T extends Mob> extends net.minecraft.world.entity.ai.goal.Goal {

    private final Goal<T> handle;

    public PaperCustomGoal(Goal<T> handle) {
        this.handle = handle;

        this.setFlags(MobGoalHelper.paperToVanilla(handle.getTypes()));
        if (this.getFlags().size() == 0) {
            this.addFlag(Flag.UNKNOWN_BEHAVIOR);
        }
    }

    @Override
    public boolean canUse() {
        return handle.shouldActivate();
    }

    @Override
    public boolean canContinueToUse() {
        return handle.shouldStayActive();
    }

    @Override
    public void start() {
        handle.start();
    }

    @Override
    public void stop() {
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
