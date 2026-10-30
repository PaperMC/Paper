package io.papermc.checkstyle.filters;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import com.puppycrawl.tools.checkstyle.api.Violation;
import io.papermc.checkstyle.filters.util.BetterSuppressFilterElement;
import java.util.List;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestBetterSuppressFilterElement {

    private static AuditEvent createEvent(final String key, final @Nullable Object @Nullable [] args) {
        final Violation violation = new Violation(
            1,
            1,
            "",
            key,
            args,
            SeverityLevel.ERROR,
            null,
            TestBetterSuppressFilterElement.class,
            null
        );
        return new AuditEvent(new Object(), "TestFile.java", violation);
    }

    @Test
    void testMatchingArgumentsSuppressesViolation() {
        final BetterSuppressFilterElement filter = new BetterSuppressFilterElement(
            null,
            Pattern.compile(".*"),
            null,
            null,
            null,
            null,
            Pattern.compile("test\\.key"),
            List.of(Pattern.compile("^deprecated$"))
        );

        final AuditEvent event = createEvent("test.key", new Object[] {"deprecated"});
        // When matched, accept returns false (suppressed)
        assertFalse(filter.accept(event));
    }

    @Test
    void testMismatchedArgumentDoesNotSuppress() {
        final BetterSuppressFilterElement filter = new BetterSuppressFilterElement(
            null,
            Pattern.compile(".*"),
            null,
            null,
            null,
            null,
            Pattern.compile("test\\.key"),
            List.of(Pattern.compile("^deprecated$"))
        );

        final AuditEvent event = createEvent("test.key", new Object[] {"other"});
        assertTrue(filter.accept(event));
    }

    @Test
    void testFewerArgumentsThanConfiguredDoesNotSuppress() {
        final BetterSuppressFilterElement filter = new BetterSuppressFilterElement(
            null,
            Pattern.compile(".*"),
            null,
            null,
            null,
            null,
            Pattern.compile("test\\.key"),
            List.of(Pattern.compile("arg1"), Pattern.compile("arg2"))
        );

        // Only 1 arg provided; previously Math.min caused arg2 to be ignored
        final AuditEvent eventWithOneArg = createEvent("test.key", new Object[] {"arg1"});
        assertTrue(filter.accept(eventWithOneArg));

        // 0 args provided
        final AuditEvent eventWithZeroArgs = createEvent("test.key", new Object[0]);
        assertTrue(filter.accept(eventWithZeroArgs));
    }

    @Test
    void testNullArgsArrayDoesNotThrowAndDoesNotSuppress() {
        final BetterSuppressFilterElement filter = new BetterSuppressFilterElement(
            null,
            Pattern.compile(".*"),
            null,
            null,
            null,
            null,
            Pattern.compile("test\\.key"),
            List.of(Pattern.compile("^deprecated$"))
        );

        final AuditEvent event = createEvent("test.key", null);
        assertTrue(filter.accept(event));
    }

    @Test
    void testNullArgElementDoesNotThrowAndDoesNotSuppress() {
        final BetterSuppressFilterElement filter = new BetterSuppressFilterElement(
            null,
            Pattern.compile(".*"),
            null,
            null,
            null,
            null,
            Pattern.compile("test\\.key"),
            List.of(Pattern.compile("^deprecated$"))
        );

        final AuditEvent event = createEvent("test.key", new Object[] {null});
        assertTrue(filter.accept(event));
    }
}
