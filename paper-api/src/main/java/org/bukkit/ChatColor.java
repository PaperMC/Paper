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
    BLACK('0', 0x01),
    /**
     * Represents dark blue
     */
    DARK_BLUE('1', 0x1),
    /**
     * Represents dark green
     */
    DARK_GREEN('2', 0x2),
    /**
     * Represents dark blue (aqua)
     */
    DARK_AQUA('3', 0x3),
    /**
     * Represents dark red
     */
    DARK_RED('4', 0x4),
    /**
     * Represents dark purple
     */
    DARK_PURPLE('5', 0x5),
    /**
     * Represents gold
     */
    GOLD('6', 0x6),
    /**
     * Represents gray
     */
    GRAY('7', 0x7),
    /**
     * Represents dark gray
     */
    DARK_GRAY('8', 0x8),
    /**
     * Represents blue
     */
    BLUE('9', 0x9),
    /**
     * Represents green
     */
    GREEN('a', 0xA),
    /**
     * Represents aqua
     */
    AQUA('b', 0xB),
    /**
     * Represents red
     */
    RED('c', 0xC),
    /**
     * Represents light purple
     */
    LIGHT_PURPLE('d', 0xD),
    /**
     * Represents yellow
     */
    YELLOW('e', 0xE),
    /**
     * Represents white
     */
    WHITE('f', 0xF),
    /**
     * Represents magical characters that change around randomly
     */
    MAGIC('k', 0x10);

    private final int intCode;
    private final char code;
    private final static Map<Integer, ChatColor> colors = new HashMap<Integer, ChatColor>();
    private final static Map<Character, ChatColor> lookup = new HashMap<Character, ChatColor>();

    private ChatColor(char code, int intCode) {
        this.code = code;
        this.intCode = intCode;
    }

    /**
     * Gets the data value associated with this color
     *
     * @return An integer value of this color code
     * @deprecated Use {@link #getChar()}
     */
    public int getCode() {
        return intCode;
    }

    /**
     * Gets the char value associated with this color
     *
     * @return A char value of this color code
     */
    public char getChar() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("\u00A7%c", code);
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code, or null if it doesn't exist
     * @deprecated Use {@link #getByChar(char)}
     */
    public static ChatColor getByCode(final int code) {
        return colors.get(code);
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code, or null if it doesn't exist
     */
    public static ChatColor getByChar(char code) {
        return lookup.get(code);
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code, or null if it doesn't exist
     */
    public static ChatColor getByChar(String code) {
        return lookup.get(code.charAt(0));
    }

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return input.replaceAll("(?i)\u00A7[0-9A-FK]", "");
    }

    static {
        for (ChatColor color : ChatColor.values()) {
            colors.put(color.getCode(), color);
            lookup.put(color.getChar(), color);
        }
    }
}
