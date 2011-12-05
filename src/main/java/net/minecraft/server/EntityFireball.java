package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
// CraftBukkit end

public class EntityFireball extends Entity {

    private int e = -1;
    private int f = -1;
    private int g = -1;
    private int h = 0;
    private boolean i = false;
    public EntityLiving shooter;
    private int j;
    private int k = 0;
    public double dirX;
    public double dirY;
    public double dirZ;

    public float yield = 1; // CraftBukkit
    public boolean isIncendiary = true; // CraftBukkit

    public EntityFireball(World world) {
        super(world);
        this.b(1.0F, 1.0F);
    }

    protected void b() {}

    public EntityFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world);
        this.shooter = entityliving;
        this.b(1.0F, 1.0F);
        this.setPositionRotation(entityliving.locX, entityliving.locY, entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        this.motX = this.motY = this.motZ = 0.0D;
        // CraftBukkit start (added setDirection method)
        this.setDirection(d0, d1, d2);
    }

    public void setDirection(double d0, double d1, double d2) {
        d0 += this.random.nextGaussian() * 0.4D;
        d1 += this.random.nextGaussian() * 0.4D;
        d2 += this.random.nextGaussian() * 0.4D;
        double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

        this.dirX = d0 / d3 * 0.1D;
        this.dirY = d1 / d3 * 0.1D;
        this.dirZ = d2 / d3 * 0.1D;
    }

    public void w_() {
        super.w_();
        this.setOnFire(1);
        if (!this.world.isStatic && (this.shooter == null || this.shooter.dead)) {
            this.die();
        }

        if (this.i) {
            int i = this.world.getTypeId(this.e, this.f, this.g);

            if (i == this.h) {
                ++this.j;
                if (this.j == 1200) {
                    this.die();
                }

                return;
            }

            this.i = false;
            this.motX *= (double) (this.random.nextFloat() * 0.2F);
            this.motY *= (double) (this.random.nextFloat() * 0.2F);
            this.motZ *= (double) (this.random.nextFloat() * 0.2F);
            this.j = 0;
            this.k = 0;
        } else {
            ++this.k;
        }

        Vec3D vec3d = Vec3D.create(this.locX, this.locY, this.locZ);
        Vec3D vec3d1 = Vec3D.create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

        vec3d = Vec3D.create(this.locX, this.locY, this.locZ);
        vec3d1 = Vec3D.create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.create(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }

        Entity entity = null;
        List list = this.world.b((Entity) this, this.boundingBox.a(this.motX, this.motY, this.motZ).b(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;

        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity) list.get(j);

            if (entity1.e_() && (!entity1.a((Entity) this.shooter) || this.k >= 25)) {
                float f = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.b((double) f, (double) f, (double) f);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                if (movingobjectposition1 != null) {
                    double d1 = vec3d.b(movingobjectposition1.f);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null) {
            this.a(movingobjectposition);
        }

        this.locX += this.motX;
        this.locY += this.motY;
        this.locZ += this.motZ;
        float f1 = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);

        this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

        for (this.pitch = (float) (Math.atan2(this.motY, (double) f1) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
            ;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
        this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
        float f2 = 0.95F;

        if (this.az()) {
            for (int k = 0; k < 4; ++k) {
                float f3 = 0.25F;

                this.world.a("bubble", this.locX - this.motX * (double) f3, this.locY - this.motY * (double) f3, this.locZ - this.motZ * (double) f3, this.motX, this.motY, this.motZ);
            }

            f2 = 0.8F;
        }

        this.motX += this.dirX;
        this.motY += this.dirY;
        this.motZ += this.dirZ;
        this.motX *= (double) f2;
        this.motY *= (double) f2;
        this.motZ *= (double) f2;
        this.world.a("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            // CraftBukkit start
            ProjectileHitEvent phe = new ProjectileHitEvent((Projectile) this.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(phe);
            // CraftBukkit end
            if (!this.world.isStatic) {
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
                            // this function returns if the fireball should stick in or not, i.e. !bounce
                            stick = movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), event.getDamage());
                        }
                    } else {
                        stick = movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 0);
                    }
                    if (stick) {
                        ;
                    }
                }

                ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive) CraftEntity.getEntity(this.world.getServer(), this));
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    // give 'this' instead of (Entity) null so we know what causes the damage
                    this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
                }
                // CraftBukkit end
            }

            // this.world.createExplosion((Entity) null, this.locX, this.locY, this.locZ, 1.0F, true); // CraftBukkit
            this.die();
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.e);
        nbttagcompound.setShort("yTile", (short) this.f);
        nbttagcompound.setShort("zTile", (short) this.g);
        nbttagcompound.setByte("inTile", (byte) this.h);
        nbttagcompound.setByte("inGround", (byte) (this.i ? 1 : 0));
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("xTile");
        this.f = nbttagcompound.getShort("yTile");
        this.g = nbttagcompound.getShort("zTile");
        this.h = nbttagcompound.getByte("inTile") & 255;
        this.i = nbttagcompound.getByte("inGround") == 1;
    }

    public boolean e_() {
        return true;
    }

    public float j_() {
        return 1.0F;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.aB();
        if (damagesource.getEntity() != null) {
            Vec3D vec3d = damagesource.getEntity().ap();

            if (vec3d != null) {
                this.motX = vec3d.a;
                this.motY = vec3d.b;
                this.motZ = vec3d.c;
                this.dirX = this.motX * 0.1D;
                this.dirY = this.motY * 0.1D;
                this.dirZ = this.motZ * 0.1D;
            }

            if (damagesource.getEntity() instanceof EntityLiving) {
                this.shooter = (EntityLiving) damagesource.getEntity();
            }

            return true;
        } else {
            return false;
        }
    }

    public float a(float f) {
        return 1.0F;
    }
}
