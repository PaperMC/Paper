package org.bukkit.support;

import org.hamcrest.Matcher;
import org.jspecify.annotations.Nullable;

/**
 * Custom assertThat methods, where the reason is put at the end of the method call.
 * To better match with JUnit 5 argument order and also help with readability for longer reasons.
 * <br>
 * <pre>
 * assertThat(String.format("""
 *         The block data created for the material %s is not an instance of the data class from that material.
 *         """, material), blockData, is(instanceOf(expectedClass)));
 * </pre>
 * vs.
 * <pre>
 * assertThat(blockData, is(instanceOf(expectedClass)), String.format("""
 *         The block data created for the material %s is not an instance of the data class from that material.
 *         """, material));
 * </pre>
 */
public final class MatcherAssert {

    private MatcherAssert() {
    }

    public static <T extends @Nullable Object> void assertThat(final T actual, final Matcher<? super T> matcher) {
        org.hamcrest.MatcherAssert.assertThat(actual, matcher);
    }

    public static <T extends @Nullable Object> void assertThat(final T actual, final Matcher<? super T> matcher, final String reason) {
        org.hamcrest.MatcherAssert.assertThat(reason, actual, matcher);
    }

    public static void assertThat(final boolean assertion, final String reason) {
        org.hamcrest.MatcherAssert.assertThat(reason, assertion);
    }
}
