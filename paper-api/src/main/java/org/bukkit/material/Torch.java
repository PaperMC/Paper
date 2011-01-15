
package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * MaterialData for torches
 */
public class Torch extends MaterialData implements Attachable {
    public Torch(final int type) {
        super(type);
    }

    public Torch(final Material type) {
        super(type);
    }

    public Torch(final int type, final byte data) {
        super(type, data);
    }

    public Torch(final Material type, final byte data) {
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
            case 0x1:
                return BlockFace.NORTH;
            case 0x2:
                return BlockFace.SOUTH;
            case 0x3:
                return BlockFace.EAST;
            case 0x4:
                return BlockFace.WEST;
            case 0x5:
                return BlockFace.DOWN;
        }

        return null;
    }
}
