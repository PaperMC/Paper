package net.minecraft.server;

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float c;
    private final boolean d;
    private boolean cu;
    private int cv;
    private int cw;
    private int cx;
    private float cy;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.d = flag;
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

        ((EntityPlayer) entityhuman).playerConnection.sendPacket(new Packet8UpdateHealth(entityhuman.getHealth(), entityhuman.getFoodData().foodLevel, entityhuman.getFoodData().saturationLevel));
        // CraftBukkit end

        world.makeSound(entityhuman, "random.burp", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        this.c(itemstack, world, entityhuman);
        return itemstack;
    }

    protected void c(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (!world.isStatic && this.cv > 0 && world.random.nextFloat() < this.cy) {
            entityhuman.addEffect(new MobEffect(this.cv, this.cw * 20, this.cx));
        }
    }

    public int c_(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation b_(ItemStack itemstack) {
        return EnumAnimation.EAT;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.i(this.cu)) {
            entityhuman.a(itemstack, this.c_(itemstack));
        }

        return itemstack;
    }

    public int getNutrition() {
        return this.b;
    }

    public float getSaturationModifier() {
        return this.c;
    }

    public boolean i() {
        return this.d;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.cv = i;
        this.cw = j;
        this.cx = k;
        this.cy = f;
        return this;
    }

    public ItemFood j() {
        this.cu = true;
        return this;
    }
}
