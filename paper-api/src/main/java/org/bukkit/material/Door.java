package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a door.
 */
public class Door extends MaterialData implements Directional, Openable {
    public Door() {
        super(Material.WOODEN_DOOR);
    }

    public Door(final int type) {
        super(type);
    }

    public Door(final Material type) {
        super(type);
    }

    public Door(final int type, final byte data) {
        super(type, data);
    }

    public Door(final Material type, final byte data) {
        super(type, data);
    }

    public boolean isOpen() {
        return ((getData() & 0x4) == 0x4);
    }

    public void setOpen(boolean isOpen) {
        setData((byte) (isOpen ? (getData() | 0x4) : (getData() & ~0x4)));
    }

    /**
     * @return whether this is the top half of the door
     */
    public boolean isTopHalf() {
        return ((getData() & 0x8) == 0x8);
    }

    /**
     * Configure this part of the door to be either the top or the bottom half;
     *
     * @param isTopHalf True to make it the top half.
     */
    public void setTopHalf(boolean isTopHalf) {
        setData((byte) (isTopHalf ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    /**
     * @return the location of the hinges
     */
    public BlockFace getHingeCorner() {
        byte d = getData();

        if ((d & 0x3) == 0x3) {
            return BlockFace.NORTH_WEST;
        } else if ((d & 0x1) == 0x1) {
            return BlockFace.SOUTH_EAST;
        } else if ((d & 0x2) == 0x2) {
            return BlockFace.SOUTH_WEST;
        }

        return BlockFace.NORTH_EAST;
    }

    @Override
    public String toString() {
        return (isTopHalf() ? "TOP" : "BOTTOM") + " half of " + (isOpen() ? "an OPEN " : "a CLOSED ") + super.toString() + " with hinges " + getHingeCorner();
    }

    /**
     * Set the direction that this door should is facing.
     *
     * @param face the direction
     */
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x12);
        switch (face) {
        case EAST:
            data |= 0x1;
            break;

        case SOUTH:
            data |= 0x2;
            break;

        case WEST:
            data |= 0x3;
            break;
        }
        setData(data);
    }

    /**
     * Get the direction that this door is facing.
     *
     * @return the direction
     */
    public BlockFace getFacing() {
        byte data = (byte) (getData() & 0x3);
        switch (data) {
        case 0:
            return BlockFace.NORTH;

        case 1:
            return BlockFace.EAST;

        case 2:
            return BlockFace.SOUTH;

        case 3:
            return BlockFace.WEST;
        }
        return null; // shouldn't happen
    }

    @Override
    public Door clone() {
        return (Door) super.clone();
    }
}
