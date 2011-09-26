package net.minecraft.server;

public final class ItemStack {

    public int count;
    public int b;
    public int id;
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
        b(k); // CraftBukkit
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

    public ItemStack b(World world, EntityHuman entityhuman) {
        return this.getItem().b(this, world, entityhuman);
    }

    public NBTTagCompound b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("id", (short) this.id);
        nbttagcompound.a("Count", (byte) this.count);
        nbttagcompound.a("Damage", (short) this.damage);
        return nbttagcompound;
    }

    public void c(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.d("id");
        this.count = nbttagcompound.c("Count");
        this.damage = nbttagcompound.d("Damage");
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
        this.damage = (id > 0) && (id < 256) ? Item.byId[id].filterData(i) : i; // CraftBukkit
    }

    public int i() {
        return Item.byId[this.id].getMaxDurability();
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

    public ItemStack cloneItemStack() {
        return new ItemStack(this.id, this.count, this.damage);
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? itemstack.d(itemstack1) : false);
    }

    private boolean d(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.id != itemstack.id ? false : this.damage == itemstack.damage);
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
        return this.getItem().b(this);
    }

    public void a(World world, EntityHuman entityhuman, int i) {
        this.getItem().a(this, world, entityhuman, i);
    }
}
