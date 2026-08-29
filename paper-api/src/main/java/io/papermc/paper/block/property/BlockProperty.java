package io.papermc.paper.block.property;

import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A property that applies to a {@link BlockPropertyHolder} such
 * as {@link org.bukkit.block.data.BlockData} or {@link io.papermc.paper.block.fluid.FluidData}.
 *
 * @param <T> the value type
 * @see BlockProperties
 */
public sealed interface BlockProperty<T extends Comparable<T>> permits AsIntegerProperty, BooleanBlockProperty, EnumBlockProperty, IntegerBlockProperty {

    /**
     * Gets the name of this property.
     *
     * @return the name
     */
    String name();

    /**
     * Gets the value's type of this property.
     *
     * @return the value type
     */
    Class<T> type();

    /**
     * Gets the name for a value of this property.
     *
     * @param value the value to get the name of
     * @return the name of the value
     * @throws IllegalArgumentException if the value is invalid
     * @see #value(String)
     */
    String name(T value);

    /**
     * Checks if the name is valid for a value of this property.
     *
     * @param name the name to check
     * @return {@code true} if valid
     * @see #value(String)
     */
    boolean isValidName(String name);

    /**
     * Gets the value of this property from the name.
     *
     * @param name the name of the value
     * @return the property with the specified name
     * @throws IllegalArgumentException if no value is found with that name
     * @see #isValidName(String)
     */
    T value(String name);

    /**
     * Checks if the value is valid for this property.
     *
     * @param value the value to check
     * @return {@code true} if valid
     */
    boolean isValidValue(T value);

    /**
     * Gets an immutable collection of possible values for this property.
     *
     * @return an immutable collection of values
     */
    @Unmodifiable Set<T> values();

    /**
     * Checks if a {@link BlockPropertyHolder} has this property.
     *
     * @param holder the holder of a set of properties (like {@link org.bukkit.block.data.BlockData})
     * @return {@code true} if this property is present
     * @see BlockPropertyHolder#hasProperty(BlockProperty)
     */
    default boolean hasValueOn(final BlockPropertyHolder holder) {
        return holder.hasProperty(this);
    }

    /**
     * Gets the value from a {@link BlockPropertyHolder} for this property.
     *
     * @param holder the holder of a set of properties (like {@link org.bukkit.block.data.BlockData})
     * @return the non-{@code null} value
     * @throws IllegalArgumentException if this property is not present
     * @see #hasValueOn(BlockPropertyHolder)
     * @see BlockPropertyHolder#getValue(BlockProperty)
     */
    default T getValue(final BlockPropertyHolder holder) {
        return holder.getValue(this);
    }

    /**
     * Gets the value optionally for this property.
     *
     * @param holder the holder of a set of properties (like {@link org.bukkit.block.data.BlockData})
     * @return the value if the property is present
     * @see #getValue(BlockPropertyHolder)
     * @see BlockPropertyHolder#getOptionalValue(BlockProperty)
     */
    default Optional<T> getOptionalValue(final BlockPropertyHolder holder) {
        return holder.getOptionalValue(this);
    }

    /**
     * Sets the value on a {@link BlockPropertyHolder} for this property.
     *
     * @param holder the mutable holder of a set of properties (like {@link org.bukkit.block.data.BlockData})
     * @param value  the value for this property
     * @throws IllegalArgumentException if this property is not present
     * @see #hasValueOn(BlockPropertyHolder)
     * @see BlockPropertyHolder#hasProperty(BlockProperty)
     */
    default void setValue(final BlockPropertyHolder.Mutable holder, final T value) {
        holder.setValue(this, value);
    }
}
