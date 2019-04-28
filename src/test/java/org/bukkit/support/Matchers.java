package org.bukkit.support;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public final class Matchers {

    private Matchers() {}

    public static <T> Matcher<T> sameHash(T value) {
        return new SameHash<T>(value);
    }

    static class SameHash<T> extends BaseMatcher<T> {
        private final int expected;

        SameHash(T object) {
            expected = object.hashCode();
        }

        @Override
        public boolean matches(Object item) {
            return item.hashCode() == expected;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(expected);
        }
    }
}
