package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Hatchable;

public abstract class CraftHatchable extends CraftBlockData implements Hatchable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty HATCH = getInteger("hatch");

    @Override
    public int getHatch() {
        return this.get(CraftHatchable.HATCH);
    }

    @Override
    public void setHatch(int hatch) {
        this.set(CraftHatchable.HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return getMax(CraftHatchable.HATCH);
    }
}
