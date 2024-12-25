package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Generic Builder for InventoryView's with no special attributes or parameters
 *
 * @param <V> the type of InventoryView created from this builder
 */
@ApiStatus.Experimental
public interface InventoryViewBuilder<V extends InventoryView> {

    /**
     * Makes a copy of this builder
     *
     * @return a copy of this builder
     */
    @NotNull
    InventoryViewBuilder<V> copy();

    /**
     * Sets the title of the builder
     *
     * @param title the title
     * @return this builder
     * @deprecated use {@link #title(Component)} instead.
     */
    @Deprecated(forRemoval = true)
    @NotNull
    InventoryViewBuilder<V> title(@NotNull final String title);

    /**
     * Sets the title of the builder
     *
     * @param title the title
     * @return this builder
     */
    @NotNull
    InventoryViewBuilder<V> title(@NotNull final Component title);

    /**
     * Builds this builder into a InventoryView
     *
     * @param player the player to assign to the view
     * @return the created InventoryView
     */
    @NotNull
    V build(@NotNull final HumanEntity player);
}
