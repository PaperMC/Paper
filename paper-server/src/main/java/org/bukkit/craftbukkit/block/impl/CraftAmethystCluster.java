/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftAmethystCluster extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.AmethystCluster, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftAmethystCluster() {
        super();
    }

    public CraftAmethystCluster(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.AmethystClusterBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftAmethystCluster.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftAmethystCluster.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftAmethystCluster.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.AmethystClusterBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftAmethystCluster.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftAmethystCluster.WATERLOGGED, waterlogged);
    }
}
