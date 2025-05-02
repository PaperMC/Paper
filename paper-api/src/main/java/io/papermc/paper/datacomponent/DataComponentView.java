package io.papermc.paper.datacomponent;

import org.bukkit.Utility;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * This represents a view of a data component holder. No
 * methods on this interface mutate the holder.
 *
 * @see DataComponentHolder
 */
@NullMarked
@ApiStatus.NonExtendable
public interface DataComponentView {
    // Paper start - data component API
    /**
     * Gets the value for the data component type on this stack.
     *
     * @param type the data component type
     * @param <T> the value type
     * @return the value for the data component type, or {@code null} if not set or marked as removed
     * @see #hasData(io.papermc.paper.datacomponent.DataComponentType) for DataComponentType.NonValued
     */
    @Contract(pure = true)
    @ApiStatus.Experimental
    public <T> @Nullable T getData(final DataComponentType.@NotNull Valued<T> type);

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
    public <T> @Nullable T getDataOrDefault(final DataComponentType.@NotNull Valued<? extends T> type, final @Nullable T fallback);

    /**
     * Checks if the data component type is set on this holder.
     *
     * @param type the data component type
     * @return {@code true} if set, {@code false} otherwise
     */
    @Contract(pure = true)
    @ApiStatus.Experimental
    boolean hasData(final io.papermc.paper.datacomponent.@NotNull DataComponentType type);

    // Not applicable to entities
    // /**
    //  * Gets all the data component types set on this holder.
    //  *
    //  * @return an immutable set of data component types
    //  */
    // @Contract("-> new")
    // @ApiStatus.Experimental
    // java.util.@Unmodifiable Set<io.papermc.paper.datacomponent.@NotNull DataComponentType> getDataTypes();
}
