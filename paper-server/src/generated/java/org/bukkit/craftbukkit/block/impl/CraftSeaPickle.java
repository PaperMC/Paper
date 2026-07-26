package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftSeaPickle extends CraftBlockData implements SeaPickle {
    private static final IntegerProperty PICKLES = SeaPickleBlock.PICKLES;

    private static final BooleanProperty WATERLOGGED = SeaPickleBlock.WATERLOGGED;

    public CraftSeaPickle(BlockState state) {
        super(state);
    }

    @Override
    public int getPickles() {
        return this.get(PICKLES);
    }

    @Override
    public void setPickles(final int pickles) {
        this.set(PICKLES, pickles);
    }

    @Override
    public int getMinimumPickles() {
        return PICKLES.min;
    }

    @Override
    public int getMaximumPickles() {
        return PICKLES.max;
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
