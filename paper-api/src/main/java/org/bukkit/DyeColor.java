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
    WHITE(0x0, Color.WHITE),
    /**
     * Represents orange dye
     */
    ORANGE(0x1, Color.fromRGB(0xD87f33)),
    /**
     * Represents magenta dye
     */
    MAGENTA(0x2, Color.fromRGB(0xB24CD8)),
    /**
     * Represents light blue dye
     */
    LIGHT_BLUE(0x3, Color.fromRGB(0x6699D8)),
    /**
     * Represents yellow dye
     */
    YELLOW(0x4, Color.fromRGB(0xE5E533)),
    /**
     * Represents lime dye
     */
    LIME(0x5, Color.fromRGB(0x7FCC19)),
    /**
     * Represents pink dye
     */
    PINK(0x6, Color.fromRGB(0xF27FA5)),
    /**
     * Represents gray dye
     */
    GRAY(0x7, Color.fromRGB(0x4C4C4C)),
    /**
     * Represents silver dye
     */
    SILVER(0x8, Color.fromRGB(0x999999)),
    /**
     * Represents cyan dye
     */
    CYAN(0x9, Color.fromRGB(0x4C7F99)),
    /**
     * Represents purple dye
     */
    PURPLE(0xA, Color.fromRGB(0x7F3FB2)),
    /**
     * Represents blue dye
     */
    BLUE(0xB, Color.fromRGB(0x334CB2)),
    /**
     * Represents brown dye
     */
    BROWN(0xC, Color.fromRGB(0x664C33)),
    /**
     * Represents green dye
     */
    GREEN(0xD, Color.fromRGB(0x667F33)),
    /**
     * Represents red dye
     */
    RED(0xE, Color.fromRGB(0x993333)),
    /**
     * Represents black dye
     */
    BLACK(0xF, Color.fromRGB(0x191919));

    private final byte data;
    private final Color color;
    private final static Map<Byte, DyeColor> BY_DATA = Maps.newHashMap();
    private final static Map<Color, DyeColor> BY_COLOR = Maps.newHashMap();

    private DyeColor(final int data, Color color) {
        this.data = (byte) data;
        this.color = color;
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
     * Gets the color that this dye represents
     *
     * @return The {@link Color} that this dye represents
     */
    public Color getColor() {
        return color;
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

    /**
     * Gets the DyeColor with the given color value
     *
     * @param color Color value to get the dye by
     * @return The {@link DyeColor} representing the given value, or null if it doesn't exist
     */
    public static DyeColor getByColor(final Color color) {
        return BY_COLOR.get(color);
    }

    static {
        for (DyeColor color : values()) {
            BY_DATA.put(color.getData(), color);
            BY_COLOR.put(color.getColor(), color);
        }
    }
}
