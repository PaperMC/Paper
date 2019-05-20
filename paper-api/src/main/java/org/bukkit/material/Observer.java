package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents an observer.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Observer extends MaterialData implements Directional, Redstone {

    public Observer() {
        super(Material.LEGACY_OBSERVER);
    }

    public Observer(BlockFace direction) {
        this();
        setFacingDirection(direction);
    }

    public Observer(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Observer(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public boolean isPowered() {
        return (getData() & 0x8) == 0x8;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x8);

        switch (face) {
            case DOWN:
                data |= 0x0;
                break;
            case UP:
                data |= 0x1;
                break;
            case SOUTH:
                data |= 0x2;
                break;
            case NORTH:
                data |= 0x3;
                break;
            case EAST:
                data |= 0x4;
                break;
            case WEST:
                data |= 0x5;
                break;
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
                return BlockFace.SOUTH;
            case 0x3:
                return BlockFace.NORTH;
            case 0x4:
                return BlockFace.EAST;
            case 0x5:
                return BlockFace.WEST;
            default:
                throw new IllegalArgumentException("Illegal facing direction " + data);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing();
    }

    @Override
    public Observer clone() {
        return (Observer) super.clone();
    }
}
