package org.bukkit.craftbukkit.inventory;

import java.util.ListIterator;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryIterator implements ListIterator<ItemStack> {
    private final Inventory inventory;
    private int nextIndex;
    private Boolean lastDirection; // true = forward, false = backward, null = haven't moved yet

    InventoryIterator(Inventory craftInventory) {
        this.inventory = craftInventory;
        this.nextIndex = 0;
    }

    InventoryIterator(Inventory craftInventory, int index) {
        this.inventory = craftInventory;
        this.nextIndex = index;
    }

    public boolean hasNext() {
        return nextIndex < inventory.getSize();
    }

    public ItemStack next() {
        lastDirection = true;
        return inventory.getItem(nextIndex++);
    }

    public int nextIndex() {
        return nextIndex;
    }

    public boolean hasPrevious() {
        return nextIndex > 0;
    }

    public ItemStack previous() {
        lastDirection = false;
        return inventory.getItem(--nextIndex);
    }

    public int previousIndex() {
        return nextIndex - 1;
    }

    public void set(ItemStack item) {
        if (lastDirection == null) {
            throw new IllegalStateException("No current item!");
        }
        int i = lastDirection ? nextIndex - 1 : nextIndex;
        inventory.setItem(i, item);
    }

    public void add(ItemStack item) {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }

    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
}