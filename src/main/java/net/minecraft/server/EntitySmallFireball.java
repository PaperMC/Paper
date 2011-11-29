package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            // CraftBukkit start - projectile hit event
            ProjectileHitEvent phe = new ProjectileHitEvent((Projectile) this.getBukkitEntity());
            final PluginManager pluginManager = this.world.getServer().getPluginManager();
            pluginManager.callEvent(phe);
            // CraftBukkit end
            if (movingobjectposition.entity != null) {
                // CraftBukkit start - entity damage by entity event + combust event
                if (!movingobjectposition.entity.isFireproof()) { // check if not fireproof
                    boolean stick;
                    org.bukkit.entity.Entity damagee = movingobjectposition.entity.getBukkitEntity();
                    Projectile projectile = (Projectile) this.getBukkitEntity();
                    if (movingobjectposition.entity instanceof EntityLiving) {

                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(projectile, damagee, EntityDamageEvent.DamageCause.PROJECTILE, 5);
                        pluginManager.callEvent(event);

                        if (event.isCancelled()) {
                            stick = !projectile.doesBounce();
                        } else {
                            // this function returns if the fireball should stick in or not, i.e. !bounce
                            stick = movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), event.getDamage());
                        }
                    } else {
                        stick = movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 5);
                    }
                    if (stick) {
                        // if the fireball 'sticks', ignite the target
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(projectile, damagee, 5);
                        pluginManager.callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            movingobjectposition.entity.setOnFire(combustEvent.getDuration());
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

    public boolean e_() {
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }
}
