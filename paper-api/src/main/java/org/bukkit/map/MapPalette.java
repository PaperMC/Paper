package org.bukkit.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Represents the palette that map items use.
 */
public final class MapPalette {

    // Internal mechanisms
    private MapPalette() {}

    private static Color c(int r, int g, int b) {
        return new Color(r, g, b);
    }

    private static double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private static final Color[] colors = {
        new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
        new Color(0, 0, 0, 0), new Color(0, 0, 0, 0),
        c(89,125,39), c(109,153,48), c(27,178,56), c(109,153,48),
        c(174,164,115), c(213,201,140), c(247,233,163), c(213,201,140),
        c(117,117,117), c(144,144,144), c(167,167,167), c(144,144,144),
        c(180,0,0), c(220,0,0), c(255,0,0), c(220,0,0),
        c(112,112,180), c(138,138,220), c(160,160,255), c(138,138,220),
        c(117,117,117), c(144,144,144), c(167,167,167), c(144,144,144),
        c(0,87,0), c(0,106,0), c(0,124,0), c(0,106,0),
        c(180,180,180), c(220,220,220), c(255,255,255), c(220,220,220),
        c(115,118,129), c(141,144,158), c(164,168,184), c(141,144,158),
        c(129,74,33), c(157,91,40), c(183,106,47), c(157,91,40),
        c(79,79,79), c(96,96,96), c(112,112,112), c(96,96,96),
        c(45,45,180), c(55,55,220), c(64,64,255), c(55,55,220),
        c(73,58,35), c(89,71,43), c(104,83,50), c(89,71,43)
    };

    // Interface
    /**
     * The base color ranges. Each entry corresponds to four colors of varying
     * shades with values entry to entry + 3.
     */
    public static final byte TRANSPARENT = 0;
    public static final byte LIGHT_GREEN = 4;
    public static final byte LIGHT_BROWN = 8;
    public static final byte GRAY_1 = 12;
    public static final byte RED = 16;
    public static final byte PALE_BLUE = 20;
    public static final byte GRAY_2 = 24;
    public static final byte DARK_GREEN = 28;
    public static final byte WHITE = 32;
    public static final byte LIGHT_GRAY = 36;
    public static final byte BROWN = 40;
    public static final byte DARK_GRAY = 44;
    public static final byte BLUE = 48;
    public static final byte DARK_BROWN = 52;

    /**
     * Resize an image to 128x128.
     *
     * @param image The image to resize.
     * @return The resized image.
     */
    public static BufferedImage resizeImage(Image image) {
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
     */
    public static byte[] imageToBytes(Image image) {
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
     * Get the index of the closest matching color in the palette to the given color.
     *
     * @param r The red component of the color.
     * @param b The blue component of the color.
     * @param g The green component of the color.
     * @return The index in the palette.
     */
    public static byte matchColor(int r, int g, int b) {
        return matchColor(new Color(r, g, b));
    }

    /**
     * Get the index of the closest matching color in the palette to the given color.
     *
     * @param color The Color to match.
     * @return The index in the palette.
     */
    public static byte matchColor(Color color) {
        if (color.getAlpha() < 128) return 0;

        int index = 0;
        double best = -1;

        for (int i = 4; i < colors.length; i++) {
            double distance = getDistance(color, colors[i]);
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }

        return (byte) index;
    }

    /**
     * Get the value of the given color in the palette.
     *
     * @param index The index in the palette.
     * @return The Color of the palette entry.
     */
    public static Color getColor(byte index) {
        if (index < 0 || index >= colors.length) {
            throw new IndexOutOfBoundsException();
        } else {
            return colors[index];
        }
    }

}
