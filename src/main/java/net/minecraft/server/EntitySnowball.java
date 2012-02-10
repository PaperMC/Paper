package net.minecraft.server;

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
            // CraftBukkit - entity.damageEntity -> event function
            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleProjectileEvent((org.bukkit.entity.Projectile) this.getBukkitEntity(), movingobjectposition.entity, DamageSource.projectile(this, this.shooter), b0)) {
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
