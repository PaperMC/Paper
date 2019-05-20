package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a command block
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Command extends MaterialData implements Redstone {
    public Command() {
        super(Material.LEGACY_COMMAND);
    }

    public Command(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Command(final Material type, final byte data) {
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
        return (getData() & 1) != 0;
    }

    /**
     * Sets the current state of this Material
     *
     * @param bool
     *            whether or not the command block is powered
     */
    public void setPowered(boolean bool) {
        setData((byte) (bool ? (getData() | 1) : (getData() & -2)));
    }

    @Override
    public String toString() {
        return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
    }

    @Override
    public Command clone() {
        return (Command) super.clone();
    }
}
