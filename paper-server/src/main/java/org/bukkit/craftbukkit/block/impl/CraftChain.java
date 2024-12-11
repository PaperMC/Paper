/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftChain extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Chain, org.bukkit.block.data.Orientable, org.bukkit.block.data.Waterlogged {

    public CraftChain() {
        super();
    }

    public CraftChain(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftOrientable

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> AXIS = getEnum(net.minecraft.world.level.block.ChainBlock.class, "axis");

    @Override
    public org.bukkit.Axis getAxis() {
        return this.get(CraftChain.AXIS, org.bukkit.Axis.class);
    }

    @Override
    public void setAxis(org.bukkit.Axis axis) {
        this.set(CraftChain.AXIS, axis);
    }

    @Override
    public java.util.Set<org.bukkit.Axis> getAxes() {
        return this.getValues(CraftChain.AXIS, org.bukkit.Axis.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.ChainBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftChain.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftChain.WATERLOGGED, waterlogged);
    }
}
