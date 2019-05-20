package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a dispenser.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Dispenser extends FurnaceAndDispenser {

    public Dispenser() {
        super(Material.LEGACY_DISPENSER);
    }

    public Dispenser(BlockFace direction) {
        this();
        setFacingDirection(direction);
    }

    public Dispenser(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Dispenser(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;

        switch (face) {
            case DOWN:
                data = 0x0;
                break;

            case UP:
                data = 0x1;
                break;

            case NORTH:
                data = 0x2;
                break;

            case SOUTH:
                data = 0x3;
                break;

            case WEST:
                data = 0x4;
                break;

            case EAST:
            default:
                data = 0x5;
        }

        setData(data);
    }

    @Override
    public BlockFace getFacing() {
        int data = getData() & 0x7;

        switch (data) {
            case 0x0:
                return BlockFace.DOWN;

            case 0x1:
                return BlockFace.UP;

            case 0x2:
                return BlockFace.NORTH;

            case 0x3:
                return BlockFace.SOUTH;

            case 0x4:
                return BlockFace.WEST;

            case 0x5:
            default:
                return BlockFace.EAST;
        }
    }

    @Override
    public Dispenser clone() {
        return (Dispenser) super.clone();
    }
}
