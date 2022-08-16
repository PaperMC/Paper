package org.bukkit.map;

import java.awt.Color;
import java.awt.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a canvas for drawing to a map. Each canvas is associated with a
 * specific {@link MapRenderer} and represents that renderer's layer on the
 * map.
 */
public interface MapCanvas {

    /**
     * Get the map this canvas is attached to.
     *
     * @return The MapView this canvas is attached to.
     */
    @NotNull
    public MapView getMapView();

    /**
     * Get the cursor collection associated with this canvas.
     *
     * @return The MapCursorCollection associated with this canvas.
     */
    @NotNull
    public MapCursorCollection getCursors();

    /**
     * Set the cursor collection associated with this canvas. This does not
     * usually need to be called since a MapCursorCollection is already
     * provided.
     *
     * @param cursors The MapCursorCollection to associate with this canvas.
     */
    public void setCursors(@NotNull MapCursorCollection cursors);

    /**
     * Draw a pixel to the canvas.
     * <p>
     * The provided color might be converted to another color,
     * which is in the map color range. This means, that
     * {@link #getPixelColor(int, int)} might return another
     * color than set.
     *
     * If null is used as color, then the color returned by
     * {@link #getBasePixelColor(int, int)} is shown on the map.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @param color The color.
     */
    void setPixelColor(int x, int y, @Nullable Color color);

    /**
     * Get a pixel from the canvas.
     *
     * If no color is set at the given position for this canvas, then null is
     * returned and the color returned by {@link #getBasePixelColor(int, int)}
     * is shown on the map.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @return The color, or null if no color is set.
     */
    @Nullable
    Color getPixelColor(int x, int y);

    /**
     * Get a pixel from the layers below this canvas.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @return The color.
     */
    @NotNull
    Color getBasePixelColor(int x, int y);

    /**
     * Draw a pixel to the canvas.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @param color The color. See {@link MapPalette}.
     * @deprecated Magic value, use {@link #setPixelColor(int, int, Color)}
     */
    public void setPixel(int x, int y, byte color);

    /**
     * Get a pixel from the canvas.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @return The color. See {@link MapPalette}.
     * @deprecated Magic value, use {@link #getPixelColor(int, int)}
     */
    @Deprecated
    public byte getPixel(int x, int y);

    /**
     * Get a pixel from the layers below this canvas.
     *
     * @param x The x coordinate, from 0 to 127.
     * @param y The y coordinate, from 0 to 127.
     * @return The color. See {@link MapPalette}.
     * @deprecated Magic value, use {@link #getBasePixelColor(int, int)}
     */
    @Deprecated
    public byte getBasePixel(int x, int y);

    /**
     * Draw an image to the map. The image will be clipped if necessary.
     *
     * @param x The x coordinate of the image.
     * @param y The y coordinate of the image.
     * @param image The Image to draw.
     */
    public void drawImage(int x, int y, @NotNull Image image);

    /**
     * Render text to the map using fancy formatting. Newline (\n) characters
     * will move down one line and return to the original column, and the text
     * color can be changed using sequences such as "§12;", replacing 12 with
     * the palette index of the color (see {@link MapPalette}).
     *
     * @param x The column to start rendering on.
     * @param y The row to start rendering on.
     * @param font The font to use.
     * @param text The formatted text to render.
     */
    public void drawText(int x, int y, @NotNull MapFont font, @NotNull String text);

}
