package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
// CraftBukkit end

public abstract class Entity {

    private static int entityCount = 0;
    public int id;
    public double l;
    public boolean m;
    public Entity passenger;
    public Entity vehicle;
    public World world;
    public double lastX;
    public double lastY;
    public double lastZ;
    public double locX;
    public double locY;
    public double locZ;
    public double motX;
    public double motY;
    public double motZ;
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;
    public final AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean positionChanged;
    public boolean G;
    public boolean H;
    public boolean velocityChanged;
    protected boolean J;
    public boolean K;
    public boolean dead;
    public float height;
    public float width;
    public float length;
    public float P;
    public float Q;
    public float R;
    public float fallDistance;
    private int c;
    public double T;
    public double U;
    public double V;
    public float W;
    public float X;
    public boolean Y;
    public float Z;
    protected Random random;
    public int ticksLived;
    public int maxFireTicks;
    public int fireTicks; // CraftBukkit - private -> public
    protected boolean ad;
    public int noDamageTicks;
    private boolean justCreated;
    protected boolean fireProof;
    protected DataWatcher datawatcher;
    private double f;
    private double g;
    public boolean ah;
    public int ai;
    public int aj;
    public int ak;
    public boolean al;
    public boolean am;
    public int an;
    protected boolean ao;
    private int h;
    public int dimension;
    protected int aq;
    public EnumEntitySize ar;
    public UUID uniqueId = UUID.randomUUID(); // CraftBukkit
    public boolean valid = false; // CraftBukkit

    public Entity(World world) {
        this.id = entityCount++;
        this.l = 1.0D;
        this.m = false;
        this.boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.H = false;
        this.velocityChanged = false;
        this.K = true;
        this.dead = false;
        this.height = 0.0F;
        this.width = 0.6F;
        this.length = 1.8F;
        this.P = 0.0F;
        this.Q = 0.0F;
        this.R = 0.0F;
        this.fallDistance = 0.0F;
        this.c = 1;
        this.W = 0.0F;
        this.X = 0.0F;
        this.Y = false;
        this.Z = 0.0F;
        this.random = new Random();
        this.ticksLived = 0;
        this.maxFireTicks = 1;
        this.fireTicks = 0;
        this.ad = false;
        this.noDamageTicks = 0;
        this.justCreated = true;
        this.fireProof = false;
        this.datawatcher = new DataWatcher();
        this.ah = false;
        this.aq = 0;
        this.ar = EnumEntitySize.SIZE_2;
        this.world = world;
        this.setPosition(0.0D, 0.0D, 0.0D);
        if (world != null) {
            this.dimension = world.worldProvider.dimension;
        }

        this.datawatcher.a(0, Byte.valueOf((byte) 0));
        this.datawatcher.a(1, Short.valueOf((short) 300));
        this.a();
    }

    protected abstract void a();

    public DataWatcher getDataWatcher() {
        return this.datawatcher;
    }

    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity) object).id == this.id : false;
    }

    public int hashCode() {
        return this.id;
    }

    public void die() {
        this.dead = true;
    }

    protected void a(float f, float f1) {
        this.width = f;
        this.length = f1;
        float f2 = f % 2.0F;

        if ((double) f2 < 0.375D) {
            this.ar = EnumEntitySize.SIZE_1;
        } else if ((double) f2 < 0.75D) {
            this.ar = EnumEntitySize.SIZE_2;
        } else if ((double) f2 < 1.0D) {
            this.ar = EnumEntitySize.SIZE_3;
        } else if ((double) f2 < 1.375D) {
            this.ar = EnumEntitySize.SIZE_4;
        } else if ((double) f2 < 1.75D) {
            this.ar = EnumEntitySize.SIZE_5;
        } else {
            this.ar = EnumEntitySize.SIZE_6;
        }
    }

    protected void b(float f, float f1) {
        // CraftBukkit start - yaw was sometimes set to NaN, so we need to set it back to 0
        if (Float.isNaN(f)) {
            f = 0;
        }

        if ((f == Float.POSITIVE_INFINITY) || (f == Float.NEGATIVE_INFINITY)) {
            if (this instanceof EntityPlayer) {
                System.err.println(((CraftPlayer) this.getBukkitEntity()).getName() + " was caught trying to crash the server with an invalid yaw");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Nope");
            }
            f = 0;
        }

        // pitch was sometimes set to NaN, so we need to set it back to 0.
        if (Float.isNaN(f1)) {
            f1 = 0;
        }

        if ((f1 == Float.POSITIVE_INFINITY) || (f1 == Float.NEGATIVE_INFINITY)) {
            if (this instanceof EntityPlayer) {
                System.err.println(((CraftPlayer) this.getBukkitEntity()).getName() + " was caught trying to crash the server with an invalid pitch");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Nope");
            }
            f1 = 0;
        }
        // CraftBukkit end

        this.yaw = f % 360.0F;
        this.pitch = f1 % 360.0F;
    }

    public void setPosition(double d0, double d1, double d2) {
        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        float f = this.width / 2.0F;
        float f1 = this.length;

        this.boundingBox.b(d0 - (double) f, d1 - (double) this.height + (double) this.W, d2 - (double) f, d0 + (double) f, d1 - (double) this.height + (double) this.W + (double) f1, d2 + (double) f);
    }

    public void j_() {
        this.y();
    }

    public void y() {
        this.world.methodProfiler.a("entityBaseTick");
        if (this.vehicle != null && this.vehicle.dead) {
            this.vehicle = null;
        }

        ++this.ticksLived;
        this.P = this.Q;
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
        int i;

        if (!this.world.isStatic && this.world instanceof WorldServer) {
            MinecraftServer minecraftserver = ((WorldServer) this.world).getMinecraftServer();

            i = this.z();
            if (this.ao) {
                if (minecraftserver.getAllowNether()) {
                    if (this.vehicle == null && this.h++ >= i) {
                        this.h = i;
                        this.an = this.ab();
                        byte b0;

                        if (this.world.worldProvider.dimension == -1) {
                            b0 = 0;
                        } else {
                            b0 = -1;
                        }

                        this.b(b0);
                    }

                    this.ao = false;
                }
            } else {
                if (this.h > 0) {
                    this.h -= 4;
                }

                if (this.h < 0) {
                    this.h = 0;
                }
            }

            if (this.an > 0) {
                --this.an;
            }
        }

        int j;

        if (this.isSprinting() && !this.H()) {
            int k = MathHelper.floor(this.locX);

            i = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
            j = MathHelper.floor(this.locZ);
            int l = this.world.getTypeId(k, i, j);

            if (l > 0) {
                this.world.addParticle("tilecrack_" + l, this.locX + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.b + 0.1D, this.locZ + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, -this.motX * 4.0D, 1.5D, -this.motZ * 4.0D);
            }
        }

        if (this.I()) {
            if (!this.ad && !this.justCreated) {
                float f = MathHelper.sqrt(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                this.world.makeSound(this, "liquid.splash", f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                float f1 = (float) MathHelper.floor(this.boundingBox.b);

                float f2;
                float f3;

                for (j = 0; (float) j < 1.0F + this.width * 20.0F; ++j) {
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.world.addParticle("bubble", this.locX + (double) f3, (double) (f1 + 1.0F), this.locZ + (double) f2, this.motX, this.motY - (double) (this.random.nextFloat() * 0.2F), this.motZ);
                }

                for (j = 0; (float) j < 1.0F + this.width * 20.0F; ++j) {
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.world.addParticle("splash", this.locX + (double) f3, (double) (f1 + 1.0F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }

            this.fallDistance = 0.0F;
            this.ad = true;
            this.fireTicks = 0;
        } else {
            this.ad = false;
        }

        if (this.world.isStatic) {
            this.fireTicks = 0;
        } else if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= 4;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            } else {
                if (this.fireTicks % 20 == 0) {
                    // CraftBukkit start - TODO: this event spams!
                    if (this instanceof EntityLiving) {
                        EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FIRE_TICK, 1);
                        this.world.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            event.getEntity().setLastDamageCause(event);
                            this.damageEntity(DamageSource.BURN, event.getDamage());
                        }
                    } else {
                        this.damageEntity(DamageSource.BURN, 1);
                    }
                    // CraftBukkit end
                }

                --this.fireTicks;
            }
        }

        if (this.J()) {
            this.A();
            this.fallDistance *= 0.5F;
        }

        if (this.locY < -64.0D) {
            this.C();
        }

        if (!this.world.isStatic) {
            this.a(0, this.fireTicks > 0);
            this.a(2, this.vehicle != null);
        }

        this.justCreated = false;
        this.world.methodProfiler.b();
    }

    public int z() {
        return 0;
    }

    protected void A() {
        if (!this.fireProof) {
            // CraftBukkit start - fallen in lava TODO: this event spams!
            if (this instanceof EntityLiving) {
                Server server = this.world.getServer();

                // TODO: shouldn't be sending null for the block.
                org.bukkit.block.Block damager = null; // ((WorldServer) this.l).getWorld().getBlockAt(i, j, k);
                org.bukkit.entity.Entity damagee = this.getBukkitEntity();

                EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, EntityDamageEvent.DamageCause.LAVA, 4);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    damagee.setLastDamageCause(event);
                    this.damageEntity(DamageSource.LAVA, event.getDamage());
                }

                if (this.fireTicks <= 0) {
                    // not on fire yet
                    EntityCombustEvent combustEvent = new org.bukkit.event.entity.EntityCombustByBlockEvent(damager, damagee, 15);
                    server.getPluginManager().callEvent(combustEvent);

                    if (!combustEvent.isCancelled()) {
                        this.setOnFire(combustEvent.getDuration());
                    }
                } else {
                    // This will be called every single tick the entity is in lava, so don't throw an event
                    this.setOnFire(15);
                }
                return;
            }
            // CraftBukkit end - we also don't throw an event unless the object in lava is living, to save on some event calls

            this.damageEntity(DamageSource.LAVA, 4);
            this.setOnFire(15);
        }
    }

    public void setOnFire(int i) {
        int j = i * 20;

        if (this.fireTicks < j) {
            this.fireTicks = j;
        }
    }

    public void extinguish() {
        this.fireTicks = 0;
    }

    protected void C() {
        this.die();
    }

    public boolean c(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.boundingBox.c(d0, d1, d2);
        List list = this.world.getCubes(this, axisalignedbb);

        return !list.isEmpty() ? false : !this.world.containsLiquid(axisalignedbb);
    }

    public void move(double d0, double d1, double d2) {
        if (this.Y) {
            this.boundingBox.d(d0, d1, d2);
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.height - (double) this.W;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
        } else {
            this.world.methodProfiler.a("move");
            this.W *= 0.4F;
            double d3 = this.locX;
            double d4 = this.locY;
            double d5 = this.locZ;

            if (this.J) {
                this.J = false;
                d0 *= 0.25D;
                d1 *= 0.05000000074505806D;
                d2 *= 0.25D;
                this.motX = 0.0D;
                this.motY = 0.0D;
                this.motZ = 0.0D;
            }

            double d6 = d0;
            double d7 = d1;
            double d8 = d2;
            AxisAlignedBB axisalignedbb = this.boundingBox.clone();
            boolean flag = this.onGround && this.isSneaking() && this instanceof EntityHuman;

            if (flag) {
                double d9;

                for (d9 = 0.05D; d0 != 0.0D && this.world.getCubes(this, this.boundingBox.c(d0, -1.0D, 0.0D)).isEmpty(); d6 = d0) {
                    if (d0 < d9 && d0 >= -d9) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d9;
                    } else {
                        d0 += d9;
                    }
                }

                for (; d2 != 0.0D && this.world.getCubes(this, this.boundingBox.c(0.0D, -1.0D, d2)).isEmpty(); d8 = d2) {
                    if (d2 < d9 && d2 >= -d9) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d9;
                    } else {
                        d2 += d9;
                    }
                }

                while (d0 != 0.0D && d2 != 0.0D && this.world.getCubes(this, this.boundingBox.c(d0, -1.0D, d2)).isEmpty()) {
                    if (d0 < d9 && d0 >= -d9) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d9;
                    } else {
                        d0 += d9;
                    }

                    if (d2 < d9 && d2 >= -d9) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d9;
                    } else {
                        d2 += d9;
                    }

                    d6 = d0;
                    d8 = d2;
                }
            }

            List list = this.world.getCubes(this, this.boundingBox.a(d0, d1, d2));

            AxisAlignedBB axisalignedbb1;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); d1 = axisalignedbb1.b(this.boundingBox, d1)) {
                axisalignedbb1 = (AxisAlignedBB) iterator.next();
            }

            this.boundingBox.d(0.0D, d1, 0.0D);
            if (!this.K && d7 != d1) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            boolean flag1 = this.onGround || d7 != d1 && d7 < 0.0D;

            AxisAlignedBB axisalignedbb2;
            Iterator iterator1;

            for (iterator1 = list.iterator(); iterator1.hasNext(); d0 = axisalignedbb2.a(this.boundingBox, d0)) {
                axisalignedbb2 = (AxisAlignedBB) iterator1.next();
            }

            this.boundingBox.d(d0, 0.0D, 0.0D);
            if (!this.K && d6 != d0) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            for (iterator1 = list.iterator(); iterator1.hasNext(); d2 = axisalignedbb2.c(this.boundingBox, d2)) {
                axisalignedbb2 = (AxisAlignedBB) iterator1.next();
            }

            this.boundingBox.d(0.0D, 0.0D, d2);
            if (!this.K && d8 != d2) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            double d10;
            double d11;
            double d12;

            if (this.X > 0.0F && flag1 && (flag || this.W < 0.05F) && (d6 != d0 || d8 != d2)) {
                d10 = d0;
                d11 = d1;
                d12 = d2;
                d0 = d6;
                d1 = (double) this.X;
                d2 = d8;
                AxisAlignedBB axisalignedbb3 = this.boundingBox.clone();

                this.boundingBox.c(axisalignedbb);
                list = this.world.getCubes(this, this.boundingBox.a(d6, d1, d8));

                Iterator iterator2;
                AxisAlignedBB axisalignedbb4;

                for (iterator2 = list.iterator(); iterator2.hasNext(); d1 = axisalignedbb4.b(this.boundingBox, d1)) {
                    axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                }

                this.boundingBox.d(0.0D, d1, 0.0D);
                if (!this.K && d7 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (iterator2 = list.iterator(); iterator2.hasNext(); d0 = axisalignedbb4.a(this.boundingBox, d0)) {
                    axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                }

                this.boundingBox.d(d0, 0.0D, 0.0D);
                if (!this.K && d6 != d0) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (iterator2 = list.iterator(); iterator2.hasNext(); d2 = axisalignedbb4.c(this.boundingBox, d2)) {
                    axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                }

                this.boundingBox.d(0.0D, 0.0D, d2);
                if (!this.K && d8 != d2) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                if (!this.K && d7 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                } else {
                    d1 = (double) (-this.X);

                    for (iterator2 = list.iterator(); iterator2.hasNext(); d1 = axisalignedbb4.b(this.boundingBox, d1)) {
                        axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                    }

                    this.boundingBox.d(0.0D, d1, 0.0D);
                }

                if (d10 * d10 + d12 * d12 >= d0 * d0 + d2 * d2) {
                    d0 = d10;
                    d1 = d11;
                    d2 = d12;
                    this.boundingBox.c(axisalignedbb3);
                } else {
                    double d13 = this.boundingBox.b - (double) ((int) this.boundingBox.b);

                    if (d13 > 0.0D) {
                        this.W = (float) ((double) this.W + d13 + 0.01D);
                    }
                }
            }

            this.world.methodProfiler.b();
            this.world.methodProfiler.a("rest");
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.height - (double) this.W;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
            this.positionChanged = d6 != d0 || d8 != d2;
            this.G = d7 != d1;
            this.onGround = d7 != d1 && d7 < 0.0D;
            this.H = this.positionChanged || this.G;
            this.a(d1, this.onGround);
            if (d6 != d0) {
                this.motX = 0.0D;
            }

            if (d7 != d1) {
                this.motY = 0.0D;
            }

            if (d8 != d2) {
                this.motZ = 0.0D;
            }

            d10 = this.locX - d3;
            d11 = this.locY - d4;
            d12 = this.locZ - d5;

            // CraftBukkit start
            if ((this.positionChanged) && (this.getBukkitEntity() instanceof Vehicle)) {
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.block.Block block = this.world.getWorld().getBlockAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY - (double) this.height), MathHelper.floor(this.locZ));

                if (d6 > d0) {
                    block = block.getRelative(BlockFace.SOUTH);
                } else if (d6 < d0) {
                    block = block.getRelative(BlockFace.NORTH);
                } else if (d8 > d2) {
                    block = block.getRelative(BlockFace.WEST);
                } else if (d8 < d2) {
                    block = block.getRelative(BlockFace.EAST);
                }

                VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, block);
                this.world.getServer().getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            if (this.f_() && !flag && this.vehicle == null) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
                int k = MathHelper.floor(this.locZ);
                int l = this.world.getTypeId(i, j, k);

                if (l == 0 && this.world.getTypeId(i, j - 1, k) == Block.FENCE.id) {
                    l = this.world.getTypeId(i, j - 1, k);
                }

                if (l != Block.LADDER.id) {
                    d11 = 0.0D;
                }

                this.Q = (float) ((double) this.Q + (double) MathHelper.sqrt(d10 * d10 + d12 * d12) * 0.6D);
                this.R = (float) ((double) this.R + (double) MathHelper.sqrt(d10 * d10 + d11 * d11 + d12 * d12) * 0.6D);
                if (this.R > (float) this.c && l > 0) {
                    this.c = (int) this.R + 1;
                    if (this.H()) {
                        float f = MathHelper.sqrt(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.35F;

                        if (f > 1.0F) {
                            f = 1.0F;
                        }

                        this.world.makeSound(this, "liquid.swim", f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                    }

                    this.a(i, j, k, l);
                    Block.byId[l].b(this.world, i, j, k, this);
                }
            }

            this.D();
            boolean flag2 = this.G();

            if (this.world.e(this.boundingBox.shrink(0.001D, 0.001D, 0.001D))) {
                this.burn(1);
                if (!flag2) {
                    ++this.fireTicks;
                    // CraftBukkit start - not on fire yet
                    if (this.fireTicks <= 0) { // only throw events on the first combust, otherwise it spams
                        EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                        this.world.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.setOnFire(event.getDuration());
                        }
                    } else {
                        // CraftBukkit end
                        this.setOnFire(8);
                    }
                }
            } else if (this.fireTicks <= 0) {
                this.fireTicks = -this.maxFireTicks;
            }

            if (flag2 && this.fireTicks > 0) {
                this.world.makeSound(this, "random.fizz", 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.fireTicks = -this.maxFireTicks;
            }

            this.world.methodProfiler.b();
        }
    }

    protected void D() {
        int i = MathHelper.floor(this.boundingBox.a + 0.001D);
        int j = MathHelper.floor(this.boundingBox.b + 0.001D);
        int k = MathHelper.floor(this.boundingBox.c + 0.001D);
        int l = MathHelper.floor(this.boundingBox.d - 0.001D);
        int i1 = MathHelper.floor(this.boundingBox.e - 0.001D);
        int j1 = MathHelper.floor(this.boundingBox.f - 0.001D);

        if (this.world.d(i, j, k, l, i1, j1)) {
            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = j; l1 <= i1; ++l1) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        int j2 = this.world.getTypeId(k1, l1, i2);

                        if (j2 > 0) {
                            Block.byId[j2].a(this.world, k1, l1, i2, this);
                        }
                    }
                }
            }
        }
    }

    protected void a(int i, int j, int k, int l) {
        StepSound stepsound = Block.byId[l].stepSound;

        if (this.world.getTypeId(i, j + 1, k) == Block.SNOW.id) {
            stepsound = Block.SNOW.stepSound;
            this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
        } else if (!Block.byId[l].material.isLiquid()) {
            this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
        }
    }

    protected boolean f_() {
        return true;
    }

    protected void a(double d0, boolean flag) {
        if (flag) {
            if (this.fallDistance > 0.0F) {
                this.a(this.fallDistance);
                this.fallDistance = 0.0F;
            }
        } else if (d0 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - d0);
        }
    }

    public AxisAlignedBB E() {
        return null;
    }

    protected void burn(int i) {
        if (!this.fireProof) {
            // CraftBukkit start
            if (this instanceof EntityLiving) {
                EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FIRE, i);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                i = event.getDamage();
                event.getEntity().setLastDamageCause(event);
            }
            // CraftBukkit end

            this.damageEntity(DamageSource.FIRE, i);
        }
    }

    public final boolean isFireproof() {
        return this.fireProof;
    }

    protected void a(float f) {
        if (this.passenger != null) {
            this.passenger.a(f);
        }
    }

    public boolean G() {
        return this.ad || this.world.B(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean H() {
        return this.ad;
    }

    public boolean I() {
        return this.world.a(this.boundingBox.grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D, 0.001D, 0.001D), Material.WATER, this);
    }

    public boolean a(Material material) {
        double d0 = this.locY + (double) this.getHeadHeight();
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.d((float) MathHelper.floor(d0));
        int k = MathHelper.floor(this.locZ);
        int l = this.world.getTypeId(i, j, k);

        if (l != 0 && Block.byId[l].material == material) {
            float f = BlockFluids.d(this.world.getData(i, j, k)) - 0.11111111F;
            float f1 = (float) (j + 1) - f;

            return d0 < (double) f1;
        } else {
            return false;
        }
    }

    public float getHeadHeight() {
        return 0.0F;
    }

    public boolean J() {
        return this.world.a(this.boundingBox.grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void a(float f, float f1, float f2) {
        float f3 = f * f + f1 * f1;

        if (f3 >= 1.0E-4F) {
            f3 = MathHelper.c(f3);
            if (f3 < 1.0F) {
                f3 = 1.0F;
            }

            f3 = f2 / f3;
            f *= f3;
            f1 *= f3;
            float f4 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F);
            float f5 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F);

            this.motX += (double) (f * f5 - f1 * f4);
            this.motZ += (double) (f1 * f5 + f * f4);
        }
    }

    public float c(float f) {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locZ);

        if (this.world.isLoaded(i, 0, j)) {
            double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.66D;
            int k = MathHelper.floor(this.locY - (double) this.height + d0);

            return this.world.o(i, k, j);
        } else {
            return 0.0F;
        }
    }

    public void spawnIn(World world) {
        // CraftBukkit start
        if (world == null) {
            this.die();
            this.world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
            return;
        }
        // CraftBukkit end

        this.world = world;
    }

    public void setLocation(double d0, double d1, double d2, float f, float f1) {
        this.lastX = this.locX = d0;
        this.lastY = this.locY = d1;
        this.lastZ = this.locZ = d2;
        this.lastYaw = this.yaw = f;
        this.lastPitch = this.pitch = f1;
        this.W = 0.0F;
        double d3 = (double) (this.lastYaw - f);

        if (d3 < -180.0D) {
            this.lastYaw += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.lastYaw -= 360.0F;
        }

        this.setPosition(this.locX, this.locY, this.locZ);
        this.b(f, f1);
    }

    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        this.T = this.lastX = this.locX = d0;
        this.U = this.lastY = this.locY = d1 + (double) this.height;
        this.V = this.lastZ = this.locZ = d2;
        this.yaw = f;
        this.pitch = f1;
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    public float d(Entity entity) {
        float f = (float) (this.locX - entity.locX);
        float f1 = (float) (this.locY - entity.locY);
        float f2 = (float) (this.locZ - entity.locZ);

        return MathHelper.c(f * f + f1 * f1 + f2 * f2);
    }

    public double e(double d0, double d1, double d2) {
        double d3 = this.locX - d0;
        double d4 = this.locY - d1;
        double d5 = this.locZ - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double f(double d0, double d1, double d2) {
        double d3 = this.locX - d0;
        double d4 = this.locY - d1;
        double d5 = this.locZ - d2;

        return (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double e(Entity entity) {
        double d0 = this.locX - entity.locX;
        double d1 = this.locY - entity.locY;
        double d2 = this.locZ - entity.locZ;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void b_(EntityHuman entityhuman) {}

    public void collide(Entity entity) {
        if (entity.passenger != this && entity.vehicle != this) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;
            double d2 = MathHelper.a(d0, d1);

            if (d2 >= 0.009999999776482582D) {
                d2 = (double) MathHelper.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= (double) (1.0F - this.Z);
                d1 *= (double) (1.0F - this.Z);
                this.g(-d0, 0.0D, -d1);
                entity.g(d0, 0.0D, d1);
            }
        }
    }

    public void g(double d0, double d1, double d2) {
        this.motX += d0;
        this.motY += d1;
        this.motZ += d2;
        this.am = true;
    }

    protected void K() {
        this.velocityChanged = true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.K();
        return false;
    }

    public boolean L() {
        return false;
    }

    public boolean M() {
        return false;
    }

    public void c(Entity entity, int i) {}

    public boolean c(NBTTagCompound nbttagcompound) {
        String s = this.Q();

        if (!this.dead && s != null) {
            nbttagcompound.setString("id", s);
            this.d(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public void d(NBTTagCompound nbttagcompound) {
        nbttagcompound.set("Pos", this.a(new double[] { this.locX, this.locY + (double) this.W, this.locZ}));
        nbttagcompound.set("Motion", this.a(new double[] { this.motX, this.motY, this.motZ}));

        // CraftBukkit start - checking for NaN pitch/yaw and resetting to zero
        // TODO: make sure this is the best way to address this.
        if (Float.isNaN(this.yaw)) {
            this.yaw = 0;
        }

        if (Float.isNaN(this.pitch)) {
            this.pitch = 0;
        }
        // CraftBukkit end

        nbttagcompound.set("Rotation", this.a(new float[] { this.yaw, this.pitch}));
        nbttagcompound.setFloat("FallDistance", this.fallDistance);
        nbttagcompound.setShort("Fire", (short) this.fireTicks);
        nbttagcompound.setShort("Air", (short) this.getAirTicks());
        nbttagcompound.setBoolean("OnGround", this.onGround);
        nbttagcompound.setInt("Dimension", this.dimension);
        // CraftBukkit start
        nbttagcompound.setLong("WorldUUIDLeast", this.world.getDataManager().getUUID().getLeastSignificantBits());
        nbttagcompound.setLong("WorldUUIDMost", this.world.getDataManager().getUUID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
        nbttagcompound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
        // CraftBukkit end
        this.b(nbttagcompound);
    }

    public void e(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getList("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.getList("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.getList("Rotation");

        this.motX = ((NBTTagDouble) nbttaglist1.get(0)).data;
        this.motY = ((NBTTagDouble) nbttaglist1.get(1)).data;
        this.motZ = ((NBTTagDouble) nbttaglist1.get(2)).data;
        /* CraftBukkit start - moved section down
        if (Math.abs(this.motX) > 10.0D) {
            this.motX = 0.0D;
        }

        if (Math.abs(this.motY) > 10.0D) {
            this.motY = 0.0D;
        }

        if (Math.abs(this.motZ) > 10.0D) {
            this.motZ = 0.0D;
        }
        // CraftBukkit end */

        this.lastX = this.T = this.locX = ((NBTTagDouble) nbttaglist.get(0)).data;
        this.lastY = this.U = this.locY = ((NBTTagDouble) nbttaglist.get(1)).data;
        this.lastZ = this.V = this.locZ = ((NBTTagDouble) nbttaglist.get(2)).data;
        this.lastYaw = this.yaw = ((NBTTagFloat) nbttaglist2.get(0)).data;
        this.lastPitch = this.pitch = ((NBTTagFloat) nbttaglist2.get(1)).data;
        this.fallDistance = nbttagcompound.getFloat("FallDistance");
        this.fireTicks = nbttagcompound.getShort("Fire");
        this.setAirTicks(nbttagcompound.getShort("Air"));
        this.onGround = nbttagcompound.getBoolean("OnGround");
        this.dimension = nbttagcompound.getInt("Dimension");
        this.setPosition(this.locX, this.locY, this.locZ);

        // CraftBukkit start
        long least = nbttagcompound.getLong("UUIDLeast");
        long most = nbttagcompound.getLong("UUIDMost");

        if (least != 0L && most != 0L) {
            this.uniqueId = new UUID(most, least);
        }
        // CraftBukkit end

        this.b(this.yaw, this.pitch);
        this.a(nbttagcompound);

        // CraftBukkit start - exempt Vehicles from notch's sanity check
        if (!(this.getBukkitEntity() instanceof Vehicle)) {
            if (Math.abs(this.motX) > 10.0D) {
                this.motX = 0.0D;
            }

            if (Math.abs(this.motY) > 10.0D) {
                this.motY = 0.0D;
            }

            if (Math.abs(this.motZ) > 10.0D) {
                this.motZ = 0.0D;
            }
        }
        // CraftBukkit end

        // CraftBukkit start - reset world
        if (this instanceof EntityPlayer) {
            Server server = Bukkit.getServer();
            org.bukkit.World bworld = null;

            // TODO: Remove World related checks, replaced with WorldUID.
            String worldName = nbttagcompound.getString("World");

            if (nbttagcompound.hasKey("WorldUUIDMost") && nbttagcompound.hasKey("WorldUUIDLeast")) {
                UUID uid = new UUID(nbttagcompound.getLong("WorldUUIDMost"), nbttagcompound.getLong("WorldUUIDLeast"));
                bworld = server.getWorld(uid);
            } else {
                bworld = server.getWorld(worldName);
            }

            if (bworld == null) {
                EntityPlayer entityPlayer = (EntityPlayer) this;
                bworld = ((org.bukkit.craftbukkit.CraftServer) server).getServer().getWorldServer(entityPlayer.dimension).getWorld();
            }

            this.spawnIn(bworld == null ? null : ((CraftWorld) bworld).getHandle());
        }
        // CraftBukkit end
    }

    protected final String Q() {
        return EntityTypes.b(this);
    }

    protected abstract void a(NBTTagCompound nbttagcompound);

    protected abstract void b(NBTTagCompound nbttagcompound);

    protected NBTTagList a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.add(new NBTTagDouble((String) null, d0));
        }

        return nbttaglist;
    }

    protected NBTTagList a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.add(new NBTTagFloat((String) null, f));
        }

        return nbttaglist;
    }

    public EntityItem b(int i, int j) {
        return this.a(i, j, 0.0F);
    }

    public EntityItem a(int i, int j, float f) {
        return this.a(new ItemStack(i, j, 0), f);
    }

    public EntityItem a(ItemStack itemstack, float f) {
        EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY + (double) f, this.locZ, itemstack);

        entityitem.pickupDelay = 10;
        this.world.addEntity(entityitem);
        return entityitem;
    }

    public boolean isAlive() {
        return !this.dead;
    }

    public boolean inBlock() {
        for (int i = 0; i < 8; ++i) {
            float f = ((float) ((i >> 0) % 2) - 0.5F) * this.width * 0.8F;
            float f1 = ((float) ((i >> 1) % 2) - 0.5F) * 0.1F;
            float f2 = ((float) ((i >> 2) % 2) - 0.5F) * this.width * 0.8F;
            int j = MathHelper.floor(this.locX + (double) f);
            int k = MathHelper.floor(this.locY + (double) this.getHeadHeight() + (double) f1);
            int l = MathHelper.floor(this.locZ + (double) f2);

            if (this.world.s(j, k, l)) {
                return true;
            }
        }

        return false;
    }

    public boolean c(EntityHuman entityhuman) {
        return false;
    }

    public AxisAlignedBB g(Entity entity) {
        return null;
    }

    public void U() {
        if (this.vehicle.dead) {
            this.vehicle = null;
        } else {
            this.motX = 0.0D;
            this.motY = 0.0D;
            this.motZ = 0.0D;
            this.j_();
            if (this.vehicle != null) {
                this.vehicle.V();
                this.g += (double) (this.vehicle.yaw - this.vehicle.lastYaw);

                for (this.f += (double) (this.vehicle.pitch - this.vehicle.lastPitch); this.g >= 180.0D; this.g -= 360.0D) {
                    ;
                }

                while (this.g < -180.0D) {
                    this.g += 360.0D;
                }

                while (this.f >= 180.0D) {
                    this.f -= 360.0D;
                }

                while (this.f < -180.0D) {
                    this.f += 360.0D;
                }

                double d0 = this.g * 0.5D;
                double d1 = this.f * 0.5D;
                float f = 10.0F;

                if (d0 > (double) f) {
                    d0 = (double) f;
                }

                if (d0 < (double) (-f)) {
                    d0 = (double) (-f);
                }

                if (d1 > (double) f) {
                    d1 = (double) f;
                }

                if (d1 < (double) (-f)) {
                    d1 = (double) (-f);
                }

                this.g -= d0;
                this.f -= d1;
                this.yaw = (float) ((double) this.yaw + d0);
                this.pitch = (float) ((double) this.pitch + d1);
            }
        }
    }

    public void V() {
        if (!(this.passenger instanceof EntityHuman) || !((EntityHuman) this.passenger).bS()) {
            this.passenger.T = this.T;
            this.passenger.U = this.U + this.X() + this.passenger.W();
            this.passenger.V = this.V;
        }

        this.passenger.setPosition(this.locX, this.locY + this.X() + this.passenger.W(), this.locZ);
    }

    public double W() {
        return (double) this.height;
    }

    public double X() {
        return (double) this.length * 0.75D;
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        this.setPassengerOf(entity);
    }

    protected org.bukkit.entity.Entity bukkitEntity;

    public org.bukkit.entity.Entity getBukkitEntity() {
        if (this.bukkitEntity == null) {
            this.bukkitEntity = org.bukkit.craftbukkit.entity.CraftEntity.getEntity(this.world.getServer(), this);
        }
        return this.bukkitEntity;
    }

    public void setPassengerOf(Entity entity) {
        // b(null) doesn't really fly for overloaded methods,
        // so this method is needed

        PluginManager pluginManager = Bukkit.getPluginManager();
        this.getBukkitEntity(); // make sure bukkitEntity is initialised
        // CraftBukkit end
        this.f = 0.0D;
        this.g = 0.0D;
        if (entity == null) {
            if (this.vehicle != null) {
                // CraftBukkit start
                if ((this.bukkitEntity instanceof LivingEntity) && (this.vehicle.getBukkitEntity() instanceof Vehicle)) {
                    VehicleExitEvent event = new VehicleExitEvent((Vehicle) this.vehicle.getBukkitEntity(), (LivingEntity) this.bukkitEntity);
                    pluginManager.callEvent(event);
                }
                // CraftBukkit end

                this.setPositionRotation(this.vehicle.locX, this.vehicle.boundingBox.b + (double) this.vehicle.length, this.vehicle.locZ, this.yaw, this.pitch);
                this.vehicle.passenger = null;
            }

            this.vehicle = null;
        } else if (this.vehicle == entity) {
            // CraftBukkit start
            if ((this.bukkitEntity instanceof LivingEntity) && (this.vehicle.getBukkitEntity() instanceof Vehicle)) {
                VehicleExitEvent event = new VehicleExitEvent((Vehicle) this.vehicle.getBukkitEntity(), (LivingEntity) this.bukkitEntity);
                pluginManager.callEvent(event);
            }
            // CraftBukkit end

            this.h(entity);
            this.vehicle.passenger = null;
            this.vehicle = null;
        } else {
            // CraftBukkit start
            if ((this.bukkitEntity instanceof LivingEntity) && (entity.getBukkitEntity() instanceof Vehicle)) {
                VehicleEnterEvent event = new VehicleEnterEvent((Vehicle) entity.getBukkitEntity(), this.bukkitEntity);
                pluginManager.callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            // CraftBukkit end

            if (this.vehicle != null) {
                this.vehicle.passenger = null;
            }

            if (entity.passenger != null) {
                entity.passenger.vehicle = null;
            }

            this.vehicle = entity;
            entity.passenger = this;
        }
    }

    public void h(Entity entity) {
        double d0 = entity.locX;
        double d1 = entity.boundingBox.b + (double) entity.length;
        double d2 = entity.locZ;

        for (double d3 = -1.5D; d3 < 2.0D; ++d3) {
            for (double d4 = -1.5D; d4 < 2.0D; ++d4) {
                if (d3 != 0.0D || d4 != 0.0D) {
                    int i = (int) (this.locX + d3);
                    int j = (int) (this.locZ + d4);
                    AxisAlignedBB axisalignedbb = this.boundingBox.c(d3, 1.0D, d4);

                    if (this.world.a(axisalignedbb).isEmpty()) {
                        if (this.world.t(i, (int) this.locY, j)) {
                            this.setPositionRotation(this.locX + d3, this.locY + 1.0D, this.locZ + d4, this.yaw, this.pitch);
                            return;
                        }

                        if (this.world.t(i, (int) this.locY - 1, j) || this.world.getMaterial(i, (int) this.locY - 1, j) == Material.WATER) {
                            d0 = this.locX + d3;
                            d1 = this.locY + 1.0D;
                            d2 = this.locZ + d4;
                        }
                    }
                }
            }
        }

        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
    }

    public float Y() {
        return 0.1F;
    }

    public Vec3D Z() {
        return null;
    }

    public void aa() {
        if (this.an > 0) {
            this.an = this.ab();
        } else {
            double d0 = this.lastX - this.locX;
            double d1 = this.lastZ - this.locZ;

            if (!this.world.isStatic && !this.ao) {
                this.aq = Direction.a(d0, d1);
            }

            this.ao = true;
        }
    }

    public int ab() {
        return 500;
    }

    public ItemStack[] getEquipment() {
        return null;
    }

    public void setEquipment(int i, ItemStack itemstack) {}

    public boolean isBurning() {
        return this.fireTicks > 0 || this.e(0);
    }

    public boolean ag() {
        return this.vehicle != null || this.e(2);
    }

    public boolean isSneaking() {
        return this.e(1);
    }

    public void setSneaking(boolean flag) {
        this.a(1, flag);
    }

    public boolean isSprinting() {
        return this.e(3);
    }

    public void setSprinting(boolean flag) {
        this.a(3, flag);
    }

    public boolean isInvisible() {
        return this.e(5);
    }

    public void setInvisible(boolean flag) {
        this.a(5, flag);
    }

    public void d(boolean flag) {
        this.a(4, flag);
    }

    protected boolean e(int i) {
        return (this.datawatcher.getByte(0) & 1 << i) != 0;
    }

    protected void a(int i, boolean flag) {
        byte b0 = this.datawatcher.getByte(0);

        if (flag) {
            this.datawatcher.watch(0, Byte.valueOf((byte) (b0 | 1 << i)));
        } else {
            this.datawatcher.watch(0, Byte.valueOf((byte) (b0 & ~(1 << i))));
        }
    }

    public int getAirTicks() {
        return this.datawatcher.getShort(1);
    }

    public void setAirTicks(int i) {
        this.datawatcher.watch(1, Short.valueOf((short) i));
    }

    public void a(EntityLightning entitylightning) {
        // CraftBukkit start
        final org.bukkit.entity.Entity thisBukkitEntity = this.getBukkitEntity();
        final org.bukkit.entity.Entity stormBukkitEntity = entitylightning.getBukkitEntity();
        final PluginManager pluginManager = Bukkit.getPluginManager();

        if (thisBukkitEntity instanceof Painting) {
            PaintingBreakByEntityEvent event = new PaintingBreakByEntityEvent((Painting) thisBukkitEntity, stormBukkitEntity);
            pluginManager.callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(stormBukkitEntity, thisBukkitEntity, EntityDamageEvent.DamageCause.LIGHTNING, 5);
        pluginManager.callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        thisBukkitEntity.setLastDamageCause(event);
        this.burn(event.getDamage());
        // CraftBukkit end

        ++this.fireTicks;
        if (this.fireTicks == 0) {
            // CraftBukkit start - raise a combust event when lightning strikes
            EntityCombustByEntityEvent entityCombustEvent = new EntityCombustByEntityEvent(stormBukkitEntity, thisBukkitEntity, 8);
            pluginManager.callEvent(entityCombustEvent);
            if (!entityCombustEvent.isCancelled()) {
                this.setOnFire(entityCombustEvent.getDuration());
            }
            // CraftBukkit end
        }
    }

    public void a(EntityLiving entityliving) {}

    protected boolean i(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);
        double d3 = d0 - (double) i;
        double d4 = d1 - (double) j;
        double d5 = d2 - (double) k;

        if (this.world.s(i, j, k)) {
            boolean flag = !this.world.s(i - 1, j, k);
            boolean flag1 = !this.world.s(i + 1, j, k);
            boolean flag2 = !this.world.s(i, j - 1, k);
            boolean flag3 = !this.world.s(i, j + 1, k);
            boolean flag4 = !this.world.s(i, j, k - 1);
            boolean flag5 = !this.world.s(i, j, k + 1);
            byte b0 = -1;
            double d6 = 9999.0D;

            if (flag && d3 < d6) {
                d6 = d3;
                b0 = 0;
            }

            if (flag1 && 1.0D - d3 < d6) {
                d6 = 1.0D - d3;
                b0 = 1;
            }

            if (flag2 && d4 < d6) {
                d6 = d4;
                b0 = 2;
            }

            if (flag3 && 1.0D - d4 < d6) {
                d6 = 1.0D - d4;
                b0 = 3;
            }

            if (flag4 && d5 < d6) {
                d6 = d5;
                b0 = 4;
            }

            if (flag5 && 1.0D - d5 < d6) {
                d6 = 1.0D - d5;
                b0 = 5;
            }

            float f = this.random.nextFloat() * 0.2F + 0.1F;

            if (b0 == 0) {
                this.motX = (double) (-f);
            }

            if (b0 == 1) {
                this.motX = (double) f;
            }

            if (b0 == 2) {
                this.motY = (double) (-f);
            }

            if (b0 == 3) {
                this.motY = (double) f;
            }

            if (b0 == 4) {
                this.motZ = (double) (-f);
            }

            if (b0 == 5) {
                this.motZ = (double) f;
            }

            return true;
        } else {
            return false;
        }
    }

    public void am() {
        this.J = true;
        this.fallDistance = 0.0F;
    }

    public String getLocalizedName() {
        String s = EntityTypes.b(this);

        if (s == null) {
            s = "generic";
        }

        return LocaleI18n.get("entity." + s + ".name");
    }

    public Entity[] ao() {
        return null;
    }

    public boolean i(Entity entity) {
        return this == entity;
    }

    public float ap() {
        return 0.0F;
    }

    public boolean aq() {
        return true;
    }

    public String toString() {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] { this.getClass().getSimpleName(), this.getLocalizedName(), Integer.valueOf(this.id), this.world == null ? "~NULL~" : this.world.getWorldData().getName(), Double.valueOf(this.locX), Double.valueOf(this.locY), Double.valueOf(this.locZ)});
    }

    public void j(Entity entity) {
        this.setPositionRotation(entity.locX, entity.locY, entity.locZ, entity.yaw, entity.pitch);
    }

    public void a(Entity entity, boolean flag) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        entity.d(nbttagcompound);
        this.e(nbttagcompound);
        this.an = entity.an;
        this.aq = entity.aq;
    }

    public void b(int i) {
        if (false && !this.world.isStatic && !this.dead) { // CraftBukkit - disable entity portal support for now.
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int j = this.dimension;
            WorldServer worldserver = minecraftserver.getWorldServer(j);
            WorldServer worldserver1 = minecraftserver.getWorldServer(i);

            this.dimension = i;
            this.world.kill(this);
            this.dead = false;
            minecraftserver.getServerConfigurationManager().a(this, j, worldserver, worldserver1);
            Entity entity = EntityTypes.createEntityByName(EntityTypes.b(this), worldserver1);

            if (entity != null) {
                entity.a(this, true);
                worldserver1.addEntity(entity);
            }

            this.dead = true;
            worldserver.i();
            worldserver1.i();
        }
    }

    public float a(Explosion explosion, Block block, int i, int j, int k) {
        return block.a(this);
    }

    public int as() {
        return 3;
    }

    public int at() {
        return this.aq;
    }

    public boolean au() {
        return false;
    }
}
