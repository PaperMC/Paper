package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.StonecutterInventory;

public class CraftInventoryStonecutter extends CraftInventory implements StonecutterInventory {

    public CraftInventoryStonecutter(IInventory inventory) {
        super(inventory);
    }
}
