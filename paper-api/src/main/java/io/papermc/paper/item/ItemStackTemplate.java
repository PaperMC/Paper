package io.papermc.paper.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentPatchView;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentView;
import io.papermc.paper.persistence.PersistentDataViewHolder;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.holder.RegistryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/**
 * An immutable description of an item stack: its type, amount and the changes
 * made to the type's default data components.
 * <p>
 * Unlike {@link ItemStack}, templates can be created and read while registries are
 * still being loaded, such as in registry events, which makes them suitable for
 * registry entries. The item type's default data components aren't available at that
 * point, so only the template's {@link #patch() changes} to them can be read, while
 * {@link #resolveData()} gives a view of all data components once they are available.
 * <p>
 * Data components whose entire value is a value of a data-driven registry, like
 * {@link io.papermc.paper.datacomponent.DataComponentTypes#CAT_VARIANT}, are written with
 * {@link Builder#setHolder(DataComponentType.Valued, RegistryHolder)} instead of
 * {@link Builder#setData(DataComponentType.Valued, Object)}.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemStackTemplate extends PersistentDataViewHolder {

    /**
     * Creates a new builder for a template of the given item type, with an amount of 1
     * and no changes to the type's default data components.
     *
     * @param type the item type, must not be {@link ItemType#AIR}
     * @return a new builder
     */
    @Contract(value = "_ -> new", pure = true)
    static Builder builder(final ItemType type) {
        return ItemStackTemplateBridge.bridge().builder(type);
    }

    /**
     * Creates a template of the given item type, with an amount of 1
     * and no changes to the type's default data components.
     *
     * @param type the item type, must not be {@link ItemType#AIR}
     * @return a new template
     */
    @Contract(value = "_ -> new", pure = true)
    static ItemStackTemplate of(final ItemType type) {
        return builder(type).build();
    }

    /**
     * Gets the item type of this template.
     *
     * @return the item type
     */
    @Contract(pure = true)
    ItemType type();

    /**
     * Gets the amount of this template.
     *
     * @return the amount
     */
    @Contract(pure = true)
    @Range(from = 1, to = 99) int amount();

    /**
     * Gets the changes this template makes to the item type's default data components.
     * <p>
     * This is available while registries are still being loaded.
     *
     * @return the data component patch
     * @see #resolveData()
     */
    @Contract(pure = true)
    DataComponentPatchView patch();

    /**
     * Gets a view of all data components of this template, which are the item type's
     * default data components with this template's {@link #patch() changes} applied.
     * <p>
     * This isn't available while registries are still being loaded, use {@link #patch()} instead.
     *
     * @return the resolved data components
     * @throws IllegalStateException if the item type's default data components aren't available yet
     */
    @Contract(pure = true)
    DataComponentView resolveData();

    /**
     * Creates a new item stack from this template.
     * <p>
     * This isn't available while registries are still being loaded.
     *
     * @return a new item stack
     */
    @Contract(value = "-> new", pure = true)
    ItemStack createItemStack();

    /**
     * Creates a new builder with the values of this template.
     *
     * @return a new builder
     */
    @Contract(value = "-> new", pure = true)
    Builder toBuilder();

    /**
     * A mutable builder for {@link ItemStackTemplate}s.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder {

        /**
         * Sets the amount.
         *
         * @param amount the amount
         * @return this builder
         * @see ItemStackTemplate#amount()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder amount(@Range(from = 1, to = 99) int amount);

        /**
         * Sets the value of a data component.
         *
         * @param type  the data component type
         * @param value the value
         * @param <T>   the value type
         * @return this builder
         * @throws IllegalArgumentException if the type's value is a value of a data-driven registry,
         *     use {@link #setHolder(DataComponentType.Valued, RegistryHolder)} for those types instead
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        <T> Builder setData(DataComponentType.Valued<T> type, T value);

        /**
         * Sets the value of a data component.
         *
         * @param type         the data component type
         * @param valueBuilder the value builder
         * @param <T>          the value type
         * @return this builder
         * @throws IllegalArgumentException if the type's value is a value of a data-driven registry,
         *     use {@link #setHolder(DataComponentType.Valued, RegistryHolder)} for those types instead
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        <T> Builder setData(DataComponentType.Valued<T> type, DataComponentBuilder<T> valueBuilder);

        /**
         * Marks a non-valued data component as present.
         *
         * @param type the data component type
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder setData(DataComponentType.NonValued type);

        /**
         * Sets the value of a data component whose entire value is a value of a data-driven registry,
         * like {@link io.papermc.paper.datacomponent.DataComponentTypes#CAT_VARIANT}.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param type   the data component type
         * @param holder the holder of the value
         * @param <T>    the registry value type
         * @param <E>    the registry entry type
         * @return this builder
         * @throws IllegalArgumentException if the type's value isn't a value of a data-driven registry
         * @see DataComponentPatchView#getHolder(DataComponentType.Valued)
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        <T extends RegistryElement.Buildable<T, E, ?>, E> Builder setHolder(DataComponentType.Valued<T> type, RegistryHolder<T, E> holder);

        /**
         * Removes a data component, including one the item type has by default.
         *
         * @param type the data component type
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder unsetData(DataComponentType type);

        /**
         * Resets a data component to the item type's default value, or removes it
         * if the item type doesn't have the data component by default.
         *
         * @param type the data component type
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder resetData(DataComponentType type);

        /**
         * Creates a template with the values of this builder.
         *
         * @return a new template
         */
        @Contract(value = "-> new", pure = true)
        ItemStackTemplate build();
    }
}
