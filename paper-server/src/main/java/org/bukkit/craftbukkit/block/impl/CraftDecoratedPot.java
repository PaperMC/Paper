/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftDecoratedPot extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.DecoratedPot, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftDecoratedPot() {
        super();
    }

    public CraftDecoratedPot(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.DecoratedPotBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftDecoratedPot.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftDecoratedPot.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftDecoratedPot.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.DecoratedPotBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftDecoratedPot.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftDecoratedPot.WATERLOGGED, waterlogged);
    }
}
