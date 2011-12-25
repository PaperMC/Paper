package org.bukkit.map;

import java.util.HashMap;

/**
 * Represents a bitmap font drawable to a map.
 */
public class MapFont {

    private final HashMap<Character, CharacterSprite> chars = new HashMap<Character, CharacterSprite>();
    private int height = 0;
    protected boolean malleable = true;

    /**
     * Set the sprite for a given character.
     *
     * @param ch The character to set the sprite for.
     * @param sprite The CharacterSprite to set.
     * @throws IllegalStateException if this font is static.
     */
    public void setChar(char ch, CharacterSprite sprite) {
        if (!malleable) {
            throw new IllegalStateException("this font is not malleable");
        }

        chars.put(ch, sprite);
        if (sprite.getHeight() > height) {
            height = sprite.getHeight();
        }
    }

    /**
     * Get the sprite for a given character.
     *
     * @param ch The character to get the sprite for.
     * @return The CharacterSprite associated with the character, or null if there is none.
     */
    public CharacterSprite getChar(char ch) {
        return chars.get(ch);
    }

    /**
     * Get the width of the given text as it would be rendered using this font.
     *
     * @param text The text.
     * @return The width in pixels.
     */
    public int getWidth(String text) {
        if (!isValid(text)) {
            throw new IllegalArgumentException("text contains invalid characters");
        }

        int result = 0;
        for (int i = 0; i < text.length(); ++i) {
            result += chars.get(text.charAt(i)).getWidth();
        }
        return result;
    }

    /**
     * Get the height of this font.
     *
     * @return The height of the font.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Check whether the given text is valid.
     *
     * @param text The text.
     * @return True if the string contains only defined characters, false otherwise.
     */
    public boolean isValid(String text) {
        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '\u00A7' || ch == '\n') continue;
            if (chars.get(ch) == null) return false;
        }
        return true;
    }

    /**
     * Represents the graphics for a single character in a MapFont.
     */
    public static class CharacterSprite {

        private final int width;
        private final int height;
        private final boolean[] data;

        public CharacterSprite(int width, int height, boolean[] data) {
            this.width = width;
            this.height = height;
            this.data = data;

            if (data.length != width * height) {
                throw new IllegalArgumentException("size of data does not match dimensions");
            }
        }

        /**
         * Get the value of a pixel of the character.
         *
         * @param row The row, in the range [0,8).
         * @param col The column, in the range [0,8).
         * @return True if the pixel is solid, false if transparent.
         */
        public boolean get(int row, int col) {
            if (row < 0 || col < 0 || row >= height || col >= width) return false;
            return data[row * width + col];
        }

        /**
         * Get the width of the character sprite.
         *
         * @return The width of the character.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Get the height of the character sprite.
         *
         * @return The height of the character.
         */
        public int getHeight() {
            return height;
        }

    }

}
