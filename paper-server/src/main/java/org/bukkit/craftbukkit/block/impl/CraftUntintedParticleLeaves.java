/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftUntintedParticleLeaves extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Leaves, org.bukkit.block.data.Waterlogged {

    public CraftUntintedParticleLeaves() {
        super();
    }

    public CraftUntintedParticleLeaves(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLeaves

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger(net.minecraft.world.level.block.UntintedParticleLeavesBlock.class, "distance");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty PERSISTENT = getBoolean(net.minecraft.world.level.block.UntintedParticleLeavesBlock.class, "persistent");

    @Override
    public boolean isPersistent() {
        return this.get(CraftUntintedParticleLeaves.PERSISTENT);
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.set(CraftUntintedParticleLeaves.PERSISTENT, persistent);
    }

    @Override
    public int getDistance() {
        return this.get(CraftUntintedParticleLeaves.DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        this.set(CraftUntintedParticleLeaves.DISTANCE, distance);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.UntintedParticleLeavesBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftUntintedParticleLeaves.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftUntintedParticleLeaves.WATERLOGGED, waterlogged);
    }

    // Paper start
    @Override
    public int getMaximumDistance() {
        return getMax(CraftUntintedParticleLeaves.DISTANCE);
    }

    @Override
    public int getMinimumDistance() {
        return getMin(CraftUntintedParticleLeaves.DISTANCE);
    }
    // Paper end
}
