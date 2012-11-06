package net.minecraft.server;

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float c;
    private final boolean cl;
    private boolean cm;
    private int cn;
    private int co;
    private int cp;
    private float cq;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.cl = flag;
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

        ((EntityPlayer) entityhuman).netServerHandler.sendPacket(new Packet8UpdateHealth(entityhuman.getHealth(), entityhuman.getFoodData().foodLevel, entityhuman.getFoodData().saturationLevel));
        // CraftBukkit end

        world.makeSound(entityhuman, "random.burp", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        this.c(itemstack, world, entityhuman);
        return itemstack;
    }

    protected void c(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (!world.isStatic && this.cn > 0 && world.random.nextFloat() < this.cq) {
            entityhuman.addEffect(new MobEffect(this.cn, this.co * 20, this.cp));
        }
    }

    public int a(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation d_(ItemStack itemstack) {
        return EnumAnimation.b;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.f(this.cm)) {
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

    public boolean i() {
        return this.cl;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.cn = i;
        this.co = j;
        this.cp = k;
        this.cq = f;
        return this;
    }

    public ItemFood j() {
        this.cm = true;
        return this;
    }
}
