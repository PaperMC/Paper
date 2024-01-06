package org.bukkit.persistence;

import java.util.Set;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface represents a map like object, capable of storing custom tags
 * in it.
 */
public interface PersistentDataContainer {

    /**
     * Stores a metadata value on the {@link PersistentDataHolder} instance.
     * <p>
     * This API cannot be used to manipulate minecraft data, as the values will
     * be stored using your namespace. This method will override any existing
     * value the {@link PersistentDataHolder} may have stored under the provided
     * key.
     *
     * @param key the key this value will be stored under
     * @param type the type this tag uses
     * @param value the value to store in the tag
     * @param <P> the generic java type of the tag value
     * @param <C> the generic type of the object to store
     *
     * @throws IllegalArgumentException if the key is null
     * @throws IllegalArgumentException if the type is null
     * @throws IllegalArgumentException if the value is null. Removing a tag should
     * be done using {@link #remove(NamespacedKey)}
     * @throws IllegalArgumentException if no suitable adapter was found for
     * the {@link PersistentDataType#getPrimitiveType()}
     */
    <P, C> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C value);

    /**
     * Returns if the persistent metadata provider has metadata registered
     * matching the provided parameters.
     * <p>
     * This method will only return true if the found value has the same primitive
     * data type as the provided key.
     * <p>
     * Storing a value using a custom {@link PersistentDataType} implementation
     * will not store the complex data type. Therefore storing a UUID (by
     * storing a byte[]) will match has("key" ,
     * {@link PersistentDataType#BYTE_ARRAY}). Likewise a stored byte[] will
     * always match your UUID {@link PersistentDataType} even if it is not 16
     * bytes long.
     * <p>
     * This method is only usable for custom object keys. Overwriting existing
     * tags, like the display name, will not work as the values are stored
     * using your namespace.
     *
     * @param key the key the value is stored under
     * @param type the type the primative stored value has to match
     * @param <P> the generic type of the stored primitive
     * @param <C> the generic type of the eventually created complex object
     *
     * @return if a value with the provided key and type exists
     *
     * @throws IllegalArgumentException if the key to look up is null
     * @throws IllegalArgumentException if the type to cast the found object to is
     * null
     */
    <P, C> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type);

    /**
     * Returns if the persistent metadata provider has metadata registered matching
     * the provided parameters.
     * <p>
     * This method will return true as long as a value with the given key exists,
     * regardless of its type.
     * <p>
     * This method is only usable for custom object keys. Overwriting existing tags,
     * like the display name, will not work as the values are stored using your
     * namespace.
     *
     * @param key the key the value is stored under
     *
     * @return if a value with the provided key exists
     *
     * @throws IllegalArgumentException if the key to look up is null
     */
    boolean has(@NotNull NamespacedKey key);

    /**
     * Returns the metadata value that is stored on the
     * {@link PersistentDataHolder} instance.
     *
     * @param key the key to look up in the custom tag map
     * @param type the type the value must have and will be casted to
     * @param <P> the generic type of the stored primitive
     * @param <C> the generic type of the eventually created complex object
     *
     * @return the value or {@code null} if no value was mapped under the given
     * value
     *
     * @throws IllegalArgumentException if the key to look up is null
     * @throws IllegalArgumentException if the type to cast the found object to is
     * null
     * @throws IllegalArgumentException if a value exists under the given key,
     * but cannot be accessed using the given type
     * @throws IllegalArgumentException if no suitable adapter was found for
     * the {@link
     * PersistentDataType#getPrimitiveType()}
     */
    @Nullable
    <P, C> C get(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type);

    /**
     * Returns the metadata value that is stored on the
     * {@link PersistentDataHolder} instance. If the value does not exist in the
     * container, the default value provided is returned.
     *
     * @param key the key to look up in the custom tag map
     * @param type the type the value must have and will be casted to
     * @param defaultValue the default value to return if no value was found for
     * the provided key
     * @param <P> the generic type of the stored primitive
     * @param <C> the generic type of the eventually created complex object
     *
     * @return the value or the default value if no value was mapped under the
     * given key
     *
     * @throws IllegalArgumentException if the key to look up is null
     * @throws IllegalArgumentException if the type to cast the found object to is
     * null
     * @throws IllegalArgumentException if a value exists under the given key,
     * but cannot be accessed using the given type
     * @throws IllegalArgumentException if no suitable adapter was found for
     * the {@link PersistentDataType#getPrimitiveType()}
     */
    @NotNull
    <P, C> C getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C defaultValue);

    /**
     * Get the set of keys present on this {@link PersistentDataContainer}
     * instance.
     *
     * Any changes made to the returned set will not be reflected on the
     * instance.
     *
     * @return the key set
     */
    @NotNull
    Set<NamespacedKey> getKeys();

    /**
     * Removes a custom key from the {@link PersistentDataHolder} instance.
     *
     * @param key the key to remove
     *
     * @throws IllegalArgumentException if the provided key is null
     */
    void remove(@NotNull NamespacedKey key);

    /**
     * Returns if the container instance is empty, therefore has no entries
     * inside it.
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Copies all values from this {@link PersistentDataContainer} to the provided
     * container.
     * <p>
     * This method only copies custom object keys. Existing tags, like the display
     * name, will not be copied as the values are stored using your namespace.
     *
     * @param other   the container to copy to
     * @param replace whether to replace any matching values in the target container
     *
     * @throws IllegalArgumentException if the other container is null
     */
    void copyTo(@NotNull PersistentDataContainer other, boolean replace);

    /**
     * Returns the adapter context this tag container uses.
     *
     * @return the tag context
     */
    @NotNull
    PersistentDataAdapterContext getAdapterContext();
}
