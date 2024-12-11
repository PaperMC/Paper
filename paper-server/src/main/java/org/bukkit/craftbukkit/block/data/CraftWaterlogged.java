package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Waterlogged;

public abstract class CraftWaterlogged extends CraftBlockData implements Waterlogged {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean("waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftWaterlogged.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftWaterlogged.WATERLOGGED, waterlogged);
    }
}
