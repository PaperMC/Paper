package net.minecraft.server;

public class InventoryCrafting implements IInventory {

    private ItemStack a[];
    private int b;
    private CraftingInventoryCB c;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return a;
    }
    // CraftBukkit end

    public InventoryCrafting(CraftingInventoryCB craftinginventorycb, int i, int j) {
        int k = i * j;

        a = new ItemStack[k];
        c = craftinginventorycb;
        b = i;
    }

    public int h_() {
        return a.length;
    }

    public ItemStack a(int i) {
        if (i >= h_()) {
            return null;
        } else {
            return a[i];
        }
    }

    public ItemStack a(int i, int j) {
        if (i < 0 || i >= b) {
            return null;
        } else {
            int k = i + j * b;

            return a(k);
        }
    }

    public String b() {
        return "Crafting";
    }

    public ItemStack b(int i, int j) {
        if (a[i] != null) {
            if (a[i].a <= j) {
                ItemStack itemstack = a[i];

                a[i] = null;
                c.a(((IInventory) (this)));
                return itemstack;
            }
            ItemStack itemstack1 = a[i].a(j);

            if (a[i].a == 0) {
                a[i] = null;
            }
            c.a(((IInventory) (this)));
            return itemstack1;
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        a[i] = itemstack;
        c.a(((IInventory) (this)));
    }

    public int c() {
        return 64;
    }

    public void d() {}

    public boolean a_(EntityPlayer entityplayer) {
        return true;
    }
}
