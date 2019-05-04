package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.CartographyInventory;

public class CraftInventoryCartography extends CraftInventory implements CartographyInventory {

    private final IInventory resultInventory;

    public CraftInventoryCartography(IInventory inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    @Override
    public int getSize() {
        return super.getSize() + resultInventory.getSize();
    }
}
