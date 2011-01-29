package net.minecraft.server;

public class Slot {

    public final int a; // CraftBukkit: private -> public
    public final IInventory b; // CraftBukkit: private -> public
    public int c;
    public int d;
    public int e;

    public Slot(IInventory iinventory, int i, int j, int k) {
        this.b = iinventory;
        this.a = i;
        this.d = j;
        this.e = k;
    }

    public void b() {
        this.d();
    }

    public boolean a(ItemStack itemstack) {
        return true;
    }

    public ItemStack c() {
        return this.b.a(this.a);
    }

    public void b(ItemStack itemstack) {
        this.b.a(this.a, itemstack);
        this.d();
    }

    public void d() {
        this.b.d();
    }

    public int a() {
        return this.b.c();
    }

    public ItemStack a(int i) {
        return this.b.b(this.a, i);
    }

    public boolean a(IInventory iinventory, int i) {
        return iinventory == this.b && i == this.a;
    }
}
