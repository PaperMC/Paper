package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class EntityIronGolem extends EntityGolem implements IEntityAngerable {

    protected static final DataWatcherObject<Byte> b = DataWatcher.a(EntityIronGolem.class, DataWatcherRegistry.a);
    private int c;
    private int d;
    private static final IntRange bo = TimeRange.a(20, 39);
    private int bp;
    private UUID bq;

    public EntityIronGolem(EntityTypes<? extends EntityIronGolem> entitytypes, World world) {
        super(entitytypes, world);
        this.G = 1.0F;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
        this.goalSelector.a(2, new PathfinderGoalStrollVillage(this, 0.6D, false));
        this.goalSelector.a(4, new PathfinderGoalStrollVillageGolem(this, 0.6D));
        this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 10, true, false, this::a_));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 5, false, false, (entityliving) -> {
            return entityliving instanceof IMonster && !(entityliving instanceof EntityCreeper);
        }));
        this.targetSelector.a(4, new PathfinderGoalUniversalAngerReset<>(this, false));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityIronGolem.b, (byte) 0);
    }

    public static AttributeProvider.Builder m() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 100.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.25D).a(GenericAttributes.KNOCKBACK_RESISTANCE, 1.0D).a(GenericAttributes.ATTACK_DAMAGE, 15.0D);
    }

    @Override
    protected int l(int i) {
        return i;
    }

    @Override
    protected void C(Entity entity) {
        if (entity instanceof IMonster && !(entity instanceof EntityCreeper) && this.getRandom().nextInt(20) == 0) {
            this.setGoalTarget((EntityLiving) entity, org.bukkit.event.entity.EntityTargetLivingEntityEvent.TargetReason.COLLISION, true); // CraftBukkit - set reason
        }

        super.C(entity);
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.c > 0) {
            --this.c;
        }

        if (this.d > 0) {
            --this.d;
        }

        if (c(this.getMot()) > 2.500000277905201E-7D && this.random.nextInt(5) == 0) {
            int i = MathHelper.floor(this.locX());
            int j = MathHelper.floor(this.locY() - 0.20000000298023224D);
            int k = MathHelper.floor(this.locZ());
            IBlockData iblockdata = this.world.getType(new BlockPosition(i, j, k));

            if (!iblockdata.isAir()) {
                this.world.addParticle(new ParticleParamBlock(Particles.BLOCK, iblockdata), this.locX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getWidth(), this.locY() + 0.1D, this.locZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getWidth(), 4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }

        if (!this.world.isClientSide) {
            this.a((WorldServer) this.world, true);
        }

    }

    @Override
    public boolean a(EntityTypes<?> entitytypes) {
        return this.isPlayerCreated() && entitytypes == EntityTypes.PLAYER ? false : (entitytypes == EntityTypes.CREEPER ? false : super.a(entitytypes));
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("PlayerCreated", this.isPlayerCreated());
        this.c(nbttagcompound);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setPlayerCreated(nbttagcompound.getBoolean("PlayerCreated"));
        this.a((WorldServer) this.world, nbttagcompound);
    }

    @Override
    public void anger() {
        this.setAnger(EntityIronGolem.bo.a(this.random));
    }

    @Override
    public void setAnger(int i) {
        this.bp = i;
    }

    @Override
    public int getAnger() {
        return this.bp;
    }

    @Override
    public void setAngerTarget(@Nullable UUID uuid) {
        this.bq = uuid;
    }

    @Override
    public UUID getAngerTarget() {
        return this.bq;
    }

    private float eO() {
        return (float) this.b(GenericAttributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        this.c = 10;
        this.world.broadcastEntityEffect(this, (byte) 4);
        float f = this.eO();
        float f1 = (int) f > 0 ? f / 2.0F + (float) this.random.nextInt((int) f) : f;
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f1);

        if (flag) {
            entity.setMot(entity.getMot().add(0.0D, 0.4000000059604645D, 0.0D));
            this.a((EntityLiving) this, entity);
        }

        this.playSound(SoundEffects.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        EntityIronGolem.CrackLevel entityirongolem_cracklevel = this.eK();
        boolean flag = super.damageEntity(damagesource, f);

        if (flag && this.eK() != entityirongolem_cracklevel) {
            this.playSound(SoundEffects.ENTITY_IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }

        return flag;
    }

    public EntityIronGolem.CrackLevel eK() {
        return EntityIronGolem.CrackLevel.a(this.getHealth() / this.getMaxHealth());
    }

    public void t(boolean flag) {
        if (flag) {
            this.d = 400;
            this.world.broadcastEntityEffect(this, (byte) 11);
        } else {
            this.d = 0;
            this.world.broadcastEntityEffect(this, (byte) 34);
        }

    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_IRON_GOLEM_DEATH;
    }

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        Item item = itemstack.getItem();

        if (item != Items.IRON_INGOT) {
            return EnumInteractionResult.PASS;
        } else {
            float f = this.getHealth();

            this.heal(25.0F);
            if (this.getHealth() == f) {
                return EnumInteractionResult.PASS;
            } else {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;

                this.playSound(SoundEffects.ENTITY_IRON_GOLEM_REPAIR, 1.0F, f1);
                if (!entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                }

                return EnumInteractionResult.a(this.world.isClientSide);
            }
        }
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    public boolean isPlayerCreated() {
        return ((Byte) this.datawatcher.get(EntityIronGolem.b) & 1) != 0;
    }

    public void setPlayerCreated(boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityIronGolem.b);

        if (flag) {
            this.datawatcher.set(EntityIronGolem.b, (byte) (b0 | 1));
        } else {
            this.datawatcher.set(EntityIronGolem.b, (byte) (b0 & -2));
        }

    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
    }

    @Override
    public boolean a(IWorldReader iworldreader) {
        BlockPosition blockposition = this.getChunkCoordinates();
        BlockPosition blockposition1 = blockposition.down();
        IBlockData iblockdata = iworldreader.getType(blockposition1);

        if (!iblockdata.a((IBlockAccess) iworldreader, blockposition1, (Entity) this)) {
            return false;
        } else {
            for (int i = 1; i < 3; ++i) {
                BlockPosition blockposition2 = blockposition.up(i);
                IBlockData iblockdata1 = iworldreader.getType(blockposition2);

                if (!SpawnerCreature.a((IBlockAccess) iworldreader, blockposition2, iblockdata1, iblockdata1.getFluid(), EntityTypes.IRON_GOLEM)) {
                    return false;
                }
            }

            return SpawnerCreature.a((IBlockAccess) iworldreader, blockposition, iworldreader.getType(blockposition), FluidTypes.EMPTY.h(), EntityTypes.IRON_GOLEM) && iworldreader.j((Entity) this);
        }
    }

    public static enum CrackLevel {

        NONE(1.0F), LOW(0.75F), MEDIUM(0.5F), HIGH(0.25F);

        private static final List<EntityIronGolem.CrackLevel> e = (List) Stream.of(values()).sorted(Comparator.comparingDouble((entityirongolem_cracklevel) -> {
            return (double) entityirongolem_cracklevel.f;
        })).collect(ImmutableList.toImmutableList());
        private final float f;

        private CrackLevel(float f) {
            this.f = f;
        }

        public static EntityIronGolem.CrackLevel a(float f) {
            Iterator iterator = EntityIronGolem.CrackLevel.e.iterator();

            EntityIronGolem.CrackLevel entityirongolem_cracklevel;

            do {
                if (!iterator.hasNext()) {
                    return EntityIronGolem.CrackLevel.NONE;
                }

                entityirongolem_cracklevel = (EntityIronGolem.CrackLevel) iterator.next();
            } while (f >= entityirongolem_cracklevel.f);

            return entityirongolem_cracklevel;
        }
    }
}
