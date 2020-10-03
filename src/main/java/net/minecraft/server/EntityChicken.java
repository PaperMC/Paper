package net.minecraft.server;

public class EntityChicken extends EntityAnimal {

    private static final RecipeItemStack bv = RecipeItemStack.a(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    public float bo;
    public float bp;
    public float bq;
    public float br;
    public float bs = 1.0F;
    public int eggLayTime;
    public boolean chickenJockey;

    public EntityChicken(EntityTypes<? extends EntityChicken> entitytypes, World world) {
        super(entitytypes, world);
        this.eggLayTime = this.random.nextInt(6000) + 6000;
        this.a(PathType.WATER, 0.0F);
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.4D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.0D, false, EntityChicken.bv));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
        this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return this.isBaby() ? entitysize.height * 0.85F : entitysize.height * 0.92F;
    }

    public static AttributeProvider.Builder eK() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 4.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void movementTick() {
        // CraftBukkit start
        if (this.isChickenJockey()) {
            this.persistent = !this.isTypeNotPersistent(0);
        }
        // CraftBukkit end
        super.movementTick();
        this.br = this.bo;
        this.bq = this.bp;
        this.bp = (float) ((double) this.bp + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.bp = MathHelper.a(this.bp, 0.0F, 1.0F);
        if (!this.onGround && this.bs < 1.0F) {
            this.bs = 1.0F;
        }

        this.bs = (float) ((double) this.bs * 0.9D);
        Vec3D vec3d = this.getMot();

        if (!this.onGround && vec3d.y < 0.0D) {
            this.setMot(vec3d.d(1.0D, 0.6D, 1.0D));
        }

        this.bo += this.bs * 2.0F;
        if (!this.world.isClientSide && this.isAlive() && !this.isBaby() && !this.isChickenJockey() && --this.eggLayTime <= 0) {
            this.playSound(SoundEffects.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.forceDrops = true; // CraftBukkit
            this.a((IMaterial) Items.EGG);
            this.forceDrops = false; // CraftBukkit
            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }

    }

    @Override
    public boolean b(float f, float f1) {
        return false;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public EntityChicken createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityChicken) EntityTypes.CHICKEN.a((World) worldserver);
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return EntityChicken.bv.test(itemstack);
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        return this.isChickenJockey() ? 10 : super.getExpValue(entityhuman);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.chickenJockey = nbttagcompound.getBoolean("IsChickenJockey");
        if (nbttagcompound.hasKey("EggLayTime")) {
            this.eggLayTime = nbttagcompound.getInt("EggLayTime");
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("IsChickenJockey", this.chickenJockey);
        nbttagcompound.setInt("EggLayTime", this.eggLayTime);
    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return this.isChickenJockey();
    }

    @Override
    public void k(Entity entity) {
        super.k(entity);
        float f = MathHelper.sin(this.aA * 0.017453292F);
        float f1 = MathHelper.cos(this.aA * 0.017453292F);
        float f2 = 0.1F;
        float f3 = 0.0F;

        entity.setPosition(this.locX() + (double) (0.1F * f), this.e(0.5D) + entity.ba() + 0.0D, this.locZ() - (double) (0.1F * f1));
        if (entity instanceof EntityLiving) {
            ((EntityLiving) entity).aA = this.aA;
        }

    }

    public boolean isChickenJockey() {
        return this.chickenJockey;
    }

    public void setChickenJockey(boolean flag) {
        this.chickenJockey = flag;
    }
}
