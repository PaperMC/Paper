package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * Represents Ladder data
 */
public class Ladder extends SimpleAttachableMaterialData {
    public Ladder() {
        super(Material.LADDER);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public Ladder(final int type) {
        super(type);
    }

    public Ladder(final Material type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Ladder(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Ladder(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the face that this block is attached on
     *
     * @return BlockFace attached to
     */
    public BlockFace getAttachedFace() {
        byte data = getData();

        switch (data) {
        case 0x2:
            return BlockFace.SOUTH;

        case 0x3:
            return BlockFace.NORTH;

        case 0x4:
            return BlockFace.EAST;

        case 0x5:
            return BlockFace.WEST;
        }

        return null;
    }

    /**
     * Sets the direction this ladder is facing
     */
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) 0x0;

        switch (face) {
        case SOUTH:
            data = 0x2;
            break;

        case NORTH:
            data = 0x3;
            break;

        case EAST:
            data = 0x4;
            break;

        case WEST:
            data = 0x5;
            break;
        }

        setData(data);

    }

    @Override
    public Ladder clone() {
        return (Ladder) super.clone();
    }
}
