package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftBrewingStand extends CraftBlockData implements BrewingStand {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean[] HAS_BOTTLE = new net.minecraft.world.level.block.state.properties.BlockStateBoolean[]{
        getBoolean("has_bottle_0"), getBoolean("has_bottle_1"), getBoolean("has_bottle_2")
    };

    @Override
    public boolean hasBottle(int bottle) {
        return get(HAS_BOTTLE[bottle]);
    }

    @Override
    public void setBottle(int bottle, boolean has) {
        set(HAS_BOTTLE[bottle], has);
    }

    @Override
    public java.util.Set<Integer> getBottles() {
        com.google.common.collect.ImmutableSet.Builder<Integer> bottles = com.google.common.collect.ImmutableSet.builder();

        for (int index = 0; index < getMaximumBottles(); index++) {
            if (hasBottle(index)) {
                bottles.add(index);
            }
        }

        return bottles.build();
    }

    @Override
    public int getMaximumBottles() {
        return HAS_BOTTLE.length;
    }
}
