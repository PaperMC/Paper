package org.bukkit.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a cursor on a map.
 */
public final class MapCursor {
    private byte x, y;
    private byte direction, type;
    private boolean visible;
    private String caption;

    /**
     * Initialize the map cursor.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible by default.
     * @deprecated Magic value
     */
    @Deprecated
    public MapCursor(byte x, byte y, byte direction, byte type, boolean visible) {
        this(x, y, direction, type, visible, null);
    }

    /**
     * Initialize the map cursor.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible by default.
     */
    public MapCursor(byte x, byte y, byte direction, @NotNull Type type, boolean visible) {
        this(x, y, direction, type, visible, null);
    }

    /**
     * Initialize the map cursor.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible by default.
     * @param caption cursor caption
     * @deprecated Magic value
     */
    @Deprecated
    public MapCursor(byte x, byte y, byte direction, byte type, boolean visible, @Nullable String caption) {
        this.x = x;
        this.y = y;
        setDirection(direction);
        setRawType(type);
        this.visible = visible;
        this.caption = caption;
    }

    /**
     * Initialize the map cursor.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible by default.
     * @param caption cursor caption
     */
    public MapCursor(byte x, byte y, byte direction, @NotNull Type type, boolean visible, @Nullable String caption) {
        this.x = x;
        this.y = y;
        setDirection(direction);
        setType(type);
        this.visible = visible;
        this.caption = caption;
    }

    /**
     * Get the X position of this cursor.
     *
     * @return The X coordinate.
     */
    public byte getX() {
        return x;
    }

    /**
     * Get the Y position of this cursor.
     *
     * @return The Y coordinate.
     */
    public byte getY() {
        return y;
    }

    /**
     * Get the direction of this cursor.
     *
     * @return The facing of the cursor, from 0 to 15.
     */
    public byte getDirection() {
        return direction;
    }

    /**
     * Get the type of this cursor.
     *
     * @return The type (color/style) of the map cursor.
     */
    @NotNull
    public Type getType() {
        // It should be impossible to set type to something without appropriate Type, so this shouldn't return null
        return Type.byValue(type);
    }

    /**
     * Get the type of this cursor.
     *
     * @return The type (color/style) of the map cursor.
     * @deprecated Magic value
     */
    @Deprecated
    public byte getRawType() {
        return type;
    }

    /**
     * Get the visibility status of this cursor.
     *
     * @return True if visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set the X position of this cursor.
     *
     * @param x The X coordinate.
     */
    public void setX(byte x) {
        this.x = x;
    }

    /**
     * Set the Y position of this cursor.
     *
     * @param y The Y coordinate.
     */
    public void setY(byte y) {
        this.y = y;
    }

    /**
     * Set the direction of this cursor.
     *
     * @param direction The facing of the cursor, from 0 to 15.
     */
    public void setDirection(byte direction) {
        if (direction < 0 || direction > 15) {
            throw new IllegalArgumentException("Direction must be in the range 0-15");
        }
        this.direction = direction;
    }

    /**
     * Set the type of this cursor.
     *
     * @param type The type (color/style) of the map cursor.
     */
    public void setType(@NotNull Type type) {
        setRawType(type.value);
    }

    /**
     * Set the type of this cursor.
     *
     * @param type The type (color/style) of the map cursor.
     * @deprecated Magic value
     */
    @Deprecated
    public void setRawType(byte type) {
        if (type < 0 || type > 26) {
            throw new IllegalArgumentException("Type must be in the range 0-26");
        }
        this.type = type;
    }

    /**
     * Set the visibility status of this cursor.
     *
     * @param visible True if visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the caption on this cursor.
     *
     * @return caption
     */
    @Nullable
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption on this cursor.
     *
     * @param caption new caption
     */
    public void setCaption(@Nullable String caption) {
        this.caption = caption;
    }

    /**
     * Represents the standard types of map cursors. More may be made
     * available by resource packs - the value is used by the client as an
     * index in the file './misc/mapicons.png' from minecraft.jar or from a
     * resource pack.
     */
    public enum Type {
        WHITE_POINTER(0),
        GREEN_POINTER(1),
        RED_POINTER(2),
        BLUE_POINTER(3),
        WHITE_CROSS(4),
        RED_MARKER(5),
        WHITE_CIRCLE(6),
        SMALL_WHITE_CIRCLE(7),
        MANSION(8),
        TEMPLE(9),
        BANNER_WHITE(10),
        BANNER_ORANGE(11),
        BANNER_MAGENTA(12),
        BANNER_LIGHT_BLUE(13),
        BANNER_YELLOW(14),
        BANNER_LIME(15),
        BANNER_PINK(16),
        BANNER_GRAY(17),
        BANNER_LIGHT_GRAY(18),
        BANNER_CYAN(19),
        BANNER_PURPLE(20),
        BANNER_BLUE(21),
        BANNER_BROWN(22),
        BANNER_GREEN(23),
        BANNER_RED(24),
        BANNER_BLACK(25),
        RED_X(26);

        private byte value;

        private Type(int value) {
            this.value = (byte) value;
        }

        /**
         * Gets the internal value of the cursor.
         *
         * @return the value
         * @deprecated Magic value
         */
        @Deprecated
        public byte getValue() {
            return value;
        }

        /**
         * Get a cursor by its internal value.
         *
         * @param value the value
         * @return the matching type
         * @deprecated Magic value
         */
        @Deprecated
        @Nullable
        public static Type byValue(byte value) {
            for (Type t : values()) {
                if (t.value == value) return t;
            }
            return null;
        }
    }

}
