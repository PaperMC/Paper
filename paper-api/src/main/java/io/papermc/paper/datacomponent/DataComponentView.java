package io.papermc.paper.datacomponent;

import org.bukkit.Utility;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This represents a view of a data component holder. No
 * methods on this interface mutate the holder.
 *
 * @see DataComponentHolder
 */
@NullMarked
@ApiStatus.NonExtendable
public interface DataComponentView {
    /**
     * Gets the value for the data component type on this stack.
     *
     * @param type the data component type
     * @param <T> the value type
     * @return the value for the data component type, or {@code null} if not set or marked as removed
     * @see #hasData(DataComponentType) for DataComponentType.NonValued
     */
    @Contract(pure = true)
    @ApiStatus.Experimental
    <T> @Nullable T getData(final DataComponentType.Valued<T> type);

    /**
     * Gets the value for the data component type on this holder with
     * a fallback value.
     *
     * @param type the data component type
     * @param fallback the fallback value if the value isn't present
     * @param <T> the value type
     * @return the value for the data component type or the fallback value
     */
    @Utility
    @Contract(value = "_, !null -> !null", pure = true)
    @ApiStatus.Experimental
    <T> @Nullable T getDataOrDefault(final DataComponentType.Valued<? extends T> type, final @Nullable T fallback);

    /**
     * Checks if the data component type is set on this holder.
     *
     * @param type the data component type
     * @return {@code true} if set, {@code false} otherwise
     */
    @Contract(pure = true)
    @ApiStatus.Experimental
    boolean hasData(final DataComponentType type);

    // Not applicable to entities
    // /**
    //  * Gets all the data component types set on this holder.
    //  *
    //  * @return an immutable set of data component types
    //  */
    // @Contract("-> new")
    // @ApiStatus.Experimental
    // @Unmodifiable Set<DataComponentType> getDataTypes();
}
