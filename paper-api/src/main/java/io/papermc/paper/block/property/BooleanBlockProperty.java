package io.papermc.paper.block.property;

/**
 * A block data property for a {@code boolean} value.
 * @see BlockProperties
 */
public sealed interface BooleanBlockProperty extends BlockProperty<Boolean> permits BooleanBlockPropertyImpl {
}
