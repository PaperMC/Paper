package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents redstone wire
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class RedstoneWire extends MaterialData implements Redstone {
    public RedstoneWire() {
        super(Material.LEGACY_REDSTONE_WIRE);
    }

    public RedstoneWire(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public RedstoneWire(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current state of this Material, indicating if it's powered or
     * unpowered
     *
     * @return true if powered, otherwise false
     */
    @Override
    public boolean isPowered() {
        return getData() > 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public RedstoneWire clone() {
        return (RedstoneWire) super.clone();
    }
}
