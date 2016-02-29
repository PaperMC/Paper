package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an item is put in a slot for repair by an anvil.
 */
public class PrepareAnvilEvent extends InventoryEvent {

    private static final HandlerList handlers = new HandlerList();
    private ItemStack result;

    public PrepareAnvilEvent(InventoryView inventory, ItemStack result) {
        super(inventory);
        this.result = result;
    }

    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    /**
     * Get result item, may be null.
     *
     * @return result item
     */
    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
