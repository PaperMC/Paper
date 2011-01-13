
package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {
    BLACK(0x0),
    RED(0x1),
    GREEN(0x2),
    BROWN(0x3),
    BLUE(0x4),
    PURPLE(0x5),
    CYAN(0x6),
    SILVER(0x7),
    GRAY(0x8),
    PINK(0x9),
    LIME(0xA),
    YELLOW(0xB),
    LIGHT_BLUE(0xC),
    MAGENTA(0xD),
    ORANGE(0xE),
    WHITE(0xF);

    private final int data;
    private final static Map<Integer, DyeColor> colors = new HashMap<Integer, DyeColor>();

    private DyeColor(final int data) {
        this.data = data;
    }

    public int getValue() {
        return data;
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            colors.put(color.getValue(), color);
        }
    }
}
