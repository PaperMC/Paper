package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the contents of an item container.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CONTAINER
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemContainerContents {

    @Contract(value = "_ -> new", pure = true)
    static ItemContainerContents containerContents(final List<ItemStack> contents) {
        return containerContents().addAll(contents).build();
    }

    @Contract(value = "-> new", pure = true)
    static ItemContainerContents.Builder containerContents() {
        return ItemComponentTypesBridge.bridge().itemContainerContents();
    }

    /**
     * Gets the contents of the container.
     *
     * @return the contents
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStack> contents();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemContainerContents> {

        /**
         * Adds an item stack to the container.
         *
         * @param stack the item stack
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack stack);

        /**
         * Adds item stacks to the container.
         *
         * @param stacks the item stacks
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> stacks);
    }
}
