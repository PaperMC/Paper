package net.minecraft.server;

public class InventoryPlayer implements IInventory {

    public ItemStack[] a = new ItemStack[36];
    public ItemStack[] b = new ItemStack[4];
    public int c = 0;
    public EntityHuman e; // CraftBukkit - private->public
    private ItemStack f;
    public boolean d = false;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return a;
    }

    public ItemStack[] getArmorContents() {
        return b;
    }
    // CraftBukkit end

    public InventoryPlayer(EntityHuman entityhuman) {
        this.e = entityhuman;
    }

    public ItemStack b() {
        if (this.c < this.a.length) {
            return this.a[this.c];
        } else {
            return null;
        }
    }

    private int d(int i) {
        for (int j = 0; j < this.a.length; ++j) {
            if (this.a[j] != null && this.a[j].id == i) {
                return j;
            }
        }

        return -1;
    }

    private int c(ItemStack itemstack) {
        for (int i = 0; i < this.a.length; ++i) {
            if (this.a[i] != null && this.a[i].id == itemstack.id && this.a[i].c() && this.a[i].count < this.a[i].b() && this.a[i].count < this.n_() && (!this.a[i].e() || this.a[i].h() == itemstack.h())) {
                return i;
            }
        }

        return -1;
    }

    private int j() {
        for (int i = 0; i < this.a.length; ++i) {
            if (this.a[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int d(ItemStack itemstack) {
        int i = itemstack.id;
        int j = itemstack.count;
        int k = this.c(itemstack);

        if (k < 0) {
            k = this.j();
        }

        if (k < 0) {
            return j;
        } else {
            if (this.a[k] == null) {
                this.a[k] = new ItemStack(i, 0, itemstack.h());
            }

            int l = j;

            if (j > this.a[k].b() - this.a[k].count) {
                l = this.a[k].b() - this.a[k].count;
            }

            if (l > this.n_() - this.a[k].count) {
                l = this.n_() - this.a[k].count;
            }

            if (l == 0) {
                return j;
            } else {
                j -= l;
                this.a[k].count += l;
                this.a[k].b = 5;
                return j;
            }
        }
    }

    public void e() {
        for (int i = 0; i < this.a.length; ++i) {
            if (this.a[i] != null && this.a[i].b > 0) {
                --this.a[i].b;
            }
        }
    }

    public boolean b(int i) {
        int j = this.d(i);

        if (j < 0) {
            return false;
        } else {
            if (--this.a[j].count <= 0) {
                this.a[j] = null;
            }

            return true;
        }
    }

    public boolean a(ItemStack itemstack) {
        if (!itemstack.f()) {
            itemstack.count = this.d(itemstack);
            if (itemstack.count == 0) {
                return true;
            }
        }

        int i = this.j();

        if (i >= 0) {
            this.a[i] = itemstack;
            this.a[i].b = 5;
            return true;
        } else {
            return false;
        }
    }

    public ItemStack a(int i, int j) {
        ItemStack[] aitemstack = this.a;

        if (i >= this.a.length) {
            aitemstack = this.b;
            i -= this.a.length;
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

    public void a(int i, ItemStack itemstack) {
        ItemStack[] aitemstack = this.a;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.b;
        }

        aitemstack[i] = itemstack;
    }

    public float a(Block block) {
        float f = 1.0F;

        if (this.a[this.c] != null) {
            f *= this.a[this.c].a(block);
        }

        return f;
    }

    public NBTTagList a(NBTTagList nbttaglist) {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.a.length; ++i) {
            if (this.a[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) i);
                this.a[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        for (i = 0; i < this.b.length; ++i) {
            if (this.b[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) (i + 100));
                this.b[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void b(NBTTagList nbttaglist) {
        this.a = new ItemStack[36];
        this.b = new ItemStack[4];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound.c("Slot") & 255;
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (itemstack.a() != null) {
                if (j >= 0 && j < this.a.length) {
                    this.a[j] = itemstack;
                }

                if (j >= 100 && j < this.b.length + 100) {
                    this.b[j - 100] = itemstack;
                }
            }
        }
    }

    public int m_() {
        return this.a.length + 4;
    }

    public ItemStack c_(int i) {
        ItemStack[] aitemstack = this.a;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.b;
        }

        return aitemstack[i];
    }

    public String c() {
        return "Inventory";
    }

    public int n_() {
        return 64;
    }

    public int a(Entity entity) {
        ItemStack itemstack = this.c_(this.c);

        return itemstack != null ? itemstack.a(entity) : 1;
    }

    public boolean b(Block block) {
        if (block.material != Material.STONE && block.material != Material.ORE && block.material != Material.SNOW_BLOCK && block.material != Material.SNOW_LAYER) {
            return true;
        } else {
            ItemStack itemstack = this.c_(this.c);

            return itemstack != null ? itemstack.b(block) : false;
        }
    }

    public int f() {
        int i = 0;
        int j = 0;
        int k = 0;

        for (int l = 0; l < this.b.length; ++l) {
            if (this.b[l] != null && this.b[l].a() instanceof ItemArmor) {
                int i1 = this.b[l].i();
                int j1 = this.b[l].g();
                int k1 = i1 - j1;

                j += k1;
                k += i1;
                int l1 = ((ItemArmor) this.b[l].a()).bj;

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
        for (int j = 0; j < this.b.length; ++j) {
            if (this.b[j] != null && this.b[j].a() instanceof ItemArmor) {
                this.b[j].b(i);
                if (this.b[j].count == 0) {
                    this.b[j].a(this.e);
                    this.b[j] = null;
                }
            }
        }
    }

    public void g() {
        int i;

        for (i = 0; i < this.a.length; ++i) {
            if (this.a[i] != null) {
                this.e.a(this.a[i], true);
                this.a[i] = null;
            }
        }

        for (i = 0; i < this.b.length; ++i) {
            if (this.b[i] != null) {
                this.e.a(this.b[i], true);
                this.b[i] = null;
            }
        }
    }

    public void h() {
        this.d = true;
    }

    public void b(ItemStack itemstack) {
        this.f = itemstack;
        this.e.a(itemstack);
    }

    public ItemStack i() {
        return this.f;
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.e.dead ? false : entityhuman.g(this.e) <= 64.0D;
    }
}
