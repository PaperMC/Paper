package org.bukkit.material;

import org.bukkit.Material;

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
}
