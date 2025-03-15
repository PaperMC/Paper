/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLeafLitter extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.LeafLitter, org.bukkit.block.data.Segmentable, org.bukkit.block.data.Directional {

    public CraftLeafLitter() {
        super();
    }

    public CraftLeafLitter(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.LeafLitterBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftLeafLitter.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftLeafLitter.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftLeafLitter.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftSegmentable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty SEGMENT_AMOUNT = getInteger(net.minecraft.world.level.block.LeafLitterBlock.class, "segment_amount");

    @Override
    public int getSegmentAmount() {
        return this.get(CraftLeafLitter.SEGMENT_AMOUNT);
    }

    @Override
    public void setSegmentAmount(int power) {
        this.set(CraftLeafLitter.SEGMENT_AMOUNT, power);
    }

    @Override
    public int getMinimumSegmentAmount() {
        return getMin(CraftLeafLitter.SEGMENT_AMOUNT);
    }

    @Override
    public int getMaximumSegmentAmount() {
        return getMax(CraftLeafLitter.SEGMENT_AMOUNT);
    }
}
