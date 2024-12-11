package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Farmland;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftFarmland extends CraftBlockData implements Farmland {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty MOISTURE = getInteger("moisture");

    @Override
    public int getMoisture() {
        return this.get(CraftFarmland.MOISTURE);
    }

    @Override
    public void setMoisture(int moisture) {
        this.set(CraftFarmland.MOISTURE, moisture);
    }

    @Override
    public int getMaximumMoisture() {
        return getMax(CraftFarmland.MOISTURE);
    }
}
