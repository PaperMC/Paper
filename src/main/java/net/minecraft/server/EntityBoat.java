package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
// CraftBukkit end

public class EntityBoat extends Entity {

    public int damage;
    public int b;
    public int c;
    private int d;
    private double e;
    private double f;
    private double g;
    private double h;
    private double i;

    // CraftBukkit start
    public double maxSpeed = 0.4D;

    @Override
    public void collide(Entity entity) {
        CraftServer server = ((WorldServer) this.world).getServer();
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

        VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent(vehicle, hitEntity);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        super.collide(entity);
    }
    // CraftBukkit end

    public EntityBoat(World world) {
        super(world);
        this.damage = 0;
        this.b = 0;
        this.c = 1;
        this.aD = true;
        this.b(1.5F, 0.6F);
        this.height = this.width / 2.0F;
    }

    protected boolean l() {
        return false;
    }

    protected void a() {}

    public AxisAlignedBB a_(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB d() {
        return this.boundingBox;
    }

    public boolean e_() {
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

        // CraftBukkit start
        CraftServer server = ((WorldServer) world).getServer();
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        VehicleCreateEvent event = new VehicleCreateEvent(vehicle);
        server.getPluginManager().callEvent(event);
        // CraftBukkit end
    }

    public double k() {
        return (double) this.width * 0.0D - 0.30000001192092896D;
    }

    public boolean damageEntity(Entity entity, int i) {
        if (!this.world.isStatic && !this.dead) {
            // CraftBukkit start
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();
            org.bukkit.entity.Entity attacker = (entity == null) ? null : entity.getBukkitEntity();
            int damage = i;

            VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, damage);
            ((WorldServer) this.world).getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            this.c = -this.c;
            this.b = 10;
            this.damage += i * 10;
            this.W();
            if (this.damage > 40) {

                // CraftBukkit start
                VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                ((WorldServer) this.world).getServer().getPluginManager().callEvent(destroyEvent);

                if (destroyEvent.isCancelled()) {
                    this.damage = 40; // Maximize damage so this doesn't get triggered again right away
                    return true;
                }
                // CraftBukkit end

                int j;

                for (j = 0; j < 3; ++j) {
                    this.a(Block.WOOD.id, 1, 0.0F);
                }

                for (j = 0; j < 2; ++j) {
                    this.a(Item.STICK.id, 1, 0.0F);
                }

                this.die();
            }

            return true;
        } else {
            return true;
        }
    }

    public boolean d_() {
        return !this.dead;
    }

    public void f_() {
        // CraftBukkit start
        double prevX = this.locX;
        double prevY = this.locY;
        double prevZ = this.locZ;
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;
        // CraftBukkit end

        super.f_();
        if (this.b > 0) {
            --this.b;
        }

        if (this.damage > 0) {
            --this.damage;
        }

        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        byte b0 = 5;
        double d0 = 0.0D;

        for (int i = 0; i < b0; ++i) {
            double d1 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 0) / (double) b0 - 0.125D;
            double d2 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 1) / (double) b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.b(this.boundingBox.a, d1, this.boundingBox.c, this.boundingBox.d, d2, this.boundingBox.f);

            if (this.world.b(axisalignedbb, Material.WATER)) {
                d0 += 1.0D / (double) b0;
            }
        }

        double d3;
        double d4;
        double d5;
        double d6;

        if (this.world.isStatic) {
            if (this.d > 0) {
                d3 = this.locX + (this.e - this.locX) / (double) this.d;
                d4 = this.locY + (this.f - this.locY) / (double) this.d;
                d5 = this.locZ + (this.g - this.locZ) / (double) this.d;

                for (d6 = this.h - (double) this.yaw; d6 < -180.0D; d6 += 360.0D) {
                    ;
                }

                while (d6 >= 180.0D) {
                    d6 -= 360.0D;
                }

                this.yaw = (float) ((double) this.yaw + d6 / (double) this.d);
                this.pitch = (float) ((double) this.pitch + (this.i - (double) this.pitch) / (double) this.d);
                --this.d;
                this.setPosition(d3, d4, d5);
                this.c(this.yaw, this.pitch);
            } else {
                d3 = this.locX + this.motX;
                d4 = this.locY + this.motY;
                d5 = this.locZ + this.motZ;
                this.setPosition(d3, d4, d5);
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
            d3 = d0 * 2.0D - 1.0D;
            this.motY += 0.03999999910593033D * d3;
            if (this.passenger != null) {
                this.motX += this.passenger.motX * 0.2D;
                this.motZ += this.passenger.motZ * 0.2D;
            }

            // CraftBukkit
            d4 = this.maxSpeed;
            if (this.motX < -d4) {
                this.motX = -d4;
            }

            if (this.motX > d4) {
                this.motX = d4;
            }

            if (this.motZ < -d4) {
                this.motZ = -d4;
            }

            if (this.motZ > d4) {
                this.motZ = d4;
            }

            if (this.onGround) {
                this.motX *= 0.5D;
                this.motY *= 0.5D;
                this.motZ *= 0.5D;
            }

            this.move(this.motX, this.motY, this.motZ);
            d5 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            double d7;

            if (d5 > 0.15D) {
                d6 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D);
                d7 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D);

                for (int j = 0; (double) j < 1.0D + d5 * 60.0D; ++j) {
                    double d8 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                    double d9 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;
                    double d10;
                    double d11;

                    if (this.random.nextBoolean()) {
                        d10 = this.locX - d6 * d8 * 0.8D + d7 * d9;
                        d11 = this.locZ - d7 * d8 * 0.8D - d6 * d9;
                        this.world.a("splash", d10, this.locY - 0.125D, d11, this.motX, this.motY, this.motZ);
                    } else {
                        d10 = this.locX + d6 + d7 * d8 * 0.7D;
                        d11 = this.locZ + d7 - d6 * d8 * 0.7D;
                        this.world.a("splash", d10, this.locY - 0.125D, d11, this.motX, this.motY, this.motZ);
                    }
                }
            }

            if (this.positionChanged && d5 > 0.15D) {
                if (!this.world.isStatic) {
                    this.die();

                    int k;

                    for (k = 0; k < 3; ++k) {
                        this.a(Block.WOOD.id, 1, 0.0F);
                    }

                    for (k = 0; k < 2; ++k) {
                        this.a(Item.STICK.id, 1, 0.0F);
                    }
                }
            } else {
                this.motX *= 0.9900000095367432D;
                this.motY *= 0.949999988079071D;
                this.motZ *= 0.9900000095367432D;
            }

            this.pitch = 0.0F;
            d6 = (double) this.yaw;
            d7 = this.lastX - this.locX;
            double d12 = this.lastZ - this.locZ;

            if (d7 * d7 + d12 * d12 > 0.0010D) {
                d6 = (double) ((float) (Math.atan2(d12, d7) * 180.0D / 3.141592653589793D));
            }

            double d13;

            for (d13 = d6 - (double) this.yaw; d13 >= 180.0D; d13 -= 360.0D) {
                ;
            }

            while (d13 < -180.0D) {
                d13 += 360.0D;
            }

            if (d13 > 20.0D) {
                d13 = 20.0D;
            }

            if (d13 < -20.0D) {
                d13 = -20.0D;
            }

            this.yaw = (float) ((double) this.yaw + d13);
            this.c(this.yaw, this.pitch);

            // CraftBukkit start
            CraftServer server = ((WorldServer) this.world).getServer();
            CraftWorld world = ((WorldServer) this.world).getWorld();
            Location from = new Location(world, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(world, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            server.getPluginManager().callEvent(new VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                VehicleMoveEvent event = new VehicleMoveEvent(vehicle, from, to);
                server.getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            List list = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && list.size() > 0) {
                for (int l = 0; l < list.size(); ++l) {
                    Entity entity = (Entity) list.get(l);

                    if (entity != this.passenger && entity.e_() && entity instanceof EntityBoat) {
                        entity.collide(this);
                    }
                }
            }

            if (this.passenger != null && this.passenger.dead) {
                this.passenger = null;
            }
        }
    }

    public void h_() {
        if (this.passenger != null) {
            double d0 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;

            this.passenger.setPosition(this.locX + d0, this.locY + this.k() + this.passenger.C(), this.locZ + d1);
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {}

    protected void b(NBTTagCompound nbttagcompound) {}

    public boolean a(EntityHuman entityhuman) {
        if (this.passenger != null && this.passenger instanceof EntityHuman && this.passenger != entityhuman) {
            return true;
        } else {
            if (!this.world.isStatic) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) this.world).getServer();
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity player = entityhuman.getBukkitEntity();

                VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
                // CraftBukkit end

                entityhuman.mount(this);
            }

            return true;
        }
    }
}
