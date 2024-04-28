package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds all items stored inside of a Bundle.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BUNDLE_CONTENTS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BundleContents {

    @Contract(value = "_ -> new", pure = true)
    static BundleContents bundleContents(final List<ItemStack> contents) {
        return ItemComponentTypesBridge.bridge().bundleContents().addAll(contents).build();
    }

    @Contract(value = "-> new", pure = true)
    static BundleContents.Builder bundleContents() {
        return ItemComponentTypesBridge.bridge().bundleContents();
    }

    /**
     * Lists the items that are currently stored inside of this component.
     *
     * @return items
     */
    @Contract(pure = true)
    @Unmodifiable List<ItemStack> contents();

    /**
     * Builder for {@link BundleContents}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BundleContents> {

        /**
         * Adds an item to this builder.
         *
         * @param stack item
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack stack);

        /**
         * Adds items to this builder.
         *
         * @param stacks items
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> stacks);
    }
}
