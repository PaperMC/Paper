package io.papermc.paper.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * De-duplicates {@link String} instances without using {@link String#intern()}.
 *
 * <p>Interning may not be desired as we may want to use the heap for our pool,
 * so it can be garbage collected as normal, etc.</p>
 *
 * <p>Additionally, interning can be slow due to the potentially large size of the
 * pool (as it is shared for the entire JVM), and because most JVMs implement
 * it using JNI.</p>
 */
@DefaultQualifier(NonNull.class)
public final class StringPool {
    private final Map<String, String> pool;

    public StringPool() {
        this(new HashMap<>());
    }

    public StringPool(final Map<String, String> map) {
        this.pool = map;
    }

    public String string(final String string) {
        return this.pool.computeIfAbsent(string, Function.identity());
    }
}
