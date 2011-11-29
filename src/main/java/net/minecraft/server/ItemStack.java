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

    public boolean placeItem(EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        boolean flag = this.getItem().a(this, entityhuman, world, i, j, k, l);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }

        return flag;
    }

    public float a(Block block) {
        return this.getItem().a(this, block);
    }

    public ItemStack a(World world, EntityHuman entityhuman) {
        return this.getItem().a(this, world, entityhuman);
    }

    public ItemStack b(World world, EntityHuman entityhuman) {
        return this.getItem().b(this, world, entityhuman);
    }

    public NBTTagCompound b(NBTTagCompound nbttagcompound) {
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
        return this.getMaxStackSize() > 1 && (!this.d() || !this.f());
    }

    public boolean d() {
        return Item.byId[this.id].getMaxDurability() > 0;
    }

    public boolean usesData() {
        return Item.byId[this.id].e();
    }

    public boolean f() {
        return this.d() && this.damage > 0;
    }

    public int g() {
        return this.damage;
    }

    public int getData() {
        return this.damage;
    }

    public void setData(int i) {
        this.damage = (this.id > 0) && (this.id < 256) ? Item.byId[this.id].filterData(i) : i; // CraftBukkit
    }

    public int i() {
        return Item.byId[this.id].getMaxDurability();
    }

    public void damage(int i, EntityLiving entityliving) {
        if (this.d()) {
            if (i > 0 && entityliving instanceof EntityHuman) {
                int j = EnchantmentManager.getDurabilityEnchantmentLevel(((EntityHuman) entityliving).inventory);

                if (j > 0 && entityliving.world.random.nextInt(j + 1) > 0) {
                    return;
                }
            }

            this.damage += i;
            if (this.damage > this.i()) {
                entityliving.c(this);
                if (entityliving instanceof EntityHuman) {
                    ((EntityHuman) entityliving).a(StatisticList.F[this.id], 1);
                }

                --this.count;
                if (this.count < 0) {
                    this.count = 0;
                }

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

    public void a(int i, int j, int k, int l, EntityHuman entityhuman) {
        boolean flag = Item.byId[this.id].a(this, i, j, k, l, entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public int a(Entity entity) {
        return Item.byId[this.id].a(entity);
    }

    public boolean b(Block block) {
        return Item.byId[this.id].a(block);
    }

    public void a(EntityHuman entityhuman) {}

    public void a(EntityLiving entityliving) {
        Item.byId[this.id].a(this, entityliving);
    }

    public ItemStack cloneItemStack() {
        ItemStack itemstack = new ItemStack(this.id, this.count, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
            if (!itemstack.tag.equals(this.tag)) {
                return itemstack;
            }
        }

        return itemstack;
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? itemstack.d(itemstack1) : false);
    }

    private boolean d(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.id != itemstack.id ? false : (this.damage != itemstack.damage ? false : (this.tag == null && itemstack.tag != null ? false : this.tag == null || this.tag.equals(itemstack.tag))));
    }

    public boolean doMaterialsMatch(ItemStack itemstack) {
        return this.id == itemstack.id && this.damage == itemstack.damage;
    }

    public String k() {
        return Item.byId[this.id].a(this);
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack == null ? null : itemstack.cloneItemStack();
    }

    public String toString() {
        return this.count + "x" + Item.byId[this.id].b() + "@" + this.damage;
    }

    public void a(World world, Entity entity, int i, boolean flag) {
        if (this.b > 0) {
            --this.b;
        }

        Item.byId[this.id].a(this, world, entity, i, flag);
    }

    public void c(World world, EntityHuman entityhuman) {
        entityhuman.a(StatisticList.D[this.id], this.count);
        Item.byId[this.id].d(this, world, entityhuman);
    }

    public boolean c(ItemStack itemstack) {
        return this.id == itemstack.id && this.count == itemstack.count && this.damage == itemstack.damage;
    }

    public int l() {
        return this.getItem().c(this);
    }

    public EnumAnimation m() {
        return this.getItem().d(this);
    }

    public void a(World world, EntityHuman entityhuman, int i) {
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
        if (Item.byId[this.id].getMaxStackSize() != 1) {
            throw new IllegalArgumentException("Cannot add tag data to a stackable item");
        } else {
            this.tag = nbttagcompound;
        }
    }

    public boolean q() {
        return !this.getItem().e(this) ? false : !this.hasEnchantments();
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
}
