package org.bukkit.persistence;

import com.google.common.base.Preconditions;
import io.papermc.paper.persistence.PersistentDataKey;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * This interface represents a map like object, capable of storing custom tags
 * in it.
 */
public interface PersistentDataContainer extends io.papermc.paper.persistence.PersistentDataContainerView { // Paper - split up view and mutable

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
     * be done using {@link #remove(NamespacedKey)} or {@link #remove(PersistentDataKey)}
     * @throws IllegalArgumentException if no suitable adapter was found for
     * the {@link PersistentDataType#getPrimitiveType()}
     */
    <P, C> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C value);

    /**
     * Stores a metadata value on the {@link PersistentDataHolder} instance.
     * <p>
     * This API cannot be used to manipulate minecraft data, as the values will
     * be stored using your namespace. This method will override any existing
     * value the {@link PersistentDataHolder} may have stored under the provided
     * key.
     *
     * @param dataKey the data key this value will be stored under
     * @param value the value to store in the tag
     * @param <C> the generic type of the object to store
     *
     * @throws IllegalArgumentException if the data key is null
     * @throws IllegalArgumentException if the value is null. Removing a tag should
     * be done using {@link #remove(NamespacedKey)} pr {@link #remove(PersistentDataKey)}
     * @throws IllegalArgumentException if no suitable adapter was found for
     * the {@link PersistentDataType#getPrimitiveType()}
     */
    default <C> void set(@NotNull PersistentDataKey<C> dataKey, @NotNull C value) {
        Preconditions.checkState(dataKey != null, "The data key cannot be null");
        this.set(dataKey.getNamespacedKey(), dataKey.getDataType(), value);
    }
    // Paper - move to PersistentDataContainerView

    /**
     * Removes a custom key from the {@link PersistentDataHolder} instance.
     *
     * @param key the key to remove
     *
     * @throws IllegalArgumentException if the provided key is null
     */
    void remove(@NotNull NamespacedKey key);

    /**
     * Removes a custom key from the {@link PersistentDataHolder} instance.
     *
     * @param dataKey the data key to remove
     * @throws IllegalArgumentException if the provided data key is null
     */
    default void remove(@NotNull PersistentDataKey<?> dataKey) {
        Preconditions.checkState(dataKey != null, "The data key cannot be null");
        this.remove(dataKey.getNamespacedKey());
    }
    // Paper - move to PersistentDataContainerView

    // Paper start - byte array serialization
    // Paper - move to PersistentDataContainerView
    /**
     * Read values from a serialized byte array into this
     * {@link PersistentDataContainer} instance.
     *
     * @param bytes the byte array to read from
     * @param clear if true, this {@link PersistentDataContainer} instance
     *              will be cleared before reading
     * @throws java.io.IOException if the byte array has an invalid format
     */
    void readFromBytes(byte @NotNull [] bytes, boolean clear) throws java.io.IOException;

    /**
     * Read values from a serialized byte array into this
     * {@link PersistentDataContainer} instance.
     * This method has the same effect as
     * <code>PersistentDataContainer#readFromBytes(bytes, true)</code>
     *
     * @param bytes the byte array to read from
     * @throws java.io.IOException if the byte array has an invalid format
     */
    default void readFromBytes(final byte @NotNull [] bytes) throws java.io.IOException {
        this.readFromBytes(bytes, true);
    }
    // Paper end - byte array serialization
}
