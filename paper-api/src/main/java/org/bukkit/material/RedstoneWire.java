package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents redstone wire
 */
public class RedstoneWire extends MaterialData implements Redstone {
    public RedstoneWire() {
        super(Material.REDSTONE_WIRE);
    }

    public RedstoneWire(final int type) {
        super(type);
    }

    public RedstoneWire(final Material type) {
        super(type);
    }

    public RedstoneWire(final int type, final byte data) {
        super(type, data);
    }

    public RedstoneWire(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current state of this Material, indicating if it's powered or
     * unpowered
     *
     * @return true if powered, otherwise false
     */
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
