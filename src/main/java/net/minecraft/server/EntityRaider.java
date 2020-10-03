package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public abstract class EntityRaider extends EntityMonsterPatrolling {

    protected static final DataWatcherObject<Boolean> c = DataWatcher.a(EntityRaider.class, DataWatcherRegistry.i);
    private static final Predicate<EntityItem> b = (entityitem) -> {
        return !entityitem.p() && entityitem.isAlive() && ItemStack.matches(entityitem.getItemStack(), Raid.s());
    };
    @Nullable
    protected Raid d;
    private int bo;
    private boolean canJoinRaid;
    private int bq;

    protected EntityRaider(EntityTypes<? extends EntityRaider> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new EntityRaider.b<>(this));
        this.goalSelector.a(3, new PathfinderGoalRaid<>(this));
        this.goalSelector.a(4, new EntityRaider.d(this, 1.0499999523162842D, 1));
        this.goalSelector.a(5, new EntityRaider.c(this));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityRaider.c, false);
    }

    public abstract void a(int i, boolean flag);

    public boolean isCanJoinRaid() {
        return this.canJoinRaid;
    }

    public void setCanJoinRaid(boolean flag) {
        this.canJoinRaid = flag;
    }

    @Override
    public void movementTick() {
        if (this.world instanceof WorldServer && this.isAlive()) {
            Raid raid = this.fa();

            if (this.isCanJoinRaid()) {
                if (raid == null) {
                    if (this.world.getTime() % 20L == 0L) {
                        Raid raid1 = ((WorldServer) this.world).b_(this.getChunkCoordinates());

                        if (raid1 != null && PersistentRaid.a(this, raid1)) {
                            raid1.a(raid1.getGroupsSpawned(), this, (BlockPosition) null, true);
                        }
                    }
                } else {
                    EntityLiving entityliving = this.getGoalTarget();

                    if (entityliving != null && (entityliving.getEntityType() == EntityTypes.PLAYER || entityliving.getEntityType() == EntityTypes.IRON_GOLEM)) {
                        this.ticksFarFromPlayer = 0;
                    }
                }
            }
        }

        super.movementTick();
    }

    @Override
    protected void eQ() {
        this.ticksFarFromPlayer += 2;
    }

    @Override
    public void die(DamageSource damagesource) {
        if (this.world instanceof WorldServer) {
            Entity entity = damagesource.getEntity();
            Raid raid = this.fa();

            if (raid != null) {
                if (this.isPatrolLeader()) {
                    raid.c(this.fc());
                }

                if (entity != null && entity.getEntityType() == EntityTypes.PLAYER) {
                    raid.a(entity);
                }

                raid.a(this, false);
            }

            if (this.isPatrolLeader() && raid == null && ((WorldServer) this.world).b_(this.getChunkCoordinates()) == null) {
                ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);
                EntityHuman entityhuman = null;

                if (entity instanceof EntityHuman) {
                    entityhuman = (EntityHuman) entity;
                } else if (entity instanceof EntityWolf) {
                    EntityWolf entitywolf = (EntityWolf) entity;
                    EntityLiving entityliving = entitywolf.getOwner();

                    if (entitywolf.isTamed() && entityliving instanceof EntityHuman) {
                        entityhuman = (EntityHuman) entityliving;
                    }
                }

                if (!itemstack.isEmpty() && ItemStack.matches(itemstack, Raid.s()) && entityhuman != null) {
                    MobEffect mobeffect = entityhuman.getEffect(MobEffects.BAD_OMEN);
                    byte b0 = 1;
                    int i;

                    if (mobeffect != null) {
                        i = b0 + mobeffect.getAmplifier();
                        entityhuman.c(MobEffects.BAD_OMEN);
                    } else {
                        i = b0 - 1;
                    }

                    i = MathHelper.clamp(i, 0, 4);
                    MobEffect mobeffect1 = new MobEffect(MobEffects.BAD_OMEN, 120000, i, false, false, true);

                    if (!this.world.getGameRules().getBoolean(GameRules.DISABLE_RAIDS)) {
                        entityhuman.addEffect(mobeffect1);
                    }
                }
            }
        }

        super.die(damagesource);
    }

    @Override
    public boolean eT() {
        return !this.fb();
    }

    public void a(@Nullable Raid raid) {
        this.d = raid;
    }

    @Nullable
    public Raid fa() {
        return this.d;
    }

    public boolean fb() {
        return this.fa() != null && this.fa().v();
    }

    public void a(int i) {
        this.bo = i;
    }

    public int fc() {
        return this.bo;
    }

    public void x(boolean flag) {
        this.datawatcher.set(EntityRaider.c, flag);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("Wave", this.bo);
        nbttagcompound.setBoolean("CanJoinRaid", this.canJoinRaid);
        if (this.d != null) {
            nbttagcompound.setInt("RaidId", this.d.getId());
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.bo = nbttagcompound.getInt("Wave");
        this.canJoinRaid = nbttagcompound.getBoolean("CanJoinRaid");
        if (nbttagcompound.hasKeyOfType("RaidId", 3)) {
            if (this.world instanceof WorldServer) {
                this.d = ((WorldServer) this.world).getPersistentRaid().a(nbttagcompound.getInt("RaidId"));
            }

            if (this.d != null) {
                this.d.a(this.bo, this, false);
                if (this.isPatrolLeader()) {
                    this.d.a(this.bo, this);
                }
            }
        }

    }

    @Override
    protected void b(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItemStack();
        boolean flag = this.fb() && this.fa().b(this.fc()) != null;

        if (this.fb() && !flag && ItemStack.matches(itemstack, Raid.s())) {
            EnumItemSlot enumitemslot = EnumItemSlot.HEAD;
            ItemStack itemstack1 = this.getEquipment(enumitemslot);
            double d0 = (double) this.e(enumitemslot);

            if (!itemstack1.isEmpty() && (double) Math.max(this.random.nextFloat() - 0.1F, 0.0F) < d0) {
                this.a(itemstack1);
            }

            this.a(entityitem);
            this.setSlot(enumitemslot, itemstack);
            this.receive(entityitem, itemstack.getCount());
            entityitem.die();
            this.fa().a(this.fc(), this);
            this.setPatrolLeader(true);
        } else {
            super.b(entityitem);
        }

    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return this.fa() == null ? super.isTypeNotPersistent(d0) : false;
    }

    @Override
    public boolean isSpecialPersistence() {
        return super.isSpecialPersistence() || this.fa() != null;
    }

    public int fe() {
        return this.bq;
    }

    public void b(int i) {
        this.bq = i;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.fb()) {
            this.fa().updateProgress();
        }

        return super.damageEntity(damagesource, f);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setCanJoinRaid(this.getEntityType() != EntityTypes.WITCH || enummobspawn != EnumMobSpawn.NATURAL);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    public abstract SoundEffect eL();

    static class d extends PathfinderGoal {

        private final EntityRaider a;
        private final double b;
        private BlockPosition c;
        private final List<BlockPosition> d = Lists.newArrayList();
        private final int e;
        private boolean f;

        public d(EntityRaider entityraider, double d0, int i) {
            this.a = entityraider;
            this.b = d0;
            this.e = i;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            this.j();
            return this.g() && this.h() && this.a.getGoalTarget() == null;
        }

        private boolean g() {
            return this.a.fb() && !this.a.fa().a();
        }

        private boolean h() {
            WorldServer worldserver = (WorldServer) this.a.world;
            BlockPosition blockposition = this.a.getChunkCoordinates();
            Optional<BlockPosition> optional = worldserver.y().a((villageplacetype) -> {
                return villageplacetype == VillagePlaceType.r;
            }, this::a, VillagePlace.Occupancy.ANY, blockposition, 48, this.a.random);

            if (!optional.isPresent()) {
                return false;
            } else {
                this.c = ((BlockPosition) optional.get()).immutableCopy();
                return true;
            }
        }

        @Override
        public boolean b() {
            return this.a.getNavigation().m() ? false : this.a.getGoalTarget() == null && !this.c.a((IPosition) this.a.getPositionVector(), (double) (this.a.getWidth() + (float) this.e)) && !this.f;
        }

        @Override
        public void d() {
            if (this.c.a((IPosition) this.a.getPositionVector(), (double) this.e)) {
                this.d.add(this.c);
            }

        }

        @Override
        public void c() {
            super.c();
            this.a.n(0);
            this.a.getNavigation().a((double) this.c.getX(), (double) this.c.getY(), (double) this.c.getZ(), this.b);
            this.f = false;
        }

        @Override
        public void e() {
            if (this.a.getNavigation().m()) {
                Vec3D vec3d = Vec3D.c((BaseBlockPosition) this.c);
                Vec3D vec3d1 = RandomPositionGenerator.a(this.a, 16, 7, vec3d, 0.3141592741012573D);

                if (vec3d1 == null) {
                    vec3d1 = RandomPositionGenerator.b(this.a, 8, 7, vec3d);
                }

                if (vec3d1 == null) {
                    this.f = true;
                    return;
                }

                this.a.getNavigation().a(vec3d1.x, vec3d1.y, vec3d1.z, this.b);
            }

        }

        private boolean a(BlockPosition blockposition) {
            Iterator iterator = this.d.iterator();

            BlockPosition blockposition1;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                blockposition1 = (BlockPosition) iterator.next();
            } while (!Objects.equals(blockposition, blockposition1));

            return false;
        }

        private void j() {
            if (this.d.size() > 2) {
                this.d.remove(0);
            }

        }
    }

    public class a extends PathfinderGoal {

        private final EntityRaider c;
        private final float d;
        public final PathfinderTargetCondition a = (new PathfinderTargetCondition()).a(8.0D).d().a().b().c().e();

        public a(EntityIllagerAbstract entityillagerabstract, float f) {
            this.c = entityillagerabstract;
            this.d = f * f;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            EntityLiving entityliving = this.c.getLastDamager();

            return this.c.fa() == null && this.c.isPatrolling() && this.c.getGoalTarget() != null && !this.c.isAggressive() && (entityliving == null || entityliving.getEntityType() != EntityTypes.PLAYER);
        }

        @Override
        public void c() {
            super.c();
            this.c.getNavigation().o();
            List<EntityRaider> list = this.c.world.a(EntityRaider.class, this.a, this.c, this.c.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityRaider entityraider = (EntityRaider) iterator.next();

                entityraider.setGoalTarget(this.c.getGoalTarget());
            }

        }

        @Override
        public void d() {
            super.d();
            EntityLiving entityliving = this.c.getGoalTarget();

            if (entityliving != null) {
                List<EntityRaider> list = this.c.world.a(EntityRaider.class, this.a, this.c, this.c.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityRaider entityraider = (EntityRaider) iterator.next();

                    entityraider.setGoalTarget(entityliving);
                    entityraider.setAggressive(true);
                }

                this.c.setAggressive(true);
            }

        }

        @Override
        public void e() {
            EntityLiving entityliving = this.c.getGoalTarget();

            if (entityliving != null) {
                if (this.c.h((Entity) entityliving) > (double) this.d) {
                    this.c.getControllerLook().a(entityliving, 30.0F, 30.0F);
                    if (this.c.random.nextInt(50) == 0) {
                        this.c.F();
                    }
                } else {
                    this.c.setAggressive(true);
                }

                super.e();
            }
        }
    }

    public class c extends PathfinderGoal {

        private final EntityRaider b;

        c(EntityRaider entityraider) {
            this.b = entityraider;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            Raid raid = this.b.fa();

            return this.b.isAlive() && this.b.getGoalTarget() == null && raid != null && raid.isLoss();
        }

        @Override
        public void c() {
            this.b.x(true);
            super.c();
        }

        @Override
        public void d() {
            this.b.x(false);
            super.d();
        }

        @Override
        public void e() {
            if (!this.b.isSilent() && this.b.random.nextInt(100) == 0) {
                EntityRaider.this.playSound(EntityRaider.this.eL(), EntityRaider.this.getSoundVolume(), EntityRaider.this.dG());
            }

            if (!this.b.isPassenger() && this.b.random.nextInt(50) == 0) {
                this.b.getControllerJump().jump();
            }

            super.e();
        }
    }

    public class b<T extends EntityRaider> extends PathfinderGoal {

        private final T b;

        public b(EntityRaider entityraider) {
            this.b = entityraider;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            Raid raid = this.b.fa();

            if (this.b.fb() && !this.b.fa().a() && this.b.eN() && !ItemStack.matches(this.b.getEquipment(EnumItemSlot.HEAD), Raid.s())) {
                EntityRaider entityraider = raid.b(this.b.fc());

                if (entityraider == null || !entityraider.isAlive()) {
                    List<EntityItem> list = this.b.world.a(EntityItem.class, this.b.getBoundingBox().grow(16.0D, 8.0D, 16.0D), EntityRaider.b);

                    if (!list.isEmpty()) {
                        return this.b.getNavigation().a((Entity) list.get(0), 1.149999976158142D);
                    }
                }

                return false;
            } else {
                return false;
            }
        }

        @Override
        public void e() {
            if (this.b.getNavigation().h().a((IPosition) this.b.getPositionVector(), 1.414D)) {
                List<EntityItem> list = this.b.world.a(EntityItem.class, this.b.getBoundingBox().grow(4.0D, 4.0D, 4.0D), EntityRaider.b);

                if (!list.isEmpty()) {
                    this.b.b((EntityItem) list.get(0));
                }
            }

        }
    }
}
