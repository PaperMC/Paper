package org.bukkit.persistence;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The list persistent data represents a data type that is capable of storing a
 * list of other data types in a {@link PersistentDataContainer}.
 *
 * @param <P> the primitive type of the list element.
 * @param <C> the complex type of the list elements.
 */
public interface ListPersistentDataType<P, C> extends PersistentDataType<List<P>, List<C>> {

    /**
     * Provides the persistent data type of the elements found in the list.
     *
     * @return the persistent data type.
     */
    @NotNull
    PersistentDataType<P, C> elementType();
}
