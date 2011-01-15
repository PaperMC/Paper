
package org.bukkit.material;

import org.bukkit.BlockFace;
import org.bukkit.Material;

/**
 * Represents Ladder data
 */
public class Ladder extends MaterialData implements Attachable {
    public Ladder(final int type) {
        super(type);
    }

    public Ladder(final Material type) {
        super(type);
    }

    public Ladder(final int type, final byte data) {
        super(type, data);
    }

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
                return BlockFace.WEST;
            case 0x3:
                return BlockFace.EAST;
            case 0x4:
                return BlockFace.SOUTH;
            case 0x5:
                return BlockFace.NORTH;
        }

        return null;
    }
}