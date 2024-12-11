package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Campfire;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCampfire extends CraftBlockData implements Campfire {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SIGNAL_FIRE = getBoolean("signal_fire");

    @Override
    public boolean isSignalFire() {
        return this.get(CraftCampfire.SIGNAL_FIRE);
    }

    @Override
    public void setSignalFire(boolean signalFire) {
        this.set(CraftCampfire.SIGNAL_FIRE, signalFire);
    }
}
