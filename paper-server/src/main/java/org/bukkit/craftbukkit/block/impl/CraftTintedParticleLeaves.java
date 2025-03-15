/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTintedParticleLeaves extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Leaves, org.bukkit.block.data.Waterlogged {

    public CraftTintedParticleLeaves() {
        super();
    }

    public CraftTintedParticleLeaves(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLeaves

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger(net.minecraft.world.level.block.TintedParticleLeavesBlock.class, "distance");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty PERSISTENT = getBoolean(net.minecraft.world.level.block.TintedParticleLeavesBlock.class, "persistent");

    @Override
    public boolean isPersistent() {
        return this.get(CraftTintedParticleLeaves.PERSISTENT);
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.set(CraftTintedParticleLeaves.PERSISTENT, persistent);
    }

    @Override
    public int getDistance() {
        return this.get(CraftTintedParticleLeaves.DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        this.set(CraftTintedParticleLeaves.DISTANCE, distance);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.TintedParticleLeavesBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftTintedParticleLeaves.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftTintedParticleLeaves.WATERLOGGED, waterlogged);
    }

    // Paper start
    @Override
    public int getMinimumDistance() {
        return getMin(CraftTintedParticleLeaves.DISTANCE);
    }

    @Override
    public int getMaximumDistance() {
        return getMax(CraftTintedParticleLeaves.DISTANCE);
    }
    // Paper end
}
