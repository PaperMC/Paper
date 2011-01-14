
package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {
    BLACK((byte) 0x0),
    RED((byte) 0x1),
    GREEN((byte) 0x2),
    BROWN((byte) 0x3),
    BLUE((byte) 0x4),
    PURPLE((byte) 0x5),
    CYAN((byte) 0x6),
    SILVER((byte) 0x7),
    GRAY((byte) 0x8),
    PINK((byte) 0x9),
    LIME((byte) 0xA),
    YELLOW((byte) 0xB),
    LIGHT_BLUE((byte) 0xC),
    MAGENTA((byte) 0xD),
    ORANGE((byte) 0xE),
    WHITE((byte) 0xF);

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
