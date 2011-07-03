package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
// CraftBukkit end

public class EntityFish extends Entity {

    private int d = -1;
    private int e = -1;
    private int f = -1;
    private int g = 0;
    private boolean h = false;
    public int a = 0;
    public EntityHuman owner;
    private int i;
    private int j = 0;
    private int k = 0;
    public Entity c = null;
    private int l;
    private double m;
    private double n;
    private double o;
    private double p;
    private double q;

    public EntityFish(World world) {
        super(world);
        this.b(0.25F, 0.25F);
        this.bK = true;
    }

    public EntityFish(World world, EntityHuman entityhuman) {
        super(world);
        this.bK = true;
        this.owner = entityhuman;
        this.owner.hookedFish = this;
        this.b(0.25F, 0.25F);
        this.setPositionRotation(entityhuman.locX, entityhuman.locY + 1.62D - (double) entityhuman.height, entityhuman.locZ, entityhuman.yaw, entityhuman.pitch);
        this.locX -= (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.locY -= 0.10000000149011612D;
        this.locZ -= (double) (MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        float f = 0.4F;

        this.motX = (double) (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
        this.motZ = (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
        this.motY = (double) (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * f);
        this.a(this.motX, this.motY, this.motZ, 1.5F, 1.0F);
    }

    protected void b() {}

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
        this.i = 0;
    }

    public void m_() {
        super.m_();
        if (this.l > 0) {
            double d0 = this.locX + (this.m - this.locX) / (double) this.l;
            double d1 = this.locY + (this.n - this.locY) / (double) this.l;
            double d2 = this.locZ + (this.o - this.locZ) / (double) this.l;

            double d3;

            for (d3 = this.p - (double) this.yaw; d3 < -180.0D; d3 += 360.0D) {
                ;
            }

            while (d3 >= 180.0D) {
                d3 -= 360.0D;
            }

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.l);
            this.pitch = (float) ((double) this.pitch + (this.q - (double) this.pitch) / (double) this.l);
            --this.l;
            this.setPosition(d0, d1, d2);
            this.c(this.yaw, this.pitch);
        } else {
            if (!this.world.isStatic) {
                ItemStack itemstack = this.owner.G();

                if (this.owner.dead || !this.owner.T() || itemstack == null || itemstack.getItem() != Item.FISHING_ROD || this.g(this.owner) > 1024.0D) {
                    this.die();
                    this.owner.hookedFish = null;
                    return;
                }

                if (this.c != null) {
                    if (!this.c.dead) {
                        this.locX = this.c.locX;
                        this.locY = this.c.boundingBox.b + (double) this.c.width * 0.8D;
                        this.locZ = this.c.locZ;
                        return;
                    }

                    this.c = null;
                }
            }

            if (this.a > 0) {
                --this.a;
            }

            if (this.h) {
                int i = this.world.getTypeId(this.d, this.e, this.f);

                if (i == this.g) {
                    ++this.i;
                    if (this.i == 1200) {
                        this.die();
                    }

                    return;
                }

                this.h = false;
                this.motX *= (double) (this.random.nextFloat() * 0.2F);
                this.motY *= (double) (this.random.nextFloat() * 0.2F);
                this.motZ *= (double) (this.random.nextFloat() * 0.2F);
                this.i = 0;
                this.j = 0;
            } else {
                ++this.j;
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
            double d4 = 0.0D;

            double d5;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.l_() && (entity1 != this.owner || this.j >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.b((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        d5 = vec3d.a(movingobjectposition1.f);
                        if (d5 < d4 || d4 == 0.0D) {
                            entity = entity1;
                            d4 = d5;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null) {
                if (movingobjectposition.entity != null) {
                    // CraftBukkit start
                    // TODO add EntityDamagedByProjectileEvent : fishing hook?
                    boolean stick;
                    if (movingobjectposition.entity instanceof EntityLiving) {
                        org.bukkit.entity.Entity damagee = movingobjectposition.entity.getBukkitEntity();
                        Projectile projectile = (Projectile) this.getBukkitEntity();

                        // TODO @see EntityArrow#162
                        EntityDamageByProjectileEvent event = new EntityDamageByProjectileEvent(damagee, projectile, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);
                        this.world.getServer().getPluginManager().callEvent(event);

                        if (event.isCancelled()) {
                            stick = !event.getBounce();
                        } else {
                            // this function returns if the fish should stick in or not, i.e. !bounce
                            stick = movingobjectposition.entity.damageEntity(this.owner, event.getDamage());
                        }
                    } else {
                        stick = movingobjectposition.entity.damageEntity(this.owner, 0);
                    }
                    if (!stick) {
                        // CraftBukkit end
                        this.c = movingobjectposition.entity;
                    }
                } else {
                    this.h = true;
                }
            }

            if (!this.h) {
                this.move(this.motX, this.motY, this.motZ);
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
                float f2 = 0.92F;

                if (this.onGround || this.positionChanged) {
                    f2 = 0.5F;
                }

                byte b0 = 5;
                double d6 = 0.0D;

                for (int k = 0; k < b0; ++k) {
                    double d7 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (k + 0) / (double) b0 - 0.125D + 0.125D;
                    double d8 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (k + 1) / (double) b0 - 0.125D + 0.125D;
                    AxisAlignedBB axisalignedbb1 = AxisAlignedBB.b(this.boundingBox.a, d7, this.boundingBox.c, this.boundingBox.d, d8, this.boundingBox.f);

                    if (this.world.b(axisalignedbb1, Material.WATER)) {
                        d6 += 1.0D / (double) b0;
                    }
                }

                if (d6 > 0.0D) {
                    if (this.k > 0) {
                        --this.k;
                    } else {
                        short short1 = 500;

                        if (this.world.s(MathHelper.floor(this.locX), MathHelper.floor(this.locY) + 1, MathHelper.floor(this.locZ))) {
                            short1 = 300;
                        }

                        if (this.random.nextInt(short1) == 0) {
                            this.k = this.random.nextInt(30) + 10;
                            this.motY -= 0.20000000298023224D;
                            this.world.makeSound(this, "random.splash", 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                            float f3 = (float) MathHelper.floor(this.boundingBox.b);

                            float f4;
                            int l;
                            float f5;

                            for (l = 0; (float) l < 1.0F + this.length * 20.0F; ++l) {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                                this.world.a("bubble", this.locX + (double) f5, (double) (f3 + 1.0F), this.locZ + (double) f4, this.motX, this.motY - (double) (this.random.nextFloat() * 0.2F), this.motZ);
                            }

                            for (l = 0; (float) l < 1.0F + this.length * 20.0F; ++l) {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                                this.world.a("splash", this.locX + (double) f5, (double) (f3 + 1.0F), this.locZ + (double) f4, this.motX, this.motY, this.motZ);
                            }
                        }
                    }
                }

                if (this.k > 0) {
                    this.motY -= (double) (this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat()) * 0.2D;
                }

                d5 = d6 * 2.0D - 1.0D;
                this.motY += 0.03999999910593033D * d5;
                if (d6 > 0.0D) {
                    f2 = (float) ((double) f2 * 0.9D);
                    this.motY *= 0.8D;
                }

                this.motX *= (double) f2;
                this.motY *= (double) f2;
                this.motZ *= (double) f2;
                this.setPosition(this.locX, this.locY, this.locZ);
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) this.d);
        nbttagcompound.a("yTile", (short) this.e);
        nbttagcompound.a("zTile", (short) this.f);
        nbttagcompound.a("inTile", (byte) this.g);
        nbttagcompound.a("shake", (byte) this.a);
        nbttagcompound.a("inGround", (byte) (this.h ? 1 : 0));
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.d("xTile");
        this.e = nbttagcompound.d("yTile");
        this.f = nbttagcompound.d("zTile");
        this.g = nbttagcompound.c("inTile") & 255;
        this.a = nbttagcompound.c("shake") & 255;
        this.h = nbttagcompound.c("inGround") == 1;
    }

    public int h() {
        byte b0 = 0;

        if (this.c != null) {
            // CraftBukkit start
            PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) this.owner.getBukkitEntity(), this.c.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_ENTITY);
            this.world.getServer().getPluginManager().callEvent(playerFishEvent);

            if (playerFishEvent.isCancelled()) {
                this.die();
                this.owner.hookedFish = null;
                return 0;
            }
            // CraftBukkit end
            double d0 = this.owner.locX - this.locX;
            double d1 = this.owner.locY - this.locY;
            double d2 = this.owner.locZ - this.locZ;
            double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);
            double d4 = 0.1D;

            this.c.motX += d0 * d4;
            this.c.motY += d1 * d4 + (double) MathHelper.a(d3) * 0.08D;
            this.c.motZ += d2 * d4;
            b0 = 3;
        } else if (this.k > 0) {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.RAW_FISH));
            // CraftBukkit start
            PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) this.owner.getBukkitEntity(), entityitem.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_FISH);
            this.world.getServer().getPluginManager().callEvent(playerFishEvent);

            if (playerFishEvent.isCancelled()) {
                this.die();
                this.owner.hookedFish = null;
                return 0;
            }
            // CraftBukkit end
            double d5 = this.owner.locX - this.locX;
            double d6 = this.owner.locY - this.locY;
            double d7 = this.owner.locZ - this.locZ;
            double d8 = (double) MathHelper.a(d5 * d5 + d6 * d6 + d7 * d7);
            double d9 = 0.1D;

            entityitem.motX = d5 * d9;
            entityitem.motY = d6 * d9 + (double) MathHelper.a(d8) * 0.08D;
            entityitem.motZ = d7 * d9;
            this.world.addEntity(entityitem);
            this.owner.a(StatisticList.B, 1);
            b0 = 1;
        }

        if (this.h) {
            // CraftBukkit start
            PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) this.owner.getBukkitEntity(), null, PlayerFishEvent.State.IN_GROUND);
            this.world.getServer().getPluginManager().callEvent(playerFishEvent);

            if (playerFishEvent.isCancelled()) {
                this.die();
                this.owner.hookedFish = null;
                return 0;
            }
            // CraftBukkit end
            b0 = 2;
        }

        // CraftBukkit start
        if (b0 == 0) {
            PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) this.owner.getBukkitEntity(), null, PlayerFishEvent.State.FAILED_ATTEMPT);
            this.world.getServer().getPluginManager().callEvent(playerFishEvent);
        }
        // CraftBukkit end
        this.die();
        this.owner.hookedFish = null;
        return b0;
    }
}
