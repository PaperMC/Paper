package io.papermc.paper.world;

import java.util.Map;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

/**
 * Represents a timeline that can apply environment attributes or define time markers for a {@link WorldClock}.
 *
 * @see Timelines
 */
@ApiStatus.NonExtendable
public interface Timeline extends Keyed {

    /**
     * Gets the clock that drives this timeline.
     *
     * @return the clock
     */
    WorldClock clock();

    /**
     * Gets the period of this timeline in ticks, if defined.
     *
     * @return the period in ticks, or an empty {@link OptionalInt} if not defined
     */
    OptionalInt periodTicks();

    /**
     * Gets the time markers defined by this timeline.
     *
     * @return an immutable map of time marker keys to time markers
     */
    @Unmodifiable Map<Key, TimeMarker> timeMarkers();

    /**
     * A time marker defined by a timeline.
     */
    @ApiStatus.NonExtendable
    interface TimeMarker {

        /**
         * Gets the tick this marker occurs on.
         *
         * @return the marker tick
         */
        @NonNegative int ticks();

        /**
         * Gets whether this marker should be suggested in commands.
         *
         * @return whether this marker should be suggested in commands
         */
        boolean showInCommands();

        /**
         * Creates a new time marker instance based on the passed values.
         *
         * @param ticks          the tick this marker occurs on, as returned by {@link #ticks()}
         * @param showInCommands whether the marker should be suggested in commands, as returned by {@link #showInCommands()}
         * @return the created instance
         */
        @Contract(value = "_, _ -> new", pure = true)
        static TimeMarker of(@NonNegative final int ticks, final boolean showInCommands) {
            @SuppressWarnings("checkstyle:MissingJavadocType")
            record Impl(int ticks, boolean showInCommands) implements TimeMarker {
            }

            return new Impl(requireNonNegative(ticks, "ticks"), showInCommands);
        }
    }
}
