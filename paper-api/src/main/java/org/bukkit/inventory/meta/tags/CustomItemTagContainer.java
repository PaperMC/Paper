package org.bukkit.inventory.meta.tags;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface represents a map like object, capable of storing custom tags
 * in it.
 *
 * @deprecated this API part has been replaced by the
 * {@link org.bukkit.persistence.PersistentDataHolder} API. Please use
 * {@link org.bukkit.persistence.PersistentDataHolder} instead of this.
 */
@Deprecated
public interface CustomItemTagContainer {

    /**
     * Stores a custom value on the {@link ItemMeta}.
     *
     * This API cannot be used to manipulate minecraft tags, as the values will
     * be stored using your namespace. This method will override any existing
     * value the meta may have stored under the provided key.
     *
     * @param key the key this value will be stored under
     * @param type the type this item tag uses
     * @param value the value stored in the tag
     * @param <T> the generic java type of the tag value
     * @param <Z> the generic type of the object to store
     * @throws NullPointerException if the key is null
     * @throws NullPointerException if the type is null
     * @throws NullPointerException if the value is null. Removing a custom tag
     * should be done using {@link #removeCustomTag(org.bukkit.NamespacedKey)}
     * @throws IllegalArgumentException if no suitable adapter will be found for
     * the {@link ItemTagType#getPrimitiveType()}
     */
    <T, Z> void setCustomTag(@NotNull NamespacedKey key, @NotNull ItemTagType<T, Z> type, @NotNull Z value);

    /**
     * Returns if the item meta has a custom tag registered matching the
     * provided parameters.
     *
     * This method will only return if the found value has the same primitive
     * data type as the provided key.
     *
     * Storing a value using a custom {@link ItemTagType} implementation will
     * not store the complex data type. Therefore storing a UUID (by storing a
     * byte[]) will match hasCustomTag("key" , {@link ItemTagType#BYTE_ARRAY}).
     * Likewise a stored byte[] will always match your UUID {@link ItemTagType}
     * even if it is not 16 bytes long.
     *
     * This method is only usable for custom object keys. Overwriting existing
     * tags, like the the display name, will not work as the values are stored
     * using your namespace.
     *
     * @param key the key the value is stored under
     * @param type the type which primitive storage type has to match the value
     * @param <T> the generic type of the stored primitive
     * @param <Z> the generic type of the eventually created complex object
     * @return if a value
     * @throws NullPointerException if the key to look up is null
     * @throws NullPointerException if the type to cast the found object to is
     * null
     */
    <T, Z> boolean hasCustomTag(@NotNull NamespacedKey key, @NotNull ItemTagType<T, Z> type);

    /**
     * Returns the custom tag's value that is stored on the item.
     *
     * @param key the key to look up in the custom tag map
     * @param type the type the value must have and will be casted to
     * @param <T> the generic type of the stored primitive
     * @param <Z> the generic type of the eventually created complex object
     * @return the value or {@code null} if no value was mapped under the given
     * value
     * @throws NullPointerException if the key to look up is null
     * @throws NullPointerException if the type to cast the found object to is
     * null
     * @throws IllegalArgumentException if the value exists under the given key,
     * but cannot be access using the given type
     * @throws IllegalArgumentException if no suitable adapter will be found for
     * the {@link ItemTagType#getPrimitiveType()}
     */
    @Nullable
    <T, Z> Z getCustomTag(@NotNull NamespacedKey key, @NotNull ItemTagType<T, Z> type);

    /**
     * Removes a custom key from the item meta.
     *
     * @param key the key
     * @throws NullPointerException if the provided key is null
     */
    void removeCustomTag(@NotNull NamespacedKey key);

    /**
     * Returns if the container instance is empty, therefore has no entries
     * inside it.
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Returns the adapter context this tag container uses.
     *
     * @return the tag context
     */
    @NotNull
    ItemTagAdapterContext getAdapterContext();
}
