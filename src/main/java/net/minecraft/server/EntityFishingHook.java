package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.entity.Fish;
import org.bukkit.event.player.PlayerFishEvent;
// CraftBukkit end

public class EntityFishingHook extends Entity {

    private int d = -1;
    private int e = -1;
    private int f = -1;
    private int g;
    private boolean h;
    public int a;
    public EntityHuman owner;
    private int i;
    private int j;
    private int au;
    public Entity hooked;
    private int av;
    private double aw;
    private double ax;
    private double ay;
    private double az;
    private double aA;

    public EntityFishingHook(World world) {
        super(world);
        this.a(0.25F, 0.25F);
        this.am = true;
    }

    public EntityFishingHook(World world, EntityHuman entityhuman) {
        super(world);
        this.am = true;
        this.owner = entityhuman;
        this.owner.hookedFish = this;
        this.a(0.25F, 0.25F);
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
        this.c(this.motX, this.motY, this.motZ, 1.5F, 1.0F);
    }

    protected void a() {}

    public void c(double d0, double d1, double d2, float f, float f1) {
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
        this.i = 0;
    }

    public void l_() {
        super.l_();
        if (this.av > 0) {
            double d0 = this.locX + (this.aw - this.locX) / (double) this.av;
            double d1 = this.locY + (this.ax - this.locY) / (double) this.av;
            double d2 = this.locZ + (this.ay - this.locZ) / (double) this.av;
            double d3 = MathHelper.g(this.az - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.av);
            this.pitch = (float) ((double) this.pitch + (this.aA - (double) this.pitch) / (double) this.av);
            --this.av;
            this.setPosition(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        } else {
            if (!this.world.isStatic) {
                ItemStack itemstack = this.owner.by();

                if (this.owner.dead || !this.owner.isAlive() || itemstack == null || itemstack.getItem() != Item.FISHING_ROD || this.e(this.owner) > 1024.0D) {
                    this.die();
                    this.owner.hookedFish = null;
                    return;
                }

                if (this.hooked != null) {
                    if (!this.hooked.dead) {
                        this.locX = this.hooked.locX;
                        this.locY = this.hooked.boundingBox.b + (double) this.hooked.length * 0.8D;
                        this.locZ = this.hooked.locZ;
                        return;
                    }

                    this.hooked = null;
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

            Vec3D vec3d = this.world.getVec3DPool().create(this.locX, this.locY, this.locZ);
            Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

            vec3d = this.world.getVec3DPool().create(this.locX, this.locY, this.locZ);
            vec3d1 = this.world.getVec3DPool().create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            if (movingobjectposition != null) {
                vec3d1 = this.world.getVec3DPool().create(movingobjectposition.pos.c, movingobjectposition.pos.d, movingobjectposition.pos.e);
            }

            Entity entity = null;
            List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
            double d4 = 0.0D;

            double d5;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.L() && (entity1 != this.owner || this.j >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.grow((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        d5 = vec3d.distanceSquared(movingobjectposition1.pos); // CraftBukkit - distance efficiency
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
                org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this); // Craftbukkit - Call event
                if (movingobjectposition.entity != null) {
                    if (movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.owner), 0.0F)) {
                        this.hooked = movingobjectposition.entity;
                    }
                } else {
                    this.h = true;
                }
            }

            if (!this.h) {
                this.move(this.motX, this.motY, this.motZ);
                float f1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

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
                    AxisAlignedBB axisalignedbb1 = AxisAlignedBB.a().a(this.boundingBox.a, d7, this.boundingBox.c, this.boundingBox.d, d8, this.boundingBox.f);

                    if (this.world.b(axisalignedbb1, Material.WATER)) {
                        d6 += 1.0D / (double) b0;
                    }
                }

                if (d6 > 0.0D) {
                    if (this.au > 0) {
                        --this.au;
                    } else {
                        if (random.nextDouble() < ((org.bukkit.entity.Fish) this.getBukkitEntity()).getBiteChance()) { // CraftBukkit - moved logic to CraftFish
                            this.au = this.random.nextInt(30) + 10;
                            this.motY -= 0.20000000298023224D;
                            this.makeSound("random.splash", 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                            float f3 = (float) MathHelper.floor(this.boundingBox.b);

                            float f4;
                            int l;
                            float f5;

                            for (l = 0; (float) l < 1.0F + this.width * 20.0F; ++l) {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                this.world.addParticle("bubble", this.locX + (double) f5, (double) (f3 + 1.0F), this.locZ + (double) f4, this.motX, this.motY - (double) (this.random.nextFloat() * 0.2F), this.motZ);
                            }

                            for (l = 0; (float) l < 1.0F + this.width * 20.0F; ++l) {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                this.world.addParticle("splash", this.locX + (double) f5, (double) (f3 + 1.0F), this.locZ + (double) f4, this.motX, this.motY, this.motZ);
                            }
                        }
                    }
                }

                if (this.au > 0) {
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
        nbttagcompound.setShort("xTile", (short) this.d);
        nbttagcompound.setShort("yTile", (short) this.e);
        nbttagcompound.setShort("zTile", (short) this.f);
        nbttagcompound.setByte("inTile", (byte) this.g);
        nbttagcompound.setByte("shake", (byte) this.a);
        nbttagcompound.setByte("inGround", (byte) (this.h ? 1 : 0));
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("xTile");
        this.e = nbttagcompound.getShort("yTile");
        this.f = nbttagcompound.getShort("zTile");
        this.g = nbttagcompound.getByte("inTile") & 255;
        this.a = nbttagcompound.getByte("shake") & 255;
        this.h = nbttagcompound.getByte("inGround") == 1;
    }

    public int c() {
        if (this.world.isStatic) {
            return 0;
        } else {
            byte b0 = 0;

            if (this.hooked != null) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.owner.getBukkitEntity(), this.hooked.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_ENTITY);
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
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                double d4 = 0.1D;

                this.hooked.motX += d0 * d4;
                this.hooked.motY += d1 * d4 + (double) MathHelper.sqrt(d3) * 0.08D;
                this.hooked.motZ += d2 * d4;
                b0 = 3;
            } else if (this.au > 0) {
                EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Item.RAW_FISH));
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.owner.getBukkitEntity(), entityitem.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_FISH);
                playerFishEvent.setExpToDrop(this.random.nextInt(6) + 1);
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
                double d8 = (double) MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
                double d9 = 0.1D;

                entityitem.motX = d5 * d9;
                entityitem.motY = d6 * d9 + (double) MathHelper.sqrt(d8) * 0.08D;
                entityitem.motZ = d7 * d9;
                this.world.addEntity(entityitem);
                this.owner.a(StatisticList.B, 1);
                // CraftBukkit - this.random.nextInt(6) + 1 -> playerFishEvent.getExpToDrop()
                this.owner.world.addEntity(new EntityExperienceOrb(this.owner.world, this.owner.locX, this.owner.locY + 0.5D, this.owner.locZ + 0.5D, playerFishEvent.getExpToDrop()));
                b0 = 1;
            }

            if (this.h) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.owner.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.IN_GROUND);
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
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.owner.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.FAILED_ATTEMPT);
                this.world.getServer().getPluginManager().callEvent(playerFishEvent);
            }
            // CraftBukkit end

            this.die();
            this.owner.hookedFish = null;
            return b0;
        }
    }

    public void die() {
        super.die();
        if (this.owner != null) {
            this.owner.hookedFish = null;
        }
    }
}
