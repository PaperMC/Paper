package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values for chat
 */
public enum ChatColor {
    BLACK(0x0),
    DARK_BLUE(0x1),
    DARK_GREEN(0x2),
    DARK_AQUA(0x3),
    DARK_RED(0x4),
    DARK_PURPLE(0x5),
    GOLD(0x6),
    GRAY(0x7),
    DARK_GRAY(0x8),
    BLUE(0x9),
    GREEN(0xA),
    AQUA(0xB),
    RED(0xC),
    LIGHT_PURPLE(0xD),
    YELLOW(0xE),
    WHITE(0xF);

    private final int code;
    private final static Map<Integer, ChatColor> colors = new HashMap<Integer, ChatColor>();

    private ChatColor(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("\u00A7%x", code);
    }

    public static ChatColor getByCode(final int code) {
        return colors.get(code);
    }

    static {
        for (ChatColor color : ChatColor.values()) {
            colors.put(color.getCode(), color);
        }
    }
}