package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
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

    @Override
    public boolean hasNext() {
        return nextIndex < inventory.getSize();
    }

    @Override
    public ItemStack next() {
        lastDirection = true;
        return inventory.getItem(nextIndex++);
    }

    @Override
    public int nextIndex() {
        return nextIndex;
    }

    @Override
    public boolean hasPrevious() {
        return nextIndex > 0;
    }

    @Override
    public ItemStack previous() {
        lastDirection = false;
        return inventory.getItem(--nextIndex);
    }

    @Override
    public int previousIndex() {
        return nextIndex - 1;
    }

    @Override
    public void set(ItemStack item) {
        Preconditions.checkState(lastDirection != null, "No current item!");
        int i = lastDirection ? nextIndex - 1 : nextIndex;
        inventory.setItem(i, item);
    }

    @Override
    public void add(ItemStack item) {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
}
