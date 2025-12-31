package io.papermc.paper.block.property;

/**
 * A block data property for an {@code enum} value.
 * @see BlockProperties
 */
public sealed interface EnumBlockProperty<E extends Enum<E>> extends BlockProperty<E> permits EnumBlockPropertyImpl {
}
