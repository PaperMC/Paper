package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.PluginManager;
// CraftBukkit end

public class EntitySmallFireball extends EntityFireball {

    public EntitySmallFireball(World world) {
        super(world);
        this.b(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.b(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world, d0, d1, d2, d3, d4, d5);
        this.b(0.3125F, 0.3125F);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            // CraftBukkit start - projectile hit event
            Projectile projectile = (Projectile) this.getBukkitEntity();
            ProjectileHitEvent phe = new ProjectileHitEvent(projectile);
            final PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.callEvent(phe);
            // CraftBukkit end
            final Entity movingEntity = movingobjectposition.entity;
            if (movingEntity != null) {
                // CraftBukkit start - entity damage by entity event + combust event
                if (!movingEntity.isFireproof()) { // check if not fireproof
                    org.bukkit.entity.Entity damagee = movingEntity.getBukkitEntity();

                    if (org.bukkit.craftbukkit.event.CraftEventFactory.handleProjectileEvent(projectile, movingobjectposition.entity, DamageSource.projectile(this, this.shooter), 0)) {
                        // if the fireball 'sticks', ignite the target
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(projectile, damagee, 5);
                        pluginManager.callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            movingEntity.setOnFire(combustEvent.getDuration());
                        }
                    }
                    // CraftBukkit end
                }
            } else {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;

                switch (movingobjectposition.face) {
                case 0:
                    --j;
                    break;

                case 1:
                    ++j;
                    break;

                case 2:
                    --k;
                    break;

                case 3:
                    ++k;
                    break;

                case 4:
                    --i;
                    break;

                case 5:
                    ++i;
                }

                if (this.world.isEmpty(i, j, k)) {
                    this.world.setTypeId(i, j, k, Block.FIRE.id);
                }
            }

            this.die();
        }
    }

    public boolean o_() {
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }
}
