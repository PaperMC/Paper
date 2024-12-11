package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftRedstoneWire extends CraftBlockData implements RedstoneWire {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> NORTH = getEnum("north");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> EAST = getEnum("east");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> SOUTH = getEnum("south");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> WEST = getEnum("west");

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
}
