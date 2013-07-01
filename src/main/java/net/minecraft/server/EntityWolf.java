package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class EntityWolf extends EntityTameableAnimal {

    private float bq;
    private float br;
    private boolean bs;
    private boolean bt;
    private float bu;
    private float bv;

    public EntityWolf(World world) {
        super(world);
        this.a(0.6F, 0.8F);
        this.getNavigation().a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, this.bp);
        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
        this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntitySheep.class, 200, false));
        this.setTamed(false);
    }

    protected void ax() {
        super.ax();
        this.a(GenericAttributes.d).a(0.30000001192092896D);
        if (this.isTamed()) {
            this.a(GenericAttributes.a).a(20.0D);
        } else {
            this.a(GenericAttributes.a).a(8.0D);
        }
    }

    public boolean bb() {
        return true;
    }

    public void setGoalTarget(EntityLiving entityliving) {
        super.setGoalTarget(entityliving);
        if (entityliving == null) {
            if (!this.isAngry()) {
                return;
            }

            this.setAngry(false);
            List list = this.world.a(this.getClass(), AxisAlignedBB.a().a(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).grow(16.0D, 10.0D, 16.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityWolf entitywolf = (EntityWolf) iterator.next();

                if (this != entitywolf) {
                    entitywolf.setAngry(false);
                }
            }
        } else {
            this.setAngry(true);
        }
    }

    protected void bg() {
        this.datawatcher.watch(18, Float.valueOf(this.getScaledHealth())); // CraftBukkit - this.getHealth() -> this.getScaledHealth()
    }

    protected void a() {
        super.a();
        this.datawatcher.a(18, new Float(this.getHealth()));
        this.datawatcher.a(19, new Byte((byte) 0));
        this.datawatcher.a(20, new Byte((byte) BlockCloth.j_(1)));
    }

    protected void a(int i, int j, int k, int l) {
        this.makeSound("mob.wolf.step", 0.15F, 1.0F);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Angry", this.isAngry());
        nbttagcompound.setByte("CollarColor", (byte) this.getCollarColor());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setAngry(nbttagcompound.getBoolean("Angry"));
        if (nbttagcompound.hasKey("CollarColor")) {
            this.setCollarColor(nbttagcompound.getByte("CollarColor"));
        }
    }

    protected String r() {
        // CraftBukkit - getInt(18) < 10 -> < this.maxHealth / 2
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getFloat(18) < this.maxHealth / 2 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String aK() {
        return "mob.wolf.hurt";
    }

    protected String aL() {
        return "mob.wolf.death";
    }

    protected float aW() {
        return 0.4F;
    }

    protected int getLootId() {
        return -1;
    }

    public void c() {
        super.c();
        if (!this.world.isStatic && this.bs && !this.bt && !this.bI() && this.onGround) {
            this.bt = true;
            this.bu = 0.0F;
            this.bv = 0.0F;
            this.world.broadcastEntityEffect(this, (byte) 8);
        }
    }

    public void l_() {
        super.l_();
        this.br = this.bq;
        if (this.ca()) {
            this.bq += (1.0F - this.bq) * 0.4F;
        } else {
            this.bq += (0.0F - this.bq) * 0.4F;
        }

        if (this.ca()) {
            this.g = 10;
        }

        if (this.F()) {
            this.bs = true;
            this.bt = false;
            this.bu = 0.0F;
            this.bv = 0.0F;
        } else if ((this.bs || this.bt) && this.bt) {
            if (this.bu == 0.0F) {
                this.makeSound("mob.wolf.shake", this.aW(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.bv = this.bu;
            this.bu += 0.05F;
            if (this.bv >= 2.0F) {
                this.bs = false;
                this.bt = false;
                this.bv = 0.0F;
                this.bu = 0.0F;
            }

            if (this.bu > 0.4F) {
                float f = (float) this.boundingBox.b;
                int i = (int) (MathHelper.sin((this.bu - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.addParticle("splash", this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    public float getHeadHeight() {
        return this.length * 0.8F;
    }

    public int bl() {
        return this.isSitting() ? 20 : super.bl();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            Entity entity = damagesource.getEntity();

            this.bp.setSitting(false);
            if (entity != null && !(entity instanceof EntityHuman) && !(entity instanceof EntityArrow)) {
                f = (f + 1.0F) / 2.0F;
            }

            return super.damageEntity(damagesource, f);
        }
    }

    public boolean m(Entity entity) {
        int i = this.isTamed() ? 4 : 2;

        return entity.damageEntity(DamageSource.mobAttack(this), (float) i);
    }

    public void setTamed(boolean flag) {
        super.setTamed(flag);
        if (flag) {
            this.a(GenericAttributes.a).a(20.0D);
        } else {
            this.a(GenericAttributes.a).a(8.0D);
        }
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (this.isTamed()) {
            if (itemstack != null) {
                if (Item.byId[itemstack.id] instanceof ItemFood) {
                    ItemFood itemfood = (ItemFood) Item.byId[itemstack.id];

                    if (itemfood.j() && this.datawatcher.getFloat(18) < 20.0F) {
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            --itemstack.count;
                        }

                        this.heal((float) itemfood.getNutrition());
                        if (itemstack.count <= 0) {
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                        }

                        return true;
                    }
                } else if (itemstack.id == Item.INK_SACK.id) {
                    int i = BlockCloth.j_(itemstack.getData());

                    if (i != this.getCollarColor()) {
                        this.setCollarColor(i);
                        if (!entityhuman.abilities.canInstantlyBuild && --itemstack.count <= 0) {
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                        }

                        return true;
                    }
                }
            }

            if (entityhuman.getName().equalsIgnoreCase(this.getOwnerName()) && !this.world.isStatic && !this.c(itemstack)) {
                this.bp.setSitting(!this.isSitting());
                this.bd = false;
                this.setPathEntity((PathEntity) null);
            }
        } else if (itemstack != null && itemstack.id == Item.BONE.id && !this.isAngry()) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
            }

            if (!this.world.isStatic) {
                // CraftBukkit - added event call and isCancelled check.
                if (this.random.nextInt(3) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    boolean updateMaxHealth = this.getMaxHealth() == this.maxHealth; // CraftBukkit
                    this.setTamed(true);
                    this.setPathEntity((PathEntity) null);
                    this.setGoalTarget((EntityLiving) null);
                    this.bp.setSitting(true);
                    // CraftBukkit start
                    if (updateMaxHealth) {
                        this.maxHealth = this.getMaxHealth();
                    }
                    this.setHealth(this.maxHealth);
                    // CraftBukkit end
                    this.setOwnerName(entityhuman.getName());
                    this.j(true);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.j(false);
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return true;
        }

        return super.a(entityhuman);
    }

    public boolean c(ItemStack itemstack) {
        return itemstack == null ? false : (!(Item.byId[itemstack.id] instanceof ItemFood) ? false : ((ItemFood) Item.byId[itemstack.id]).j());
    }

    public int br() {
        return 8;
    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    public int getCollarColor() {
        return this.datawatcher.getByte(20) & 15;
    }

    public void setCollarColor(int i) {
        this.datawatcher.watch(20, Byte.valueOf((byte) (i & 15)));
    }

    public EntityWolf b(EntityAgeable entityageable) {
        EntityWolf entitywolf = new EntityWolf(this.world);
        String s = this.getOwnerName();

        if (s != null && s.trim().length() > 0) {
            entitywolf.setOwnerName(s);
            entitywolf.setTamed(true);
        }

        return entitywolf;
    }

    public void n(boolean flag) {
        if (flag) {
            this.datawatcher.watch(19, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(19, Byte.valueOf((byte) 0));
        }
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(entityanimal instanceof EntityWolf)) {
            return false;
        } else {
            EntityWolf entitywolf = (EntityWolf) entityanimal;

            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.bU() && entitywolf.bU());
        }
    }

    public boolean ca() {
        return this.datawatcher.getByte(19) == 1;
    }

    protected boolean isTypeNotPersistent() {
        return !this.isTamed() && this.ticksLived > 2400;
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
