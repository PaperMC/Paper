package net.minecraft.server;

public class TileEntityChest extends TileEntity implements IInventory {

    private ItemStack[] e = new ItemStack[36];

    // CraftBukkit start
    public ItemStack[] getContents() {
        return e;
    }
    // CraftBukkit end

    public TileEntityChest() {}

    public int h_() {
        return 27;
    }

    public ItemStack a(int i) {
        return this.e[i];
    }

    public ItemStack b(int i, int j) {
        if (this.e[i] != null) {
            ItemStack itemstack;

            if (this.e[i].count <= j) {
                itemstack = this.e[i];
                this.e[i] = null;
                this.d();
                return itemstack;
            } else {
                itemstack = this.e[i].a(j);
                if (this.e[i].count == 0) {
                    this.e[i] = null;
                }

                this.d();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        this.e[i] = itemstack;
        if (itemstack != null && itemstack.count > this.c()) {
            itemstack.count = this.c();
        }

        this.d();
    }

    public String b() {
        return "Chest";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.k("Items");

        this.e = new ItemStack[this.h_()];

        for (int i = 0; i < nbttaglist.b(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound1.b("Slot") & 255;

            if (j >= 0 && j < this.e.length) {
                this.e[j] = new ItemStack(nbttagcompound1);
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.e.length; ++i) {
            if (this.e[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Slot", (byte) i);
                this.e[i].a(nbttagcompound1);
                nbttaglist.a((NBTBase) nbttagcompound1);
            }
        }

        nbttagcompound.a("Items", (NBTBase) nbttaglist);
    }

    public int c() {
        return 64;
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.a.getTileEntity(this.b, this.c, this.d) != this ? false : entityhuman.d((double) this.b + 0.5D, (double) this.c + 0.5D, (double) this.d + 0.5D) <= 64.0D;
    }
}
