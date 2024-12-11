package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftDaylightDetector extends CraftBlockData implements DaylightDetector {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty INVERTED = getBoolean("inverted");

    @Override
    public boolean isInverted() {
        return this.get(CraftDaylightDetector.INVERTED);
    }

    @Override
    public void setInverted(boolean inverted) {
        this.set(CraftDaylightDetector.INVERTED, inverted);
    }
}
