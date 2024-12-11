package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftScaffolding extends CraftBlockData implements Scaffolding {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty BOTTOM = getBoolean("bottom");
    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger("distance");

    @Override
    public boolean isBottom() {
        return this.get(CraftScaffolding.BOTTOM);
    }

    @Override
    public void setBottom(boolean bottom) {
        this.set(CraftScaffolding.BOTTOM, bottom);
    }

    @Override
    public int getDistance() {
        return this.get(CraftScaffolding.DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        this.set(CraftScaffolding.DISTANCE, distance);
    }

    @Override
    public int getMaximumDistance() {
        return getMax(CraftScaffolding.DISTANCE);
    }
}
