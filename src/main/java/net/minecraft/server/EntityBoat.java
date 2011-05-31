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
        this.aI = true;
        this.b(1.5F, 0.6F);
        this.height = this.width / 2.0F;
    }

    protected boolean n() {
        return false;
    }

    protected void b() {}

    public AxisAlignedBB a_(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB e_() {
        return this.boundingBox;
    }

    public boolean d_() {
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

    public double m() {
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
            this.ae();
            if (this.damage > 40) {

                // CraftBukkit start
                VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                ((WorldServer) this.world).getServer().getPluginManager().callEvent(destroyEvent);

                if (destroyEvent.isCancelled()) {
                    this.damage = 40; // Maximize damage so this doesn't get triggered again right away
                    return true;
                }
                // CraftBukkit end

                if (this.passenger != null) {
                    this.passenger.mount(this);
                }

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

    public boolean n_() {
        return !this.dead;
    }

    public void o_() {
        // CraftBukkit start
        double prevX = this.locX;
        double prevY = this.locY;
        double prevZ = this.locZ;
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;
        // CraftBukkit end

        super.o_();
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
            if (d0 < 1.0D) {
                d3 = d0 * 2.0D - 1.0D;
                this.motY += 0.03999999910593033D * d3;
            } else {
                if (this.motY < 0.0D) {
                    this.motY /= 2.0D;
                }

                this.motY += 0.007000000216066837D;
            }

            if (this.passenger != null) {
                this.motX += this.passenger.motX * 0.2D;
                this.motZ += this.passenger.motZ * 0.2D;
            }

            // CraftBukkit
            d3 = this.maxSpeed;
            if (this.motX < -d3) {
                this.motX = -d3;
            }

            if (this.motX > d3) {
                this.motX = d3;
            }

            if (this.motZ < -d3) {
                this.motZ = -d3;
            }

            if (this.motZ > d3) {
                this.motZ = d3;
            }

            if (this.onGround) {
                this.motX *= 0.5D;
                this.motY *= 0.5D;
                this.motZ *= 0.5D;
            }

            this.move(this.motX, this.motY, this.motZ);
            d4 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            if (d4 > 0.15D) {
                d5 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D);
                d6 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D);

                for (int j = 0; (double) j < 1.0D + d4 * 60.0D; ++j) {
                    double d7 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                    double d8 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;
                    double d9;
                    double d10;

                    if (this.random.nextBoolean()) {
                        d9 = this.locX - d5 * d7 * 0.8D + d6 * d8;
                        d10 = this.locZ - d6 * d7 * 0.8D - d5 * d8;
                        this.world.a("splash", d9, this.locY - 0.125D, d10, this.motX, this.motY, this.motZ);
                    } else {
                        d9 = this.locX + d5 + d6 * d7 * 0.7D;
                        d10 = this.locZ + d6 - d5 * d7 * 0.7D;
                        this.world.a("splash", d9, this.locY - 0.125D, d10, this.motX, this.motY, this.motZ);
                    }
                }
            }

            if (this.positionChanged && d4 > 0.15D) {
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
            d5 = (double) this.yaw;
            d6 = this.lastX - this.locX;
            double d11 = this.lastZ - this.locZ;

            if (d6 * d6 + d11 * d11 > 0.0010D) {
                d5 = (double) ((float) (Math.atan2(d11, d6) * 180.0D / 3.141592653589793D));
            }

            double d12;

            for (d12 = d5 - (double) this.yaw; d12 >= 180.0D; d12 -= 360.0D) {
                ;
            }

            while (d12 < -180.0D) {
                d12 += 360.0D;
            }

            if (d12 > 20.0D) {
                d12 = 20.0D;
            }

            if (d12 < -20.0D) {
                d12 = -20.0D;
            }

            this.yaw = (float) ((double) this.yaw + d12);
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
            int l;

            if (list != null && list.size() > 0) {
                for (l = 0; l < list.size(); ++l) {
                    Entity entity = (Entity) list.get(l);

                    if (entity != this.passenger && entity.d_() && entity instanceof EntityBoat) {
                        entity.collide(this);
                    }
                }
            }

            for (l = 0; l < 4; ++l) {
                int i1 = MathHelper.floor(this.locX + ((double) (l % 2) - 0.5D) * 0.8D);
                int j1 = MathHelper.floor(this.locY);
                int k1 = MathHelper.floor(this.locZ + ((double) (l / 2) - 0.5D) * 0.8D);

                if (this.world.getTypeId(i1, j1, k1) == Block.SNOW.id) {
                    this.world.setTypeId(i1, j1, k1, 0);
                }
            }

            if (this.passenger != null && this.passenger.dead) {
                this.passenger = null;
            }
        }
    }

    public void f() {
        if (this.passenger != null) {
            double d0 = Math.cos((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.yaw * 3.141592653589793D / 180.0D) * 0.4D;

            this.passenger.setPosition(this.locX + d0, this.locY + this.m() + this.passenger.H(), this.locZ + d1);
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {}

    protected void a(NBTTagCompound nbttagcompound) {}

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
