package org.bukkit.craftbukkit.inventory;

import java.util.HashMap;

import net.minecraft.server.IInventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class CraftInventory implements org.bukkit.inventory.Inventory {
    protected IInventory inventory;

    public CraftInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    public int getSize() {
        return getInventory().q_();
    }

    public String getName() {
        return getInventory().c();
    }

    public CraftItemStack getItem(int index) {
        return new CraftItemStack(getInventory().c_(index));
    }

    public CraftItemStack[] getContents() {
        CraftItemStack[] items = new CraftItemStack[getSize()];
        net.minecraft.server.ItemStack[] mcItems = getInventory().getContents();

        for (int i = 0; i < mcItems.length; i++ ) {
            items[i] = new CraftItemStack(mcItems[i]);
        }

        return items;
    }

    public void setContents(ItemStack[] items) {
        if (getInventory().getContents().length != items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getContents().length);
        }

        net.minecraft.server.ItemStack[] mcItems = getInventory().getContents();

        for (int i = 0; i < items.length; i++ ) {
            ItemStack item = items[i];
            if (item == null || item.getTypeId() <= 0) {
                mcItems[i] = null;
            } else {
                mcItems[i] = new net.minecraft.server.ItemStack( item.getTypeId(), item.getAmount(), item.getDurability());
            }
        }
    }

    public void setItem(int index, ItemStack item) {
        getInventory().a(index, (item == null ? null : new net.minecraft.server.ItemStack( item.getTypeId(), item.getAmount(), item.getDurability())));
    }

    public boolean contains(int materialId) {
        for (ItemStack item: getContents()) {
            if (item.getTypeId() == materialId) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Material material) {
        return contains(material.getId());
    }

    public boolean contains(ItemStack item) {
        for (ItemStack i: getContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(int materialId, int amount) {
        for (ItemStack item: getContents()) {
            if (item.getTypeId() == materialId && item.getAmount() >= amount) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Material material, int amount) {
        return contains(material.getId(), amount);
    }

    public boolean contains(ItemStack item, int amount) {
        for (ItemStack i: getContents()) {
            if (item.equals(i) && item.getAmount() >= amount) {
                return true;
            }
        }
        return false;
    }
    
    public HashMap<Integer, CraftItemStack> all(int materialId) {
        HashMap<Integer, CraftItemStack> slots = new HashMap<Integer, CraftItemStack>();

        CraftItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            CraftItemStack item = inventory[i];
            if (item.getTypeId() == materialId) {
                slots.put( i, item );
            }
        }
        return slots;
    }

    public HashMap<Integer, CraftItemStack> all(Material material) {
        return all(material.getId());
    }

    public HashMap<Integer, CraftItemStack> all(ItemStack item) {
        HashMap<Integer, CraftItemStack> slots = new HashMap<Integer, CraftItemStack>();

        CraftItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            if (item.equals(inventory[i])) {
                slots.put( i, inventory[i] );
            }
        }
        return slots;
    }

    public int first(int materialId) {
        CraftItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getTypeId() == materialId) {
                return i;
            }
        }
        return -1;
    }

    public int first(Material material) {
        return first(material.getId());
    }

    public int first(ItemStack item) {
        CraftItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            if (item.equals(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    public int firstEmpty() {
        return first(Material.AIR);
    }

    public int firstPartial(int materialId) {
        CraftItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            CraftItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    public int firstPartial(Material material) {
        return firstPartial(material.getId());
    }

    public int firstPartial(ItemStack item) {
        CraftItemStack[] inventory = getContents();
        for (int i = 0; i < inventory.length; i++) {
            CraftItemStack cItem = inventory[i];
            if (item != null && cItem.getTypeId() == item.getTypeId() && cItem.getAmount() < cItem.getMaxStackSize() && cItem.getDurability() == item.getDurability()) {
                return i;
            }
        }
        return -1;
    }

    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(item);

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
                            setItem( firstFree, new CraftItemStack(item.getTypeId(), getMaxItemStack(), item.getDurability()));
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                            setItem( firstFree, item );
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    CraftItemStack partialItem = getItem(firstPartial);

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

    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer,ItemStack> leftover = new HashMap<Integer,ItemStack>();

        // TODO: optimization

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = first(item.getType());

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount( toDelete );
                    leftover.put(i, item);
                    break;
                } else {
                    CraftItemStack itemStack = getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        clear( first );
                    } else {
                        // split the stack and store
                        itemStack.setAmount( amount - toDelete );
                        setItem( first, itemStack );
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return getInventory().r_();
    }

    public void remove(int materialId) {
        ItemStack[] items = getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getTypeId() == materialId) {
                clear(i);
            }
        }
    }

    public void remove(Material material) {
        remove(material.getId());
    }

    public void remove(ItemStack item) {
        ItemStack[] items = getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                clear(i);
            }
        }
    }

    public void clear(int index) {
        setItem(index, null);
    }

    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }
}
