package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a cauldron
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Cauldron extends MaterialData {
    private static final int CAULDRON_FULL = 3;
    private static final int CAULDRON_EMPTY = 0;

    public Cauldron() {
        super(Material.LEGACY_CAULDRON);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Cauldron(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Cauldron(byte data) {
        super(Material.LEGACY_CAULDRON, data);
    }

    /**
     * Check if the cauldron is full.
     *
     * @return True if it is full.
     */
    public boolean isFull() {
        return getData() >= CAULDRON_FULL;
    }

    /**
     * Check if the cauldron is empty.
     *
     * @return True if it is empty.
     */
    public boolean isEmpty() {
        return getData() <= CAULDRON_EMPTY;
    }

    @Override
    public String toString() {
        return (isEmpty() ? "EMPTY" : (isFull() ? "FULL" : getData() + "/3 FULL")) + " CAULDRON";
    }

    @Override
    public Cauldron clone() {
        return (Cauldron) super.clone();
    }
}
