package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Sapling;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftSapling extends CraftBlockData implements Sapling {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty STAGE = getInteger("stage");

    @Override
    public int getStage() {
        return this.get(CraftSapling.STAGE);
    }

    @Override
    public void setStage(int stage) {
        this.set(CraftSapling.STAGE, stage);
    }

    @Override
    public int getMaximumStage() {
        return getMax(CraftSapling.STAGE);
    }
}
