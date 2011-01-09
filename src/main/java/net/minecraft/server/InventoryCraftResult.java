package net.minecraft.server;


public class InventoryCraftResult
        implements IInventory {

    private ItemStack a[];

    // CraftBukkit
    public ItemStack[] getContents() {
        return a;
    }

    public InventoryCraftResult() {
        a = new ItemStack[1];
    }

    public int a() {
        return 1;
    }

    public ItemStack a(int i) {
        return a[i];
    }

    public String b() {
        return "Result";
    }

    public ItemStack a(int i, int j) {
        if (a[i] != null) {
            ItemStack itemstack = a[i];

            a[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        a[i] = itemstack;
    }

    public int c() {
        return 64;
    }

    public void d() {}

    public boolean a_(EntityPlayer entityplayer) {
        return true;
    }
}
