package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

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
    InventoryViewBuilder<V> copy();

    /**
     * Sets the title of the builder
     *
     * @param title the title, or null for a default title
     * @return this builder
     */
    InventoryViewBuilder<V> title(@Nullable final Component title);

    /**
     * Builds this builder into a InventoryView
     *
     * @param player the player to assign to the view
     * @return the created InventoryView
     */
    V build(final HumanEntity player);
}
