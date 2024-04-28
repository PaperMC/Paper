package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.Map;
import org.bukkit.map.MapCursor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds a list of markers to be placed on a Filled Map (used for Explorer Maps).
 * @see io.papermc.paper.datacomponent.DataComponentTypes#MAP_DECORATIONS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface MapDecorations {

    @Contract(value = "_ -> new", pure = true)
    static MapDecorations mapDecorations(final Map<String, DecorationEntry> entries) {
        return mapDecorations().putAll(entries).build();
    }

    @Contract(value = "-> new", pure = true)
    static MapDecorations.Builder mapDecorations() {
        return ItemComponentTypesBridge.bridge().mapDecorations();
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static DecorationEntry decorationEntry(final MapCursor.Type type, final double x, final double z, final float rotation) {
        return ItemComponentTypesBridge.bridge().decorationEntry(type, x, z, rotation);
    }

    /**
     * Gets the decoration entry with the given id.
     *
     * @param id id
     * @return decoration entry, or {@code null} if not present
     */
    @Contract(pure = true)
    @Nullable DecorationEntry decoration(String id);

    /**
     * Gets the decoration entries.
     *
     * @return the decoration entries
     */
    @Contract(pure = true)
    @Unmodifiable Map<String, DecorationEntry> decorations();

    /**
     * Decoration present on the map.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface DecorationEntry {

        /**
         * Type of decoration.
         *
         * @return type
         */
        @Contract(pure = true)
        MapCursor.Type type();

        /**
         * X world coordinate of the decoration.
         *
         * @return x coordinate
         */
        @Contract(pure = true)
        double x();

        /**
         * Z world coordinate of the decoration.
         *
         * @return z coordinate
         */
        @Contract(pure = true)
        double z();

        /**
         * Clockwise rotation from north in degrees.
         *
         * @return rotation
         */
        @Contract(pure = true)
        float rotation();
    }

    /**
     * Builder for {@link MapDecorations}.
     */
    @ApiStatus.NonExtendable
    @ApiStatus.Experimental
    interface Builder extends DataComponentBuilder<MapDecorations> {

        /**
         * Puts the decoration with the given id in this builder.
         *
         * @param id id
         * @param entry decoration
         * @return the builder for chaining
         * @see #decorations()
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        MapDecorations.Builder put(String id, DecorationEntry entry);

        /**
         * Puts all the decoration with the given id in this builder.
         *
         * @param entries decorations
         * @return the builder for chaining
         * @see #decorations()
         */
        @Contract(value = "_ -> this", mutates = "this")
        MapDecorations.Builder putAll(Map<String, DecorationEntry> entries);
    }
}
