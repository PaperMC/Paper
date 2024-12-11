/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTrialSpawner extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TrialSpawner {

    public CraftTrialSpawner() {
        super();
    }

    public CraftTrialSpawner(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTrialSpawner

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> TRIAL_SPAWNER_STATE = getEnum(net.minecraft.world.level.block.TrialSpawnerBlock.class, "trial_spawner_state");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OMINOUS = getBoolean(net.minecraft.world.level.block.TrialSpawnerBlock.class, "ominous");

    @Override
    public org.bukkit.block.data.type.TrialSpawner.State getTrialSpawnerState() {
        return this.get(CraftTrialSpawner.TRIAL_SPAWNER_STATE, org.bukkit.block.data.type.TrialSpawner.State.class);
    }

    @Override
    public void setTrialSpawnerState(org.bukkit.block.data.type.TrialSpawner.State state) {
        this.set(CraftTrialSpawner.TRIAL_SPAWNER_STATE, state);
    }

    @Override
    public boolean isOminous() {
        return this.get(CraftTrialSpawner.OMINOUS);
    }

    @Override
    public void setOminous(boolean ominous) {
        this.set(CraftTrialSpawner.OMINOUS, ominous);
    }
}
