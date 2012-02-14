package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * This is the superclass for the {@link DetectorRail} and {@link PoweredRail} classes
 */
public class ExtendedRails extends Rails {
    public ExtendedRails(final int type) {
        super(type);
    }

    public ExtendedRails(final Material type) {
        super(type);
    }

    public ExtendedRails(final int type, final byte data) {
        super(type, data);
    }

    public ExtendedRails(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public boolean isCurve() {
        return false;
    }

    @Override
    protected byte getConvertedData() {
        return (byte) (getData() & 0x7);
    }

    @Override
    public void setDirection(BlockFace face, boolean isOnSlope) {
        boolean extraBitSet = (getData() & 0x8) == 0x8;

        if (face != BlockFace.NORTH && face != BlockFace.SOUTH && face != BlockFace.EAST && face != BlockFace.WEST) {
            throw new IllegalArgumentException("Detector rails and powered rails cannot be set on a curve!");
        }

        super.setDirection(face, isOnSlope);
        setData((byte) (extraBitSet ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    @Override
    public ExtendedRails clone() {
        return (ExtendedRails) super.clone();
    }
}
