package io.papermc.paper.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * Denotes that this type is filterable by the client, and may be shown differently
 * depending on the player's set configuration.
 *
 * @param <T> type of value
 */
public interface Filtered<T> {

    /**
     * Creates a filterable value.
     *
     * @param raw      the unfiltered value
     * @param filtered the value shown to players with text filtering enabled, or {@code null} to show them the raw value
     * @param <T>      type of value
     * @return a new filterable value
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> Filtered<T> of(final T raw, final @Nullable T filtered) {
        @ApiStatus.Internal
        record Instance<T>(T raw, @Nullable T filtered) implements Filtered<T> {
        }

        return new Instance<>(raw, filtered);
    }

    /**
     * Gets the unfiltered value.
     *
     * @return the unfiltered value
     */
    @Contract(pure = true)
    T raw();

    /**
     * Gets the value shown to players with text filtering enabled.
     *
     * @return the filtered value, or {@code null} if those players are shown the {@link #raw() raw} value
     */
    @Contract(pure = true)
    @Nullable T filtered();
}
