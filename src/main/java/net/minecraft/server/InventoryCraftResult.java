package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryCraftResult implements IInventory {

    private ItemStack[] items = new ItemStack[1];

    // CraftBukkit start
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return this.items;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return null; // Result slots don't get an owner
    }

    // Don't need a transaction; the InventoryCrafting keeps track of it for us
    public void onOpen(CraftHumanEntity who) {}
    public void onClose(CraftHumanEntity who) {}
    public java.util.List<HumanEntity> getViewers() {
        return new java.util.ArrayList<HumanEntity>();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public InventoryCraftResult() {}

    public int getSize() {
        return 1;
    }

    public ItemStack getItem(int i) {
        return this.items[0];
    }

    public String getName() {
        return "Result";
    }

    public boolean c() {
        return false;
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[0] != null) {
            ItemStack itemstack = this.items[0];

            this.items[0] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[0] != null) {
            ItemStack itemstack = this.items[0];

            this.items[0] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[0] = itemstack;
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public void update() {}

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void startOpen() {}

    public void g() {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }
}
