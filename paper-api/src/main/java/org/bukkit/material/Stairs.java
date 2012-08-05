package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents stairs.
 */
public class Stairs extends MaterialData implements Directional {

    public Stairs(final int type) {
        super(type);
    }

    public Stairs(final Material type) {
        super(type);
    }

    public Stairs(final int type, final byte data) {
        super(type, data);
    }

    public Stairs(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * @return the direction the stairs ascend towards
     */
    public BlockFace getAscendingDirection() {
        byte data = getData();

        switch (data & 0x3) {
        case 0x0:
        default:
            return BlockFace.SOUTH;

        case 0x1:
            return BlockFace.NORTH;

        case 0x2:
            return BlockFace.WEST;

        case 0x3:
            return BlockFace.EAST;
        }
    }

    /**
     * @return the direction the stairs descend towards
     */
    public BlockFace getDescendingDirection() {
        return getAscendingDirection().getOppositeFace();
    }

    /**
     * Set the direction the stair part of the block is facing
     */
    public void setFacingDirection(BlockFace face) {
        byte data;

        switch (face) {
        case NORTH:
        default:
            data = 0x0;
            break;

        case SOUTH:
            data = 0x1;
            break;

        case EAST:
            data = 0x2;
            break;

        case WEST:
            data = 0x3;
            break;
        }

        setData((byte) ((getData() & 0xC) | data));
    }

    /**
     * @return the direction the stair part of the block is facing
     */
    public BlockFace getFacing() {
        return getDescendingDirection();
    }

    /**
     * Test if step is inverted
     * @return true if inverted (top half), false if normal (bottom half)
     */
    public boolean isInverted() {
        return ((getData() & 0x4) != 0);
    }
    
    /**
     * Set step inverted state
     * @param inv - true if step is inverted (top half), false if step is normal (bottom half)
     */
    public void setInverted(boolean inv) {
        int dat = getData() & 0x3;
        if (inv) {
            dat |= 0x4;
        }
        setData((byte) dat);
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing() + (isInverted()?" inverted":"");
    }

    @Override
    public Stairs clone() {
        return (Stairs) super.clone();
    }
}
