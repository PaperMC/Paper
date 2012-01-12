package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
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
    public double bb;
    public boolean bc;
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
    public boolean bw;
    public boolean bx;
    public boolean velocityChanged;
    protected boolean bz;
    public boolean bA;
    public boolean dead;
    public float height;
    public float width;
    public float length;
    public float bF;
    public float bG;
    public float fallDistance; // CraftBukkit - private -> public
    private int b;
    public double bI;
    public double bJ;
    public double bK;
    public float bL;
    public float bM;
    public boolean bN;
    public float bO;
    protected Random random;
    public int ticksLived;
    public int maxFireTicks;
    public int fireTicks; // CraftBukkit - private -> public
    protected boolean bS;
    public int noDamageTicks;
    private boolean justCreated;
    protected boolean fireProof;
    protected DataWatcher datawatcher;
    private double e;
    private double f;
    public boolean bW;
    public int bX;
    public int bY;
    public int bZ;
    public boolean ca;
    public boolean cb;
    public UUID uniqueId = UUID.randomUUID(); // CraftBukkit

    public Entity(World world) {
        this.id = entityCount++;
        this.bb = 1.0D;
        this.bc = false;
        this.boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.bx = false;
        this.velocityChanged = false;
        this.bA = true;
        this.dead = false;
        this.height = 0.0F;
        this.width = 0.6F;
        this.length = 1.8F;
        this.bF = 0.0F;
        this.bG = 0.0F;
        this.fallDistance = 0.0F;
        this.b = 1;
        this.bL = 0.0F;
        this.bM = 0.0F;
        this.bN = false;
        this.bO = 0.0F;
        this.random = new Random();
        this.ticksLived = 0;
        this.maxFireTicks = 1;
        this.fireTicks = 0;
        this.bS = false;
        this.noDamageTicks = 0;
        this.justCreated = true;
        this.fireProof = false;
        this.datawatcher = new DataWatcher();
        this.bW = false;
        this.world = world;
        this.setPosition(0.0D, 0.0D, 0.0D);
        this.datawatcher.a(0, Byte.valueOf((byte) 0));
        this.datawatcher.a(1, Short.valueOf((short) 300));
        this.b();
    }

    protected abstract void b();

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

    protected void b(float f, float f1) {
        this.width = f;
        this.length = f1;
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

        this.boundingBox.c(d0 - (double) f, d1 - (double) this.height + (double) this.bL, d2 - (double) f, d0 + (double) f, d1 - (double) this.height + (double) this.bL + (double) f1, d2 + (double) f);
    }

    public void w_() {
        this.af();
    }

    public void af() {
        // MethodProfiler.a("entityBaseTick"); // CraftBukkit - not in production code
        if (this.vehicle != null && this.vehicle.dead) {
            this.vehicle = null;
        }

        ++this.ticksLived;
        this.bF = this.bG;
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
        int i;

        if (this.isSprinting()) {
            int j = MathHelper.floor(this.locX);
            int k = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);

            i = MathHelper.floor(this.locZ);
            int l = this.world.getTypeId(j, k, i);

            if (l > 0) {
                this.world.a("tilecrack_" + l, this.locX + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.b + 0.1D, this.locZ + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, -this.motX * 4.0D, 1.5D, -this.motZ * 4.0D);
            }
        }

        if (this.i_()) {
            if (!this.bS && !this.justCreated) {
                float f = MathHelper.a(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                this.world.makeSound(this, "random.splash", f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                float f1 = (float) MathHelper.floor(this.boundingBox.b);

                float f2;
                float f3;

                for (i = 0; (float) i < 1.0F + this.width * 20.0F; ++i) {
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.world.a("bubble", this.locX + (double) f3, (double) (f1 + 1.0F), this.locZ + (double) f2, this.motX, this.motY - (double) (this.random.nextFloat() * 0.2F), this.motZ);
                }

                for (i = 0; (float) i < 1.0F + this.width * 20.0F; ++i) {
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.world.a("splash", this.locX + (double) f3, (double) (f1 + 1.0F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }

            this.fallDistance = 0.0F;
            this.bS = true;
            this.fireTicks = 0;
        } else {
            this.bS = false;
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

        if (this.aA()) {
            this.av();
            this.fallDistance *= 0.5F;
        }

        if (this.locY < -64.0D) {
            this.ao();
        }

        if (!this.world.isStatic) {
            this.a(0, this.fireTicks > 0);
            this.a(2, this.vehicle != null);
        }

        this.justCreated = false;
        // MethodProfiler.a(); // CraftBukkit - not in production code
    }

    protected void av() {
        if (!this.fireProof) {
            // CraftBukkit start - fallen in lava TODO: this event spams!
            if (this instanceof EntityLiving) {
                org.bukkit.Server server = this.world.getServer();

                // TODO: shouldn't be sending null for the block.
                org.bukkit.block.Block damager = null; // ((WorldServer) this.l).getWorld().getBlockAt(i, j, k);
                org.bukkit.entity.Entity damagee = this.getBukkitEntity();

                EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, EntityDamageEvent.DamageCause.LAVA, 4);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.damageEntity(DamageSource.LAVA, event.getDamage());
                }

                if (this.fireTicks <= 0) {
                    // not on fire yet
                    EntityCombustEvent combustEvent = new EntityCombustByBlockEvent(damager, damagee, 15);
                    server.getPluginManager().callEvent(combustEvent);

                    if (!combustEvent.isCancelled()) {
                        this.setOnFire(combustEvent.getDuration());
                    }
                } else {
                    // This will be called every single tick the entity is in lava, so don't throw an event.
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

    protected void ao() {
        this.die();
    }

    public boolean d(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.boundingBox.c(d0, d1, d2);
        List list = this.world.getEntities(this, axisalignedbb);

        return list.size() > 0 ? false : !this.world.c(axisalignedbb);
    }

    public void move(double d0, double d1, double d2) {
        if (this.bN) {
            this.boundingBox.d(d0, d1, d2);
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.height - (double) this.bL;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
        } else {
            // MethodProfiler.a("move"); // CraftBukkit - not in production code
            this.bL *= 0.4F;
            double d3 = this.locX;
            double d4 = this.locZ;

            if (this.bz) {
                this.bz = false;
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
            if (!this.bA && d6 != d1) {
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
            if (!this.bA && d5 != d0) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            for (j = 0; j < list.size(); ++j) {
                d2 = ((AxisAlignedBB) list.get(j)).c(this.boundingBox, d2);
            }

            this.boundingBox.d(0.0D, 0.0D, d2);
            if (!this.bA && d7 != d2) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            double d9;
            double d10;
            int k;

            if (this.bM > 0.0F && flag1 && (flag || this.bL < 0.05F) && (d5 != d0 || d7 != d2)) {
                d9 = d0;
                d10 = d1;
                double d11 = d2;

                d0 = d5;
                d1 = (double) this.bM;
                d2 = d7;
                AxisAlignedBB axisalignedbb1 = this.boundingBox.clone();

                this.boundingBox.b(axisalignedbb);
                list = this.world.getEntities(this, this.boundingBox.a(d5, d1, d7));

                for (k = 0; k < list.size(); ++k) {
                    d1 = ((AxisAlignedBB) list.get(k)).b(this.boundingBox, d1);
                }

                this.boundingBox.d(0.0D, d1, 0.0D);
                if (!this.bA && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    d0 = ((AxisAlignedBB) list.get(k)).a(this.boundingBox, d0);
                }

                this.boundingBox.d(d0, 0.0D, 0.0D);
                if (!this.bA && d5 != d0) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    d2 = ((AxisAlignedBB) list.get(k)).c(this.boundingBox, d2);
                }

                this.boundingBox.d(0.0D, 0.0D, d2);
                if (!this.bA && d7 != d2) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                if (!this.bA && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                } else {
                    d1 = (double) (-this.bM);

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
                        this.bL = (float) ((double) this.bL + d12 + 0.01D);
                    }
                }
            }

            // MethodProfiler.a(); // CraftBukkit - not in production code
            // MethodProfiler.a("rest"); // CraftBukkit - not in production code
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.locY = this.boundingBox.b + (double) this.height - (double) this.bL;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
            this.positionChanged = d5 != d0 || d7 != d2;
            this.bw = d6 != d1;
            this.onGround = d6 != d1 && d6 < 0.0D;
            this.bx = this.positionChanged || this.bw;
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
            if ((this.positionChanged) && (this.getBukkitEntity() instanceof Vehicle)) {
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.block.Block block = this.world.getWorld().getBlockAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height), MathHelper.floor(this.locZ));

                if (d5 > d0) {
                    block = block.getRelative(BlockFace.SOUTH);
                } else if (d5 < d0) {
                    block = block.getRelative(BlockFace.NORTH);
                } else if (d7 > d2) {
                    block = block.getRelative(BlockFace.WEST);
                } else if (d7 < d2) {
                    block = block.getRelative(BlockFace.EAST);
                }

                VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, block);
                this.world.getServer().getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            if (this.g_() && !flag && this.vehicle == null) {
                this.bG = (float) ((double) this.bG + (double) MathHelper.a(d9 * d9 + d10 * d10) * 0.6D);
                l = MathHelper.floor(this.locX);
                i1 = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
                j1 = MathHelper.floor(this.locZ);
                k = this.world.getTypeId(l, i1, j1);
                if (k == 0 && this.world.getTypeId(l, i1 - 1, j1) == Block.FENCE.id) {
                    k = this.world.getTypeId(l, i1 - 1, j1);
                }

                if (this.bG > (float) this.b && k > 0) {
                    this.b = (int) this.bG + 1;
                    this.a(l, i1, j1, k);
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

            boolean flag2 = this.ay();

            if (this.world.d(this.boundingBox.shrink(0.0010D, 0.0010D, 0.0010D))) {
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

            // MethodProfiler.a(); // CraftBukkit - not in production code
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

    protected boolean g_() {
        return true;
    }

    protected void a(double d0, boolean flag) {
        if (flag) {
            if (this.fallDistance > 0.0F) {
                this.b(this.fallDistance);
                this.fallDistance = 0.0F;
            }
        } else if (d0 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - d0);
        }
    }

    public AxisAlignedBB h_() {
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
            }
            // CraftBukkit end
            this.damageEntity(DamageSource.FIRE, i);
        }
    }

    public final boolean isFireproof() {
        return this.fireProof;
    }

    protected void b(float f) {
        if (this.passenger != null) {
            this.passenger.b(f);
        }
    }

    public boolean ay() {
        return this.bS || this.world.v(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean az() {
        return this.bS;
    }

    public boolean i_() {
        return this.world.a(this.boundingBox.b(0.0D, -0.4000000059604645D, 0.0D).shrink(0.0010D, 0.0010D, 0.0010D), Material.WATER, this);
    }

    public boolean a(Material material) {
        double d0 = this.locY + (double) this.x();
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

    public float x() {
        return 0.0F;
    }

    public boolean aA() {
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

    public float a(float f) {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locZ);

        if (this.world.isLoaded(i, this.world.height / 2, j)) {
            double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.66D;
            int k = MathHelper.floor(this.locY - (double) this.height + d0);

            return this.world.m(i, k, j);
        } else {
            return 0.0F;
        }
    }

    public void spawnIn(World world) {
        // CraftBukkit start
        if (world == null) {
            this.die();
            this.world = ((org.bukkit.craftbukkit.CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
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
        this.bL = 0.0F;
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
        this.bI = this.lastX = this.locX = d0;
        this.bJ = this.lastY = this.locY = d1 + (double) this.height;
        this.bK = this.lastZ = this.locZ = d2;
        this.yaw = f;
        this.pitch = f1;
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    public float h(Entity entity) {
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

        return (double) MathHelper.a(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double i(Entity entity) {
        double d0 = this.locX - entity.locX;
        double d1 = this.locY - entity.locY;
        double d2 = this.locZ - entity.locZ;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void a_(EntityHuman entityhuman) {}

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
                d0 *= (double) (1.0F - this.bO);
                d1 *= (double) (1.0F - this.bO);
                this.b_(-d0, 0.0D, -d1);
                entity.b_(d0, 0.0D, d1);
            }
        }
    }

    public void b_(double d0, double d1, double d2) {
        this.motX += d0;
        this.motY += d1;
        this.motZ += d2;
        this.cb = true;
    }

    protected void aB() {
        this.velocityChanged = true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.aB();
        return false;
    }

    public boolean e_() {
        return false;
    }

    public boolean f_() {
        return false;
    }

    public void b(Entity entity, int i) {}

    public boolean c(NBTTagCompound nbttagcompound) {
        String s = this.aC();

        if (!this.dead && s != null) {
            nbttagcompound.setString("id", s);
            this.d(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public void d(NBTTagCompound nbttagcompound) {
        nbttagcompound.set("Pos", this.a(new double[] { this.locX, this.locY + (double) this.bL, this.locZ}));
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
        // CraftBukkit start
        nbttagcompound.setLong("WorldUUIDLeast", this.world.getUUID().getLeastSignificantBits());
        nbttagcompound.setLong("WorldUUIDMost", this.world.getUUID().getMostSignificantBits());
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

        this.lastX = this.bI = this.locX = ((NBTTagDouble) nbttaglist.get(0)).data;
        this.lastY = this.bJ = this.locY = ((NBTTagDouble) nbttaglist.get(1)).data;
        this.lastZ = this.bK = this.locZ = ((NBTTagDouble) nbttaglist.get(2)).data;
        this.lastYaw = this.yaw = ((NBTTagFloat) nbttaglist2.get(0)).data;
        this.lastPitch = this.pitch = ((NBTTagFloat) nbttaglist2.get(1)).data;
        this.fallDistance = nbttagcompound.getFloat("FallDistance");
        this.fireTicks = nbttagcompound.getShort("Fire");
        this.setAirTicks(nbttagcompound.getShort("Air"));
        this.onGround = nbttagcompound.getBoolean("OnGround");
        this.setPosition(this.locX, this.locY, this.locZ);

        // CraftBukkit start
        long least = nbttagcompound.getLong("UUIDLeast");
        long most = nbttagcompound.getLong("UUIDMost");

        if (least != 0L && most != 0L) {
            this.uniqueId = new UUID(most, least);
        }
        // CraftBukkit end

        this.c(this.yaw, this.pitch);
        this.a(nbttagcompound);

        // CraftBukkit start - Exempt Vehicles from notch's sanity check
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
            org.bukkit.Server server = Bukkit.getServer();
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

            this.spawnIn(bworld == null ? null : ((org.bukkit.craftbukkit.CraftWorld) bworld).getHandle());
        }
        // CraftBukkit end
    }

    protected final String aC() {
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

    public boolean T() {
        for (int i = 0; i < 8; ++i) {
            float f = ((float) ((i >> 0) % 2) - 0.5F) * this.width * 0.8F;
            float f1 = ((float) ((i >> 1) % 2) - 0.5F) * 0.1F;
            float f2 = ((float) ((i >> 2) % 2) - 0.5F) * this.width * 0.8F;
            int j = MathHelper.floor(this.locX + (double) f);
            int k = MathHelper.floor(this.locY + (double) this.x() + (double) f1);
            int l = MathHelper.floor(this.locZ + (double) f2);

            if (this.world.e(j, k, l)) {
                return true;
            }
        }

        return false;
    }

    public boolean b(EntityHuman entityhuman) {
        return false;
    }

    public AxisAlignedBB a_(Entity entity) {
        return null;
    }

    public void M() {
        if (this.vehicle.dead) {
            this.vehicle = null;
        } else {
            this.motX = 0.0D;
            this.motY = 0.0D;
            this.motZ = 0.0D;
            this.w_();
            if (this.vehicle != null) {
                this.vehicle.i();
                this.f += (double) (this.vehicle.yaw - this.vehicle.lastYaw);

                for (this.e += (double) (this.vehicle.pitch - this.vehicle.lastPitch); this.f >= 180.0D; this.f -= 360.0D) {
                    ;
                }

                while (this.f < -180.0D) {
                    this.f += 360.0D;
                }

                while (this.e >= 180.0D) {
                    this.e -= 360.0D;
                }

                while (this.e < -180.0D) {
                    this.e += 360.0D;
                }

                double d0 = this.f * 0.5D;
                double d1 = this.e * 0.5D;
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

                this.f -= d0;
                this.e -= d1;
                this.yaw = (float) ((double) this.yaw + d0);
                this.pitch = (float) ((double) this.pitch + d1);
            }
        }
    }

    public void i() {
        this.passenger.setPosition(this.locX, this.locY + this.q() + this.passenger.R(), this.locZ);
    }

    public double R() {
        return (double) this.height;
    }

    public double q() {
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
        this.e = 0.0D;
        this.f = 0.0D;
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

            this.vehicle.passenger = null;
            this.vehicle = null;
            this.setPositionRotation(entity.locX, entity.boundingBox.b + (double) entity.length, entity.locZ, this.yaw, this.pitch);
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

    public float j_() {
        return 0.1F;
    }

    public Vec3D ap() {
        return null;
    }

    public void Y() {}

    public ItemStack[] getEquipment() {
        return null;
    }

    public boolean z() {
        return this.fireTicks > 0 || this.k(0);
    }

    public boolean isSneaking() {
        return this.k(1);
    }

    public void setSneak(boolean flag) {
        this.a(1, flag);
    }

    public boolean isSprinting() {
        return this.k(3);
    }

    public void setSprinting(boolean flag) {
        this.a(3, flag);
    }

    public void g(boolean flag) {
        this.a(4, flag);
    }

    protected boolean k(int i) {
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
        return this.datawatcher.b(1);
    }

    public void setAirTicks(int i) {
        this.datawatcher.watch(1, Short.valueOf((short) i));
    }

    public void a(EntityWeatherLighting entityweatherlighting) {
        // CraftBukkit start
        final org.bukkit.entity.Entity thisBukkitEntity = this.getBukkitEntity();
        final org.bukkit.entity.Entity stormBukkitEntity = entityweatherlighting.getBukkitEntity();
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

    protected boolean g(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);
        double d3 = d0 - (double) i;
        double d4 = d1 - (double) j;
        double d5 = d2 - (double) k;

        if (this.world.e(i, j, k)) {
            boolean flag = !this.world.e(i - 1, j, k);
            boolean flag1 = !this.world.e(i + 1, j, k);
            boolean flag2 = !this.world.e(i, j - 1, k);
            boolean flag3 = !this.world.e(i, j + 1, k);
            boolean flag4 = !this.world.e(i, j, k - 1);
            boolean flag5 = !this.world.e(i, j, k + 1);
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

    public void s() {
        this.bz = true;
    }

    public String ad() {
        String s = EntityTypes.b(this);

        if (s == null) {
            s = "generic";
        }

        return LocaleI18n.a("entity." + s + ".name");
    }

    public Entity[] aG() {
        return null;
    }

    public boolean a(Entity entity) {
        return this == entity;
    }
}
