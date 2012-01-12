package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
// CraftBukkit end

public class EntitySnowball extends EntityProjectile {

    public EntitySnowball(World world) {
        super(world);
    }

    public EntitySnowball(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntitySnowball(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entity != null) {
            int b0 = 0; // CraftBukkit - byte -> int

            if (movingobjectposition.entity instanceof EntityBlaze) {
                b0 = 3;
            }
            // CraftBukkit start
            final Entity movingEntity = movingobjectposition.entity;
            boolean stick = false;

            if (movingEntity != null) {
                if (movingEntity instanceof EntityLiving) {
                    org.bukkit.entity.Entity damagee = movingEntity.getBukkitEntity();
                    Projectile projectile = (Projectile) this.getBukkitEntity();

                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(projectile, damagee, EntityDamageEvent.DamageCause.PROJECTILE, b0);
                    Bukkit.getPluginManager().callEvent(event);
                    this.shooter = (projectile.getShooter() == null) ? null : ((CraftLivingEntity) projectile.getShooter()).getHandle();
                    b0 = event.getDamage();

                    if (event.isCancelled()) {
                        stick = !projectile.doesBounce();
                    } else {
                        // this function returns if the snowball should stick in or not, i.e. !bounce
                        stick = movingEntity.damageEntity(DamageSource.projectile(this, this.shooter), b0);
                    }
                } else {
                    stick = movingEntity.damageEntity(DamageSource.projectile(this, this.shooter), b0);
                }
            }

            if (stick) {
                // CraftBukkit end
                ;
            }
        }

        for (int i = 0; i < 8; ++i) {
            this.world.a("snowballpoof", this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.world.isStatic) {
            this.die();
        }
    }
}
