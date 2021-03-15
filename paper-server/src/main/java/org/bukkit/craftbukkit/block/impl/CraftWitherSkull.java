/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWitherSkull extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Rotatable {

    public CraftWitherSkull() {
        super();
    }

    public CraftWitherSkull(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftRotatable

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger ROTATION = getInteger(net.minecraft.world.level.block.BlockWitherSkull.class, "rotation");

    @Override
    public org.bukkit.block.BlockFace getRotation() {
        int data = get(ROTATION);
        switch (data) {
            case 0x0:
                return org.bukkit.block.BlockFace.SOUTH;
            case 0x1:
                return org.bukkit.block.BlockFace.SOUTH_SOUTH_WEST;
            case 0x2:
                return org.bukkit.block.BlockFace.SOUTH_WEST;
            case 0x3:
                return org.bukkit.block.BlockFace.WEST_SOUTH_WEST;
            case 0x4:
                return org.bukkit.block.BlockFace.WEST;
            case 0x5:
                return org.bukkit.block.BlockFace.WEST_NORTH_WEST;
            case 0x6:
                return org.bukkit.block.BlockFace.NORTH_WEST;
            case 0x7:
                return org.bukkit.block.BlockFace.NORTH_NORTH_WEST;
            case 0x8:
                return org.bukkit.block.BlockFace.NORTH;
            case 0x9:
                return org.bukkit.block.BlockFace.NORTH_NORTH_EAST;
            case 0xA:
                return org.bukkit.block.BlockFace.NORTH_EAST;
            case 0xB:
                return org.bukkit.block.BlockFace.EAST_NORTH_EAST;
            case 0xC:
                return org.bukkit.block.BlockFace.EAST;
            case 0xD:
                return org.bukkit.block.BlockFace.EAST_SOUTH_EAST;
            case 0xE:
                return org.bukkit.block.BlockFace.SOUTH_EAST;
            case 0xF:
                return org.bukkit.block.BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalArgumentException("Unknown rotation " + data);
        }
    }

    @Override
    public void setRotation(org.bukkit.block.BlockFace rotation) {
        int val;
        switch (rotation) {
            case SOUTH:
                val = 0x0;
                break;
            case SOUTH_SOUTH_WEST:
                val = 0x1;
                break;
            case SOUTH_WEST:
                val = 0x2;
                break;
            case WEST_SOUTH_WEST:
                val = 0x3;
                break;
            case WEST:
                val = 0x4;
                break;
            case WEST_NORTH_WEST:
                val = 0x5;
                break;
            case NORTH_WEST:
                val = 0x6;
                break;
            case NORTH_NORTH_WEST:
                val = 0x7;
                break;
            case NORTH:
                val = 0x8;
                break;
            case NORTH_NORTH_EAST:
                val = 0x9;
                break;
            case NORTH_EAST:
                val = 0xA;
                break;
            case EAST_NORTH_EAST:
                val = 0xB;
                break;
            case EAST:
                val = 0xC;
                break;
            case EAST_SOUTH_EAST:
                val = 0xD;
                break;
            case SOUTH_EAST:
                val = 0xE;
                break;
            case SOUTH_SOUTH_EAST:
                val = 0xF;
                break;
            default:
                throw new IllegalArgumentException("Illegal rotation " + rotation);
        }
        set(ROTATION, val);
    }
}
