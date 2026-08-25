package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryView;

public abstract class CraftInventoryInteractEvent extends CraftInventoryEvent implements InventoryInteractEvent {

    private Result result = Result.DEFAULT;

    protected CraftInventoryInteractEvent(final InventoryView transaction) {
        super(transaction);
    }

    @Override
    public void setResult(final Result newResult) {
        this.result = newResult;
    }

    @Override
    public Result getResult() {
        return this.result;
    }
}
