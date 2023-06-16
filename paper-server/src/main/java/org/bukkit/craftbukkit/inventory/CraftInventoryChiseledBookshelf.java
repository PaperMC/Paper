package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryChiseledBookshelf extends CraftInventory implements ChiseledBookshelfInventory {

    public CraftInventoryChiseledBookshelf(ChiseledBookShelfBlockEntity inventory) {
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
    public ChiseledBookshelf getHolder() {
        return (ChiseledBookshelf) inventory.getOwner();
    }
}
