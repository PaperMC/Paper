package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * Represents a button
 */
public class Button extends SimpleAttachableMaterialData implements Redstone {
    public Button() {
        super(Material.STONE_BUTTON);
    }

    /**
     * @param type the type
     * @deprecated Magic value
     */
    @Deprecated
    public Button(final int type) {
        super(type);
    }

    public Button(final Material type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Button(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
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
     * Sets the current state of this button
     *
     * @param bool
     *            whether or not the button is powered
     */
    public void setPowered(boolean bool) {
        setData((byte) (bool ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    /**
     * Gets the face that this block is attached on
     *
     * @return BlockFace attached to
     */
    public BlockFace getAttachedFace() {
        byte data = (byte) (getData() & 0x7);

        switch (data) {
        case 0x0:
            return BlockFace.UP;

        case 0x1:
            return BlockFace.WEST;

        case 0x2:
            return BlockFace.EAST;

        case 0x3:
            return BlockFace.NORTH;

        case 0x4:
            return BlockFace.SOUTH;

        case 0x5:
            return BlockFace.DOWN;
        }

        return null;
    }

    /**
     * Sets the direction this button is pointing toward
     */
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x8);

        switch (face) {
        case DOWN:
            data |= 0x0;
            break;

        case EAST:
            data |= 0x1;
            break;

        case WEST:
            data |= 0x2;
            break;

        case SOUTH:
            data |= 0x3;
            break;

        case NORTH:
            data |= 0x4;
            break;

        case UP:
            data |= 0x5;
            break;
        }

        setData(data);
    }

    @Override
    public String toString() {
        return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public Button clone() {
        return (Button) super.clone();
    }
}
