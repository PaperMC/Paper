package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;

public class CraftInventoryHorse extends CraftInventory {
    private final IInventory resultInventory = null;

    public CraftInventoryHorse(IInventory inventory) {
        super(inventory);
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        //return getResultInventory().getSize() + getIngredientsInventory().getSize();
        return getIngredientsInventory().getSize();
    }
}
