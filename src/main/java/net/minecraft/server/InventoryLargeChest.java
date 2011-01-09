package net.minecraft.server;


public class InventoryLargeChest
        implements IInventory {

    private String a;
    private IInventory b;
    private IInventory c;
    
    // CraftBukkit start
    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[a()];
        for (int i = 0; i < result.length; i++) {
            result[i] = a(i);
        }
        return result;
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, IInventory iinventory, IInventory iinventory1) {
        a = s;
        b = iinventory;
        c = iinventory1;
    }

    public int a() {
        return b.a() + c.a();
    }

    public String b() {
        return a;
    }

    public ItemStack a(int i) {
        if (i >= b.a()) {
            return c.a(i - b.a());
        } else {
            return b.a(i);
        }
    }

    public ItemStack a(int i, int j) {
        if (i >= b.a()) {
            return c.a(i - b.a(), j);
        } else {
            return b.a(i, j);
        }
    }

    public void a(int i, ItemStack itemstack) {
        if (i >= b.a()) {
            c.a(i - b.a(), itemstack);
        } else {
            b.a(i, itemstack);
        }
    }

    public int c() {
        return b.c();
    }

    public void d() {
        b.d();
        c.d();
    }

    public boolean a_(EntityPlayer entityplayer) {
        return b.a_(entityplayer) && c.a_(entityplayer);
    }
}
