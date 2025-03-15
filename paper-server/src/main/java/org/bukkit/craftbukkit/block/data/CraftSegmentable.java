package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Segmentable;

public abstract class CraftSegmentable extends CraftBlockData implements Segmentable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty SEGMENT_AMOUNT = getInteger("segment_amount");

    @Override
    public int getSegmentAmount() {
        return this.get(CraftSegmentable.SEGMENT_AMOUNT);
    }

    @Override
    public void setSegmentAmount(int segmentAmount) {
        this.set(CraftSegmentable.SEGMENT_AMOUNT, segmentAmount);
    }

    @Override
    public int getMinimumSegmentAmount() {
        return getMin(CraftSegmentable.SEGMENT_AMOUNT);
    }

    @Override
    public int getMaximumSegmentAmount() {
        return getMax(CraftSegmentable.SEGMENT_AMOUNT);
    }
}
