package net.minecraft.server;

import com.google.common.collect.UnmodifiableIterator;
import javax.annotation.Nullable;

public class EntityPig extends EntityAnimal implements ISteerable, ISaddleable {

    private static final DataWatcherObject<Boolean> bo = DataWatcher.a(EntityPig.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Integer> bp = DataWatcher.a(EntityPig.class, DataWatcherRegistry.b);
    private static final RecipeItemStack bq = RecipeItemStack.a(Items.CARROT, Items.POTATO, Items.BEETROOT);
    public final SaddleStorage saddleStorage;

    public EntityPig(EntityTypes<? extends EntityPig> entitytypes, World world) {
        super(entitytypes, world);
        this.saddleStorage = new SaddleStorage(this.datawatcher, EntityPig.bp, EntityPig.bo);
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
        this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, RecipeItemStack.a(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, false, EntityPig.bq));
        this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 1.1D));
        this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    }

    public static AttributeProvider.Builder eK() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.25D);
    }

    @Nullable
    @Override
    public Entity getRidingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    @Override
    public boolean er() {
        Entity entity = this.getRidingPassenger();

        if (!(entity instanceof EntityHuman)) {
            return false;
        } else {
            EntityHuman entityhuman = (EntityHuman) entity;

            return entityhuman.getItemInMainHand().getItem() == Items.CARROT_ON_A_STICK || entityhuman.getItemInOffHand().getItem() == Items.CARROT_ON_A_STICK;
        }
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityPig.bp.equals(datawatcherobject) && this.world.isClientSide) {
            this.saddleStorage.a();
        }

        super.a(datawatcherobject);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityPig.bo, false);
        this.datawatcher.register(EntityPig.bp, 0);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        this.saddleStorage.a(nbttagcompound);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.saddleStorage.b(nbttagcompound);
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_PIG_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PIG_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        boolean flag = this.k(entityhuman.b(enumhand));

        if (!flag && this.hasSaddle() && !this.isVehicle() && !entityhuman.ep()) {
            if (!this.world.isClientSide) {
                entityhuman.startRiding(this);
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            EnumInteractionResult enuminteractionresult = super.b(entityhuman, enumhand);

            if (!enuminteractionresult.a()) {
                ItemStack itemstack = entityhuman.b(enumhand);

                return itemstack.getItem() == Items.SADDLE ? itemstack.a(entityhuman, (EntityLiving) this, enumhand) : EnumInteractionResult.PASS;
            } else {
                return enuminteractionresult;
            }
        }
    }

    @Override
    public boolean canSaddle() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (this.hasSaddle()) {
            this.a((IMaterial) Items.SADDLE);
        }

    }

    @Override
    public boolean hasSaddle() {
        return this.saddleStorage.hasSaddle();
    }

    @Override
    public void saddle(@Nullable SoundCategory soundcategory) {
        this.saddleStorage.setSaddle(true);
        if (soundcategory != null) {
            this.world.playSound((EntityHuman) null, (Entity) this, SoundEffects.ENTITY_PIG_SADDLE, soundcategory, 0.5F, 1.0F);
        }

    }

    @Override
    public Vec3D b(EntityLiving entityliving) {
        EnumDirection enumdirection = this.getAdjustedDirection();

        if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
            return super.b(entityliving);
        } else {
            int[][] aint = DismountUtil.a(enumdirection);
            BlockPosition blockposition = this.getChunkCoordinates();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            UnmodifiableIterator unmodifiableiterator = entityliving.ei().iterator();

            while (unmodifiableiterator.hasNext()) {
                EntityPose entitypose = (EntityPose) unmodifiableiterator.next();
                AxisAlignedBB axisalignedbb = entityliving.f(entitypose);
                int[][] aint1 = aint;
                int i = aint.length;

                for (int j = 0; j < i; ++j) {
                    int[] aint2 = aint1[j];

                    blockposition_mutableblockposition.d(blockposition.getX() + aint2[0], blockposition.getY(), blockposition.getZ() + aint2[1]);
                    double d0 = this.world.h(blockposition_mutableblockposition);

                    if (DismountUtil.a(d0)) {
                        Vec3D vec3d = Vec3D.a((BaseBlockPosition) blockposition_mutableblockposition, d0);

                        if (DismountUtil.a(this.world, entityliving, axisalignedbb.c(vec3d))) {
                            entityliving.setPose(entitypose);
                            return vec3d;
                        }
                    }
                }
            }

            return super.b(entityliving);
        }
    }

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {
        if (worldserver.getDifficulty() != EnumDifficulty.PEACEFUL) {
            EntityPigZombie entitypigzombie = (EntityPigZombie) EntityTypes.ZOMBIFIED_PIGLIN.a((World) worldserver);

            entitypigzombie.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            entitypigzombie.setPositionRotation(this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
            entitypigzombie.setNoAI(this.isNoAI());
            entitypigzombie.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                entitypigzombie.setCustomName(this.getCustomName());
                entitypigzombie.setCustomNameVisible(this.getCustomNameVisible());
            }

            entitypigzombie.setPersistent();
            worldserver.addEntity(entitypigzombie);
            this.die();
        } else {
            super.onLightningStrike(worldserver, entitylightning);
        }

    }

    @Override
    public void g(Vec3D vec3d) {
        this.a((EntityInsentient) this, this.saddleStorage, vec3d);
    }

    @Override
    public float N_() {
        return (float) this.b(GenericAttributes.MOVEMENT_SPEED) * 0.225F;
    }

    @Override
    public void a_(Vec3D vec3d) {
        super.g(vec3d);
    }

    @Override
    public boolean O_() {
        return this.saddleStorage.a(this.getRandom());
    }

    @Override
    public EntityPig createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityPig) EntityTypes.PIG.a((World) worldserver);
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return EntityPig.bq.test(itemstack);
    }
}
