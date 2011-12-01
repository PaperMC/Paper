package net.minecraft.server;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityEnderPearl extends EntityProjectile {
    public EntityEnderPearl(World world) {
        super(world);
    }

    public EntityEnderPearl(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityEnderPearl(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entity != null && movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shooter), 0)) {
            ;
        }

        for (int i = 0; i < 32; ++i) {
            this.world.a("portal", this.locX, this.locY + this.random.nextDouble() * 2.0D, this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.world.isStatic) {
            // CraftBukkit start - dupe fix + damage event
            boolean damage = false;
            if (this.shooter != null) {
                if (this.shooter instanceof EntityPlayer) {
                    damage = ((CraftPlayer)this.shooter.bukkitEntity).isOnline();
                } else {
                    damage = true;
                }
            }

            if (damage) {
                this.shooter.a_(this.locX, this.locY, this.locZ);
                this.shooter.fallDistance = 0.0F;
                EntityDamageEvent event = new EntityDamageEvent(getBukkitEntity(), EntityDamageEvent.DamageCause.FALL, 5);
                Bukkit.getPluginManager().callEvent(event);
                this.shooter.damageEntity(DamageSource.FALL, event.getDamage());
            }
            // CraftBukkit end

            this.die();
        }
    }
}
