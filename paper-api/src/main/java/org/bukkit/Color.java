package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A container for a color palette. This class is immutable; the set methods
 * return a new color. The color names listed as fields are HTML4 standards,
 * but subject to change.
 */
@SerializableAs("Color")
public final class Color implements ConfigurationSerializable {
    private static final int BIT_MASK = 0xff;
    private static final int DEFAULT_ALPHA = 255;

    /**
     * White, or (0xFF,0xFF,0xFF) in (R,G,B)
     */
    public static final Color WHITE = fromRGB(0xFFFFFF);

    /**
     * Silver, or (0xC0,0xC0,0xC0) in (R,G,B)
     */
    public static final Color SILVER = fromRGB(0xC0C0C0);

    /**
     * Gray, or (0x80,0x80,0x80) in (R,G,B)
     */
    public static final Color GRAY = fromRGB(0x808080);

    /**
     * Black, or (0x00,0x00,0x00) in (R,G,B)
     */
    public static final Color BLACK = fromRGB(0x000000);

    /**
     * Red, or (0xFF,0x00,0x00) in (R,G,B)
     */
    public static final Color RED = fromRGB(0xFF0000);

    /**
     * Maroon, or (0x80,0x00,0x00) in (R,G,B)
     */
    public static final Color MAROON = fromRGB(0x800000);

    /**
     * Yellow, or (0xFF,0xFF,0x00) in (R,G,B)
     */
    public static final Color YELLOW = fromRGB(0xFFFF00);

    /**
     * Olive, or (0x80,0x80,0x00) in (R,G,B)
     */
    public static final Color OLIVE = fromRGB(0x808000);

    /**
     * Lime, or (0x00,0xFF,0x00) in (R,G,B)
     */
    public static final Color LIME = fromRGB(0x00FF00);

    /**
     * Green, or (0x00,0x80,0x00) in (R,G,B)
     */
    public static final Color GREEN = fromRGB(0x008000);

    /**
     * Aqua, or (0x00,0xFF,0xFF) in (R,G,B)
     */
    public static final Color AQUA = fromRGB(0x00FFFF);

    /**
     * Teal, or (0x00,0x80,0x80) in (R,G,B)
     */
    public static final Color TEAL = fromRGB(0x008080);

    /**
     * Blue, or (0x00,0x00,0xFF) in (R,G,B)
     */
    public static final Color BLUE = fromRGB(0x0000FF);

    /**
     * Navy, or (0x00,0x00,0x80) in (R,G,B)
     */
    public static final Color NAVY = fromRGB(0x000080);

    /**
     * Fuchsia, or (0xFF,0x00,0xFF) in (R,G,B)
     */
    public static final Color FUCHSIA = fromRGB(0xFF00FF);

    /**
     * Purple, or (0x80,0x00,0x80) in (R,G,B)
     */
    public static final Color PURPLE = fromRGB(0x800080);

    /**
     * Orange, or (0xFF,0xA5,0x00) in (R,G,B)
     */
    public static final Color ORANGE = fromRGB(0xFFA500);

    private final byte alpha;
    private final byte red;
    private final byte green;
    private final byte blue;

    /**
     * Creates a new Color object from an alpha, red, green, and blue
     *
     * @param alpha integer from 0-255
     * @param red integer from 0-255
     * @param green integer from 0-255
     * @param blue integer from 0-255
     * @return a new Color object for the alpha, red, green, blue
     * @throws IllegalArgumentException if any value is strictly {@literal >255 or <0}
     */
    @NotNull
    public static Color fromARGB(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        return new Color(alpha, red, green, blue);
    }

    /**
     * Creates a new Color object from a red, green, and blue
     *
     * @param red integer from 0-255
     * @param green integer from 0-255
     * @param blue integer from 0-255
     * @return a new Color object for the red, green, blue
     * @throws IllegalArgumentException if any value is strictly {@literal >255 or <0}
     */
    @NotNull
    public static Color fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new Color(DEFAULT_ALPHA, red, green, blue);
    }

    /**
     * Creates a new Color object from a blue, green, and red
     *
     * @param blue integer from 0-255
     * @param green integer from 0-255
     * @param red integer from 0-255
     * @return a new Color object for the red, green, blue
     * @throws IllegalArgumentException if any value is strictly {@literal >255 or <0}
     */
    @NotNull
    public static Color fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new Color(DEFAULT_ALPHA, red, green, blue);
    }

    /**
     * Creates a new color object from an integer that contains the red,
     * green, and blue bytes in the lowest order 24 bits.
     *
     * @param rgb the integer storing the red, green, and blue values
     * @return a new color object for specified values
     * @throws IllegalArgumentException if any data is in the highest order 8
     *     bits
     */
    @NotNull
    public static Color fromRGB(int rgb) throws IllegalArgumentException {
        Preconditions.checkArgument((rgb >> 24) == 0, "Extraneous data in: %s", rgb);
        return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb & BIT_MASK);
    }

    /**
     * Creates a new color object from an integer that contains the alpha, red,
     * green, and blue bytes.
     *
     * @param argb the integer storing the alpha, red, green, and blue values
     * @return a new color object for specified values
     */
    @NotNull
    public static Color fromARGB(int argb) {
        return fromARGB(argb >> 24 & BIT_MASK, argb >> 16 & BIT_MASK, argb >> 8 & BIT_MASK, argb & BIT_MASK);
    }

    /**
     * Creates a new color object from an integer that contains the blue,
     * green, and red bytes in the lowest order 24 bits.
     *
     * @param bgr the integer storing the blue, green, and red values
     * @return a new color object for specified values
     * @throws IllegalArgumentException if any data is in the highest order 8
     *     bits
     */
    @NotNull
    public static Color fromBGR(int bgr) throws IllegalArgumentException {
        Preconditions.checkArgument((bgr >> 24) == 0, "Extrenuous data in: %s", bgr);
        return fromBGR(bgr >> 16 & BIT_MASK, bgr >> 8 & BIT_MASK, bgr & BIT_MASK);
    }

    private Color(int red, int green, int blue) {
        this(DEFAULT_ALPHA, red, green, blue);
    }

    private Color(int alpha, int red, int green, int blue) {
        Preconditions.checkArgument(alpha >= 0 && alpha <= BIT_MASK, "Alpha[%s] is not between 0-255", alpha);
        Preconditions.checkArgument(red >= 0 && red <= BIT_MASK, "Red[%s] is not between 0-255", red);
        Preconditions.checkArgument(green >= 0 && green <= BIT_MASK, "Green[%s] is not between 0-255", green);
        Preconditions.checkArgument(blue >= 0 && blue <= BIT_MASK, "Blue[%s] is not between 0-255", blue);

        this.alpha = (byte) alpha;
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    /**
     * Gets the alpha component
     *
     * @return alpha component, from 0 to 255
     */
    public int getAlpha() {
        return BIT_MASK & alpha;
    }

    /**
     * Creates a new Color object with specified component
     *
     * @param alpha the alpha component, from 0 to 255
     * @return a new color object with the red component
     */
    @NotNull
    public Color setAlpha(int alpha) {
        return fromARGB(alpha, getRed(), getGreen(), getBlue());
    }

    /**
     * Gets the red component
     *
     * @return red component, from 0 to 255
     */
    public int getRed() {
        return BIT_MASK & red;
    }

    /**
     * Creates a new Color object with specified component
     *
     * @param red the red component, from 0 to 255
     * @return a new color object with the red component
     */
    @NotNull
    public Color setRed(int red) {
        return fromARGB(getAlpha(), red, getGreen(), getBlue());
    }

    /**
     * Gets the green component
     *
     * @return green component, from 0 to 255
     */
    public int getGreen() {
        return BIT_MASK & green;
    }

    /**
     * Creates a new Color object with specified component
     *
     * @param green the red component, from 0 to 255
     * @return a new color object with the red component
     */
    @NotNull
    public Color setGreen(int green) {
        return fromARGB(getAlpha(), getRed(), green, getBlue());
    }

    /**
     * Gets the blue component
     *
     * @return blue component, from 0 to 255
     */
    public int getBlue() {
        return BIT_MASK & blue;
    }

    /**
     * Creates a new Color object with specified component
     *
     * @param blue the red component, from 0 to 255
     * @return a new color object with the red component
     */
    @NotNull
    public Color setBlue(int blue) {
        return fromARGB(getAlpha(), getRed(), getGreen(), blue);
    }

    /**
     * Gets the color as an RGB integer.
     *
     * @return An integer representation of this color, as 0xRRGGBB
     */
    public int asRGB() {
        return getRed() << 16 | getGreen() << 8 | getBlue();
    }

    /**
     * Gets the color as an ARGB integer.
     *
     * @return An integer representation of this color, as 0xAARRGGBB
     */
    public int asARGB() {
        return getAlpha() << 24 | getRed() << 16 | getGreen() << 8 | getBlue();
    }

    /**
     * Gets the color as an BGR integer.
     *
     * @return An integer representation of this color, as 0xBBGGRR
     */
    public int asBGR() {
        return getBlue() << 16 | getGreen() << 8 | getRed();
    }

    /**
     * Creates a new color with its RGB components changed as if it was dyed
     * with the colors passed in, replicating vanilla workbench dyeing
     *
     * @param colors The DyeColors to dye with
     * @return A new color with the changed rgb components
     */
    // TODO: Javadoc what this method does, not what it mimics. API != Implementation
    @NotNull
    public Color mixDyes(@NotNull DyeColor... colors) {
        Preconditions.checkArgument(colors != null && Arrays.stream(colors).allMatch(Objects::nonNull), "DyeColor cannot be null or contain null values");

        Color[] toPass = new Color[colors.length];
        for (int i = 0; i < colors.length; i++) {
            toPass[i] = colors[i].getColor();
        }

        return mixColors(toPass);
    }

    /**
     * Creates a new color with its RGB components changed as if it was dyed
     * with the colors passed in, replicating vanilla workbench dyeing.
     *
     * <b>Note that this method does not currently take into account alpha
     * components.</b>
     *
     * @param colors The colors to dye with
     * @return A new color with the changed rgb components
     */
    // TODO: Javadoc what this method does, not what it mimics. API != Implementation
    @NotNull
    public Color mixColors(@NotNull Color... colors) {
        Preconditions.checkArgument(colors != null && Arrays.stream(colors).allMatch(Objects::nonNull), "Colors cannot be null");

        int totalRed = this.getRed();
        int totalGreen = this.getGreen();
        int totalBlue = this.getBlue();
        int totalMax = Math.max(Math.max(totalRed, totalGreen), totalBlue);
        for (Color color : colors) {
            totalRed += color.getRed();
            totalGreen += color.getGreen();
            totalBlue += color.getBlue();
            totalMax += Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());
        }

        float averageRed = totalRed / (colors.length + 1);
        float averageGreen = totalGreen / (colors.length + 1);
        float averageBlue = totalBlue / (colors.length + 1);
        float averageMax = totalMax / (colors.length + 1);

        float maximumOfAverages = Math.max(Math.max(averageRed, averageGreen), averageBlue);
        float gainFactor = averageMax / maximumOfAverages;

        return Color.fromRGB((int) (averageRed * gainFactor), (int) (averageGreen * gainFactor), (int) (averageBlue * gainFactor));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Color)) {
            return false;
        }
        final Color that = (Color) o;
        return this.alpha == that.alpha && this.blue == that.blue && this.green == that.green && this.red == that.red;
    }

    @Override
    public int hashCode() {
        return asARGB() ^ Color.class.hashCode();
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return ImmutableMap.of(
                "ALPHA", getAlpha(),
                "RED", getRed(),
                "BLUE", getBlue(),
                "GREEN", getGreen()
        );
    }

    @SuppressWarnings("javadoc")
    @NotNull
    public static Color deserialize(@NotNull Map<String, Object> map) {
        return fromARGB(
                asInt("ALPHA", map, DEFAULT_ALPHA),
                asInt("RED", map),
                asInt("GREEN", map),
                asInt("BLUE", map)
        );
    }

    private static int asInt(@NotNull String string, @NotNull Map<String, Object> map) {
        return asInt(string, map, null);
    }

    private static int asInt(@NotNull String string, @NotNull Map<String, Object> map, @Nullable Object defaultValue) {
        Object value = map.getOrDefault(string, defaultValue);
        if (value == null) {
            throw new IllegalArgumentException(string + " not in map " + map);
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException(string + '(' + value + ") is not a number");
        }
        return ((Number) value).intValue();
    }

    @Override
    public String toString() {
        return "Color:[argb0x" + Integer.toHexString(asARGB()).toUpperCase() + "]";
    }
}
