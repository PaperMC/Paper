package net.minecraft.server;

public class InventoryPlayer implements IInventory {

    public ItemStack[] items = new ItemStack[36];
    public ItemStack[] armor = new ItemStack[4];
    public int itemInHandIndex = 0;
    public EntityHuman d;
    private ItemStack f;
    public boolean e = false;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return this.items;
    }

    public ItemStack[] getArmorContents() {
        return this.armor;
    }
    // CraftBukkit end

    public InventoryPlayer(EntityHuman entityhuman) {
        this.d = entityhuman;
    }

    public ItemStack getItemInHand() {
        return this.itemInHandIndex < 9 && this.itemInHandIndex >= 0 ? this.items[this.itemInHandIndex] : null;
    }

    public static int h() {
        return 9;
    }

    private int e(int i) {
        for (int j = 0; j < this.items.length; ++j) {
            if (this.items[j] != null && this.items[j].id == i) {
                return j;
            }
        }

        return -1;
    }

    private int firstPartial(ItemStack itemstack) {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].isStackable() && this.items[i].count < this.items[i].getMaxStackSize() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].usesData() || this.items[i].getData() == itemstack.getData())) {
                return i;
            }
        }

        return -1;
    }

    // CraftBukkit start - watch method above! :D
    public int canHold(ItemStack itemstack) {
        int remains = itemstack.count;
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) return itemstack.count;

            // Taken from firstPartial(ItemStack)
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].isStackable() && this.items[i].count < this.items[i].getMaxStackSize() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].usesData() || this.items[i].getData() == itemstack.getData())) {
                remains -= (this.items[i].getMaxStackSize() < this.getMaxStackSize() ? this.items[i].getMaxStackSize() : this.getMaxStackSize()) - this.items[i].count;
            }
            if (remains <= 0) return itemstack.count;
        }
        return itemstack.count - remains;
    }
    // CraftBukkit end

    private int m() {
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
        int k;

        if (itemstack.getMaxStackSize() == 1) {
            k = this.m();
            if (k < 0) {
                return j;
            } else {
                if (this.items[k] == null) {
                    this.items[k] = ItemStack.b(itemstack);
                }

                return 0;
            }
        } else {
            k = this.firstPartial(itemstack);
            if (k < 0) {
                k = this.m();
            }

            if (k < 0) {
                return j;
            } else {
                if (this.items[k] == null) {
                    this.items[k] = new ItemStack(i, 0, itemstack.getData());
                }

                int l = j;

                if (j > this.items[k].getMaxStackSize() - this.items[k].count) {
                    l = this.items[k].getMaxStackSize() - this.items[k].count;
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
    }

    public void i() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.items[i].a(this.d.world, this.d, i, this.itemInHandIndex == i);
            }
        }
    }

    public boolean b(int i) {
        int j = this.e(i);

        if (j < 0) {
            return false;
        } else {
            if (--this.items[j].count <= 0) {
                this.items[j] = null;
            }

            return true;
        }
    }

    public boolean c(int i) {
        int j = this.e(i);

        return j >= 0;
    }

    public boolean pickup(ItemStack itemstack) {
        int i;

        if (itemstack.f()) {
            i = this.m();
            if (i >= 0) {
                this.items[i] = ItemStack.b(itemstack);
                this.items[i].b = 5;
                itemstack.count = 0;
                return true;
            } else if (this.d.abilities.canInstantlyBuild) {
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

            if (itemstack.count == i && this.d.abilities.canInstantlyBuild) {
                itemstack.count = 0;
                return true;
            } else {
                return itemstack.count < i;
            }
        }
    }

    public ItemStack splitStack(int i, int j) {
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
                this.items[i].b(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) (i + 100));
                this.armor[i].b(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void b(NBTTagList nbttaglist) {
        this.items = new ItemStack[36];
        this.armor = new ItemStack[4];

        for (int i = 0; i < nbttaglist.d(); ++i) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound.d("Slot") & 255;
            ItemStack itemstack = ItemStack.a(nbttagcompound);

            if (itemstack != null) {
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
        // CraftBukkit start - fixed NPE
        if (block == null) {
            return false;
        }
        // CraftBukkit end

        if (block.material.k()) {
            return true;
        } else {
            ItemStack itemstack = this.getItem(this.itemInHandIndex);

            return itemstack != null ? itemstack.b(block) : false;
        }
    }

    public int j() {
        int i = 0;

        for (int j = 0; j < this.armor.length; ++j) {
            if (this.armor[j] != null && this.armor[j].getItem() instanceof ItemArmor) {
                int k = ((ItemArmor) this.armor[j].getItem()).b;

                i += k;
            }
        }

        return i;
    }

    public void d(int i) {
        i /= 4;
        if (i < 1) {
            i = 1;
        }

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

    public void k() {
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

    public ItemStack l() {
        return this.f;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.d.dead ? false : entityhuman.i(this.d) <= 64.0D;
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

    public void f() {}

    public void g() {}

    public void a(InventoryPlayer inventoryplayer) {
        int i;

        for (i = 0; i < this.items.length; ++i) {
            this.items[i] = ItemStack.b(inventoryplayer.items[i]);
        }

        for (i = 0; i < this.armor.length; ++i) {
            this.armor[i] = ItemStack.b(inventoryplayer.armor[i]);
        }
    }
}
