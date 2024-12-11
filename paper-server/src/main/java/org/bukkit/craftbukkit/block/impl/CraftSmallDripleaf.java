/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSmallDripleaf extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.SmallDripleaf, org.bukkit.block.data.type.Dripleaf, org.bukkit.block.data.Bisected, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftSmallDripleaf() {
        super();
    }

    public CraftSmallDripleaf(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum(net.minecraft.world.level.block.SmallDripleafBlock.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftSmallDripleaf.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftSmallDripleaf.HALF, half);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.SmallDripleafBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftSmallDripleaf.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftSmallDripleaf.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftSmallDripleaf.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.SmallDripleafBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftSmallDripleaf.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftSmallDripleaf.WATERLOGGED, waterlogged);
    }
}
