
package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {
    /**
     * Represents white dye
     */
    WHITE((byte) 0x0),
    /**
     * Represents orange dye
     */
    ORANGE((byte) 0x1),
    /**
     * Represents magenta dye
     */
    MAGENTA((byte) 0x2),
    /**
     * Represents light blue dye
     */
    LIGHT_BLUE((byte) 0x3),
    /**
     * Represents yellow dye
     */
    YELLOW((byte) 0x4),
    /**
     * Represents lime dye
     */
    LIME((byte) 0x5),
    /**
     * Represents pink dye
     */
    PINK((byte) 0x6),
    /**
     * Represents gray dye
     */
    GRAY((byte) 0x7),
    /**
     * Represents silver dye
     */
    SILVER((byte) 0x8),
    /**
     * Represents cyan dye
     */
    CYAN((byte) 0x9),
    /**
     * Represents purple dye
     */
    PURPLE((byte) 0xA),
    /**
     * Represents blue dye
     */
    BLUE((byte) 0xB),
    /**
     * Represents brown dye
     */
    BROWN((byte) 0xC),
    /**
     * Represents green dye
     */
    GREEN((byte) 0xD),
    /**
     * Represents red dye
     */
    RED((byte) 0xE),
    /**
     * Represents black dye
     */
    BLACK((byte) 0xF);

    private final byte data;
    private final static Map<Byte, DyeColor> colors = new HashMap<Byte, DyeColor>();

    private DyeColor(final byte data) {
        this.data = data;
    }

    /**
     * Gets the associated data value representing this color
     *
     * @return A byte containing the data value of this color
     */
    public byte getData() {
        return data;
    }

    /**
     * Gets the DyeColor with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link DyeColor} representing the given value, or null if it doesn't exist
     */
    public static DyeColor getByData(final byte data) {
        return colors.get(data);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            colors.put(color.getData(), color);
        }
    }
}
