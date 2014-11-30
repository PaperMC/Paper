package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Material data for the piston extension block
 */
public class PistonExtensionMaterial extends MaterialData implements Attachable {
    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public PistonExtensionMaterial(final int type) {
        super(type);
    }

    public PistonExtensionMaterial(final Material type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public PistonExtensionMaterial(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public PistonExtensionMaterial(final Material type, final byte data) {
        super(type, data);
    }

    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x8);

        switch (face) {
        case UP:
            data |= 1;
            break;
        case NORTH:
            data |= 2;
            break;
        case SOUTH:
            data |= 3;
            break;
        case WEST:
            data |= 4;
            break;
        case EAST:
            data |= 5;
            break;
        }
        setData(data);
    }

    public BlockFace getFacing() {
        byte dir = (byte) (getData() & 7);

        switch (dir) {
        case 0:
            return BlockFace.DOWN;
        case 1:
            return BlockFace.UP;
        case 2:
            return BlockFace.NORTH;
        case 3:
            return BlockFace.SOUTH;
        case 4:
            return BlockFace.WEST;
        case 5:
            return BlockFace.EAST;
        default:
            return BlockFace.SELF;
        }
    }

    /**
     * Checks if this piston extension is sticky, and returns true if so
     *
     * @return true if this piston is "sticky", or false
     */
    public boolean isSticky() {
        return (getData() & 8) == 8;
    }

    /**
     * Sets whether or not this extension is sticky
     *
     * @param sticky true if sticky, otherwise false
     */
    public void setSticky(boolean sticky) {
        setData((byte) (sticky ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    @Override
    public PistonExtensionMaterial clone() {
        return (PistonExtensionMaterial) super.clone();
    }
}
