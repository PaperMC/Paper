package net.minecraft.server;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryLargeChest implements IInventory {

    private String a;
    public IInventory left; // CraftBukkit - private -> public
    public IInventory right; // CraftBukkit - private -> public

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[this.getSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.getItem(i);
        }
        return result;
    }

    public void onOpen(CraftHumanEntity who) {
        this.left.onOpen(who);
        this.right.onOpen(who);
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        this.left.onClose(who);
        this.right.onClose(who);
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return null; // This method won't be called since CraftInventoryDoubleChest doesn't defer to here
    }

    public void setMaxStackSize(int size) {
        this.left.setMaxStackSize(size);
        this.right.setMaxStackSize(size);
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, IInventory iinventory, IInventory iinventory1) {
        this.a = s;
        if (iinventory == null) {
            iinventory = iinventory1;
        }

        if (iinventory1 == null) {
            iinventory1 = iinventory;
        }

        this.left = iinventory;
        this.right = iinventory1;
    }

    public int getSize() {
        return this.left.getSize() + this.right.getSize();
    }

    public boolean a(IInventory iinventory) {
        return this.left == iinventory || this.right == iinventory;
    }

    public String getName() {
        return this.left.c() ? this.left.getName() : (this.right.c() ? this.right.getName() : this.a);
    }

    public boolean c() {
        return this.left.c() || this.right.c();
    }

    public ItemStack getItem(int i) {
        return i >= this.left.getSize() ? this.right.getItem(i - this.left.getSize()) : this.left.getItem(i);
    }

    public ItemStack splitStack(int i, int j) {
        return i >= this.left.getSize() ? this.right.splitStack(i - this.left.getSize(), j) : this.left.splitStack(i, j);
    }

    public ItemStack splitWithoutUpdate(int i) {
        return i >= this.left.getSize() ? this.right.splitWithoutUpdate(i - this.left.getSize()) : this.left.splitWithoutUpdate(i);
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i >= this.left.getSize()) {
            this.right.setItem(i - this.left.getSize(), itemstack);
        } else {
            this.left.setItem(i, itemstack);
        }
    }

    public int getMaxStackSize() {
        return Math.min(this.left.getMaxStackSize(), this.right.getMaxStackSize()); // CraftBukkit - check both sides
    }

    public void update() {
        this.left.update();
        this.right.update();
    }

    public boolean a(EntityHuman entityhuman) {
        return this.left.a(entityhuman) && this.right.a(entityhuman);
    }

    public void startOpen() {
        this.left.startOpen();
        this.right.startOpen();
    }

    public void g() {
        this.left.g();
        this.right.g();
    }

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }
}
