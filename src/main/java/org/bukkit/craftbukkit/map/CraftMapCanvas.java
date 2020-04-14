package org.bukkit.craftbukkit.map;

import java.awt.Image;
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
        Arrays.fill(buffer, (byte) -1);
    }

    @Override
    public CraftMapView getMapView() {
        return mapView;
    }

    @Override
    public MapCursorCollection getCursors() {
        return cursors;
    }

    @Override
    public void setCursors(MapCursorCollection cursors) {
        this.cursors = cursors;
    }

    @Override
    public void setPixel(int x, int y, byte color) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128)
            return;
        if (buffer[y * 128 + x] != color) {
            buffer[y * 128 + x] = color;
            mapView.worldMap.flagDirty(x, y);
        }
    }

    @Override
    public byte getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128)
            return 0;
        return buffer[y * 128 + x];
    }

    @Override
    public byte getBasePixel(int x, int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128)
            return 0;
        return base[y * 128 + x];
    }

    protected void setBase(byte[] base) {
        this.base = base;
    }

    protected byte[] getBuffer() {
        return buffer;
    }

    @Override
    public void drawImage(int x, int y, Image image) {
        byte[] bytes = MapPalette.imageToBytes(image);
        for (int x2 = 0; x2 < image.getWidth(null); ++x2) {
            for (int y2 = 0; y2 < image.getHeight(null); ++y2) {
                setPixel(x + x2, y + y2, bytes[y2 * image.getWidth(null) + x2]);
            }
        }
    }

    @Override
    public void drawText(int x, int y, MapFont font, String text) {
        int xStart = x;
        byte color = MapPalette.DARK_GRAY;
        if (!font.isValid(text)) {
            throw new IllegalArgumentException("text contains invalid characters");
        }

        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                x = xStart;
                y += font.getHeight() + 1;
                continue;
            } else if (ch == '\u00A7') {
                int j = text.indexOf(';', i);
                if (j >= 0) {
                    try {
                        color = Byte.parseByte(text.substring(i + 1, j));
                        i = j;
                        continue;
                    } catch (NumberFormatException ex) {
                    }
                }
                throw new IllegalArgumentException("Text contains unterminated color string");
            }

            CharacterSprite sprite = font.getChar(text.charAt(i));
            for (int r = 0; r < font.getHeight(); ++r) {
                for (int c = 0; c < sprite.getWidth(); ++c) {
                    if (sprite.get(r, c)) {
                        setPixel(x + c, y + r, color);
                    }
                }
            }
            x += sprite.getWidth() + 1;
        }
    }

}
