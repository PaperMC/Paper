package org.bukkit.inventory.view;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * merchant view data.
 *
 * @since 1.21
 */
public interface MerchantView extends InventoryView {

    /**
     * @since 1.21.1
     */
    @NotNull
    @Override
    MerchantInventory getTopInventory();

    /**
     * Gets the merchant that this view is for.
     *
     * @return The merchant that this view uses
     */
    @NotNull
    Merchant getMerchant();
}
