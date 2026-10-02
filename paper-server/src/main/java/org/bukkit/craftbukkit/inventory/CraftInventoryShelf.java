package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import org.bukkit.block.Shelf;
import org.bukkit.inventory.ShelfInventory;

public class CraftInventoryShelf extends CraftInventory implements ShelfInventory {

    public CraftInventoryShelf(ShelfBlockEntity inventory) {
        super(inventory);
    }

    @Override
    public Shelf getHolder() {
        return (Shelf) this.inventory.getOwner();
    }
}
