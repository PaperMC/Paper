
package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {
    WHITE((byte) 0x0),
    ORANGE((byte) 0x1),
    MAGENTA((byte) 0x2),
    LIGHT_BLUE((byte) 0x3),
    YELLOW((byte) 0x4),
    LIME((byte) 0x5),
    PINK((byte) 0x6),
    GRAY((byte) 0x7),
    SILVER((byte) 0x8),
    CYAN((byte) 0x9),
    PURPLE((byte) 0xA),
    BLUE((byte) 0xB),
    BROWN((byte) 0xC),
    GREEN((byte) 0xD),
    RED((byte) 0xE),
    BLACK((byte) 0xF);

    private final byte data;
    private final static Map<Byte, DyeColor> colors = new HashMap<Byte, DyeColor>();

    private DyeColor(final byte data) {
        this.data = data;
    }

    public byte getData() {
        return data;
    }

    public static DyeColor getByData(final byte data) {
        return colors.get(data);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            colors.put(color.getData(), color);
        }
    }
}
