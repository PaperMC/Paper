package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.PinkPetals;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftPinkPetals extends CraftBlockData implements PinkPetals {

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger FLOWER_AMOUNT = getInteger("flower_amount");

    @Override
    public int getFlowerAmount() {
        return get(FLOWER_AMOUNT);
    }

    @Override
    public void setFlowerAmount(int flower_amount) {
        set(FLOWER_AMOUNT, flower_amount);
    }

    @Override
    public int getMaximumFlowerAmount() {
        return getMax(FLOWER_AMOUNT);
    }
}
