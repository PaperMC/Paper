package net.minecraft.server;

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float c;
    private final boolean co;
    private boolean cp;
    private int cq;
    private int cr;
    private int cs;
    private float ct;

    public ItemFood(int i, int j, float f, boolean flag) {
        super(i);
        this.a = 32;
        this.b = j;
        this.co = flag;
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
        if (!world.isStatic && this.cq > 0 && world.random.nextFloat() < this.ct) {
            entityhuman.addEffect(new MobEffect(this.cq, this.cr * 20, this.cs));
        }
    }

    public int c_(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation b_(ItemStack itemstack) {
        return EnumAnimation.b;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.g(this.cp)) {
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
        return this.co;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.cq = i;
        this.cr = j;
        this.cs = k;
        this.ct = f;
        return this;
    }

    public ItemFood j() {
        this.cp = true;
        return this;
    }
}
