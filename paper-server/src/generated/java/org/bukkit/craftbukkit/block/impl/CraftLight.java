package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Light;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftLight extends CraftBlockData implements Light {
    private static final IntegerProperty LEVEL = LightBlock.LEVEL;

    private static final BooleanProperty WATERLOGGED = LightBlock.WATERLOGGED;

    public CraftLight(BlockState state) {
        super(state);
    }

    @Override
    public int getLevel() {
        return this.get(LEVEL);
    }

    @Override
    public void setLevel(final int level) {
        this.set(LEVEL, level);
    }

    @Override
    public int getMinimumLevel() {
        return LEVEL.min;
    }

    @Override
    public int getMaximumLevel() {
        return LEVEL.max;
    }

    @Override
    public boolean isWaterlogged() {
        return this.get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(final boolean waterlogged) {
        this.set(WATERLOGGED, waterlogged);
    }
}
