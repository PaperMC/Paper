package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.GrindstoneInventory;

public class CraftInventoryGrindstone extends CraftInventory implements GrindstoneInventory {

    private final IInventory resultInventory;

    public CraftInventoryGrindstone(IInventory inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }
}
