package net.minecraft.server;

public class InventoryLargeChest implements IInventory {

    private String a;
    private IInventory b;
    private IInventory c;

    // CraftBukkit start
    public ItemStack[] getContents() {
        ItemStack[] result = new ItemStack[getSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getItem(i);
        }
        return result;
    }
    // CraftBukkit end

    public InventoryLargeChest(String s, IInventory iinventory, IInventory iinventory1) {
        this.a = s;
        this.b = iinventory;
        this.c = iinventory1;
    }

    public int getSize() {
        return this.b.getSize() + this.c.getSize();
    }

    public String getName() {
        return this.a;
    }

    public ItemStack getItem(int i) {
        return i >= this.b.getSize() ? this.c.getItem(i - this.b.getSize()) : this.b.getItem(i);
    }

    public ItemStack a(int i, int j) {
        return i >= this.b.getSize() ? this.c.a(i - this.b.getSize(), j) : this.b.a(i, j);
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i >= this.b.getSize()) {
            this.c.setItem(i - this.b.getSize(), itemstack);
        } else {
            this.b.setItem(i, itemstack);
        }
    }

    public int getMaxStackSize() {
        return this.b.getMaxStackSize();
    }

    public void update() {
        this.b.update();
        this.c.update();
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.b.a_(entityhuman) && this.c.a_(entityhuman);
    }
}
