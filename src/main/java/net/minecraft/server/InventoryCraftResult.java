package net.minecraft.server;

public class InventoryCraftResult implements IInventory {

    private ItemStack[] a = new ItemStack[1];

    // CraftBukkit start
    public ItemStack[] getContents() {
        return a;
    }
    // CraftBukkit end

    public InventoryCraftResult() {}

    public int m_() {
        return 1;
    }

    public ItemStack c_(int i) {
        return this.a[i];
    }

    public String c() {
        return "Result";
    }

    public ItemStack a(int i, int j) {
        if (this.a[i] != null) {
            ItemStack itemstack = this.a[i];

            this.a[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        this.a[i] = itemstack;
    }

    public int n_() {
        return 64;
    }

    public void h() {}

    public boolean a_(EntityHuman entityhuman) {
        return true;
    }
}
