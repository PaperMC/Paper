/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftPointedDripstone extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.PointedDripstone, org.bukkit.block.data.Waterlogged {

    public CraftPointedDripstone() {
        super();
    }

    public CraftPointedDripstone(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftPointedDripstone

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> VERTICAL_DIRECTION = getEnum(net.minecraft.world.level.block.PointedDripstoneBlock.class, "vertical_direction");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> THICKNESS = getEnum(net.minecraft.world.level.block.PointedDripstoneBlock.class, "thickness");

    @Override
    public org.bukkit.block.BlockFace getVerticalDirection() {
        return this.get(CraftPointedDripstone.VERTICAL_DIRECTION, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setVerticalDirection(org.bukkit.block.BlockFace direction) {
        this.set(CraftPointedDripstone.VERTICAL_DIRECTION, direction);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getVerticalDirections() {
        return this.getValues(CraftPointedDripstone.VERTICAL_DIRECTION, org.bukkit.block.BlockFace.class);
    }

    @Override
    public org.bukkit.block.data.type.PointedDripstone.Thickness getThickness() {
        return this.get(CraftPointedDripstone.THICKNESS, org.bukkit.block.data.type.PointedDripstone.Thickness.class);
    }

    @Override
    public void setThickness(org.bukkit.block.data.type.PointedDripstone.Thickness thickness) {
        this.set(CraftPointedDripstone.THICKNESS, thickness);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.PointedDripstoneBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftPointedDripstone.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftPointedDripstone.WATERLOGGED, waterlogged);
    }
}
