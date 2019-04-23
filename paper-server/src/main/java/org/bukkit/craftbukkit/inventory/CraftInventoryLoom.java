package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.LoomInventory;

public class CraftInventoryLoom extends CraftInventory implements LoomInventory {

    private final IInventory resultInventory;

    public CraftInventoryLoom(IInventory inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }
}
