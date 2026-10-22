package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.util.ARGBLike;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * A container for a color palette. This class is immutable; the set methods
 * return a new color. The color names listed as fields are HTML4 standards,
 * but subject to change.
 */
@SerializableAs("Color")
public final class Color implements ConfigurationSerializable, ARGBLike {
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
    public static Color fromARGB(
        final @Range(from = 0, to = 255) int alpha,
        final @Range(from = 0, to = 255) int red,
        final @Range(from = 0, to = 255) int green,
        final @Range(from = 0, to = 255) int blue
    ) throws IllegalArgumentException {
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
    public static Color fromRGB(
        final @Range(from = 0, to = 255) int red,
        final @Range(from = 0, to = 255) int green,
        final @Range(from = 0, to = 255) int blue
    ) throws IllegalArgumentException {
        return new Color(DEFAULT_ALPHA, red, green, blue);
    }

    /**
     * Creates a new Color object from a blue, green, and red
     *
     * @param blue integer from 0-255
     * @param green integer from 0-255
     * @param red integer from 0-255
     * @return a new Color object for the blue, green, red
     * @throws IllegalArgumentException if any value is strictly {@literal >255 or <0}
     */
    @NotNull
    public static Color fromBGR(
        final @Range(from = 0, to = 255) int blue,
        final @Range(from = 0, to = 255) int green,
        final @Range(from = 0, to = 255) int red
    ) throws IllegalArgumentException {
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
    public static Color fromRGB(final int rgb) throws IllegalArgumentException {
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
    public static Color fromARGB(final int argb) {
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
    public static Color fromBGR(final int bgr) throws IllegalArgumentException {
        Preconditions.checkArgument((bgr >> 24) == 0, "Extrenuous data in: %s", bgr);
        return fromBGR(bgr >> 16 & BIT_MASK, bgr >> 8 & BIT_MASK, bgr & BIT_MASK);
    }

    private Color(final int red, final int green, final int blue) {
        this(DEFAULT_ALPHA, red, green, blue);
    }

    private Color(final int alpha, final int red, final int green, final int blue) {
        Preconditions.checkArgument(alpha >= 0 && alpha <= 0xff, "Alpha[%s] is not between 0-255", alpha);
        Preconditions.checkArgument(red >= 0 && red <= 0xff, "Red[%s] is not between 0-255", red);
        Preconditions.checkArgument(green >= 0 && green <= 0xff, "Green[%s] is not between 0-255", green);
        Preconditions.checkArgument(blue >= 0 && blue <= 0xff, "Blue[%s] is not between 0-255", blue);

        this.alpha = (byte) alpha;
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    /**
     * Gets the alpha component.
     *
     * @return alpha component, from 0 to 255
     */
    public @Range(from = 0, to = 255) int getAlpha() {
        return BIT_MASK & alpha;
    }

    /**
     * Gets the alpha component.
     *
     * @return alpha component, from 0 to 255
     */
    @Override
    public @Range(from = 0, to = 255) int alpha() {
        return this.getAlpha();
    }

    /**
     * Creates a new Color object with a specified component.
     *
     * @param alpha the alpha component, from 0 to 255
     * @return a new color object with the alpha component
     */
    @NotNull
    public Color setAlpha(final @Range(from = 0, to = 255) int alpha) {
        return fromARGB(alpha, this.getRed(), this.getGreen(), this.getBlue());
    }

    /**
     * Gets the red component.
     *
     * @return red component, from 0 to 255
     */
    public @Range(from = 0, to = 255) int getRed() {
        return BIT_MASK & this.red;
    }

    /**
     * Gets the red component.
     *
     * @return red component, from 0 to 255
     */
    @Override
    public @Range(from = 0, to = 255) int red() {
        return this.getRed();
    }

    /**
     * Creates a new Color object with a specified component.
     *
     * @param red the red component, from 0 to 255
     * @return a new color object with the red component
     */
    @NotNull
    public Color setRed(final @Range(from = 0, to = 255) int red) {
        return fromARGB(this.getAlpha(), red, this.getGreen(), this.getBlue());
    }

    /**
     * Gets the green component.
     *
     * @return green component, from 0 to 255
     */
    public @Range(from = 0, to = 255) int getGreen() {
        return BIT_MASK & this.green;
    }

    /**
     * Gets the green component.
     *
     * @return green component, from 0 to 255
     */
    @Override
    public @Range(from = 0, to = 255) int green() {
        return this.getGreen();
    }

    /**
     * Creates a new Color object with a specified component.
     *
     * @param green the green component, from 0 to 255
     * @return a new color object with the green component
     */
    @NotNull
    public Color setGreen(final @Range(from = 0, to = 255) int green) {
        return fromARGB(this.getAlpha(), this.getRed(), green, this.getBlue());
    }

    /**
     * Gets the blue component.
     *
     * @return blue component, from 0 to 255
     */
    public @Range(from = 0, to = 255) int getBlue() {
        return BIT_MASK & this.blue;
    }

    /**
     * Gets the blue component.
     *
     * @return blue component, from 0 to 255
     */
    @Override
    public @Range(from = 0, to = 255) int blue() {
        return this.getBlue();
    }

    /**
     * Creates a new Color object with a specified component.
     *
     * @param blue the blue component, from 0 to 255
     * @return a new color object with the blue component
     */
    @NotNull
    public Color setBlue(final @Range(from = 0, to = 255) int blue) {
        return fromARGB(this.getAlpha(), this.getRed(), this.getGreen(), blue);
    }

    /**
     * Gets the color as an RGB integer.
     *
     * @return An integer representation of this color, as 0xRRGGBB
     */
    public int asRGB() {
        return this.getRed() << 16 | this.getGreen() << 8 | this.getBlue();
    }

    /**
     * Gets the color as an ARGB integer.
     *
     * @return An integer representation of this color, as 0xAARRGGBB
     */
    public int asARGB() {
        return this.getAlpha() << 24 | this.getRed() << 16 | this.getGreen() << 8 | this.getBlue();
    }

    /**
     * Gets the color as an BGR integer.
     *
     * @return An integer representation of this color, as 0xBBGGRR
     */
    public int asBGR() {
        return this.getBlue() << 16 | this.getGreen() << 8 | this.getRed();
    }

    /**
     * Creates a new color with its RGB components changed as if it was dyed
     * with the colors passed in, replicating vanilla workbench dyeing.
     *
     * @param colors The DyeColors to dye with
     * @return A new color with the changed rgb components
     */
    // TODO: Javadoc what this method does, not what it mimics. API != Implementation
    @NotNull
    public Color mixDyes(final @NotNull DyeColor... colors) {
        Preconditions.checkArgument(colors != null && Arrays.stream(colors).allMatch(Objects::nonNull), "DyeColor cannot be null or contain null values");

        final Color[] toPass = new Color[colors.length];
        for (int i = 0; i < colors.length; i++) {
            toPass[i] = colors[i].getColor();
        }

        return this.mixColors(toPass);
    }

    /**
     * Creates a new color with its RGB components changed as if it was dyed
     * with the colors passed in, replicating vanilla workbench dyeing.
     * <br>
     * <b>Note that this method does not currently take into account alpha
     * components.</b>
     *
     * @param colors The colors to dye with
     * @return A new color with the changed rgb components
     */
    // TODO: Javadoc what this method does, not what it mimics. API != Implementation
    @NotNull
    public Color mixColors(final @NotNull Color... colors) {
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

        final float averageRed = totalRed / (colors.length + 1);
        final float averageGreen = totalGreen / (colors.length + 1);
        final float averageBlue = totalBlue / (colors.length + 1);
        final float averageMax = totalMax / (colors.length + 1);

        final float maximumOfAverages = Math.max(Math.max(averageRed, averageGreen), averageBlue);
        final float gainFactor = averageMax / maximumOfAverages;

        return Color.fromRGB((int) (averageRed * gainFactor), (int) (averageGreen * gainFactor), (int) (averageBlue * gainFactor));
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Color that)) {
            return false;
        }
        return this.alpha == that.alpha && this.blue == that.blue && this.green == that.green && this.red == that.red;
    }

    @Override
    public int hashCode() {
        return this.asARGB() ^ Color.class.hashCode();
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return ImmutableMap.of(
                "ALPHA", this.getAlpha(),
                "RED", this.getRed(),
                "BLUE", this.getBlue(),
                "GREEN", this.getGreen()
        );
    }

    @NotNull
    public static Color deserialize(final @NotNull Map<String, Object> map) {
        return fromARGB(
                asInt("ALPHA", map, DEFAULT_ALPHA),
                asInt("RED", map),
                asInt("GREEN", map),
                asInt("BLUE", map)
        );
    }

    private static int asInt(final @NotNull String string, final @NotNull Map<String, Object> map) {
        return asInt(string, map, null);
    }

    private static int asInt(final @NotNull String string, final @NotNull Map<String, Object> map, final @Nullable Object defaultValue) {
        final Object value = map.getOrDefault(string, defaultValue);
        if (value == null) {
            throw new IllegalArgumentException(string + " not in map " + map);
        }
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException(string + '(' + value + ") is not a number");
        }
        return number.intValue();
    }

    @Override
    public String toString() {
        return "Color:[argb0x" + Integer.toHexString(this.asARGB()).toUpperCase(Locale.ROOT) + "]";
    }
}
