package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Piston;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftPiston extends CraftBlockData implements Piston {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty EXTENDED = getBoolean("extended");

    @Override
    public boolean isExtended() {
        return this.get(CraftPiston.EXTENDED);
    }

    @Override
    public void setExtended(boolean extended) {
        this.set(CraftPiston.EXTENDED, extended);
    }
}
