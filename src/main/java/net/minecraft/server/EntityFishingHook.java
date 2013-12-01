package net.minecraft.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.entity.Fish;
import org.bukkit.event.player.PlayerFishEvent;
// CraftBukkit end

public class EntityFishingHook extends Entity {

    private static final List d = Arrays.asList(new PossibleFishingResult[] { (new PossibleFishingResult(new ItemStack(Items.LEATHER_BOOTS), 10)).a(0.9F), new PossibleFishingResult(new ItemStack(Items.LEATHER), 10), new PossibleFishingResult(new ItemStack(Items.BONE), 10), new PossibleFishingResult(new ItemStack(Items.POTION), 10), new PossibleFishingResult(new ItemStack(Items.STRING), 5), (new PossibleFishingResult(new ItemStack(Items.FISHING_ROD), 2)).a(0.9F), new PossibleFishingResult(new ItemStack(Items.BOWL), 10), new PossibleFishingResult(new ItemStack(Items.STICK), 5), new PossibleFishingResult(new ItemStack(Items.INK_SACK, 10, 0), 1), new PossibleFishingResult(new ItemStack(Blocks.TRIPWIRE_SOURCE), 10), new PossibleFishingResult(new ItemStack(Items.ROTTEN_FLESH), 10)});
    private static final List e = Arrays.asList(new PossibleFishingResult[] { new PossibleFishingResult(new ItemStack(Blocks.WATER_LILY), 1), new PossibleFishingResult(new ItemStack(Items.NAME_TAG), 1), new PossibleFishingResult(new ItemStack(Items.SADDLE), 1), (new PossibleFishingResult(new ItemStack(Items.BOW), 1)).a(0.25F).a(), (new PossibleFishingResult(new ItemStack(Items.FISHING_ROD), 1)).a(0.25F).a(), (new PossibleFishingResult(new ItemStack(Items.BOOK), 1)).a()});
    private static final List f = Arrays.asList(new PossibleFishingResult[] { new PossibleFishingResult(new ItemStack(Items.RAW_FISH, 1, EnumFish.COD.a()), 60), new PossibleFishingResult(new ItemStack(Items.RAW_FISH, 1, EnumFish.SALMON.a()), 25), new PossibleFishingResult(new ItemStack(Items.RAW_FISH, 1, EnumFish.CLOWNFISH.a()), 2), new PossibleFishingResult(new ItemStack(Items.RAW_FISH, 1, EnumFish.PUFFERFISH.a()), 13)});
    private int g = -1;
    private int h = -1;
    private int i = -1;
    private Block j;
    private boolean au;
    public int a;
    public EntityHuman owner;
    private int av;
    private int aw;
    private int ax;
    private int ay;
    private int az;
    private float aA;
    public Entity hooked;
    private int aB;
    private double aC;
    private double aD;
    private double aE;
    private double aF;
    private double aG;

    public EntityFishingHook(World world) {
        super(world);
        this.a(0.25F, 0.25F);
        this.al = true;
    }

    public EntityFishingHook(World world, EntityHuman entityhuman) {
        super(world);
        this.al = true;
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

    protected void c() {}

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
        this.av = 0;
    }

    public void h() {
        super.h();
        if (this.aB > 0) {
            double d0 = this.locX + (this.aC - this.locX) / (double) this.aB;
            double d1 = this.locY + (this.aD - this.locY) / (double) this.aB;
            double d2 = this.locZ + (this.aE - this.locZ) / (double) this.aB;
            double d3 = MathHelper.g(this.aF - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.aB);
            this.pitch = (float) ((double) this.pitch + (this.aG - (double) this.pitch) / (double) this.aB);
            --this.aB;
            this.setPosition(d0, d1, d2);
            this.b(this.yaw, this.pitch);
        } else {
            if (!this.world.isStatic) {
                ItemStack itemstack = this.owner.bD();

                if (this.owner.dead || !this.owner.isAlive() || itemstack == null || itemstack.getItem() != Items.FISHING_ROD || this.e(this.owner) > 1024.0D) {
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

            if (this.au) {
                if (this.world.getType(this.g, this.h, this.i) == this.j) {
                    ++this.av;
                    if (this.av == 1200) {
                        this.die();
                    }

                    return;
                }

                this.au = false;
                this.motX *= (double) (this.random.nextFloat() * 0.2F);
                this.motY *= (double) (this.random.nextFloat() * 0.2F);
                this.motZ *= (double) (this.random.nextFloat() * 0.2F);
                this.av = 0;
                this.aw = 0;
            } else {
                ++this.aw;
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

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.R() && (entity1 != this.owner || this.aw >= 5)) {
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
                    this.au = true;
                }
            }

            if (!this.au) {
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

                for (int j = 0; j < b0; ++j) {
                    double d7 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (j + 0) / (double) b0 - 0.125D + 0.125D;
                    double d8 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (j + 1) / (double) b0 - 0.125D + 0.125D;
                    AxisAlignedBB axisalignedbb1 = AxisAlignedBB.a().a(this.boundingBox.a, d7, this.boundingBox.c, this.boundingBox.d, d8, this.boundingBox.f);

                    if (this.world.b(axisalignedbb1, Material.WATER)) {
                        d6 += 1.0D / (double) b0;
                    }
                }

                if (!this.world.isStatic && d6 > 0.0D) {
                    WorldServer worldserver = (WorldServer) this.world;
                    int k = 1;

                    if (this.random.nextFloat() < 0.25F && this.world.isRainingAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY) + 1, MathHelper.floor(this.locZ))) {
                        k = 2;
                    }

                    if (this.random.nextFloat() < 0.5F && !this.world.i(MathHelper.floor(this.locX), MathHelper.floor(this.locY) + 1, MathHelper.floor(this.locZ))) {
                        --k;
                    }

                    if (this.ax > 0) {
                        --this.ax;
                        if (this.ax <= 0) {
                            this.ay = 0;
                            this.az = 0;
                        }
                    } else {
                        float f3;
                        double d9;
                        float f4;
                        float f5;
                        double d10;
                        double d11;

                        if (this.az > 0) {
                            this.az -= k;
                            if (this.az <= 0) {
                                this.motY -= 0.20000000298023224D;
                                this.makeSound("random.splash", 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                                f4 = (float) MathHelper.floor(this.boundingBox.b);
                                worldserver.a("bubble", this.locX, (double) (f4 + 1.0F), this.locZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D);
                                worldserver.a("wake", this.locX, (double) (f4 + 1.0F), this.locZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D);
                                this.ax = MathHelper.nextInt(this.random, 10, 30);
                            } else {
                                this.aA = (float) ((double) this.aA + this.random.nextGaussian() * 4.0D);
                                f4 = this.aA * 0.017453292F;
                                f5 = MathHelper.sin(f4);
                                f3 = MathHelper.cos(f4);
                                d9 = this.locX + (double) (f5 * (float) this.az * 0.1F);
                                d11 = (double) ((float) MathHelper.floor(this.boundingBox.b) + 1.0F);
                                d10 = this.locZ + (double) (f3 * (float) this.az * 0.1F);
                                if (this.random.nextFloat() < 0.15F) {
                                    worldserver.a("bubble", d9, d11 - 0.10000000149011612D, d10, 1, (double) f5, 0.1D, (double) f3, 0.0D);
                                }

                                float f6 = f5 * 0.04F;
                                float f7 = f3 * 0.04F;

                                worldserver.a("wake", d9, d11, d10, 0, (double) f7, 0.01D, (double) (-f6), 1.0D);
                                worldserver.a("wake", d9, d11, d10, 0, (double) (-f7), 0.01D, (double) f6, 1.0D);
                            }
                        } else if (this.ay > 0) {
                            this.ay -= k;
                            f4 = 0.15F;
                            if (this.ay < 20) {
                                f4 = (float) ((double) f4 + (double) (20 - this.ay) * 0.05D);
                            } else if (this.ay < 40) {
                                f4 = (float) ((double) f4 + (double) (40 - this.ay) * 0.02D);
                            } else if (this.ay < 60) {
                                f4 = (float) ((double) f4 + (double) (60 - this.ay) * 0.01D);
                            }

                            if (this.random.nextFloat() < f4) {
                                f5 = MathHelper.a(this.random, 0.0F, 360.0F) * 0.017453292F;
                                f3 = MathHelper.a(this.random, 25.0F, 60.0F);
                                d9 = this.locX + (double) (MathHelper.sin(f5) * f3 * 0.1F);
                                d11 = (double) ((float) MathHelper.floor(this.boundingBox.b) + 1.0F);
                                d10 = this.locZ + (double) (MathHelper.cos(f5) * f3 * 0.1F);
                                worldserver.a("splash", d9, d11, d10, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                            }

                            if (this.ay <= 0) {
                                this.aA = MathHelper.a(this.random, 0.0F, 360.0F);
                                this.az = MathHelper.nextInt(this.random, 20, 80);
                            }
                        } else {
                            this.ay = MathHelper.nextInt(this.random, 100, 900);
                            this.ay -= EnchantmentManager.getLureEnchantmentLevel(this.owner) * 20 * 5;
                        }
                    }

                    if (this.ax > 0) {
                        this.motY -= (double) (this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat()) * 0.2D;
                    }
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
        nbttagcompound.setShort("xTile", (short) this.g);
        nbttagcompound.setShort("yTile", (short) this.h);
        nbttagcompound.setShort("zTile", (short) this.i);
        nbttagcompound.setByte("inTile", (byte) Block.b(this.j));
        nbttagcompound.setByte("shake", (byte) this.a);
        nbttagcompound.setByte("inGround", (byte) (this.au ? 1 : 0));
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.g = nbttagcompound.getShort("xTile");
        this.h = nbttagcompound.getShort("yTile");
        this.i = nbttagcompound.getShort("zTile");
        this.j = Block.e(nbttagcompound.getByte("inTile") & 255);
        this.a = nbttagcompound.getByte("shake") & 255;
        this.au = nbttagcompound.getByte("inGround") == 1;
    }

    public int e() {
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
            } else if (this.ax > 0) {
                EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY, this.locZ, this.f());
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
                // CraftBukkit - this.random.nextInt(6) + 1 -> playerFishEvent.getExpToDrop()
                this.owner.world.addEntity(new EntityExperienceOrb(this.owner.world, this.owner.locX, this.owner.locY + 0.5D, this.owner.locZ + 0.5D, playerFishEvent.getExpToDrop()));
                b0 = 1;
            }

            if (this.au) {
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

    private ItemStack f() {
        float f = this.world.random.nextFloat();
        int i = EnchantmentManager.getLuckEnchantmentLevel(this.owner);
        int j = EnchantmentManager.getLureEnchantmentLevel(this.owner);
        float f1 = 0.1F - (float) i * 0.025F - (float) j * 0.01F;
        float f2 = 0.05F + (float) i * 0.01F - (float) j * 0.01F;

        f1 = MathHelper.a(f1, 0.0F, 1.0F);
        f2 = MathHelper.a(f2, 0.0F, 1.0F);
        if (f < f1) {
            this.owner.a(StatisticList.A, 1);
            return ((PossibleFishingResult) WeightedRandom.a(this.random, (Collection) d)).a(this.random);
        } else {
            f -= f1;
            if (f < f2) {
                this.owner.a(StatisticList.B, 1);
                return ((PossibleFishingResult) WeightedRandom.a(this.random, (Collection) e)).a(this.random);
            } else {
                float f3 = f - f2;

                this.owner.a(StatisticList.z, 1);
                return ((PossibleFishingResult) WeightedRandom.a(this.random, (Collection) EntityFishingHook.f)).a(this.random); // CraftBukkit - fix static reference to fish list
            }
        }
    }

    public void die() {
        super.die();
        if (this.owner != null) {
            this.owner.hookedFish = null;
        }
    }
}
