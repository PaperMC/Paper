package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Lantern;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftLantern extends CraftBlockData implements Lantern {

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
