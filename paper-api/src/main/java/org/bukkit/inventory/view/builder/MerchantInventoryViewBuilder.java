package org.bukkit.inventory.view.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Merchant;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * An InventoryViewBuilder for creating merchant views
 *
 * @param <V> the type of InventoryView created by this builder
 */
@ApiStatus.Experimental
public interface MerchantInventoryViewBuilder<V extends InventoryView> extends InventoryViewBuilder<V> {

    @NotNull
    @Override
    MerchantInventoryViewBuilder<V> copy();

    @NotNull
    @Override
    MerchantInventoryViewBuilder<V> title(final @NotNull Component title);

    @NotNull
    @Override
    MerchantInventoryViewBuilder<V> title(final @NotNull String title);


    /**
     * Adds a merchant to this builder
     *
     * @param merchant the merchant
     * @return this builder
     */
    @NotNull
    MerchantInventoryViewBuilder<V> merchant(@NotNull final Merchant merchant);

    /**
     * Determines whether or not the server should check if the player can reach
     * the location.
     * <p>
     * Given checkReachable is provided and a virtual merchant is provided to
     * the builder from {@link Server#createMerchant(net.kyori.adventure.text.Component)} this method will
     * have no effect on the actual menu status.
     *
     * @param checkReachable whether or not to check if the view is "reachable"
     * @return this builder
     */
    @NotNull
    MerchantInventoryViewBuilder<V> checkReachable(final boolean checkReachable);
}
