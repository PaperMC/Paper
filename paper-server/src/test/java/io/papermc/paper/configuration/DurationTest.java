package io.papermc.paper.configuration;

import io.papermc.paper.configuration.type.Duration;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Normal
class DurationTest {

    @Test
    void testBasicUnits() {
        assertEquals(10, Duration.of("10s").seconds());
        assertEquals(1500, Duration.of("25m").seconds());
        assertEquals(43200, Duration.of("12h").seconds());
        assertEquals(172800, Duration.of("2d").seconds());
    }

    @Test
    void testDecimals() {
        assertEquals(5400, Duration.of("1.5h").seconds());
    }

    @Test
    void testWhitespaceIgnored() {
        assertEquals(3600, Duration.of("  1h ").seconds());
    }

    @Test
    void testUppercaseUnit() {
        assertEquals(36000, Duration.of("10H").seconds());
    }

    @Test
    void testCompoundDuration() {
        assertEquals(5400, Duration.of("1h30m").seconds());
        assertEquals(3661, Duration.of("1h1m1s").seconds());
    }

    @Test
    void testEmptyStringRejected() {
        assertThrows(IllegalArgumentException.class, () -> Duration.of(""));
        assertThrows(IllegalArgumentException.class, () -> Duration.of("   "));
    }

    @Test
    void testInvalidInputRejected() {
        assertThrows(IllegalArgumentException.class, () -> Duration.of("abc"));
        assertThrows(IllegalArgumentException.class, () -> Duration.of("1h30"));
    }
}
