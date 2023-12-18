package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.server.ServerTickRateManager;
import org.bukkit.ServerTickManager;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

final class CraftServerTickManager implements ServerTickManager {

    private final ServerTickRateManager manager;

    CraftServerTickManager(ServerTickRateManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean isRunningNormally() {
        return manager.runsNormally();
    }

    @Override
    public boolean isStepping() {
        return manager.isSteppingForward();
    }

    @Override
    public boolean isSprinting() {
        return manager.isSprinting();
    }

    @Override
    public boolean isFrozen() {
        return manager.isFrozen();
    }

    @Override
    public float getTickRate() {
        return manager.tickrate();
    }

    @Override
    public void setTickRate(final float tickRate) {
        Preconditions.checkArgument(tickRate >= 1.0F && tickRate <= 10_000.0F, "The given tick rate must not be less than 1.0 or greater than 10,000.0");
        manager.setTickRate(tickRate);
    }

    @Override
    public void setFrozen(final boolean frozen) {
        if (frozen) {
            if (manager.isSprinting()) {
                manager.stopSprinting();
            }

            if (manager.isSteppingForward()) {
                manager.stopStepping();
            }
        }

        manager.setFrozen(frozen);
    }

    @Override
    public boolean stepGameIfFrozen(final int ticks) {
        return manager.stepGameIfPaused(ticks);
    }

    @Override
    public boolean stopStepping() {
        return manager.stopStepping();
    }

    @Override
    public boolean requestGameToSprint(final int ticks) {
        return manager.requestGameToSprint(ticks);
    }

    @Override
    public boolean stopSprinting() {
        return manager.stopSprinting();
    }

    @Override
    public boolean isFrozen(final Entity entity) {
        Preconditions.checkArgument(entity != null, "The given entity must not be null");
        return manager.isEntityFrozen(((CraftEntity) entity).getHandle());
    }

    @Override
    public int getFrozenTicksToRun() {
        return manager.frozenTicksToRun();
    }
}
