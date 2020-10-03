package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryLargeChest implements IInventory {

    public final IInventory left;
    public final IInventory right;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

    public List<ItemStack> getContents() {
        List<ItemStack> result = new ArrayList<ItemStack>(this.getSize());
        for (int i = 0; i < this.getSize(); i++) {
            result.add(this.getItem(i));
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

    @Override
    public Location getLocation() {
        return left.getLocation(); // TODO: right?
    }
    // CraftBukkit end

    public InventoryLargeChest(IInventory iinventory, IInventory iinventory1) {
        if (iinventory == null) {
            iinventory = iinventory1;
        }

        if (iinventory1 == null) {
            iinventory1 = iinventory;
        }

        this.left = iinventory;
        this.right = iinventory1;
    }

    @Override
    public int getSize() {
        return this.left.getSize() + this.right.getSize();
    }

    @Override
    public boolean isEmpty() {
        return this.left.isEmpty() && this.right.isEmpty();
    }

    public boolean a(IInventory iinventory) {
        return this.left == iinventory || this.right == iinventory;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= this.left.getSize() ? this.right.getItem(i - this.left.getSize()) : this.left.getItem(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        return i >= this.left.getSize() ? this.right.splitStack(i - this.left.getSize(), j) : this.left.splitStack(i, j);
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        return i >= this.left.getSize() ? this.right.splitWithoutUpdate(i - this.left.getSize()) : this.left.splitWithoutUpdate(i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        if (i >= this.left.getSize()) {
            this.right.setItem(i - this.left.getSize(), itemstack);
        } else {
            this.left.setItem(i, itemstack);
        }

    }

    @Override
    public int getMaxStackSize() {
        return Math.min(this.left.getMaxStackSize(), this.right.getMaxStackSize()); // CraftBukkit - check both sides
    }

    @Override
    public void update() {
        this.left.update();
        this.right.update();
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.left.a(entityhuman) && this.right.a(entityhuman);
    }

    @Override
    public void startOpen(EntityHuman entityhuman) {
        this.left.startOpen(entityhuman);
        this.right.startOpen(entityhuman);
    }

    @Override
    public void closeContainer(EntityHuman entityhuman) {
        this.left.closeContainer(entityhuman);
        this.right.closeContainer(entityhuman);
    }

    @Override
    public boolean b(int i, ItemStack itemstack) {
        return i >= this.left.getSize() ? this.right.b(i - this.left.getSize(), itemstack) : this.left.b(i, itemstack);
    }

    @Override
    public void clear() {
        this.left.clear();
        this.right.clear();
    }
}
