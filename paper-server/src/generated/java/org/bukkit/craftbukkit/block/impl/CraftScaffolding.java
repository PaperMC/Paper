package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftScaffolding extends CraftBlockData implements Scaffolding {
    private static final BooleanProperty BOTTOM = ScaffoldingBlock.BOTTOM;

    private static final IntegerProperty DISTANCE = ScaffoldingBlock.DISTANCE;

    private static final BooleanProperty WATERLOGGED = ScaffoldingBlock.WATERLOGGED;

    public CraftScaffolding(BlockState state) {
        super(state);
    }

    @Override
    public boolean isBottom() {
        return this.get(BOTTOM);
    }

    @Override
    public void setBottom(final boolean bottom) {
        this.set(BOTTOM, bottom);
    }

    @Override
    public int getDistance() {
        return this.get(DISTANCE);
    }

    @Override
    public void setDistance(final int distance) {
        this.set(DISTANCE, distance);
    }

    @Override
    public int getMaximumDistance() {
        return DISTANCE.max;
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
