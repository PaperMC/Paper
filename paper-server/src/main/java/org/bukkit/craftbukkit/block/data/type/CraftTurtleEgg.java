package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftTurtleEgg extends CraftBlockData implements TurtleEgg {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty EGGS = getInteger("eggs");

    @Override
    public int getEggs() {
        return this.get(CraftTurtleEgg.EGGS);
    }

    @Override
    public void setEggs(int eggs) {
        this.set(CraftTurtleEgg.EGGS, eggs);
    }

    @Override
    public int getMinimumEggs() {
        return getMin(CraftTurtleEgg.EGGS);
    }

    @Override
    public int getMaximumEggs() {
        return getMax(CraftTurtleEgg.EGGS);
    }
}
