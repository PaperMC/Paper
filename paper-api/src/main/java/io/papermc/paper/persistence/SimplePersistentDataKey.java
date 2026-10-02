package io.papermc.paper.persistence;

import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

/**
 * This class represents the union of a {@link net.kyori.adventure.key.Key} and a
 * {@link PersistentDataType}.
 * <p>
 * This variant of the {@link PersistentDataKey} holds a {@link PersistentDataType} with both generic arguments
 * combined into one. This saves one generic argument for primitive data types, such as {@link PersistentDataType#INTEGER}.
 *
 * @see PersistentDataKey
 * @param <P> the object type both stored in the tag and retrieved when applying the key
 */
@NullMarked
public interface SimplePersistentDataKey<P> extends PersistentDataKey<P, P> {
}
