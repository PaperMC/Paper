/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWeatheringCopperTrapDoor extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TrapDoor, org.bukkit.block.data.Bisected, org.bukkit.block.data.Directional, org.bukkit.block.data.Openable, org.bukkit.block.data.Powerable, org.bukkit.block.data.Waterlogged {

    public CraftWeatheringCopperTrapDoor() {
        super();
    }

    public CraftWeatheringCopperTrapDoor(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum(net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftWeatheringCopperTrapDoor.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftWeatheringCopperTrapDoor.HALF, half);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftWeatheringCopperTrapDoor.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftWeatheringCopperTrapDoor.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftWeatheringCopperTrapDoor.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftOpenable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OPEN = getBoolean(net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock.class, "open");

    @Override
    public boolean isOpen() {
        return this.get(CraftWeatheringCopperTrapDoor.OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        this.set(CraftWeatheringCopperTrapDoor.OPEN, open);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftWeatheringCopperTrapDoor.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftWeatheringCopperTrapDoor.POWERED, powered);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftWeatheringCopperTrapDoor.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftWeatheringCopperTrapDoor.WATERLOGGED, waterlogged);
    }
}
