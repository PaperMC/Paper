package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.item.ItemStackTemplate;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Holds all items stored inside of a Bundle.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BUNDLE_CONTENTS
 */
@ApiStatus.NonExtendable
public interface BundleContents {

    @Contract(value = "_ -> new", pure = true)
    static BundleContents bundleContents(final List<ItemStack> contents) {
        return ItemComponentTypesBridge.bridge().bundleContents().addAll(contents).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static BundleContents bundleContentsFromTemplates(final List<ItemStackTemplate> contents) {
        return ItemComponentTypesBridge.bridge().bundleContents().addAllTemplates(contents).build();
    }

    @Contract(value = "-> new", pure = true)
    static BundleContents.Builder bundleContents() {
        return ItemComponentTypesBridge.bridge().bundleContents();
    }

    /**
     * Lists the items that are currently stored inside of this component.
     *
     * @return items
     * @see #contentTemplates()
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStack> contents();

    /**
     * Lists the items that are currently stored inside of this component.
     * <p>
     * Unlike {@link #contents()}, this is available while registries are
     * still being loaded, such as in registry events.
     *
     * @return items
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStackTemplate> contentTemplates();

    /**
     * Builder for {@link BundleContents}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BundleContents> {

        /**
         * Adds an item to this builder.
         *
         * @param stack item, cannot be {@link ItemStack#isEmpty() empty}
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack stack);

        /**
         * Adds an item to this builder.
         *
         * @param template item
         * @return the builder for chaining
         * @see #contentTemplates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStackTemplate template);

        /**
         * Adds items to this builder.
         *
         * @param stacks items, none can be {@link ItemStack#isEmpty() empty}
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> stacks);

        /**
         * Adds items to this builder.
         *
         * @param templates items
         * @return the builder for chaining
         * @see #contentTemplates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAllTemplates(List<ItemStackTemplate> templates);
    }
}
