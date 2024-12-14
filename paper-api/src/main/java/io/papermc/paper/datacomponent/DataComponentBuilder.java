package io.papermc.paper.datacomponent;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Base builder type for all component builders.
 *
 * @param <C> built component type
 * @since 1.21.3
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DataComponentBuilder<C> {

    /**
     * Builds the immutable component value.
     *
     * @return a new component value
     */
    @Contract(value = "-> new", pure = true)
    C build();
}
