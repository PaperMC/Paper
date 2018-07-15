package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a furnace or dispenser, two types of directional containers
 */
public class FurnaceAndDispenser extends DirectionalContainer {

    public FurnaceAndDispenser(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public FurnaceAndDispenser(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public FurnaceAndDispenser clone() {
        return (FurnaceAndDispenser) super.clone();
    }
}
