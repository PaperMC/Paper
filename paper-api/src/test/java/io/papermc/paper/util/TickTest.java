package io.papermc.paper.util;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TickTest {

    @Test
    public void testTickLength() {
        assertEquals(50, Duration.of(1, Tick.tick()).toMillis());
        assertEquals(100, Duration.of(2, Tick.tick()).toMillis());
    }

    @Test
    public void testTickFromDuration() {
        assertEquals(0, Tick.tick().fromDuration(Duration.ofMillis(0)));
        assertEquals(0, Tick.tick().fromDuration(Duration.ofMillis(10)));
        assertEquals(1, Tick.tick().fromDuration(Duration.ofMillis(60)));
        assertEquals(2, Tick.tick().fromDuration(Duration.ofMillis(100)));
    }

    @Test
    public void testAddTickToInstant() {
        Instant now = Instant.now();
        assertEquals(now, now.plus(0, Tick.tick()));
        assertEquals(now.plus(50, ChronoUnit.MILLIS), now.plus(1, Tick.tick()));
        assertEquals(now.plus(100, ChronoUnit.MILLIS), now.plus(2, Tick.tick()));
        assertEquals(now.plus(150, ChronoUnit.MILLIS), now.plus(3, Tick.tick()));
    }

    @Test
    public void testTicksBetweenInstants() {
        Instant now = Instant.now();
        assertEquals(0, now.until(now.plus(20, ChronoUnit.MILLIS), Tick.tick()));
        assertEquals(1, now.until(now.plus(50, ChronoUnit.MILLIS), Tick.tick()));
        assertEquals(1, now.until(now.plus(60, ChronoUnit.MILLIS), Tick.tick()));
        assertEquals(2, now.until(now.plus(100, ChronoUnit.MILLIS), Tick.tick()));
    }
}
