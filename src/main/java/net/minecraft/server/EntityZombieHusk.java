package net.minecraft.server;

import java.util.Random;

public class EntityZombieHusk extends EntityZombie {

    public EntityZombieHusk(EntityTypes<? extends EntityZombieHusk> entitytypes, World world) {
        super(entitytypes, world);
    }

    public static boolean a(EntityTypes<EntityZombieHusk> entitytypes, WorldAccess worldaccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return b(entitytypes, worldaccess, enummobspawn, blockposition, random) && (enummobspawn == EnumMobSpawn.SPAWNER || worldaccess.e(blockposition));
    }

    @Override
    protected boolean T_() {
        return false;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_HUSK_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_HUSK_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_HUSK_DEATH;
    }

    @Override
    protected SoundEffect getSoundStep() {
        return SoundEffects.ENTITY_HUSK_STEP;
    }

    @Override
    public boolean attackEntity(Entity entity) {
        boolean flag = super.attackEntity(entity);

        if (flag && this.getItemInMainHand().isEmpty() && entity instanceof EntityLiving) {
            float f = this.world.getDamageScaler(this.getChunkCoordinates()).b();

            ((EntityLiving) entity).addEffect(new MobEffect(MobEffects.HUNGER, 140 * (int) f), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.ATTACK); // CraftBukkit
        }

        return flag;
    }

    @Override
    protected boolean eN() {
        return true;
    }

    @Override
    protected void eP() {
        this.b(EntityTypes.ZOMBIE);
        if (!this.isSilent()) {
            this.world.a((EntityHuman) null, 1041, this.getChunkCoordinates(), 0);
        }

    }

    @Override
    protected ItemStack eM() {
        return ItemStack.b;
    }
}
