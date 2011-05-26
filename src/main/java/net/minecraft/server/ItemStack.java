package net.minecraft.server;

public final class ItemStack {

    public int count;
    public int b;
    public int id;
    public int damage; // CraftBukkit - private -> public

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
        this.damage = k;
    }

    public ItemStack(NBTTagCompound nbttagcompound) {
        this.count = 0;
        this.b(nbttagcompound);
    }

    public ItemStack a(int i) {
        this.count -= i;
        return new ItemStack(this.id, i, this.damage);
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

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("id", (short) this.id);
        nbttagcompound.a("Count", (byte) this.count);
        nbttagcompound.a("Damage", (short) this.damage);
        return nbttagcompound;
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.d("id");
        this.count = nbttagcompound.c("Count");
        this.damage = nbttagcompound.d("Damage");
    }

    public int b() {
        return this.getItem().getMaxStackSize();
    }

    public boolean c() {
        return this.b() > 1 && (!this.d() || !this.f());
    }

    public boolean d() {
        return Item.byId[this.id].e() > 0;
    }

    public boolean e() {
        return Item.byId[this.id].d();
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

    public void b(int i) {
        this.damage = i;
    }

    public int i() {
        return Item.byId[this.id].e();
    }

    public void damage(int i, Entity entity) {
        if (this.d()) {
            this.damage += i;
            if (this.damage > this.i()) {
                if (entity instanceof EntityHuman) {
                    ((EntityHuman) entity).a(StatisticList.F[this.id], 1);
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

    public ItemStack j() {
        return new ItemStack(this.id, this.count, this.damage);
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? itemstack.d(itemstack1) : false);
    }

    private boolean d(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.id != itemstack.id ? false : this.damage == itemstack.damage);
    }

    public boolean a(ItemStack itemstack) {
        return this.id == itemstack.id && this.damage == itemstack.damage;
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack == null ? null : itemstack.j();
    }

    public String toString() {
        return this.count + "x" + Item.byId[this.id].a() + "@" + this.damage;
    }

    public void a(World world, Entity entity, int i, boolean flag) {
        if (this.b > 0) {
            --this.b;
        }

        Item.byId[this.id].a(this, world, entity, i, flag);
    }

    public void b(World world, EntityHuman entityhuman) {
        entityhuman.a(StatisticList.D[this.id], this.count);
        Item.byId[this.id].c(this, world, entityhuman);
    }

    public boolean c(ItemStack itemstack) {
        return this.id == itemstack.id && this.count == itemstack.count && this.damage == itemstack.damage;
    }
}
