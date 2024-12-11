/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTrapdoor extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TrapDoor, org.bukkit.block.data.Bisected, org.bukkit.block.data.Directional, org.bukkit.block.data.Openable, org.bukkit.block.data.Powerable, org.bukkit.block.data.Waterlogged {

    public CraftTrapdoor() {
        super();
    }

    public CraftTrapdoor(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum(net.minecraft.world.level.block.TrapDoorBlock.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftTrapdoor.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftTrapdoor.HALF, half);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.TrapDoorBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftTrapdoor.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftTrapdoor.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftTrapdoor.FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftOpenable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OPEN = getBoolean(net.minecraft.world.level.block.TrapDoorBlock.class, "open");

    @Override
    public boolean isOpen() {
        return this.get(CraftTrapdoor.OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        this.set(CraftTrapdoor.OPEN, open);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.TrapDoorBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftTrapdoor.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftTrapdoor.POWERED, powered);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.TrapDoorBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return this.get(CraftTrapdoor.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.set(CraftTrapdoor.WATERLOGGED, waterlogged);
    }
}
