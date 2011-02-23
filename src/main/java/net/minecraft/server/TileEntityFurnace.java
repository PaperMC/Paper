package net.minecraft.server;

public class TileEntityFurnace extends TileEntity implements IInventory {

    private ItemStack[] h = new ItemStack[3];
    public int a = 0;
    public int b = 0;
    public int c = 0;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return h;
    }
    // CraftBukkit end

    public TileEntityFurnace() {}

    public int m_() {
        return this.h.length;
    }

    public ItemStack c_(int i) {
        return this.h[i];
    }

    public ItemStack a(int i, int j) {
        if (this.h[i] != null) {
            ItemStack itemstack;

            if (this.h[i].count <= j) {
                itemstack = this.h[i];
                this.h[i] = null;
                return itemstack;
            } else {
                itemstack = this.h[i].a(j);
                if (this.h[i].count == 0) {
                    this.h[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        this.h[i] = itemstack;
        if (itemstack != null && itemstack.count > this.n_()) {
            itemstack.count = this.n_();
        }
    }

    public String c() {
        return "Furnace";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Items");

        this.h = new ItemStack[this.m_()];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            byte b0 = nbttagcompound1.c("Slot");

            if (b0 >= 0 && b0 < this.h.length) {
                this.h[b0] = new ItemStack(nbttagcompound1);
            }
        }

        this.a = nbttagcompound.d("BurnTime");
        this.c = nbttagcompound.d("CookTime");
        this.b = this.a(this.h[1]);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("BurnTime", (short) this.a);
        nbttagcompound.a("CookTime", (short) this.c);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.h.length; ++i) {
            if (this.h[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Slot", (byte) i);
                this.h[i].a(nbttagcompound1);
                nbttaglist.a((NBTBase) nbttagcompound1);
            }
        }

        nbttagcompound.a("Items", (NBTBase) nbttaglist);
    }

    public int n_() {
        return 64;
    }

    public boolean f() {
        return this.a > 0;
    }

    public void i_() {
        boolean flag = this.a > 0;
        boolean flag1 = false;

        if (this.a > 0) {
            --this.a;
        }

        if (!this.d.isStatic) {
            if (this.a == 0 && this.i()) {
                this.b = this.a = this.a(this.h[1]);
                if (this.a > 0) {
                    flag1 = true;
                    if (this.h[1] != null) {
                        --this.h[1].count;
                        if (this.h[1].count == 0) {
                            this.h[1] = null;
                        }
                    }
                }
            }

            if (this.f() && this.i()) {
                ++this.c;
                if (this.c == 200) {
                    this.c = 0;
                    this.g();
                    flag1 = true;
                }
            } else {
                this.c = 0;
            }

            if (flag != this.a > 0) {
                flag1 = true;
                BlockFurnace.a(this.a > 0, this.d, this.e, this.f, this.g);
            }
        }

        if (flag1) {
            this.h();
        }
    }

    private boolean i() {
        if (this.h[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.a().a(this.h[0].a().id);

            return itemstack == null ? false : (this.h[2] == null ? true : (!this.h[2].a(itemstack) ? false : (this.h[2].count < this.n_() && this.h[2].count < this.h[2].b() ? true : this.h[2].count < itemstack.b())));
        }
    }

    public void g() {
        if (this.i()) {
            ItemStack itemstack = FurnaceRecipes.a().a(this.h[0].a().id);

            if (this.h[2] == null) {
                this.h[2] = itemstack.j();
            } else if (this.h[2].id == itemstack.id) {
                ++this.h[2].count;
            }

            --this.h[0].count;
            if (this.h[0].count <= 0) {
                this.h[0] = null;
            }
        }
    }

    private int a(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            int i = itemstack.a().id;

            return i < 256 && Block.byId[i].material == Material.WOOD ? 300 : (i == Item.STICK.id ? 100 : (i == Item.COAL.id ? 1600 : (i == Item.LAVA_BUCKET.id ? 20000 : 0)));
        }
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.d.getTileEntity(this.e, this.f, this.g) != this ? false : entityhuman.d((double) this.e + 0.5D, (double) this.f + 0.5D, (double) this.g + 0.5D) <= 64.0D;
    }
}
