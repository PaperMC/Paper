package net.minecraft.server;

public class Slot {

    public final int d; // CraftBukkit: private -> public
    public final IInventory e; // CraftBukkit: private -> public
    public int a;
    public int b;
    public int c;

    public Slot(IInventory iinventory, int i, int j, int k) {
        this.e = iinventory;
        this.d = i;
        this.b = j;
        this.c = k;
    }

    public void a() {
        this.c();
    }

    public boolean a(ItemStack itemstack) {
        return true;
    }

    public ItemStack b() {
        return this.e.c_(this.d);
    }

    public void b(ItemStack itemstack) {
        this.e.a(this.d, itemstack);
        this.c();
    }

    public void c() {
        this.e.h();
    }

    public int d() {
        return this.e.n_();
    }

    public ItemStack a(int i) {
        return this.e.a(this.d, i);
    }

    public boolean a(IInventory iinventory, int i) {
        return iinventory == this.e && i == this.d;
    }
}
