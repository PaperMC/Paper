/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCoralFanWall extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.CoralWallFan, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftCoralFanWall() {
        super();
    }

    public CraftCoralFanWall(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.CoralWallFanBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftCoralFanWall.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftCoralFanWall.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftCoralFanWall.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.CoralWallFanBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftCoralFanWall.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftCoralFanWall.WATERLOGGED, waterlogged);
    }
}
