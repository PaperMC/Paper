package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import org.bukkit.block.Shelf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShelfInventory;

public class CraftInventoryShelf extends CraftInventory implements ShelfInventory {

    public CraftInventoryShelf(ShelfBlockEntity inventory) {
        super(inventory);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);

        if (nms.isEmpty()) {
            this.getInventory().removeItemNoUpdate(index);
        } else {
            this.getInventory().setItem(index, nms);
        }
    }

    @Override
    public Shelf getHolder() {
        return (Shelf) this.inventory.getOwner();
    }
}
