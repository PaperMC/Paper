package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.Timeline;
import io.papermc.paper.world.WorldClock;
import java.util.Map;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

/**
 * A data-centric version-specific registry entry for the {@link Timeline} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface TimelineRegistryEntry {

    /**
     * Gets the clock that drives this timeline.
     *
     * @return the clock
     */
    @Contract(pure = true)
    TypedKey<WorldClock> clock();

    /**
     * Gets the timeline period, if any.
     *
     * @return the timeline period
     */
    @Contract(pure = true)
    OptionalInt periodTicks();

    /**
     * Gets the time markers defined by this timeline.
     *
     * @return the time markers
     */
    @Contract(pure = true)
    Map<Key, TimeMarker> timeMarkers();

    /**
     * A mutable builder for the {@link TimelineRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #clock(TypedKey)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends TimelineRegistryEntry, RegistryBuilder<Timeline> {

        /**
         * Sets the clock that drives this timeline.
         *
         * @param clock the clock
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder clock(TypedKey<WorldClock> clock);

        /**
         * Sets the timeline period.
         *
         * @param periodTicks the period in ticks
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder periodTicks(@Range(from = 1, to = Integer.MAX_VALUE) int periodTicks);

        /**
         * Clears the timeline period.
         *
         * @return this builder
         */
        @Contract(value = "-> this", mutates = "this")
        Builder clearPeriodTicks();

        /**
         * Adds or replaces a time marker on this timeline.
         *
         * @param key            the time marker key
         * @param ticks          the marker tick
         * @param showInCommands whether the marker should be suggested in commands
         * @return this builder
         */
        @Contract(value = "_, _, _ -> this", mutates = "this")
        Builder addTimeMarker(Key key, @Range(from = 0, to = Integer.MAX_VALUE) int ticks, boolean showInCommands);
    }

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
        @Range(from = 0, to = Integer.MAX_VALUE)
        int ticks();

        /**
         * Gets whether this marker should be suggested in commands.
         *
         * @return whether the marker should be suggested in commands
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
        static TimeMarker of(@Range(from = 0, to = Integer.MAX_VALUE) final int ticks, final boolean showInCommands) {
            record Impl(int ticks, boolean showInCommands) implements TimeMarker {
            }

            return new Impl(requireNonNegative(ticks, "ticks"), showInCommands);
        }
    }
}
