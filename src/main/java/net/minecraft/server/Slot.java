package net.minecraft.server;

public class Slot {

    public final int index; // CraftBukkit: private -> public
    public final IInventory inventory; // CraftBukkit: private -> public
    public int a;
    public int b;
    public int c;

    public Slot(IInventory iinventory, int i, int j, int k) {
        this.inventory = iinventory;
        this.index = i;
        this.b = j;
        this.c = k;
    }

    public void a(ItemStack itemstack) {
        this.c();
    }

    public boolean isAllowed(ItemStack itemstack) {
        return true;
    }

    public ItemStack getItem() {
        return this.inventory.getItem(this.index);
    }

    public boolean b() {
        return this.getItem() != null;
    }

    public void c(ItemStack itemstack) {
        this.inventory.setItem(this.index, itemstack);
        this.c();
    }

    public void c() {
        this.inventory.update();
    }

    public int d() {
        return this.inventory.getMaxStackSize();
    }

    public ItemStack a(int i) {
        return this.inventory.a(this.index, i);
    }

    public boolean a(IInventory iinventory, int i) {
        return iinventory == this.inventory && i == this.index;
    }

    public boolean e() {
        return false;
    }
}
