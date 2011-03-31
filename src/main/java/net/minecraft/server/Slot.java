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

    public void a(ItemStack itemstack) {
        this.b();
    }

    public boolean b(ItemStack itemstack) {
        return true;
    }

    public ItemStack a() {
        return this.e.c_(this.d);
    }

    public void c(ItemStack itemstack) {
        this.e.a(this.d, itemstack);
        this.b();
    }

    public void b() {
        this.e.i();
    }

    public int c() {
        return this.e.r_();
    }

    public ItemStack a(int i) {
        return this.e.a(this.d, i);
    }

    public boolean a(IInventory iinventory, int i) {
        return iinventory == this.e && i == this.d;
    }

    public boolean d() {
        return false;
    }
}
