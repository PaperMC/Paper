package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Hopper;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftHopper extends CraftBlockData implements Hopper {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty ENABLED = getBoolean("enabled");

    @Override
    public boolean isEnabled() {
        return this.get(CraftHopper.ENABLED);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.set(CraftHopper.ENABLED, enabled);
    }
}
