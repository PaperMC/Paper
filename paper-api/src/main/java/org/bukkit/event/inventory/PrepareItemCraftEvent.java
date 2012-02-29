package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

public class PrepareItemCraftEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private boolean repair;
    private CraftingInventory matrix;

    public PrepareItemCraftEvent(CraftingInventory what, InventoryView view, boolean isRepair) {
        super(view);
        this.matrix = what;
        this.repair = isRepair;
    }

    /**
     * Get the recipe that has been formed. If this event was triggered by a tool repair, this
     * will be a temporary shapeless recipe representing the repair.
     * @return The recipe being crafted.
     */
    public Recipe getRecipe() {
        return matrix.getRecipe();
    }

    /**
     * @return The crafting inventory on which the recipe was formed.
     */
    @Override
    public CraftingInventory getInventory() {
        return matrix;
    }

    /**
     * Check if this event was triggered by a tool repair operation rather than a crafting recipe.
     * @return True if this is a repair.
     */
    public boolean isRepair() {
        return repair;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
