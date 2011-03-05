package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values for chat
 */
public enum ChatColor {
    /**
     * Represents black
     */
    BLACK(0x0),
    /**
     * Represents dark blue
     */
    DARK_BLUE(0x1),
    /**
     * Represents dark green
     */
    DARK_GREEN(0x2),
    /**
     * Represents dark blue (aqua)
     */
    DARK_AQUA(0x3),
    /**
     * Represents dark red
     */
    DARK_RED(0x4),
    /**
     * Represents dark purple
     */
    DARK_PURPLE(0x5),
    /**
     * Represents gold
     */
    GOLD(0x6),
    /**
     * Represents gray
     */
    GRAY(0x7),
    /**
     * Represents dark gray
     */
    DARK_GRAY(0x8),
    /**
     * Represents blue
     */
    BLUE(0x9),
    /**
     * Represents green
     */
    GREEN(0xA),
    /**
     * Represents aqua
     */
    AQUA(0xB),
    /**
     * Represents red
     */
    RED(0xC),
    /**
     * Represents light purple
     */
    LIGHT_PURPLE(0xD),
    /**
     * Represents yellow
     */
    YELLOW(0xE),
    /**
     * Represents white
     */
    WHITE(0xF);

    private final int code;
    private final static Map<Integer, ChatColor> colors = new HashMap<Integer, ChatColor>();

    private ChatColor(final int code) {
        this.code = code;
    }

    /**
     * Gets the data value associated with this color
     *
     * @return An integer value of this color code
     */
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("\u00A7%x", code);
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link Color} with the given code, or null if it doesn't exist
     */
    public static ChatColor getByCode(final int code) {
        return colors.get(code);
    }

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(final String input) {
        if (input == null)
            return null;
        
        return input.replaceAll("(?i)\u00A7[0-F]", "");
    }

    static {
        for (ChatColor color : ChatColor.values()) {
            colors.put(color.getCode(), color);
        }
    }
}