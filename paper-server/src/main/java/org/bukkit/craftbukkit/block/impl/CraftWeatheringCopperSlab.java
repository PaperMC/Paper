/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWeatheringCopperSlab extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Slab, org.bukkit.block.data.Waterlogged {

    public CraftWeatheringCopperSlab() {
        super();
    }

    public CraftWeatheringCopperSlab(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSlab

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> TYPE = getEnum(net.minecraft.world.level.block.WeatheringCopperSlabBlock.class, "type");

    @Override
    public org.bukkit.block.data.type.Slab.Type getType() {
        return this.get(CraftWeatheringCopperSlab.TYPE, org.bukkit.block.data.type.Slab.Type.class);
    }

    @Override
    public void setType(org.bukkit.block.data.type.Slab.Type type) {
        this.set(CraftWeatheringCopperSlab.TYPE, type);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.WeatheringCopperSlabBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftWeatheringCopperSlab.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftWeatheringCopperSlab.WATERLOGGED, waterlogged);
    }
}
