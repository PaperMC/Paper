package net.minecraft.server;

import java.util.EnumSet;
import javax.annotation.Nullable;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityVex extends EntityMonster {

    protected static final DataWatcherObject<Byte> b = DataWatcher.a(EntityVex.class, DataWatcherRegistry.a);
    private EntityInsentient c;
    @Nullable
    private BlockPosition d;
    private boolean bo;
    private int bp;

    public EntityVex(EntityTypes<? extends EntityVex> entitytypes, World world) {
        super(entitytypes, world);
        this.moveController = new EntityVex.c(this);
        this.f = 3;
    }

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        super.move(enummovetype, vec3d);
        this.checkBlockCollisions();
    }

    @Override
    public void tick() {
        this.noclip = true;
        super.tick();
        this.noclip = false;
        this.setNoGravity(true);
        if (this.bo && --this.bp <= 0) {
            this.bp = 20;
            this.damageEntity(DamageSource.STARVE, 1.0F);
        }

    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(4, new EntityVex.a());
        this.goalSelector.a(8, new EntityVex.d());
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a(new Class[0])); // CraftBukkit - decompile error
        this.targetSelector.a(2, new EntityVex.b(this));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }

    public static AttributeProvider.Builder m() {
        return EntityMonster.eR().a(GenericAttributes.MAX_HEALTH, 14.0D).a(GenericAttributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityVex.b, (byte) 0);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKey("BoundX")) {
            this.d = new BlockPosition(nbttagcompound.getInt("BoundX"), nbttagcompound.getInt("BoundY"), nbttagcompound.getInt("BoundZ"));
        }

        if (nbttagcompound.hasKey("LifeTicks")) {
            this.a(nbttagcompound.getInt("LifeTicks"));
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if (this.d != null) {
            nbttagcompound.setInt("BoundX", this.d.getX());
            nbttagcompound.setInt("BoundY", this.d.getY());
            nbttagcompound.setInt("BoundZ", this.d.getZ());
        }

        if (this.bo) {
            nbttagcompound.setInt("LifeTicks", this.bp);
        }

    }

    public EntityInsentient eK() {
        return this.c;
    }

    @Nullable
    public BlockPosition eL() {
        return this.d;
    }

    public void g(@Nullable BlockPosition blockposition) {
        this.d = blockposition;
    }

    private boolean b(int i) {
        byte b0 = (Byte) this.datawatcher.get(EntityVex.b);

        return (b0 & i) != 0;
    }

    private void a(int i, boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityVex.b);
        int j;

        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.datawatcher.set(EntityVex.b, (byte) (j & 255));
    }

    public boolean isCharging() {
        return this.b(1);
    }

    public void setCharging(boolean flag) {
        this.a(1, flag);
    }

    public void a(EntityInsentient entityinsentient) {
        this.c = entityinsentient;
    }

    public void a(int i) {
        this.bo = true;
        this.bp = i;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_VEX_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_VEX_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_VEX_HURT;
    }

    @Override
    public float aQ() {
        return 1.0F;
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.a(difficultydamagescaler);
        this.b(difficultydamagescaler);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.a(EnumItemSlot.MAINHAND, 0.0F);
    }

    class b extends PathfinderGoalTarget {

        private final PathfinderTargetCondition b = (new PathfinderTargetCondition()).c().e();

        public b(EntityCreature entitycreature) {
            super(entitycreature, false);
        }

        @Override
        public boolean a() {
            return EntityVex.this.c != null && EntityVex.this.c.getGoalTarget() != null && this.a(EntityVex.this.c.getGoalTarget(), this.b);
        }

        @Override
        public void c() {
            EntityVex.this.setGoalTarget(EntityVex.this.c.getGoalTarget(), EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true); // CraftBukkit
            super.c();
        }
    }

    class d extends PathfinderGoal {

        public d() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            return !EntityVex.this.getControllerMove().b() && EntityVex.this.random.nextInt(7) == 0;
        }

        @Override
        public boolean b() {
            return false;
        }

        @Override
        public void e() {
            BlockPosition blockposition = EntityVex.this.eL();

            if (blockposition == null) {
                blockposition = EntityVex.this.getChunkCoordinates();
            }

            for (int i = 0; i < 3; ++i) {
                BlockPosition blockposition1 = blockposition.b(EntityVex.this.random.nextInt(15) - 7, EntityVex.this.random.nextInt(11) - 5, EntityVex.this.random.nextInt(15) - 7);

                if (EntityVex.this.world.isEmpty(blockposition1)) {
                    EntityVex.this.moveController.a((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.5D, (double) blockposition1.getZ() + 0.5D, 0.25D);
                    if (EntityVex.this.getGoalTarget() == null) {
                        EntityVex.this.getControllerLook().a((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.5D, (double) blockposition1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class a extends PathfinderGoal {

        public a() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            return EntityVex.this.getGoalTarget() != null && !EntityVex.this.getControllerMove().b() && EntityVex.this.random.nextInt(7) == 0 ? EntityVex.this.h((Entity) EntityVex.this.getGoalTarget()) > 4.0D : false;
        }

        @Override
        public boolean b() {
            return EntityVex.this.getControllerMove().b() && EntityVex.this.isCharging() && EntityVex.this.getGoalTarget() != null && EntityVex.this.getGoalTarget().isAlive();
        }

        @Override
        public void c() {
            EntityLiving entityliving = EntityVex.this.getGoalTarget();
            Vec3D vec3d = entityliving.j(1.0F);

            EntityVex.this.moveController.a(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityVex.this.setCharging(true);
            EntityVex.this.playSound(SoundEffects.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        @Override
        public void d() {
            EntityVex.this.setCharging(false);
        }

        @Override
        public void e() {
            EntityLiving entityliving = EntityVex.this.getGoalTarget();

            if (EntityVex.this.getBoundingBox().c(entityliving.getBoundingBox())) {
                EntityVex.this.attackEntity(entityliving);
                EntityVex.this.setCharging(false);
            } else {
                double d0 = EntityVex.this.h((Entity) entityliving);

                if (d0 < 9.0D) {
                    Vec3D vec3d = entityliving.j(1.0F);

                    EntityVex.this.moveController.a(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }

        }
    }

    class c extends ControllerMove {

        public c(EntityVex entityvex) {
            super(entityvex);
        }

        @Override
        public void a() {
            if (this.h == ControllerMove.Operation.MOVE_TO) {
                Vec3D vec3d = new Vec3D(this.b - EntityVex.this.locX(), this.c - EntityVex.this.locY(), this.d - EntityVex.this.locZ());
                double d0 = vec3d.f();

                if (d0 < EntityVex.this.getBoundingBox().a()) {
                    this.h = ControllerMove.Operation.WAIT;
                    EntityVex.this.setMot(EntityVex.this.getMot().a(0.5D));
                } else {
                    EntityVex.this.setMot(EntityVex.this.getMot().e(vec3d.a(this.e * 0.05D / d0)));
                    if (EntityVex.this.getGoalTarget() == null) {
                        Vec3D vec3d1 = EntityVex.this.getMot();

                        EntityVex.this.yaw = -((float) MathHelper.d(vec3d1.x, vec3d1.z)) * 57.295776F;
                        EntityVex.this.aA = EntityVex.this.yaw;
                    } else {
                        double d1 = EntityVex.this.getGoalTarget().locX() - EntityVex.this.locX();
                        double d2 = EntityVex.this.getGoalTarget().locZ() - EntityVex.this.locZ();

                        EntityVex.this.yaw = -((float) MathHelper.d(d1, d2)) * 57.295776F;
                        EntityVex.this.aA = EntityVex.this.yaw;
                    }
                }

            }
        }
    }
}
