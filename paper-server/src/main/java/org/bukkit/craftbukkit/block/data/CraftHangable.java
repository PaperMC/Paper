package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Hangable;

public abstract class CraftHangable extends CraftBlockData implements Hangable {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean HANGING = getBoolean("hanging");

    @Override
    public boolean isHanging() {
        return get(HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        set(HANGING, hanging);
    }
}
