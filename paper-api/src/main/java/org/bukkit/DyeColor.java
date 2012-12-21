package org.bukkit;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {

    /**
     * Represents white dye
     */
    WHITE(0x0, Color.WHITE, Color.fromRGB(0x1E1B1B)),
    /**
     * Represents orange dye
     */
    ORANGE(0x1, Color.fromRGB(0xD87f33), Color.fromRGB(0xB3312C)),
    /**
     * Represents magenta dye
     */
    MAGENTA(0x2, Color.fromRGB(0xB24CD8), Color.fromRGB(0x3B511A)),
    /**
     * Represents light blue dye
     */
    LIGHT_BLUE(0x3, Color.fromRGB(0x6699D8), Color.fromRGB(0x51301A)),
    /**
     * Represents yellow dye
     */
    YELLOW(0x4, Color.fromRGB(0xE5E533), Color.fromRGB(0x253192)),
    /**
     * Represents lime dye
     */
    LIME(0x5, Color.fromRGB(0x7FCC19), Color.fromRGB(0x7B2FBE)),
    /**
     * Represents pink dye
     */
    PINK(0x6, Color.fromRGB(0xF27FA5), Color.fromRGB(0x287697)),
    /**
     * Represents gray dye
     */
    GRAY(0x7, Color.fromRGB(0x4C4C4C), Color.fromRGB(0xABABAB)),
    /**
     * Represents silver dye
     */
    SILVER(0x8, Color.fromRGB(0x999999), Color.fromRGB(0x434343)),
    /**
     * Represents cyan dye
     */
    CYAN(0x9, Color.fromRGB(0x4C7F99), Color.fromRGB(0xD88198)),
    /**
     * Represents purple dye
     */
    PURPLE(0xA, Color.fromRGB(0x7F3FB2), Color.fromRGB(0x41CD34)),
    /**
     * Represents blue dye
     */
    BLUE(0xB, Color.fromRGB(0x334CB2), Color.fromRGB(0xDECF2A)),
    /**
     * Represents brown dye
     */
    BROWN(0xC, Color.fromRGB(0x664C33), Color.fromRGB(0x6689D3)),
    /**
     * Represents green dye
     */
    GREEN(0xD, Color.fromRGB(0x667F33), Color.fromRGB(0xC354CD)),
    /**
     * Represents red dye
     */
    RED(0xE, Color.fromRGB(0x993333), Color.fromRGB(0xEB8844)),
    /**
     * Represents black dye
     */
    BLACK(0xF, Color.fromRGB(0x191919), Color.fromRGB(0xF0F0F0));

    private final byte data;
    private final Color color;
    private final Color firework;
    private final static DyeColor[] BY_DATA;
    private final static Map<Color, DyeColor> BY_COLOR;
    private final static Map<Color, DyeColor> BY_FIREWORK;

    private DyeColor(final int data, Color color, Color firework) {
        this.data = (byte) data;
        this.color = color;
        this.firework = firework;
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
     * Gets the firework color that this dye represents
     *
     * @return The {@link Color} that this dye represents
     */
    public Color getFireworkColor() {
        return firework;
    }

    /**
     * Gets the DyeColor with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link DyeColor} representing the given value, or null if it doesn't exist
     */
    public static DyeColor getByData(final byte data) {
        int i = 0xff & data;
        if (i > BY_DATA.length) {
            return null;
        }
        return BY_DATA[i];
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

    /**
     * Gets the DyeColor with the given firework color value
     *
     * @param color Color value to get dye by
     * @return The {@link DyeColor} representing the given value, or null if it doesn't exist
     */
    public static DyeColor getByFireworkColor(final Color color) {
        return BY_FIREWORK.get(color);
    }

    static {
        BY_DATA = values();
        ImmutableMap.Builder<Color, DyeColor> byColor = ImmutableMap.builder();
        ImmutableMap.Builder<Color, DyeColor> byFirework = ImmutableMap.builder();

        for (DyeColor color : values()) {
            BY_DATA[color.data & 0xff] = color;
            byColor.put(color.getColor(), color);
            byFirework.put(color.getFireworkColor(), color);
        }

        BY_COLOR = byColor.build();
        BY_FIREWORK = byFirework.build();
    }
}
