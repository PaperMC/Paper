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
     * Gets the value type of this property.
     *
     * @return the value type
     */
    Class<T> type();

    /**
     * Gets the string name for a value of this property.
     *
     * @param value the value to get the string name of
     * @return the string name of the value
     * @throws IllegalArgumentException if the value is not valid for
     * @see #value(String)
     */
    String name(T value);

    /**
     * Checks if the name is a valid name
     * for a value of this property.
     *
     * @param name the name to check
     * @return true if valid
     * @see #value(String)
     */
    boolean isValidName(String name);

    /**
     * Gets the value of this property from the string name.
     * Throws an exception if no value is found with that name.
     *
     * @param name the name of the value
     * @return the property with the specified name
     * @see #isValidName(String)
     */
    T value(String name);

    /**
     * Checks if the value is valid for this property.
     *
     * @param value the value to check
     * @return true if valid
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
     * @return true if this property is present
     * @see BlockPropertyHolder#hasProperty(BlockProperty)
     */
    default boolean hasValueOn(final BlockPropertyHolder holder) {
        return holder.hasProperty(this);
    }

    /**
     * Gets the value from a {@link BlockPropertyHolder} for this property.
     *
     * @param holder the holder of a set of properties (like {@link org.bukkit.block.data.BlockData})
     * @return the non-null value
     * @throws IllegalArgumentException if this property is not present
     * @see #hasValueOn(BlockPropertyHolder)
     * @see BlockPropertyHolder#getValue(BlockProperty)
     */
    default T getValue(final BlockPropertyHolder holder) {
        return holder.getValue(this);
    }

    /**
     * Gets the optional of the value for this property.
     *
     * @param holder the holder of a set of properties (like {@link org.bukkit.block.data.BlockData})
     * @return the optional of the value, will be empty if the property is not present
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
