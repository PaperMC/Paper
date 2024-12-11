package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.AnaloguePowerable;

public abstract class CraftAnaloguePowerable extends CraftBlockData implements AnaloguePowerable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = getInteger("power");

    @Override
    public int getPower() {
        return this.get(CraftAnaloguePowerable.POWER);
    }

    @Override
    public void setPower(int power) {
        this.set(CraftAnaloguePowerable.POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(CraftAnaloguePowerable.POWER);
    }
}
