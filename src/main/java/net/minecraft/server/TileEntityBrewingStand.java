package net.minecraft.server;

import java.util.List;

public class TileEntityBrewingStand extends TileEntity implements IInventory {

    public ItemStack[] a = new ItemStack[4]; // CraftBukkit private -> public
    public int b; // CraftBukkit private -> public
    private int c;
    private int d;

    public TileEntityBrewingStand() {}

    // CraftBukkit start
    public ItemStack[] getContents() {
        return this.a;
    }
    // CraftBukkit end

    public String getName() {
        return "Brewing Stand";
    }

    public int getSize() {
        return this.a.length;
    }

    public void l_() {
        if (this.b > 0) {
            --this.b;
            if (this.b == 0) {
                this.p();
                this.update();
            } else if (!this.o()) {
                this.b = 0;
                this.update();
            } else if (this.d != this.a[3].id) {
                this.b = 0;
                this.update();
            }
        } else if (this.o()) {
            this.b = 400;
            this.d = this.a[3].id;
        }

        int i = this.n();

        if (i != this.c) {
            this.c = i;
            this.world.setData(this.x, this.y, this.z, i);
        }

        super.l_();
    }

    public int h() {
        return this.b;
    }

    private boolean o() {
        if (this.a[3] != null && this.a[3].count > 0) {
            ItemStack itemstack = this.a[3];

            if (!Item.byId[itemstack.id].n()) {
                return false;
            } else {
                boolean flag = false;

                for (int i = 0; i < 3; ++i) {
                    if (this.a[i] != null && this.a[i].id == Item.POTION.id) {
                        int j = this.a[i].getData();
                        int k = this.b(j, itemstack);

                        if (!ItemPotion.c(j) && ItemPotion.c(k)) {
                            flag = true;
                            break;
                        }

                        List list = Item.POTION.b(j);
                        List list1 = Item.POTION.b(k);

                        if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null) && j != k) {
                            flag = true;
                            break;
                        }
                    }
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    private void p() {
        if (this.o()) {
            ItemStack itemstack = this.a[3];

            for (int i = 0; i < 3; ++i) {
                if (this.a[i] != null && this.a[i].id == Item.POTION.id) {
                    int j = this.a[i].getData();
                    int k = this.b(j, itemstack);
                    List list = Item.POTION.b(j);
                    List list1 = Item.POTION.b(k);

                    if ((j <= 0 || list != list1) && (list == null || !list.equals(list1) && list1 != null)) {
                        if (j != k) {
                            this.a[i].setData(k);
                        }
                    } else if (!ItemPotion.c(j) && ItemPotion.c(k)) {
                        this.a[i].setData(k);
                    }
                }
            }

            if (Item.byId[itemstack.id].k()) {
                this.a[3] = new ItemStack(Item.byId[itemstack.id].j());
            } else {
                --this.a[3].count;
                if (this.a[3].count <= 0) {
                    this.a[3] = null;
                }
            }
        }
    }

    private int b(int i, ItemStack itemstack) {
        return itemstack == null ? i : (Item.byId[itemstack.id].n() ? PotionBrewer.a(i, Item.byId[itemstack.id].m()) : i);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Items");

        this.a = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.a.length) {
                this.a[b0] = ItemStack.a(nbttagcompound1);
            }
        }

        this.b = nbttagcompound.getShort("BrewTime");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("BrewTime", (short) this.b);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.a.length; ++i) {
            if (this.a[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Slot", (byte) i);
                this.a[i].b(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.set("Items", nbttaglist);
    }

    public ItemStack getItem(int i) {
        return i >= 0 && i < this.a.length ? this.a[i] : null;
    }

    public ItemStack splitStack(int i, int j) {
        if (i >= 0 && i < this.a.length) {
            ItemStack itemstack = this.a[i];

            this.a[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.a.length) {
            this.a[i] = itemstack;
        }
    }

    public int getMaxStackSize() {
        return 1;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public void f() {}

    public void g() {}

    public int n() {
        int i = 0;

        for (int j = 0; j < 3; ++j) {
            if (this.a[j] != null) {
                i |= 1 << j;
            }
        }

        return i;
    }
}
