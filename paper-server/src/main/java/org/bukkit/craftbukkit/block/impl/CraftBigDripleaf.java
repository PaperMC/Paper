/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBigDripleaf extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.BigDripleaf, org.bukkit.block.data.type.Dripleaf, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftBigDripleaf() {
        super();
    }

    public CraftBigDripleaf(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftBigDripleaf

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> TILT = getEnum(net.minecraft.world.level.block.BigDripleafBlock.class, "tilt");

    @Override
    public Tilt getTilt() {
        return this.get(CraftBigDripleaf.TILT, org.bukkit.block.data.type.BigDripleaf.Tilt.class);
    }

    @Override
    public void setTilt(org.bukkit.block.data.type.BigDripleaf.Tilt tilt) {
        this.set(CraftBigDripleaf.TILT, tilt);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.BigDripleafBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftBigDripleaf.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftBigDripleaf.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftBigDripleaf.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.BigDripleafBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftBigDripleaf.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftBigDripleaf.WATERLOGGED, waterlogged);
    }
}
