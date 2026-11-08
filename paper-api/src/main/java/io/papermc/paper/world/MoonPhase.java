package io.papermc.paper.world;

import java.util.HashMap;
import java.util.Map;

/**
 * The phases of the moon, which cycle every eight days.
 */
public enum MoonPhase {
    /**
     * The full moon.
     */
    FULL_MOON(0L),
    /**
     * The waning gibbous moon.
     */
    WANING_GIBBOUS(1L),
    /**
     * The last quarter moon.
     */
    LAST_QUARTER(2L),
    /**
     * The waning crescent moon.
     */
    WANING_CRESCENT(3L),
    /**
     * The new moon.
     */
    NEW_MOON(4L),
    /**
     * The waxing crescent moon.
     */
    WAXING_CRESCENT(5L),
    /**
     * The first quarter moon.
     */
    FIRST_QUARTER(6L),
    /**
     * The waxing gibbous moon.
     */
    WAXING_GIBBOUS(7L);

    private static final Map<Long, MoonPhase> BY_DAY = new HashMap<>();

    private final long day;

    MoonPhase(final long day) {
        this.day = day;
    }

    static {
        for (final MoonPhase phase : values()) {
            BY_DAY.put(phase.day, phase);
        }
    }

    /**
     * Gets the moon phase on the given day of the cycle.
     *
     * @param day the non-negative number of days since the first full moon
     * @return the moon phase on that day
     */
    public static MoonPhase getPhase(final long day) {
        return BY_DAY.get(day % 8L);
    }
}
