package net.minecraft.server;

import org.bukkit.event.entity.ExplosionPrimeEvent; // CraftBukkit

public class EntityLargeFireball extends EntityFireballFireball {

    public int yield = 1;

    public EntityLargeFireball(EntityTypes<? extends EntityLargeFireball> entitytypes, World world) {
        super(entitytypes, world);
        isIncendiary = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING); // CraftBukkit
    }

    public EntityLargeFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(EntityTypes.FIREBALL, entityliving, d0, d1, d2, world);
        isIncendiary = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING); // CraftBukkit
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
        super.a(movingobjectposition);
        if (!this.world.isClientSide) {
            boolean flag = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);

            // CraftBukkit start - fire ExplosionPrimeEvent
            ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) this.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                // give 'this' instead of (Entity) null so we know what causes the damage
                this.world.createExplosion(this, this.locX(), this.locY(), this.locZ(), event.getRadius(), event.getFire(), flag ? Explosion.Effect.DESTROY : Explosion.Effect.NONE);
            }
            // CraftBukkit end
            this.die();
        }

    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);
        if (!this.world.isClientSide) {
            Entity entity = movingobjectpositionentity.getEntity();
            Entity entity1 = this.getShooter();

            entity.damageEntity(DamageSource.fireball(this, entity1), 6.0F);
            if (entity1 instanceof EntityLiving) {
                this.a((EntityLiving) entity1, entity);
            }

        }
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("ExplosionPower", this.yield);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("ExplosionPower", 99)) {
            // CraftBukkit - set bukkitYield when setting explosionpower
            bukkitYield = this.yield = nbttagcompound.getInt("ExplosionPower");
        }

    }
}
