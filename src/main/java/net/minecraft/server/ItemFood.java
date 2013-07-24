package net.minecraft.server;

public class ItemFood extends Item {

    public final int a;
    private final int b;
    private final float c;
    private final boolean d;
    private boolean cB;
    private int cC;
    private int cD;
    private int cE;
    private float cF;

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

        ((EntityPlayer) entityhuman).playerConnection.sendPacket(new Packet8UpdateHealth(((EntityPlayer) entityhuman).getBukkitEntity().getScaledHealth(), entityhuman.getFoodData().foodLevel, entityhuman.getFoodData().saturationLevel));
        // CraftBukkit end

        world.makeSound(entityhuman, "random.burp", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        this.c(itemstack, world, entityhuman);
        return itemstack;
    }

    protected void c(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (!world.isStatic && this.cC > 0 && world.random.nextFloat() < this.cF) {
            entityhuman.addEffect(new MobEffect(this.cC, this.cD * 20, this.cE));
        }
    }

    public int d_(ItemStack itemstack) {
        return 32;
    }

    public EnumAnimation c_(ItemStack itemstack) {
        return EnumAnimation.EAT;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.g(this.cB)) {
            entityhuman.a(itemstack, this.d_(itemstack));
        }

        return itemstack;
    }

    public int getNutrition() {
        return this.b;
    }

    public float getSaturationModifier() {
        return this.c;
    }

    public boolean j() {
        return this.d;
    }

    public ItemFood a(int i, int j, int k, float f) {
        this.cC = i;
        this.cD = j;
        this.cE = k;
        this.cF = f;
        return this;
    }

    public ItemFood k() {
        this.cB = true;
        return this;
    }
}
