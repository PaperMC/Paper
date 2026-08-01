package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Merchant;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * An InventoryViewBuilder for creating merchant views
 *
 * @param <V> the type of InventoryView created by this builder
 */
@ApiStatus.Experimental
public interface MerchantInventoryViewBuilder<V extends InventoryView> extends InventoryViewBuilder<V> {

    @Override
    MerchantInventoryViewBuilder<V> copy();

    @Override
    MerchantInventoryViewBuilder<V> title(final @Nullable Component title);

    /**
     * Adds a merchant to this builder
     *
     * @param merchant the merchant
     * @return this builder
     * @see Server#createMerchant()
     */
    MerchantInventoryViewBuilder<V> merchant(final Merchant merchant);

    /**
     * Determines whether or not the server should check if the player can reach
     * the location.
     * <p>
     * Given checkReachable is provided and a virtual merchant is provided to
     * the builder from {@link Server#createMerchant()} this method will
     * have no effect on the actual menu status.
     *
     * @param checkReachable whether or not to check if the view is "reachable"
     * @return this builder
     */
    MerchantInventoryViewBuilder<V> checkReachable(final boolean checkReachable);
}
