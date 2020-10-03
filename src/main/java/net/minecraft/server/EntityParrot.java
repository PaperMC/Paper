package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityParrot extends EntityPerchable implements EntityBird {

    private static final DataWatcherObject<Integer> bu = DataWatcher.a(EntityParrot.class, DataWatcherRegistry.b);
    private static final Predicate<EntityInsentient> bv = new Predicate<EntityInsentient>() {
        public boolean test(@Nullable EntityInsentient entityinsentient) {
            return entityinsentient != null && EntityParrot.by.containsKey(entityinsentient.getEntityType());
        }
    };
    private static final Item bw = Items.COOKIE;
    private static final Set<Item> bx = Sets.newHashSet(new Item[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS});
    private static final Map<EntityTypes<?>, SoundEffect> by = (Map) SystemUtils.a(Maps.newHashMap(), (hashmap) -> { // CraftBukkit - decompile error
        hashmap.put(EntityTypes.BLAZE, SoundEffects.ENTITY_PARROT_IMITATE_BLAZE);
        hashmap.put(EntityTypes.CAVE_SPIDER, SoundEffects.ENTITY_PARROT_IMITATE_SPIDER);
        hashmap.put(EntityTypes.CREEPER, SoundEffects.ENTITY_PARROT_IMITATE_CREEPER);
        hashmap.put(EntityTypes.DROWNED, SoundEffects.ENTITY_PARROT_IMITATE_DROWNED);
        hashmap.put(EntityTypes.ELDER_GUARDIAN, SoundEffects.ENTITY_PARROT_IMITATE_ELDER_GUARDIAN);
        hashmap.put(EntityTypes.ENDER_DRAGON, SoundEffects.ENTITY_PARROT_IMITATE_ENDER_DRAGON);
        hashmap.put(EntityTypes.ENDERMITE, SoundEffects.ENTITY_PARROT_IMITATE_ENDERMITE);
        hashmap.put(EntityTypes.EVOKER, SoundEffects.ENTITY_PARROT_IMITATE_EVOKER);
        hashmap.put(EntityTypes.GHAST, SoundEffects.ENTITY_PARROT_IMITATE_GHAST);
        hashmap.put(EntityTypes.GUARDIAN, SoundEffects.ENTITY_PARROT_IMITATE_GUARDIAN);
        hashmap.put(EntityTypes.HOGLIN, SoundEffects.ENTITY_PARROT_IMITATE_HOGLIN);
        hashmap.put(EntityTypes.HUSK, SoundEffects.ENTITY_PARROT_IMITATE_HUSK);
        hashmap.put(EntityTypes.ILLUSIONER, SoundEffects.ENTITY_PARROT_IMITATE_ILLUSIONER);
        hashmap.put(EntityTypes.MAGMA_CUBE, SoundEffects.ENTITY_PARROT_IMITATE_MAGMA_CUBE);
        hashmap.put(EntityTypes.PHANTOM, SoundEffects.ENTITY_PARROT_IMITATE_PHANTOM);
        hashmap.put(EntityTypes.PIGLIN, SoundEffects.ENTITY_PARROT_IMITATE_PIGLIN);
        hashmap.put(EntityTypes.PIGLIN_BRUTE, SoundEffects.ENTITY_PARROT_IMITATE_PIGLIN_BRUTE);
        hashmap.put(EntityTypes.PILLAGER, SoundEffects.ENTITY_PARROT_IMITATE_PILLAGER);
        hashmap.put(EntityTypes.RAVAGER, SoundEffects.ENTITY_PARROT_IMITATE_RAVAGER);
        hashmap.put(EntityTypes.SHULKER, SoundEffects.ENTITY_PARROT_IMITATE_SHULKER);
        hashmap.put(EntityTypes.SILVERFISH, SoundEffects.ENTITY_PARROT_IMITATE_SILVERFISH);
        hashmap.put(EntityTypes.SKELETON, SoundEffects.ENTITY_PARROT_IMITATE_SKELETON);
        hashmap.put(EntityTypes.SLIME, SoundEffects.ENTITY_PARROT_IMITATE_SLIME);
        hashmap.put(EntityTypes.SPIDER, SoundEffects.ENTITY_PARROT_IMITATE_SPIDER);
        hashmap.put(EntityTypes.STRAY, SoundEffects.ENTITY_PARROT_IMITATE_STRAY);
        hashmap.put(EntityTypes.VEX, SoundEffects.ENTITY_PARROT_IMITATE_VEX);
        hashmap.put(EntityTypes.VINDICATOR, SoundEffects.ENTITY_PARROT_IMITATE_VINDICATOR);
        hashmap.put(EntityTypes.WITCH, SoundEffects.ENTITY_PARROT_IMITATE_WITCH);
        hashmap.put(EntityTypes.WITHER, SoundEffects.ENTITY_PARROT_IMITATE_WITHER);
        hashmap.put(EntityTypes.WITHER_SKELETON, SoundEffects.ENTITY_PARROT_IMITATE_WITHER_SKELETON);
        hashmap.put(EntityTypes.ZOGLIN, SoundEffects.ENTITY_PARROT_IMITATE_ZOGLIN);
        hashmap.put(EntityTypes.ZOMBIE, SoundEffects.ENTITY_PARROT_IMITATE_ZOMBIE);
        hashmap.put(EntityTypes.ZOMBIE_VILLAGER, SoundEffects.ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER);
    });
    public float bq;
    public float br;
    public float bs;
    public float bt;
    private float bz = 1.0F;
    private boolean bA;
    private BlockPosition bB;

    public EntityParrot(EntityTypes<? extends EntityParrot> entitytypes, World world) {
        super(entitytypes, world);
        this.moveController = new ControllerMoveFlying(this, 10, false);
        this.a(PathType.DANGER_FIRE, -1.0F);
        this.a(PathType.DAMAGE_FIRE, -1.0F);
        this.a(PathType.COCOA, -1.0F);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setVariant(this.random.nextInt(5));
        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(false);
        }

        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalPanic(this, 1.25D));
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(2, new PathfinderGoalSit(this));
        this.goalSelector.a(2, new PathfinderGoalFollowOwner(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.a(2, new PathfinderGoalRandomFly(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalPerch(this));
        this.goalSelector.a(3, new PathfinderGoalFollowEntity(this, 1.0D, 3.0F, 7.0F));
    }

    public static AttributeProvider.Builder eU() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 6.0D).a(GenericAttributes.FLYING_SPEED, 0.4000000059604645D).a(GenericAttributes.MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    protected NavigationAbstract b(World world) {
        NavigationFlying navigationflying = new NavigationFlying(this, world);

        navigationflying.a(false);
        navigationflying.d(true);
        navigationflying.b(true);
        return navigationflying;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.6F;
    }

    @Override
    public void movementTick() {
        if (this.bB == null || !this.bB.a((IPosition) this.getPositionVector(), 3.46D) || !this.world.getType(this.bB).a(Blocks.JUKEBOX)) {
            this.bA = false;
            this.bB = null;
        }

        if (this.world.random.nextInt(400) == 0) {
            a(this.world, (Entity) this);
        }

        super.movementTick();
        this.eZ();
    }

    private void eZ() {
        this.bt = this.bq;
        this.bs = this.br;
        this.br = (float) ((double) this.br + (double) (!this.onGround && !this.isPassenger() ? 4 : -1) * 0.3D);
        this.br = MathHelper.a(this.br, 0.0F, 1.0F);
        if (!this.onGround && this.bz < 1.0F) {
            this.bz = 1.0F;
        }

        this.bz = (float) ((double) this.bz * 0.9D);
        Vec3D vec3d = this.getMot();

        if (!this.onGround && vec3d.y < 0.0D) {
            this.setMot(vec3d.d(1.0D, 0.6D, 1.0D));
        }

        this.bq += this.bz * 2.0F;
    }

    public static boolean a(World world, Entity entity) {
        if (entity.isAlive() && !entity.isSilent() && world.random.nextInt(2) == 0) {
            List<EntityInsentient> list = world.a(EntityInsentient.class, entity.getBoundingBox().g(20.0D), EntityParrot.bv);

            if (!list.isEmpty()) {
                EntityInsentient entityinsentient = (EntityInsentient) list.get(world.random.nextInt(list.size()));

                if (!entityinsentient.isSilent()) {
                    SoundEffect soundeffect = b(entityinsentient.getEntityType());

                    world.playSound((EntityHuman) null, entity.locX(), entity.locY(), entity.locZ(), soundeffect, entity.getSoundCategory(), 0.7F, a(world.random));
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!this.isTamed() && EntityParrot.bx.contains(itemstack.getItem())) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }

            if (!this.isSilent()) {
                this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.world.isClientSide) {
                if (this.random.nextInt(10) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) { // CraftBukkit
                    this.tame(entityhuman);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else if (itemstack.getItem() == EntityParrot.bw) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }

            this.addEffect(new MobEffect(MobEffects.POISON, 900), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.FOOD); // CraftBukkit
            if (entityhuman.isCreative() || !this.isInvulnerable()) {
                this.damageEntity(DamageSource.playerAttack(entityhuman), Float.MAX_VALUE);
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else if (!this.fa() && this.isTamed() && this.i((EntityLiving) entityhuman)) {
            if (!this.world.isClientSide) {
                this.setWillSit(!this.isWillSit());
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    @Override
    public boolean k(ItemStack itemstack) {
        return false;
    }

    public static boolean c(EntityTypes<EntityParrot> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        IBlockData iblockdata = generatoraccess.getType(blockposition.down());

        return (iblockdata.a((Tag) TagsBlock.LEAVES) || iblockdata.a(Blocks.GRASS_BLOCK) || iblockdata.a((Tag) TagsBlock.LOGS) || iblockdata.a(Blocks.AIR)) && generatoraccess.getLightLevel(blockposition, 0) > 8;
    }

    @Override
    public boolean b(float f, float f1) {
        return false;
    }

    @Override
    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {}

    @Override
    public boolean mate(EntityAnimal entityanimal) {
        return false;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return null;
    }

    @Override
    public boolean attackEntity(Entity entity) {
        return entity.damageEntity(DamageSource.mobAttack(this), 3.0F);
    }

    @Nullable
    @Override
    public SoundEffect getSoundAmbient() {
        return a(this.world, this.world.random);
    }

    public static SoundEffect a(World world, Random random) {
        if (world.getDifficulty() != EnumDifficulty.PEACEFUL && random.nextInt(1000) == 0) {
            List<EntityTypes<?>> list = Lists.newArrayList(EntityParrot.by.keySet());

            return b((EntityTypes) list.get(random.nextInt(list.size())));
        } else {
            return SoundEffects.ENTITY_PARROT_AMBIENT;
        }
    }

    private static SoundEffect b(EntityTypes<?> entitytypes) {
        return (SoundEffect) EntityParrot.by.getOrDefault(entitytypes, SoundEffects.ENTITY_PARROT_AMBIENT);
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PARROT_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PARROT_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float e(float f) {
        this.playSound(SoundEffects.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        return f + this.br / 2.0F;
    }

    @Override
    protected boolean ay() {
        return true;
    }

    @Override
    protected float dG() {
        return a(this.random);
    }

    public static float a(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public boolean isCollidable() {
        return super.isCollidable(); // CraftBukkit - collidable API
    }

    @Override
    protected void C(Entity entity) {
        if (!(entity instanceof EntityHuman)) {
            super.C(entity);
        }
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            // this.setWillSit(false); // CraftBukkit - moved into EntityLiving.damageEntity(DamageSource, float)
            return super.damageEntity(damagesource, f);
        }
    }

    public int getVariant() {
        return MathHelper.clamp((Integer) this.datawatcher.get(EntityParrot.bu), 0, 4);
    }

    public void setVariant(int i) {
        this.datawatcher.set(EntityParrot.bu, i);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityParrot.bu, 0);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("Variant", this.getVariant());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setVariant(nbttagcompound.getInt("Variant"));
    }

    public boolean fa() {
        return !this.onGround;
    }
}
