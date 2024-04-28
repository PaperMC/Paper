package io.papermc.paper.datacomponent.item;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the state of whether a data component should be shown
 * in an item's tooltip.
 *
 * @param <T> the data component type
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ShownInTooltip<T> {

    /**
     * Gets if the data component should be shown in the item's tooltip.
     *
     * @return {@code true} to show in the tooltip
     */
    @Contract(pure = true)
    boolean showInTooltip();

    /**
     * Returns a copy of this data component with the specified
     * show-in-tooltip state.
     *
     * @param showInTooltip {@code true} to show in the tooltip
     * @return the new data component
     */
    @Contract(value = "_ -> new", pure = true)
    T showInTooltip(boolean showInTooltip);

    /**
     * A builder for creating a {@link ShownInTooltip} data component.
     *
     * @param <B> builder type
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder<B> {

        /**
         * Sets if the data component should be shown in the item's tooltip.
         *
         * @param showInTooltip {@code true} to show in the tooltip
         * @return the builder for chaining
         * @see #showInTooltip()
         */
        @Contract(value = "_ -> this", mutates = "this")
        B showInTooltip(boolean showInTooltip);
    }
}
