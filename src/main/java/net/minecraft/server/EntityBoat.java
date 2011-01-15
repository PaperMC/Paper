package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.CraftMappable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
// CraftBukkit end

public class EntityBoat extends Entity implements CraftMappable { // CraftBukkit

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
    private CraftBoat boat;

    public CraftEntity getCraftEntity() {
        return boat;
    }
    // CraftBukkit end

    public EntityBoat(World world) {
        super(world);
        a = 0;
        b = 0;
        c = 1;
        i = true;
        a(1.5F, 0.6F);
        H = J / 2.0F;
        M = false;

        handleCreation(world); // CraftBukkit
    }

    protected void a() {}

    public AxisAlignedBB d(Entity entity) {
        return entity.z;
    }

    public AxisAlignedBB u() {
        return z;
    }

    public boolean z() {
        return true;
    }

    public EntityBoat(World world, double d1, double d2, double d3) {
        this(world);
        a(d1, d2 + (double) H, d3);
        s = 0.0D;
        t = 0.0D;
        u = 0.0D;
        m = d1;
        n = d2;
        o = d3;

        handleCreation(world); // CraftBukkit
    }

    // CraftBukkit start
    private void handleCreation(World world) {
        CraftServer server = ((WorldServer) world).getServer();
        boat = new CraftBoat(server, this);
        VehicleCreateEvent event = new VehicleCreateEvent( Type.VEHICLE_CREATE, boat);
        server.getPluginManager().callEvent(event);
    }
    // CraftBukkit end

    public double k() {
        return (double) J * 0.0D - 0.30000001192092896D;
    }

    public boolean a(Entity entity, int i) {
        // CraftBukkit start
        VehicleDamageEvent event = new VehicleDamageEvent( Type.VEHICLE_DAMAGE, boat, ((WorldServer)l).getWorld().toCraftEntity(entity), i);
        ((WorldServer) l).getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return true;
        }
        // CraftBukkit end

        if (this.l.z || G) {
            return true;
        }
        c = -c;
        b = 10;
        a += i * 10;
        y();
        if (a > 40) {
            for (int j = 0; j < 3; j++) {
                a(Block.x.bi, 1, 0.0F);
            }

            for (int l = 0; l < 2; l++) {
                a(Item.B.ba, 1, 0.0F);
            }

            q();
        }
        return true;
    }

    public boolean c_() {
        return !G;
    }

    public void b_() {
        // CraftBukkit start
        double prevX = p;
        double prevY = q;
        double prevZ = r;
        float prevYaw = v;
        float prevPitch = w;
        // CraftBukkit end

        super.b_();
        if (b > 0) {
            b--;
        }
        if (a > 0) {
            a--;
        }
        m = p;
        n = q;
        o = r;
        int i = 5;
        double d1 = 0.0D;

        for (int j = 0; j < i; j++) {
            double d2 = (z.b + ((z.e - z.b) * (double) (j + 0)) / (double) i) - 0.125D;
            double d3 = (z.b + ((z.e - z.b) * (double) (j + 1)) / (double) i) - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.b(z.a, d2, z.c, z.d, d3, z.f);

            if (this.l.b(axisalignedbb, Material.f)) {
                d1 += 1.0D / (double) i;
            }
        }

        if (this.l.z) {
            if (d > 0) {
                double d4 = p + (e - p) / (double) d;
                double d7 = q + (f - q) / (double) d;
                double d10 = r + (ak - r) / (double) d;
                double d13;

                for (d13 = al - (double) v; d13 < -180D; d13 += 360D) {
                    ;
                }
                for (; d13 >= 180D; d13 -= 360D) {
                    ;
                }
                v += ((float) (d13 / (double) d));
                w += ((float) ((am - (double) w) / (double) d));
                d--;
                a(d4, d7, d10);
                b(v, w);
            } else {
                double d5 = p + s;
                double d8 = q + t;
                double d11 = r + u;

                a(d5, d8, d11);
                if (A) {
                    s *= 0.5D;
                    t *= 0.5D;
                    u *= 0.5D;
                }
                s *= 0.99000000953674316D;
                t *= 0.94999998807907104D;
                u *= 0.99000000953674316D;
            }
            return;
        }
        double d6 = d1 * 2D - 1.0D;

        t += 0.039999999105930328D * d6;
        if (this.j != null) {
            s += this.j.s * 0.20000000000000001D;
            u += this.j.u * 0.20000000000000001D;
        }
        double d9 = 0.40000000000000002D;

        if (s < -d9) {
            s = -d9;
        }
        if (s > d9) {
            s = d9;
        }
        if (u < -d9) {
            u = -d9;
        }
        if (u > d9) {
            u = d9;
        }
        if (A) {
            s *= 0.5D;
            t *= 0.5D;
            u *= 0.5D;
        }
        c(s, t, u);
        double d12 = Math.sqrt(s * s + u * u);

        if (d12 > 0.14999999999999999D) {
            double d14 = Math.cos(((double) v * 3.1415926535897931D) / 180D);
            double d16 = Math.sin(((double) v * 3.1415926535897931D) / 180D);

            for (int l = 0; (double) l < 1.0D + d12 * 60D; l++) {
                double d18 = W.nextFloat() * 2.0F - 1.0F;
                double d19 = (double) (W.nextInt(2) * 2 - 1) * 0.69999999999999996D;

                if (W.nextBoolean()) {
                    double d20 = (p - d14 * d18 * 0.80000000000000004D) + d16 * d19;
                    double d22 = r - d16 * d18 * 0.80000000000000004D - d14 * d19;

                    this.l.a("splash", d20, q - 0.125D, d22, s, t, u);
                } else {
                    double d21 = p + d14 + d16 * d18 * 0.69999999999999996D;
                    double d23 = (r + d16) - d14 * d18 * 0.69999999999999996D;

                    this.l.a("splash", d21, q - 0.125D, d23, s, t, u);
                }
            }
        }
        if (B && d12 > 0.14999999999999999D) {
            if (!this.l.z) {
                q();
                for (int i1 = 0; i1 < 3; i1++) {
                    a(Block.x.bi, 1, 0.0F);
                }

                for (int j1 = 0; j1 < 2; j1++) {
                    a(Item.B.ba, 1, 0.0F);
                }
            }
        } else {
            s *= 0.99000000953674316D;
            t *= 0.94999998807907104D;
            u *= 0.99000000953674316D;
        }
        w = 0.0F;
        double d15 = v;
        double d17 = m - p;
        double d24 = o - r;

        if (d17 * d17 + d24 * d24 > 0.001D) {
            d15 = (float) ((Math.atan2(d24, d17) * 180D) / 3.1415926535897931D);
        }
        double d25;

        for (d25 = d15 - (double) v; d25 >= 180D; d25 -= 360D) {
            ;
        }
        for (; d25 < -180D; d25 += 360D) {
            ;
        }
        if (d25 > 20D) {
            d25 = 20D;
        }
        if (d25 < -20D) {
            d25 = -20D;
        }
        v += ((float) (d25));
        b(v, w);

        // CraftBukkit start
        CraftServer server = ((WorldServer)l).getServer();
        VehicleMoveEvent event = new VehicleMoveEvent(
            Type.VEHICLE_MOVE,
            boat,
            new Location(((WorldServer)l).getWorld(), prevX, prevY, prevZ, prevYaw, prevPitch),
            new Location(((WorldServer)l).getWorld(), p, q, r, v, w)
        );
        server.getPluginManager().callEvent(event);
        // CraftBukkit end

        List list = this.l.b(((Entity) (this)), z.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int k1 = 0; k1 < list.size(); k1++) {
                Entity entity = (Entity) list.get(k1);

                if (entity != this.j && entity.z() && (entity instanceof EntityBoat)) {
                    entity.c(((Entity) (this)));
                }
            }
        }
        if (this.j != null && this.j.G) {
            this.j = null;
        }
    }

    public void E() {
        if (j == null) {
            return;
        } else {
            double d1 = Math.cos(((double) v * 3.1415926535897931D) / 180D) * 0.40000000000000002D;
            double d2 = Math.sin(((double) v * 3.1415926535897931D) / 180D) * 0.40000000000000002D;

            j.a(p + d1, q + k() + j.F(), r + d2);
            return;
        }
    }

    // CraftBukkit start
    public void c(Entity entity) {
        CraftServer server = ((WorldServer)l).getServer();
        VehicleEntityCollisionEvent collsionEvent = new VehicleEntityCollisionEvent( Type.VEHICLE_COLLISION_ENTITY, boat, ((WorldServer)l).getWorld().toCraftEntity(entity));
        server.getPluginManager().callEvent(collsionEvent);

        if (collsionEvent.isCancelled()) {
            return;
        }

        super.c(entity);
    }
    // CraftBukkit end

    protected void a(NBTTagCompound nbttagcompound) {}

    protected void b(NBTTagCompound nbttagcompound) {}

    public boolean a(EntityPlayer entityplayer) {
        if (j != null && (j instanceof EntityPlayer) && j != entityplayer) {
            return true;
        }
        if (!l.z) {
            // CraftBukkit start
            CraftServer server = ((WorldServer)l).getServer();
            VehicleEnterEvent event = new VehicleEnterEvent( Type.VEHICLE_ENTER, boat, ((WorldServer)l).getWorld().toCraftEntity(entityplayer));
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            entityplayer.e(((Entity) (this)));
        }
        return true;
    }
}
