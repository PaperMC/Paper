
package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * MaterialData for signs
 */
public class Sign extends MaterialData implements Attachable {
    public Sign(final int type) {
        super(type);
    }

    public Sign(final Material type) {
        super(type);
    }

    public Sign(final int type, final byte data) {
        super(type, data);
    }

    public Sign(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the face that this block is attached on
     *
     * @return BlockFace attached to
     */
    public BlockFace getAttachedFace() {
        if (getItemType() == Material.WALL_SIGN) {
            byte data = getData();

            switch (data) {
                case 0x2:
                    return BlockFace.WEST;
                case 0x3:
                    return BlockFace.EAST;
                case 0x4:
                    return BlockFace.SOUTH;
                case 0x5:
                    return BlockFace.NORTH;
            }

            return null;
        } else {
            return BlockFace.DOWN;
        }
    }

    /**
     * Gets the direction that this sign is currently facing
     *
     * @return BlockFace indicating where this sign is facing
     */
    public BlockFace getFacing() {
        byte data = getData();

        if (getAttachedFace() == BlockFace.DOWN) {
            switch (data) {
                case 0x0:
                case 0x1:
                    return BlockFace.WEST;
                case 0x2:
                case 0x3:
                    return BlockFace.NORTH_WEST;
                case 0x4:
                case 0x5:
                    return BlockFace.NORTH;
                case 0x6:
                case 0x7:
                    return BlockFace.NORTH_EAST;
                case 0x8:
                case 0x9:
                    return BlockFace.EAST;
                case 0xA:
                case 0xB:
                    return BlockFace.SOUTH_EAST;
                case 0xC:
                case 0xD:
                    return BlockFace.SOUTH;
                case 0xE:
                case 0xF:
                    return BlockFace.SOUTH_WEST;
            }

            return null;
        } else {
            switch (data) {
                case 0x2:
                    return BlockFace.EAST;
                case 0x3:
                    return BlockFace.WEST;
                case 0x4:
                    return BlockFace.NORTH;
                case 0x5:
                    return BlockFace.SOUTH;
            }
        }

        return null;
    }
}
