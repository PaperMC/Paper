package io.papermc.paper.util;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;

public record RGBColor(
    @Range(from = 0x0, to = 0xff) int red,
    @Range(from = 0x0, to = 0xff) int green,
    @Range(from = 0x0, to = 0xff) int blue
) implements RGBLike {
    public static RGBColor fromRGB(int value) {
        return new RGBColor((value >> 16) & 0xFF, (value >> 8) & 0xFF, value & 0xFF);
    }

    public int asRGB() {
        return (red << 16) | (green << 8) | blue;
    }
}
