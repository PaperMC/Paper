package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
// CraftBukkit end

public class EntityArrow extends Entity {

    private int e = -1;
    private int f = -1;
    private int g = -1;
    private int h = 0;
    private int i = 0;
    private boolean inGround = false;
    public boolean fromPlayer = false;
    public int shake = 0;
    public Entity shooter;
    private int k;
    private int l = 0;
    private double damage = 2.0D;
    private int n;
    public boolean d = false;

    public EntityArrow(World world) {
        super(world);
        this.b(0.5F, 0.5F);
    }

    public EntityArrow(World world, double d0, double d1, double d2) {
        super(world);
        this.b(0.5F, 0.5F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
    }

    public EntityArrow(World world, EntityLiving entityliving, EntityLiving entityliving1, float f, float f1) {
        super(world);
        this.shooter = entityliving;
        this.fromPlayer = entityliving instanceof EntityHuman;
        this.locY = entityliving.locY + (double) entityliving.getHeadHeight() - 0.10000000149011612D;
        double d0 = entityliving1.locX - entityliving.locX;
        double d1 = entityliving1.locY + (double) entityliving1.getHeadHeight() - 0.699999988079071D - this.locY;
        double d2 = entityliving1.locZ - entityliving.locZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D) {
            float f2 = (float) (Math.atan2(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / 3.1415927410125732D));
            double d4 = d0 / d3;
            double d5 = d2 / d3;

            this.setPositionRotation(entityliving.locX + d4, this.locY, entityliving.locZ + d5, f2, f3);
            this.height = 0.0F;
            float f4 = (float) d3 * 0.2F;

            this.shoot(d0, d1 + (double) f4, d2, f, f1);
        }
    }

    public EntityArrow(World world, EntityLiving entityliving, float f) {
        super(world);
        this.shooter = entityliving;
        this.fromPlayer = entityliving instanceof EntityHuman;
        this.b(0.5F, 0.5F);
        this.setPositionRotation(entityliving.locX, entityliving.locY + (double) entityliving.getHeadHeight(), entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.locX -= (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.locY -= 0.10000000149011612D;
        this.locZ -= (double) (MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        this.motX = (double) (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
        this.motZ = (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
        this.motY = (double) (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F));
        this.shoot(this.motX, this.motY, this.motZ, f * 1.5F, 1.0F);
    }

    protected void b() {}

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

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
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.lastYaw = this.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        this.lastPitch = this.pitch = (float) (Math.atan2(d1, (double) f3) * 180.0D / 3.1415927410125732D);
        this.k = 0;
    }

    public void G_() {
        super.G_();
        if (this.lastPitch == 0.0F && this.lastYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

            this.lastYaw = this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);
            this.lastPitch = this.pitch = (float) (Math.atan2(this.motY, (double) f) * 180.0D / 3.1415927410125732D);
        }

        int i = this.world.getTypeId(this.e, this.f, this.g);

        if (i > 0) {
            Block.byId[i].updateShape(this.world, this.e, this.f, this.g);
            AxisAlignedBB axisalignedbb = Block.byId[i].e(this.world, this.e, this.f, this.g);

            if (axisalignedbb != null && axisalignedbb.a(Vec3D.create(this.locX, this.locY, this.locZ))) {
                this.inGround = true;
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            i = this.world.getTypeId(this.e, this.f, this.g);
            int j = this.world.getData(this.e, this.f, this.g);

            if (i == this.h && j == this.i) {
                ++this.k;
                if (this.k == 1200) {
                    this.die();
                }
            } else {
                this.inGround = false;
                this.motX *= (double) (this.random.nextFloat() * 0.2F);
                this.motY *= (double) (this.random.nextFloat() * 0.2F);
                this.motZ *= (double) (this.random.nextFloat() * 0.2F);
                this.k = 0;
                this.l = 0;
            }
        } else {
            ++this.l;
            Vec3D vec3d = Vec3D.create(this.locX, this.locY, this.locZ);
            Vec3D vec3d1 = Vec3D.create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1, false, true);

            vec3d = Vec3D.create(this.locX, this.locY, this.locZ);
            vec3d1 = Vec3D.create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3D.create(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
            }

            Entity entity = null;
            List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            int k;
            float f1;

            for (k = 0; k < list.size(); ++k) {
                Entity entity1 = (Entity) list.get(k);

                if (entity1.o_() && (entity1 != this.shooter || this.l >= 5)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.grow((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.distanceSquared(movingobjectposition1.pos); // CraftBukkit - distance efficiency

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

            float f2;

            if (movingobjectposition != null) {
                // CraftBukkit start
                Projectile projectile = (Projectile) this.getBukkitEntity();
                ProjectileHitEvent phe = new ProjectileHitEvent(projectile);
                this.world.getServer().getPluginManager().callEvent(phe);
                // CraftBukkit end
                if (movingobjectposition.entity != null) {
                    f2 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
                    int l = (int) Math.ceil((double) f2 * this.damage);

                    if (this.d) {
                        l += this.random.nextInt(l / 2 + 2);
                    }

                    DamageSource damagesource = null;

                    if (this.shooter == null) {
                        damagesource = DamageSource.arrow(this, this);
                    } else {
                        damagesource = DamageSource.arrow(this, this.shooter);
                    }

                    if (this.isBurning() && (!(movingobjectposition.entity instanceof EntityPlayer) || this.world.pvpMode)) { // CraftBukkit - abide by pvp setting if destination is a player.
                        // CraftBukkit start
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
                        Bukkit.getPluginManager().callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            movingobjectposition.entity.setOnFire(combustEvent.getDuration());
                        }
                        // CraftBukkit end
                    }

                    if (movingobjectposition.entity.damageEntity(damagesource, l)) {
                        if (movingobjectposition.entity instanceof EntityLiving) {
                            ++((EntityLiving) movingobjectposition.entity).aI;
                            if (this.n > 0) {
                                float f3 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

                                if (f3 > 0.0F) {
                                    movingobjectposition.entity.b_(this.motX * (double) this.n * 0.6000000238418579D / (double) f3, 0.1D, this.motZ * (double) this.n * 0.6000000238418579D / (double) f3);
                                }
                            }
                        }

                        this.world.makeSound(this, "random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                        this.die();
                    } else {
                        this.motX *= -0.10000000149011612D;
                        this.motY *= -0.10000000149011612D;
                        this.motZ *= -0.10000000149011612D;
                        this.yaw += 180.0F;
                        this.lastYaw += 180.0F;
                        this.l = 0;
                    }
                } else {
                    this.e = movingobjectposition.b;
                    this.f = movingobjectposition.c;
                    this.g = movingobjectposition.d;
                    this.h = this.world.getTypeId(this.e, this.f, this.g);
                    this.i = this.world.getData(this.e, this.f, this.g);
                    this.motX = (double) ((float) (movingobjectposition.pos.a - this.locX));
                    this.motY = (double) ((float) (movingobjectposition.pos.b - this.locY));
                    this.motZ = (double) ((float) (movingobjectposition.pos.c - this.locZ));
                    f2 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
                    this.locX -= this.motX / (double) f2 * 0.05000000074505806D;
                    this.locY -= this.motY / (double) f2 * 0.05000000074505806D;
                    this.locZ -= this.motZ / (double) f2 * 0.05000000074505806D;
                    this.world.makeSound(this, "random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.shake = 7;
                    this.d = false;
                }
            }

            if (this.d) {
                for (k = 0; k < 4; ++k) {
                    this.world.a("crit", this.locX + this.motX * (double) k / 4.0D, this.locY + this.motY * (double) k / 4.0D, this.locZ + this.motZ * (double) k / 4.0D, -this.motX, -this.motY + 0.2D, -this.motZ);
                }
            }

            this.locX += this.motX;
            this.locY += this.motY;
            this.locZ += this.motZ;
            f2 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

            for (this.pitch = (float) (Math.atan2(this.motY, (double) f2) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
            float f4 = 0.99F;

            f1 = 0.05F;
            if (this.aT()) {
                for (int i1 = 0; i1 < 4; ++i1) {
                    float f5 = 0.25F;

                    this.world.a("bubble", this.locX - this.motX * (double) f5, this.locY - this.motY * (double) f5, this.locZ - this.motZ * (double) f5, this.motX, this.motY, this.motZ);
                }

                f4 = 0.8F;
            }

            this.motX *= (double) f4;
            this.motY *= (double) f4;
            this.motZ *= (double) f4;
            this.motY -= (double) f1;
            this.setPosition(this.locX, this.locY, this.locZ);
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.e);
        nbttagcompound.setShort("yTile", (short) this.f);
        nbttagcompound.setShort("zTile", (short) this.g);
        nbttagcompound.setByte("inTile", (byte) this.h);
        nbttagcompound.setByte("inData", (byte) this.i);
        nbttagcompound.setByte("shake", (byte) this.shake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbttagcompound.setBoolean("player", this.fromPlayer);
        nbttagcompound.setDouble("damage", this.damage);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("xTile");
        this.f = nbttagcompound.getShort("yTile");
        this.g = nbttagcompound.getShort("zTile");
        this.h = nbttagcompound.getByte("inTile") & 255;
        this.i = nbttagcompound.getByte("inData") & 255;
        this.shake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        this.fromPlayer = nbttagcompound.getBoolean("player");
        if (nbttagcompound.hasKey("damage")) {
            this.damage = nbttagcompound.getDouble("damage");
        }
    }

    public void a_(EntityHuman entityhuman) {
        if (!this.world.isStatic) {
            // CraftBukkit start
            ItemStack itemstack = new ItemStack(Item.ARROW, 1);
            if (this.inGround && this.fromPlayer && this.shake <= 0 && entityhuman.inventory.canHold(itemstack) > 0) {
                net.minecraft.server.EntityItem item = new net.minecraft.server.EntityItem(this.world, this.locX, this.locY, this.locZ, itemstack);

                PlayerPickupItemEvent event = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), new org.bukkit.craftbukkit.entity.CraftItem(this.world.getServer(), item), 0);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            // CraftBukkit end

            if (this.inGround && this.fromPlayer && this.shake <= 0 && entityhuman.inventory.pickup(new ItemStack(Item.ARROW, 1))) {
                this.world.makeSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityhuman.receive(this, 1);
                this.die();
            }
        }
    }

    public void a(double d0) {
        this.damage = d0;
    }

    public double k() {
        return this.damage;
    }

    public void b(int i) {
        this.n = i;
    }

    public boolean k_() {
        return false;
    }
}
