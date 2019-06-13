package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.LoomInventory;

public class CraftInventoryLoom extends CraftResultInventory implements LoomInventory {

    public CraftInventoryLoom(IInventory inventory, IInventory resultInventory) {
        super(inventory, resultInventory);
    }
}
