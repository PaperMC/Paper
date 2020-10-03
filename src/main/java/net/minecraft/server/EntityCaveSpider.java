package net.minecraft.server;

import javax.annotation.Nullable;

public class EntityCaveSpider extends EntitySpider {

    public EntityCaveSpider(EntityTypes<? extends EntityCaveSpider> entitytypes, World world) {
        super(entitytypes, world);
    }

    public static AttributeProvider.Builder m() {
        return EntitySpider.eK().a(GenericAttributes.MAX_HEALTH, 12.0D);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        if (super.attackEntity(entity)) {
            if (entity instanceof EntityLiving) {
                byte b0 = 0;

                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 7;
                } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 15;
                }

                if (b0 > 0) {
                    ((EntityLiving) entity).addEffect(new MobEffect(MobEffects.POISON, b0 * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        return groupdataentity;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 0.45F;
    }
}
