/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSculkSensor extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.SculkSensor, org.bukkit.block.data.AnaloguePowerable, org.bukkit.block.data.Waterlogged {

    public CraftSculkSensor() {
        super();
    }

    public CraftSculkSensor(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSculkSensor

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> PHASE = getEnum(net.minecraft.world.level.block.SculkSensorBlock.class, "sculk_sensor_phase");

    @Override
    public org.bukkit.block.data.type.SculkSensor.Phase getPhase() {
        return get(PHASE, org.bukkit.block.data.type.SculkSensor.Phase.class);
    }

    @Override
    public void setPhase(org.bukkit.block.data.type.SculkSensor.Phase phase) {
        set(PHASE, phase);
    }

    // org.bukkit.craftbukkit.block.data.CraftAnaloguePowerable

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger POWER = getInteger(net.minecraft.world.level.block.SculkSensorBlock.class, "power");

    @Override
    public int getPower() {
        return get(POWER);
    }

    @Override
    public void setPower(int power) {
        set(POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(POWER);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean WATERLOGGED = getBoolean(net.minecraft.world.level.block.SculkSensorBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
