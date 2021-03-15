package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Campfire;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCampfire extends CraftBlockData implements Campfire {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean SIGNAL_FIRE = getBoolean("signal_fire");

    @Override
    public boolean isSignalFire() {
        return get(SIGNAL_FIRE);
    }

    @Override
    public void setSignalFire(boolean signalFire) {
        set(SIGNAL_FIRE, signalFire);
    }
}
