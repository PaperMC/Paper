package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the two types of coal
 * @author sunkid
 */
public enum CoalType {
    COAL((byte) 0x0),
    CHARCOAL((byte) 0x1);

    private final byte data;
    private final static Map<Byte, CoalType> types = new HashMap<Byte, CoalType>();

    private CoalType(byte data) {
        this.data = data;
    }

    /**
     * Gets the associated data value representing this type of coal
     *
     * @return A byte containing the data value of this coal type
     */
    public byte getData() {
        return data;
    }

    /**
     * Gets the type of coal with the given data value
     *
     * @param data
     *            Data value to fetch
     * @return The {@link CoalType} representing the given value, or null if
     *         it doesn't exist
     */
    public static CoalType getByData(final byte data) {
        return types.get(data);
    }

    static {
        for (CoalType type : CoalType.values()) {
            types.put(type.getData(), type);
        }
    }
}
