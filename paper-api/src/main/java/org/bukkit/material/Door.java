package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a door.
 *
 * This class was previously deprecated, but has been retrofitted to
 * work with modern doors. Some methods are undefined dependant on <code>isTopHalf()</code>
 * due to Minecraft's internal representation of doors.
 */
public class Door extends MaterialData implements Directional, Openable {

    // This class breaks API contracts on Directional and Openable because
    // of the way doors are currently implemented. Beware!

    /**
     * @deprecated Artifact of old API, equivalent to new <code>Door(Material.WOODEN_DOOR);</code>
     */
    @Deprecated
    public Door() {
        super(Material.WOODEN_DOOR);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public Door(final int type) {
        super(type);
    }

    public Door(final Material type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Door(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Door(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Result is undefined if <code>isTopHalf()</code> is true.
     */
    public boolean isOpen() {
        return ((getData() & 0x4) == 0x4);
    }

    /**
     * Set whether the door is open. Undefined if <code>isTopHalf()</code> is true.
     */
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
     * Configure this part of the door to be either the top or the bottom half
     *
     * @param isTopHalf True to make it the top half.
     */
    public void setTopHalf(boolean isTopHalf) {
        setData((byte) (isTopHalf ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    /**
     * @return BlockFace.SELF
     * @deprecated This method should not be used; use hinge and facing accessors instead.
     */
    @Deprecated
    public BlockFace getHingeCorner() {
        return BlockFace.SELF;
    }

    @Override
    public String toString() {
        return (isTopHalf() ? "TOP" : "BOTTOM") + " half of " + super.toString();
    }

    /**
     * Set the direction that this door should is facing.
     *
     * Undefined if <code>isTopHalf()</code> is true.
     *
     * @param face the direction
     */
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x12);
        switch (face) {
        case WEST:
            data |= 0x0;
            break;

        case NORTH:
            data |= 0x1;
            break;

        case EAST:
            data |= 0x2;
            break;

        case SOUTH:
            data |= 0x3;
            break;
        }
        setData(data);
    }

    /**
     * Get the direction that this door is facing.
     *
     * Undefined if <code>isTopHalf()</code> is true.
     *
     * @return the direction
     */
    public BlockFace getFacing() {
        byte data = (byte) (getData() & 0x3);
        switch (data) {
        case 0:
            return BlockFace.WEST;

        case 1:
            return BlockFace.NORTH;

        case 2:
            return BlockFace.EAST;

        case 3:
            return BlockFace.SOUTH;
        }
        return null; // shouldn't happen
    }

    /**
     * Returns the side of the door the hinge is on.
     *
     * Undefined if <code>isTopHalf()</code> is false.
     *
     * @return false for left hinge, true for right hinge
     */
    public boolean getHinge() {
        return (getData() & 0x1) == 1;
    }

    /**
     * Set whether the hinge is on the left or right side. Left is false, right is true.
     *
     * Undefined if <code>isTopHalf()</code> is false.
     */
    public void setHinge(boolean hinge) {
        setData((byte) (hinge ? (getData() | 0x1) : (getData() & ~0x1)));
    }

    @Override
    public Door clone() {
        return (Door) super.clone();
    }
}
