package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.CraftMappable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event.Type;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
// CraftBukkit end

public class EntityBoat extends Entity {

    public int a;
    public int b;
    public int c;
    private int d;
    private double e;
    private double f;
    private double ak;
    private double al;
    private double am;

    // CraftBukkit start
    private void handleCreation(World world) {
        CraftServer server = ((WorldServer) world).getServer();
        Type eventType = Type.VEHICLE_CREATE;
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        VehicleCreateEvent event = new VehicleCreateEvent(eventType, vehicle);
        server.getPluginManager().callEvent(event);
    }

    public void c(Entity entity) {
        CraftServer server = ((WorldServer) this.world).getServer();
        Type eventType = Type.VEHICLE_COLLISION_ENTITY;
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

        VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent(eventType, vehicle, hitEntity);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        super.c(entity);
    }
    // CraftBukkit end

    public EntityBoat(World world) {
        super(world);
        this.a = 0;
        this.b = 0;
        this.c = 1;
        this.i = true;
        this.a(1.5F, 0.6F);
        this.height = this.width / 2.0F;
        this.M = false;

        // CraftBukkit start
        handleCreation(world);
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftBoat(server, this);
        // CraftBukkit end
    }

    protected void a() {}

    public AxisAlignedBB d(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB u() {
        return this.boundingBox;
    }

    public boolean z() {
        return true;
    }

    public EntityBoat(World world, double d0, double d1, double d2) {
        this(world);
        this.a(d0, d1 + (double) this.height, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;

        handleCreation(world); // CraftBukkit
    }

    public double k() {
        return (double) this.width * 0.0D - 0.30000001192092896D;
    }

    public boolean a(Entity entity, int i) {
        if (!this.world.isStatic && !this.dead) {
            // CraftBukkit start
            Type eventType = Type.VEHICLE_DAMAGE;
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();
            org.bukkit.entity.Entity attacker = (entity == null) ? null : entity.getBukkitEntity();
            int damage = i;

            VehicleDamageEvent event = new VehicleDamageEvent(eventType, vehicle, attacker, damage);
            ((WorldServer) this.world).getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            this.c = -this.c;
            this.b = 10;
            this.a += i * 10;
            this.y();
            if (this.a > 40) {
                int j;

                for (j = 0; j < 3; ++j) {
                    this.a(Block.WOOD.id, 1, 0.0F);
                }

                for (j = 0; j < 2; ++j) {
                    this.a(Item.STICK.id, 1, 0.0F);
                }

                this.q();
            }

            return true;
        } else {
            return true;
        }
    }

    public boolean c_() {
        return !this.dead;
    }

    public void b_() {
        // CraftBukkit start
        double prevX = this.locX;
        double prevY = this.locY;
        double prevZ = this.locZ;
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;
        // CraftBukkit end

        super.b_();
        if (this.b > 0) {
            --this.b;
        }

        if (this.a > 0) {
            --this.a;
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
                d5 = this.locZ + (this.ak - this.locZ) / (double) this.d;

                for (d6 = this.al - (double) this.yaw; d6 < -180.0D; d6 += 360.0D) {
                    ;
                }

                while (d6 >= 180.0D) {
                    d6 -= 360.0D;
                }

                this.yaw = (float) ((double) this.yaw + d6 / (double) this.d);
                this.pitch = (float) ((double) this.pitch + (this.am - (double) this.pitch) / (double) this.d);
                --this.d;
                this.a(d3, d4, d5);
                this.b(this.yaw, this.pitch);
            } else {
                d3 = this.locX + this.motX;
                d4 = this.locY + this.motY;
                d5 = this.locZ + this.motZ;
                this.a(d3, d4, d5);
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

            d4 = 0.4D;
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

            this.c(this.motX, this.motY, this.motZ);
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

            if (this.B && d5 > 0.15D) {
                if (!this.world.isStatic) {
                    this.q();

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
            this.b(this.yaw, this.pitch);

            // CraftBukkit start
            CraftServer server = ((WorldServer) this.world).getServer();
            CraftWorld world = ((WorldServer) this.world).getWorld();
            Type eventType = Type.VEHICLE_MOVE;
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();
            Location from = new Location(world, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(world, this.locX, this.locY, this.locZ, this.yaw, this.pitch);

            VehicleMoveEvent event = new VehicleMoveEvent(eventType, vehicle, from, to);
            server.getPluginManager().callEvent(event);
            // CraftBukkit end

            List list = this.world.b((Entity) this, this.boundingBox.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && list.size() > 0) {
                for (int l = 0; l < list.size(); ++l) {
                    Entity entity = (Entity) list.get(l);

                    if (entity != this.passenger && entity.z() && entity instanceof EntityBoat) {
                        entity.c((Entity) this);
                    }
                }
            }

            if (this.passenger != null && this.passenger.dead) {
                this.passenger = null;
            }
        }
    }

    public void E() {
        if (this.passenger != null) {
            double d0 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;

            this.passenger.a(this.locX + d0, this.locY + this.k() + this.passenger.F(), this.locZ + d1);
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
                Type eventType = Type.VEHICLE_ENTER;
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity player = entityhuman.getBukkitEntity();

                VehicleEnterEvent event = new VehicleEnterEvent(eventType, vehicle, player);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
                // CraftBukkit end

                entityhuman.e(this);
            }

            return true;
        }
    }
}
