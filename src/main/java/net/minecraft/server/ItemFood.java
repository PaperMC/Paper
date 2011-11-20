package net.minecraft.server;

import org.bukkit.event.entity.FoodLevelChangeEvent; // CraftBukkit

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float bR;
    private final boolean bS;
    private boolean bT;
    private int bU;
    private int bV;
    private int bW;
    private float bX;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.bS = flag;
        this.bR = f;
    }

    public ItemFood(int i, int j, boolean flag) {
        this(i, j, 0.6F, flag);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        --itemstack.count;
        // CraftBukkit start
        int oldFoodLevel = entityhuman.getFoodData().foodLevel;

        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entityhuman.getBukkitEntity(), Math.min(this.n() + entityhuman.getFoodData().foodLevel, 20));
        entityhuman.world.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            entityhuman.getFoodData().a(event.getFoodLevel() - oldFoodLevel, this.o());
        }
        // CraftBukkit end

        if (!world.isStatic && this.bU > 0 && world.random.nextFloat() < this.bX) {
            entityhuman.addEffect(new MobEffect(this.bU, this.bV * 20, this.bW));
        }

        return itemstack;
    }

    public int c(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation d(ItemStack itemstack) {
        return EnumAnimation.b;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.b(this.bT)) {
            entityhuman.a(itemstack, this.c(itemstack));
        }

        return itemstack;
    }

    public int n() {
        return this.b;
    }

    public float o() {
        return this.bR;
    }

    public boolean p() {
        return this.bS;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.bU = i;
        this.bV = j;
        this.bW = k;
        this.bX = f;
        return this;
    }

    public ItemFood q() {
        this.bT = true;
        return this;
    }

    public Item a(String s) {
        return super.a(s);
    }
}
