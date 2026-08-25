package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.view.MerchantView;

public class CraftTradeSelectEvent extends CraftInventoryInteractEvent implements TradeSelectEvent {

    private final int index;

    public CraftTradeSelectEvent(final MerchantView transaction, final int newIndex) {
        super(transaction);
        this.index = newIndex;
    }

    @Override
    public MerchantInventory getInventory() {
        return (MerchantInventory) super.getInventory();
    }

    @Override
    public Merchant getMerchant() {
        return this.getInventory().getMerchant();
    }

    @Override
    public MerchantView getView() {
        return (MerchantView) super.getView();
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public HandlerList getHandlers() {
        return TradeSelectEvent.getHandlerList();
    }
}
