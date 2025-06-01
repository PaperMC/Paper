package org.bukkit.map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Locale;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a cursor on a map.
 */
public final class MapCursor {
    private byte x, y;
    private byte direction;
    private boolean visible;
    private net.kyori.adventure.text.Component caption; // Paper
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
    @Deprecated(since = "1.6.2")
    public MapCursor(byte x, byte y, byte direction, byte type, boolean visible) {
        this(x, y, direction, type, visible, (String) null); // Paper
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
        this(x, y, direction, type, visible, (String) null); // Paper
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
     * @deprecated Magic value. Use {@link #MapCursor(byte, byte, byte, Type, boolean, net.kyori.adventure.text.Component)}
     */
    @Deprecated(since = "1.13")
    public MapCursor(byte x, byte y, byte direction, byte type, boolean visible, @Nullable String caption) {
        this.x = x;
        this.y = y;
        setDirection(direction);
        setRawType(type);
        this.visible = visible;
        this.caption = caption == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(caption); // Paper
    }
    // Paper start
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
    public MapCursor(byte x, byte y, byte direction, byte type, boolean visible, net.kyori.adventure.text.@Nullable Component caption) {
        this.x = x; this.y = y; this.visible = visible; this.caption = caption;
        setDirection(direction);
        setRawType(type);
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
    public MapCursor(byte x, byte y, byte direction, @NotNull Type type, boolean visible, net.kyori.adventure.text.@Nullable Component caption) {
        this.x = x; this.y = y; this.visible = visible; this.caption = caption;
        setDirection(direction);
        setType(type);
    }
    // Paper end

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
        this.caption = caption == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(caption); // Paper
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
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public byte getRawType() {
        return type.getValue();
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
     * @deprecated use {@link #setType(Type)}
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
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

    // Paper start
    /**
     * Gets the caption on this cursor.
     *
     * @return caption
     */
    public net.kyori.adventure.text.@Nullable Component caption() {
        return this.caption;
    }
    /**
     * Sets the caption on this cursor.
     *
     * @param caption new caption
     */
    public void caption(net.kyori.adventure.text.@Nullable Component caption) {
        this.caption = caption;
    }
    // Paper end
    /**
     * Gets the caption on this cursor.
     *
     * @return caption
     * @deprecated in favour of {@link #caption()}
     */
    @Nullable
    @Deprecated // Paper
    public String getCaption() {
        return this.caption == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.caption); // Paper
    }

    /**
     * Sets the caption on this cursor.
     *
     * @param caption new caption
     * @deprecated in favour of {@link #caption(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setCaption(@Nullable String caption) {
        this.caption = caption == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(caption); // Paper
    }

    /**
     * Represents the standard types of map cursors. More may be made
     * available by resource packs - the value is used by the client as an
     * index in the file './assets/minecraft/textures/map/map_icons.png' from minecraft.jar or from a
     * resource pack.
     */
    public interface Type extends OldEnum<Type>, Keyed {

        // Start generate - MapCursorType
        // @GeneratedFrom 1.21.6-pre1
        Type BANNER_BLACK = getType("banner_black");

        Type BANNER_BLUE = getType("banner_blue");

        Type BANNER_BROWN = getType("banner_brown");

        Type BANNER_CYAN = getType("banner_cyan");

        Type BANNER_GRAY = getType("banner_gray");

        Type BANNER_GREEN = getType("banner_green");

        Type BANNER_LIGHT_BLUE = getType("banner_light_blue");

        Type BANNER_LIGHT_GRAY = getType("banner_light_gray");

        Type BANNER_LIME = getType("banner_lime");

        Type BANNER_MAGENTA = getType("banner_magenta");

        Type BANNER_ORANGE = getType("banner_orange");

        Type BANNER_PINK = getType("banner_pink");

        Type BANNER_PURPLE = getType("banner_purple");

        Type BANNER_RED = getType("banner_red");

        Type BANNER_WHITE = getType("banner_white");

        Type BANNER_YELLOW = getType("banner_yellow");

        Type BLUE_MARKER = getType("blue_marker");

        Type FRAME = getType("frame");

        Type JUNGLE_TEMPLE = getType("jungle_temple");

        Type MANSION = getType("mansion");

        Type MONUMENT = getType("monument");

        Type PLAYER = getType("player");

        Type PLAYER_OFF_LIMITS = getType("player_off_limits");

        Type PLAYER_OFF_MAP = getType("player_off_map");

        Type RED_MARKER = getType("red_marker");

        Type RED_X = getType("red_x");

        Type SWAMP_HUT = getType("swamp_hut");

        Type TARGET_POINT = getType("target_point");

        Type TARGET_X = getType("target_x");

        Type TRIAL_CHAMBERS = getType("trial_chambers");

        Type VILLAGE_DESERT = getType("village_desert");

        Type VILLAGE_PLAINS = getType("village_plains");

        Type VILLAGE_SAVANNA = getType("village_savanna");

        Type VILLAGE_SNOWY = getType("village_snowy");

        Type VILLAGE_TAIGA = getType("village_taiga");
        // End generate - MapCursorType

        @NotNull
        private static Type getType(@NotNull String key) {
            return Registry.MAP_DECORATION_TYPE.getOrThrow(NamespacedKey.minecraft(key));
        }

        /**
         * Gets the internal value of the cursor.
         *
         * @return the value
         * @apiNote Internal Use Only
         */
        @ApiStatus.Internal // Paper
        byte getValue();

        /**
         * Get a cursor by its internal value.
         *
         * @param value the value
         * @return the matching type
         * @apiNote Internal Use Only
         */
        @ApiStatus.Internal // Paper
        @Nullable
        static Type byValue(byte value) {
            for (Type t : values()) {
                if (t.getValue() == value) return t;
            }
            return null;
        }

        /**
         * @param name of the type.
         * @return the type with the given name.
         * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Type valueOf(@NotNull String name) {
            Type type = Registry.MAP_DECORATION_TYPE.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
            Preconditions.checkArgument(type != null, "No Type found with the name %s", name);
            return type;
        }

        /**
         * @return an array of all known map cursor types.
         * @deprecated use {@link Registry#iterator()}.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Type[] values() {
            return Lists.newArrayList(Registry.MAP_DECORATION_TYPE).toArray(new Type[0]);
        }
    }

}
