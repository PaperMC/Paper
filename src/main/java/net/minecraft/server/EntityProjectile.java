package net.minecraft.server;

import java.util.List;

public abstract class EntityProjectile extends Entity implements IProjectile {

    private int blockX = -1;
    private int blockY = -1;
    private int blockZ = -1;
    private Block inBlockId;
    protected boolean inGround;
    public int shake;
    public EntityLiving shooter; // CraftBukkit - private -> public
    public String shooterName; // CraftBukkit - private -> public
    private int i;
    private int j;

    public EntityProjectile(World world) {
        super(world);
        this.a(0.25F, 0.25F);
    }

    protected void c() {}

    public EntityProjectile(World world, EntityLiving entityliving) {
        super(world);
        this.shooter = entityliving;
        this.a(0.25F, 0.25F);
        this.setPositionRotation(entityliving.locX, entityliving.locY + (double) entityliving.getHeadHeight(), entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.locX -= (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.locY -= 0.10000000149011612D;
        this.locZ -= (double) (MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        float f = 0.4F;

        this.motX = (double) (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
        this.motZ = (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
        this.motY = (double) (-MathHelper.sin((this.pitch + this.f()) / 180.0F * 3.1415927F) * f);
        this.shoot(this.motX, this.motY, this.motZ, this.e(), 1.0F);
    }

    public EntityProjectile(World world, double d0, double d1, double d2) {
        super(world);
        this.i = 0;
        this.a(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
    }

    protected float e() {
        return 1.5F;
    }

    protected float f() {
        return 0.0F;
    }

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
        this.i = 0;
    }

    public void h() {
        this.T = this.locX;
        this.U = this.locY;
        this.V = this.locZ;
        super.h();
        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            if (this.world.getType(this.blockX, this.blockY, this.blockZ) == this.inBlockId) {
                ++this.i;
                if (this.i == 1200) {
                    this.die();
                }

                return;
            }

            this.inGround = false;
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

        if (!this.world.isStatic) {
            Entity entity = null;
            List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityLiving entityliving = this.getShooter();

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.R() && (entity1 != entityliving || this.j >= 5)) {
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
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.type == EnumMovingObjectType.BLOCK && this.world.getType(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d) == Blocks.PORTAL) {
                this.ah();
            } else {
                this.a(movingobjectposition);
                // CraftBukkit start
                if (this.dead) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this);
                }
                // CraftBukkit end
            }
        }

        this.locX += this.motX;
        this.locY += this.motY;
        this.locZ += this.motZ;
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
        float f2 = 0.99F;
        float f3 = this.i();

        if (this.M()) {
            for (int j = 0; j < 4; ++j) {
                float f4 = 0.25F;

                this.world.addParticle("bubble", this.locX - this.motX * (double) f4, this.locY - this.motY * (double) f4, this.locZ - this.motZ * (double) f4, this.motX, this.motY, this.motZ);
            }

            f2 = 0.8F;
        }

        this.motX *= (double) f2;
        this.motY *= (double) f2;
        this.motZ *= (double) f2;
        this.motY -= (double) f3;
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    protected float i() {
        return 0.03F;
    }

    protected abstract void a(MovingObjectPosition movingobjectposition);

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.blockX);
        nbttagcompound.setShort("yTile", (short) this.blockY);
        nbttagcompound.setShort("zTile", (short) this.blockZ);
        nbttagcompound.setByte("inTile", (byte) Block.b(this.inBlockId));
        nbttagcompound.setByte("shake", (byte) this.shake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        if ((this.shooterName == null || this.shooterName.length() == 0) && this.shooter != null && this.shooter instanceof EntityHuman) {
            this.shooterName = this.shooter.getName();
        }

        nbttagcompound.setString("ownerName", this.shooterName == null ? "" : this.shooterName);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.blockX = nbttagcompound.getShort("xTile");
        this.blockY = nbttagcompound.getShort("yTile");
        this.blockZ = nbttagcompound.getShort("zTile");
        this.inBlockId = Block.e(nbttagcompound.getByte("inTile") & 255);
        this.shake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        this.shooterName = nbttagcompound.getString("ownerName");
        if (this.shooterName != null && this.shooterName.length() == 0) {
            this.shooterName = null;
        }
    }

    public EntityLiving getShooter() {
        if (this.shooter == null && this.shooterName != null && this.shooterName.length() > 0) {
            this.shooter = this.world.a(this.shooterName);
        }

        return this.shooter;
    }
}
