package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.jspecify.annotations.Nullable;

public class CraftPrepareItemCraftEvent extends CraftInventoryEvent implements PrepareItemCraftEvent {

    private final boolean repair;
    private final CraftingInventory matrix;

    public CraftPrepareItemCraftEvent(final CraftingInventory matrix, final InventoryView view, final boolean isRepair) {
        super(view);
        this.matrix = matrix;
        this.repair = isRepair;
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return this.matrix.getRecipe();
    }

    @Override
    public CraftingInventory getInventory() {
        return this.matrix;
    }

    @Override
    public boolean isRepair() {
        return this.repair;
    }

    @Override
    public HandlerList getHandlers() {
        return PrepareItemCraftEvent.getHandlerList();
    }
}
