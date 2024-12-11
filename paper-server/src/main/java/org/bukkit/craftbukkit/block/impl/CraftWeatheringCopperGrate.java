/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWeatheringCopperGrate extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Waterlogged {

    public CraftWeatheringCopperGrate() {
        super();
    }

    public CraftWeatheringCopperGrate(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.WeatheringCopperGrateBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftWeatheringCopperGrate.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftWeatheringCopperGrate.WATERLOGGED, waterlogged);
    }
}
