package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.SuspiciousSand;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftSuspiciousSand extends CraftBlockData implements SuspiciousSand {

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger DUSTED = getInteger("dusted");

    @Override
    public int getDusted() {
        return get(DUSTED);
    }

    @Override
    public void setDusted(int dusted) {
        set(DUSTED, dusted);
    }

    @Override
    public int getMaximumDusted() {
        return getMax(DUSTED);
    }
}
