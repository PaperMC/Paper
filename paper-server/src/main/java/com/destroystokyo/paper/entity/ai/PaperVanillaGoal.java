package com.destroystokyo.paper.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.entity.Mob;

/**
 * Wraps vanilla in api
 */
public class PaperVanillaGoal<T extends Mob> implements VanillaGoal<T> {

    private final Goal handle;
    private final GoalKey<T> key;

    private final EnumSet<GoalType> types;

    public PaperVanillaGoal(Goal handle) {
        this.handle = handle;
        this.key = MobGoalHelper.getKey(handle.getClass());
        this.types = MobGoalHelper.vanillaToPaper(handle);
    }

    public Goal getHandle() {
        return handle;
    }

    @Override
    public boolean shouldActivate() {
        return handle.canUse();
    }

    @Override
    public boolean shouldStayActive() {
        return handle.canContinueToUse();
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

    @Override
    public GoalKey<T> getKey() {
        return key;
    }

    @Override
    public EnumSet<GoalType> getTypes() {
        return types;
    }
}
