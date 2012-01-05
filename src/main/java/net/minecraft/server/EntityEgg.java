package net.minecraft.server;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EntityEgg extends EntityProjectile {

    public EntityEgg(World world) {
        super(world);
    }

    public EntityEgg(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityEgg(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        // CraftBukkit start
        if (movingobjectposition.entity != null) {
            boolean stick;
            if (movingobjectposition.entity instanceof EntityLiving) {
                org.bukkit.entity.Entity damagee = movingobjectposition.entity.getBukkitEntity();
                Projectile projectile = (Projectile) this.getBukkitEntity();

                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(projectile, damagee, EntityDamageEvent.DamageCause.PROJECTILE, 0);
                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    stick = !projectile.doesBounce();
                } else {
                    // this function returns if the egg should stick in or not, i.e. !bounce
                    stick = movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shooter), event.getDamage());
                }
            } else {
                stick = movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shooter), 0);
            }

            if (stick) {
                ; // Original code does nothing *yet*
            }
        }

        boolean hatching = !this.world.isStatic && this.random.nextInt(8) == 0;
        int numHatching = (this.random.nextInt(32) == 0) ? 4 : 1;
        if (!hatching) {
            numHatching = 0;
        }

        CreatureType hatchingType = CreatureType.CHICKEN;

        if (this.shooter instanceof EntityPlayer) {
            org.bukkit.entity.Player player = (this.shooter == null) ? null : (org.bukkit.entity.Player) this.shooter.getBukkitEntity();

            PlayerEggThrowEvent event = new PlayerEggThrowEvent(player, (org.bukkit.entity.Egg) this.getBukkitEntity(), hatching, (byte) numHatching, hatchingType);
            this.world.getServer().getPluginManager().callEvent(event);

            hatching = event.isHatching();
            numHatching = event.getNumHatches();
            hatchingType = event.getHatchType();
        }

        if (hatching) {
            for (int k = 0; k < numHatching; k++) {
                org.bukkit.entity.Entity entity = world.getWorld().spawn(new Location(world.getWorld(), this.locX, this.locY, this.locZ, this.yaw, 0.0F), hatchingType.getEntityClass(), SpawnReason.EGG);

                if (entity instanceof Animals) {
                    ((Animals) entity).setBaby();
                }
            }
        }
        // CraftBukkit end

        for (int j = 0; j < 8; ++j) {
            this.world.a("snowballpoof", this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.world.isStatic) {
            this.die();
        }
    }
}
