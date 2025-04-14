package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * An InventoryViewBuilder that can be bound by location within the world
 *
 * @param <V> the type of InventoryView created from this builder
 */
@ApiStatus.Experimental
public interface LocationInventoryViewBuilder<V extends InventoryView> extends InventoryViewBuilder<V> {

    @Override
    LocationInventoryViewBuilder<V> copy();

    @Override
    LocationInventoryViewBuilder<V> title(final @Nullable Component title);

    /**
     * Determines whether or not the server should check if the player can reach
     * the location.
     * <p>
     * Not providing a location but setting checkReachable to true will
     * automatically close the view when opened.
     * <p>
     * If checkReachable is set to false and a location is set on the builder if
     * the target block exists and this builder is the correct menu for that
     * block, e.g. MenuType.GENERIC_9X3 builder and target block set to chest,
     * if that block is destroyed the view would persist.
     *
     * @param checkReachable whether or not to check if the view is "reachable"
     * @return this builder
     */
    LocationInventoryViewBuilder<V> checkReachable(final boolean checkReachable);

    /**
     * Binds a location to this builder.
     * <p>
     * By binding a location in an unloaded chunk to this builder it is likely
     * that the given chunk the location is will load. That means that when,
     * building this view it may come with the costs associated with chunk
     * loading.
     * <p>
     * Providing a location of a block entity with a non matching menu comes with
     * extra costs associated with ensuring that the correct view is created.
     *
     * @param location the location to bind to this view
     * @return this builder
     */
    LocationInventoryViewBuilder<V> location(final Location location);
}
