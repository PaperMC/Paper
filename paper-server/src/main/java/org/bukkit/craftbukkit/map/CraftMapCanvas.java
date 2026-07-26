package org.bukkit.craftbukkit.map;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapFont.CharacterSprite;
import org.bukkit.map.MapPalette;

public class CraftMapCanvas implements MapCanvas {

    private final byte[] buffer = new byte[128 * 128];
    private final CraftMapView mapView;
    private byte[] base;
    private MapCursorCollection cursors = new MapCursorCollection();

    protected CraftMapCanvas(CraftMapView mapView) {
        this.mapView = mapView;
        Arrays.fill(this.buffer, (byte) -1);
    }

    @Override
    public CraftMapView getMapView() {
        return this.mapView;
    }

    @Override
    public MapCursorCollection getCursors() {
        return this.cursors;
    }

    @Override
    public void setCursors(MapCursorCollection cursors) {
        this.cursors = cursors;
    }

    @Override
    public void setPixelColor(int x, int y, Color color) {
        this.setPixel(x, y, (color == null) ? -1 : MapPalette.matchColor(color));
    }

    @Override
    public Color getPixelColor(int x, int y) {
        byte pixel = this.getPixel(x, y);
        if (pixel == -1) {
            return null;
        }

        return MapPalette.getColor(pixel);
    }

    @Override
    public Color getBasePixelColor(int x, int y) {
        return MapPalette.getColor(this.getBasePixel(x, y));
    }

    @Override
    public void setPixel(int x, int y, byte color) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128)
            return;
        if (this.buffer[y * 128 + x] != color) {
            this.buffer[y * 128 + x] = color;
            this.mapView.worldMap.setColorsDirty(x, y, false); // Paper - Fix unnecessary map data saves
        }
    }

    @Override
    public byte getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128)
            return 0;
        return this.buffer[y * 128 + x];
    }

    @Override
    public byte getBasePixel(int x, int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128)
            return 0;
        return this.base[y * 128 + x];
    }

    protected void setBase(byte[] base) {
        this.base = base;
    }

    protected byte[] getBuffer() {
        return this.buffer;
    }

    @Override
    public void drawImage(int x, int y, Image image) {
        // Paper start - Reduce work done by limiting size of image and using System.arraycopy
        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);

        // The source x value *may* be negative, meaning we'd need to "offset" the source image before drawing it.
        final int sourceX = Math.max(-x, 0);
        final int sourceY = Math.max(-y, 0);
        final int destX = Math.max(x, 0);
        final int destY = Math.max(y, 0);

        // The effective width/height to draw on the canvas.
        final int effectiveWidth = Math.min(imageWidth - sourceX, 128 - destX);
        final int effectiveHeight = Math.min(imageHeight - sourceY, 128 - destY);

        if (effectiveWidth <= 0 || effectiveHeight <= 0)
            return;

        // Create a subimage if the image is larger than the max allowed size
        BufferedImage temp;
        if (imageWidth >= effectiveWidth && image instanceof BufferedImage bImage) {
            // If the image is larger than the max allowed size, get a subimage, otherwise use the image as is
            if (imageWidth > effectiveWidth || imageHeight > effectiveHeight) {
                temp = bImage.getSubimage(sourceX, sourceY, effectiveWidth, effectiveHeight);
            } else {
                temp = bImage;
            }
        } else {
            temp = new BufferedImage(effectiveWidth, effectiveHeight, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D graphics = temp.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
        }

        byte[] bytes = MapPalette.imageToBytes(temp);

        // Since we now control the size of the image, we can safely use System.arraycopy
        // If x is 0, we can just copy the entire image as width is 128 and height is <=(128-y)
        if (x == 0 && effectiveWidth == 128) { // This only works great if the width is 128, otherwise an empty area appears
            System.arraycopy(bytes, 0, this.buffer, destY * effectiveWidth, effectiveWidth * effectiveHeight);
        } else {
            for (int yToCopy = 0; yToCopy < effectiveHeight; ++yToCopy) {
                final int src = yToCopy * effectiveWidth;
                final int dest = (destY + yToCopy) * 128 + destX;

                System.arraycopy(bytes, src, this.buffer, dest, effectiveWidth);
            }
        }

        // Mark all colors within the image as dirty
        this.mapView.worldMap.setColorsDirty(destX, destY, false);
        this.mapView.worldMap.setColorsDirty(destX + effectiveWidth - 1, destY + effectiveHeight - 1, false);
        // Paper end
    }

    @Override
    public void drawText(int x, int y, MapFont font, String text) {
        int xStart = x;
        byte color = MapPalette.DARK_GRAY;
        Preconditions.checkArgument(font.isValid(text), "text (%s) contains invalid characters", text);

        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                x = xStart;
                y += font.getHeight() + 1;
                continue;
            } else if (ch == '§') {
                int j = text.indexOf(';', i);
                Preconditions.checkArgument(j >= 0, "text (%s) unterminated color string", text);
                try {
                    color = Byte.parseByte(text.substring(i + 1, j));
                    i = j;
                    continue;
                } catch (NumberFormatException ignored) {
                }
            }

            CharacterSprite sprite = font.getChar(text.charAt(i));
            for (int r = 0; r < font.getHeight(); ++r) {
                for (int c = 0; c < sprite.getWidth(); ++c) {
                    if (sprite.get(r, c)) {
                        this.setPixel(x + c, y + r, color);
                    }
                }
            }
            x += sprite.getWidth() + 1;
        }
    }

}
