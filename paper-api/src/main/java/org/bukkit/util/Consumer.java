package org.bukkit.util;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result.
 *
 * @param <T> the type of the input to the operation
 * @deprecated Use {@link java.util.function.Consumer} instead
 */
// Bukkit developer note (NOT plugin developers):
// NEVER use this consumer in the API.
// API methods which use this consumer will be remapped to Java's consumer at runtime, resulting in an error.
@Deprecated(since = "1.20.2", forRemoval = true)
public interface Consumer<T> extends java.util.function.Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    @Override
    void accept(T t);
}
