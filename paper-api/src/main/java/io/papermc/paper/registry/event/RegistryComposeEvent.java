package io.papermc.paper.registry.event;

import io.papermc.paper.registry.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Event object for {@link RegistryEventProvider#compose()}. This
 * event is fired after a registry is loaded with its normal values.
 * It provides a way for plugins to add new objects to the registry.
 *
 * @param <T> registry entry type
 * @param <B> registry entry builder type
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface RegistryComposeEvent<T, B extends RegistryBuilder<T>> extends RegistryEvent<T> {

    /**
     * Get the writable registry.
     *
     * @return a writable registry
     */
    WritableRegistry<T, B> registry();

    /**
     * Gets the registry retriever, which can be used to retrieve or create
     * entries in the registry.
     *
     * @return the registry retriever
     */
    @Contract(pure = true)
    RegistryRetriever retriever();
}
