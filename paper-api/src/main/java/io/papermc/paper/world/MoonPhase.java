package io.papermc.paper.world;

import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum MoonPhase {
    FULL_MOON(0L),
    WANING_GIBBOUS(1L),
    LAST_QUARTER(2L),
    WANING_CRESCENT(3L),
    NEW_MOON(4L),
    WAXING_CRESCENT(5L),
    FIRST_QUARTER(6L),
    WAXING_GIBBOUS(7L);

    private final long day;

    MoonPhase(final long day) {
        this.day = day;
    }

    private static final Map<Long, MoonPhase> BY_DAY = new HashMap<>();

    static {
        for (final MoonPhase phase : values()) {
            BY_DAY.put(phase.day, phase);
        }
    }

    public static MoonPhase getPhase(final long day) {
        return BY_DAY.get(day % 8L);
    }
}
