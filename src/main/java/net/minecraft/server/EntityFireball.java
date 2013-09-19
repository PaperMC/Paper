package net.minecraft.server;

import java.util.List;

public abstract class EntityFireball extends Entity {

    private int e = -1;
    private int f = -1;
    private int g = -1;
    private int h;
    private boolean i;
    public EntityLiving shooter;
    private int j;
    private int au;
    public double dirX;
    public double dirY;
    public double dirZ;
    public float bukkitYield = 1; // CraftBukkit
    public boolean isIncendiary = true; // CraftBukkit

    public EntityFireball(World world) {
        super(world);
        this.a(1.0F, 1.0F);
    }

    protected void a() {}

    public EntityFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world);
        this.a(1.0F, 1.0F);
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
        this.setPosition(d0, d1, d2);
        double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

        this.dirX = d3 / d6 * 0.1D;
        this.dirY = d4 / d6 * 0.1D;
        this.dirZ = d5 / d6 * 0.1D;
    }

    public EntityFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world);
        this.shooter = entityliving;
        this.a(1.0F, 1.0F);
        this.setPositionRotation(entityliving.locX, entityliving.locY, entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        this.motX = this.motY = this.motZ = 0.0D;
        // CraftBukkit start - Added setDirection method
        this.setDirection(d0, d1, d2);
    }

    public void setDirection(double d0, double d1, double d2) {
        // CraftBukkit end
        d0 += this.random.nextGaussian() * 0.4D;
        d1 += this.random.nextGaussian() * 0.4D;
        d2 += this.random.nextGaussian() * 0.4D;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        this.dirX = d0 / d3 * 0.1D;
        this.dirY = d1 / d3 * 0.1D;
        this.dirZ = d2 / d3 * 0.1D;
    }

    public void l_() {
        if (!this.world.isStatic && (this.shooter != null && this.shooter.dead || !this.world.isLoaded((int) this.locX, (int) this.locY, (int) this.locZ))) {
            this.die();
        } else {
            super.l_();
            this.setOnFire(1);
            if (this.i) {
                int i = this.world.getTypeId(this.e, this.f, this.g);

                if (i == this.h) {
                    ++this.j;
                    if (this.j == 600) {
                        this.die();
                    }

                    return;
                }

                this.i = false;
                this.motX *= (double) (this.random.nextFloat() * 0.2F);
                this.motY *= (double) (this.random.nextFloat() * 0.2F);
                this.motZ *= (double) (this.random.nextFloat() * 0.2F);
                this.j = 0;
                this.au = 0;
            } else {
                ++this.au;
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
            double d0 = 0.0D;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.L() && (!entity1.h(this.shooter) || this.au >= 25)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.grow((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

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

            if (movingobjectposition != null) {
                this.a(movingobjectposition);

                // CraftBukkit start
                if (this.dead) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this);
                }
                // CraftBukkit end
            }

            this.locX += this.motX;
            this.locY += this.motY;
            this.locZ += this.motZ;
            float f1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

            this.yaw = (float) (Math.atan2(this.motZ, this.motX) * 180.0D / 3.1415927410125732D) + 90.0F;

            for (this.pitch = (float) (Math.atan2((double) f1, this.motY) * 180.0D / 3.1415927410125732D) - 90.0F; this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
            float f2 = this.c();

            if (this.H()) {
                for (int k = 0; k < 4; ++k) {
                    float f3 = 0.25F;

                    this.world.addParticle("bubble", this.locX - this.motX * (double) f3, this.locY - this.motY * (double) f3, this.locZ - this.motZ * (double) f3, this.motX, this.motY, this.motZ);
                }

                f2 = 0.8F;
            }

            this.motX += this.dirX;
            this.motY += this.dirY;
            this.motZ += this.dirZ;
            this.motX *= (double) f2;
            this.motY *= (double) f2;
            this.motZ *= (double) f2;
            this.world.addParticle("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.locX, this.locY, this.locZ);
        }
    }

    protected float c() {
        return 0.95F;
    }

    protected abstract void a(MovingObjectPosition movingobjectposition);

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.e);
        nbttagcompound.setShort("yTile", (short) this.f);
        nbttagcompound.setShort("zTile", (short) this.g);
        nbttagcompound.setByte("inTile", (byte) this.h);
        nbttagcompound.setByte("inGround", (byte) (this.i ? 1 : 0));
        // CraftBukkit - Fix direction being mismapped to invalid variables
        nbttagcompound.set("power", this.a(new double[] { this.dirX, this.dirY, this.dirZ}));
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("xTile");
        this.f = nbttagcompound.getShort("yTile");
        this.g = nbttagcompound.getShort("zTile");
        this.h = nbttagcompound.getByte("inTile") & 255;
        this.i = nbttagcompound.getByte("inGround") == 1;
        // CraftBukkit start - direction -> power
        if (nbttagcompound.hasKey("power")) {
            NBTTagList nbttaglist = nbttagcompound.getList("power");

            this.dirX = ((NBTTagDouble) nbttaglist.get(0)).data;
            this.dirY = ((NBTTagDouble) nbttaglist.get(1)).data;
            this.dirZ = ((NBTTagDouble) nbttaglist.get(2)).data;
            // CraftBukkit end
        } else {
            this.die();
        }
    }

    public boolean L() {
        return true;
    }

    public float Z() {
        return 1.0F;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            this.K();
            if (damagesource.getEntity() != null) {
                Vec3D vec3d = damagesource.getEntity().aa();

                if (vec3d != null) {
                    this.motX = vec3d.c;
                    this.motY = vec3d.d;
                    this.motZ = vec3d.e;
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
    }

    public float d(float f) {
        return 1.0F;
    }
}
