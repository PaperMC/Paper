package org.bukkit.map;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents all the map cursors on a {@link MapCanvas}. Like MapCanvas, a
 * MapCursorCollection is linked to a specific {@link MapRenderer}.
 *
 * @since 1.0.0
 */
public final class MapCursorCollection {
    private List<MapCursor> cursors = new ArrayList<MapCursor>();

    /**
     * Get the amount of cursors in this collection.
     *
     * @return The size of this collection.
     */
    public int size() {
        return cursors.size();
    }

    /**
     * Get a cursor from this collection.
     *
     * @param index The index of the cursor.
     * @return The MapCursor.
     */
    @NotNull
    public MapCursor getCursor(int index) {
        return cursors.get(index);
    }

    /**
     * Remove a cursor from the collection.
     *
     * @param cursor The MapCursor to remove.
     * @return Whether the cursor was removed successfully.
     */
    public boolean removeCursor(@NotNull MapCursor cursor) {
        return cursors.remove(cursor);
    }

    /**
     * Add a cursor to the collection.
     *
     * @param cursor The MapCursor to add.
     * @return The MapCursor that was passed.
     */
    @NotNull
    public MapCursor addCursor(@NotNull MapCursor cursor) {
        cursors.add(cursor);
        return cursor;
    }

    /**
     * Add a cursor to the collection.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @return The newly added MapCursor.
     */
    @NotNull
    public MapCursor addCursor(int x, int y, byte direction) {
        return addCursor(x, y, direction, (byte) 0, true);
    }

    /**
     * Add a cursor to the collection.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @return The newly added MapCursor.
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    @NotNull
    public MapCursor addCursor(int x, int y, byte direction, byte type) {
        return addCursor(x, y, direction, type, true);
    }

    /**
     * Add a cursor to the collection.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible.
     * @return The newly added MapCursor.
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    @NotNull
    public MapCursor addCursor(int x, int y, byte direction, byte type, boolean visible) {
        return addCursor(new MapCursor((byte) x, (byte) y, direction, type, visible));
    }

    /**
     * Add a cursor to the collection.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible.
     * @param caption banner caption
     * @return The newly added MapCursor.
     * @deprecated Magic value
     * @since 1.13
     */
    @Deprecated(since = "1.13")
    @NotNull
    public MapCursor addCursor(int x, int y, byte direction, byte type, boolean visible, @Nullable String caption) {
        return addCursor(new MapCursor((byte) x, (byte) y, direction, type, visible, caption));
    }
    // Paper start
    /**
     * Add a cursor to the collection.
     *
     * @param x The x coordinate, from -128 to 127.
     * @param y The y coordinate, from -128 to 127.
     * @param direction The facing of the cursor, from 0 to 15.
     * @param type The type (color/style) of the map cursor.
     * @param visible Whether the cursor is visible.
     * @param caption banner caption
     * @return The newly added MapCursor.
     * @deprecated Magic value
     * @since 1.16.5
     */
    @Deprecated
    public @NotNull MapCursor addCursor(int x, int y, byte direction, byte type, boolean visible, net.kyori.adventure.text.@Nullable Component caption) {
        return addCursor(new MapCursor((byte) x, (byte) y, direction, type, visible, caption));
    }
    // Paper end
}
