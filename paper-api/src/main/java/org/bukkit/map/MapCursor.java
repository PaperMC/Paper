package org.bukkit.map;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a cursor on a map.
 */
public final class MapCursor {
    private byte x, y;
    private byte direction;
    private boolean visible;
    private String caption;
    private Type type;

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
     * @deprecated Magic value, use {@link #MapCursor(byte, byte, byte, Type, boolean, String)}
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
        this.type = type;
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
        return type;
    }

    /**
     * Get the type of this cursor.
     *
     * @return The type (color/style) of the map cursor.
     * @deprecated Magic value
     */
    @Deprecated
    public byte getRawType() {
        return type.value;
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
        Preconditions.checkArgument(direction >= 0 && direction <= 15, "direction must be between 0 and 15 but is %s", direction);
        this.direction = direction;
    }

    /**
     * Set the type of this cursor.
     *
     * @param type The type (color/style) of the map cursor.
     */
    public void setType(@NotNull Type type) {
        this.type = type;
    }

    /**
     * Set the type of this cursor.
     *
     * @param type The type (color/style) of the map cursor.
     * @deprecated Magic value
     */
    @Deprecated
    public void setRawType(byte type) {
        Type enumType = Type.byValue(type);
        Preconditions.checkArgument(enumType != null, "Unknown type by id %s", type);
        this.type = enumType;
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
     * index in the file './assets/minecraft/textures/map/map_icons.png' from minecraft.jar or from a
     * resource pack.
     */
    public enum Type implements Keyed {
        PLAYER(0, "player"),
        FRAME(1, "frame"),
        RED_MARKER(2, "red_marker"),
        BLUE_MARKER(3, "blue_marker"),
        TARGET_X(4, "target_x"),
        TARGET_POINT(5, "target_point"),
        PLAYER_OFF_MAP(6, "player_off_map"),
        PLAYER_OFF_LIMITS(7, "player_off_limits"),
        MANSION(8, "mansion"),
        MONUMENT(9, "monument"),
        BANNER_WHITE(10, "banner_white"),
        BANNER_ORANGE(11, "banner_orange"),
        BANNER_MAGENTA(12, "banner_magenta"),
        BANNER_LIGHT_BLUE(13, "banner_light_blue"),
        BANNER_YELLOW(14, "banner_yellow"),
        BANNER_LIME(15, "banner_lime"),
        BANNER_PINK(16, "banner_pink"),
        BANNER_GRAY(17, "banner_gray"),
        BANNER_LIGHT_GRAY(18, "banner_light_gray"),
        BANNER_CYAN(19, "banner_cyan"),
        BANNER_PURPLE(20, "banner_purple"),
        BANNER_BLUE(21, "banner_blue"),
        BANNER_BROWN(22, "banner_brown"),
        BANNER_GREEN(23, "banner_green"),
        BANNER_RED(24, "banner_red"),
        BANNER_BLACK(25, "banner_black"),
        RED_X(26, "red_x"),
        VILLAGE_DESERT(27, "village_desert"),
        VILLAGE_PLAINS(28, "village_plains"),
        VILLAGE_SAVANNA(29, "village_savanna"),
        VILLAGE_SNOWY(30, "village_snowy"),
        VILLAGE_TAIGA(31, "village_taiga"),
        JUNGLE_TEMPLE(32, "jungle_temple"),
        SWAMP_HUT(33, "swamp_hut"),
        TRIAL_CHAMBERS(34, "trial_chambers")
        ;

        private final byte value;
        private final NamespacedKey key;

        Type(int value, String key) {
            this.value = (byte) value;
            this.key = NamespacedKey.minecraft(key);
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return key;
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
