package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
// CraftBukkit end

public class EntityBoat extends Entity {

    private boolean a;
    private double b;
    private int c;
    private double d;
    private double e;
    private double f;
    private double g;
    private double h;

    // CraftBukkit start
    public double maxSpeed = 0.4D;
    public double occupiedDeceleration = 0.2D;
    public double unoccupiedDeceleration = -1;
    public boolean landBoats = false;

    @Override
    public void collide(Entity entity) {
        org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

        VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), hitEntity);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        super.collide(entity);
    }
    // CraftBukkit end

    public EntityBoat(World world) {
        super(world);
        this.a = true;
        this.b = 0.07D;
        this.l = true;
        this.a(1.5F, 0.6F);
        this.height = this.length / 2.0F;
    }

    protected boolean g_() {
        return false;
    }

    protected void c() {
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(1));
        this.datawatcher.a(19, new Float(0.0F));
    }

    public AxisAlignedBB g(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB J() {
        return this.boundingBox;
    }

    public boolean S() {
        return true;
    }

    public EntityBoat(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1 + (double) this.height, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;

        this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    public double ae() {
        return (double) this.length * 0.0D - 0.30000001192092896D;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (!this.world.isStatic && !this.dead) {
            // CraftBukkit start
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();
            org.bukkit.entity.Entity attacker = (damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity();

            VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, (double) f);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // f = event.getDamage(); // TODO Why don't we do this?
            // CraftBukkit end

            this.c(-this.i());
            this.a(10);
            this.setDamage(this.getDamage() + f * 10.0F);
            this.Q();
            boolean flag = damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).abilities.canInstantlyBuild;

            if (flag || this.getDamage() > 40.0F) {
                // CraftBukkit start
                VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                this.world.getServer().getPluginManager().callEvent(destroyEvent);

                if (destroyEvent.isCancelled()) {
                    this.setDamage(40F); // Maximize damage so this doesn't get triggered again right away
                    return true;
                }
                // CraftBukkit end

                if (this.passenger != null) {
                    this.passenger.mount(this);
                }

                if (!flag) {
                    this.a(Items.BOAT, 1, 0.0F);
                }

                this.die();
            }

            return true;
        } else {
            return true;
        }
    }

    public boolean R() {
        return !this.dead;
    }

        public void h() {
        // CraftBukkit start
        double prevX = this.locX;
        double prevY = this.locY;
        double prevZ = this.locZ;
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;
        // CraftBukkit end

        super.h();
        if (this.f() > 0) {
            this.a(this.f() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        byte b0 = 5;
        double d0 = 0.0D;

        for (int i = 0; i < b0; ++i) {
            double d1 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 0) / (double) b0 - 0.125D;
            double d2 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 1) / (double) b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.a().a(this.boundingBox.a, d1, this.boundingBox.c, this.boundingBox.d, d2, this.boundingBox.f);

            if (this.world.b(axisalignedbb, Material.WATER)) {
                d0 += 1.0D / (double) b0;
            }
        }

        double d3 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
        double d4;
        double d5;
        int j;

        if (d3 > 0.26249999999999996D) {
            d4 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D);
            d5 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D);

            for (j = 0; (double) j < 1.0D + d3 * 60.0D; ++j) {
                double d6 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                double d7 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double d9;

                if (this.random.nextBoolean()) {
                    d8 = this.locX - d4 * d6 * 0.8D + d5 * d7;
                    d9 = this.locZ - d5 * d6 * 0.8D - d4 * d7;
                    this.world.addParticle("splash", d8, this.locY - 0.125D, d9, this.motX, this.motY, this.motZ);
                } else {
                    d8 = this.locX + d4 + d5 * d6 * 0.7D;
                    d9 = this.locZ + d5 - d4 * d6 * 0.7D;
                    this.world.addParticle("splash", d8, this.locY - 0.125D, d9, this.motX, this.motY, this.motZ);
                }
            }
        }

        double d10;
        double d11;

        if (this.world.isStatic && this.a) {
            if (this.c > 0) {
                d4 = this.locX + (this.d - this.locX) / (double) this.c;
                d5 = this.locY + (this.e - this.locY) / (double) this.c;
                d10 = this.locZ + (this.f - this.locZ) / (double) this.c;
                d11 = MathHelper.g(this.g - (double) this.yaw);
                this.yaw = (float) ((double) this.yaw + d11 / (double) this.c);
                this.pitch = (float) ((double) this.pitch + (this.h - (double) this.pitch) / (double) this.c);
                --this.c;
                this.setPosition(d4, d5, d10);
                this.b(this.yaw, this.pitch);
            } else {
                d4 = this.locX + this.motX;
                d5 = this.locY + this.motY;
                d10 = this.locZ + this.motZ;
                this.setPosition(d4, d5, d10);
                if (this.onGround) {
                    this.motX *= 0.5D;
                    this.motY *= 0.5D;
                    this.motZ *= 0.5D;
                }

                this.motX *= 0.9900000095367432D;
                this.motY *= 0.949999988079071D;
                this.motZ *= 0.9900000095367432D;
            }
        } else {
            if (d0 < 1.0D) {
                d4 = d0 * 2.0D - 1.0D;
                this.motY += 0.03999999910593033D * d4;
            } else {
                if (this.motY < 0.0D) {
                    this.motY /= 2.0D;
                }

                this.motY += 0.007000000216066837D;
            }

            if (this.passenger != null && this.passenger instanceof EntityLiving) {
                EntityLiving entityliving = (EntityLiving) this.passenger;
                float f = this.passenger.yaw + -entityliving.be * 90.0F;

                this.motX += -Math.sin((double) (f * 3.1415927F / 180.0F)) * this.b * (double) entityliving.bf * 0.05000000074505806D;
                this.motZ += Math.cos((double) (f * 3.1415927F / 180.0F)) * this.b * (double) entityliving.bf * 0.05000000074505806D;
            }
            // CraftBukkit start - Support unoccupied deceleration
            else if (unoccupiedDeceleration >= 0) {
                this.motX *= unoccupiedDeceleration;
                this.motZ *= unoccupiedDeceleration;
                // Kill lingering speed
                if (motX <= 0.00001) {
                    motX = 0;
                }
                if (motZ <= 0.00001) {
                    motZ = 0;
                }
            }
            // CraftBukkit end

            d4 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            if (d4 > 0.35D) {
                d5 = 0.35D / d4;
                this.motX *= d5;
                this.motZ *= d5;
                d4 = 0.35D;
            }

            if (d4 > d3 && this.b < 0.35D) {
                this.b += (0.35D - this.b) / 35.0D;
                if (this.b > 0.35D) {
                    this.b = 0.35D;
                }
            } else {
                this.b -= (this.b - 0.07D) / 35.0D;
                if (this.b < 0.07D) {
                    this.b = 0.07D;
                }
            }

            int k;

            for (k = 0; k < 4; ++k) {
                int l = MathHelper.floor(this.locX + ((double) (k % 2) - 0.5D) * 0.8D);

                j = MathHelper.floor(this.locZ + ((double) (k / 2) - 0.5D) * 0.8D);

                for (int i1 = 0; i1 < 2; ++i1) {
                    int j1 = MathHelper.floor(this.locY) + i1;
                    Block block = this.world.getType(l, j1, j);

                    if (block == Blocks.SNOW) {
                        // CraftBukkit start
                        if (CraftEventFactory.callEntityChangeBlockEvent(this, l, j1, j, Blocks.AIR, 0).isCancelled()) {
                            continue;
                        }
                        // CraftBukkit end
                        this.world.setAir(l, j1, j);
                        this.positionChanged = false;
                    } else if (block == Blocks.WATER_LILY) {
                        // CraftBukkit start
                        if (CraftEventFactory.callEntityChangeBlockEvent(this, l, j1, j, Blocks.AIR, 0).isCancelled()) {
                            continue;
                        }
                        // CraftBukkit end
                        this.world.setAir(l, j1, j, true);
                        this.positionChanged = false;
                    }
                }
            }

            if (this.onGround && !this.landBoats) { // CraftBukkit
                this.motX *= 0.5D;
                this.motY *= 0.5D;
                this.motZ *= 0.5D;
            }

            this.move(this.motX, this.motY, this.motZ);
            if (this.positionChanged && d3 > 0.2D) {
                if (!this.world.isStatic && !this.dead) {
                    // CraftBukkit start
                    Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, null);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);
                    if (!destroyEvent.isCancelled()) {
                        this.die();

                        for (k = 0; k < 3; ++k) {
                            this.a(Item.getItemOf(Blocks.WOOD), 1, 0.0F);
                        }

                        for (k = 0; k < 2; ++k) {
                            this.a(Items.STICK, 1, 0.0F);
                        }
                    }
                    // CraftBukkit end
                }
            } else {
                this.motX *= 0.9900000095367432D;
                this.motY *= 0.949999988079071D;
                this.motZ *= 0.9900000095367432D;
            }

            this.pitch = 0.0F;
            d5 = (double) this.yaw;
            d10 = this.lastX - this.locX;
            d11 = this.lastZ - this.locZ;
            if (d10 * d10 + d11 * d11 > 0.001D) {
                d5 = (double) ((float) (Math.atan2(d11, d10) * 180.0D / 3.141592653589793D));
            }

            double d12 = MathHelper.g(d5 - (double) this.yaw);

            if (d12 > 20.0D) {
                d12 = 20.0D;
            }

            if (d12 < -20.0D) {
                d12 = -20.0D;
            }

            this.yaw = (float) ((double) this.yaw + d12);
            this.b(this.yaw, this.pitch);

            // CraftBukkit start
            org.bukkit.Server server = this.world.getServer();
            org.bukkit.World bworld = this.world.getWorld();

            Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(bworld, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            server.getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                VehicleMoveEvent event = new VehicleMoveEvent(vehicle, from, to);
                server.getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            if (!this.world.isStatic) {
                List list = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (list != null && !list.isEmpty()) {
                    for (int k1 = 0; k1 < list.size(); ++k1) {
                        Entity entity = (Entity) list.get(k1);

                        if (entity != this.passenger && entity.S() && entity instanceof EntityBoat) {
                            entity.collide(this);
                        }
                    }
                }

                if (this.passenger != null && this.passenger.dead) {
                    this.passenger.vehicle = null; // CraftBukkit
                    this.passenger = null;
                }
            }
        }
    }

    public void ac() {
        if (this.passenger != null) {
            double d0 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;

            this.passenger.setPosition(this.locX + d0, this.locY + this.ae() + this.passenger.ad(), this.locZ + d1);
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {}

    protected void a(NBTTagCompound nbttagcompound) {}

    public boolean c(EntityHuman entityhuman) {
        if (this.passenger != null && this.passenger instanceof EntityHuman && this.passenger != entityhuman) {
            return true;
        } else {
            if (!this.world.isStatic) {
                entityhuman.mount(this);
            }

            return true;
        }
    }

    protected void a(double d0, boolean flag) {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);

        if (flag) {
            if (this.fallDistance > 3.0F) {
                this.b(this.fallDistance);
                if (!this.world.isStatic && !this.dead) {
                    this.die();

                    int l;

                    for (l = 0; l < 3; ++l) {
                        this.a(Item.getItemOf(Blocks.WOOD), 1, 0.0F);
                    }

                    for (l = 0; l < 2; ++l) {
                        this.a(Items.STICK, 1, 0.0F);
                    }
                }

                this.fallDistance = 0.0F;
            }
        } else if (this.world.getType(i, j - 1, k).getMaterial() != Material.WATER && d0 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - d0);
        }
    }

    public void setDamage(float f) {
        this.datawatcher.watch(19, Float.valueOf(f));
    }

    public float getDamage() {
        return this.datawatcher.getFloat(19);
    }

    public void a(int i) {
        this.datawatcher.watch(17, Integer.valueOf(i));
    }

    public int f() {
        return this.datawatcher.getInt(17);
    }

    public void c(int i) {
        this.datawatcher.watch(18, Integer.valueOf(i));
    }

    public int i() {
        return this.datawatcher.getInt(18);
    }
}
