package io.papermc.paper.datacomponent;

import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.holder.RegistryHolder;
import org.bukkit.Utility;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A view of the changes made to an item type's default data components. Each
 * data component is either set to a value, marked as removed, or left unchanged,
 * in which case the item type's default applies.
 * <p>
 * Unlike a {@link DataComponentView}, a patch doesn't know about the item type's
 * defaults, which lets it be read while registries are still being loaded, such as
 * in registry events. Data components whose entire value is a value of a data-driven
 * registry, like {@link DataComponentTypes#CAT_VARIANT}, are read with
 * {@link #getHolder(DataComponentType.Valued)} instead of {@link #getData(DataComponentType.Valued)}.
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DataComponentPatchView {

    /**
     * Gets the value this patch sets for a data component.
     *
     * @param type the data component type
     * @param <T>  the value type
     * @return the value, or {@code null} if this patch doesn't set it or marks it as removed
     * @throws IllegalArgumentException if the type's value is a value of a data-driven
     *     registry, use {@link #getHolder(DataComponentType.Valued)} for those types instead
     * @see #hasData(DataComponentType) for DataComponentType.NonValued
     */
    @Contract(pure = true)
    <T> @Nullable T getData(DataComponentType.Valued<T> type);

    /**
     * Gets the value this patch sets for a data component with a fallback value.
     *
     * @param type     the data component type
     * @param fallback the fallback value if this patch doesn't set the value
     * @param <T>      the value type
     * @return the value or the fallback value
     * @throws IllegalArgumentException if the type's value is a value of a data-driven
     *     registry, use {@link #getHolder(DataComponentType.Valued)} for those types instead
     */
    @Utility
    @Contract(value = "_, !null -> !null", pure = true)
    default <T> @Nullable T getDataOrDefault(final DataComponentType.Valued<? extends T> type, final @Nullable T fallback) {
        final T value = this.getData(type);
        return value != null ? value : fallback;
    }

    /**
     * Gets the value this patch sets for a data component whose entire value is a value of
     * a data-driven registry, like {@link DataComponentTypes#CAT_VARIANT}.
     *
     * @param type the data component type
     * @param <T>  the registry value type
     * @param <E>  the registry entry type
     * @return the holder of the value, or {@code null} if this patch doesn't set it or marks it as removed
     * @throws IllegalArgumentException if the type's value isn't a value of a data-driven registry
     */
    @Contract(pure = true)
    <T extends RegistryElement.Buildable<T, E, ?>, E> @Nullable RegistryHolder<T, E> getHolder(DataComponentType.Valued<T> type);

    /**
     * Checks if this patch sets a data component.
     *
     * @param type the data component type
     * @return {@code true} if this patch sets the data component
     */
    @Contract(pure = true)
    boolean hasData(DataComponentType type);

    /**
     * Checks if this patch marks a data component as removed.
     *
     * @param type the data component type
     * @return {@code true} if this patch marks the data component as removed
     */
    @Contract(pure = true)
    boolean isDataRemoved(DataComponentType type);
}
