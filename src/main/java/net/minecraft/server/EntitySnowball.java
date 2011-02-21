package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
// CraftBukkit end

public class EntitySnowball extends Entity {

    private int b = -1;
    private int c = -1;
    private int d = -1;
    private int e = 0;
    private boolean f = false;
    public int a = 0;
    private EntityLiving ak;
    private int al;
    private int am = 0;

    public EntitySnowball(World world) {
        super(world);
        this.a(0.25F, 0.25F);
    }

    protected void a() {}

    public EntitySnowball(World world, EntityLiving entityliving) {
        super(world);
        this.ak = entityliving;
        this.a(0.25F, 0.25F);
        this.c(entityliving.locX, entityliving.locY + (double) entityliving.w(), entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.locX -= (double) (MathHelper.b(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.locY -= 0.10000000149011612D;
        this.locZ -= (double) (MathHelper.a(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.a(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        float f = 0.4F;

        this.motX = (double) (-MathHelper.a(this.yaw / 180.0F * 3.1415927F) * MathHelper.b(this.pitch / 180.0F * 3.1415927F) * f);
        this.motZ = (double) (MathHelper.b(this.yaw / 180.0F * 3.1415927F) * MathHelper.b(this.pitch / 180.0F * 3.1415927F) * f);
        this.motY = (double) (-MathHelper.a(this.pitch / 180.0F * 3.1415927F) * f);
        this.a(this.motX, this.motY, this.motZ, 1.5F, 1.0F);
    }

    public EntitySnowball(World world, double d0, double d1, double d2) {
        super(world);
        this.al = 0;
        this.a(0.25F, 0.25F);
        this.a(d0, d1, d2);
        this.height = 0.0F;
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.motX = d0;
        this.motY = d1;
        this.motZ = d2;
        float f3 = MathHelper.a(d0 * d0 + d2 * d2);

        this.lastYaw = this.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        this.lastPitch = this.pitch = (float) (Math.atan2(d1, (double) f3) * 180.0D / 3.1415927410125732D);
        this.al = 0;
    }

    public void b_() {
        this.O = this.locX;
        this.P = this.locY;
        this.Q = this.locZ;
        super.b_();
        if (this.a > 0) {
            --this.a;
        }

        if (this.f) {
            int i = this.world.getTypeId(this.b, this.c, this.d);

            if (i == this.e) {
                ++this.al;
                if (this.al == 1200) {
                    this.q();
                }

                return;
            }

            this.f = false;
            this.motX *= (double) (this.random.nextFloat() * 0.2F);
            this.motY *= (double) (this.random.nextFloat() * 0.2F);
            this.motZ *= (double) (this.random.nextFloat() * 0.2F);
            this.al = 0;
            this.am = 0;
        } else {
            ++this.am;
        }

        Vec3D vec3d = Vec3D.b(this.locX, this.locY, this.locZ);
        Vec3D vec3d1 = Vec3D.b(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

        vec3d = Vec3D.b(this.locX, this.locY, this.locZ);
        vec3d1 = Vec3D.b(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.b(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }

        if (!this.world.isStatic) {
            Entity entity = null;
            List list = this.world.b((Entity) this, this.boundingBox.a(this.motX, this.motY, this.motZ).b(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.c_() && (entity1 != this.ak || this.am >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.b((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.a(movingobjectposition1.f);

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
        }

        if (movingobjectposition != null) {
            // CraftBukkit start
            if (movingobjectposition.g != null) {
                boolean stick;
                if (movingobjectposition.g instanceof EntityLiving) {
                    CraftServer server = ((WorldServer) this.world).getServer();
                    org.bukkit.entity.Entity shooter = (this.ak == null) ? null : this.ak.getBukkitEntity();
                    org.bukkit.entity.Entity damagee = movingobjectposition.g.getBukkitEntity();
                    org.bukkit.entity.Entity projectile = this.getBukkitEntity();
                    DamageCause damageCause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
                    int damage = 0;

                    // TODO @see EntityArrow#162
                    EntityDamageByProjectileEvent event = new EntityDamageByProjectileEvent(shooter, damagee, projectile, damageCause, damage);
                    server.getPluginManager().callEvent(event);

                    if(!event.isCancelled()) {
                        // this function returns if the snowball should stick or not, i.e. !bounce
                        stick = movingobjectposition.g.a(this.ak, event.getDamage());
                    } else {
                        // event was cancelled, get if the snowball should bounce or not
                        stick = !event.getBounce();
                    }
                } else {
                    stick = movingobjectposition.g.a(this.ak, 0);
                }
                if (stick) {
                    ;
                }
            }
            // CraftBukkit end

            for (int k = 0; k < 8; ++k) {
                this.world.a("snowballpoof", this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D);
            }

            this.q();
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
        float f2 = 0.99F;
        float f3 = 0.03F;

        if (this.v()) {
            for (int l = 0; l < 4; ++l) {
                float f4 = 0.25F;

                this.world.a("bubble", this.locX - this.motX * (double) f4, this.locY - this.motY * (double) f4, this.locZ - this.motZ * (double) f4, this.motX, this.motY, this.motZ);
            }

            f2 = 0.8F;
        }

        this.motX *= (double) f2;
        this.motY *= (double) f2;
        this.motZ *= (double) f2;
        this.motY -= (double) f3;
        this.a(this.locX, this.locY, this.locZ);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) this.b);
        nbttagcompound.a("yTile", (short) this.c);
        nbttagcompound.a("zTile", (short) this.d);
        nbttagcompound.a("inTile", (byte) this.e);
        nbttagcompound.a("shake", (byte) this.a);
        nbttagcompound.a("inGround", (byte) (this.f ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.b = nbttagcompound.c("xTile");
        this.c = nbttagcompound.c("yTile");
        this.d = nbttagcompound.c("zTile");
        this.e = nbttagcompound.b("inTile") & 255;
        this.a = nbttagcompound.b("shake") & 255;
        this.f = nbttagcompound.b("inGround") == 1;
    }

    public void b(EntityHuman entityhuman) {
        if (this.f && this.ak == entityhuman && this.a <= 0 && entityhuman.inventory.a(new ItemStack(Item.ARROW, 1))) {
            this.world.a(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityhuman.c(this, 1);
            this.q();
        }
    }
}
