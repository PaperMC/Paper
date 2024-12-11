package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.PinkPetals;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftPinkPetals extends CraftBlockData implements PinkPetals {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty FLOWER_AMOUNT = getInteger("flower_amount");

    @Override
    public int getFlowerAmount() {
        return this.get(CraftPinkPetals.FLOWER_AMOUNT);
    }

    @Override
    public void setFlowerAmount(int flower_amount) {
        this.set(CraftPinkPetals.FLOWER_AMOUNT, flower_amount);
    }

    @Override
    public int getMaximumFlowerAmount() {
        return getMax(CraftPinkPetals.FLOWER_AMOUNT);
    }
}
