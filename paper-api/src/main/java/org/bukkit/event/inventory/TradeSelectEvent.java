package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.MerchantView;

/**
 * This event is called whenever a player clicks a new trade on the trades
 * sidebar.
 * <p>
 * This event allows the user to get the index of the trade, letting them get
 * the MerchantRecipe via the Merchant.
 */
public interface TradeSelectEvent extends InventoryInteractEvent {

    @Override
    MerchantInventory getInventory();

    /**
     * Get the Merchant involved.
     *
     * @return the Merchant
     */
    Merchant getMerchant();

    @Override
    MerchantView getView();

    /**
     * Used to get the index of the trade the player clicked on.
     *
     * @return The index of the trade clicked by the player
     */
    int getIndex();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
