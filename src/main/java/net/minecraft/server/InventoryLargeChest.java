package net.minecraft.server;

public class InventoryLargeChest implements IInventory {

    private String a;
    private IInventory b;
    private IInventory c;

    // CraftBukkit start
    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[h_()];
        for (int i = 0; i < result.length; i++) {
            result[i] = a(i);
        }
        return result;
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, IInventory iinventory, IInventory iinventory1) {
        this.a = s;
        this.b = iinventory;
        this.c = iinventory1;
    }

    public int h_() {
        return this.b.h_() + this.c.h_();
    }

    public String b() {
        return this.a;
    }

    public ItemStack a(int i) {
        return i >= this.b.h_() ? this.c.a(i - this.b.h_()) : this.b.a(i);
    }

    public ItemStack b(int i, int j) {
        return i >= this.b.h_() ? this.c.b(i - this.b.h_(), j) : this.b.b(i, j);
    }

    public void a(int i, ItemStack itemstack) {
        if (i >= this.b.h_()) {
            this.c.a(i - this.b.h_(), itemstack);
        } else {
            this.b.a(i, itemstack);
        }
    }

    public int c() {
        return this.b.c();
    }

    public void d() {
        this.b.d();
        this.c.d();
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.b.a_(entityhuman) && this.c.a_(entityhuman);
    }
}
