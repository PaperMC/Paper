package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;

/**
 * Represents the different types of coals.
 */
public class Coal extends MaterialData {
    public Coal() {
        super(Material.COAL);
    }

    public Coal(CoalType type) {
        this();
        setType(type);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public Coal(final int type) {
        super(type);
    }

    public Coal(final Material type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Coal(final int type, final byte data) {
        super(type, data);
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
