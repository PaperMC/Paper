package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public abstract class InventorySubcontainer implements IInventory { // CraftBukkit - abstract

    private String a;
    private int b;
    protected ItemStack[] items; // CraftBukkit - protected
    private List d;
    private boolean e;

    public InventorySubcontainer(String s, boolean flag, int i) {
        this.a = s;
        this.e = flag;
        this.b = i;
        this.items = new ItemStack[i];
    }

    public void a(IInventoryListener iinventorylistener) {
        if (this.d == null) {
            this.d = new ArrayList();
        }

        this.d.add(iinventorylistener);
    }

    public void b(IInventoryListener iinventorylistener) {
        this.d.remove(iinventorylistener);
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                this.update();
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                this.update();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public int getSize() {
        return this.b;
    }

    public String getInventoryName() {
        return this.a;
    }

    public boolean k_() {
        return this.e;
    }

    public void a(String s) {
        this.e = true;
        this.a = s;
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void update() {
        if (this.d != null) {
            for (int i = 0; i < this.d.size(); ++i) {
                ((IInventoryListener) this.d.get(i)).a(this);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void startOpen() {}

    public void l_() {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }
}
