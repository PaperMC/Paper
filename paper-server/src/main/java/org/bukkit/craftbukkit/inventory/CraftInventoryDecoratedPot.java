package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.block.DecoratedPot;
import org.bukkit.inventory.DecoratedPotInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDecoratedPot extends CraftInventory implements DecoratedPotInventory {

    public CraftInventoryDecoratedPot(Container inventory) {
        super(inventory);
    }

    @Override
    public void setItem(ItemStack item) {
        this.setItem(0, item);
    }

    @Override
    public ItemStack getItem() {
        return this.getItem(0);
    }

    @Override
    public DecoratedPot getHolder() {
        return (DecoratedPot) this.inventory.getOwner();
    }
}
