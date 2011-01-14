package net.minecraft.server;

public class Slot {

    public final int a; // CraftBukkit: private -> public
    public final IInventory b; // CraftBukkit: private -> public
    public int c;
    public int d;
    public int e;

    public Slot(IInventory iinventory, int i, int j, int k) {
        b = iinventory;
        a = i;
        d = j;
        e = k;
    }

    public void b() {
        d();
    }

    public boolean a(ItemStack itemstack) {
        return true;
    }

    public ItemStack c() {
        return b.a(a);
    }

    public void b(ItemStack itemstack) {
        b.a(a, itemstack);
        d();
    }

    public void d() {
        b.d();
    }

    public int a() {
        return b.c();
    }

    public ItemStack a(int i) {
        return b.b(a, i);
    }

    public boolean a(IInventory iinventory, int i) {
        return iinventory == b && i == a;
    }
}
