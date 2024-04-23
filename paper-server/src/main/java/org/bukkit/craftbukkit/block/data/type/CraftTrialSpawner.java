package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.TrialSpawner;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftTrialSpawner extends CraftBlockData implements TrialSpawner {

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> TRIAL_SPAWNER_STATE = getEnum("trial_spawner_state");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean OMINOUS = getBoolean("ominous");

    @Override
    public org.bukkit.block.data.type.TrialSpawner.State getTrialSpawnerState() {
        return get(TRIAL_SPAWNER_STATE, org.bukkit.block.data.type.TrialSpawner.State.class);
    }

    @Override
    public void setTrialSpawnerState(org.bukkit.block.data.type.TrialSpawner.State state) {
        set(TRIAL_SPAWNER_STATE, state);
    }

    @Override
    public boolean isOminous() {
        return get(OMINOUS);
    }

    @Override
    public void setOminous(boolean ominous) {
        set(OMINOUS, ominous);
    }
}
