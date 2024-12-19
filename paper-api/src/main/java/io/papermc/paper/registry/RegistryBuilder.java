package io.papermc.paper.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * To be implemented by any type used for modifying registries.
 *
 * @param <T> registry value type
 * @since 1.21
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface RegistryBuilder<T> {
}
