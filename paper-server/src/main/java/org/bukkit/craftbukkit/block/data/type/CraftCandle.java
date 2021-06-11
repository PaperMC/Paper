package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Candle;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCandle extends CraftBlockData implements Candle {

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger CANDLES = getInteger("candles");

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
}
