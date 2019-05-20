package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;

/**
 * Represents the different types of coals.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Coal extends MaterialData {
    public Coal() {
        super(Material.LEGACY_COAL);
    }

    public Coal(CoalType type) {
        this();
        setType(type);
    }

    public Coal(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Coal(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current type of this coal
     *
     * @return CoalType of this coal
     */
    public CoalType getType() {
        return CoalType.getByData(getData());
    }

    /**
     * Sets the type of this coal
     *
     * @param type New type of this coal
     */
    public void setType(CoalType type) {
        setData(type.getData());
    }

    @Override
    public String toString() {
        return getType() + " " + super.toString();
    }

    @Override
    public Coal clone() {
        return (Coal) super.clone();
    }
}
