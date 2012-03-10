package net.minecraft.server;

public class EntityWolf extends EntityTameableAnimal {

    private boolean b = false;
    private float c;
    private float g;
    private boolean h;
    private boolean i;
    private float j;
    private float k;

    public EntityWolf(World world) {
        super(world);
        this.texture = "/mob/wolf.png";
        this.b(0.6F, 0.8F);
        this.bb = 0.3F;
        this.ak().a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, this.a);
        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, this.bb, true));
        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, this.bb, 10.0F, 2.0F));
        this.goalSelector.a(6, new PathfinderGoalBreed(this, this.bb));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, this.bb));
        this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
        this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntitySheep.class, 16.0F, 200, false));
    }

    public boolean c_() {
        return true;
    }

    public void b(EntityLiving entityliving) {
        super.b(entityliving);
        if (entityliving instanceof EntityHuman) {
            this.setAngry(true);
        }
    }

    protected void g() {
        this.datawatcher.watch(18, Integer.valueOf(this.getHealth()));
    }

    public int getMaxHealth() {
        return this.isTamed() ? 20 : 8;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(18, new Integer(this.getHealth()));
    }

    protected boolean g_() {
        return false;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Angry", this.isAngry());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setAngry(nbttagcompound.getBoolean("Angry"));
    }

    protected boolean n() {
        return this.isAngry();
    }

    protected String i() {
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String j() {
        return "mob.wolf.hurt";
    }

    protected String k() {
        return "mob.wolf.death";
    }

    protected float p() {
        return 0.4F;
    }

    protected int getLootId() {
        return -1;
    }

    public void e() {
        super.e();
        if (!this.world.isStatic && this.h && !this.i && !this.G() && this.onGround) {
            this.i = true;
            this.j = 0.0F;
            this.k = 0.0F;
            this.world.broadcastEntityEffect(this, (byte) 8);
        }
    }

    public void G_() {
        super.G_();
        this.g = this.c;
        if (this.b) {
            this.c += (1.0F - this.c) * 0.4F;
        } else {
            this.c += (0.0F - this.c) * 0.4F;
        }

        if (this.b) {
            this.bc = 10;
        }

        if (this.aS()) {
            this.h = true;
            this.i = false;
            this.j = 0.0F;
            this.k = 0.0F;
        } else if ((this.h || this.i) && this.i) {
            if (this.j == 0.0F) {
                this.world.makeSound(this, "mob.wolf.shake", this.p(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.k = this.j;
            this.j += 0.05F;
            if (this.k >= 2.0F) {
                this.h = false;
                this.i = false;
                this.k = 0.0F;
                this.j = 0.0F;
            }

            if (this.j > 0.4F) {
                float f = (float) this.boundingBox.b;
                int i = (int) (MathHelper.sin((this.j - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.a("splash", this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    public float getHeadHeight() {
        return this.length * 0.8F;
    }

    public int C() {
        return this.isSitting() ? 20 : super.C();
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        Entity entity = damagesource.getEntity();

        this.a.a(false);
        if (entity != null && !(entity instanceof EntityHuman) && !(entity instanceof EntityArrow)) {
            i = (i + 1) / 2;
        }

        return super.damageEntity(damagesource, i);
    }

    public boolean a(Entity entity) {
        int i = this.isTamed() ? 4 : 2;

        return entity.damageEntity(DamageSource.mobAttack(this), i);
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (!this.isTamed()) {
            if (itemstack != null && itemstack.id == Item.BONE.id && !this.isAngry()) {
                --itemstack.count;
                if (itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }

                if (!this.world.isStatic) {
                    if (this.random.nextInt(3) == 0) {
                        this.setTamed(true);
                        this.setPathEntity((PathEntity) null);
                        this.b((EntityLiving) null);
                        this.a.a(true);
                        this.setHealth(20);
                        this.setOwnerName(entityhuman.name);
                        this.a(true);
                        this.world.broadcastEntityEffect(this, (byte) 7);
                    } else {
                        this.a(false);
                        this.world.broadcastEntityEffect(this, (byte) 6);
                    }
                }

                return true;
            }
        } else {
            if (itemstack != null && Item.byId[itemstack.id] instanceof ItemFood) {
                ItemFood itemfood = (ItemFood) Item.byId[itemstack.id];

                if (itemfood.q() && this.datawatcher.getInt(18) < 20) {
                    --itemstack.count;
                    this.heal(itemfood.getNutrition());
                    if (itemstack.count <= 0) {
                        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                    }

                    return true;
                }
            }

            if (entityhuman.name.equalsIgnoreCase(this.getOwnerName()) && !this.world.isStatic && !this.a(itemstack)) {
                this.a.a(!this.isSitting());
                this.aZ = false;
                this.setPathEntity((PathEntity) null);
            }
        }

        return super.b(entityhuman);
    }

    public boolean a(ItemStack itemstack) {
        return itemstack == null ? false : (!(Item.byId[itemstack.id] instanceof ItemFood) ? false : ((ItemFood) Item.byId[itemstack.id]).q());
    }

    public int q() {
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

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        EntityWolf entitywolf = new EntityWolf(this.world);

        entitywolf.setOwnerName(this.getOwnerName());
        entitywolf.setTamed(true);
        return entitywolf;
    }

    public void e(boolean flag) {
        this.b = flag;
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

            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.r_() && entitywolf.r_());
        }
    }
}
