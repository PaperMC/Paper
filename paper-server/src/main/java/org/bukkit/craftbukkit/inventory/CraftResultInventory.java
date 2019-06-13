package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.ItemStack;

public class CraftResultInventory extends CraftInventory {

    private final IInventory resultInventory;

    public CraftResultInventory(IInventory inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().getSize()) {
            net.minecraft.server.ItemStack item = getIngredientsInventory().getItem(slot);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.server.ItemStack item = getResultInventory().getItem(slot - getIngredientsInventory().getSize());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().getSize()) {
            getIngredientsInventory().setItem(index, CraftItemStack.asNMSCopy(item));
        } else {
            getResultInventory().setItem((index - getIngredientsInventory().getSize()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().getSize() + getIngredientsInventory().getSize();
    }
}
