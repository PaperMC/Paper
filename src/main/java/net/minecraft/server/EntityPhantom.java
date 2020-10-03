package net.minecraft.server;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class EntityPhantom extends EntityFlying implements IMonster {

    private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityPhantom.class, DataWatcherRegistry.b);
    private Vec3D c;
    private BlockPosition d;
    private EntityPhantom.AttackPhase bo;

    public EntityPhantom(EntityTypes<? extends EntityPhantom> entitytypes, World world) {
        super(entitytypes, world);
        this.c = Vec3D.ORIGIN;
        this.d = BlockPosition.ZERO;
        this.bo = EntityPhantom.AttackPhase.CIRCLE;
        this.f = 5;
        this.moveController = new EntityPhantom.g(this);
        this.lookController = new EntityPhantom.f(this);
    }

    @Override
    protected EntityAIBodyControl r() {
        return new EntityPhantom.d(this);
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new EntityPhantom.c());
        this.goalSelector.a(2, new EntityPhantom.i());
        this.goalSelector.a(3, new EntityPhantom.e());
        this.targetSelector.a(1, new EntityPhantom.b());
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityPhantom.b, 0);
    }

    public void setSize(int i) {
        this.datawatcher.set(EntityPhantom.b, MathHelper.clamp(i, 0, 64));
    }

    private void eJ() {
        this.updateSize();
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double) (6 + this.getSize()));
    }

    public int getSize() {
        return (Integer) this.datawatcher.get(EntityPhantom.b);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.35F;
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityPhantom.b.equals(datawatcherobject)) {
            this.eJ();
        }

        super.a(datawatcherobject);
    }

    @Override
    protected boolean L() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide) {
            float f = MathHelper.cos((float) (this.getId() * 3 + this.ticksLived) * 0.13F + 3.1415927F);
            float f1 = MathHelper.cos((float) (this.getId() * 3 + this.ticksLived + 1) * 0.13F + 3.1415927F);

            if (f > 0.0F && f1 <= 0.0F) {
                this.world.a(this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
            }

            int i = this.getSize();
            float f2 = MathHelper.cos(this.yaw * 0.017453292F) * (1.3F + 0.21F * (float) i);
            float f3 = MathHelper.sin(this.yaw * 0.017453292F) * (1.3F + 0.21F * (float) i);
            float f4 = (0.3F + f * 0.45F) * ((float) i * 0.2F + 1.0F);

            this.world.addParticle(Particles.MYCELIUM, this.locX() + (double) f2, this.locY() + (double) f4, this.locZ() + (double) f3, 0.0D, 0.0D, 0.0D);
            this.world.addParticle(Particles.MYCELIUM, this.locX() - (double) f2, this.locY() + (double) f4, this.locZ() - (double) f3, 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    public void movementTick() {
        if (this.isAlive() && this.eG()) {
            this.setOnFire(8);
        }

        super.movementTick();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.d = this.getChunkCoordinates().up(5);
        this.setSize(0);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKey("AX")) {
            this.d = new BlockPosition(nbttagcompound.getInt("AX"), nbttagcompound.getInt("AY"), nbttagcompound.getInt("AZ"));
        }

        this.setSize(nbttagcompound.getInt("Size"));
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("AX", this.d.getX());
        nbttagcompound.setInt("AY", this.d.getY());
        nbttagcompound.setInt("AZ", this.d.getZ());
        nbttagcompound.setInt("Size", this.getSize());
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PHANTOM_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PHANTOM_DEATH;
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public boolean a(EntityTypes<?> entitytypes) {
        return true;
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        int i = this.getSize();
        EntitySize entitysize = super.a(entitypose);
        float f = (entitysize.width + 0.2F * (float) i) / entitysize.width;

        return entitysize.a(f);
    }

    class b extends PathfinderGoal {

        private final PathfinderTargetCondition b;
        private int c;

        private b() {
            this.b = (new PathfinderTargetCondition()).a(64.0D);
            this.c = 20;
        }

        @Override
        public boolean a() {
            if (this.c > 0) {
                --this.c;
                return false;
            } else {
                this.c = 60;
                List<EntityHuman> list = EntityPhantom.this.world.a(this.b, (EntityLiving) EntityPhantom.this, EntityPhantom.this.getBoundingBox().grow(16.0D, 64.0D, 16.0D));

                if (!list.isEmpty()) {
                    list.sort(Comparator.comparing(Entity::locY).reversed());
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityHuman entityhuman = (EntityHuman) iterator.next();

                        if (EntityPhantom.this.a((EntityLiving) entityhuman, PathfinderTargetCondition.a)) {
                            EntityPhantom.this.setGoalTarget(entityhuman);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        @Override
        public boolean b() {
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            return entityliving != null ? EntityPhantom.this.a(entityliving, PathfinderTargetCondition.a) : false;
        }
    }

    class c extends PathfinderGoal {

        private int b;

        private c() {}

        @Override
        public boolean a() {
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            return entityliving != null ? EntityPhantom.this.a(EntityPhantom.this.getGoalTarget(), PathfinderTargetCondition.a) : false;
        }

        @Override
        public void c() {
            this.b = 10;
            EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
            this.g();
        }

        @Override
        public void d() {
            EntityPhantom.this.d = EntityPhantom.this.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, EntityPhantom.this.d).up(10 + EntityPhantom.this.random.nextInt(20));
        }

        @Override
        public void e() {
            if (EntityPhantom.this.bo == EntityPhantom.AttackPhase.CIRCLE) {
                --this.b;
                if (this.b <= 0) {
                    EntityPhantom.this.bo = EntityPhantom.AttackPhase.SWOOP;
                    this.g();
                    this.b = (8 + EntityPhantom.this.random.nextInt(4)) * 20;
                    EntityPhantom.this.playSound(SoundEffects.ENTITY_PHANTOM_SWOOP, 10.0F, 0.95F + EntityPhantom.this.random.nextFloat() * 0.1F);
                }
            }

        }

        private void g() {
            EntityPhantom.this.d = EntityPhantom.this.getGoalTarget().getChunkCoordinates().up(20 + EntityPhantom.this.random.nextInt(20));
            if (EntityPhantom.this.d.getY() < EntityPhantom.this.world.getSeaLevel()) {
                EntityPhantom.this.d = new BlockPosition(EntityPhantom.this.d.getX(), EntityPhantom.this.world.getSeaLevel() + 1, EntityPhantom.this.d.getZ());
            }

        }
    }

    class i extends EntityPhantom.h {

        private i() {
            super();
        }

        @Override
        public boolean a() {
            return EntityPhantom.this.getGoalTarget() != null && EntityPhantom.this.bo == EntityPhantom.AttackPhase.SWOOP;
        }

        @Override
        public boolean b() {
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            if (entityliving == null) {
                return false;
            } else if (!entityliving.isAlive()) {
                return false;
            } else if (entityliving instanceof EntityHuman && (((EntityHuman) entityliving).isSpectator() || ((EntityHuman) entityliving).isCreative())) {
                return false;
            } else if (!this.a()) {
                return false;
            } else {
                if (EntityPhantom.this.ticksLived % 20 == 0) {
                    List<EntityCat> list = EntityPhantom.this.world.a(EntityCat.class, EntityPhantom.this.getBoundingBox().g(16.0D), IEntitySelector.a);

                    if (!list.isEmpty()) {
                        Iterator iterator = list.iterator();

                        while (iterator.hasNext()) {
                            EntityCat entitycat = (EntityCat) iterator.next();

                            entitycat.eZ();
                        }

                        return false;
                    }
                }

                return true;
            }
        }

        @Override
        public void c() {}

        @Override
        public void d() {
            EntityPhantom.this.setGoalTarget((EntityLiving) null);
            EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
        }

        @Override
        public void e() {
            EntityLiving entityliving = EntityPhantom.this.getGoalTarget();

            EntityPhantom.this.c = new Vec3D(entityliving.locX(), entityliving.e(0.5D), entityliving.locZ());
            if (EntityPhantom.this.getBoundingBox().g(0.20000000298023224D).c(entityliving.getBoundingBox())) {
                EntityPhantom.this.attackEntity(entityliving);
                EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
                if (!EntityPhantom.this.isSilent()) {
                    EntityPhantom.this.world.triggerEffect(1039, EntityPhantom.this.getChunkCoordinates(), 0);
                }
            } else if (EntityPhantom.this.positionChanged || EntityPhantom.this.hurtTicks > 0) {
                EntityPhantom.this.bo = EntityPhantom.AttackPhase.CIRCLE;
            }

        }
    }

    class e extends EntityPhantom.h {

        private float c;
        private float d;
        private float e;
        private float f;

        private e() {
            super();
        }

        @Override
        public boolean a() {
            return EntityPhantom.this.getGoalTarget() == null || EntityPhantom.this.bo == EntityPhantom.AttackPhase.CIRCLE;
        }

        @Override
        public void c() {
            this.d = 5.0F + EntityPhantom.this.random.nextFloat() * 10.0F;
            this.e = -4.0F + EntityPhantom.this.random.nextFloat() * 9.0F;
            this.f = EntityPhantom.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.h();
        }

        @Override
        public void e() {
            if (EntityPhantom.this.random.nextInt(350) == 0) {
                this.e = -4.0F + EntityPhantom.this.random.nextFloat() * 9.0F;
            }

            if (EntityPhantom.this.random.nextInt(250) == 0) {
                ++this.d;
                if (this.d > 15.0F) {
                    this.d = 5.0F;
                    this.f = -this.f;
                }
            }

            if (EntityPhantom.this.random.nextInt(450) == 0) {
                this.c = EntityPhantom.this.random.nextFloat() * 2.0F * 3.1415927F;
                this.h();
            }

            if (this.g()) {
                this.h();
            }

            if (EntityPhantom.this.c.y < EntityPhantom.this.locY() && !EntityPhantom.this.world.isEmpty(EntityPhantom.this.getChunkCoordinates().down(1))) {
                this.e = Math.max(1.0F, this.e);
                this.h();
            }

            if (EntityPhantom.this.c.y > EntityPhantom.this.locY() && !EntityPhantom.this.world.isEmpty(EntityPhantom.this.getChunkCoordinates().up(1))) {
                this.e = Math.min(-1.0F, this.e);
                this.h();
            }

        }

        private void h() {
            if (BlockPosition.ZERO.equals(EntityPhantom.this.d)) {
                EntityPhantom.this.d = EntityPhantom.this.getChunkCoordinates();
            }

            this.c += this.f * 15.0F * 0.017453292F;
            EntityPhantom.this.c = Vec3D.b((BaseBlockPosition) EntityPhantom.this.d).add((double) (this.d * MathHelper.cos(this.c)), (double) (-4.0F + this.e), (double) (this.d * MathHelper.sin(this.c)));
        }
    }

    abstract class h extends PathfinderGoal {

        public h() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        protected boolean g() {
            return EntityPhantom.this.c.c(EntityPhantom.this.locX(), EntityPhantom.this.locY(), EntityPhantom.this.locZ()) < 4.0D;
        }
    }

    class f extends ControllerLook {

        public f(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        public void a() {}
    }

    class d extends EntityAIBodyControl {

        public d(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        public void a() {
            EntityPhantom.this.aC = EntityPhantom.this.aA;
            EntityPhantom.this.aA = EntityPhantom.this.yaw;
        }
    }

    class g extends ControllerMove {

        private float j = 0.1F;

        public g(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        public void a() {
            if (EntityPhantom.this.positionChanged) {
                EntityPhantom.this.yaw += 180.0F;
                this.j = 0.1F;
            }

            float f = (float) (EntityPhantom.this.c.x - EntityPhantom.this.locX());
            float f1 = (float) (EntityPhantom.this.c.y - EntityPhantom.this.locY());
            float f2 = (float) (EntityPhantom.this.c.z - EntityPhantom.this.locZ());
            double d0 = (double) MathHelper.c(f * f + f2 * f2);
            double d1 = 1.0D - (double) MathHelper.e(f1 * 0.7F) / d0;

            f = (float) ((double) f * d1);
            f2 = (float) ((double) f2 * d1);
            d0 = (double) MathHelper.c(f * f + f2 * f2);
            double d2 = (double) MathHelper.c(f * f + f2 * f2 + f1 * f1);
            float f3 = EntityPhantom.this.yaw;
            float f4 = (float) MathHelper.d((double) f2, (double) f);
            float f5 = MathHelper.g(EntityPhantom.this.yaw + 90.0F);
            float f6 = MathHelper.g(f4 * 57.295776F);

            EntityPhantom.this.yaw = MathHelper.d(f5, f6, 4.0F) - 90.0F;
            EntityPhantom.this.aA = EntityPhantom.this.yaw;
            if (MathHelper.d(f3, EntityPhantom.this.yaw) < 3.0F) {
                this.j = MathHelper.c(this.j, 1.8F, 0.005F * (1.8F / this.j));
            } else {
                this.j = MathHelper.c(this.j, 0.2F, 0.025F);
            }

            float f7 = (float) (-(MathHelper.d((double) (-f1), d0) * 57.2957763671875D));

            EntityPhantom.this.pitch = f7;
            float f8 = EntityPhantom.this.yaw + 90.0F;
            double d3 = (double) (this.j * MathHelper.cos(f8 * 0.017453292F)) * Math.abs((double) f / d2);
            double d4 = (double) (this.j * MathHelper.sin(f8 * 0.017453292F)) * Math.abs((double) f2 / d2);
            double d5 = (double) (this.j * MathHelper.sin(f7 * 0.017453292F)) * Math.abs((double) f1 / d2);
            Vec3D vec3d = EntityPhantom.this.getMot();

            EntityPhantom.this.setMot(vec3d.e((new Vec3D(d3, d5, d4)).d(vec3d).a(0.2D)));
        }
    }

    static enum AttackPhase {

        CIRCLE, SWOOP;

        private AttackPhase() {}
    }
}
