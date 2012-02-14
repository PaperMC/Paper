package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a detector rail
 */
public class DetectorRail extends ExtendedRails implements PressureSensor {
    public DetectorRail() {
        super(Material.DETECTOR_RAIL);
    }

    public DetectorRail(final int type) {
        super(type);
    }

    public DetectorRail(final Material type) {
        super(type);
    }

    public DetectorRail(final int type, final byte data) {
        super(type, data);
    }

    public DetectorRail(final Material type, final byte data) {
        super(type, data);
    }

    public boolean isPressed() {
        return (getData() & 0x8) == 0x8;
    }

    public void setPressed(boolean isPressed) {
        setData((byte) (isPressed ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    @Override
    public DetectorRail clone() {
        return (DetectorRail) super.clone();
    }
}
