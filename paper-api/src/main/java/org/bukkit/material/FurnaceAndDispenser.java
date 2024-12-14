package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a furnace or dispenser, two types of directional containers
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 * @since 1.0.0 R1
 */
@Deprecated(since = "1.13", forRemoval = true)
public class FurnaceAndDispenser extends DirectionalContainer {

    public FurnaceAndDispenser(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public FurnaceAndDispenser(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * @since 1.1.0 R5
     */
    @Override
    public FurnaceAndDispenser clone() {
        return (FurnaceAndDispenser) super.clone();
    }
}
