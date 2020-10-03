package net.minecraft.server;

import java.util.EnumSet;
import java.util.Random;

public class EntityGhast extends EntityFlying implements IMonster {

    private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntityGhast.class, DataWatcherRegistry.i);
    private int c = 1;

    public EntityGhast(EntityTypes<? extends EntityGhast> entitytypes, World world) {
        super(entitytypes, world);
        this.f = 5;
        this.moveController = new EntityGhast.ControllerGhast(this);
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(5, new EntityGhast.PathfinderGoalGhastIdleMove(this));
        this.goalSelector.a(7, new EntityGhast.PathfinderGoalGhastMoveTowardsTarget(this));
        this.goalSelector.a(7, new EntityGhast.PathfinderGoalGhastAttackTarget(this));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 10, true, false, (entityliving) -> {
            return Math.abs(entityliving.locY() - this.locY()) <= 4.0D;
        }));
    }

    public void t(boolean flag) {
        this.datawatcher.set(EntityGhast.b, flag);
    }

    public int getPower() {
        return this.c;
    }

    @Override
    protected boolean L() {
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (damagesource.j() instanceof EntityLargeFireball && damagesource.getEntity() instanceof EntityHuman) {
            super.damageEntity(damagesource, 1000.0F);
            return true;
        } else {
            return super.damageEntity(damagesource, f);
        }
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityGhast.b, false);
    }

    public static AttributeProvider.Builder eJ() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.FOLLOW_RANGE, 100.0D);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_GHAST_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_GHAST_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_GHAST_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    public static boolean b(EntityTypes<EntityGhast> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return generatoraccess.getDifficulty() != EnumDifficulty.PEACEFUL && random.nextInt(20) == 0 && a(entitytypes, generatoraccess, enummobspawn, blockposition, random);
    }

    @Override
    public int getMaxSpawnGroup() {
        return 1;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("ExplosionPower", this.c);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("ExplosionPower", 99)) {
            this.c = nbttagcompound.getInt("ExplosionPower");
        }

    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 2.6F;
    }

    static class PathfinderGoalGhastAttackTarget extends PathfinderGoal {

        private final EntityGhast ghast;
        public int a;

        public PathfinderGoalGhastAttackTarget(EntityGhast entityghast) {
            this.ghast = entityghast;
        }

        @Override
        public boolean a() {
            return this.ghast.getGoalTarget() != null;
        }

        @Override
        public void c() {
            this.a = 0;
        }

        @Override
        public void d() {
            this.ghast.t(false);
        }

        @Override
        public void e() {
            EntityLiving entityliving = this.ghast.getGoalTarget();
            double d0 = 64.0D;

            if (entityliving.h((Entity) this.ghast) < 4096.0D && this.ghast.hasLineOfSight(entityliving)) {
                World world = this.ghast.world;

                ++this.a;
                if (this.a == 10 && !this.ghast.isSilent()) {
                    world.a((EntityHuman) null, 1015, this.ghast.getChunkCoordinates(), 0);
                }

                if (this.a == 20) {
                    double d1 = 4.0D;
                    Vec3D vec3d = this.ghast.f(1.0F);
                    double d2 = entityliving.locX() - (this.ghast.locX() + vec3d.x * 4.0D);
                    double d3 = entityliving.e(0.5D) - (0.5D + this.ghast.e(0.5D));
                    double d4 = entityliving.locZ() - (this.ghast.locZ() + vec3d.z * 4.0D);

                    if (!this.ghast.isSilent()) {
                        world.a((EntityHuman) null, 1016, this.ghast.getChunkCoordinates(), 0);
                    }

                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.ghast, d2, d3, d4);

                    // CraftBukkit - set bukkitYield when setting explosionpower
                    entitylargefireball.bukkitYield = entitylargefireball.yield = this.ghast.getPower();
                    entitylargefireball.setPosition(this.ghast.locX() + vec3d.x * 4.0D, this.ghast.e(0.5D) + 0.5D, entitylargefireball.locZ() + vec3d.z * 4.0D);
                    world.addEntity(entitylargefireball);
                    this.a = -40;
                }
            } else if (this.a > 0) {
                --this.a;
            }

            this.ghast.t(this.a > 10);
        }
    }

    static class PathfinderGoalGhastMoveTowardsTarget extends PathfinderGoal {

        private final EntityGhast a;

        public PathfinderGoalGhastMoveTowardsTarget(EntityGhast entityghast) {
            this.a = entityghast;
            this.a(EnumSet.of(PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            return true;
        }

        @Override
        public void e() {
            if (this.a.getGoalTarget() == null) {
                Vec3D vec3d = this.a.getMot();

                this.a.yaw = -((float) MathHelper.d(vec3d.x, vec3d.z)) * 57.295776F;
                this.a.aA = this.a.yaw;
            } else {
                EntityLiving entityliving = this.a.getGoalTarget();
                double d0 = 64.0D;

                if (entityliving.h((Entity) this.a) < 4096.0D) {
                    double d1 = entityliving.locX() - this.a.locX();
                    double d2 = entityliving.locZ() - this.a.locZ();

                    this.a.yaw = -((float) MathHelper.d(d1, d2)) * 57.295776F;
                    this.a.aA = this.a.yaw;
                }
            }

        }
    }

    static class PathfinderGoalGhastIdleMove extends PathfinderGoal {

        private final EntityGhast a;

        public PathfinderGoalGhastIdleMove(EntityGhast entityghast) {
            this.a = entityghast;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            ControllerMove controllermove = this.a.getControllerMove();

            if (!controllermove.b()) {
                return true;
            } else {
                double d0 = controllermove.d() - this.a.locX();
                double d1 = controllermove.e() - this.a.locY();
                double d2 = controllermove.f() - this.a.locZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        @Override
        public boolean b() {
            return false;
        }

        @Override
        public void c() {
            Random random = this.a.getRandom();
            double d0 = this.a.locX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.a.locY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.a.locZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);

            this.a.getControllerMove().a(d0, d1, d2, 1.0D);
        }
    }

    static class ControllerGhast extends ControllerMove {

        private final EntityGhast i;
        private int j;

        public ControllerGhast(EntityGhast entityghast) {
            super(entityghast);
            this.i = entityghast;
        }

        @Override
        public void a() {
            if (this.h == ControllerMove.Operation.MOVE_TO) {
                if (this.j-- <= 0) {
                    this.j += this.i.getRandom().nextInt(5) + 2;
                    Vec3D vec3d = new Vec3D(this.b - this.i.locX(), this.c - this.i.locY(), this.d - this.i.locZ());
                    double d0 = vec3d.f();

                    vec3d = vec3d.d();
                    if (this.a(vec3d, MathHelper.f(d0))) {
                        this.i.setMot(this.i.getMot().e(vec3d.a(0.1D)));
                    } else {
                        this.h = ControllerMove.Operation.WAIT;
                    }
                }

            }
        }

        private boolean a(Vec3D vec3d, int i) {
            AxisAlignedBB axisalignedbb = this.i.getBoundingBox();

            for (int j = 1; j < i; ++j) {
                axisalignedbb = axisalignedbb.c(vec3d);
                if (!this.i.world.getCubes(this.i, axisalignedbb)) {
                    return false;
                }
            }

            return true;
        }
    }
}
