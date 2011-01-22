package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * Represents a lever
 */
public class Lever extends MaterialData implements Redstone, Attachable {
    public Lever(final int type) {
        super(type);
    }

    public Lever(final Material type) {
        super(type);
    }

    public Lever(final int type, final byte data) {
        super(type, data);
    }

    public Lever(final Material type, final byte data) {
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
        byte data = (byte) (getData() & 0x7);

        switch (data) {
            case 0x1:
                return BlockFace.NORTH;
            case 0x2:
                return BlockFace.SOUTH;
            case 0x3:
                return BlockFace.EAST;
            case 0x4:
                return BlockFace.WEST;
            case 0x5:
            case 0x6:
                return BlockFace.DOWN;
        }

        return null;
    }
}
