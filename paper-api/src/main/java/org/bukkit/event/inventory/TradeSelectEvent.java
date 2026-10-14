package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.MerchantView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called whenever a player clicks a new trade on the trades
 * sidebar.
 * <p>
 * This event allows the user to get the index of the trade, letting them get
 * the MerchantRecipe via the Merchant.
 */
public class TradeSelectEvent extends InventoryInteractEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int index;

    @ApiStatus.Internal
    public TradeSelectEvent(@NotNull MerchantView transaction, int newIndex) {
        super(transaction);
        this.index = newIndex;
    }

    @NotNull
    @Override
    public MerchantInventory getInventory() {
        return (MerchantInventory) super.getInventory();
    }

    /**
     * Get the Merchant involved.
     *
     * @return the Merchant
     */
    @NotNull
    public Merchant getMerchant() {
        return this.getInventory().getMerchant();
    }

    @NotNull
    @Override
    public MerchantView getView() {
        return (MerchantView) super.getView();
    }

    /**
     * Used to get the index of the trade the player clicked on.
     *
     * @return The index of the trade clicked by the player
     */
    public int getIndex() {
        return this.index;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
