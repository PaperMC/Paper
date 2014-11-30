package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents the cocoa plant
 */
public class CocoaPlant extends MaterialData implements Directional, Attachable {

    public enum CocoaPlantSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    public CocoaPlant() {
        super(Material.COCOA);
    }

    /**
     * @param type the raw type id     
     * @deprecated Magic value
     */
    @Deprecated
    public CocoaPlant(final int type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public CocoaPlant(final int type, final byte data) {
        super(type, data);
    }

    public CocoaPlant(CocoaPlantSize sz) {
        this();
        setSize(sz);
    }

    public CocoaPlant(CocoaPlantSize sz, BlockFace dir) {
        this();
        setSize(sz);
        setFacingDirection(dir);
    }

    /**
     * Get size of plant
     *
     * @return size
     */
    public CocoaPlantSize getSize() {
        switch (getData() & 0xC) {
            case 0:
                return CocoaPlantSize.SMALL;
            case 4:
                return CocoaPlantSize.MEDIUM;
            default:
                return CocoaPlantSize.LARGE;
        }
    }

    /**
     * Set size of plant
     *
     * @param sz - size of plant
     */
    public void setSize(CocoaPlantSize sz) {
        int dat = getData() & 0x3;
        switch (sz) {
            case SMALL:
                break;
            case MEDIUM:
                dat |= 0x4;
                break;
            case LARGE:
                dat |= 0x8;
                break;
        }
        setData((byte) dat);
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        int dat = getData() & 0xC;
        switch (face) {
            default:
            case SOUTH:
                break;
            case WEST:
                dat |= 0x1;
                break;
            case NORTH:
                dat |= 0x2;
                break;
            case EAST:
                dat |= 0x3;
                break;
        }
        setData((byte) dat);
    }

    public BlockFace getFacing() {
        switch (getData() & 0x3) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
        }
        return null;
    }

    @Override
    public CocoaPlant clone() {
        return (CocoaPlant) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing() + " " + getSize();
    }
}
