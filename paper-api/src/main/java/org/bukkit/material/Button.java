
package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * Represents a button
 */
public class Button extends MaterialData implements Redstone, Attachable {
    public Button(final int type) {
        super(type);
    }

    public Button(final Material type) {
        super(type);
    }

    public Button(final int type, final byte data) {
        super(type, data);
    }

    public Button(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current state of this Material, indicating if it's powered or
     * unpowered
     *
     * @return true if powered, otherwise false
     */
    public boolean isPowered() {
        return (getData() & 0x8) == 0x8;
    }

    /**
     * Gets the face that this block is attached on
     *
     * @return BlockFace attached to
     */
    public BlockFace getAttachedFace() {
        byte data = (byte) (getData() ^ 0x8);

        switch (data) {
            case 0x1:
                return BlockFace.NORTH;
            case 0x2:
                return BlockFace.SOUTH;
            case 0x3:
                return BlockFace.EAST;
            case 0x4:
                return BlockFace.WEST;
        }

        return null;
    }
}
