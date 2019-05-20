package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents the tripwire
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Tripwire extends MaterialData {

    public Tripwire() {
        super(Material.LEGACY_TRIPWIRE);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Tripwire(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Test if tripwire is currently activated
     *
     * @return true if activated, false if not
     */
    public boolean isActivated() {
        return (getData() & 0x4) != 0;
    }

    /**
     * Set tripwire activated state
     *
     * @param act - true if activated, false if not
     */
    public void setActivated(boolean act) {
        int dat = getData() & (0x8 | 0x3);
        if (act) {
            dat |= 0x4;
        }
        setData((byte) dat);
    }

    /**
     * Test if object triggering this tripwire directly
     *
     * @return true if object activating tripwire, false if not
     */
    public boolean isObjectTriggering() {
        return (getData() & 0x1) != 0;
    }

    /**
     * Set object triggering state for this tripwire
     *
     * @param trig - true if object activating tripwire, false if not
     */
    public void setObjectTriggering(boolean trig) {
        int dat = getData() & 0xE;
        if (trig) {
            dat |= 0x1;
        }
        setData((byte) dat);
    }

    @Override
    public Tripwire clone() {
        return (Tripwire) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + (isActivated() ? " Activated" : "") + (isObjectTriggering() ? " Triggered" : "");
    }
}
