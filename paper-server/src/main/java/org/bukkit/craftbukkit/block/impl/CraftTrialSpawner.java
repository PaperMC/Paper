/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTrialSpawner extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TrialSpawner {

    public CraftTrialSpawner() {
        super();
    }

    public CraftTrialSpawner(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTrialSpawner

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> TRIAL_SPAWNER_STATE = getEnum(net.minecraft.world.level.block.TrialSpawnerBlock.class, "trial_spawner_state");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean OMINOUS = getBoolean(net.minecraft.world.level.block.TrialSpawnerBlock.class, "ominous");

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
