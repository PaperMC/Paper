package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.MerchantView;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called whenever a player clicks a new trade on the trades
 * sidebar.
 * <p>
 * This event allows the user to get the index of the trade, letting them get
 * the MerchantRecipe via the Merchant.
 *
 * @since 1.14
 */
public class TradeSelectEvent extends InventoryInteractEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final int index;

    public TradeSelectEvent(@NotNull MerchantView transaction, int newIndex) {
        super(transaction);
        this.index = newIndex;
    }

    /**
     * Used to get the index of the trade the player clicked on.
     *
     * @return The index of the trade clicked by the player
     */
    public int getIndex() {
        return index;
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
        return getInventory().getMerchant();
    }

    /**
     * @since 1.21
     */
    @NotNull
    @Override
    public MerchantView getView() {
        return (MerchantView) super.getView();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
