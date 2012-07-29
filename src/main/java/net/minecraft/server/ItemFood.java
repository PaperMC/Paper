package net.minecraft.server;

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float c;
    private final boolean bY;
    private boolean bZ;
    private int ca;
    private int cb;
    private int cc;
    private float cd;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.bY = flag;
        this.c = f;
        this.a(CreativeModeTab.h);
    }

    public ItemFood(int i, int j, boolean flag) {
        this(i, j, 0.6F, flag);
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        --itemstack.count;
        // CraftBukkit start
        int oldFoodLevel = entityhuman.getFoodData().foodLevel;

        org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, this.getNutrition() + oldFoodLevel);

        if (!event.isCancelled()) {
            entityhuman.getFoodData().eat(event.getFoodLevel() - oldFoodLevel, this.getSaturationModifier());
        }
        // CraftBukkit end

        world.makeSound(entityhuman, "random.burp", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        this.c(itemstack, world, entityhuman);
        return itemstack;
    }

    protected void c(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (!world.isStatic && this.ca > 0 && world.random.nextFloat() < this.cd) {
            entityhuman.addEffect(new MobEffect(this.ca, this.cb * 20, this.cc));
        }
    }

    public int a(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation b(ItemStack itemstack) {
        return EnumAnimation.b;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.e(this.bZ)) {
            entityhuman.a(itemstack, this.a(itemstack));
        }

        return itemstack;
    }

    public int getNutrition() {
        return this.b;
    }

    public float getSaturationModifier() {
        return this.c;
    }

    public boolean h() {
        return this.bY;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.ca = i;
        this.cb = j;
        this.cc = k;
        this.cd = f;
        return this;
    }

    public ItemFood i() {
        this.bZ = true;
        return this;
    }
}
