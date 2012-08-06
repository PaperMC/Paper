package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a furnace or dispenser, two types of directional containers
 */
public class FurnaceAndDispenser extends DirectionalContainer {
    public FurnaceAndDispenser(final int type) {
        super(type);
    }

    public FurnaceAndDispenser(final Material type) {
        super(type);
    }

    public FurnaceAndDispenser(final int type, final byte data) {
        super(type, data);
    }

    public FurnaceAndDispenser(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public FurnaceAndDispenser clone() {
        return (FurnaceAndDispenser) super.clone();
    }
}
