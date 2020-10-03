package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityCat extends EntityTameableAnimal {

    private static final RecipeItemStack br = RecipeItemStack.a(Items.COD, Items.SALMON);
    private static final DataWatcherObject<Integer> bs = DataWatcher.a(EntityCat.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> bt = DataWatcher.a(EntityCat.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> bu = DataWatcher.a(EntityCat.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Integer> bv = DataWatcher.a(EntityCat.class, DataWatcherRegistry.b);
    public static final Map<Integer, MinecraftKey> bq = (Map) SystemUtils.a((Object) Maps.newHashMap(), (hashmap) -> {
        hashmap.put(0, new MinecraftKey("textures/entity/cat/tabby.png"));
        hashmap.put(1, new MinecraftKey("textures/entity/cat/black.png"));
        hashmap.put(2, new MinecraftKey("textures/entity/cat/red.png"));
        hashmap.put(3, new MinecraftKey("textures/entity/cat/siamese.png"));
        hashmap.put(4, new MinecraftKey("textures/entity/cat/british_shorthair.png"));
        hashmap.put(5, new MinecraftKey("textures/entity/cat/calico.png"));
        hashmap.put(6, new MinecraftKey("textures/entity/cat/persian.png"));
        hashmap.put(7, new MinecraftKey("textures/entity/cat/ragdoll.png"));
        hashmap.put(8, new MinecraftKey("textures/entity/cat/white.png"));
        hashmap.put(9, new MinecraftKey("textures/entity/cat/jellie.png"));
        hashmap.put(10, new MinecraftKey("textures/entity/cat/all_black.png"));
    });
    private EntityCat.a<EntityHuman> bw;
    private PathfinderGoalTempt bx;
    private float by;
    private float bz;
    private float bA;
    private float bB;
    private float bC;
    private float bD;

    public EntityCat(EntityTypes<? extends EntityCat> entitytypes, World world) {
        super(entitytypes, world);
    }

    public MinecraftKey eU() {
        return (MinecraftKey) EntityCat.bq.getOrDefault(this.getCatType(), EntityCat.bq.get(0));
    }

    @Override
    protected void initPathfinder() {
        this.bx = new EntityCat.PathfinderGoalTemptChance(this, 0.6D, EntityCat.br, true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalSit(this));
        this.goalSelector.a(2, new EntityCat.b(this));
        this.goalSelector.a(3, this.bx);
        this.goalSelector.a(5, new PathfinderGoalCatSitOnBed(this, 1.1D, 8));
        this.goalSelector.a(6, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.a(7, new PathfinderGoalJumpOnBlock(this, 0.8D));
        this.goalSelector.a(8, new PathfinderGoalLeapAtTarget(this, 0.3F));
        this.goalSelector.a(9, new PathfinderGoalOcelotAttack(this));
        this.goalSelector.a(10, new PathfinderGoalBreed(this, 0.8D));
        this.goalSelector.a(11, new PathfinderGoalRandomStrollLand(this, 0.8D, 1.0000001E-5F));
        this.goalSelector.a(12, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
        this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed<>(this, EntityRabbit.class, false, (Predicate) null));
        this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed<>(this, EntityTurtle.class, false, EntityTurtle.bo));
    }

    public int getCatType() {
        return (Integer) this.datawatcher.get(EntityCat.bs);
    }

    public void setCatType(int i) {
        if (i < 0 || i >= 11) {
            i = this.random.nextInt(10);
        }

        this.datawatcher.set(EntityCat.bs, i);
    }

    public void x(boolean flag) {
        this.datawatcher.set(EntityCat.bt, flag);
    }

    public boolean eW() {
        return (Boolean) this.datawatcher.get(EntityCat.bt);
    }

    public void y(boolean flag) {
        this.datawatcher.set(EntityCat.bu, flag);
    }

    public boolean eX() {
        return (Boolean) this.datawatcher.get(EntityCat.bu);
    }

    public EnumColor getCollarColor() {
        return EnumColor.fromColorIndex((Integer) this.datawatcher.get(EntityCat.bv));
    }

    public void setCollarColor(EnumColor enumcolor) {
        this.datawatcher.set(EntityCat.bv, enumcolor.getColorIndex());
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityCat.bs, 1);
        this.datawatcher.register(EntityCat.bt, false);
        this.datawatcher.register(EntityCat.bu, false);
        this.datawatcher.register(EntityCat.bv, EnumColor.RED.getColorIndex());
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("CatType", this.getCatType());
        nbttagcompound.setByte("CollarColor", (byte) this.getCollarColor().getColorIndex());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setCatType(nbttagcompound.getInt("CatType"));
        if (nbttagcompound.hasKeyOfType("CollarColor", 99)) {
            this.setCollarColor(EnumColor.fromColorIndex(nbttagcompound.getInt("CollarColor")));
        }

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

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return this.isTamed() ? (this.isInLove() ? SoundEffects.ENTITY_CAT_PURR : (this.random.nextInt(4) == 0 ? SoundEffects.ENTITY_CAT_PURREOW : SoundEffects.ENTITY_CAT_AMBIENT)) : SoundEffects.ENTITY_CAT_STRAY_AMBIENT;
    }

    @Override
    public int D() {
        return 120;
    }

    public void eZ() {
        this.playSound(SoundEffects.ENTITY_CAT_HISS, this.getSoundVolume(), this.dG());
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_CAT_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_CAT_DEATH;
    }

    public static AttributeProvider.Builder fa() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.30000001192092896D).a(GenericAttributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    public boolean b(float f, float f1) {
        return false;
    }

    @Override
    protected void a(EntityHuman entityhuman, ItemStack itemstack) {
        if (this.k(itemstack)) {
            this.playSound(SoundEffects.ENTITY_CAT_EAT, 1.0F, 1.0F);
        }

        super.a(entityhuman, itemstack);
    }

    private float fb() {
        return (float) this.b(GenericAttributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        return entity.damageEntity(DamageSource.mobAttack(this), this.fb());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.bx != null && this.bx.h() && !this.isTamed() && this.ticksLived % 100 == 0) {
            this.playSound(SoundEffects.ENTITY_CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }

        this.fc();
    }

    private void fc() {
        if ((this.eW() || this.eX()) && this.ticksLived % 5 == 0) {
            this.playSound(SoundEffects.ENTITY_CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
        }

        this.fd();
        this.fe();
    }

    private void fd() {
        this.bz = this.by;
        this.bB = this.bA;
        if (this.eW()) {
            this.by = Math.min(1.0F, this.by + 0.15F);
            this.bA = Math.min(1.0F, this.bA + 0.08F);
        } else {
            this.by = Math.max(0.0F, this.by - 0.22F);
            this.bA = Math.max(0.0F, this.bA - 0.13F);
        }

    }

    private void fe() {
        this.bD = this.bC;
        if (this.eX()) {
            this.bC = Math.min(1.0F, this.bC + 0.1F);
        } else {
            this.bC = Math.max(0.0F, this.bC - 0.13F);
        }

    }

    @Override
    public EntityCat createChild(WorldServer worldserver, EntityAgeable entityageable) {
        EntityCat entitycat = (EntityCat) EntityTypes.CAT.a((World) worldserver);

        if (entityageable instanceof EntityCat) {
            if (this.random.nextBoolean()) {
                entitycat.setCatType(this.getCatType());
            } else {
                entitycat.setCatType(((EntityCat) entityageable).getCatType());
            }

            if (this.isTamed()) {
                entitycat.setOwnerUUID(this.getOwnerUUID());
                entitycat.setTamed(true);
                if (this.random.nextBoolean()) {
                    entitycat.setCollarColor(this.getCollarColor());
                } else {
                    entitycat.setCollarColor(((EntityCat) entityageable).getCollarColor());
                }
            }
        }

        return entitycat;
    }

    @Override
    public boolean mate(EntityAnimal entityanimal) {
        if (!this.isTamed()) {
            return false;
        } else if (!(entityanimal instanceof EntityCat)) {
            return false;
        } else {
            EntityCat entitycat = (EntityCat) entityanimal;

            return entitycat.isTamed() && super.mate(entityanimal);
        }
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        groupdataentity = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
        if (worldaccess.ae() > 0.9F) {
            this.setCatType(this.random.nextInt(11));
        } else {
            this.setCatType(this.random.nextInt(10));
        }

        WorldServer worldserver = worldaccess.getMinecraftWorld();

        if (worldserver instanceof WorldServer && ((WorldServer) worldserver).getStructureManager().a(this.getChunkCoordinates(), true, StructureGenerator.SWAMP_HUT).e()) {
            this.setCatType(10);
            this.setPersistent();
        }

        return groupdataentity;
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        Item item = itemstack.getItem();

        if (this.world.isClientSide) {
            return this.isTamed() && this.i((EntityLiving) entityhuman) ? EnumInteractionResult.SUCCESS : (this.k(itemstack) && (this.getHealth() < this.getMaxHealth() || !this.isTamed()) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.PASS);
        } else {
            EnumInteractionResult enuminteractionresult;

            if (this.isTamed()) {
                if (this.i((EntityLiving) entityhuman)) {
                    if (!(item instanceof ItemDye)) {
                        if (item.isFood() && this.k(itemstack) && this.getHealth() < this.getMaxHealth()) {
                            this.a(entityhuman, itemstack);
                            this.heal((float) item.getFoodInfo().getNutrition());
                            return EnumInteractionResult.CONSUME;
                        }

                        enuminteractionresult = super.b(entityhuman, enumhand);
                        if (!enuminteractionresult.a() || this.isBaby()) {
                            this.setWillSit(!this.isWillSit());
                        }

                        return enuminteractionresult;
                    }

                    EnumColor enumcolor = ((ItemDye) item).d();

                    if (enumcolor != this.getCollarColor()) {
                        this.setCollarColor(enumcolor);
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            itemstack.subtract(1);
                        }

                        this.setPersistent();
                        return EnumInteractionResult.CONSUME;
                    }
                }
            } else if (this.k(itemstack)) {
                this.a(entityhuman, itemstack);
                if (this.random.nextInt(3) == 0) {
                    this.tame(entityhuman);
                    this.setWillSit(true);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }

                this.setPersistent();
                return EnumInteractionResult.CONSUME;
            }

            enuminteractionresult = super.b(entityhuman, enumhand);
            if (enuminteractionresult.a()) {
                this.setPersistent();
            }

            return enuminteractionresult;
        }
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return EntityCat.br.test(itemstack);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.5F;
    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return !this.isTamed() && this.ticksLived > 2400;
    }

    @Override
    protected void eL() {
        if (this.bw == null) {
            this.bw = new EntityCat.a<>(this, EntityHuman.class, 16.0F, 0.8D, 1.33D);
        }

        this.goalSelector.a((PathfinderGoal) this.bw);
        if (!this.isTamed()) {
            this.goalSelector.a(4, this.bw);
        }

    }

    static class b extends PathfinderGoal {

        private final EntityCat a;
        private EntityHuman b;
        private BlockPosition c;
        private int d;

        public b(EntityCat entitycat) {
            this.a = entitycat;
        }

        @Override
        public boolean a() {
            if (!this.a.isTamed()) {
                return false;
            } else if (this.a.isWillSit()) {
                return false;
            } else {
                EntityLiving entityliving = this.a.getOwner();

                if (entityliving instanceof EntityHuman) {
                    this.b = (EntityHuman) entityliving;
                    if (!entityliving.isSleeping()) {
                        return false;
                    }

                    if (this.a.h((Entity) this.b) > 100.0D) {
                        return false;
                    }

                    BlockPosition blockposition = this.b.getChunkCoordinates();
                    IBlockData iblockdata = this.a.world.getType(blockposition);

                    if (iblockdata.getBlock().a((Tag) TagsBlock.BEDS)) {
                        this.c = (BlockPosition) iblockdata.d(BlockBed.FACING).map((enumdirection) -> {
                            return blockposition.shift(enumdirection.opposite());
                        }).orElseGet(() -> {
                            return new BlockPosition(blockposition);
                        });
                        return !this.g();
                    }
                }

                return false;
            }
        }

        private boolean g() {
            List<EntityCat> list = this.a.world.a(EntityCat.class, (new AxisAlignedBB(this.c)).g(2.0D));
            Iterator iterator = list.iterator();

            EntityCat entitycat;

            do {
                do {
                    if (!iterator.hasNext()) {
                        return false;
                    }

                    entitycat = (EntityCat) iterator.next();
                } while (entitycat == this.a);
            } while (!entitycat.eW() && !entitycat.eX());

            return true;
        }

        @Override
        public boolean b() {
            return this.a.isTamed() && !this.a.isWillSit() && this.b != null && this.b.isSleeping() && this.c != null && !this.g();
        }

        @Override
        public void c() {
            if (this.c != null) {
                this.a.setSitting(false);
                this.a.getNavigation().a((double) this.c.getX(), (double) this.c.getY(), (double) this.c.getZ(), 1.100000023841858D);
            }

        }

        @Override
        public void d() {
            this.a.x(false);
            float f = this.a.world.f(1.0F);

            if (this.b.eB() >= 100 && (double) f > 0.77D && (double) f < 0.8D && (double) this.a.world.getRandom().nextFloat() < 0.7D) {
                this.h();
            }

            this.d = 0;
            this.a.y(false);
            this.a.getNavigation().o();
        }

        private void h() {
            Random random = this.a.getRandom();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

            blockposition_mutableblockposition.g(this.a.getChunkCoordinates());
            this.a.a((double) (blockposition_mutableblockposition.getX() + random.nextInt(11) - 5), (double) (blockposition_mutableblockposition.getY() + random.nextInt(5) - 2), (double) (blockposition_mutableblockposition.getZ() + random.nextInt(11) - 5), false);
            blockposition_mutableblockposition.g(this.a.getChunkCoordinates());
            LootTable loottable = this.a.world.getMinecraftServer().getLootTableRegistry().getLootTable(LootTables.ak);
            LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.a.world)).set(LootContextParameters.ORIGIN, this.a.getPositionVector()).set(LootContextParameters.THIS_ENTITY, this.a).a(random);
            List<ItemStack> list = loottable.populateLoot(loottableinfo_builder.build(LootContextParameterSets.GIFT));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                this.a.world.addEntity(new EntityItem(this.a.world, (double) blockposition_mutableblockposition.getX() - (double) MathHelper.sin(this.a.aA * 0.017453292F), (double) blockposition_mutableblockposition.getY(), (double) blockposition_mutableblockposition.getZ() + (double) MathHelper.cos(this.a.aA * 0.017453292F), itemstack));
            }

        }

        @Override
        public void e() {
            if (this.b != null && this.c != null) {
                this.a.setSitting(false);
                this.a.getNavigation().a((double) this.c.getX(), (double) this.c.getY(), (double) this.c.getZ(), 1.100000023841858D);
                if (this.a.h((Entity) this.b) < 2.5D) {
                    ++this.d;
                    if (this.d > 16) {
                        this.a.x(true);
                        this.a.y(false);
                    } else {
                        this.a.a((Entity) this.b, 45.0F, 45.0F);
                        this.a.y(true);
                    }
                } else {
                    this.a.x(false);
                }
            }

        }
    }

    static class PathfinderGoalTemptChance extends PathfinderGoalTempt {

        @Nullable
        private EntityHuman chosenTarget;
        private final EntityCat d;

        public PathfinderGoalTemptChance(EntityCat entitycat, double d0, RecipeItemStack recipeitemstack, boolean flag) {
            super(entitycat, d0, recipeitemstack, flag);
            this.d = entitycat;
        }

        @Override
        public void e() {
            super.e();
            if (this.chosenTarget == null && this.a.getRandom().nextInt(600) == 0) {
                this.chosenTarget = this.target;
            } else if (this.a.getRandom().nextInt(500) == 0) {
                this.chosenTarget = null;
            }

        }

        @Override
        protected boolean g() {
            return this.chosenTarget != null && this.chosenTarget.equals(this.target) ? false : super.g();
        }

        @Override
        public boolean a() {
            return super.a() && !this.d.isTamed();
        }
    }

    static class a<T extends EntityLiving> extends PathfinderGoalAvoidTarget<T> {

        private final EntityCat i;

        public a(EntityCat entitycat, Class<T> oclass, float f, double d0, double d1) {
            Predicate predicate = IEntitySelector.e;

            super(entitycat, oclass, f, d0, d1, predicate::test);
            this.i = entitycat;
        }

        @Override
        public boolean a() {
            return !this.i.isTamed() && super.a();
        }

        @Override
        public boolean b() {
            return !this.i.isTamed() && super.b();
        }
    }
}
