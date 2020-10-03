package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityTurtle extends EntityAnimal {

    private static final DataWatcherObject<BlockPosition> bp = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> bq = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> br = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<BlockPosition> bs = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> bt = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> bu = DataWatcher.a(EntityTurtle.class, DataWatcherRegistry.i);
    private int bv;
    public static final Predicate<EntityLiving> bo = (entityliving) -> {
        return entityliving.isBaby() && !entityliving.isInWater();
    };

    public EntityTurtle(EntityTypes<? extends EntityTurtle> entitytypes, World world) {
        super(entitytypes, world);
        this.a(PathType.WATER, 0.0F);
        this.moveController = new EntityTurtle.e(this);
        this.G = 1.0F;
    }

    public void setHomePos(BlockPosition blockposition) {
        this.datawatcher.set(EntityTurtle.bp, blockposition);
    }

    private BlockPosition getHomePos() {
        return (BlockPosition) this.datawatcher.get(EntityTurtle.bp);
    }

    private void setTravelPos(BlockPosition blockposition) {
        this.datawatcher.set(EntityTurtle.bs, blockposition);
    }

    private BlockPosition getTravelPos() {
        return (BlockPosition) this.datawatcher.get(EntityTurtle.bs);
    }

    public boolean hasEgg() {
        return (Boolean) this.datawatcher.get(EntityTurtle.bq);
    }

    private void setHasEgg(boolean flag) {
        this.datawatcher.set(EntityTurtle.bq, flag);
    }

    public boolean eL() {
        return (Boolean) this.datawatcher.get(EntityTurtle.br);
    }

    private void u(boolean flag) {
        this.bv = flag ? 1 : 0;
        this.datawatcher.set(EntityTurtle.br, flag);
    }

    private boolean eU() {
        return (Boolean) this.datawatcher.get(EntityTurtle.bt);
    }

    private void v(boolean flag) {
        this.datawatcher.set(EntityTurtle.bt, flag);
    }

    private boolean eV() {
        return (Boolean) this.datawatcher.get(EntityTurtle.bu);
    }

    private void w(boolean flag) {
        this.datawatcher.set(EntityTurtle.bu, flag);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityTurtle.bp, BlockPosition.ZERO);
        this.datawatcher.register(EntityTurtle.bq, false);
        this.datawatcher.register(EntityTurtle.bs, BlockPosition.ZERO);
        this.datawatcher.register(EntityTurtle.bt, false);
        this.datawatcher.register(EntityTurtle.bu, false);
        this.datawatcher.register(EntityTurtle.br, false);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("HomePosX", this.getHomePos().getX());
        nbttagcompound.setInt("HomePosY", this.getHomePos().getY());
        nbttagcompound.setInt("HomePosZ", this.getHomePos().getZ());
        nbttagcompound.setBoolean("HasEgg", this.hasEgg());
        nbttagcompound.setInt("TravelPosX", this.getTravelPos().getX());
        nbttagcompound.setInt("TravelPosY", this.getTravelPos().getY());
        nbttagcompound.setInt("TravelPosZ", this.getTravelPos().getZ());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInt("HomePosX");
        int j = nbttagcompound.getInt("HomePosY");
        int k = nbttagcompound.getInt("HomePosZ");

        this.setHomePos(new BlockPosition(i, j, k));
        super.loadData(nbttagcompound);
        this.setHasEgg(nbttagcompound.getBoolean("HasEgg"));
        int l = nbttagcompound.getInt("TravelPosX");
        int i1 = nbttagcompound.getInt("TravelPosY");
        int j1 = nbttagcompound.getInt("TravelPosZ");

        this.setTravelPos(new BlockPosition(l, i1, j1));
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setHomePos(this.getChunkCoordinates());
        this.setTravelPos(BlockPosition.ZERO);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    public static boolean c(EntityTypes<EntityTurtle> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return blockposition.getY() < generatoraccess.getSeaLevel() + 4 && BlockTurtleEgg.a((IBlockAccess) generatoraccess, blockposition) && generatoraccess.getLightLevel(blockposition, 0) > 8;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new EntityTurtle.f(this, 1.2D));
        this.goalSelector.a(1, new EntityTurtle.a(this, 1.0D));
        this.goalSelector.a(1, new EntityTurtle.d(this, 1.0D));
        this.goalSelector.a(2, new EntityTurtle.i(this, 1.1D, Blocks.SEAGRASS.getItem()));
        this.goalSelector.a(3, new EntityTurtle.c(this, 1.0D));
        this.goalSelector.a(4, new EntityTurtle.b(this, 1.0D));
        this.goalSelector.a(7, new EntityTurtle.j(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(9, new EntityTurtle.h(this, 1.0D, 100));
    }

    public static AttributeProvider.Builder eM() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 30.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean bU() {
        return false;
    }

    @Override
    public boolean cL() {
        return true;
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.WATER_MOB;
    }

    @Override
    public int D() {
        return 200;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return !this.isInWater() && this.onGround && !this.isBaby() ? SoundEffects.ENTITY_TURTLE_AMBIENT_LAND : super.getSoundAmbient();
    }

    @Override
    protected void d(float f) {
        super.d(f * 1.5F);
    }

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_TURTLE_SWIM;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return this.isBaby() ? SoundEffects.ENTITY_TURTLE_HURT_BABY : SoundEffects.ENTITY_TURTLE_HURT;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundDeath() {
        return this.isBaby() ? SoundEffects.ENTITY_TURTLE_DEATH_BABY : SoundEffects.ENTITY_TURTLE_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        SoundEffect soundeffect = this.isBaby() ? SoundEffects.ENTITY_TURTLE_SHAMBLE_BABY : SoundEffects.ENTITY_TURTLE_SHAMBLE;

        this.playSound(soundeffect, 0.15F, 1.0F);
    }

    @Override
    public boolean eP() {
        return super.eP() && !this.hasEgg();
    }

    @Override
    protected float as() {
        return this.B + 0.15F;
    }

    @Override
    public float cR() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Override
    protected NavigationAbstract b(World world) {
        return new EntityTurtle.g(this, world);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityAgeable) EntityTypes.TURTLE.a((World) worldserver);
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return itemstack.getItem() == Blocks.SEAGRASS.getItem();
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return !this.eU() && iworldreader.getFluid(blockposition).a((Tag) TagsFluid.WATER) ? 10.0F : (BlockTurtleEgg.a((IBlockAccess) iworldreader, blockposition) ? 10.0F : iworldreader.y(blockposition) - 0.5F);
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.isAlive() && this.eL() && this.bv >= 1 && this.bv % 5 == 0) {
            BlockPosition blockposition = this.getChunkCoordinates();

            if (BlockTurtleEgg.a((IBlockAccess) this.world, blockposition)) {
                this.world.triggerEffect(2001, blockposition, Block.getCombinedId(Blocks.SAND.getBlockData()));
            }
        }

    }

    @Override
    protected void m() {
        super.m();
        if (!this.isBaby() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            this.forceDrops = true; // CraftBukkit
            this.a((IMaterial) Items.SCUTE, 1);
            this.forceDrops = false; // CraftBukkit
        }

    }

    @Override
    public void g(Vec3D vec3d) {
        if (this.doAITick() && this.isInWater()) {
            this.a(0.1F, vec3d);
            this.move(EnumMoveType.SELF, this.getMot());
            this.setMot(this.getMot().a(0.9D));
            if (this.getGoalTarget() == null && (!this.eU() || !this.getHomePos().a((IPosition) this.getPositionVector(), 20.0D))) {
                this.setMot(this.getMot().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.g(vec3d);
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return false;
    }

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {
        org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = entitylightning; // CraftBukkit
        this.damageEntity(DamageSource.LIGHTNING, Float.MAX_VALUE);
        org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = null; // CraftBukkit
    }

    static class g extends NavigationGuardian {

        g(EntityTurtle entityturtle, World world) {
            super(entityturtle, world);
        }

        @Override
        protected boolean a() {
            return true;
        }

        @Override
        protected Pathfinder a(int i) {
            this.o = new PathfinderTurtle();
            return new Pathfinder(this.o, i);
        }

        @Override
        public boolean a(BlockPosition blockposition) {
            if (this.a instanceof EntityTurtle) {
                EntityTurtle entityturtle = (EntityTurtle) this.a;

                if (entityturtle.eV()) {
                    return this.b.getType(blockposition).a(Blocks.WATER);
                }
            }

            return !this.b.getType(blockposition.down()).isAir();
        }
    }

    static class e extends ControllerMove {

        private final EntityTurtle i;

        e(EntityTurtle entityturtle) {
            super(entityturtle);
            this.i = entityturtle;
        }

        private void g() {
            if (this.i.isInWater()) {
                this.i.setMot(this.i.getMot().add(0.0D, 0.005D, 0.0D));
                if (!this.i.getHomePos().a((IPosition) this.i.getPositionVector(), 16.0D)) {
                    this.i.q(Math.max(this.i.dM() / 2.0F, 0.08F));
                }

                if (this.i.isBaby()) {
                    this.i.q(Math.max(this.i.dM() / 3.0F, 0.06F));
                }
            } else if (this.i.onGround) {
                this.i.q(Math.max(this.i.dM() / 2.0F, 0.06F));
            }

        }

        @Override
        public void a() {
            this.g();
            if (this.h == ControllerMove.Operation.MOVE_TO && !this.i.getNavigation().m()) {
                double d0 = this.b - this.i.locX();
                double d1 = this.c - this.i.locY();
                double d2 = this.d - this.i.locZ();
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                d1 /= d3;
                float f = (float) (MathHelper.d(d2, d0) * 57.2957763671875D) - 90.0F;

                this.i.yaw = this.a(this.i.yaw, f, 90.0F);
                this.i.aA = this.i.yaw;
                float f1 = (float) (this.e * this.i.b(GenericAttributes.MOVEMENT_SPEED));

                this.i.q(MathHelper.g(0.125F, this.i.dM(), f1));
                this.i.setMot(this.i.getMot().add(0.0D, (double) this.i.dM() * d1 * 0.1D, 0.0D));
            } else {
                this.i.q(0.0F);
            }
        }
    }

    static class c extends PathfinderGoalGotoTarget {

        private final EntityTurtle g;

        private c(EntityTurtle entityturtle, double d0) {
            super(entityturtle, entityturtle.isBaby() ? 2.0D : d0, 24);
            this.g = entityturtle;
            this.f = -1;
        }

        @Override
        public boolean b() {
            return !this.g.isInWater() && this.d <= 1200 && this.a(this.g.world, this.e);
        }

        @Override
        public boolean a() {
            return this.g.isBaby() && !this.g.isInWater() ? super.a() : (!this.g.eU() && !this.g.isInWater() && !this.g.hasEgg() ? super.a() : false);
        }

        @Override
        public boolean k() {
            return this.d % 160 == 0;
        }

        @Override
        protected boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
            return iworldreader.getType(blockposition).a(Blocks.WATER);
        }
    }

    static class h extends PathfinderGoalRandomStroll {

        private final EntityTurtle h;

        private h(EntityTurtle entityturtle, double d0, int i) {
            super(entityturtle, d0, i);
            this.h = entityturtle;
        }

        @Override
        public boolean a() {
            return !this.a.isInWater() && !this.h.eU() && !this.h.hasEgg() ? super.a() : false;
        }
    }

    static class d extends PathfinderGoalGotoTarget {

        private final EntityTurtle g;

        d(EntityTurtle entityturtle, double d0) {
            super(entityturtle, d0, 16);
            this.g = entityturtle;
        }

        @Override
        public boolean a() {
            return this.g.hasEgg() && this.g.getHomePos().a((IPosition) this.g.getPositionVector(), 9.0D) ? super.a() : false;
        }

        @Override
        public boolean b() {
            return super.b() && this.g.hasEgg() && this.g.getHomePos().a((IPosition) this.g.getPositionVector(), 9.0D);
        }

        @Override
        public void e() {
            super.e();
            BlockPosition blockposition = this.g.getChunkCoordinates();

            if (!this.g.isInWater() && this.l()) {
                if (this.g.bv < 1) {
                    this.g.u(true);
                } else if (this.g.bv > 200) {
                    World world = this.g.world;

                    // CraftBukkit start
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.g, this.e.up(), (IBlockData) Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, this.g.random.nextInt(4) + 1)).isCancelled()) {
                    world.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                    world.setTypeAndData(this.e.up(), (IBlockData) Blocks.TURTLE_EGG.getBlockData().set(BlockTurtleEgg.b, this.g.random.nextInt(4) + 1), 3);
                    }
                    // CraftBukkit end
                    this.g.setHasEgg(false);
                    this.g.u(false);
                    this.g.setLoveTicks(600);
                }

                if (this.g.eL()) {
                    this.g.bv++;
                }
            }

        }

        @Override
        protected boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
            return !iworldreader.isEmpty(blockposition.up()) ? false : BlockTurtleEgg.b(iworldreader, blockposition);
        }
    }

    static class a extends PathfinderGoalBreed {

        private final EntityTurtle d;

        a(EntityTurtle entityturtle, double d0) {
            super(entityturtle, d0);
            this.d = entityturtle;
        }

        @Override
        public boolean a() {
            return super.a() && !this.d.hasEgg();
        }

        @Override
        protected void g() {
            EntityPlayer entityplayer = this.animal.getBreedCause();

            if (entityplayer == null && this.partner.getBreedCause() != null) {
                entityplayer = this.partner.getBreedCause();
            }

            if (entityplayer != null) {
                entityplayer.a(StatisticList.ANIMALS_BRED);
                CriterionTriggers.o.a(entityplayer, this.animal, this.partner, (EntityAgeable) null);
            }

            this.d.setHasEgg(true);
            this.animal.resetLove();
            this.partner.resetLove();
            Random random = this.animal.getRandom();

            if (this.b.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.b.addEntity(new EntityExperienceOrb(this.b, this.animal.locX(), this.animal.locY(), this.animal.locZ(), random.nextInt(7) + 1));
            }

        }
    }

    static class i extends PathfinderGoal {

        private static final PathfinderTargetCondition a = (new PathfinderTargetCondition()).a(10.0D).b().a();
        private final EntityTurtle b;
        private final double c;
        private EntityHuman d;
        private int e;
        private final Set<Item> f;

        i(EntityTurtle entityturtle, double d0, Item item) {
            this.b = entityturtle;
            this.c = d0;
            this.f = Sets.newHashSet(new Item[]{item});
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            if (this.e > 0) {
                --this.e;
                return false;
            } else {
                this.d = this.b.world.a(this.a, (EntityLiving) this.b); // CraftBukkit - decompile error
                return this.d == null ? false : this.a(this.d.getItemInMainHand()) || this.a(this.d.getItemInOffHand());
            }
        }

        private boolean a(ItemStack itemstack) {
            return this.f.contains(itemstack.getItem());
        }

        @Override
        public boolean b() {
            return this.a();
        }

        @Override
        public void d() {
            this.d = null;
            this.b.getNavigation().o();
            this.e = 100;
        }

        @Override
        public void e() {
            this.b.getControllerLook().a(this.d, (float) (this.b.eo() + 20), (float) this.b.O());
            if (this.b.h((Entity) this.d) < 6.25D) {
                this.b.getNavigation().o();
            } else {
                this.b.getNavigation().a((Entity) this.d, this.c);
            }

        }
    }

    static class b extends PathfinderGoal {

        private final EntityTurtle a;
        private final double b;
        private boolean c;
        private int d;

        b(EntityTurtle entityturtle, double d0) {
            this.a = entityturtle;
            this.b = d0;
        }

        @Override
        public boolean a() {
            return this.a.isBaby() ? false : (this.a.hasEgg() ? true : (this.a.getRandom().nextInt(700) != 0 ? false : !this.a.getHomePos().a((IPosition) this.a.getPositionVector(), 64.0D)));
        }

        @Override
        public void c() {
            this.a.v(true);
            this.c = false;
            this.d = 0;
        }

        @Override
        public void d() {
            this.a.v(false);
        }

        @Override
        public boolean b() {
            return !this.a.getHomePos().a((IPosition) this.a.getPositionVector(), 7.0D) && !this.c && this.d <= 600;
        }

        @Override
        public void e() {
            BlockPosition blockposition = this.a.getHomePos();
            boolean flag = blockposition.a((IPosition) this.a.getPositionVector(), 16.0D);

            if (flag) {
                ++this.d;
            }

            if (this.a.getNavigation().m()) {
                Vec3D vec3d = Vec3D.c((BaseBlockPosition) blockposition);
                Vec3D vec3d1 = RandomPositionGenerator.a(this.a, 16, 3, vec3d, 0.3141592741012573D);

                if (vec3d1 == null) {
                    vec3d1 = RandomPositionGenerator.b(this.a, 8, 7, vec3d);
                }

                if (vec3d1 != null && !flag && !this.a.world.getType(new BlockPosition(vec3d1)).a(Blocks.WATER)) {
                    vec3d1 = RandomPositionGenerator.b(this.a, 16, 5, vec3d);
                }

                if (vec3d1 == null) {
                    this.c = true;
                    return;
                }

                this.a.getNavigation().a(vec3d1.x, vec3d1.y, vec3d1.z, this.b);
            }

        }
    }

    static class j extends PathfinderGoal {

        private final EntityTurtle a;
        private final double b;
        private boolean c;

        j(EntityTurtle entityturtle, double d0) {
            this.a = entityturtle;
            this.b = d0;
        }

        @Override
        public boolean a() {
            return !this.a.eU() && !this.a.hasEgg() && this.a.isInWater();
        }

        @Override
        public void c() {
            boolean flag = true;
            boolean flag1 = true;
            Random random = this.a.random;
            int i = random.nextInt(1025) - 512;
            int j = random.nextInt(9) - 4;
            int k = random.nextInt(1025) - 512;

            if ((double) j + this.a.locY() > (double) (this.a.world.getSeaLevel() - 1)) {
                j = 0;
            }

            BlockPosition blockposition = new BlockPosition((double) i + this.a.locX(), (double) j + this.a.locY(), (double) k + this.a.locZ());

            this.a.setTravelPos(blockposition);
            this.a.w(true);
            this.c = false;
        }

        @Override
        public void e() {
            if (this.a.getNavigation().m()) {
                Vec3D vec3d = Vec3D.c((BaseBlockPosition) this.a.getTravelPos());
                Vec3D vec3d1 = RandomPositionGenerator.a(this.a, 16, 3, vec3d, 0.3141592741012573D);

                if (vec3d1 == null) {
                    vec3d1 = RandomPositionGenerator.b(this.a, 8, 7, vec3d);
                }

                if (vec3d1 != null) {
                    int i = MathHelper.floor(vec3d1.x);
                    int j = MathHelper.floor(vec3d1.z);
                    boolean flag = true;

                    if (!this.a.world.isAreaLoaded(i - 34, 0, j - 34, i + 34, 0, j + 34)) {
                        vec3d1 = null;
                    }
                }

                if (vec3d1 == null) {
                    this.c = true;
                    return;
                }

                this.a.getNavigation().a(vec3d1.x, vec3d1.y, vec3d1.z, this.b);
            }

        }

        @Override
        public boolean b() {
            return !this.a.getNavigation().m() && !this.c && !this.a.eU() && !this.a.isInLove() && !this.a.hasEgg();
        }

        @Override
        public void d() {
            this.a.w(false);
            super.d();
        }
    }

    static class f extends PathfinderGoalPanic {

        f(EntityTurtle entityturtle, double d0) {
            super(entityturtle, d0);
        }

        @Override
        public boolean a() {
            if (this.a.getLastDamager() == null && !this.a.isBurning()) {
                return false;
            } else {
                BlockPosition blockposition = this.a(this.a.world, this.a, 7, 4);

                if (blockposition != null) {
                    this.c = (double) blockposition.getX();
                    this.d = (double) blockposition.getY();
                    this.e = (double) blockposition.getZ();
                    return true;
                } else {
                    return this.g();
                }
            }
        }
    }
}
