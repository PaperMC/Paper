package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * All supported color values for dyes and cloth
 */
public enum DyeColor {

    /**
     * Represents white dye.
     */
    WHITE(0x0, 0xF, Color.fromRGB(0xF9FFFE), Color.fromRGB(0xF0F0F0)),
    /**
     * Represents orange dye.
     */
    ORANGE(0x1, 0xE, Color.fromRGB(0xF9801D), Color.fromRGB(0xEB8844)),
    /**
     * Represents magenta dye.
     */
    MAGENTA(0x2, 0xD, Color.fromRGB(0xC74EBD), Color.fromRGB(0xC354CD)),
    /**
     * Represents light blue dye.
     */
    LIGHT_BLUE(0x3, 0xC, Color.fromRGB(0x3AB3DA), Color.fromRGB(0x6689D3)),
    /**
     * Represents yellow dye.
     */
    YELLOW(0x4, 0xB, Color.fromRGB(0xFED83D), Color.fromRGB(0xDECF2A)),
    /**
     * Represents lime dye.
     */
    LIME(0x5, 0xA, Color.fromRGB(0x80C71F), Color.fromRGB(0x41CD34)),
    /**
     * Represents pink dye.
     */
    PINK(0x6, 0x9, Color.fromRGB(0xF38BAA), Color.fromRGB(0xD88198)),
    /**
     * Represents gray dye.
     */
    GRAY(0x7, 0x8, Color.fromRGB(0x474F52), Color.fromRGB(0x434343)),
    /**
     * Represents light gray dye.
     */
    LIGHT_GRAY(0x8, 0x7, Color.fromRGB(0x9D9D97), Color.fromRGB(0xABABAB)),
    /**
     * Represents cyan dye.
     */
    CYAN(0x9, 0x6, Color.fromRGB(0x169C9C), Color.fromRGB(0x287697)),
    /**
     * Represents purple dye.
     */
    PURPLE(0xA, 0x5, Color.fromRGB(0x8932B8), Color.fromRGB(0x7B2FBE)),
    /**
     * Represents blue dye.
     */
    BLUE(0xB, 0x4, Color.fromRGB(0x3C44AA), Color.fromRGB(0x253192)),
    /**
     * Represents brown dye.
     */
    BROWN(0xC, 0x3, Color.fromRGB(0x835432), Color.fromRGB(0x51301A)),
    /**
     * Represents green dye.
     */
    GREEN(0xD, 0x2, Color.fromRGB(0x5E7C16), Color.fromRGB(0x3B511A)),
    /**
     * Represents red dye.
     */
    RED(0xE, 0x1, Color.fromRGB(0xB02E26), Color.fromRGB(0xB3312C)),
    /**
     * Represents black dye.
     */
    BLACK(0xF, 0x0, Color.fromRGB(0x1D1D21), Color.fromRGB(0x1E1B1B));

    private final byte woolData;
    private final byte dyeData;
    private final Color color;
    private final Color firework;
    private static final DyeColor[] BY_WOOL_DATA;
    private static final DyeColor[] BY_DYE_DATA;
    private static final Map<Color, DyeColor> BY_COLOR;
    private static final Map<Color, DyeColor> BY_FIREWORK;

    private DyeColor(final int woolData, final int dyeData, /*@NotNull*/ Color color, /*@NotNull*/ Color firework) {
        this.woolData = (byte) woolData;
        this.dyeData = (byte) dyeData;
        this.color = color;
        this.firework = firework;
    }

    /**
     * Gets the associated wool data value representing this color.
     *
     * @return A byte containing the wool data value of this color
     * @see #getDyeData()
     * @deprecated Magic value
     */
    @Deprecated
    public byte getWoolData() {
        return woolData;
    }

    /**
     * Gets the associated dye data value representing this color.
     *
     * @return A byte containing the dye data value of this color
     * @see #getWoolData()
     * @deprecated Magic value
     */
    @Deprecated
    public byte getDyeData() {
        return dyeData;
    }

    /**
     * Gets the color that this dye represents.
     *
     * @return The {@link Color} that this dye represents
     */
    @NotNull
    public Color getColor() {
        return color;
    }

    /**
     * Gets the firework color that this dye represents.
     *
     * @return The {@link Color} that this dye represents
     */
    @NotNull
    public Color getFireworkColor() {
        return firework;
    }

    /**
     * Gets the DyeColor with the given wool data value.
     *
     * @param data Wool data value to fetch
     * @return The {@link DyeColor} representing the given value, or null if
     *     it doesn't exist
     * @see #getByDyeData(byte)
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static DyeColor getByWoolData(final byte data) {
        int i = 0xff & data;
        if (i >= BY_WOOL_DATA.length) {
            return null;
        }
        return BY_WOOL_DATA[i];
    }

    /**
     * Gets the DyeColor with the given dye data value.
     *
     * @param data Dye data value to fetch
     * @return The {@link DyeColor} representing the given value, or null if
     *     it doesn't exist
     * @see #getByWoolData(byte)
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static DyeColor getByDyeData(final byte data) {
        int i = 0xff & data;
        if (i >= BY_DYE_DATA.length) {
            return null;
        }
        return BY_DYE_DATA[i];
    }

    /**
     * Gets the DyeColor with the given color value.
     *
     * @param color Color value to get the dye by
     * @return The {@link DyeColor} representing the given value, or null if
     *     it doesn't exist
     */
    @Nullable
    public static DyeColor getByColor(@NotNull final Color color) {
        return BY_COLOR.get(color);
    }

    /**
     * Gets the DyeColor with the given firework color value.
     *
     * @param color Color value to get dye by
     * @return The {@link DyeColor} representing the given value, or null if
     *     it doesn't exist
     */
    @Nullable
    public static DyeColor getByFireworkColor(@NotNull final Color color) {
        return BY_FIREWORK.get(color);
    }

    /**
     * Gets the DyeColor for the given name, possibly doing legacy transformations.
     *
     * @param name dye name
     * @return dye color
     * @deprecated legacy use only
     */
    @Deprecated
    @NotNull
    public static DyeColor legacyValueOf(@Nullable String name) {
        return "SILVER".equals(name) ? DyeColor.LIGHT_GRAY : DyeColor.valueOf(name);
    }

    static {
        BY_WOOL_DATA = values();
        BY_DYE_DATA = values();
        ImmutableMap.Builder<Color, DyeColor> byColor = ImmutableMap.builder();
        ImmutableMap.Builder<Color, DyeColor> byFirework = ImmutableMap.builder();

        for (DyeColor color : values()) {
            BY_WOOL_DATA[color.woolData & 0xff] = color;
            BY_DYE_DATA[color.dyeData & 0xff] = color;
            byColor.put(color.getColor(), color);
            byFirework.put(color.getFireworkColor(), color);
        }

        BY_COLOR = byColor.build();
        BY_FIREWORK = byFirework.build();
    }
}
