package io.papermc.paper.block.property;

import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

/**
 * Represents an object that holds {@linkplain BlockProperty block properties}.
 *
 * @see BlockProperties
 */
public interface BlockPropertyHolder {

    /**
     * Gets the property with this name on the holder (if present).
     *
     * @param propertyName name of the property
     * @param <T>          property type
     * @return the property, if one is found with that name
     */
    <T extends Comparable<T>> @Nullable BlockProperty<T> getProperty(String propertyName);

    /**
     * Checks if this holder has the given property.
     *
     * @param property the property to check for
     * @param <T>      property type
     * @return {@code true} if property is present
     * @see BlockProperty#hasValueOn(BlockPropertyHolder)
     */
    <T extends Comparable<T>> boolean hasProperty(BlockProperty<T> property);

    /**
     * Gets the value for the given property.
     *
     * @param property the property
     * @param <T>      property type
     * @return the non-{@code null} value
     * @throws IllegalArgumentException if the property is not present
     * @see #hasProperty(BlockProperty)
     * @see BlockProperty#getValue(BlockPropertyHolder)
     */
    <T extends Comparable<T>> T getValue(BlockProperty<T> property);

    /**
     * Gets the value optionally for the given property.
     *
     * @param property the property
     * @param <T>      property type
     * @return the value if the property is present
     * @see #getValue(BlockProperty)
     * @see BlockProperty#getOptionalValue(BlockPropertyHolder)
     */
    <T extends Comparable<T>> Optional<T> getOptionalValue(BlockProperty<T> property);

    /**
     * Get all properties present on this holder.
     *
     * @return an unmodifiable collection of properties
     */
    @Unmodifiable Collection<BlockProperty<?>> getProperties();

    /**
     * Represents an object that holds {@linkplain BlockProperty block properties} that can be changed.
     *
     * @see BlockProperties
     */
    interface Mutable extends BlockPropertyHolder {

        /**
         * Sets the value of the given property.
         *
         * @param property the property
         * @param value    the value for the property
         * @param <T>      property type
         * @throws IllegalArgumentException if the property is not present or if the value is invalid
         * @see #hasProperty(BlockProperty)
         * @see BlockProperty#setValue(Mutable, Comparable)
         */
        <T extends Comparable<T>> void setValue(BlockProperty<T> property, T value);
    }
}
