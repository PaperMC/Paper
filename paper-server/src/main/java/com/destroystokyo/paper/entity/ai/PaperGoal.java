package com.destroystokyo.paper.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.entity.Mob;

/**
 * Wraps vanilla in api
 */
public class PaperGoal<T extends Mob> implements com.destroystokyo.paper.entity.ai.Goal<T> {

    private final Goal handle;
    private final GoalKey<T> key;

    private final EnumSet<GoalType> types;

    public PaperGoal(Goal handle) {
        this.handle = handle;
        this.key = MobGoalHelper.getKey(handle.getClass());
        this.types = MobGoalHelper.vanillaToPaper(handle);
    }

    public Goal getHandle() {
        return this.handle;
    }

    @Override
    public boolean shouldActivate() {
        return this.handle.canUse();
    }

    @Override
    public boolean shouldStayActive() {
        return this.handle.canContinueToUse();
    }

    @Override
    public void start() {
        this.handle.start();
    }

    @Override
    public void stop() {
        this.handle.stop();
    }

    @Override
    public void tick() {
        this.handle.tick();
    }

    @Override
    public GoalKey<T> getKey() {
        return this.key;
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return this.types;
    }

    public String toString() {
        return this.key.toString();
    }
}
