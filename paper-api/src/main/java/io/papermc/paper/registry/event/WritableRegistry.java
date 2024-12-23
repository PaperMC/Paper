package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A registry which supports registering new objects.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface WritableRegistry<T, B extends RegistryBuilder<T>> {

    /**
     * Register a new value with the specified key. This will
     * fire a {@link RegistryEntryAddEvent} for the new entry.
     *
     * @param key the entry's key (must be unique from others)
     * @param value a consumer for the entry's builder
     */
    void register(TypedKey<T> key, Consumer<? super B> value);

    /**
     * Register a new value with the specified key. This will
     * fire a {@link RegistryEntryAddEvent} for the new entry. The
     * builder in the consumer will be pre-filled with the values
     * from the copyFrom key.
     *
     * @param key the entry's key (must be unique from others)
     * @param copyFrom the key to copy values from (must already be registered)
     * @param value a consumer for the entry's builder
     * @throws IllegalArgumentException if copyFrom doesn't exist
     */
    void register(TypedKey<T> key, TypedKey<T> copyFrom, Consumer<? super B> value);
}
