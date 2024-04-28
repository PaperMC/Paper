package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Additional lines to include in an item's tooltip.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#LORE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemLore {

    @Contract(value = "_ -> new", pure = true)
    static ItemLore lore(final List<? extends ComponentLike> lines) {
        return lore().lines(lines).build();
    }

    @Contract(value = "-> new", pure = true)
    static ItemLore.Builder lore() {
        return ItemComponentTypesBridge.bridge().lore();
    }

    /**
     * Lists the components that are added to an item's tooltip.
     *
     * @return component list
     */
    @Contract(pure = true)
    @Unmodifiable List<Component> lines();

    /**
     * Lists the styled components (example: italicized and purple) that are added to an item's tooltip.
     *
     * @return component list
     */
    @Contract(pure = true)
    @Unmodifiable List<Component> styledLines();

    /**
     * Builder for {@link ItemLore}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemLore> {

        /**
         * Sets the components of this lore.
         *
         * @param lines components
         * @return the builder for chaining
         * @see #lines()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder lines(List<? extends ComponentLike> lines);

        /**
         * Adds a component to the lore.
         *
         * @param line component
         * @return the builder for chaining
         * @see #lines()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addLine(ComponentLike line);

        /**
         * Adds components to the lore.
         *
         * @param lines components
         * @return the builder for chaining
         * @see #lines()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addLines(List<? extends ComponentLike> lines);
    }
}
