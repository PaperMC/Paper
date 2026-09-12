package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.data.util.Conversions;
import org.jspecify.annotations.Nullable;

/**
 * Creates the base registry entry (not the builder) from an optional minecraft value.
 *
 * @param <M> the minecraft type
 * @param <E> the registry entry type
 */
@FunctionalInterface
public interface EntryFactory<M, E> {

    E create(Conversions conversions, @Nullable M nms);
}
