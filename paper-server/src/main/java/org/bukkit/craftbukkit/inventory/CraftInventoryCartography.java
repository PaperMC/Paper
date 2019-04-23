package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.CartographyInventory;

public class CraftInventoryCartography extends CraftInventory implements CartographyInventory {

    public CraftInventoryCartography(IInventory inventory) {
        super(inventory);
    }
}
