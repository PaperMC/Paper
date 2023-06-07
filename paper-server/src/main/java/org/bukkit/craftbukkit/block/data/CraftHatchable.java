package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Hatchable;

public abstract class CraftHatchable extends CraftBlockData implements Hatchable {

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger HATCH = getInteger("hatch");

    @Override
    public int getHatch() {
        return get(HATCH);
    }

    @Override
    public void setHatch(int hatch) {
        set(HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return getMax(HATCH);
    }
}
