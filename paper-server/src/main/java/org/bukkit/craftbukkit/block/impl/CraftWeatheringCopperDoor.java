/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWeatheringCopperDoor extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Door, org.bukkit.block.data.Bisected, org.bukkit.block.data.Directional, org.bukkit.block.data.Openable, org.bukkit.block.data.Powerable {

    public CraftWeatheringCopperDoor() {
        super();
    }

    public CraftWeatheringCopperDoor(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftDoor

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HINGE = getEnum(net.minecraft.world.level.block.WeatheringCopperDoorBlock.class, "hinge");

    @Override
    public org.bukkit.block.data.type.Door.Hinge getHinge() {
        return this.get(CraftWeatheringCopperDoor.HINGE, org.bukkit.block.data.type.Door.Hinge.class);
    }

    @Override
    public void setHinge(org.bukkit.block.data.type.Door.Hinge hinge) {
        this.set(CraftWeatheringCopperDoor.HINGE, hinge);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum(net.minecraft.world.level.block.WeatheringCopperDoorBlock.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftWeatheringCopperDoor.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftWeatheringCopperDoor.HALF, half);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.WeatheringCopperDoorBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftWeatheringCopperDoor.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftWeatheringCopperDoor.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftWeatheringCopperDoor.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftOpenable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OPEN = getBoolean(net.minecraft.world.level.block.WeatheringCopperDoorBlock.class, "open");

    @Override
    public boolean isOpen() {
        return this.get(CraftWeatheringCopperDoor.OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        this.set(CraftWeatheringCopperDoor.OPEN, open);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.WeatheringCopperDoorBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftWeatheringCopperDoor.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftWeatheringCopperDoor.POWERED, powered);
    }
}
