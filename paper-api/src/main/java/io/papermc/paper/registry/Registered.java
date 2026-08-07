package io.papermc.paper.registry;

import org.jetbrains.annotations.ApiStatus;

/**
 * Marker interface for API types that are, or relate to, values in a registry.
 */
@ApiStatus.Experimental
public interface Registered {

    /**
     * Marker interface for registered types that link an API type to its
     * registry entry and builder types.
     *
     * @param <T> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     */
    @ApiStatus.Experimental
    interface Buildable<T, E, B extends RegistryBuilder<T>> extends Registered {
    }

    /**
     * Marker interface for registered types that are valid to be constructed inline.
     *
     * @param <T> the API type
     * @param <E> the registry entry type
     * @param <B> the builder type
     */
    @ApiStatus.Experimental
    interface Inlineable<T, E, B extends RegistryBuilder<T>> extends Registered.Buildable<T, E, B> {
    }
}
