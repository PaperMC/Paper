/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSculkSensor extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.SculkSensor, org.bukkit.block.data.AnaloguePowerable, org.bukkit.block.data.Waterlogged {

    public CraftSculkSensor() {
        super();
    }

    public CraftSculkSensor(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSculkSensor

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> PHASE = getEnum(net.minecraft.world.level.block.SculkSensorBlock.class, "sculk_sensor_phase");

    @Override
    public org.bukkit.block.data.type.SculkSensor.Phase getPhase() {
        return this.get(CraftSculkSensor.PHASE, org.bukkit.block.data.type.SculkSensor.Phase.class);
    }

    @Override
    public void setPhase(org.bukkit.block.data.type.SculkSensor.Phase phase) {
        this.set(CraftSculkSensor.PHASE, phase);
    }

    // org.bukkit.craftbukkit.block.data.CraftAnaloguePowerable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = getInteger(net.minecraft.world.level.block.SculkSensorBlock.class, "power");

    @Override
    public int getPower() {
        return this.get(CraftSculkSensor.POWER);
    }

    @Override
    public void setPower(int power) {
        this.set(CraftSculkSensor.POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(CraftSculkSensor.POWER);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.SculkSensorBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftSculkSensor.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftSculkSensor.WATERLOGGED, waterlogged);
    }
}
