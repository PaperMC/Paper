/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMinecartDetector extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.RedstoneRail, org.bukkit.block.data.Powerable, org.bukkit.block.data.Rail, org.bukkit.block.data.Waterlogged {

    public CraftMinecartDetector() {
        super();
    }

    public CraftMinecartDetector(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.DetectorRailBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftMinecartDetector.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftMinecartDetector.POWERED, powered);
    }

    // org.bukkit.craftbukkit.block.data.CraftRail

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> SHAPE = getEnum(net.minecraft.world.level.block.DetectorRailBlock.class, "shape");

    @Override
    public org.bukkit.block.data.Rail.Shape getShape() {
        return this.get(CraftMinecartDetector.SHAPE, org.bukkit.block.data.Rail.Shape.class);
    }

    @Override
    public void setShape(org.bukkit.block.data.Rail.Shape shape) {
        this.set(CraftMinecartDetector.SHAPE, shape);
    }

    @Override
    public java.util.Set<org.bukkit.block.data.Rail.Shape> getShapes() {
        return this.getValues(CraftMinecartDetector.SHAPE, org.bukkit.block.data.Rail.Shape.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.DetectorRailBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftMinecartDetector.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftMinecartDetector.WATERLOGGED, waterlogged);
    }
}
