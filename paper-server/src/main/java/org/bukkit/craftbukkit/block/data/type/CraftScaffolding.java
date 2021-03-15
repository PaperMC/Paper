package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftScaffolding extends CraftBlockData implements Scaffolding {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean BOTTOM = getBoolean("bottom");
    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger DISTANCE = getInteger("distance");

    @Override
    public boolean isBottom() {
        return get(BOTTOM);
    }

    @Override
    public void setBottom(boolean bottom) {
        set(BOTTOM, bottom);
    }

    @Override
    public int getDistance() {
        return get(DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        set(DISTANCE, distance);
    }

    @Override
    public int getMaximumDistance() {
        return getMax(DISTANCE);
    }
}
