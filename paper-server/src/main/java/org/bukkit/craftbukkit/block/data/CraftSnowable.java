package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Snowable;

public abstract class CraftSnowable extends CraftBlockData implements Snowable {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SNOWY = getBoolean("snowy");

    @Override
    public boolean isSnowy() {
        return this.get(CraftSnowable.SNOWY);
    }

    @Override
    public void setSnowy(boolean snowy) {
        this.set(CraftSnowable.SNOWY, snowy);
    }
}
