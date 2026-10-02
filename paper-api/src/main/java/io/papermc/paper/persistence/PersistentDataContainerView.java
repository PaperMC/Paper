package io.papermc.paper.persistence;

import java.util.Set;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// This represents a view of a persistent data container. No
/// methods on this interface mutate the container.
///
/// @see PersistentDataContainer
@NullMarked
@ApiStatus.NonExtendable
public interface PersistentDataContainerView {

    /// Returns if the persistent metadata provider has metadata registered
    /// matching the provided parameters.
    ///
    /// This method will only return true if the found value has the same primitive
    /// data type as the provided key.
    ///
    /// Storing a value using a custom [PersistentDataType] implementation
    /// will not store the complex data type. Therefore storing a UUID (by
    /// storing a byte[]) will match has("key" ,
    /// [PersistentDataType#BYTE_ARRAY]). Likewise a stored byte[] will
    /// always match your UUID [PersistentDataType] even if it is not 16
    /// bytes long.
    ///
    /// This method is only usable for custom object keys. Overwriting existing
    /// tags, like the display name, will not work as the values are stored
    /// using your namespace.
    ///
    /// @param key the key the value is stored under
    /// @param type the type the primitive stored value has to match
    /// @param <P> the generic type of the stored primitive
    /// @param <C> the generic type of the eventually created complex object
    /// @return if a value with the provided key and type exists
    /// @throws IllegalArgumentException if the key to look up is null
    /// @throws IllegalArgumentException if the type to cast the found object to is
    /// null
    <P, C> boolean has(NamespacedKey key, PersistentDataType<P, C> type);

    /// Returns if the persistent metadata provider has metadata registered matching
    /// the provided parameters.
    ///
    /// This method will return true as long as a value with the given key exists,
    /// regardless of its type.
    ///
    /// This method is only usable for custom object keys. Overwriting existing tags,
    /// like the display name, will not work as the values are stored using your
    /// namespace.
    ///
    /// @param key the key the value is stored under
    /// @return if a value with the provided key exists
    /// @throws IllegalArgumentException if the key to look up is null
    boolean has(NamespacedKey key);

    /// Returns the metadata value that is stored on the
    /// [PersistentDataHolder] instance.
    ///
    /// @param key the key to look up in the custom tag map
    /// @param type the type the value must have and will be cast to
    /// @param <P> the generic type of the stored primitive
    /// @param <C> the generic type of the eventually created complex object
    /// @return the value or `null` if no value was mapped under the given
    /// value
    /// @throws IllegalArgumentException if the key to look up is null
    /// @throws IllegalArgumentException if the type to cast the found object to is
    /// null
    /// @throws IllegalArgumentException if a value exists under the given key,
    /// but cannot be accessed using the given type
    /// @throws IllegalArgumentException if no suitable adapter was found for
    /// the
    /// [PersistentDataType#getPrimitiveType()]
    <P, C> @Nullable C get(NamespacedKey key, PersistentDataType<P, C> type);

    /// Returns the metadata value that is stored on the
    /// [PersistentDataHolder] instance. If the value does not exist in the
    /// container, the default value provided is returned.
    ///
    /// @param key the key to look up in the custom tag map
    /// @param type the type the value must have and will be cast to
    /// @param defaultValue the default value to return if no value was found for
    /// the provided key
    /// @param <P> the generic type of the stored primitive
    /// @param <C> the generic type of the eventually created complex object
    /// @return the value or the default value if no value was mapped under the
    /// given key
    /// @throws IllegalArgumentException if the key to look up is null
    /// @throws IllegalArgumentException if the type to cast the found object to is
    /// null
    /// @throws IllegalArgumentException if a value exists under the given key,
    /// but cannot be accessed using the given type
    /// @throws IllegalArgumentException if no suitable adapter was found for
    /// the [PersistentDataType#getPrimitiveType()]
    <P, C> C getOrDefault(NamespacedKey key, PersistentDataType<P, C> type, C defaultValue);

    /// Get the set of keys present on this [PersistentDataContainer]
    /// instance.
    ///
    /// Any changes made to the returned set will not be reflected on the
    /// instance.
    ///
    /// @return the key set
    Set<NamespacedKey> getKeys();

    /// Returns if the container instance is empty, therefore has no entries
    /// inside it.
    ///
    /// @return the boolean
    boolean isEmpty();

    /// Copies all values from this [PersistentDataContainer] to the provided
    /// container.
    ///
    /// This method only copies custom object keys. Existing tags, like the display
    /// name, will not be copied as the values are stored using your namespace.
    ///
    /// @param other the container to copy to
    /// @param replace whether to replace any matching values in the target container
    /// @throws IllegalArgumentException if the other container is null
    void copyTo(PersistentDataContainer other, boolean replace);

    /// Returns the adapter context this tag container uses.
    ///
    /// @return the tag context
    PersistentDataAdapterContext getAdapterContext();

    /// Serialize this [PersistentDataContainer] instance to a
    /// byte array.
    ///
    /// @return a binary representation of this container
    /// @throws java.io.IOException if we fail to write this container to a byte array
    byte[] serializeToBytes() throws java.io.IOException;

    /// {@return the size of the data container}
    int getSize();
}
