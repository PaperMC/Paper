/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftScaffolding extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Scaffolding, org.bukkit.block.data.Waterlogged {

    public CraftScaffolding() {
        super();
    }

    public CraftScaffolding(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftScaffolding

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty BOTTOM = getBoolean(net.minecraft.world.level.block.ScaffoldingBlock.class, "bottom");
    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger(net.minecraft.world.level.block.ScaffoldingBlock.class, "distance");

    @Override
    public boolean isBottom() {
        return this.get(CraftScaffolding.BOTTOM);
    }

    @Override
    public void setBottom(boolean bottom) {
        this.set(CraftScaffolding.BOTTOM, bottom);
    }

    @Override
    public int getDistance() {
        return this.get(CraftScaffolding.DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        this.set(CraftScaffolding.DISTANCE, distance);
    }

    @Override
    public int getMaximumDistance() {
        return getMax(CraftScaffolding.DISTANCE);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.ScaffoldingBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftScaffolding.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftScaffolding.WATERLOGGED, waterlogged);
    }
}
