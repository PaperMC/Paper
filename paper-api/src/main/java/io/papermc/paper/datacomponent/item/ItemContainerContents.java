package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.item.ItemStackTemplate;
import java.util.List;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Holds the contents of an item container.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CONTAINER
 */
@ApiStatus.NonExtendable
public interface ItemContainerContents {

    @Contract(value = "_ -> new", pure = true)
    static ItemContainerContents containerContents(final List<ItemStack> contents) {
        return containerContents().addAll(contents).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static ItemContainerContents containerContentsFromTemplates(final List<Optional<ItemStackTemplate>> contents) {
        return containerContents().addAllTemplates(contents).build();
    }

    @Contract(value = "-> new", pure = true)
    static ItemContainerContents.Builder containerContents() {
        return ItemComponentTypesBridge.bridge().itemContainerContents();
    }

    /**
     * Gets the contents of the container, one element per slot.
     *
     * @return the contents, with {@link ItemStack#empty() empty} item stacks for empty slots
     * @see #contentTemplates()
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStack> contents();

    /**
     * Gets the contents of the container, one element per slot.
     * <p>
     * Unlike {@link #contents()}, this is available while registries are
     * still being loaded, such as in registry events.
     *
     * @return the contents, with {@link Optional#empty() empty} optionals for empty slots
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<Optional<ItemStackTemplate>> contentTemplates();

    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemContainerContents> {

        /**
         * Adds an item stack to the next slot of the container.
         *
         * @param stack the item stack, or an {@link ItemStack#empty() empty} item stack for an empty slot
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack stack);

        /**
         * Adds an item to the next slot of the container.
         *
         * @param template the item, or an {@link Optional#empty() empty} optional for an empty slot
         * @return the builder for chaining
         * @see #contentTemplates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(Optional<ItemStackTemplate> template);

        /**
         * Adds item stacks to the next slots of the container.
         *
         * @param stacks the item stacks, with {@link ItemStack#empty() empty} item stacks for empty slots
         * @return the builder for chaining
         * @see #contents()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> stacks);

        /**
         * Adds items to the next slots of the container.
         *
         * @param templates the items, with {@link Optional#empty() empty} optionals for empty slots
         * @return the builder for chaining
         * @see #contentTemplates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAllTemplates(List<Optional<ItemStackTemplate>> templates);
    }
}
