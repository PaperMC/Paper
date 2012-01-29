package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {

    /**
     * Represents white dye
     */
    WHITE(0x0),
    /**
     * Represents orange dye
     */
    ORANGE(0x1),
    /**
     * Represents magenta dye
     */
    MAGENTA(0x2),
    /**
     * Represents light blue dye
     */
    LIGHT_BLUE(0x3),
    /**
     * Represents yellow dye
     */
    YELLOW(0x4),
    /**
     * Represents lime dye
     */
    LIME(0x5),
    /**
     * Represents pink dye
     */
    PINK(0x6),
    /**
     * Represents gray dye
     */
    GRAY(0x7),
    /**
     * Represents silver dye
     */
    SILVER(0x8),
    /**
     * Represents cyan dye
     */
    CYAN(0x9),
    /**
     * Represents purple dye
     */
    PURPLE(0xA),
    /**
     * Represents blue dye
     */
    BLUE(0xB),
    /**
     * Represents brown dye
     */
    BROWN(0xC),
    /**
     * Represents green dye
     */
    GREEN(0xD),
    /**
     * Represents red dye
     */
    RED(0xE),
    /**
     * Represents black dye
     */
    BLACK(0xF);

    private final byte data;
    private final static Map<Byte, DyeColor> BY_DATA = Maps.newHashMap();

    private DyeColor(final int data) {
        this.data = (byte) data;
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
        return BY_DATA.get(data);
    }

    static {
        for (DyeColor color : values()) {
            BY_DATA.put(color.getData(), color);
        }
    }
}
