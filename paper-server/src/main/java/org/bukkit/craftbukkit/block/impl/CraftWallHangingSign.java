/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWallHangingSign extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.WallHangingSign, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftWallHangingSign() {
        super();
    }

    public CraftWallHangingSign(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.WallHangingSignBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftWallHangingSign.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftWallHangingSign.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftWallHangingSign.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.WallHangingSignBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftWallHangingSign.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftWallHangingSign.WATERLOGGED, waterlogged);
    }
}
