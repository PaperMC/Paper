package net.minecraft.server;

import org.bukkit.craftbukkit.util.CraftMagicNumbers; // CraftBukkit

public class EntityChicken extends EntityAnimal {

    public float bp;
    public float bq;
    public float br;
    public float bs;
    public float bt = 1.0F;
    public int bu;
    public boolean bv;

    public EntityChicken(World world) {
        super(world);
        this.a(0.3F, 0.7F);
        this.bu = this.random.nextInt(6000) + 6000;
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.4D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.0D, Items.SEEDS, false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    public boolean bj() {
        return true;
    }

    protected void aC() {
        super.aC();
        this.getAttributeInstance(GenericAttributes.a).setValue(4.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.25D);
    }

    public void e() {
        super.e();
        this.bs = this.bp;
        this.br = this.bq;
        this.bq = (float) ((double) this.bq + (double) (this.onGround ? -1 : 4) * 0.3D);
        if (this.bq < 0.0F) {
            this.bq = 0.0F;
        }

        if (this.bq > 1.0F) {
            this.bq = 1.0F;
        }

        if (!this.onGround && this.bt < 1.0F) {
            this.bt = 1.0F;
        }

        this.bt = (float) ((double) this.bt * 0.9D);
        if (!this.onGround && this.motY < 0.0D) {
            this.motY *= 0.6D;
        }

        this.bp += this.bt * 2.0F;
        if (!this.world.isStatic && !this.isBaby() && !this.bZ() && --this.bu <= 0) {
            this.makeSound("mob.chicken.plop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.a(Items.EGG, 1);
            this.bu = this.random.nextInt(6000) + 6000;
        }
    }

    protected void b(float f) {}

    protected String t() {
        return "mob.chicken.say";
    }

    protected String aS() {
        return "mob.chicken.hurt";
    }

    protected String aT() {
        return "mob.chicken.hurt";
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.chicken.step", 0.15F, 1.0F);
    }

    protected Item getLoot() {
        return Items.FEATHER;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - Whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3) + this.random.nextInt(1 + i);

        if (j > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.FEATHER), j));
        }

        if (this.isBurning()) {
            loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.COOKED_CHICKEN), 1));
        } else {
            loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.RAW_CHICKEN), 1));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public EntityChicken b(EntityAgeable entityageable) {
        return new EntityChicken(this.world);
    }

    public boolean c(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() instanceof ItemSeeds;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.bv = nbttagcompound.getBoolean("IsChickenJockey");
    }

    protected int getExpValue(EntityHuman entityhuman) {
        return this.bZ() ? 10 : super.getExpValue(entityhuman);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("IsChickenJockey", this.bv);
    }

    protected boolean isTypeNotPersistent() {
        return this.bZ() && this.passenger == null;
    }

    public void ab() {
        super.ab();
        float f = MathHelper.sin(this.aM * 3.1415927F / 180.0F);
        float f1 = MathHelper.cos(this.aM * 3.1415927F / 180.0F);
        float f2 = 0.1F;
        float f3 = 0.0F;

        this.passenger.setPosition(this.locX + (double) (f2 * f), this.locY + (double) (this.length * 0.5F) + this.passenger.ac() + (double) f3, this.locZ - (double) (f2 * f1));
        if (this.passenger instanceof EntityLiving) {
            ((EntityLiving) this.passenger).aM = this.aM;
        }
    }

    public boolean bZ() {
        return this.bv;
    }

    public void i(boolean flag) {
        this.bv = flag;
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
