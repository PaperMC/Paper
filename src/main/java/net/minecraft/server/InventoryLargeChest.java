package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public class InventoryLargeChest implements IInventory {

    private String a;
    public IInventory b; // CraftBukkit - private -> public
    public IInventory c; // CraftBukkit - private -> public

    // CraftBukkit start
    public List<HumanEntity> transaction = new ArrayList<HumanEntity>();
    
    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[this.getSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.getItem(i);
        }
        return result;
    }

    public void onOpen(CraftHumanEntity who) {
        b.onOpen(who);
        c.onOpen(who);
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        b.onClose(who);
        c.onClose(who);
        transaction.remove(who);
    }
    
    public List<HumanEntity> getViewers() {
        return transaction;
    }
    
    public InventoryHolder getOwner() {
        return null; // Double chests technically have multiple owners, so there's no sensible way to pick one
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

        this.b = iinventory;
        this.c = iinventory1;
    }

    public int getSize() {
        return this.b.getSize() + this.c.getSize();
    }

    public String getName() {
        return this.a;
    }

    public ItemStack getItem(int i) {
        return i >= this.b.getSize() ? this.c.getItem(i - this.b.getSize()) : this.b.getItem(i);
    }

    public ItemStack splitStack(int i, int j) {
        return i >= this.b.getSize() ? this.c.splitStack(i - this.b.getSize(), j) : this.b.splitStack(i, j);
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i >= this.b.getSize()) {
            this.c.setItem(i - this.b.getSize(), itemstack);
        } else {
            this.b.setItem(i, itemstack);
        }
    }

    public int getMaxStackSize() {
        return this.b.getMaxStackSize();
    }

    public void update() {
        this.b.update();
        this.c.update();
    }

    public boolean a(EntityHuman entityhuman) {
        return this.b.a(entityhuman) && this.c.a(entityhuman);
    }

    public void f() {
        this.b.f();
        this.c.f();
    }

    public void g() {
        this.b.g();
        this.c.g();
    }
}
