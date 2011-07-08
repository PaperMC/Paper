package net.minecraft.server;

import java.util.Random;

public class TileEntityDispenser extends TileEntity implements IInventory {

    private ItemStack[] items = new ItemStack[9];
    private Random b = new Random();

    // CraftBukkit start
    public ItemStack[] getContents() {
        return this.items;
    }
    // CraftBukkit end

    public TileEntityDispenser() {}

    public int getSize() {
        return 9;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                this.update();
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                this.update();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    // CraftBukkit - change signature
    public int findDispenseSlot() {
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.items.length; ++k) {
            if (this.items[k] != null && this.b.nextInt(j++) == 0) {
                if (this.items[k].count == 0) continue; // CraftBukkit
                i = k;
            }
        }

        // CraftBukkit start
        return i;
    }

    public ItemStack b() {
        int i = this.findDispenseSlot();
        // CraftBukkit end

        if (i >= 0) {
            return this.splitStack(i, 1);
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        this.update();
    }

    public String getName() {
        return "Trap";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound1.c("Slot") & 255;

            if (j >= 0 && j < this.items.length) {
                this.items[j] = new ItemStack(nbttagcompound1);
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Slot", (byte) i);
                this.items[i].a(nbttagcompound1);
                nbttaglist.a((NBTBase) nbttagcompound1);
            }
        }

        nbttagcompound.a("Items", (NBTBase) nbttaglist);
    }

    public int getMaxStackSize() {
        return 64;
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }
}
