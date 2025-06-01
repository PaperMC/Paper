package org.bukkit.map;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the palette that map items use.
 * <p>
 * These fields are hee base color ranges. Each entry corresponds to four
 * colors of varying shades with values entry to entry + 3.
 */
public final class MapPalette {
    // Internal mechanisms
    private MapPalette() {}

    private static double getDistance(@NotNull Color c1, @NotNull Color c2) {
        // Paper start - Optimize color distance calculation by removing floating point math
        int rsum = c1.getRed() + c2.getRed(); // Use sum instead of mean for no division
        int r = c1.getRed() - c2.getRed();
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        // All weights are 512x their original to avoid floating point division
        int weightR = 1024 + rsum;
        int weightG = 2048;
        int weightB = 1024 + (255*2 - rsum);

        // Division by 256 here is unnecessary as this won't change the result of the sort
        return weightR * r * r + weightG * g * g + weightB * b * b;
        // Paper end
    }

    @NotNull
    static final Color[] colors = {
        // Start generate - MapPalette#colors
        // @GeneratedFrom 1.21.6-pre1
        new Color(0x00000000, true),
        new Color(0x00000000, true),
        new Color(0x00000000, true),
        new Color(0x00000000, true),
        new Color(0x597D27),
        new Color(0x6D9930),
        new Color(0x7FB238),
        new Color(0x435E1D),
        new Color(0xAEA473),
        new Color(0xD5C98C),
        new Color(0xF7E9A3),
        new Color(0x827B56),
        new Color(0x8C8C8C),
        new Color(0xABABAB),
        new Color(0xC7C7C7),
        new Color(0x696969),
        new Color(0xB40000),
        new Color(0xDC0000),
        new Color(0xFF0000),
        new Color(0x870000),
        new Color(0x7070B4),
        new Color(0x8A8ADC),
        new Color(0xA0A0FF),
        new Color(0x545487),
        new Color(0x757575),
        new Color(0x909090),
        new Color(0xA7A7A7),
        new Color(0x585858),
        new Color(0x005700),
        new Color(0x006A00),
        new Color(0x007C00),
        new Color(0x004100),
        new Color(0xB4B4B4),
        new Color(0xDCDCDC),
        new Color(0xFFFFFF),
        new Color(0x878787),
        new Color(0x737681),
        new Color(0x8D909E),
        new Color(0xA4A8B8),
        new Color(0x565861),
        new Color(0x6A4C36),
        new Color(0x825E42),
        new Color(0x976D4D),
        new Color(0x4F3928),
        new Color(0x4F4F4F),
        new Color(0x606060),
        new Color(0x707070),
        new Color(0x3B3B3B),
        new Color(0x2D2DB4),
        new Color(0x3737DC),
        new Color(0x4040FF),
        new Color(0x212187),
        new Color(0x645432),
        new Color(0x7B663E),
        new Color(0x8F7748),
        new Color(0x4B3F26),
        new Color(0xB4B1AC),
        new Color(0xDCD9D3),
        new Color(0xFFFCF5),
        new Color(0x878581),
        new Color(0x985924),
        new Color(0xBA6D2C),
        new Color(0xD87F33),
        new Color(0x72431B),
        new Color(0x7D3598),
        new Color(0x9941BA),
        new Color(0xB24CD8),
        new Color(0x5E2872),
        new Color(0x486C98),
        new Color(0x5884BA),
        new Color(0x6699D8),
        new Color(0x365172),
        new Color(0xA1A124),
        new Color(0xC5C52C),
        new Color(0xE5E533),
        new Color(0x79791B),
        new Color(0x599011),
        new Color(0x6DB015),
        new Color(0x7FCC19),
        new Color(0x436C0D),
        new Color(0xAA5974),
        new Color(0xD06D8E),
        new Color(0xF27FA5),
        new Color(0x804357),
        new Color(0x353535),
        new Color(0x414141),
        new Color(0x4C4C4C),
        new Color(0x282828),
        new Color(0x6C6C6C),
        new Color(0x848484),
        new Color(0x999999),
        new Color(0x515151),
        new Color(0x35596C),
        new Color(0x416D84),
        new Color(0x4C7F99),
        new Color(0x284351),
        new Color(0x592C7D),
        new Color(0x6D3699),
        new Color(0x7F3FB2),
        new Color(0x43215E),
        new Color(0x24357D),
        new Color(0x2C4199),
        new Color(0x334CB2),
        new Color(0x1B285E),
        new Color(0x483524),
        new Color(0x58412C),
        new Color(0x664C33),
        new Color(0x36281B),
        new Color(0x485924),
        new Color(0x586D2C),
        new Color(0x667F33),
        new Color(0x36431B),
        new Color(0x6C2424),
        new Color(0x842C2C),
        new Color(0x993333),
        new Color(0x511B1B),
        new Color(0x111111),
        new Color(0x151515),
        new Color(0x191919),
        new Color(0x0D0D0D),
        new Color(0xB0A836),
        new Color(0xD7CD42),
        new Color(0xFAEE4D),
        new Color(0x847E28),
        new Color(0x409A96),
        new Color(0x4FBCB7),
        new Color(0x5CDBD5),
        new Color(0x307370),
        new Color(0x345AB4),
        new Color(0x3F6EDC),
        new Color(0x4A80FF),
        new Color(0x274387),
        new Color(0x009928),
        new Color(0x00BB32),
        new Color(0x00D93A),
        new Color(0x00721E),
        new Color(0x5B3C22),
        new Color(0x6F4A2A),
        new Color(0x815631),
        new Color(0x442D19),
        new Color(0x4F0100),
        new Color(0x600100),
        new Color(0x700200),
        new Color(0x3B0100),
        new Color(0x937C71),
        new Color(0xB4988A),
        new Color(0xD1B1A1),
        new Color(0x6E5D55),
        new Color(0x703919),
        new Color(0x89461F),
        new Color(0x9F5224),
        new Color(0x542B13),
        new Color(0x693D4C),
        new Color(0x804B5D),
        new Color(0x95576C),
        new Color(0x4E2E39),
        new Color(0x4F4C61),
        new Color(0x605D77),
        new Color(0x706C8A),
        new Color(0x3B3949),
        new Color(0x835D19),
        new Color(0xA0721F),
        new Color(0xBA8524),
        new Color(0x624613),
        new Color(0x485225),
        new Color(0x58642D),
        new Color(0x677535),
        new Color(0x363D1C),
        new Color(0x703637),
        new Color(0x8A4243),
        new Color(0xA04D4E),
        new Color(0x542829),
        new Color(0x281C18),
        new Color(0x31231E),
        new Color(0x392923),
        new Color(0x1E1512),
        new Color(0x5F4B45),
        new Color(0x745C54),
        new Color(0x876B62),
        new Color(0x473833),
        new Color(0x3D4040),
        new Color(0x4B4F4F),
        new Color(0x575C5C),
        new Color(0x2E3030),
        new Color(0x56333E),
        new Color(0x693E4B),
        new Color(0x7A4958),
        new Color(0x40262E),
        new Color(0x352B40),
        new Color(0x41354F),
        new Color(0x4C3E5C),
        new Color(0x282030),
        new Color(0x352318),
        new Color(0x412B1E),
        new Color(0x4C3223),
        new Color(0x281A12),
        new Color(0x35391D),
        new Color(0x414624),
        new Color(0x4C522A),
        new Color(0x282B16),
        new Color(0x642A20),
        new Color(0x7A3327),
        new Color(0x8E3C2E),
        new Color(0x4B1F18),
        new Color(0x1A0F0B),
        new Color(0x1F120D),
        new Color(0x251610),
        new Color(0x130B08),
        new Color(0x852122),
        new Color(0xA3292A),
        new Color(0xBD3031),
        new Color(0x641919),
        new Color(0x682C44),
        new Color(0x7F3653),
        new Color(0x943F61),
        new Color(0x4E2133),
        new Color(0x401114),
        new Color(0x4F1519),
        new Color(0x5C191D),
        new Color(0x300D0F),
        new Color(0x0F585E),
        new Color(0x126C73),
        new Color(0x167E86),
        new Color(0x0B4246),
        new Color(0x286462),
        new Color(0x327A78),
        new Color(0x3A8E8C),
        new Color(0x1E4B4A),
        new Color(0x3C1F2B),
        new Color(0x4A2535),
        new Color(0x562C3E),
        new Color(0x2D1720),
        new Color(0x0E7F5D),
        new Color(0x119B72),
        new Color(0x14B485),
        new Color(0x0A5F46),
        new Color(0x464646),
        new Color(0x565656),
        new Color(0x646464),
        new Color(0x343434),
        new Color(0x987B67),
        new Color(0xBA967E),
        new Color(0xD8AF93),
        new Color(0x725C4D),
        new Color(0x597569),
        new Color(0x6D9081),
        new Color(0x7FA796),
        new Color(0x43584F),
        // End generate - MapPalette#colors
    };

    // Interface
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte TRANSPARENT = 0;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte LIGHT_GREEN = 4;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte LIGHT_BROWN = 8;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte GRAY_1 = 12;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte RED = 16;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte PALE_BLUE = 20;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte GRAY_2 = 24;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte DARK_GREEN = 28;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte WHITE = 32;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte LIGHT_GRAY = 36;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte BROWN = 40;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte DARK_GRAY = 44;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte BLUE = 48;
    /**
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public static final byte DARK_BROWN = 52;

    /**
     * Resize an image to 128x128.
     *
     * @param image The image to resize.
     * @return The resized image.
     */
    @NotNull
    public static BufferedImage resizeImage(@Nullable Image image) {
        BufferedImage result = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = result.createGraphics();
        graphics.drawImage(image, 0, 0, 128, 128, null);
        graphics.dispose();
        return result;
    }

    /**
     * Convert an Image to a byte[] using the palette.
     *
     * @param image The image to convert.
     * @return A byte[] containing the pixels of the image.
     * @deprecated use color-related methods
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
    @NotNull
    public static byte[] imageToBytes(@NotNull Image image) {
        BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = temp.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        int[] pixels = new int[temp.getWidth() * temp.getHeight()];
        temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());

        byte[] result = new byte[temp.getWidth() * temp.getHeight()];
        for (int i = 0; i < pixels.length; i++) {
            result[i] = matchColor(new Color(pixels[i], true));
        }
        return result;
    }

    /**
     * Get the index of the closest matching color in the palette to the given
     * color.
     *
     * @param r The red component of the color.
     * @param b The blue component of the color.
     * @param g The green component of the color.
     * @return The index in the palette.
     * @deprecated use color-related methods
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
    public static byte matchColor(int r, int g, int b) {
        return matchColor(new Color(r, g, b));
    }

    /**
     * Get the index of the closest matching color in the palette to the given
     * color.
     *
     * @param color The Color to match.
     * @return The index in the palette.
     * @deprecated use color-related methods
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
    public static byte matchColor(@NotNull Color color) {
        if (color.getAlpha() < 128) return 0;

        if (mapColorCache != null && mapColorCache.isCached()) {
            return mapColorCache.matchColor(color);
        }

        int index = 0;
        double best = -1;

        for (int i = 4; i < colors.length; i++) {
            double distance = getDistance(color, colors[i]);
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }

        // Minecraft has 248 colors, some of which have negative byte representations
        return (byte) (index < 128 ? index : -129 + (index - 127));
    }

    /**
     * Get the value of the given color in the palette.
     *
     * @param index The index in the palette.
     * @return The Color of the palette entry.
     * @deprecated use color directly
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
    @NotNull
    public static Color getColor(byte index) {
        // Minecraft has 248 colors, some of which have negative byte representations
        return colors[index >= 0 ? index : index + 256];
    }

    private static MapColorCache mapColorCache;

    /**
     * Sets the given MapColorCache.
     *
     * @param mapColorCache The map color cache to set
     */
    public static void setMapColorCache(@NotNull MapColorCache mapColorCache) {
        Preconditions.checkState(MapPalette.mapColorCache == null, "Map color cache already set");

        MapPalette.mapColorCache = mapColorCache;
    }

    /**
     * Holds cached information for matching map colors of a given RBG color.
     */
    public interface MapColorCache {

        /**
         * Returns true if the MapColorCache has values cached, if not it will
         * return false.
         * A case where it might return false is when the cache is not build jet.
         *
         * @return true if this MapColorCache has values cached otherwise false
         */
        boolean isCached();

        /**
         * Get the cached index of the closest matching color in the palette to the given
         * color.
         *
         * @param color The Color to match.
         * @return The index in the palette.
         * @throws IllegalStateException if {@link #isCached()} returns false
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        byte matchColor(@NotNull Color color);
    }
}
