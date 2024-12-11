/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCandle extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Candle, org.bukkit.block.data.Lightable, org.bukkit.block.data.Waterlogged {

    public CraftCandle() {
        super();
    }

    public CraftCandle(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCandle

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty CANDLES = getInteger(net.minecraft.world.level.block.CandleBlock.class, "candles");

    @Override
    public int getCandles() {
        return this.get(CraftCandle.CANDLES);
    }

    @Override
    public void setCandles(int candles) {
        this.set(CraftCandle.CANDLES, candles);
    }

    @Override
    public int getMaximumCandles() {
        return getMax(CraftCandle.CANDLES);
    }
    // Paper start
    @Override
    public int getMinimumCandles() {
        return getMin(CraftCandle.CANDLES);
    }
    // Paper end

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean(net.minecraft.world.level.block.CandleBlock.class, "lit");

    @Override
    public boolean isLit() {
        return this.get(CraftCandle.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.set(CraftCandle.LIT, lit);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.CandleBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftCandle.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftCandle.WATERLOGGED, waterlogged);
    }
}
