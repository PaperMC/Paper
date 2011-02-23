package net.minecraft.server;

public class InventoryLargeChest implements IInventory {

    private String a;
    private IInventory b;
    private IInventory c;

    // CraftBukkit start
    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[m_()];
        for (int i = 0; i < result.length; i++) {
            result[i] = c_(i);
        }
        return result;
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, IInventory iinventory, IInventory iinventory1) {
        this.a = s;
        this.b = iinventory;
        this.c = iinventory1;
    }

    public int m_() {
        return this.b.m_() + this.c.m_();
    }

    public String c() {
        return this.a;
    }

    public ItemStack c_(int i) {
        return i >= this.b.m_() ? this.c.c_(i - this.b.m_()) : this.b.c_(i);
    }

    public ItemStack a(int i, int j) {
        return i >= this.b.m_() ? this.c.a(i - this.b.m_(), j) : this.b.a(i, j);
    }

    public void a(int i, ItemStack itemstack) {
        if (i >= this.b.m_()) {
            this.c.a(i - this.b.m_(), itemstack);
        } else {
            this.b.a(i, itemstack);
        }
    }

    public int n_() {
        return this.b.n_();
    }

    public void h() {
        this.b.h();
        this.c.h();
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.b.a_(entityhuman) && this.c.a_(entityhuman);
    }
}
