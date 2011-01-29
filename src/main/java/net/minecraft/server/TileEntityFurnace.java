package net.minecraft.server;

public class TileEntityFurnace extends TileEntity implements IInventory {

    private ItemStack[] h = new ItemStack[3];
    public int e = 0;
    public int f = 0;
    public int g = 0;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return h;
    }
    // CraftBukkit end

    public TileEntityFurnace() {}

    public int h_() {
        return this.h.length;
    }

    public ItemStack a(int i) {
        return this.h[i];
    }

    public ItemStack b(int i, int j) {
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
        if (itemstack != null && itemstack.count > this.c()) {
            itemstack.count = this.c();
        }
    }

    public String b() {
        return "Furnace";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.k("Items");

        this.h = new ItemStack[this.h_()];

        for (int i = 0; i < nbttaglist.b(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            byte b0 = nbttagcompound1.b("Slot");

            if (b0 >= 0 && b0 < this.h.length) {
                this.h[b0] = new ItemStack(nbttagcompound1);
            }
        }

        this.e = nbttagcompound.c("BurnTime");
        this.g = nbttagcompound.c("CookTime");
        this.f = this.a(this.h[1]);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("BurnTime", (short) this.e);
        nbttagcompound.a("CookTime", (short) this.g);
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

    public int c() {
        return 64;
    }

    public boolean e() {
        return this.e > 0;
    }

    public void f() {
        boolean flag = this.e > 0;
        boolean flag1 = false;

        if (this.e > 0) {
            --this.e;
        }

        if (!this.a.isStatic) {
            if (this.e == 0 && this.i()) {
                this.f = this.e = this.a(this.h[1]);
                if (this.e > 0) {
                    flag1 = true;
                    if (this.h[1] != null) {
                        --this.h[1].count;
                        if (this.h[1].count == 0) {
                            this.h[1] = null;
                        }
                    }
                }
            }

            if (this.e() && this.i()) {
                ++this.g;
                if (this.g == 200) {
                    this.g = 0;
                    this.h();
                    flag1 = true;
                }
            } else {
                this.g = 0;
            }

            if (flag != this.e > 0) {
                flag1 = true;
                BlockFurnace.a(this.e > 0, this.a, this.b, this.c, this.d);
            }
        }

        if (flag1) {
            this.d();
        }
    }

    private boolean i() {
        if (this.h[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.a().a(this.h[0].a().id);

            return itemstack == null ? false : (this.h[2] == null ? true : (!this.h[2].a(itemstack) ? false : (this.h[2].count < this.c() && this.h[2].count < this.h[2].b() ? true : this.h[2].count < itemstack.b())));
        }
    }

    public void h() {
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
        return this.a.getTileEntity(this.b, this.c, this.d) != this ? false : entityhuman.d((double) this.b + 0.5D, (double) this.c + 0.5D, (double) this.d + 0.5D) <= 64.0D;
    }
}
