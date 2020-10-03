package net.minecraft.server;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityPanda extends EntityAnimal {

    private static final DataWatcherObject<Integer> bp = DataWatcher.a(EntityPanda.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> bq = DataWatcher.a(EntityPanda.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> br = DataWatcher.a(EntityPanda.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Byte> bs = DataWatcher.a(EntityPanda.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Byte> bt = DataWatcher.a(EntityPanda.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Byte> bu = DataWatcher.a(EntityPanda.class, DataWatcherRegistry.a);
    private static final PathfinderTargetCondition bv = (new PathfinderTargetCondition()).a(8.0D).b().a();
    private boolean bw;
    private boolean bx;
    public int bo;
    private Vec3D by;
    private float bz;
    private float bA;
    private float bB;
    private float bC;
    private float bD;
    private float bE;
    private EntityPanda.g bF;
    private static final Predicate<EntityItem> PICKUP_PREDICATE = (entityitem) -> {
        Item item = entityitem.getItemStack().getItem();

        return (item == Blocks.BAMBOO.getItem() || item == Blocks.CAKE.getItem()) && entityitem.isAlive() && !entityitem.p();
    };

    public EntityPanda(EntityTypes<? extends EntityPanda> entitytypes, World world) {
        super(entitytypes, world);
        this.moveController = new EntityPanda.h(this);
        if (!this.isBaby()) {
            this.setCanPickupLoot(true);
        }

    }

    @Override
    public boolean e(ItemStack itemstack) {
        EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);

        return !this.getEquipment(enumitemslot).isEmpty() ? false : enumitemslot == EnumItemSlot.MAINHAND && super.e(itemstack);
    }

    public int eK() {
        return (Integer) this.datawatcher.get(EntityPanda.bp);
    }

    public void t(int i) {
        this.datawatcher.set(EntityPanda.bp, i);
    }

    public boolean eL() {
        return this.w(2);
    }

    public boolean eM() {
        return this.w(8);
    }

    public void t(boolean flag) {
        this.d(8, flag);
    }

    public boolean eN() {
        return this.w(16);
    }

    public void u(boolean flag) {
        this.d(16, flag);
    }

    public boolean eO() {
        return (Integer) this.datawatcher.get(EntityPanda.br) > 0;
    }

    public void v(boolean flag) {
        this.datawatcher.set(EntityPanda.br, flag ? 1 : 0);
    }

    private int fk() {
        return (Integer) this.datawatcher.get(EntityPanda.br);
    }

    private void v(int i) {
        this.datawatcher.set(EntityPanda.br, i);
    }

    public void w(boolean flag) {
        this.d(2, flag);
        if (!flag) {
            this.u(0);
        }

    }

    public int eU() {
        return (Integer) this.datawatcher.get(EntityPanda.bq);
    }

    public void u(int i) {
        this.datawatcher.set(EntityPanda.bq, i);
    }

    public EntityPanda.Gene getMainGene() {
        return EntityPanda.Gene.a((Byte) this.datawatcher.get(EntityPanda.bs));
    }

    public void setMainGene(EntityPanda.Gene entitypanda_gene) {
        if (entitypanda_gene.a() > 6) {
            entitypanda_gene = EntityPanda.Gene.a(this.random);
        }

        this.datawatcher.set(EntityPanda.bs, (byte) entitypanda_gene.a());
    }

    public EntityPanda.Gene getHiddenGene() {
        return EntityPanda.Gene.a((Byte) this.datawatcher.get(EntityPanda.bt));
    }

    public void setHiddenGene(EntityPanda.Gene entitypanda_gene) {
        if (entitypanda_gene.a() > 6) {
            entitypanda_gene = EntityPanda.Gene.a(this.random);
        }

        this.datawatcher.set(EntityPanda.bt, (byte) entitypanda_gene.a());
    }

    public boolean eX() {
        return this.w(4);
    }

    public void x(boolean flag) {
        this.d(4, flag);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityPanda.bp, 0);
        this.datawatcher.register(EntityPanda.bq, 0);
        this.datawatcher.register(EntityPanda.bs, (byte) 0);
        this.datawatcher.register(EntityPanda.bt, (byte) 0);
        this.datawatcher.register(EntityPanda.bu, (byte) 0);
        this.datawatcher.register(EntityPanda.br, 0);
    }

    private boolean w(int i) {
        return ((Byte) this.datawatcher.get(EntityPanda.bu) & i) != 0;
    }

    private void d(int i, boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityPanda.bu);

        if (flag) {
            this.datawatcher.set(EntityPanda.bu, (byte) (b0 | i));
        } else {
            this.datawatcher.set(EntityPanda.bu, (byte) (b0 & ~i));
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setString("MainGene", this.getMainGene().b());
        nbttagcompound.setString("HiddenGene", this.getHiddenGene().b());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setMainGene(EntityPanda.Gene.a(nbttagcompound.getString("MainGene")));
        this.setHiddenGene(EntityPanda.Gene.a(nbttagcompound.getString("HiddenGene")));
    }

    @Nullable
    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        EntityPanda entitypanda = (EntityPanda) EntityTypes.PANDA.a((World) worldserver);

        if (entityageable instanceof EntityPanda) {
            entitypanda.a(this, (EntityPanda) entityageable);
        }

        entitypanda.fg();
        return entitypanda;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new EntityPanda.i(this, 2.0D));
        this.goalSelector.a(2, new EntityPanda.d(this, 1.0D));
        this.goalSelector.a(3, new EntityPanda.b(this, 1.2000000476837158D, true));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.0D, RecipeItemStack.a(Blocks.BAMBOO.getItem()), false));
        this.goalSelector.a(6, new EntityPanda.c<>(this, EntityHuman.class, 8.0F, 2.0D, 2.0D));
        this.goalSelector.a(6, new EntityPanda.c<>(this, EntityMonster.class, 4.0F, 2.0D, 2.0D));
        this.goalSelector.a(7, new EntityPanda.k());
        this.goalSelector.a(8, new EntityPanda.f(this));
        this.goalSelector.a(8, new EntityPanda.l(this));
        this.bF = new EntityPanda.g(this, EntityHuman.class, 6.0F);
        this.goalSelector.a(9, this.bF);
        this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(12, new EntityPanda.j(this));
        this.goalSelector.a(13, new PathfinderGoalFollowParent(this, 1.25D));
        this.goalSelector.a(14, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.targetSelector.a(1, (new EntityPanda.e(this, new Class[0])).a(new Class[0]));
    }

    public static AttributeProvider.Builder eY() {
        return EntityInsentient.p().a(GenericAttributes.MOVEMENT_SPEED, 0.15000000596046448D).a(GenericAttributes.ATTACK_DAMAGE, 6.0D);
    }

    public EntityPanda.Gene getActiveGene() {
        return EntityPanda.Gene.b(this.getMainGene(), this.getHiddenGene());
    }

    public boolean isLazy() {
        return this.getActiveGene() == EntityPanda.Gene.LAZY;
    }

    public boolean isWorried() {
        return this.getActiveGene() == EntityPanda.Gene.WORRIED;
    }

    public boolean isPlayful() {
        return this.getActiveGene() == EntityPanda.Gene.PLAYFUL;
    }

    public boolean isWeak() {
        return this.getActiveGene() == EntityPanda.Gene.WEAK;
    }

    @Override
    public boolean isAggressive() {
        return this.getActiveGene() == EntityPanda.Gene.AGGRESSIVE;
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return false;
    }

    @Override
    public boolean attackEntity(Entity entity) {
        this.playSound(SoundEffects.ENTITY_PANDA_BITE, 1.0F, 1.0F);
        if (!this.isAggressive()) {
            this.bx = true;
        }

        return super.attackEntity(entity);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isWorried()) {
            if (this.world.V() && !this.isInWater()) {
                this.t(true);
                this.v(false);
            } else if (!this.eO()) {
                this.t(false);
            }
        }

        if (this.getGoalTarget() == null) {
            this.bw = false;
            this.bx = false;
        }

        if (this.eK() > 0) {
            if (this.getGoalTarget() != null) {
                this.a((Entity) this.getGoalTarget(), 90.0F, 90.0F);
            }

            if (this.eK() == 29 || this.eK() == 14) {
                this.playSound(SoundEffects.ENTITY_PANDA_CANT_BREED, 1.0F, 1.0F);
            }

            this.t(this.eK() - 1);
        }

        if (this.eL()) {
            this.u(this.eU() + 1);
            if (this.eU() > 20) {
                this.w(false);
                this.fr();
            } else if (this.eU() == 1) {
                this.playSound(SoundEffects.ENTITY_PANDA_PRE_SNEEZE, 1.0F, 1.0F);
            }
        }

        if (this.eX()) {
            this.fq();
        } else {
            this.bo = 0;
        }

        if (this.eM()) {
            this.pitch = 0.0F;
        }

        this.fn();
        this.fl();
        this.fo();
        this.fp();
    }

    public boolean ff() {
        return this.isWorried() && this.world.V();
    }

    private void fl() {
        if (!this.eO() && this.eM() && !this.ff() && !this.getEquipment(EnumItemSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
            this.v(true);
        } else if (this.getEquipment(EnumItemSlot.MAINHAND).isEmpty() || !this.eM()) {
            this.v(false);
        }

        if (this.eO()) {
            this.fm();
            if (!this.world.isClientSide && this.fk() > 80 && this.random.nextInt(20) == 1) {
                if (this.fk() > 100 && this.l(this.getEquipment(EnumItemSlot.MAINHAND))) {
                    if (!this.world.isClientSide) {
                        this.setSlot(EnumItemSlot.MAINHAND, ItemStack.b);
                    }

                    this.t(false);
                }

                this.v(false);
                return;
            }

            this.v(this.fk() + 1);
        }

    }

    private void fm() {
        if (this.fk() % 5 == 0) {
            this.playSound(SoundEffects.ENTITY_PANDA_EAT, 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for (int i = 0; i < 6; ++i) {
                Vec3D vec3d = new Vec3D(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) this.random.nextFloat() - 0.5D) * 0.1D);

                vec3d = vec3d.a(-this.pitch * 0.017453292F);
                vec3d = vec3d.b(-this.yaw * 0.017453292F);
                double d0 = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3D vec3d1 = new Vec3D(((double) this.random.nextFloat() - 0.5D) * 0.8D, d0, 1.0D + ((double) this.random.nextFloat() - 0.5D) * 0.4D);

                vec3d1 = vec3d1.b(-this.aA * 0.017453292F);
                vec3d1 = vec3d1.add(this.locX(), this.getHeadY() + 1.0D, this.locZ());
                this.world.addParticle(new ParticleParamItem(Particles.ITEM, this.getEquipment(EnumItemSlot.MAINHAND)), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
            }
        }

    }

    private void fn() {
        this.bA = this.bz;
        if (this.eM()) {
            this.bz = Math.min(1.0F, this.bz + 0.15F);
        } else {
            this.bz = Math.max(0.0F, this.bz - 0.19F);
        }

    }

    private void fo() {
        this.bC = this.bB;
        if (this.eN()) {
            this.bB = Math.min(1.0F, this.bB + 0.15F);
        } else {
            this.bB = Math.max(0.0F, this.bB - 0.19F);
        }

    }

    private void fp() {
        this.bE = this.bD;
        if (this.eX()) {
            this.bD = Math.min(1.0F, this.bD + 0.15F);
        } else {
            this.bD = Math.max(0.0F, this.bD - 0.19F);
        }

    }

    private void fq() {
        ++this.bo;
        if (this.bo > 32) {
            this.x(false);
        } else {
            if (!this.world.isClientSide) {
                Vec3D vec3d = this.getMot();

                if (this.bo == 1) {
                    float f = this.yaw * 0.017453292F;
                    float f1 = this.isBaby() ? 0.1F : 0.2F;

                    this.by = new Vec3D(vec3d.x + (double) (-MathHelper.sin(f) * f1), 0.0D, vec3d.z + (double) (MathHelper.cos(f) * f1));
                    this.setMot(this.by.add(0.0D, 0.27D, 0.0D));
                } else if ((float) this.bo != 7.0F && (float) this.bo != 15.0F && (float) this.bo != 23.0F) {
                    this.setMot(this.by.x, vec3d.y, this.by.z);
                } else {
                    this.setMot(0.0D, this.onGround ? 0.27D : vec3d.y, 0.0D);
                }
            }

        }
    }

    private void fr() {
        Vec3D vec3d = this.getMot();

        this.world.addParticle(Particles.SNEEZE, this.locX() - (double) (this.getWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(this.aA * 0.017453292F), this.getHeadY() - 0.10000000149011612D, this.locZ() + (double) (this.getWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(this.aA * 0.017453292F), vec3d.x, 0.0D, vec3d.z);
        this.playSound(SoundEffects.ENTITY_PANDA_SNEEZE, 1.0F, 1.0F);
        List<EntityPanda> list = this.world.a(EntityPanda.class, this.getBoundingBox().g(10.0D));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityPanda entitypanda = (EntityPanda) iterator.next();

            if (!entitypanda.isBaby() && entitypanda.onGround && !entitypanda.isInWater() && entitypanda.fh()) {
                entitypanda.jump();
            }
        }

        if (!this.world.s_() && this.random.nextInt(700) == 0 && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            this.a((IMaterial) Items.SLIME_BALL);
        }

    }

    @Override
    protected void b(EntityItem entityitem) {
        if (this.getEquipment(EnumItemSlot.MAINHAND).isEmpty() && EntityPanda.PICKUP_PREDICATE.test(entityitem)) {
            this.a(entityitem);
            ItemStack itemstack = entityitem.getItemStack();

            this.setSlot(EnumItemSlot.MAINHAND, itemstack);
            this.dropChanceHand[EnumItemSlot.MAINHAND.b()] = 2.0F;
            this.receive(entityitem, itemstack.getCount());
            entityitem.die();
        }

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        this.t(false);
        return super.damageEntity(damagesource, f);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setMainGene(EntityPanda.Gene.a(this.random));
        this.setHiddenGene(EntityPanda.Gene.a(this.random));
        this.fg();
        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(0.2F);
        }

        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    public void a(EntityPanda entitypanda, @Nullable EntityPanda entitypanda1) {
        if (entitypanda1 == null) {
            if (this.random.nextBoolean()) {
                this.setMainGene(entitypanda.fs());
                this.setHiddenGene(EntityPanda.Gene.a(this.random));
            } else {
                this.setMainGene(EntityPanda.Gene.a(this.random));
                this.setHiddenGene(entitypanda.fs());
            }
        } else if (this.random.nextBoolean()) {
            this.setMainGene(entitypanda.fs());
            this.setHiddenGene(entitypanda1.fs());
        } else {
            this.setMainGene(entitypanda1.fs());
            this.setHiddenGene(entitypanda.fs());
        }

        if (this.random.nextInt(32) == 0) {
            this.setMainGene(EntityPanda.Gene.a(this.random));
        }

        if (this.random.nextInt(32) == 0) {
            this.setHiddenGene(EntityPanda.Gene.a(this.random));
        }

    }

    private EntityPanda.Gene fs() {
        return this.random.nextBoolean() ? this.getMainGene() : this.getHiddenGene();
    }

    public void fg() {
        if (this.isWeak()) {
            this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(10.0D);
        }

        if (this.isLazy()) {
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.07000000029802322D);
        }

    }

    private void ft() {
        if (!this.isInWater()) {
            this.t(0.0F);
            this.getNavigation().o();
            this.t(true);
        }

    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (this.ff()) {
            return EnumInteractionResult.PASS;
        } else if (this.eN()) {
            this.u(false);
            return EnumInteractionResult.a(this.world.isClientSide);
        } else if (this.k(itemstack)) {
            if (this.getGoalTarget() != null) {
                this.bw = true;
            }

            if (this.isBaby()) {
                this.a(entityhuman, itemstack);
                this.setAge((int) ((float) (-this.getAge() / 20) * 0.1F), true);
            } else if (!this.world.isClientSide && this.getAge() == 0 && this.eP()) {
                this.a(entityhuman, itemstack);
                this.g(entityhuman);
            } else {
                if (this.world.isClientSide || this.eM() || this.isInWater()) {
                    return EnumInteractionResult.PASS;
                }

                this.ft();
                this.v(true);
                ItemStack itemstack1 = this.getEquipment(EnumItemSlot.MAINHAND);

                if (!itemstack1.isEmpty() && !entityhuman.abilities.canInstantlyBuild) {
                    this.a(itemstack1);
                }

                this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(itemstack.getItem(), 1));
                this.a(entityhuman, itemstack);
            }

            return EnumInteractionResult.SUCCESS;
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return this.isAggressive() ? SoundEffects.ENTITY_PANDA_AGGRESSIVE_AMBIENT : (this.isWorried() ? SoundEffects.ENTITY_PANDA_WORRIED_AMBIENT : SoundEffects.ENTITY_PANDA_AMBIENT);
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_PANDA_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return itemstack.getItem() == Blocks.BAMBOO.getItem();
    }

    private boolean l(ItemStack itemstack) {
        return this.k(itemstack) || itemstack.getItem() == Blocks.CAKE.getItem();
    }

    @Nullable
    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PANDA_DEATH;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PANDA_HURT;
    }

    public boolean fh() {
        return !this.eN() && !this.ff() && !this.eO() && !this.eX() && !this.eM();
    }

    static class i extends PathfinderGoalPanic {

        private final EntityPanda g;

        public i(EntityPanda entitypanda, double d0) {
            super(entitypanda, d0);
            this.g = entitypanda;
        }

        @Override
        public boolean a() {
            if (!this.g.isBurning()) {
                return false;
            } else {
                BlockPosition blockposition = this.a(this.a.world, this.a, 5, 4);

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

        @Override
        public boolean b() {
            if (this.g.eM()) {
                this.g.getNavigation().o();
                return false;
            } else {
                return super.b();
            }
        }
    }

    static class e extends PathfinderGoalHurtByTarget {

        private final EntityPanda a;

        public e(EntityPanda entitypanda, Class<?>... aclass) {
            super(entitypanda, aclass);
            this.a = entitypanda;
        }

        @Override
        public boolean b() {
            if (!this.a.bw && !this.a.bx) {
                return super.b();
            } else {
                this.a.setGoalTarget((EntityLiving) null);
                return false;
            }
        }

        @Override
        protected void a(EntityInsentient entityinsentient, EntityLiving entityliving) {
            if (entityinsentient instanceof EntityPanda && ((EntityPanda) entityinsentient).isAggressive()) {
                entityinsentient.setGoalTarget(entityliving);
            }

        }
    }

    static class f extends PathfinderGoal {

        private final EntityPanda a;
        private int b;

        public f(EntityPanda entitypanda) {
            this.a = entitypanda;
        }

        @Override
        public boolean a() {
            return this.b < this.a.ticksLived && this.a.isLazy() && this.a.fh() && this.a.random.nextInt(400) == 1;
        }

        @Override
        public boolean b() {
            return !this.a.isInWater() && (this.a.isLazy() || this.a.random.nextInt(600) != 1) ? this.a.random.nextInt(2000) != 1 : false;
        }

        @Override
        public void c() {
            this.a.u(true);
            this.b = 0;
        }

        @Override
        public void d() {
            this.a.u(false);
            this.b = this.a.ticksLived + 200;
        }
    }

    class k extends PathfinderGoal {

        private int b;

        public k() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            if (this.b <= EntityPanda.this.ticksLived && !EntityPanda.this.isBaby() && !EntityPanda.this.isInWater() && EntityPanda.this.fh() && EntityPanda.this.eK() <= 0) {
                List<EntityItem> list = EntityPanda.this.world.a(EntityItem.class, EntityPanda.this.getBoundingBox().grow(6.0D, 6.0D, 6.0D), EntityPanda.PICKUP_PREDICATE);

                return !list.isEmpty() || !EntityPanda.this.getEquipment(EnumItemSlot.MAINHAND).isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public boolean b() {
            return !EntityPanda.this.isInWater() && (EntityPanda.this.isLazy() || EntityPanda.this.random.nextInt(600) != 1) ? EntityPanda.this.random.nextInt(2000) != 1 : false;
        }

        @Override
        public void e() {
            if (!EntityPanda.this.eM() && !EntityPanda.this.getEquipment(EnumItemSlot.MAINHAND).isEmpty()) {
                EntityPanda.this.ft();
            }

        }

        @Override
        public void c() {
            List<EntityItem> list = EntityPanda.this.world.a(EntityItem.class, EntityPanda.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityPanda.PICKUP_PREDICATE);

            if (!list.isEmpty() && EntityPanda.this.getEquipment(EnumItemSlot.MAINHAND).isEmpty()) {
                EntityPanda.this.getNavigation().a((Entity) list.get(0), 1.2000000476837158D);
            } else if (!EntityPanda.this.getEquipment(EnumItemSlot.MAINHAND).isEmpty()) {
                EntityPanda.this.ft();
            }

            this.b = 0;
        }

        @Override
        public void d() {
            ItemStack itemstack = EntityPanda.this.getEquipment(EnumItemSlot.MAINHAND);

            if (!itemstack.isEmpty()) {
                EntityPanda.this.a(itemstack);
                EntityPanda.this.setSlot(EnumItemSlot.MAINHAND, ItemStack.b);
                int i = EntityPanda.this.isLazy() ? EntityPanda.this.random.nextInt(50) + 10 : EntityPanda.this.random.nextInt(150) + 10;

                this.b = EntityPanda.this.ticksLived + i * 20;
            }

            EntityPanda.this.t(false);
        }
    }

    static class c<T extends EntityLiving> extends PathfinderGoalAvoidTarget<T> {

        private final EntityPanda i;

        public c(EntityPanda entitypanda, Class<T> oclass, float f, double d0, double d1) {
            Predicate predicate = IEntitySelector.g;

            super(entitypanda, oclass, f, d0, d1, predicate::test);
            this.i = entitypanda;
        }

        @Override
        public boolean a() {
            return this.i.isWorried() && this.i.fh() && super.a();
        }
    }

    class d extends PathfinderGoalBreed {

        private final EntityPanda e;
        private int f;

        public d(EntityPanda entitypanda, double d0) {
            super(entitypanda, d0);
            this.e = entitypanda;
        }

        @Override
        public boolean a() {
            if (super.a() && this.e.eK() == 0) {
                if (!this.h()) {
                    if (this.f <= this.e.ticksLived) {
                        this.e.t(32);
                        this.f = this.e.ticksLived + 600;
                        if (this.e.doAITick()) {
                            EntityHuman entityhuman = this.b.a(EntityPanda.bv, (EntityLiving) this.e);

                            this.e.bF.a((EntityLiving) entityhuman);
                        }
                    }

                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        private boolean h() {
            BlockPosition blockposition = this.e.getChunkCoordinates();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 8; ++j) {
                    for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, k, i, l);
                            if (this.b.getType(blockposition_mutableblockposition).a(Blocks.BAMBOO)) {
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    static class l extends PathfinderGoal {

        private final EntityPanda a;

        public l(EntityPanda entitypanda) {
            this.a = entitypanda;
        }

        @Override
        public boolean a() {
            return this.a.isBaby() && this.a.fh() ? (this.a.isWeak() && this.a.random.nextInt(500) == 1 ? true : this.a.random.nextInt(6000) == 1) : false;
        }

        @Override
        public boolean b() {
            return false;
        }

        @Override
        public void c() {
            this.a.w(true);
        }
    }

    static class j extends PathfinderGoal {

        private final EntityPanda a;

        public j(EntityPanda entitypanda) {
            this.a = entitypanda;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK, PathfinderGoal.Type.JUMP));
        }

        @Override
        public boolean a() {
            if ((this.a.isBaby() || this.a.isPlayful()) && this.a.onGround) {
                if (!this.a.fh()) {
                    return false;
                } else {
                    float f = this.a.yaw * 0.017453292F;
                    int i = 0;
                    int j = 0;
                    float f1 = -MathHelper.sin(f);
                    float f2 = MathHelper.cos(f);

                    if ((double) Math.abs(f1) > 0.5D) {
                        i = (int) ((float) i + f1 / Math.abs(f1));
                    }

                    if ((double) Math.abs(f2) > 0.5D) {
                        j = (int) ((float) j + f2 / Math.abs(f2));
                    }

                    return this.a.world.getType(this.a.getChunkCoordinates().b(i, -1, j)).isAir() ? true : (this.a.isPlayful() && this.a.random.nextInt(60) == 1 ? true : this.a.random.nextInt(500) == 1);
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean b() {
            return false;
        }

        @Override
        public void c() {
            this.a.x(true);
        }

        @Override
        public boolean C_() {
            return false;
        }
    }

    static class g extends PathfinderGoalLookAtPlayer {

        private final EntityPanda g;

        public g(EntityPanda entitypanda, Class<? extends EntityLiving> oclass, float f) {
            super(entitypanda, oclass, f);
            this.g = entitypanda;
        }

        public void a(EntityLiving entityliving) {
            this.b = entityliving;
        }

        @Override
        public boolean b() {
            return this.b != null && super.b();
        }

        @Override
        public boolean a() {
            if (this.a.getRandom().nextFloat() >= this.d) {
                return false;
            } else {
                if (this.b == null) {
                    if (this.e == EntityHuman.class) {
                        this.b = this.a.world.a(this.f, this.a, this.a.locX(), this.a.getHeadY(), this.a.locZ());
                    } else {
                        this.b = this.a.world.b(this.e, this.f, this.a, this.a.locX(), this.a.getHeadY(), this.a.locZ(), this.a.getBoundingBox().grow((double) this.c, 3.0D, (double) this.c));
                    }
                }

                return this.g.fh() && this.b != null;
            }
        }

        @Override
        public void e() {
            if (this.b != null) {
                super.e();
            }

        }
    }

    static class b extends PathfinderGoalMeleeAttack {

        private final EntityPanda b;

        public b(EntityPanda entitypanda, double d0, boolean flag) {
            super(entitypanda, d0, flag);
            this.b = entitypanda;
        }

        @Override
        public boolean a() {
            return this.b.fh() && super.a();
        }
    }

    static class h extends ControllerMove {

        private final EntityPanda i;

        public h(EntityPanda entitypanda) {
            super(entitypanda);
            this.i = entitypanda;
        }

        @Override
        public void a() {
            if (this.i.fh()) {
                super.a();
            }
        }
    }

    public static enum Gene {

        NORMAL(0, "normal", false), LAZY(1, "lazy", false), WORRIED(2, "worried", false), PLAYFUL(3, "playful", false), BROWN(4, "brown", true), WEAK(5, "weak", true), AGGRESSIVE(6, "aggressive", false);

        private static final EntityPanda.Gene[] h = (EntityPanda.Gene[]) Arrays.stream(values()).sorted(Comparator.comparingInt(EntityPanda.Gene::a)).toArray((i) -> {
            return new EntityPanda.Gene[i];
        });
        private final int i;
        private final String j;
        private final boolean k;

        private Gene(int i, String s, boolean flag) {
            this.i = i;
            this.j = s;
            this.k = flag;
        }

        public int a() {
            return this.i;
        }

        public String b() {
            return this.j;
        }

        public boolean isRecessive() {
            return this.k;
        }

        private static EntityPanda.Gene b(EntityPanda.Gene entitypanda_gene, EntityPanda.Gene entitypanda_gene1) {
            return entitypanda_gene.isRecessive() ? (entitypanda_gene == entitypanda_gene1 ? entitypanda_gene : EntityPanda.Gene.NORMAL) : entitypanda_gene;
        }

        public static EntityPanda.Gene a(int i) {
            if (i < 0 || i >= EntityPanda.Gene.h.length) {
                i = 0;
            }

            return EntityPanda.Gene.h[i];
        }

        public static EntityPanda.Gene a(String s) {
            EntityPanda.Gene[] aentitypanda_gene = values();
            int i = aentitypanda_gene.length;

            for (int j = 0; j < i; ++j) {
                EntityPanda.Gene entitypanda_gene = aentitypanda_gene[j];

                if (entitypanda_gene.j.equals(s)) {
                    return entitypanda_gene;
                }
            }

            return EntityPanda.Gene.NORMAL;
        }

        public static EntityPanda.Gene a(Random random) {
            int i = random.nextInt(16);

            return i == 0 ? EntityPanda.Gene.LAZY : (i == 1 ? EntityPanda.Gene.WORRIED : (i == 2 ? EntityPanda.Gene.PLAYFUL : (i == 4 ? EntityPanda.Gene.AGGRESSIVE : (i < 9 ? EntityPanda.Gene.WEAK : (i < 11 ? EntityPanda.Gene.BROWN : EntityPanda.Gene.NORMAL)))));
        }
    }
}
