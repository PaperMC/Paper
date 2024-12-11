/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftRedstoneComparator extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Comparator, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftRedstoneComparator() {
        super();
    }

    public CraftRedstoneComparator(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftComparator

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> MODE = getEnum(net.minecraft.world.level.block.ComparatorBlock.class, "mode");

    @Override
    public org.bukkit.block.data.type.Comparator.Mode getMode() {
        return this.get(CraftRedstoneComparator.MODE, org.bukkit.block.data.type.Comparator.Mode.class);
    }

    @Override
    public void setMode(org.bukkit.block.data.type.Comparator.Mode mode) {
        this.set(CraftRedstoneComparator.MODE, mode);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.ComparatorBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftRedstoneComparator.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftRedstoneComparator.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftRedstoneComparator.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.ComparatorBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftRedstoneComparator.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftRedstoneComparator.POWERED, powered);
    }
}
