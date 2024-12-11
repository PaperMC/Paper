package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Powerable;

public abstract class CraftPowerable extends CraftBlockData implements Powerable {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean("powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftPowerable.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftPowerable.POWERED, powered);
    }
}
