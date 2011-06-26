package net.minecraft.server;

public class InventoryPlayer implements IInventory {

    public ItemStack[] items = new ItemStack[36];
    public ItemStack[] armor = new ItemStack[4];
    public int itemInHandIndex = 0;
    public EntityHuman d; // CraftBukkit - private -> public
    private ItemStack f;
    public boolean e = false;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return items;
    }

    public ItemStack[] getArmorContents() {
        return armor;
    }
    // CraftBukkit end

    public InventoryPlayer(EntityHuman entityhuman) {
        this.d = entityhuman;
    }

    public ItemStack getItemInHand() {
        return this.itemInHandIndex < 9 && this.itemInHandIndex >= 0 ? this.items[this.itemInHandIndex] : null;
    }

    public static int e() {
        return 9;
    }

    private int d(int i) {
        for (int j = 0; j < this.items.length; ++j) {
            if (this.items[j] != null && this.items[j].id == i) {
                return j;
            }
        }

        return -1;
    }

    private int d(ItemStack itemstack) {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].c() && this.items[i].count < this.items[i].b() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].e() || this.items[i].getData() == itemstack.getData())) {
                return i;
            }
        }

        return -1;
    }

    // CraftBukkit start - watch method above! :D
    public int canPickup(ItemStack itemstack) {
        int remains = itemstack.count;
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) return itemstack.count;

            // Taken from d(ItemStack)I
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].c() && this.items[i].count < this.items[i].b() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].e() || this.items[i].getData() == itemstack.getData())) {
                remains -= (this.items[i].b() < this.getMaxStackSize() ? this.items[i].b() : this.getMaxStackSize()) - this.items[i].count;
            }
            if (remains <= 0) return itemstack.count;
        }
        return itemstack.count - remains;
    }
    // CraftBukkit end

    private int k() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int e(ItemStack itemstack) {
        int i = itemstack.id;
        int j = itemstack.count;
        int k = this.d(itemstack);

        if (k < 0) {
            k = this.k();
        }

        if (k < 0) {
            return j;
        } else {
            if (this.items[k] == null) {
                this.items[k] = new ItemStack(i, 0, itemstack.getData());
            }

            int l = j;

            if (j > this.items[k].b() - this.items[k].count) {
                l = this.items[k].b() - this.items[k].count;
            }

            if (l > this.getMaxStackSize() - this.items[k].count) {
                l = this.getMaxStackSize() - this.items[k].count;
            }

            if (l == 0) {
                return j;
            } else {
                j -= l;
                this.items[k].count += l;
                this.items[k].b = 5;
                return j;
            }
        }
    }

    public void f() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.items[i].a(this.d.world, this.d, i, this.itemInHandIndex == i);
            }
        }
    }

    public boolean b(int i) {
        int j = this.d(i);

        if (j < 0) {
            return false;
        } else {
            if (--this.items[j].count <= 0) {
                this.items[j] = null;
            }

            return true;
        }
    }

    public boolean canHold(ItemStack itemstack) {
        int i;

        if (itemstack.f()) {
            i = this.k();
            if (i >= 0) {
                this.items[i] = ItemStack.b(itemstack);
                this.items[i].b = 5;
                itemstack.count = 0;
                return true;
            } else {
                return false;
            }
        } else {
            do {
                i = itemstack.count;
                itemstack.count = this.e(itemstack);
            } while (itemstack.count > 0 && itemstack.count < i);

            return itemstack.count < i;
        }
    }

    public ItemStack a(int i, int j) {
        ItemStack[] aitemstack = this.items;

        if (i >= this.items.length) {
            aitemstack = this.armor;
            i -= this.items.length;
        }

        if (aitemstack[i] != null) {
            ItemStack itemstack;

            if (aitemstack[i].count <= j) {
                itemstack = aitemstack[i];
                aitemstack[i] = null;
                return itemstack;
            } else {
                itemstack = aitemstack[i].a(j);
                if (aitemstack[i].count == 0) {
                    aitemstack[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] aitemstack = this.items;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armor;
        }

        aitemstack[i] = itemstack;
    }

    public float a(Block block) {
        float f = 1.0F;

        if (this.items[this.itemInHandIndex] != null) {
            f *= this.items[this.itemInHandIndex].a(block);
        }

        return f;
    }

    public NBTTagList a(NBTTagList nbttaglist) {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) i);
                this.items[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) (i + 100));
                this.armor[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void b(NBTTagList nbttaglist) {
        this.items = new ItemStack[36];
        this.armor = new ItemStack[4];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound.c("Slot") & 255;
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (itemstack.getItem() != null) {
                if (j >= 0 && j < this.items.length) {
                    this.items[j] = itemstack;
                }

                if (j >= 100 && j < this.armor.length + 100) {
                    this.armor[j - 100] = itemstack;
                }
            }
        }
    }

    public int getSize() {
        return this.items.length + 4;
    }

    public ItemStack getItem(int i) {
        ItemStack[] aitemstack = this.items;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armor;
        }

        return aitemstack[i];
    }

    public String getName() {
        return "Inventory";
    }

    public int getMaxStackSize() {
        return 64;
    }

    public int a(Entity entity) {
        ItemStack itemstack = this.getItem(this.itemInHandIndex);

        return itemstack != null ? itemstack.a(entity) : 1;
    }

    public boolean b(Block block) {
        if (block.material != Material.STONE && block.material != Material.ORE && block.material != Material.SNOW_BLOCK && block.material != Material.SNOW_LAYER) {
            return true;
        } else {
            ItemStack itemstack = this.getItem(this.itemInHandIndex);

            return itemstack != null ? itemstack.b(block) : false;
        }
    }

    public int g() {
        int i = 0;
        int j = 0;
        int k = 0;

        for (int l = 0; l < this.armor.length; ++l) {
            if (this.armor[l] != null && this.armor[l].getItem() instanceof ItemArmor) {
                int i1 = this.armor[l].i();
                int j1 = this.armor[l].g();
                int k1 = i1 - j1;

                j += k1;
                k += i1;
                int l1 = ((ItemArmor) this.armor[l].getItem()).bk;

                i += l1;
            }
        }

        if (k == 0) {
            return 0;
        } else {
            return (i - 1) * j / k + 1;
        }
    }

    public void c(int i) {
        for (int j = 0; j < this.armor.length; ++j) {
            if (this.armor[j] != null && this.armor[j].getItem() instanceof ItemArmor) {
                this.armor[j].damage(i, this.d);
                if (this.armor[j].count == 0) {
                    this.armor[j].a(this.d);
                    this.armor[j] = null;
                }
            }
        }
    }

    public void h() {
        int i;

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.d.a(this.items[i], true);
                this.items[i] = null;
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                this.d.a(this.armor[i], true);
                this.armor[i] = null;
            }
        }
    }

    public void update() {
        this.e = true;
    }

    public void b(ItemStack itemstack) {
        this.f = itemstack;
        this.d.a(itemstack);
    }

    public ItemStack j() {
        return this.f;
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.d.dead ? false : entityhuman.g(this.d) <= 64.0D;
    }

    public boolean c(ItemStack itemstack) {
        int i;

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null && this.armor[i].c(itemstack)) {
                return true;
            }
        }

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && this.items[i].c(itemstack)) {
                return true;
            }
        }

        return false;
    }
}
