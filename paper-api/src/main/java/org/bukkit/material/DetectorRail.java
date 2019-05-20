package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a detector rail
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class DetectorRail extends ExtendedRails implements PressureSensor {
    public DetectorRail() {
        super(Material.LEGACY_DETECTOR_RAIL);
    }

    public DetectorRail(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public DetectorRail(final Material type, final byte data) {
        super(type, data);
    }

    @Override
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
