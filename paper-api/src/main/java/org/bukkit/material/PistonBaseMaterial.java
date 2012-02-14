package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Material data for the piston base block
 */
public class PistonBaseMaterial extends MaterialData implements Directional, Redstone {
    public PistonBaseMaterial(final int type) {
        super(type);
    }

    public PistonBaseMaterial(final Material type) {
        super(type);
    }

    public PistonBaseMaterial(final int type, final byte data) {
        super(type, data);
    }

    public PistonBaseMaterial(final Material type, final byte data) {
        super(type, data);
    }

    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x8);

        switch (face) {
        case UP:
            data |= 1;
            break;
        case EAST:
            data |= 2;
            break;
        case WEST:
            data |= 3;
            break;
        case NORTH:
            data |= 4;
            break;
        case SOUTH:
            data |= 5;
            break;
        }
        setData(data);
    }

    public BlockFace getFacing() {
        byte dir = (byte) (getData() & 7);

        switch (dir) {
        case 0:
            return BlockFace.DOWN;
        case 1:
            return BlockFace.UP;
        case 2:
            return BlockFace.EAST;
        case 3:
            return BlockFace.WEST;
        case 4:
            return BlockFace.NORTH;
        case 5:
            return BlockFace.SOUTH;
        default:
            return BlockFace.SELF;
        }
    }

    public boolean isPowered() {
        return (getData() & 0x8) == 0x8;
    }

    /**
     * Sets the current state of this piston
     *
     * @param powered true if the piston is extended & powered, or false
     */
    public void setPowered(boolean powered) {
        setData((byte) (powered ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    /**
     * Checks if this piston base is sticky, and returns true if so
     *
     * @return true if this piston is "sticky", or false
     */
    public boolean isSticky() {
        return this.getItemType() == Material.PISTON_STICKY_BASE;
    }

    @Override
    public PistonBaseMaterial clone() {
        return (PistonBaseMaterial) super.clone();
    }
}
