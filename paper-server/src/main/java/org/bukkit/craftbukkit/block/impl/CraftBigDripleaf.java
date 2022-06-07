/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBigDripleaf extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.BigDripleaf, org.bukkit.block.data.type.Dripleaf, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftBigDripleaf() {
        super();
    }

    public CraftBigDripleaf(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftBigDripleaf

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> TILT = getEnum(net.minecraft.world.level.block.BigDripleafBlock.class, "tilt");

    @Override
    public Tilt getTilt() {
        return get(TILT, org.bukkit.block.data.type.BigDripleaf.Tilt.class);
    }

    @Override
    public void setTilt(org.bukkit.block.data.type.BigDripleaf.Tilt tilt) {
        set(TILT, tilt);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> FACING = getEnum(net.minecraft.world.level.block.BigDripleafBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.world.level.block.BigDripleafBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
