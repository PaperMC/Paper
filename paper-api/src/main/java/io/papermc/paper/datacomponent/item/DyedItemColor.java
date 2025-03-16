package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.bukkit.Color;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a color applied to a dyeable item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#DYED_COLOR
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DyedItemColor {

    @Contract(value = "_, _ -> new", pure = true)
    static DyedItemColor dyedItemColor(final Color color) {
        return dyedItemColor().color(color).build();
    }

    @Contract(value = "-> new", pure = true)
    static DyedItemColor.Builder dyedItemColor() {
        return ItemComponentTypesBridge.bridge().dyedItemColor();
    }

    /**
     * Color of the item.
     *
     * @return color
     */
    @Contract(value = "-> new", pure = true)
    Color color();

    /**
     * Builder for {@link DyedItemColor}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<DyedItemColor> {

        /**
         * Sets the color of this builder.
         *
         * @param color color
         * @return the builder for chaining
         * @see #color()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder color(Color color);
    }
}
