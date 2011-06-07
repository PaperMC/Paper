package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftVehicle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
// CraftBukkit end

public abstract class Entity {

    private static int entityCount = 0;
    public int id;
    public double aH;
    public boolean aI;
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
    public boolean bc;
    public boolean bd;
    public boolean velocityChanged;
    public boolean bf;
    public boolean bg;
    public boolean dead;
    public float height;
    public float length;
    public float width;
    public float bl;
    public float bm;
    public float fallDistance; // CraftBukkit - private -> public
    private int b;
    public double bo;
    public double bp;
    public double bq;
    public float br;
    public float bs;
    public boolean bt;
    public float bu;
    protected Random random;
    public int ticksLived;
    public int maxFireTicks;
    public int fireTicks;
    public int maxAirTicks; // CraftBukkit - protected - >public
    protected boolean bA;
    public int noDamageTicks;
    public int airTicks;
    private boolean justCreated;
    protected boolean bD;
    protected DataWatcher datawatcher;
    private double d;
    private double e;
    public boolean bF;
    public int bG;
    public int bH;
    public int bI;
    public boolean bJ;
    public UUID uniqueId = UUID.randomUUID(); // CraftBukkit

    public Entity(World world) {
        this.id = entityCount++;
        this.aH = 1.0D;
        this.aI = false;
        this.boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.bd = false;
        this.velocityChanged = false;
        this.bg = true;
        this.dead = false;
        this.height = 0.0F;
        this.length = 0.6F;
        this.width = 1.8F;
        this.bl = 0.0F;
        this.bm = 0.0F;
        this.fallDistance = 0.0F;
        this.b = 1;
        this.br = 0.0F;
        this.bs = 0.0F;
        this.bt = false;
        this.bu = 0.0F;
        this.random = new Random();
        this.ticksLived = 0;
        this.maxFireTicks = 1;
        this.fireTicks = 0;
        this.maxAirTicks = 300;
        this.bA = false;
        this.noDamageTicks = 0;
        this.airTicks = 300;
        this.justCreated = true;
        this.bD = false;
        this.datawatcher = new DataWatcher();
        this.bF = false;
        this.world = world;
        this.setPosition(0.0D, 0.0D, 0.0D);
        this.datawatcher.a(0, Byte.valueOf((byte) 0));
        this.b();
    }

    protected abstract void b();

    public DataWatcher Z() {
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

    protected void b(float f, float f1) {
        this.length = f;
        this.width = f1;
    }

    protected void c(float f, float f1) {
        // CraftBukkit start - yaw was sometimes set to NaN, so we need to set it back to 0.
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

        if ((f1 == Float.POSITIVE_INFINITY) || (f1 == Float.NEGATIVE_INFINITY))  {
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
        float f = this.length / 2.0F;
        float f1 = this.width;

        this.boundingBox.c(d0 - (double) f, d1 - (double) this.height + (double) this.br, d2 - (double) f, d0 + (double) f, d1 - (double) this.height + (double) this.br + (double) f1, d2 + (double) f);
    }

    public void o_() {
        this.Q();
    }

    public void Q() {
        if (this.vehicle != null && this.vehicle.dead) {
            this.vehicle = null;
        }

        ++this.ticksLived;
        this.bl = this.bm;
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
        if (this.f_()) {
            if (!this.bA && !this.justCreated) {
                float f = MathHelper.a(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                this.world.makeSound(this, "random.splash", f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                float f1 = (float) MathHelper.floor(this.boundingBox.b);

                int i;
                float f2;
                float f3;

                for (i = 0; (float) i < 1.0F + this.length * 20.0F; ++i) {
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                    this.world.a("bubble", this.locX + (double) f2, (double) (f1 + 1.0F), this.locZ + (double) f3, this.motX, this.motY - (double) (this.random.nextFloat() * 0.2F), this.motZ);
                }

                for (i = 0; (float) i < 1.0F + this.length * 20.0F; ++i) {
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length;
                    this.world.a("splash", this.locX + (double) f2, (double) (f1 + 1.0F), this.locZ + (double) f3, this.motX, this.motY, this.motZ);
                }
            }

            this.fallDistance = 0.0F;
            this.bA = true;
            this.fireTicks = 0;
        } else {
            this.bA = false;
        }

        if (this.world.isStatic) {
            this.fireTicks = 0;
        } else if (this.fireTicks > 0) {
            if (this.bD) {
                this.fireTicks -= 4;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            } else {
                if (this.fireTicks % 20 == 0) {
                    // CraftBukkit start
                    // TODO: this event spams!
                    if (this instanceof EntityLiving) {
                        CraftServer server = ((WorldServer) this.world).getServer();
                        org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                        DamageCause damageType = EntityDamageEvent.DamageCause.FIRE_TICK;
                        int damageDone = 1;

                        EntityDamageEvent event = new EntityDamageEvent(damagee, damageType, damageDone);
                        server.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.damageEntity((Entity) null, event.getDamage());
                        }
                    } else {
                        this.damageEntity((Entity) null, 1);
                    }
                    // CraftBukkit end
                }

                --this.fireTicks;
            }
        }

        if (this.ad()) {
            this.aa();
        }

        if (this.locY < -64.0D) {
            this.X();
        }

        if (!this.world.isStatic) {
            this.a(0, this.fireTicks > 0);
            this.a(2, this.vehicle != null);
        }

        this.justCreated = false;
    }

    protected void aa() {
        if (!this.bD) {
            // CraftBukkit start - TODO: this event spams!
            if (this instanceof EntityLiving) {
                CraftServer server = ((WorldServer) this.world).getServer();
                // TODO: shouldn't be sending null for the block.
                org.bukkit.block.Block damager = null; // ((WorldServer) this.l).getWorld().getBlockAt(i, j, k);
                org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.LAVA;
                int damageDone = 4;

                EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, damageType, damageDone);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.damageEntity((Entity) null, event.getDamage());
                }

                if (this.fireTicks <= 0) {
                    // not on fire yet
                    EntityCombustEvent combustEvent = new EntityCombustEvent(damagee);
                    server.getPluginManager().callEvent(combustEvent);
                    if (!combustEvent.isCancelled()) {
                        this.fireTicks = 600;
                    }
                } else {
                    // reset fire level back to max
                    this.fireTicks = 600;
                }
                return;
            }
            // CraftBukkit end

            this.damageEntity((Entity) null, 4);
            this.fireTicks = 600;
        }
    }

    protected void X() {
        this.die();
    }

    public boolean b(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.boundingBox.c(d0, d1, d2);
        List list = this.world.getEntities(this, axisalignedbb);

        return list.size() > 0 ? false : !this.world.c(axisalignedbb);
    }

    public void move(double d0, double d1, double d2) {
        if (this.bt) {
            this.boundingBox.d(d0, d1, d2);
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.height - (double) this.br;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
        } else {
            this.br *= 0.4F;
            double d3 = this.locX;
            double d4 = this.locZ;

            if (this.bf) {
                this.bf = false;
                d0 *= 0.25D;
                d1 *= 0.05000000074505806D;
                d2 *= 0.25D;
                this.motX = 0.0D;
                this.motY = 0.0D;
                this.motZ = 0.0D;
            }

            double d5 = d0;
            double d6 = d1;
            double d7 = d2;
            AxisAlignedBB axisalignedbb = this.boundingBox.clone();
            boolean flag = this.onGround && this.isSneaking();

            if (flag) {
                double d8;

                for (d8 = 0.05D; d0 != 0.0D && this.world.getEntities(this, this.boundingBox.c(d0, -1.0D, 0.0D)).size() == 0; d5 = d0) {
                    if (d0 < d8 && d0 >= -d8) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d8;
                    } else {
                        d0 += d8;
                    }
                }

                for (; d2 != 0.0D && this.world.getEntities(this, this.boundingBox.c(0.0D, -1.0D, d2)).size() == 0; d7 = d2) {
                    if (d2 < d8 && d2 >= -d8) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d8;
                    } else {
                        d2 += d8;
                    }
                }
            }

            List list = this.world.getEntities(this, this.boundingBox.a(d0, d1, d2));

            for (int i = 0; i < list.size(); ++i) {
                d1 = ((AxisAlignedBB) list.get(i)).b(this.boundingBox, d1);
            }

            this.boundingBox.d(0.0D, d1, 0.0D);
            if (!this.bg && d6 != d1) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            boolean flag1 = this.onGround || d6 != d1 && d6 < 0.0D;

            int j;

            for (j = 0; j < list.size(); ++j) {
                d0 = ((AxisAlignedBB) list.get(j)).a(this.boundingBox, d0);
            }

            this.boundingBox.d(d0, 0.0D, 0.0D);
            if (!this.bg && d5 != d0) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            for (j = 0; j < list.size(); ++j) {
                d2 = ((AxisAlignedBB) list.get(j)).c(this.boundingBox, d2);
            }

            this.boundingBox.d(0.0D, 0.0D, d2);
            if (!this.bg && d7 != d2) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            double d9;
            double d10;
            int k;

            if (this.bs > 0.0F && flag1 && (flag || this.br < 0.05F) && (d5 != d0 || d7 != d2)) {
                d9 = d0;
                d10 = d1;
                double d11 = d2;

                d0 = d5;
                d1 = (double) this.bs;
                d2 = d7;
                AxisAlignedBB axisalignedbb1 = this.boundingBox.clone();

                this.boundingBox.b(axisalignedbb);
                list = this.world.getEntities(this, this.boundingBox.a(d5, d1, d7));

                for (k = 0; k < list.size(); ++k) {
                    d1 = ((AxisAlignedBB) list.get(k)).b(this.boundingBox, d1);
                }

                this.boundingBox.d(0.0D, d1, 0.0D);
                if (!this.bg && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    d0 = ((AxisAlignedBB) list.get(k)).a(this.boundingBox, d0);
                }

                this.boundingBox.d(d0, 0.0D, 0.0D);
                if (!this.bg && d5 != d0) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    d2 = ((AxisAlignedBB) list.get(k)).c(this.boundingBox, d2);
                }

                this.boundingBox.d(0.0D, 0.0D, d2);
                if (!this.bg && d7 != d2) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                if (!this.bg && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                } else {
                    d1 = (double) (-this.bs);

                    for (k = 0; k < list.size(); ++k) {
                        d1 = ((AxisAlignedBB) list.get(k)).b(this.boundingBox, d1);
                    }

                    this.boundingBox.d(0.0D, d1, 0.0D);
                }

                if (d9 * d9 + d11 * d11 >= d0 * d0 + d2 * d2) {
                    d0 = d9;
                    d1 = d10;
                    d2 = d11;
                    this.boundingBox.b(axisalignedbb1);
                } else {
                    double d12 = this.boundingBox.b - (double) ((int) this.boundingBox.b);

                    if (d12 > 0.0D) {
                        this.br = (float) ((double) this.br + d12 + 0.01D);
                    }
                }
            }

            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.height - (double) this.br;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
            this.positionChanged = d5 != d0 || d7 != d2;
            this.bc = d6 != d1;
            this.onGround = d6 != d1 && d6 < 0.0D;
            this.bd = this.positionChanged || this.bc;
            this.a(d1, this.onGround);
            if (d5 != d0) {
                this.motX = 0.0D;
            }

            if (d6 != d1) {
                this.motY = 0.0D;
            }

            if (d7 != d2) {
                this.motZ = 0.0D;
            }

            d9 = this.locX - d3;
            d10 = this.locZ - d4;
            int l;
            int i1;
            int j1;

            // CraftBukkit start
            if ((this.positionChanged) && (getBukkitEntity() instanceof Vehicle)) {
                Vehicle vehicle = (Vehicle) getBukkitEntity();
                org.bukkit.World wrld = ((WorldServer) world).getWorld();
                org.bukkit.block.Block block = wrld.getBlockAt(MathHelper.floor(locX), MathHelper.floor(locY - 0.20000000298023224D - (double) this.height), MathHelper.floor(locZ));

                if (d5 > d0) {
                    block = block.getFace(BlockFace.SOUTH);
                } else if (d5 < d0) {
                    block = block.getFace(BlockFace.NORTH);
                } else if (d7 > d2) {
                    block = block.getFace(BlockFace.WEST);
                } else if (d7 < d2) {
                    block = block.getFace(BlockFace.EAST);
                }

                VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, block);
                ((WorldServer) world).getServer().getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            if (this.n() && !flag && this.vehicle == null) {
                this.bm = (float) ((double) this.bm + (double) MathHelper.a(d9 * d9 + d10 * d10) * 0.6D);
                l = MathHelper.floor(this.locX);
                i1 = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
                j1 = MathHelper.floor(this.locZ);
                k = this.world.getTypeId(l, i1, j1);
                if (this.world.getTypeId(l, i1 - 1, j1) == Block.FENCE.id) {
                    k = this.world.getTypeId(l, i1 - 1, j1);
                }

                if (this.bm > (float) this.b && k > 0) {
                    ++this.b;
                    StepSound stepsound = Block.byId[k].stepSound;

                    if (this.world.getTypeId(l, i1 + 1, j1) == Block.SNOW.id) {
                        stepsound = Block.SNOW.stepSound;
                        this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                    } else if (!Block.byId[k].material.isLiquid()) {
                        this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                    }

                    Block.byId[k].b(this.world, l, i1, j1, this);
                }
            }

            l = MathHelper.floor(this.boundingBox.a + 0.0010D);
            i1 = MathHelper.floor(this.boundingBox.b + 0.0010D);
            j1 = MathHelper.floor(this.boundingBox.c + 0.0010D);
            k = MathHelper.floor(this.boundingBox.d - 0.0010D);
            int k1 = MathHelper.floor(this.boundingBox.e - 0.0010D);
            int l1 = MathHelper.floor(this.boundingBox.f - 0.0010D);

            if (this.world.a(l, i1, j1, k, k1, l1)) {
                for (int i2 = l; i2 <= k; ++i2) {
                    for (int j2 = i1; j2 <= k1; ++j2) {
                        for (int k2 = j1; k2 <= l1; ++k2) {
                            int l2 = this.world.getTypeId(i2, j2, k2);

                            if (l2 > 0) {
                                Block.byId[l2].a(this.world, i2, j2, k2, this);
                            }
                        }
                    }
                }
            }

            boolean flag2 = this.ab();

            if (this.world.d(this.boundingBox.shrink(0.0010D, 0.0010D, 0.0010D))) {
                this.a(1);
                if (!flag2) {
                    ++this.fireTicks;
                    // CraftBukkit start
                    if (this.fireTicks <= 0) {
                        // not on fire yet
                        CraftServer server = ((WorldServer) this.world).getServer();
                        org.bukkit.entity.Entity damagee = this.getBukkitEntity();

                        EntityCombustEvent event = new EntityCombustEvent(damagee);
                        server.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.fireTicks = 300;
                        }
                    } else {
                        // CraftBukkit end - reset fire level back to max
                        this.fireTicks = 300;
                    }
                }
            } else if (this.fireTicks <= 0) {
                this.fireTicks = -this.maxFireTicks;
            }

            if (flag2 && this.fireTicks > 0) {
                this.world.makeSound(this, "random.fizz", 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.fireTicks = -this.maxFireTicks;
            }
        }
    }

    protected boolean n() {
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

    public AxisAlignedBB e_() {
        return null;
    }

    protected void a(int i) {
        if (!this.bD) {
            // CraftBukkit start
            if (this instanceof EntityLiving) {
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.FIRE;
                int damageDone = i;

                EntityDamageEvent event = new EntityDamageEvent(damagee, damageType, damageDone);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                i = event.getDamage();
            }
            // CraftBukkit end
            this.damageEntity((Entity) null, i);
        }
    }

    protected void a(float f) {
        if (this.passenger != null) {
            this.passenger.a(f);
        }
    }

    public boolean ab() {
        return this.bA || this.world.s(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean ac() {
        return this.bA;
    }

    public boolean f_() {
        return this.world.a(this.boundingBox.b(0.0D, -0.4000000059604645D, 0.0D).shrink(0.0010D, 0.0010D, 0.0010D), Material.WATER, this);
    }

    public boolean a(Material material) {
        double d0 = this.locY + (double) this.s();
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.d((float) MathHelper.floor(d0));
        int k = MathHelper.floor(this.locZ);
        int l = this.world.getTypeId(i, j, k);

        if (l != 0 && Block.byId[l].material == material) {
            float f = BlockFluids.c(this.world.getData(i, j, k)) - 0.11111111F;
            float f1 = (float) (j + 1) - f;

            return d0 < (double) f1;
        } else {
            return false;
        }
    }

    public float s() {
        return 0.0F;
    }

    public boolean ad() {
        return this.world.a(this.boundingBox.b(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void a(float f, float f1, float f2) {
        float f3 = MathHelper.c(f * f + f1 * f1);

        if (f3 >= 0.01F) {
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
        double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.66D;
        int j = MathHelper.floor(this.locY - (double) this.height + d0);
        int k = MathHelper.floor(this.locZ);

        return this.world.a(MathHelper.floor(this.boundingBox.a), MathHelper.floor(this.boundingBox.b), MathHelper.floor(this.boundingBox.c), MathHelper.floor(this.boundingBox.d), MathHelper.floor(this.boundingBox.e), MathHelper.floor(this.boundingBox.f)) ? this.world.m(i, j, k) : 0.0F;
    }

    public void a(World world) {
        this.world = world;
    }

    public void setLocation(double d0, double d1, double d2, float f, float f1) {
        this.lastX = this.locX = d0;
        this.lastY = this.locY = d1;
        this.lastZ = this.locZ = d2;
        this.lastYaw = this.yaw = f;
        this.lastPitch = this.pitch = f1;
        this.br = 0.0F;
        double d3 = (double) (this.lastYaw - f);

        if (d3 < -180.0D) {
            this.lastYaw += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.lastYaw -= 360.0F;
        }

        this.setPosition(this.locX, this.locY, this.locZ);
        this.c(f, f1);
    }

    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        this.bo = this.lastX = this.locX = d0;
        this.bp = this.lastY = this.locY = d1 + (double) this.height;
        this.bq = this.lastZ = this.locZ = d2;
        this.yaw = f;
        this.pitch = f1;
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    public float f(Entity entity) {
        float f = (float) (this.locX - entity.locX);
        float f1 = (float) (this.locY - entity.locY);
        float f2 = (float) (this.locZ - entity.locZ);

        return MathHelper.c(f * f + f1 * f1 + f2 * f2);
    }

    public double d(double d0, double d1, double d2) {
        double d3 = this.locX - d0;
        double d4 = this.locY - d1;
        double d5 = this.locZ - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double e(double d0, double d1, double d2) {
        double d3 = this.locX - d0;
        double d4 = this.locY - d1;
        double d5 = this.locZ - d2;

        return (double) MathHelper.a(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double g(Entity entity) {
        double d0 = this.locX - entity.locX;
        double d1 = this.locY - entity.locY;
        double d2 = this.locZ - entity.locZ;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void b(EntityHuman entityhuman) {}

    public void collide(Entity entity) {
        if (entity.passenger != this && entity.vehicle != this) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;
            double d2 = MathHelper.a(d0, d1);

            if (d2 >= 0.009999999776482582D) {
                d2 = (double) MathHelper.a(d2);
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
                d0 *= (double) (1.0F - this.bu);
                d1 *= (double) (1.0F - this.bu);
                this.f(-d0, 0.0D, -d1);
                entity.f(d0, 0.0D, d1);
            }
        }
    }

    public void f(double d0, double d1, double d2) {
        this.motX += d0;
        this.motY += d1;
        this.motZ += d2;
    }

    protected void ae() {
        this.velocityChanged = true;
    }

    public boolean damageEntity(Entity entity, int i) {
        this.ae();
        return false;
    }

    public boolean n_() {
        return false;
    }

    public boolean d_() {
        return false;
    }

    public void c(Entity entity, int i) {}

    public boolean c(NBTTagCompound nbttagcompound) {
        String s = this.af();

        if (!this.dead && s != null) {
            nbttagcompound.setString("id", s);
            this.d(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public void d(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Pos", (NBTBase) this.a(new double[] { this.locX, this.locY + (double) this.br, this.locZ}));
        nbttagcompound.a("Motion", (NBTBase) this.a(new double[] { this.motX, this.motY, this.motZ}));

        // CraftBukkit start - checking for NaN pitch/yaw and resetting to zero
        // TODO: make sure this is the best way to address this.
        if (Float.isNaN(this.yaw)) {
            this.yaw = 0;
        }

        if (Float.isNaN(this.pitch)) {
            this.pitch = 0;
        }
        // CraftBukkit end

        nbttagcompound.a("Rotation", (NBTBase) this.a(new float[] { this.yaw, this.pitch}));
        nbttagcompound.a("FallDistance", this.fallDistance);
        nbttagcompound.a("Fire", (short) this.fireTicks);
        nbttagcompound.a("Air", (short) this.airTicks);
        nbttagcompound.a("OnGround", this.onGround);
        nbttagcompound.setString("World", world.worldData.name); // CraftBukkit
        nbttagcompound.a("UUIDLeast", this.uniqueId.getLeastSignificantBits()); // CraftBukkit
        nbttagcompound.a("UUIDMost", this.uniqueId.getMostSignificantBits()); // CraftBukkit
        this.b(nbttagcompound);
    }

    public void e(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.l("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.l("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.l("Rotation");

        this.motX = ((NBTTagDouble) nbttaglist1.a(0)).a;
        this.motY = ((NBTTagDouble) nbttaglist1.a(1)).a;
        this.motZ = ((NBTTagDouble) nbttaglist1.a(2)).a;

        this.lastX = this.bo = this.locX = ((NBTTagDouble) nbttaglist.a(0)).a;
        this.lastY = this.bp = this.locY = ((NBTTagDouble) nbttaglist.a(1)).a;
        this.lastZ = this.bq = this.locZ = ((NBTTagDouble) nbttaglist.a(2)).a;
        this.lastYaw = this.yaw = ((NBTTagFloat) nbttaglist2.a(0)).a;
        this.lastPitch = this.pitch = ((NBTTagFloat) nbttaglist2.a(1)).a;
        this.fallDistance = nbttagcompound.g("FallDistance");
        this.fireTicks = nbttagcompound.d("Fire");
        this.airTicks = nbttagcompound.d("Air");
        this.onGround = nbttagcompound.m("OnGround");
        this.setPosition(this.locX, this.locY, this.locZ);

        // CraftBukkit start
        long least = nbttagcompound.f("UUIDLeast");
        long most = nbttagcompound.f("UUIDMost");

        if (least != 0L && most != 0L) {
            this.uniqueId = new UUID(most, least);
        }
        // CraftBukkit end

        this.c(this.yaw, this.pitch);
        this.a(nbttagcompound);

        // CraftBukkit start - Exempt Vehicles from notch's sanity check
        if (!(this.getBukkitEntity() instanceof CraftVehicle)) {
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
    }

    protected final String af() {
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

            nbttaglist.a((NBTBase) (new NBTTagDouble(d0)));
        }

        return nbttaglist;
    }

    protected NBTTagList a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.a((NBTBase) (new NBTTagFloat(f)));
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

    public boolean S() {
        return !this.dead;
    }

    public boolean J() {
        for (int i = 0; i < 8; ++i) {
            float f = ((float) ((i >> 0) % 2) - 0.5F) * this.length * 0.9F;
            float f1 = ((float) ((i >> 1) % 2) - 0.5F) * 0.1F;
            float f2 = ((float) ((i >> 2) % 2) - 0.5F) * this.length * 0.9F;
            int j = MathHelper.floor(this.locX + (double) f);
            int k = MathHelper.floor(this.locY + (double) this.s() + (double) f1);
            int l = MathHelper.floor(this.locZ + (double) f2);

            if (this.world.d(j, k, l)) {
                return true;
            }
        }

        return false;
    }

    public boolean a(EntityHuman entityhuman) {
        return false;
    }

    public AxisAlignedBB a_(Entity entity) {
        return null;
    }

    public void D() {
        if (this.vehicle.dead) {
            this.vehicle = null;
        } else {
            this.motX = 0.0D;
            this.motY = 0.0D;
            this.motZ = 0.0D;
            this.o_();
            if (this.vehicle != null) {
                this.vehicle.f();
                this.e += (double) (this.vehicle.yaw - this.vehicle.lastYaw);

                for (this.d += (double) (this.vehicle.pitch - this.vehicle.lastPitch); this.e >= 180.0D; this.e -= 360.0D) {
                    ;
                }

                while (this.e < -180.0D) {
                    this.e += 360.0D;
                }

                while (this.d >= 180.0D) {
                    this.d -= 360.0D;
                }

                while (this.d < -180.0D) {
                    this.d += 360.0D;
                }

                double d0 = this.e * 0.5D;
                double d1 = this.d * 0.5D;
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

                this.e -= d0;
                this.d -= d1;
                this.yaw = (float) ((double) this.yaw + d0);
                this.pitch = (float) ((double) this.pitch + d1);
            }
        }
    }

    public void f() {
        this.passenger.setPosition(this.locX, this.locY + this.m() + this.passenger.H(), this.locZ);
    }

    public double H() {
        return (double) this.height;
    }

    public double m() {
        return (double) this.width * 0.75D;
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        setPassengerOf(entity);
    }

    protected org.bukkit.entity.Entity bukkitEntity;

    public org.bukkit.entity.Entity getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = org.bukkit.craftbukkit.entity.CraftEntity.getEntity(((WorldServer) this.world).getServer(), this);
        }
        return bukkitEntity;
    }

    public void setPassengerOf(Entity entity) {
        // b(null) doesn't really fly for overloaded methods,
        // so this method is needed

        // CraftBukkit end
        this.d = 0.0D;
        this.e = 0.0D;
        if (entity == null) {
            if (this.vehicle != null) {
                // CraftBukkit start
                if ((this.getBukkitEntity() instanceof LivingEntity) && (vehicle.getBukkitEntity() instanceof CraftVehicle)) {
                    CraftVehicle cvehicle = (CraftVehicle) vehicle.getBukkitEntity();
                    LivingEntity living = (LivingEntity) getBukkitEntity();

                    VehicleExitEvent event = new VehicleExitEvent(cvehicle, living);
                    ((WorldServer) world).getServer().getPluginManager().callEvent(event);
                }
                // CraftBukkit end

                this.setPositionRotation(this.vehicle.locX, this.vehicle.boundingBox.b + (double) this.vehicle.width, this.vehicle.locZ, this.yaw, this.pitch);
                this.vehicle.passenger = null;
            }

            this.vehicle = null;
        } else if (this.vehicle == entity) {
            // CraftBukkit start
            if ((this.getBukkitEntity() instanceof LivingEntity) && (vehicle.getBukkitEntity() instanceof CraftVehicle)) {
                CraftVehicle cvehicle = (CraftVehicle) vehicle.getBukkitEntity();
                LivingEntity living = (LivingEntity) getBukkitEntity();

                VehicleExitEvent event = new VehicleExitEvent(cvehicle, living);
                ((WorldServer) world).getServer().getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            this.vehicle.passenger = null;
            this.vehicle = null;
            this.setPositionRotation(entity.locX, entity.boundingBox.b + (double) entity.width, entity.locZ, this.yaw, this.pitch);
        } else {
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

    public Vec3D Y() {
        return null;
    }

    public void O() {}

    public ItemStack[] getEquipment() {
        return null;
    }

    public boolean isSneaking() {
        return this.d(1);
    }

    public void setSneak(boolean flag) {
        this.a(1, flag);
    }

    protected boolean d(int i) {
        return (this.datawatcher.a(0) & 1 << i) != 0;
    }

    protected void a(int i, boolean flag) {
        byte b0 = this.datawatcher.a(0);

        if (flag) {
            this.datawatcher.b(0, Byte.valueOf((byte) (b0 | 1 << i)));
        } else {
            this.datawatcher.b(0, Byte.valueOf((byte) (b0 & ~(1 << i))));
        }
    }

    public void a(EntityWeatherStorm entityweatherstorm) {
        // CraftBukkit start
        int damage = 5;
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entityweatherstorm.getBukkitEntity(), getBukkitEntity(), DamageCause.LIGHTNING, damage);
        Bukkit.getServer().getPluginManager().callEvent(event);
        damage = event.getDamage();

        if (event.isCancelled()) {
            return;
        }

        this.a(damage);
        // CraftBukkit end

        ++this.fireTicks;
        if (this.fireTicks == 0) {
            this.fireTicks = 300;
        }
    }

    public void a(EntityLiving entityliving) {}

    protected boolean g(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);
        double d3 = d0 - (double) i;
        double d4 = d1 - (double) j;
        double d5 = d2 - (double) k;

        if (this.world.d(i, j, k)) {
            boolean flag = !this.world.d(i - 1, j, k);
            boolean flag1 = !this.world.d(i + 1, j, k);
            boolean flag2 = !this.world.d(i, j - 1, k);
            boolean flag3 = !this.world.d(i, j + 1, k);
            boolean flag4 = !this.world.d(i, j, k - 1);
            boolean flag5 = !this.world.d(i, j, k + 1);
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
        }

        return false;
    }
}
