package net.minecraft.server;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;

public class EntityDrowned extends EntityZombie implements IRangedEntity {

    private boolean d;
    protected final NavigationGuardian navigationWater;
    protected final Navigation navigationLand;

    public EntityDrowned(EntityTypes<? extends EntityDrowned> entitytypes, World world) {
        super(entitytypes, world);
        this.G = 1.0F;
        this.moveController = new EntityDrowned.d(this);
        this.a(PathType.WATER, 0.0F);
        this.navigationWater = new NavigationGuardian(this, world);
        this.navigationLand = new Navigation(this, world);
    }

    @Override
    protected void m() {
        this.goalSelector.a(1, new EntityDrowned.c(this, 1.0D));
        this.goalSelector.a(2, new EntityDrowned.f(this, 1.0D, 40, 10.0F));
        this.goalSelector.a(2, new EntityDrowned.a(this, 1.0D, false));
        this.goalSelector.a(5, new EntityDrowned.b(this, 1.0D));
        this.goalSelector.a(6, new EntityDrowned.e(this, 1.0D, this.world.getSeaLevel()));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityDrowned.class})).a(EntityPigZombie.class));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 10, true, false, this::i));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, false));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
        this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bo));
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        groupdataentity = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
        if (this.getEquipment(EnumItemSlot.OFFHAND).isEmpty() && this.random.nextFloat() < 0.03F) {
            this.setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.NAUTILUS_SHELL));
            this.dropChanceHand[EnumItemSlot.OFFHAND.b()] = 2.0F;
        }

        return groupdataentity;
    }

    public static boolean a(EntityTypes<EntityDrowned> entitytypes, WorldAccess worldaccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        Optional<ResourceKey<BiomeBase>> optional = worldaccess.i(blockposition);
        boolean flag = worldaccess.getDifficulty() != EnumDifficulty.PEACEFUL && a(worldaccess, blockposition, random) && (enummobspawn == EnumMobSpawn.SPAWNER || worldaccess.getFluid(blockposition).a((Tag) TagsFluid.WATER));

        return !Objects.equals(optional, Optional.of(Biomes.RIVER)) && !Objects.equals(optional, Optional.of(Biomes.FROZEN_RIVER)) ? random.nextInt(40) == 0 && a((GeneratorAccess) worldaccess, blockposition) && flag : random.nextInt(15) == 0 && flag;
    }

    private static boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        return blockposition.getY() < generatoraccess.getSeaLevel() - 5;
    }

    @Override
    protected boolean eK() {
        return false;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return this.isInWater() ? SoundEffects.ENTITY_DROWNED_AMBIENT_WATER : SoundEffects.ENTITY_DROWNED_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return this.isInWater() ? SoundEffects.ENTITY_DROWNED_HURT_WATER : SoundEffects.ENTITY_DROWNED_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return this.isInWater() ? SoundEffects.ENTITY_DROWNED_DEATH_WATER : SoundEffects.ENTITY_DROWNED_DEATH;
    }

    @Override
    protected SoundEffect getSoundStep() {
        return SoundEffects.ENTITY_DROWNED_STEP;
    }

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_DROWNED_SWIM;
    }

    @Override
    protected ItemStack eM() {
        return ItemStack.b;
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        if ((double) this.random.nextFloat() > 0.9D) {
            int i = this.random.nextInt(16);

            if (i < 10) {
                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else {
                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
        }

    }

    @Override
    protected boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.getItem() == Items.NAUTILUS_SHELL ? false : (itemstack1.getItem() == Items.TRIDENT ? (itemstack.getItem() == Items.TRIDENT ? itemstack.getDamage() < itemstack1.getDamage() : false) : (itemstack.getItem() == Items.TRIDENT ? true : super.a(itemstack, itemstack1)));
    }

    @Override
    protected boolean eN() {
        return false;
    }

    @Override
    public boolean a(IWorldReader iworldreader) {
        return iworldreader.j((Entity) this);
    }

    public boolean i(@Nullable EntityLiving entityliving) {
        return entityliving != null ? !this.world.isDay() || entityliving.isInWater() : false;
    }

    @Override
    public boolean bU() {
        return !this.isSwimming();
    }

    private boolean eW() {
        if (this.d) {
            return true;
        } else {
            EntityLiving entityliving = this.getGoalTarget();

            return entityliving != null && entityliving.isInWater();
        }
    }

    @Override
    public void g(Vec3D vec3d) {
        if (this.doAITick() && this.isInWater() && this.eW()) {
            this.a(0.01F, vec3d);
            this.move(EnumMoveType.SELF, this.getMot());
            this.setMot(this.getMot().a(0.9D));
        } else {
            super.g(vec3d);
        }

    }

    @Override
    public void aI() {
        if (!this.world.isClientSide) {
            if (this.doAITick() && this.isInWater() && this.eW()) {
                this.navigation = this.navigationWater;
                this.setSwimming(true);
            } else {
                this.navigation = this.navigationLand;
                this.setSwimming(false);
            }
        }

    }

    protected boolean eO() {
        PathEntity pathentity = this.getNavigation().k();

        if (pathentity != null) {
            BlockPosition blockposition = pathentity.m();

            if (blockposition != null) {
                double d0 = this.h((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());

                if (d0 < 4.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        EntityThrownTrident entitythrowntrident = new EntityThrownTrident(this.world, this, new ItemStack(Items.TRIDENT));
        double d0 = entityliving.locX() - this.locX();
        double d1 = entityliving.e(0.3333333333333333D) - entitythrowntrident.locY();
        double d2 = entityliving.locZ() - this.locZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        entitythrowntrident.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().a() * 4));
        this.playSound(SoundEffects.ENTITY_DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(entitythrowntrident);
    }

    public void t(boolean flag) {
        this.d = flag;
    }

    static class d extends ControllerMove {

        private final EntityDrowned i;

        public d(EntityDrowned entitydrowned) {
            super(entitydrowned);
            this.i = entitydrowned;
        }

        @Override
        public void a() {
            EntityLiving entityliving = this.i.getGoalTarget();

            if (this.i.eW() && this.i.isInWater()) {
                if (entityliving != null && entityliving.locY() > this.i.locY() || this.i.d) {
                    this.i.setMot(this.i.getMot().add(0.0D, 0.002D, 0.0D));
                }

                if (this.h != ControllerMove.Operation.MOVE_TO || this.i.getNavigation().m()) {
                    this.i.q(0.0F);
                    return;
                }

                double d0 = this.b - this.i.locX();
                double d1 = this.c - this.i.locY();
                double d2 = this.d - this.i.locZ();
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                d1 /= d3;
                float f = (float) (MathHelper.d(d2, d0) * 57.2957763671875D) - 90.0F;

                this.i.yaw = this.a(this.i.yaw, f, 90.0F);
                this.i.aA = this.i.yaw;
                float f1 = (float) (this.e * this.i.b(GenericAttributes.MOVEMENT_SPEED));
                float f2 = MathHelper.g(0.125F, this.i.dM(), f1);

                this.i.q(f2);
                this.i.setMot(this.i.getMot().add((double) f2 * d0 * 0.005D, (double) f2 * d1 * 0.1D, (double) f2 * d2 * 0.005D));
            } else {
                if (!this.i.onGround) {
                    this.i.setMot(this.i.getMot().add(0.0D, -0.008D, 0.0D));
                }

                super.a();
            }

        }
    }

    static class a extends PathfinderGoalZombieAttack {

        private final EntityDrowned b;

        public a(EntityDrowned entitydrowned, double d0, boolean flag) {
            super((EntityZombie) entitydrowned, d0, flag);
            this.b = entitydrowned;
        }

        @Override
        public boolean a() {
            return super.a() && this.b.i(this.b.getGoalTarget());
        }

        @Override
        public boolean b() {
            return super.b() && this.b.i(this.b.getGoalTarget());
        }
    }

    static class c extends PathfinderGoal {

        private final EntityCreature a;
        private double b;
        private double c;
        private double d;
        private final double e;
        private final World f;

        public c(EntityCreature entitycreature, double d0) {
            this.a = entitycreature;
            this.e = d0;
            this.f = entitycreature.world;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            if (!this.f.isDay()) {
                return false;
            } else if (this.a.isInWater()) {
                return false;
            } else {
                Vec3D vec3d = this.g();

                if (vec3d == null) {
                    return false;
                } else {
                    this.b = vec3d.x;
                    this.c = vec3d.y;
                    this.d = vec3d.z;
                    return true;
                }
            }
        }

        @Override
        public boolean b() {
            return !this.a.getNavigation().m();
        }

        @Override
        public void c() {
            this.a.getNavigation().a(this.b, this.c, this.d, this.e);
        }

        @Nullable
        private Vec3D g() {
            Random random = this.a.getRandom();
            BlockPosition blockposition = this.a.getChunkCoordinates();

            for (int i = 0; i < 10; ++i) {
                BlockPosition blockposition1 = blockposition.b(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);

                if (this.f.getType(blockposition1).a(Blocks.WATER)) {
                    return Vec3D.c((BaseBlockPosition) blockposition1);
                }
            }

            return null;
        }
    }

    static class b extends PathfinderGoalGotoTarget {

        private final EntityDrowned g;

        public b(EntityDrowned entitydrowned, double d0) {
            super(entitydrowned, d0, 8, 2);
            this.g = entitydrowned;
        }

        @Override
        public boolean a() {
            return super.a() && !this.g.world.isDay() && this.g.isInWater() && this.g.locY() >= (double) (this.g.world.getSeaLevel() - 3);
        }

        @Override
        public boolean b() {
            return super.b();
        }

        @Override
        protected boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
            BlockPosition blockposition1 = blockposition.up();

            return iworldreader.isEmpty(blockposition1) && iworldreader.isEmpty(blockposition1.up()) ? iworldreader.getType(blockposition).a((IBlockAccess) iworldreader, blockposition, (Entity) this.g) : false;
        }

        @Override
        public void c() {
            this.g.t(false);
            this.g.navigation = this.g.navigationLand;
            super.c();
        }

        @Override
        public void d() {
            super.d();
        }
    }

    static class e extends PathfinderGoal {

        private final EntityDrowned a;
        private final double b;
        private final int c;
        private boolean d;

        public e(EntityDrowned entitydrowned, double d0, int i) {
            this.a = entitydrowned;
            this.b = d0;
            this.c = i;
        }

        @Override
        public boolean a() {
            return !this.a.world.isDay() && this.a.isInWater() && this.a.locY() < (double) (this.c - 2);
        }

        @Override
        public boolean b() {
            return this.a() && !this.d;
        }

        @Override
        public void e() {
            if (this.a.locY() < (double) (this.c - 1) && (this.a.getNavigation().m() || this.a.eO())) {
                Vec3D vec3d = RandomPositionGenerator.b(this.a, 4, 8, new Vec3D(this.a.locX(), (double) (this.c - 1), this.a.locZ()));

                if (vec3d == null) {
                    this.d = true;
                    return;
                }

                this.a.getNavigation().a(vec3d.x, vec3d.y, vec3d.z, this.b);
            }

        }

        @Override
        public void c() {
            this.a.t(true);
            this.d = false;
        }

        @Override
        public void d() {
            this.a.t(false);
        }
    }

    static class f extends PathfinderGoalArrowAttack {

        private final EntityDrowned a;

        public f(IRangedEntity irangedentity, double d0, int i, float f) {
            super(irangedentity, d0, i, f);
            this.a = (EntityDrowned) irangedentity;
        }

        @Override
        public boolean a() {
            return super.a() && this.a.getItemInMainHand().getItem() == Items.TRIDENT;
        }

        @Override
        public void c() {
            super.c();
            this.a.setAggressive(true);
            this.a.c(EnumHand.MAIN_HAND);
        }

        @Override
        public void d() {
            super.d();
            this.a.clearActiveItem();
            this.a.setAggressive(false);
        }
    }
}
