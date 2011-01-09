package org.bukkit.craftbukkit;

import java.util.HashMap;

import net.minecraft.server.IInventory;

import org.bukkit.ItemStack;
import org.bukkit.Material;

public class CraftInventory implements org.bukkit.Inventory {
    protected IInventory inventory;

    public CraftInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    public int getSize() {
        return getInventory().a();
    }

    public String getName() {
        return getInventory().b();
    }

    public CraftItemStack getItem(int index) {
        return new CraftItemStack(getInventory().a(index));
    }

    public CraftItemStack[] getContents() {
        CraftItemStack[] items = new CraftItemStack[getSize()];
        net.minecraft.server.ItemStack[] mcItems = getInventory().getContents();

        for (int i = 0; i < mcItems.length; i++ ) {
            items[i] = new CraftItemStack(mcItems[i]);
        }

        return items;
    }

    public void setItem(int index, ItemStack item) {
        getInventory().a( index, new net.minecraft.server.ItemStack( item.getTypeID(), item.getAmount()));
    }

    public boolean contains(int materialId) {
        for (ItemStack item: getContents()) {
            if (item.getTypeID() == materialId) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Material material) {
        return contains(material.getID());
    }

    public boolean contains(ItemStack item) {
        for (ItemStack i: getContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

        ItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item.getTypeID() == materialId) {
                slots.put( i, item );
            }
        }
        return slots;
    }

    public HashMap<Integer, ItemStack> all(Material material) {
        return all(material.getID());
    }

    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

        ItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            if (item.equals(inventory[i])) {
                slots.put( i, item );
            }
        }
        return slots;
    }

    public int first(int materialId) {
        ItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getTypeID() == materialId) {
                return i;
            }
        }
        return -1;
    }

    public int first(Material material) {
        return first(material.getID());
    }

    public int first(ItemStack item) {
        ItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            if (item.equals(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    public int firstEmpty() {
        return first(Material.Air);
    }

    public int firstPartial(int materialId) {
        ItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getTypeID() == materialId && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    public int firstPartial(Material material) {
        return firstPartial(material.getID());
    }

    public int firstPartial(ItemStack item) {
        return firstPartial(item.getTypeID());
    }
    
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer,ItemStack> leftover = new HashMap<Integer,ItemStack>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial( item.getTypeID() );

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxItemStack()) {
                            setItem( firstFree, new ItemStack(item.getTypeID(), getMaxItemStack()));
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                            setItem( firstFree, item );
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();
    
                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount( amount + partialAmount );
                        break;
                    }
    
                    // It fits partially
                    partialItem.setAmount( maxAmount );
                    item.setAmount( amount + partialAmount - maxAmount );
                }
            }
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return getInventory().c();
    }

}
