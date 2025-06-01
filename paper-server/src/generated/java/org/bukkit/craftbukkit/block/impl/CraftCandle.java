package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Candle;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftCandle extends CraftBlockData implements Candle {
    private static final IntegerProperty CANDLES = CandleBlock.CANDLES;

    private static final BooleanProperty LIT = CandleBlock.LIT;

    private static final BooleanProperty WATERLOGGED = CandleBlock.WATERLOGGED;

    public CraftCandle(BlockState state) {
        super(state);
    }

    @Override
    public int getCandles() {
        return this.get(CANDLES);
    }

    @Override
    public void setCandles(final int candles) {
        this.set(CANDLES, candles);
    }

    @Override
    public int getMinimumCandles() {
        return CANDLES.min;
    }

    @Override
    public int getMaximumCandles() {
        return CANDLES.max;
    }

    @Override
    public boolean isLit() {
        return this.get(LIT);
    }

    @Override
    public void setLit(final boolean lit) {
        this.set(LIT, lit);
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
