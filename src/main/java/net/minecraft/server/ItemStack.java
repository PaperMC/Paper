package net.minecraft.server;

public final class ItemStack {

    public int a;
    public int b;
    public int c;
    public int d; // Craftbukkit - make public

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int k) {
        this(block.bi, k, 0);
    }

    public ItemStack(Block block, int k, int l) {
        this(block.bi, k, l);
    }

    public ItemStack(Item item) {
        this(item.ba, 1, 0);
    }

    public ItemStack(Item item, int k) {
        this(item.ba, k, 0);
    }

    public ItemStack(Item item, int k, int l) {
        this(item.ba, k, l);
    }

    public ItemStack(int k, int l, int i1) {
        a = 0;
        c = k;
        a = l;
        d = i1;
    }

    public ItemStack(NBTTagCompound nbttagcompound) {
        a = 0;
        b(nbttagcompound);
    }

    public ItemStack a(int k) {
        a -= k;
        return new ItemStack(c, k, d);
    }

    public Item a() {
        return Item.c[c];
    }

    public boolean a(EntityPlayer entityplayer, World world, int k, int l, int i1, int j1) {
        return a().a(this, entityplayer, world, k, l, i1, j1);
    }

    public float a(Block block) {
        return a().a(this, block);
    }

    public ItemStack a(World world, EntityPlayer entityplayer) {
        return a().a(this, world, entityplayer);
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("id", (short) c);
        nbttagcompound.a("Count", (byte) a);
        nbttagcompound.a("Damage", (short) d);
        return nbttagcompound;
    }

    public void b(NBTTagCompound nbttagcompound) {
        c = ((int) (nbttagcompound.c("id")));
        a = ((int) (nbttagcompound.b("Count")));
        d = ((int) (nbttagcompound.c("Damage")));
    }

    public int b() {
        return a().b();
    }

    public boolean c() {
        return b() > 1 && (!d() || !f());
    }

    public boolean d() {
        return Item.c[c].d() > 0;
    }

    public boolean e() {
        return Item.c[c].c();
    }

    public boolean f() {
        return d() && d > 0;
    }

    public int g() {
        return d;
    }

    public int h() {
        return d;
    }

    public int i() {
        return Item.c[c].d();
    }

    public void b(int k) {
        if (!d()) {
            return;
        }
        d += k;
        if (d > i()) {
            a--;
            if (a < 0) {
                a = 0;
            }
            d = 0;
        }
    }

    public void a(EntityLiving entityliving) {
        Item.c[c].a(this, entityliving);
    }

    public void a(int k, int l, int i1, int j1) {
        Item.c[c].a(this, k, l, i1, j1);
    }

    public int a(Entity entity) {
        return Item.c[c].a(entity);
    }

    public boolean b(Block block) {
        return Item.c[c].a(block);
    }

    public void a(EntityPlayer entityplayer) {}

    public void b(EntityLiving entityliving) {
        Item.c[c].b(this, entityliving);
    }

    public ItemStack j() {
        return new ItemStack(c, a, d);
    }

    public static boolean a(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack == null && itemstack1 == null) {
            return true;
        }
        if (itemstack == null || itemstack1 == null) {
            return false;
        } else {
            return itemstack.c(itemstack1);
        }
    }

    private boolean c(ItemStack itemstack) {
        if (a != itemstack.a) {
            return false;
        }
        if (c != itemstack.c) {
            return false;
        }
        return d == itemstack.d;
    }

    public boolean a(ItemStack itemstack) {
        return c == itemstack.c && d == itemstack.d;
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack != null ? itemstack.j() : null;
    }

    public String toString() {
        return (new StringBuilder()).append(a).append("x").append(Item.c[c].a()).append("@").append(d).toString();
    }
}
