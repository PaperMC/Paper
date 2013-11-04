package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
// CraftBukkit end

public class EntityArrow extends Entity implements IProjectile {

    private int d = -1;
    private int e = -1;
    private int f = -1;
    private Block g;
    private int h;
    private boolean inGround;
    public int fromPlayer;
    public int shake;
    public Entity shooter;
    private int j;
    private int au;
    private double damage = 2.0D;
    private int aw;

    public EntityArrow(World world) {
        super(world);
        this.k = 10.0D;
        this.a(0.5F, 0.5F);
    }

    public EntityArrow(World world, double d0, double d1, double d2) {
        super(world);
        this.k = 10.0D;
        this.a(0.5F, 0.5F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
    }

    public EntityArrow(World world, EntityLiving entityliving, EntityLiving entityliving1, float f, float f1) {
        super(world);
        this.k = 10.0D;
        this.shooter = entityliving;
        if (entityliving instanceof EntityHuman) {
            this.fromPlayer = 1;
        }

        this.locY = entityliving.locY + (double) entityliving.getHeadHeight() - 0.10000000149011612D;
        double d0 = entityliving1.locX - entityliving.locX;
        double d1 = entityliving1.boundingBox.b + (double) (entityliving1.length / 3.0F) - this.locY;
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
        this.k = 10.0D;
        this.shooter = entityliving;
        if (entityliving instanceof EntityHuman) {
            this.fromPlayer = 1;
        }

        this.a(0.5F, 0.5F);
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

    protected void c() {
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.random.nextGaussian() * (double) (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) f1;
        d1 += this.random.nextGaussian() * (double) (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) f1;
        d2 += this.random.nextGaussian() * (double) (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.motX = d0;
        this.motY = d1;
        this.motZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.lastYaw = this.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        this.lastPitch = this.pitch = (float) (Math.atan2(d1, (double) f3) * 180.0D / 3.1415927410125732D);
        this.j = 0;
    }

    public void h() {
        super.h();
        if (this.lastPitch == 0.0F && this.lastYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

            this.lastYaw = this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);
            this.lastPitch = this.pitch = (float) (Math.atan2(this.motY, (double) f) * 180.0D / 3.1415927410125732D);
        }

        Block block = this.world.getType(this.d, this.e, this.f);

        if (block.getMaterial() != Material.AIR) {
            block.updateShape(this.world, this.d, this.e, this.f);
            AxisAlignedBB axisalignedbb = block.a(this.world, this.d, this.e, this.f);

            if (axisalignedbb != null && axisalignedbb.a(this.world.getVec3DPool().create(this.locX, this.locY, this.locZ))) {
                this.inGround = true;
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            int i = this.world.getData(this.d, this.e, this.f);

            if (block == this.g && i == this.h) {
                ++this.j;
                if (this.j == 1200) {
                    this.die();
                }
            } else {
                this.inGround = false;
                this.motX *= (double) (this.random.nextFloat() * 0.2F);
                this.motY *= (double) (this.random.nextFloat() * 0.2F);
                this.motZ *= (double) (this.random.nextFloat() * 0.2F);
                this.j = 0;
                this.au = 0;
            }
        } else {
            ++this.au;
            Vec3D vec3d = this.world.getVec3DPool().create(this.locX, this.locY, this.locZ);
            Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1, false, true, false);

            vec3d = this.world.getVec3DPool().create(this.locX, this.locY, this.locZ);
            vec3d1 = this.world.getVec3DPool().create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            if (movingobjectposition != null) {
                vec3d1 = this.world.getVec3DPool().create(movingobjectposition.pos.c, movingobjectposition.pos.d, movingobjectposition.pos.e);
            }

            Entity entity = null;
            List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            int j;
            float f1;

            for (j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.R() && (entity1 != this.shooter || this.au >= 5)) {
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

            if (movingobjectposition != null && movingobjectposition.entity != null && movingobjectposition.entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) movingobjectposition.entity;

                if (entityhuman.abilities.isInvulnerable || this.shooter instanceof EntityHuman && !((EntityHuman) this.shooter).a(entityhuman)) {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f3;

            if (movingobjectposition != null) {
                org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this); // CraftBukkit - Call event

                if (movingobjectposition.entity != null) {
                    f2 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
                    int k = MathHelper.f((double) f2 * this.damage);

                    if (this.f()) {
                        k += this.random.nextInt(k / 2 + 2);
                    }

                    DamageSource damagesource = null;

                    if (this.shooter == null) {
                        damagesource = DamageSource.arrow(this, this);
                    } else {
                        damagesource = DamageSource.arrow(this, this.shooter);
                    }

                    // CraftBukkit start - Moved damage call
                    if (movingobjectposition.entity.damageEntity(damagesource, k)) {
                    if (this.isBurning() && !(movingobjectposition.entity instanceof EntityEnderman) && (!(movingobjectposition.entity instanceof EntityPlayer) || !(this.shooter instanceof EntityPlayer) || this.world.pvpMode)) { // CraftBukkit - abide by pvp setting if destination is a player
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
                        org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            movingobjectposition.entity.setOnFire(combustEvent.getDuration());
                        }
                        // CraftBukkit end
                    }

                    // if (movingobjectposition.entity.damageEntity(damagesource, (float) k)) { // CraftBukkit - moved up
                        if (movingobjectposition.entity instanceof EntityLiving) {
                            EntityLiving entityliving = (EntityLiving) movingobjectposition.entity;

                            if (!this.world.isStatic) {
                                entityliving.p(entityliving.aZ() + 1);
                            }

                            if (this.aw > 0) {
                                f3 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
                                if (f3 > 0.0F) {
                                    movingobjectposition.entity.g(this.motX * (double) this.aw * 0.6000000238418579D / (double) f3, 0.1D, this.motZ * (double) this.aw * 0.6000000238418579D / (double) f3);
                                }
                            }

                            if (this.shooter != null && this.shooter instanceof EntityLiving) {
                                EnchantmentManager.a(entityliving, this.shooter);
                                EnchantmentManager.b((EntityLiving) this.shooter, entityliving);
                            }

                            if (this.shooter != null && movingobjectposition.entity != this.shooter && movingobjectposition.entity instanceof EntityHuman && this.shooter instanceof EntityPlayer) {
                                ((EntityPlayer) this.shooter).playerConnection.sendPacket(new PacketPlayOutGameStateChange(6, 0.0F));
                            }
                        }

                        this.makeSound("random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                        if (!(movingobjectposition.entity instanceof EntityEnderman)) {
                            this.die();
                        }
                    } else {
                        this.motX *= -0.10000000149011612D;
                        this.motY *= -0.10000000149011612D;
                        this.motZ *= -0.10000000149011612D;
                        this.yaw += 180.0F;
                        this.lastYaw += 180.0F;
                        this.au = 0;
                    }
                } else {
                    this.d = movingobjectposition.b;
                    this.e = movingobjectposition.c;
                    this.f = movingobjectposition.d;
                    this.g = block;
                    this.h = this.world.getData(this.d, this.e, this.f);
                    this.motX = (double) ((float) (movingobjectposition.pos.c - this.locX));
                    this.motY = (double) ((float) (movingobjectposition.pos.d - this.locY));
                    this.motZ = (double) ((float) (movingobjectposition.pos.e - this.locZ));
                    f2 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
                    this.locX -= this.motX / (double) f2 * 0.05000000074505806D;
                    this.locY -= this.motY / (double) f2 * 0.05000000074505806D;
                    this.locZ -= this.motZ / (double) f2 * 0.05000000074505806D;
                    this.makeSound("random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.shake = 7;
                    this.a(false);
                    if (this.g.getMaterial() != Material.AIR) {
                        this.g.a(this.world, this.d, this.e, this.f, (Entity) this);
                    }
                }
            }

            if (this.f()) {
                for (j = 0; j < 4; ++j) {
                    this.world.addParticle("crit", this.locX + this.motX * (double) j / 4.0D, this.locY + this.motY * (double) j / 4.0D, this.locZ + this.motZ * (double) j / 4.0D, -this.motX, -this.motY + 0.2D, -this.motZ);
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
            if (this.M()) {
                for (int l = 0; l < 4; ++l) {
                    f3 = 0.25F;
                    this.world.addParticle("bubble", this.locX - this.motX * (double) f3, this.locY - this.motY * (double) f3, this.locZ - this.motZ * (double) f3, this.motX, this.motY, this.motZ);
                }

                f4 = 0.8F;
            }

            if (this.L()) {
                this.extinguish();
            }

            this.motX *= (double) f4;
            this.motY *= (double) f4;
            this.motZ *= (double) f4;
            this.motY -= (double) f1;
            this.setPosition(this.locX, this.locY, this.locZ);
            this.I();
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.d);
        nbttagcompound.setShort("yTile", (short) this.e);
        nbttagcompound.setShort("zTile", (short) this.f);
        nbttagcompound.setShort("life", (short) this.j);
        nbttagcompound.setByte("inTile", (byte) Block.b(this.g));
        nbttagcompound.setByte("inData", (byte) this.h);
        nbttagcompound.setByte("shake", (byte) this.shake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbttagcompound.setByte("pickup", (byte) this.fromPlayer);
        nbttagcompound.setDouble("damage", this.damage);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("xTile");
        this.e = nbttagcompound.getShort("yTile");
        this.f = nbttagcompound.getShort("zTile");
        this.j = nbttagcompound.getShort("life");
        this.g = Block.e(nbttagcompound.getByte("inTile") & 255);
        this.h = nbttagcompound.getByte("inData") & 255;
        this.shake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        if (nbttagcompound.hasKeyOfType("damage", 99)) {
            this.damage = nbttagcompound.getDouble("damage");
        }

        if (nbttagcompound.hasKeyOfType("pickup", 99)) {
            this.fromPlayer = nbttagcompound.getByte("pickup");
        } else if (nbttagcompound.hasKeyOfType("player", 99)) {
            this.fromPlayer = nbttagcompound.getBoolean("player") ? 1 : 0;
        }
    }

    public void b_(EntityHuman entityhuman) {
        if (!this.world.isStatic && this.inGround && this.shake <= 0) {
            // CraftBukkit start
            ItemStack itemstack = new ItemStack(Items.ARROW);
            if (this.fromPlayer == 1 && entityhuman.inventory.canHold(itemstack) > 0) {
                EntityItem item = new EntityItem(this.world, this.locX, this.locY, this.locZ, itemstack);

                PlayerPickupItemEvent event = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), new org.bukkit.craftbukkit.entity.CraftItem(this.world.getServer(), this, item), 0);
                // event.setCancelled(!entityhuman.canPickUpLoot); TODO
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            // CraftBukkit end

            boolean flag = this.fromPlayer == 1 || this.fromPlayer == 2 && entityhuman.abilities.canInstantlyBuild;

            if (this.fromPlayer == 1 && !entityhuman.inventory.pickup(new ItemStack(Items.ARROW, 1))) {
                flag = false;
            }

            if (flag) {
                this.makeSound("random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityhuman.receive(this, 1);
                this.die();
            }
        }
    }

    protected boolean g_() {
        return false;
    }

    public void b(double d0) {
        this.damage = d0;
    }

    public double e() {
        return this.damage;
    }

    public void a(int i) {
        this.aw = i;
    }

    public boolean av() {
        return false;
    }

    public void a(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public boolean f() {
        byte b0 = this.datawatcher.getByte(16);

        return (b0 & 1) != 0;
    }

    // CraftBukkit start
    public boolean isInGround() {
        return inGround;
    }
    // CraftBukkit end
}
