package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Leaves;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftLeaves extends CraftBlockData implements Leaves {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger("distance");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty PERSISTENT = getBoolean("persistent");

    @Override
    public boolean isPersistent() {
        return this.get(CraftLeaves.PERSISTENT);
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.set(CraftLeaves.PERSISTENT, persistent);
    }

    @Override
    public int getDistance() {
        return this.get(CraftLeaves.DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        this.set(CraftLeaves.DISTANCE, distance);
    }
}
