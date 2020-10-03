package net.minecraft.server;

import javax.annotation.Nullable;

public class EntitySkeletonWither extends EntitySkeletonAbstract {

    public EntitySkeletonWither(EntityTypes<? extends EntitySkeletonWither> entitytypes, World world) {
        super(entitytypes, world);
        this.a(PathType.LAVA, 8.0F);
    }

    @Override
    protected void initPathfinder() {
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityPiglinAbstract.class, true));
        super.initPathfinder();
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_WITHER_SKELETON_DEATH;
    }

    @Override
    SoundEffect eK() {
        return SoundEffects.ENTITY_WITHER_SKELETON_STEP;
    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
        super.dropDeathLoot(damagesource, i, flag);
        Entity entity = damagesource.getEntity();

        if (entity instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) entity;

            if (entitycreeper.canCauseHeadDrop()) {
                entitycreeper.setCausedHeadDrop();
                this.a((IMaterial) Items.WITHER_SKELETON_SKULL);
            }
        }

    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }

    @Override
    protected void b(DifficultyDamageScaler difficultydamagescaler) {}

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        GroupDataEntity groupdataentity1 = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);

        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
        this.eL();
        return groupdataentity1;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 2.1F;
    }

    @Override
    public boolean attackEntity(Entity entity) {
        if (!super.attackEntity(entity)) {
            return false;
        } else {
            if (entity instanceof EntityLiving) {
                ((EntityLiving) entity).addEffect(new MobEffect(MobEffects.WITHER, 200), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.ATTACK); // CraftBukkit
            }

            return true;
        }
    }

    @Override
    protected EntityArrow b(ItemStack itemstack, float f) {
        EntityArrow entityarrow = super.b(itemstack, f);

        entityarrow.setOnFire(100);
        return entityarrow;
    }

    @Override
    public boolean d(MobEffect mobeffect) {
        return mobeffect.getMobEffect() == MobEffects.WITHER ? false : super.d(mobeffect);
    }
}
