package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftTurtleEgg extends CraftBlockData implements TurtleEgg {

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger EGGS = getInteger("eggs");

    @Override
    public int getEggs() {
        return get(EGGS);
    }

    @Override
    public void setEggs(int eggs) {
        set(EGGS, eggs);
    }

    @Override
    public int getMinimumEggs() {
        return getMin(EGGS);
    }

    @Override
    public int getMaximumEggs() {
        return getMax(EGGS);
    }
}
