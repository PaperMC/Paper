package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * MaterialData for torches
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Torch extends SimpleAttachableMaterialData {
    public Torch() {
        super(Material.LEGACY_TORCH);
    }

    public Torch(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Torch(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the face that this block is attached on
     *
     * @return BlockFace attached to
     */
    @Override
    public BlockFace getAttachedFace() {
        byte data = getData();

        switch (data) {
        case 0x1:
            return BlockFace.WEST;

        case 0x2:
            return BlockFace.EAST;

        case 0x3:
            return BlockFace.NORTH;

        case 0x4:
            return BlockFace.SOUTH;

        case 0x5:
        default:
            return BlockFace.DOWN;
        }
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;

        switch (face) {
        case EAST:
            data = 0x1;
            break;

        case WEST:
            data = 0x2;
            break;

        case SOUTH:
            data = 0x3;
            break;

        case NORTH:
            data = 0x4;
            break;

        case UP:
        default:
            data = 0x5;
        }

        setData(data);
    }

    @Override
    public Torch clone() {
        return (Torch) super.clone();
    }
}
