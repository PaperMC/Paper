package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values
 */
public enum Color {
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
    GREEN(0xa),
    AQUA(0xb),
    RED(0xc),
    LIGHT_PURPLE(0xd),
    YELLOW(0xe),
    WHITE(0xf);

    private final int code;
    private final static Map<Integer, Color> colors = new HashMap<Integer, Color>();

    private Color(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "\u00A7" + code;
    }

    static {
        for (Color color : Color.values()) {
            colors.put(color.getCode(), color);
        }
    }
}