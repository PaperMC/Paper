package net.minecraft.server;

import org.bukkit.event.entity.FoodLevelChangeEvent; // CraftBukkit

public class ItemFood extends Item {

    public final int a;
    private final int bt;
    private final float bu;
    private final boolean bv;
    private boolean bw;
    private int bx;
    private int by;
    private int bz;
    private float bA;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.bt = j;
        this.bv = flag;
        this.bu = f;
    }

    public ItemFood(int i, int j, boolean flag) {
        this(i, j, 0.6F, flag);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        --itemstack.count;
        // CraftBukkit start
        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entityhuman.getBukkitEntity(), Math.min(this.k() + entityhuman.foodData.foodLevel, 20));
        entityhuman.world.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            entityhuman.getFoodData().a(event.getFoodLevel(), this.l());
        }
        // CraftBukkit end

        if (!world.isStatic && this.bx > 0 && world.random.nextFloat() < this.bA) {
            entityhuman.d(new MobEffect(this.bx, this.by * 20, this.bz));
        }

        return itemstack;
    }

    public int c(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation b(ItemStack itemstack) {
        return EnumAnimation.b;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.c(this.bw)) {
            entityhuman.a(itemstack, this.c(itemstack));
        }

        return itemstack;
    }

    public int k() {
        return this.bt;
    }

    public float l() {
        return this.bu;
    }

    public boolean m() {
        return this.bv;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.bx = i;
        this.by = j;
        this.bz = k;
        this.bA = f;
        return this;
    }

    public ItemFood n() {
        this.bw = true;
        return this;
    }

    public Item a(String s) {
        return super.a(s);
    }
}
