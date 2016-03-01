package co.aikar.util;

import com.google.common.collect.ForwardingMap;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public class Counter <T> extends ForwardingMap<T, Long> {
    private final Map<T, Long> counts = new HashMap<>();

    public long decrement(@Nullable T key) {
        return increment(key, -1);
    }
    public long increment(@Nullable T key) {
        return increment(key, 1);
    }
    public long decrement(@Nullable T key, long amount) {
        return increment(key, -amount);
    }
    public long increment(@Nullable T key, long amount) {
        Long count = this.getCount(key);
        count += amount;
        this.counts.put(key, count);
        return count;
    }

    public long getCount(@Nullable T key) {
        return this.counts.getOrDefault(key, 0L);
    }

    @NotNull
    @Override
    protected Map<T, Long> delegate() {
        return this.counts;
    }
}
