package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Dispenser;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftDispenser extends CraftBlockData implements Dispenser {

    private static final net.minecraft.server.BlockStateBoolean TRIGGERED = getBoolean("triggered");

    @Override
    public boolean isTriggered() {
        return get(TRIGGERED);
    }

    @Override
    public void setTriggered(boolean triggered) {
        set(TRIGGERED, triggered);
    }
}
