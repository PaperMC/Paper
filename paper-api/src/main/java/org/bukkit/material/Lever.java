package org.bukkit.material;

import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * Represents a lever
 */
public class Lever extends SimpleAttachableMaterialData implements Redstone {
    public Lever() {
        super(Material.LEVER);
    }

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
     * Set this lever to be powered or not.
     *
     * @param isPowered whether the lever should be powered or not
     */
    public void setPowered(boolean isPowered) {
        setData((byte) (isPowered ? (getData() | 0x8) : (getData() & ~0x8)));
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
        
        case 0x0:
        case 0x7:
            return BlockFace.UP;
            
        }

        return null;
    }

    /**
     * Sets the direction this lever is pointing in
     */
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x8);
        BlockFace attach = getAttachedFace();
        
        if (attach == BlockFace.DOWN) {
            switch (face) {
            case WEST:
            case EAST:
                data |= 0x5;
                break;

            case SOUTH:
            case NORTH:
                data |= 0x6;
                break;
            }
        } else if (attach == BlockFace.UP) {
            switch (face) {
            case WEST:
            case EAST:
                data |= 0x7;
                break;

            case SOUTH:
            case NORTH:
                data |= 0x0;
                break;
            }
        } else {
            switch (face) {
            case SOUTH:
                data |= 0x1;
                break;

            case NORTH:
                data |= 0x2;
                break;

            case WEST:
                data |= 0x3;
                break;

            case EAST:
                data |= 0x4;
                break;
            }
        }
        setData(data);
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public Lever clone() {
        return (Lever) super.clone();
    }
}
