package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Repeater;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftRepeater extends CraftBlockData implements Repeater {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DELAY = getInteger("delay");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LOCKED = getBoolean("locked");

    @Override
    public int getDelay() {
        return this.get(CraftRepeater.DELAY);
    }

    @Override
    public void setDelay(int delay) {
        this.set(CraftRepeater.DELAY, delay);
    }

    @Override
    public int getMinimumDelay() {
        return getMin(CraftRepeater.DELAY);
    }

    @Override
    public int getMaximumDelay() {
        return getMax(CraftRepeater.DELAY);
    }

    @Override
    public boolean isLocked() {
        return this.get(CraftRepeater.LOCKED);
    }

    @Override
    public void setLocked(boolean locked) {
        this.set(CraftRepeater.LOCKED, locked);
    }
}
