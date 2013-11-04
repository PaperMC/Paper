package net.minecraft.server;

public class Slot {

    public final int index; // CraftBukkit - private -> public
    public final IInventory inventory;
    public int rawSlotIndex;
    public int h;
    public int i;

    public Slot(IInventory iinventory, int i, int j, int k) {
        this.inventory = iinventory;
        this.index = i;
        this.h = j;
        this.i = k;
    }

    public void a(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack != null && itemstack1 != null) {
            if (itemstack.getItem() == itemstack1.getItem()) {
                int i = itemstack1.count - itemstack.count;

                if (i > 0) {
                    this.a(itemstack, i);
                }
            }
        }
    }

    protected void a(ItemStack itemstack, int i) {}

    protected void b(ItemStack itemstack) {}

    public void a(EntityHuman entityhuman, ItemStack itemstack) {
        this.f();
    }

    public boolean isAllowed(ItemStack itemstack) {
        return true;
    }

    public ItemStack getItem() {
        return this.inventory.getItem(this.index);
    }

    public boolean e() {
        return this.getItem() != null;
    }

    public void set(ItemStack itemstack) {
        this.inventory.setItem(this.index, itemstack);
        this.f();
    }

    public void f() {
        this.inventory.update();
    }

    public int a() {
        return this.inventory.getMaxStackSize();
    }

    public ItemStack a(int i) {
        return this.inventory.splitStack(this.index, i);
    }

    public boolean a(IInventory iinventory, int i) {
        return iinventory == this.inventory && i == this.index;
    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }
}
