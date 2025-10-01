package org.bukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.entity.Shearable;
import io.papermc.paper.world.WeatheringCopperState;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CopperGolem extends Golem, Shearable {

    /**
     * Get the current weathering state of the copper golem.
     *
     * @return the weathering state
     */
    WeatheringCopperState getWeatheringState();

    /**
     * Set the weathering state of the copper golem.
     *
     * @param state the new weathering state
     */
    void setWeatheringState(WeatheringCopperState state);

    /**
     * Get the current oxidizing state of the copper golem.
     *
     * @return the oxidizing state
     */
    Oxidizing getOxidizing();

    /**
     * Set the oxidizing state of the copper golem.
     *
     * @param oxidizing the new oxidizing state
     */
    void setOxidizing(Oxidizing oxidizing);

    /**
     * Represents the oxidizing state of a copper golem.
     *
     * <ul>
     *     <li>{@link Oxidizing.Waxed}: the golem is waxed and will not oxidize further</li>
     *     <li>{@link Oxidizing.Unset}: the golem is not waxed but has not yet had its next weathering event 'scheduled'</li>
     *     <li>{@link Oxidizing.AtTime}: the absolute game time of the next weathering event</li>
     * </ul>
     */
    sealed interface Oxidizing permits Oxidizing.Waxed, Oxidizing.Unset, Oxidizing.AtTime {
        /**
         * Returns the {@link Waxed} instance, valid for object identity comparisons.
         *
         * @return the waxed instance
         */
        static Oxidizing waxed() {
            return Waxed.INSTANCE;
        }

        /**
         * Returns the {@link Unset} instance, valid for object identity comparisons.
         *
         * @return the unset instance
         */
        static Oxidizing unset() {
            return Unset.INSTANCE;
        }

        /**
         * Creates a new {@link AtTime} instance with the given time.
         *
         * @param time the absolute game time of the next weathering event
         * @return new AtTime instance
         * @see World#getGameTime()
         */
        static AtTime atTime(@Range(from = 0, to = Long.MAX_VALUE) final long time) {
            Preconditions.checkArgument(time >= 0, "time must be non-negative");
            return new AtTimeImpl(time);
        }

        /**
         * Waxed oxidizing state - the golem is waxed and will not oxidize further.
         *
         * @see #waxed()
         */
        final class Waxed implements Oxidizing {
            private static final Waxed INSTANCE = new Waxed();

            private Waxed() {
            }
        }

        /**
         * Unset oxidizing state - the golem is not waxed but has not yet had its next weathering event 'scheduled'.
         *
         * @see #unset()
         */
        final class Unset implements Oxidizing {
            private static final Unset INSTANCE = new Unset();

            private Unset() {
            }
        }

        /**
         * AtTime oxidizing state - the absolute game time of the next weathering event.
         *
         * @see #atTime(long)
         */
        sealed interface AtTime extends Oxidizing permits AtTimeImpl {
            /**
             * Gets the absolute game time of the next weathering event.
             *
             * @return the absolute game time of the next weathering event
             * @see World#getGameTime()
             */
            @Range(from = 0, to = Long.MAX_VALUE)
            long time();
        }

        /**
         * @hidden
         */
        @ApiStatus.Internal
        record AtTimeImpl(long time) implements AtTime {}
    }
}
