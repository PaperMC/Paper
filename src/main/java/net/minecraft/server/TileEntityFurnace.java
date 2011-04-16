package net.minecraft.server;

public class TileEntityFurnace extends TileEntity implements IInventory {

    private ItemStack[] items = new ItemStack[3];
    public int burnTime = 0;
    public int b = 0;
    public int cookTime = 0;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return items;
    }
    // CraftBukkit end

    public TileEntityFurnace() {}

    public int getSize() {
        return this.items.length;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack a(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }
    }

    public String getName() {
        return "Furnace";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            byte b0 = nbttagcompound1.c("Slot");

            if (b0 >= 0 && b0 < this.items.length) {
                this.items[b0] = new ItemStack(nbttagcompound1);
            }
        }

        this.burnTime = nbttagcompound.d("BurnTime");
        this.cookTime = nbttagcompound.d("CookTime");
        this.b = this.a(this.items[1]);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("BurnTime", (short) this.burnTime);
        nbttagcompound.a("CookTime", (short) this.cookTime);
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

    public boolean f() {
        return this.burnTime > 0;
    }

    public void g_() {
        boolean flag = this.burnTime > 0;
        boolean flag1 = false;

        if (this.burnTime > 0) {
            --this.burnTime;
        }

        if (!this.world.isStatic) {
            if (this.burnTime == 0 && this.h()) {
                this.b = this.burnTime = this.a(this.items[1]);
                if (this.burnTime > 0) {
                    flag1 = true;
                    if (this.items[1] != null) {
                        --this.items[1].count;
                        if (this.items[1].count == 0) {
                            this.items[1] = null;
                        }
                    }
                }
            }

            if (this.f() && this.h()) {
                ++this.cookTime;
                if (this.cookTime == 200) {
                    this.cookTime = 0;
                    this.g();
                    flag1 = true;
                }
            } else {
                this.cookTime = 0;
            }

            if (flag != this.burnTime > 0) {
                flag1 = true;
                BlockFurnace.a(this.burnTime > 0, this.world, this.e, this.f, this.g);
            }
        }

        if (flag1) {
            this.update();
        }
    }

    private boolean h() {
        if (this.items[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.a().a(this.items[0].getItem().id);

            // CraftBukkit - consider resultant count instead of current count
            return itemstack == null ? false : (this.items[2] == null ? true : (!this.items[2].a(itemstack) ? false : (this.items[2].count+itemstack.count <= this.getMaxStackSize() && this.items[2].count < this.items[2].b() ? true : this.items[2].count+itemstack.count <= itemstack.b())));
        }
    }

    public void g() {
        if (this.h()) {
            ItemStack itemstack = FurnaceRecipes.a().a(this.items[0].getItem().id);

            if (this.items[2] == null) {
                this.items[2] = itemstack.j();
            } else if (this.items[2].id == itemstack.id && this.items[2].damage == itemstack.damage) { // CraftBukkit - compare damage too
                this.items[2].count += itemstack.count; // CraftBukkit - increment by count instead of 1
            }

            --this.items[0].count;
            if (this.items[0].count <= 0) {
                this.items[0] = null;
            }
        }
    }

    private int a(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            int i = itemstack.getItem().id;

            return i < 256 && Block.byId[i].material == Material.WOOD ? 300 : (i == Item.STICK.id ? 100 : (i == Item.COAL.id ? 1600 : (i == Item.LAVA_BUCKET.id ? 20000 : (i == Block.SAPLING.id ? 100 : 0))));
        }
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.e, this.f, this.g) != this ? false : entityhuman.d((double) this.e + 0.5D, (double) this.f + 0.5D, (double) this.g + 0.5D) <= 64.0D;
    }
}
