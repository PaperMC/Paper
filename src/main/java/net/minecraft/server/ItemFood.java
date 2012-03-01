package net.minecraft.server;

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float bU;
    private final boolean bV;
    private boolean bW;
    private int bX;
    private int bY;
    private int bZ;
    private float ca;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.bV = flag;
        this.bU = f;
    }

    public ItemFood(int i, int j, boolean flag) {
        this(i, j, 0.6F, flag);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        --itemstack.count;
        // CraftBukkit start
        int oldFoodLevel = entityhuman.getFoodData().foodLevel;

        org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, Math.min(this.getNutrition() + oldFoodLevel, 20));

        if (!event.isCancelled()) {
            entityhuman.getFoodData().eat(event.getFoodLevel() - oldFoodLevel, this.getSaturationModifier());
        }
        // CraftBukkit end

        if (!world.isStatic && this.bX > 0 && world.random.nextFloat() < this.ca) {
            entityhuman.addEffect(new MobEffect(this.bX, this.bY * 20, this.bZ));
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
        if (entityhuman.b(this.bW)) {
            entityhuman.a(itemstack, this.c(itemstack));
        }

        return itemstack;
    }

    public int getNutrition() {
        return this.b;
    }

    public float getSaturationModifier() {
        return this.bU;
    }

    public boolean q() {
        return this.bV;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.bX = i;
        this.bY = j;
        this.bZ = k;
        this.ca = f;
        return this;
    }

    public ItemFood r() {
        this.bW = true;
        return this;
    }

    public Item a(String s) {
        return super.a(s);
    }
}
