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
        return this.manager.runsNormally();
    }

    @Override
    public boolean isStepping() {
        return this.manager.isSteppingForward();
    }

    @Override
    public boolean isSprinting() {
        return this.manager.isSprinting();
    }

    @Override
    public boolean isFrozen() {
        return this.manager.isFrozen();
    }

    @Override
    public float getTickRate() {
        return this.manager.tickrate();
    }

    @Override
    public void setTickRate(final float tickRate) {
        Preconditions.checkArgument(tickRate >= 1.0F && tickRate <= 10_000.0F, "The given tick rate must not be less than 1.0 or greater than 10,000.0");
        this.manager.setTickRate(tickRate);
    }

    @Override
    public void setFrozen(final boolean frozen) {
        if (frozen) {
            if (this.manager.isSprinting()) {
                this.manager.stopSprinting();
            }

            if (this.manager.isSteppingForward()) {
                this.manager.stopStepping();
            }
        }

        this.manager.setFrozen(frozen);
    }

    @Override
    public boolean stepGameIfFrozen(final int ticks) {
        return this.manager.stepGameIfPaused(ticks);
    }

    @Override
    public boolean stopStepping() {
        return this.manager.stopStepping();
    }

    @Override
    public boolean requestGameToSprint(final int ticks) {
        return this.manager.requestGameToSprint(ticks);
    }

    @Override
    public boolean stopSprinting() {
        return this.manager.stopSprinting();
    }

    @Override
    public boolean isFrozen(final Entity entity) {
        Preconditions.checkArgument(entity != null, "The given entity must not be null");
        return this.manager.isEntityFrozen(((CraftEntity) entity).getHandle());
    }

    @Override
    public int getFrozenTicksToRun() {
        return this.manager.frozenTicksToRun();
    }
}
