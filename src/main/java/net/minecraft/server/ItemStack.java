package net.minecraft.server;

public final class ItemStack {

    public int count;
    public int b;
    public int id;
    public NBTTagCompound tag;
    private int damage;

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int i) {
        this(block.id, i, 0);
    }

    public ItemStack(Block block, int i, int j) {
        this(block.id, i, j);
    }

    public ItemStack(Item item) {
        this(item.id, 1, 0);
    }

    public ItemStack(Item item, int i) {
        this(item.id, i, 0);
    }

    public ItemStack(Item item, int i, int j) {
        this(item.id, i, j);
    }

    public ItemStack(int i, int j, int k) {
        this.count = 0;
        this.id = i;
        this.count = j;
        this.setData(k); // CraftBukkit
    }

    // CraftBukkit start - used to create a new ItemStack, specifying the enchantments at time of creation.
    public ItemStack(int id, int count, int data, NBTTagList enchantments) {
        this(id, count, data);
        // taken from .addEnchantment
        if (enchantments != null && Item.byId[this.id].getMaxStackSize() == 1) {
            if (this.tag == null) {
                this.setTag(new NBTTagCompound());
            }

            this.tag.set("ench", enchantments.clone()); // modify this part to use passed in enchantments list
            // TODO Books
        }
    }
    // CraftBukkit end

    public static ItemStack a(NBTTagCompound nbttagcompound) {
        ItemStack itemstack = new ItemStack();

        itemstack.c(nbttagcompound);
        return itemstack.getItem() != null ? itemstack : null;
    }

    private ItemStack() {
        this.count = 0;
    }

    public ItemStack a(int i) {
        ItemStack itemstack = new ItemStack(this.id, i, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        this.count -= i;
        return itemstack;
    }

    public Item getItem() {
        return Item.byId[this.id];
    }

    public boolean placeItem(EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        boolean flag = this.getItem().interactWith(this, entityhuman, world, i, j, k, l, f, f1, f2);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }

        return flag;
    }

    public float a(Block block) {
        return this.getItem().getDestroySpeed(this, block);
    }

    public ItemStack a(World world, EntityHuman entityhuman) {
        return this.getItem().a(this, world, entityhuman);
    }

    public ItemStack b(World world, EntityHuman entityhuman) {
        return this.getItem().b(this, world, entityhuman);
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("id", (short) this.id);
        nbttagcompound.setByte("Count", (byte) this.count);
        nbttagcompound.setShort("Damage", (short) this.damage);
        if (this.tag != null) {
            nbttagcompound.set("tag", this.tag);
        }

        return nbttagcompound;
    }

    public void c(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.getShort("id");
        this.count = nbttagcompound.getByte("Count");
        this.damage = nbttagcompound.getShort("Damage");
        if (nbttagcompound.hasKey("tag")) {
            this.tag = nbttagcompound.getCompound("tag");
        }
    }

    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.f() || !this.h());
    }

    public boolean f() {
        return Item.byId[this.id].getMaxDurability() > 0;
    }

    public boolean usesData() {
        return Item.byId[this.id].k();
    }

    public boolean h() {
        return this.f() && this.damage > 0;
    }

    public int i() {
        return this.damage;
    }

    public int getData() {
        return this.damage;
    }

    public void setData(int i) {
        this.damage = (this.id > 0) && (this.id < 256) ? Item.byId[this.id].filterData(i) : i; // CraftBukkit
    }

    public int k() {
        return Item.byId[this.id].getMaxDurability();
    }

    public void damage(int i, EntityLiving entityliving) {
        if (this.f()) {
            if (i > 0 && entityliving instanceof EntityHuman) {
                int j = EnchantmentManager.getDurabilityEnchantmentLevel(((EntityHuman) entityliving).inventory);

                if (j > 0 && entityliving.world.random.nextInt(j + 1) > 0) {
                    return;
                }
            }

            if (!(entityliving instanceof EntityHuman) || !((EntityHuman) entityliving).abilities.canInstantlyBuild) {
                this.damage += i;
            }

            if (this.damage > this.k()) {
                entityliving.a(this);
                if (entityliving instanceof EntityHuman) {
                    ((EntityHuman) entityliving).a(StatisticList.F[this.id], 1);
                }

                --this.count;
                if (this.count < 0) {
                    this.count = 0;
                }

                // CraftBukkit start - Check for item breaking
                if (this.count == 0 && entityliving instanceof EntityHuman) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemBreakEvent((EntityHuman) entityliving, this);
                }
                // CraftBukkit end

                this.damage = 0;
            }
        }
    }

    public void a(EntityLiving entityliving, EntityHuman entityhuman) {
        boolean flag = Item.byId[this.id].a(this, entityliving, (EntityLiving) entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {
        boolean flag = Item.byId[this.id].a(this, world, i, j, k, l, entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public int a(Entity entity) {
        return Item.byId[this.id].a(entity);
    }

    public boolean b(Block block) {
        return Item.byId[this.id].canDestroySpecialBlock(block);
    }

    public boolean a(EntityLiving entityliving) {
        return Item.byId[this.id].a(this, entityliving);
    }

    public ItemStack cloneItemStack() {
        ItemStack itemstack = new ItemStack(this.id, this.count, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        return itemstack;
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? (itemstack.tag == null && itemstack1.tag != null ? false : itemstack.tag == null || itemstack.tag.equals(itemstack1.tag)) : false);
    }

    public static boolean matches(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? itemstack.d(itemstack1) : false);
    }

    private boolean d(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.id != itemstack.id ? false : (this.damage != itemstack.damage ? false : (this.tag == null && itemstack.tag != null ? false : this.tag == null || this.tag.equals(itemstack.tag))));
    }

    public boolean doMaterialsMatch(ItemStack itemstack) {
        return this.id == itemstack.id && this.damage == itemstack.damage;
    }

    public String a() {
        return Item.byId[this.id].c(this);
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack == null ? null : itemstack.cloneItemStack();
    }

    public String toString() {
        return this.count + "x" + Item.byId[this.id].getName() + "@" + this.damage;
    }

    public void a(World world, Entity entity, int i, boolean flag) {
        if (this.b > 0) {
            --this.b;
        }

        Item.byId[this.id].a(this, world, entity, i, flag);
    }

    public void a(World world, EntityHuman entityhuman, int i) {
        entityhuman.a(StatisticList.D[this.id], i);
        Item.byId[this.id].d(this, world, entityhuman);
    }

    public boolean c(ItemStack itemstack) {
        return this.id == itemstack.id && this.count == itemstack.count && this.damage == itemstack.damage;
    }

    public int m() {
        return this.getItem().a(this);
    }

    public EnumAnimation n() {
        return this.getItem().b(this);
    }

    public void b(World world, EntityHuman entityhuman, int i) {
        this.getItem().a(this, world, entityhuman, i);
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    public NBTTagCompound getTag() {
        return this.tag;
    }

    public NBTTagList getEnchantments() {
        return this.tag == null ? null : (NBTTagList) this.tag.get("ench");
    }

    public void setTag(NBTTagCompound nbttagcompound) {
        this.tag = nbttagcompound;
    }

    public boolean u() {
        return !this.getItem().k(this) ? false : !this.hasEnchantments();
    }

    public void addEnchantment(Enchantment enchantment, int i) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        if (!this.tag.hasKey("ench")) {
            this.tag.set("ench", new NBTTagList("ench"));
        }

        NBTTagList nbttaglist = (NBTTagList) this.tag.get("ench");
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setShort("id", (short) enchantment.id);
        nbttagcompound.setShort("lvl", (short) ((byte) i));
        nbttaglist.add(nbttagcompound);
    }

    public boolean hasEnchantments() {
        return this.tag != null && this.tag.hasKey("ench");
    }

    // CraftBukkit start - temporary method for book fix
    public void a(String s, NBTBase nbtbase) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        this.tag.set(s, nbtbase);
    }
    // CraftBukkit end
}
