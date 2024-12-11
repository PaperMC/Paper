/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftRedstoneWire extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.RedstoneWire, org.bukkit.block.data.AnaloguePowerable {

    public CraftRedstoneWire() {
        super();
    }

    public CraftRedstoneWire(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftRedstoneWire

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> NORTH = getEnum(net.minecraft.world.level.block.RedStoneWireBlock.class, "north");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> EAST = getEnum(net.minecraft.world.level.block.RedStoneWireBlock.class, "east");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> SOUTH = getEnum(net.minecraft.world.level.block.RedStoneWireBlock.class, "south");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> WEST = getEnum(net.minecraft.world.level.block.RedStoneWireBlock.class, "west");

    @Override
    public org.bukkit.block.data.type.RedstoneWire.Connection getFace(org.bukkit.block.BlockFace face) {
        switch (face) {
            case NORTH:
                return this.get(CraftRedstoneWire.NORTH, org.bukkit.block.data.type.RedstoneWire.Connection.class);
            case EAST:
                return this.get(CraftRedstoneWire.EAST, org.bukkit.block.data.type.RedstoneWire.Connection.class);
            case SOUTH:
                return this.get(CraftRedstoneWire.SOUTH, org.bukkit.block.data.type.RedstoneWire.Connection.class);
            case WEST:
                return this.get(CraftRedstoneWire.WEST, org.bukkit.block.data.type.RedstoneWire.Connection.class);
            default:
                throw new IllegalArgumentException("Cannot have face " + face);
        }
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, org.bukkit.block.data.type.RedstoneWire.Connection connection) {
        switch (face) {
            case NORTH:
                this.set(CraftRedstoneWire.NORTH, connection);
                break;
            case EAST:
                this.set(CraftRedstoneWire.EAST, connection);
                break;
            case SOUTH:
                this.set(CraftRedstoneWire.SOUTH, connection);
                break;
            case WEST:
                this.set(CraftRedstoneWire.WEST, connection);
                break;
            default:
                throw new IllegalArgumentException("Cannot have face " + face);
        }
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        return com.google.common.collect.ImmutableSet.of(org.bukkit.block.BlockFace.NORTH, org.bukkit.block.BlockFace.EAST, org.bukkit.block.BlockFace.SOUTH, org.bukkit.block.BlockFace.WEST);
    }

    // org.bukkit.craftbukkit.block.data.CraftAnaloguePowerable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = getInteger(net.minecraft.world.level.block.RedStoneWireBlock.class, "power");

    @Override
    public int getPower() {
        return this.get(CraftRedstoneWire.POWER);
    }

    @Override
    public void setPower(int power) {
        this.set(CraftRedstoneWire.POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(CraftRedstoneWire.POWER);
    }
}
