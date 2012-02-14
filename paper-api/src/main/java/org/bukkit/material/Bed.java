package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a bed.
 */
public class Bed extends MaterialData implements Directional {

    /**
     * Default constructor for a bed.
     */
    public Bed() {
        super(Material.BED_BLOCK);
    }

    /**
     * Instantiate a bed facing in a particular direction.
     *
     * @param direction the direction the bed's head is facing
     */
    public Bed(BlockFace direction) {
        this();
        setFacingDirection(direction);
    }

    public Bed(final int type) {
        super(type);
    }

    public Bed(final Material type) {
        super(type);
    }

    public Bed(final int type, final byte data) {
        super(type, data);
    }

    public Bed(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Determine if this block represents the head of the bed
     *
     * @return true if this is the head of the bed, false if it is the foot
     */
    public boolean isHeadOfBed() {
        return (getData() & 0x8) == 0x8;
    }

    /**
     * Configure this to be either the head or the foot of the bed
     *
     * @param isHeadOfBed True to make it the head.
     */
    public void setHeadOfBed(boolean isHeadOfBed) {
        setData((byte) (isHeadOfBed ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    /**
     * Set which direction the head of the bed is facing. Note that this will
     * only affect one of the two blocks the bed is made of.
     */
    public void setFacingDirection(BlockFace face) {
        byte data;

        switch (face) {
        case WEST:
            data = 0x0;
            break;

        case NORTH:
            data = 0x1;
            break;

        case EAST:
            data = 0x2;
            break;

        case SOUTH:
        default:
            data = 0x3;
        }

        if (isHeadOfBed()) {
            data |= 0x8;
        }

        setData(data);
    }

    /**
     * Get the direction that this bed's head is facing toward
     *
     * @return the direction the head of the bed is facing
     */
    public BlockFace getFacing() {
        byte data = (byte) (getData() & 0x7);

        switch (data) {
        case 0x0:
            return BlockFace.WEST;

        case 0x1:
            return BlockFace.NORTH;

        case 0x2:
            return BlockFace.EAST;

        case 0x3:
        default:
            return BlockFace.SOUTH;
        }
    }

    @Override
    public String toString() {
        return (isHeadOfBed() ? "HEAD" : "FOOT") + " of " + super.toString() + " facing " + getFacing();
    }

    @Override
    public Bed clone() {
        return (Bed) super.clone();
    }
}
