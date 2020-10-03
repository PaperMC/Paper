package net.minecraft.server;

public class InventoryLargeChest implements IInventory {

    public final IInventory left;
    public final IInventory right;

    public InventoryLargeChest(IInventory iinventory, IInventory iinventory1) {
        if (iinventory == null) {
            iinventory = iinventory1;
        }

        if (iinventory1 == null) {
            iinventory1 = iinventory;
        }

        this.left = iinventory;
        this.right = iinventory1;
    }

    @Override
    public int getSize() {
        return this.left.getSize() + this.right.getSize();
    }

    @Override
    public boolean isEmpty() {
        return this.left.isEmpty() && this.right.isEmpty();
    }

    public boolean a(IInventory iinventory) {
        return this.left == iinventory || this.right == iinventory;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= this.left.getSize() ? this.right.getItem(i - this.left.getSize()) : this.left.getItem(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        return i >= this.left.getSize() ? this.right.splitStack(i - this.left.getSize(), j) : this.left.splitStack(i, j);
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        return i >= this.left.getSize() ? this.right.splitWithoutUpdate(i - this.left.getSize()) : this.left.splitWithoutUpdate(i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        if (i >= this.left.getSize()) {
            this.right.setItem(i - this.left.getSize(), itemstack);
        } else {
            this.left.setItem(i, itemstack);
        }

    }

    @Override
    public int getMaxStackSize() {
        return this.left.getMaxStackSize();
    }

    @Override
    public void update() {
        this.left.update();
        this.right.update();
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.left.a(entityhuman) && this.right.a(entityhuman);
    }

    @Override
    public void startOpen(EntityHuman entityhuman) {
        this.left.startOpen(entityhuman);
        this.right.startOpen(entityhuman);
    }

    @Override
    public void closeContainer(EntityHuman entityhuman) {
        this.left.closeContainer(entityhuman);
        this.right.closeContainer(entityhuman);
    }

    @Override
    public boolean b(int i, ItemStack itemstack) {
        return i >= this.left.getSize() ? this.right.b(i - this.left.getSize(), itemstack) : this.left.b(i, itemstack);
    }

    @Override
    public void clear() {
        this.left.clear();
        this.right.clear();
    }
}
