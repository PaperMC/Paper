package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.TNT;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftTNT extends CraftBlockData implements TNT {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty UNSTABLE = getBoolean("unstable");

    @Override
    public boolean isUnstable() {
        return this.get(CraftTNT.UNSTABLE);
    }

    @Override
    public void setUnstable(boolean unstable) {
        this.set(CraftTNT.UNSTABLE, unstable);
    }
}
