
package org.bukkit.craftbukkit.event.inventory;

import java.util.List;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftInventoryEvent extends CraftEvent implements InventoryEvent {

    protected InventoryView transaction;

    public CraftInventoryEvent(final InventoryView transaction) {
        this.transaction = transaction;
    }

    @Override
    public Inventory getInventory() {
        return this.transaction.getTopInventory();
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.transaction.getTopInventory().getViewers();
    }

    @Override
    public InventoryView getView() {
        return this.transaction;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryEvent.getHandlerList();
    }
}
