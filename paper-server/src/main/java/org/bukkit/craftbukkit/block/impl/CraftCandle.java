/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCandle extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Candle, org.bukkit.block.data.Lightable, org.bukkit.block.data.Waterlogged {

    public CraftCandle() {
        super();
    }

    public CraftCandle(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCandle

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger CANDLES = getInteger(net.minecraft.world.level.block.CandleBlock.class, "candles");

    @Override
    public int getCandles() {
        return get(CANDLES);
    }

    @Override
    public void setCandles(int candles) {
        set(CANDLES, candles);
    }

    @Override
    public int getMaximumCandles() {
        return getMax(CANDLES);
    }

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean LIT = getBoolean(net.minecraft.world.level.block.CandleBlock.class, "lit");

    @Override
    public boolean isLit() {
        return get(LIT);
    }

    @Override
    public void setLit(boolean lit) {
        set(LIT, lit);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.world.level.block.CandleBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
