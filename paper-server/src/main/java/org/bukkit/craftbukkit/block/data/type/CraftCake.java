package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Cake;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCake extends CraftBlockData implements Cake {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty BITES = getInteger("bites");

    @Override
    public int getBites() {
        return this.get(CraftCake.BITES);
    }

    @Override
    public void setBites(int bites) {
        this.set(CraftCake.BITES, bites);
    }

    @Override
    public int getMaximumBites() {
        return getMax(CraftCake.BITES);
    }
}
