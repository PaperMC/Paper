package net.minecraft.server;

import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityOcelot extends EntityAnimal {

    private static final RecipeItemStack bo = RecipeItemStack.a(Items.COD, Items.SALMON);
    private static final DataWatcherObject<Boolean> bp = DataWatcher.a(EntityOcelot.class, DataWatcherRegistry.i);
    private EntityOcelot.a<EntityHuman> bq;
    private EntityOcelot.b br;

    public EntityOcelot(EntityTypes<? extends EntityOcelot> entitytypes, World world) {
        super(entitytypes, world);
        this.eL();
    }

    private boolean isTrusting() {
        return (Boolean) this.datawatcher.get(EntityOcelot.bp);
    }

    private void setTrusting(boolean flag) {
        this.datawatcher.set(EntityOcelot.bp, flag);
        this.eL();
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("Trusting", this.isTrusting());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setTrusting(nbttagcompound.getBoolean("Trusting"));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityOcelot.bp, false);
    }

    @Override
    protected void initPathfinder() {
        this.br = new EntityOcelot.b(this, 0.6D, EntityOcelot.bo, true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(3, this.br);
        this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
        this.goalSelector.a(8, new PathfinderGoalOcelotAttack(this));
        this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.8D));
        this.goalSelector.a(10, new PathfinderGoalRandomStrollLand(this, 0.8D, 1.0000001E-5F));
        this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityChicken.class, false));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityTurtle.class, 10, false, false, EntityTurtle.bo));
    }

    @Override
    public void mobTick() {
        if (this.getControllerMove().b()) {
            double d0 = this.getControllerMove().c();

            if (d0 == 0.6D) {
                this.setPose(EntityPose.CROUCHING);
                this.setSprinting(false);
            } else if (d0 == 1.33D) {
                this.setPose(EntityPose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(EntityPose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(EntityPose.STANDING);
            this.setSprinting(false);
        }

    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return !this.isTrusting() /*&& this.ticksLived > 2400*/; // CraftBukkit
    }

    public static AttributeProvider.Builder eK() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.30000001192092896D).a(GenericAttributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    public boolean b(float f, float f1) {
        return false;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_OCELOT_AMBIENT;
    }

    @Override
    public int D() {
        return 900;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_OCELOT_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_OCELOT_DEATH;
    }

    private float eN() {
        return (float) this.b(GenericAttributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        return entity.damageEntity(DamageSource.mobAttack(this), this.eN());
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return this.isInvulnerable(damagesource) ? false : super.damageEntity(damagesource, f);
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if ((this.br == null || this.br.h()) && !this.isTrusting() && this.k(itemstack) && entityhuman.h((Entity) this) < 9.0D) {
            this.a(entityhuman, itemstack);
            if (!this.world.isClientSide) {
                // CraftBukkit - added event call and isCancelled check
                if (this.random.nextInt(3) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    this.setTrusting(true);
                    this.u(true);
                    this.world.broadcastEntityEffect(this, (byte) 41);
                } else {
                    this.u(false);
                    this.world.broadcastEntityEffect(this, (byte) 40);
                }
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    private void u(boolean flag) {
        ParticleType particletype = Particles.HEART;

        if (!flag) {
            particletype = Particles.SMOKE;
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;

            this.world.addParticle(particletype, this.d(1.0D), this.cE() + 0.5D, this.g(1.0D), d0, d1, d2);
        }

    }

    protected void eL() {
        if (this.bq == null) {
            this.bq = new EntityOcelot.a<>(this, EntityHuman.class, 16.0F, 0.8D, 1.33D);
        }

        this.goalSelector.a((PathfinderGoal) this.bq);
        if (!this.isTrusting()) {
            this.goalSelector.a(4, this.bq);
        }

    }

    @Override
    public EntityOcelot createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityOcelot) EntityTypes.OCELOT.a((World) worldserver);
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return EntityOcelot.bo.test(itemstack);
    }

    public static boolean c(EntityTypes<EntityOcelot> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return random.nextInt(3) != 0;
    }

    @Override
    public boolean a(IWorldReader iworldreader) {
        if (iworldreader.j((Entity) this) && !iworldreader.containsLiquid(this.getBoundingBox())) {
            BlockPosition blockposition = this.getChunkCoordinates();

            if (blockposition.getY() < iworldreader.getSeaLevel()) {
                return false;
            }

            IBlockData iblockdata = iworldreader.getType(blockposition.down());

            if (iblockdata.a(Blocks.GRASS_BLOCK) || iblockdata.a((Tag) TagsBlock.LEAVES)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(1.0F);
        }

        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    static class b extends PathfinderGoalTempt {

        private final EntityOcelot c;

        public b(EntityOcelot entityocelot, double d0, RecipeItemStack recipeitemstack, boolean flag) {
            super(entityocelot, d0, recipeitemstack, flag);
            this.c = entityocelot;
        }

        @Override
        protected boolean g() {
            return super.g() && !this.c.isTrusting();
        }
    }

    static class a<T extends EntityLiving> extends PathfinderGoalAvoidTarget<T> {

        private final EntityOcelot i;

        public a(EntityOcelot entityocelot, Class<T> oclass, float f, double d0, double d1) {
            // Predicate predicate = IEntitySelector.e; // CraftBukkit - decompile error

            super(entityocelot, oclass, f, d0, d1, IEntitySelector.e::test); // CraftBukkit - decompile error
            this.i = entityocelot;
        }

        @Override
        public boolean a() {
            return !this.i.isTrusting() && super.a();
        }

        @Override
        public boolean b() {
            return !this.i.isTrusting() && super.b();
        }
    }
}
