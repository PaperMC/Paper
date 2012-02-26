package net.minecraft.server;

import java.util.List;

public class ContainerEnchantTableSubcontainer implements IInventory {

    private String a;
    private int b;
    private ItemStack[] c;
    private List d;

    public ContainerEnchantTableSubcontainer(String s, int i) {
        this.a = s;
        this.b = i;
        this.c = new ItemStack[i];
    }

    public ItemStack getItem(int i) {
        return this.c[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.c[i] != null) {
            ItemStack itemstack;

            if (this.c[i].count <= j) {
                itemstack = this.c[i];
                this.c[i] = null;
                this.update();
                return itemstack;
            } else {
                itemstack = this.c[i].a(j);
                if (this.c[i].count == 0) {
                    this.c[i] = null;
                }

                this.update();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.c[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public int getSize() {
        return this.b;
    }

    public String getName() {
        return this.a;
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

    public void f() {}

    public void g() {}
}
