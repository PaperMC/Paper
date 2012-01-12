package net.minecraft.server;

import org.bukkit.event.entity.FoodLevelChangeEvent; // CraftBukkit

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float bS;
    private final boolean bT;
    private boolean bU;
    private int bV;
    private int bW;
    private int bX;
    private float bY;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.bT = flag;
        this.bS = f;
    }

    public ItemFood(int i, int j, boolean flag) {
        this(i, j, 0.6F, flag);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        --itemstack.count;
        // CraftBukkit start
        int oldFoodLevel = entityhuman.getFoodData().foodLevel;

        FoodLevelChangeEvent event = new FoodLevelChangeEvent(entityhuman.getBukkitEntity(), Math.min(this.o() + entityhuman.getFoodData().foodLevel, 20));
        entityhuman.world.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            entityhuman.getFoodData().a(event.getFoodLevel() - oldFoodLevel, this.p());
        }
        // CraftBukkit end

        if (!world.isStatic && this.bV > 0 && world.random.nextFloat() < this.bY) {
            entityhuman.addEffect(new MobEffect(this.bV, this.bW * 20, this.bX));
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
        if (entityhuman.b(this.bU)) {
            entityhuman.a(itemstack, this.c(itemstack));
        }

        return itemstack;
    }

    public int o() {
        return this.b;
    }

    public float p() {
        return this.bS;
    }

    public boolean q() {
        return this.bT;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.bV = i;
        this.bW = j;
        this.bX = k;
        this.bY = f;
        return this;
    }

    public ItemFood r() {
        this.bU = true;
        return this;
    }

    public Item a(String s) {
        return super.a(s);
    }
}
