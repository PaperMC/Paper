package io.papermc.paper.block.property;

/**
 * A block data property for an {@code int} value.
 * @see BlockProperties
 */
public sealed interface IntegerBlockProperty extends BlockProperty<Integer> permits IntegerBlockPropertyImpl {

    /**
     * Gets the min value for this property.
     *
     * @return the min value
     */
    int min();

    /**
     * Gets the max value for this property.
     *
     * @return the max value
     */
    int max();
}
