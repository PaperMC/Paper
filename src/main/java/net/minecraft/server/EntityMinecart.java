package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPoweredMinecart;
import org.bukkit.craftbukkit.entity.CraftStorageMinecart;
import org.bukkit.craftbukkit.CraftMappable;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event.Type;
import org.bukkit.event.vehicle.*;
// CraftBukkit end

public class EntityMinecart extends Entity implements IInventory, CraftMappable { // CraftBukkit

    private ItemStack al[];
    public int a;
    public int b;
    public int c;
    private boolean am;
    public int d;
    public int e;
    public double f;
    public double ak;
    private static final int an[][][] = {
        {
            {
                0, 0, -1
            }, {
                0, 0, 1
            }
        }, {
            {
                -1, 0, 0
            }, {
                1, 0, 0
            }
        }, {
            {
                -1, -1, 0
            }, {
                1, 0, 0
            }
        }, {
            {
                -1, 0, 0
            }, {
                1, -1, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                0, -1, 1
            }
        }, {
            {
                0, -1, -1
            }, {
                0, 0, 1
            }
        }, {
            {
                0, 0, 1
            }, {
                1, 0, 0
            }
        }, {
            {
                0, 0, 1
            }, {
                -1, 0, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                -1, 0, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                1, 0, 0
            }
        }
    };
    private int ao;
    private double ap;
    private double aq;
    private double ar;
    private double as;
    private double at;

    // CraftBukkit start
    protected org.bukkit.entity.Entity bukkitPoweredMinecart; //CraftBukkit
    protected org.bukkit.entity.Entity bukkitStorageMinecart; //CraftBukkit
    /**@deprecated*/
    private CraftMinecart minecart;

    private boolean slowWhenEmpty = true;
    private double derailedX = 0.5;
    private double derailedY = 0.5;
    private double derailedZ = 0.5;
    private double flyingX = 0.94999998807907104;
    private double flyingY = 0.94999998807907104;
    private double flyingZ = 0.94999998807907104;
    /**@deprecated*/
    public CraftEntity getCraftEntity() {
        return minecart;
    }

    public ItemStack[] getContents() {
        return this.al;
    }
    // CraftBukkit end

    public EntityMinecart(World world) {
        super(world);
        al = new ItemStack[36];
        a = 0;
        b = 0;
        c = 1;
        am = false;
        i = true;
        a(0.98F, 0.7F);
        H = J / 2.0F;
        M = false;

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftMinecart(server, this);
        this.bukkitPoweredMinecart = new CraftPoweredMinecart(server, this);
        this.bukkitStorageMinecart = new CraftStorageMinecart(server, this);
        handleCreation(world);
        // CraftBukkit end
    }

    protected void a() {}

    public AxisAlignedBB d(Entity entity) {
        return entity.z;
    }

    public AxisAlignedBB u() {
        return null;
    }

    public boolean z() {
        return true;
    }

    public EntityMinecart(World world, double d1, double d2, double d3, int i) {
        this(world);
        a(d1, d2 + (double) H, d3);
        s = 0.0D;
        t = 0.0D;
        u = 0.0D;
        m = d1;
        n = d2;
        o = d3;
        d = i;
    }

    // CraftBukkit start
    private void handleCreation(World world) {
        CraftServer server = ((WorldServer) world).getServer();
        Type eventType = Type.VEHICLE_CREATE;
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        
        VehicleCreateEvent event = new VehicleCreateEvent(eventType, vehicle);
        server.getPluginManager().callEvent(event);
    }
    // CraftBukkit end

    public double k() {
        return (double) J * 0.0D - 0.30000001192092896D;
    }

    public boolean a(Entity entity, int i) {
        // CraftBukkit start
        Type eventType = Type.VEHICLE_DAMAGE;
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        org.bukkit.entity.Entity passenger = (entity == null)?null:entity.getBukkitEntity();
        int damage = i;
        
        VehicleDamageEvent event = new VehicleDamageEvent(eventType, vehicle, passenger, damage);
        ((WorldServer)l).getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return true;
        }
        i = event.getDamage();
        // CraftBukkit end

        if (l.z || G) {
            return true;
        }
        c = -c;
        b = 10;
        y();
        a += i * 10;
        if (a > 40) {
            a(Item.ax.ba, 1, 0.0F);
            if (d == 1) {
                a(Block.au.bi, 1, 0.0F);
            } else if (d == 2) {
                a(Block.aB.bi, 1, 0.0F);
            }
            q();
        }
        return true;
    }

    public boolean c_() {
        return !G;
    }

    public void q() {
        label0:
        for (int i = 0; i < h_(); i++) {
            ItemStack itemstack = a(i);

            if (itemstack == null) {
                continue;
            }
            float f1 = W.nextFloat() * 0.8F + 0.1F;
            float f2 = W.nextFloat() * 0.8F + 0.1F;
            float f3 = W.nextFloat() * 0.8F + 0.1F;

            do {
                if (itemstack.a <= 0) {
                    continue label0;
                }
                int j = W.nextInt(21) + 10;

                if (j > itemstack.a) {
                    j = itemstack.a;
                }
                itemstack.a -= j;
                EntityItem entityitem = new EntityItem(l, p + (double) f1, q + (double) f2, r + (double) f3, new ItemStack(itemstack.c, j, itemstack.h()));
                float f4 = 0.05F;

                entityitem.s = (float) W.nextGaussian() * f4;
                entityitem.t = (float) W.nextGaussian() * f4 + 0.2F;
                entityitem.u = (float) W.nextGaussian() * f4;
                l.a(((Entity) (entityitem)));
            } while (true);
        }

        super.q();
    }

    public void b_() {
        // CraftBukkit start
        double prevX = p;
        double prevY = q;
        double prevZ = r;
        float prevYaw = v;
        float prevPitch = w;
        // CraftBukkit end
        if (b > 0) {
            b--;
        }
        if (a > 0) {
            a--;
        }
        if (this.l.z && ao > 0) {
            if (ao > 0) {
                double d1 = p + (ap - p) / (double) ao;
                double d2 = q + (aq - q) / (double) ao;
                double d3 = r + (ar - r) / (double) ao;
                double d4;

                for (d4 = as - (double) v; d4 < -180D; d4 += 360D) {
                    ;
                }
                for (; d4 >= 180D; d4 -= 360D) {
                    ;
                }
                v += ((float) (d4 / (double) ao));
                w += ((float) ((at - (double) w) / (double) ao));
                ao--;
                a(d1, d2, d3);
                b(v, w);
            } else {
                a(p, q, r);
                b(v, w);
            }
            return;
        }
        m = p;
        n = q;
        o = r;
        t -= 0.039999999105930328D;
        int i = MathHelper.b(p);
        int j = MathHelper.b(q);
        int l = MathHelper.b(r);

        if (this.l.a(i, j - 1, l) == Block.aG.bi) {
            j--;
        }
        double d6 = 0.40000000000000002D;
        boolean flag = false;
        double d5 = 0.0078125D;

        if (this.l.a(i, j, l) == Block.aG.bi) {
            Vec3D vec3d = g(p, q, r);
            int i1 = this.l.b(i, j, l);

            q = j;
            if (i1 >= 2 && i1 <= 5) {
                q = j + 1;
            }
            if (i1 == 2) {
                s -= d5;
            }
            if (i1 == 3) {
                s += d5;
            }
            if (i1 == 4) {
                u += d5;
            }
            if (i1 == 5) {
                u -= d5;
            }
            int ai[][] = an[i1];
            double d7 = ai[1][0] - ai[0][0];
            double d8 = ai[1][2] - ai[0][2];
            double d9 = Math.sqrt(d7 * d7 + d8 * d8);
            double d10 = s * d7 + u * d8;

            if (d10 < 0.0D) {
                d7 = -d7;
                d8 = -d8;
            }
            double d11 = Math.sqrt(s * s + u * u);

            s = (d11 * d7) / d9;
            u = (d11 * d8) / d9;
            double d14 = 0.0D;
            double d15 = (double) i + 0.5D + (double) ai[0][0] * 0.5D;
            double d16 = (double) l + 0.5D + (double) ai[0][2] * 0.5D;
            double d17 = (double) i + 0.5D + (double) ai[1][0] * 0.5D;
            double d18 = (double) l + 0.5D + (double) ai[1][2] * 0.5D;

            d7 = d17 - d15;
            d8 = d18 - d16;
            if (d7 == 0.0D) {
                p = (double) i + 0.5D;
                d14 = r - (double) l;
            } else if (d8 == 0.0D) {
                r = (double) l + 0.5D;
                d14 = p - (double) i;
            } else {
                double d19 = p - d15;
                double d21 = r - d16;
                double d23 = (d19 * d7 + d21 * d8) * 2D;

                d14 = d23;
            }
            p = d15 + d7 * d14;
            r = d16 + d8 * d14;
            a(p, q + (double) H, r);
            double d20 = s;
            double d22 = u;

            if (this.j != null) {
                d20 *= 0.75D;
                d22 *= 0.75D;
            }
            if (d20 < -d6) {
                d20 = -d6;
            }
            if (d20 > d6) {
                d20 = d6;
            }
            if (d22 < -d6) {
                d22 = -d6;
            }
            if (d22 > d6) {
                d22 = d6;
            }
            c(d20, 0.0D, d22);
            if (ai[0][1] != 0 && MathHelper.b(p) - i == ai[0][0] && MathHelper.b(r) - l == ai[0][2]) {
                a(p, q + (double) ai[0][1], r);
            } else if (ai[1][1] != 0 && MathHelper.b(p) - i == ai[1][0] && MathHelper.b(r) - l == ai[1][2]) {
                a(p, q + (double) ai[1][1], r);
            }
            if (this.j != null || !slowWhenEmpty) { // CraftBukkit
                s *= 0.99699997901916504D;
                t *= 0.0D;
                u *= 0.99699997901916504D;
            } else {
                if (d == 2) {
                    double d24 = MathHelper.a(f * f + ak * ak);

                    if (d24 > 0.01D) {
                        flag = true;
                        f /= d24;
                        ak /= d24;
                        double d25 = 0.040000000000000001D;

                        s *= 0.80000001192092896D;
                        t *= 0.0D;
                        u *= 0.80000001192092896D;
                        s += f * d25;
                        u += ak * d25;
                    } else {
                        s *= 0.89999997615814209D;
                        t *= 0.0D;
                        u *= 0.89999997615814209D;
                    }
                }
                s *= 0.95999997854232788D;
                t *= 0.0D;
                u *= 0.95999997854232788D;
            }
            Vec3D vec3d1 = g(p, q, r);

            if (vec3d1 != null && vec3d != null) {
                double d26 = (vec3d.b - vec3d1.b) * 0.050000000000000003D;
                double d12 = Math.sqrt(s * s + u * u);

                if (d12 > 0.0D) {
                    s = (s / d12) * (d12 + d26);
                    u = (u / d12) * (d12 + d26);
                }
                a(p, vec3d1.b, r);
            }
            int j1 = MathHelper.b(p);
            int k1 = MathHelper.b(r);

            if (j1 != i || k1 != l) {
                double d13 = Math.sqrt(s * s + u * u);

                s = d13 * (double) (j1 - i);
                u = d13 * (double) (k1 - l);
            }
            if (d == 2) {
                double d27 = MathHelper.a(f * f + ak * ak);

                if (d27 > 0.01D && s * s + u * u > 0.001D) {
                    f /= d27;
                    ak /= d27;
                    if (f * s + ak * u < 0.0D) {
                        f = 0.0D;
                        ak = 0.0D;
                    } else {
                        f = s;
                        ak = u;
                    }
                }
            }
        } else {
            if (s < -d6) {
                s = -d6;
            }
            if (s > d6) {
                s = d6;
            }
            if (u < -d6) {
                u = -d6;
            }
            if (u > d6) {
                u = d6;
            }
            if (A) {
                // CraftBukkit start
                s *= derailedX;
                t *= derailedY;
                u *= derailedZ;
                // CraftBukkit end
            }
            c(s, t, u);
            if (!A) {
                // CraftBukkit start
                s *= flyingX;
                t *= flyingY;
                u *= flyingZ;
                // CraftBukkit end
            }
        }
        w = 0.0F;
        double d28 = m - p;
        double d29 = o - r;

        if (d28 * d28 + d29 * d29 > 0.001D) {
            v = (float) ((Math.atan2(d29, d28) * 180D) / 3.1415926535897931D);
            if (am) {
                v += 180F;
            }
        }
        double d30;

        for (d30 = v - x; d30 >= 180D; d30 -= 360D) {
            ;
        }
        for (; d30 < -180D; d30 += 360D) {
            ;
        }
        if (d30 < -170D || d30 >= 170D) {
            v += 180F;
            am = !am;
        }
        b(v, w);

        // CraftBukkit start
        CraftServer server = ((WorldServer)this.l).getServer();
        CraftWorld world = ((WorldServer)this.l).getWorld();
        Type eventType = Type.VEHICLE_MOVE;
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();        
        Location from = new Location(world, prevX, prevY, prevZ, prevYaw, prevPitch);
        Location to = new Location(world, p, q, r, v, w);
        
        VehicleMoveEvent event = new VehicleMoveEvent(eventType , vehicle , from, to);
        server.getPluginManager().callEvent(event);
        // CraftBukkit end

        List list = this.l.b(((Entity) (this)), z.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int l1 = 0; l1 < list.size(); l1++) {
                Entity entity = (Entity) list.get(l1);

                if (entity != this.j && entity.z() && (entity instanceof EntityMinecart)) {
                    entity.c(((Entity) (this)));
                }
            }
        }
        if (this.j != null && this.j.G) {
            this.j = null;
        }
        if (flag && W.nextInt(4) == 0) {
            e--;
            if (e < 0) {
                f = ak = 0.0D;
            }
            this.l.a("largesmoke", p, q + 0.80000000000000004D, r, 0.0D, 0.0D, 0.0D);
        }
    }

    public Vec3D g(double d1, double d2, double d3) {
        int i = MathHelper.b(d1);
        int j = MathHelper.b(d2);
        int l = MathHelper.b(d3);

        if (this.l.a(i, j - 1, l) == Block.aG.bi) {
            j--;
        }
        if (this.l.a(i, j, l) == Block.aG.bi) {
            int i1 = this.l.b(i, j, l);

            d2 = j;
            if (i1 >= 2 && i1 <= 5) {
                d2 = j + 1;
            }
            int ai[][] = an[i1];
            double d4 = 0.0D;
            double d5 = (double) i + 0.5D + (double) ai[0][0] * 0.5D;
            double d6 = (double) j + 0.5D + (double) ai[0][1] * 0.5D;
            double d7 = (double) l + 0.5D + (double) ai[0][2] * 0.5D;
            double d8 = (double) i + 0.5D + (double) ai[1][0] * 0.5D;
            double d9 = (double) j + 0.5D + (double) ai[1][1] * 0.5D;
            double d10 = (double) l + 0.5D + (double) ai[1][2] * 0.5D;
            double d11 = d8 - d5;
            double d12 = (d9 - d6) * 2D;
            double d13 = d10 - d7;

            if (d11 == 0.0D) {
                d1 = (double) i + 0.5D;
                d4 = d3 - (double) l;
            } else if (d13 == 0.0D) {
                d3 = (double) l + 0.5D;
                d4 = d1 - (double) i;
            } else {
                double d14 = d1 - d5;
                double d15 = d3 - d7;
                double d16 = (d14 * d11 + d15 * d13) * 2D;

                d4 = d16;
            }
            d1 = d5 + d11 * d4;
            d2 = d6 + d12 * d4;
            d3 = d7 + d13 * d4;
            if (d12 < 0.0D) {
                d2++;
            }
            if (d12 > 0.0D) {
                d2 += 0.5D;
            }
            return Vec3D.b(d1, d2, d3);
        } else {
            return null;
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Type", d);
        if (d == 2) {
            nbttagcompound.a("PushX", f);
            nbttagcompound.a("PushZ", ak);
            nbttagcompound.a("Fuel", (short) e);
        } else if (d == 1) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 0; i < al.length; i++) {
                if (al[i] != null) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.a("Slot", (byte) i);
                    al[i].a(nbttagcompound1);
                    nbttaglist.a(((NBTBase) (nbttagcompound1)));
                }
            }

            nbttagcompound.a("Items", ((NBTBase) (nbttaglist)));
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        d = nbttagcompound.d("Type");
        if (d == 2) {
            f = nbttagcompound.g("PushX");
            ak = nbttagcompound.g("PushZ");
            e = ((int) (nbttagcompound.c("Fuel")));
        } else if (d == 1) {
            NBTTagList nbttaglist = nbttagcompound.k("Items");

            al = new ItemStack[h_()];
            for (int i = 0; i < nbttaglist.b(); i++) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
                int j = nbttagcompound1.b("Slot") & 0xff;

                if (j >= 0 && j < al.length) {
                    al[j] = new ItemStack(nbttagcompound1);
                }
            }
        }
    }

    public void c(Entity entity) {
        if (l.z) {
            return;
        }
        if (entity == j) {
            return;
        }

        // CraftBukkit start
        CraftServer server = ((WorldServer)l).getServer();
        Type eventType = Type.VEHICLE_COLLISION_ENTITY;
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();
        org.bukkit.entity.Entity hitEntity = (entity == null)?null:entity.getBukkitEntity();
        
        VehicleEntityCollisionEvent collsionEvent = new VehicleEntityCollisionEvent(eventType, vehicle, hitEntity);
        server.getPluginManager().callEvent(collsionEvent);

        if (collsionEvent.isCancelled()) {
            return;
        }

        if ((entity instanceof EntityLiving) && !(entity instanceof EntityPlayer) && d == 0 && s * s + u * u > 0.01D && j == null && entity.k == null) {
            if (!collsionEvent.isPickupCancelled()) {
                eventType = Type.VEHICLE_ENTER;
                
                VehicleEnterEvent enterEvent = new VehicleEnterEvent(eventType, vehicle, hitEntity);
                server.getPluginManager().callEvent(enterEvent);

                if (!enterEvent.isCancelled()) {
                    entity.e(((Entity) (this)));
                }
            }
        }
        // CraftBukkit end

        double d1 = entity.p - p;
        double d2 = entity.r - r;
        double d3 = d1 * d1 + d2 * d2;

        // CraftBukkit
        if (d3 >= 9.9999997473787516E-005D && !collsionEvent.isCollisionCancelled()) {
            d3 = MathHelper.a(d3);
            d1 /= d3;
            d2 /= d3;
            double d4 = 1.0D / d3;

            if (d4 > 1.0D) {
                d4 = 1.0D;
            }
            d1 *= d4;
            d2 *= d4;
            d1 *= 0.10000000149011612D;
            d2 *= 0.10000000149011612D;
            d1 *= 1.0F - U;
            d2 *= 1.0F - U;
            d1 *= 0.5D;
            d2 *= 0.5D;
            if (entity instanceof EntityMinecart) {
                double d5 = entity.s + s;
                double d6 = entity.u + u;

                if (((EntityMinecart) entity).d == 2 && d != 2) {
                    s *= 0.20000000298023224D;
                    u *= 0.20000000298023224D;
                    f(entity.s - d1, 0.0D, entity.u - d2);
                    entity.s *= 0.69999998807907104D;
                    entity.u *= 0.69999998807907104D;
                } else if (((EntityMinecart) entity).d != 2 && d == 2) {
                    entity.s *= 0.20000000298023224D;
                    entity.u *= 0.20000000298023224D;
                    entity.f(s + d1, 0.0D, u + d2);
                    s *= 0.69999998807907104D;
                    u *= 0.69999998807907104D;
                } else {
                    d5 /= 2D;
                    d6 /= 2D;
                    s *= 0.20000000298023224D;
                    u *= 0.20000000298023224D;
                    f(d5 - d1, 0.0D, d6 - d2);
                    entity.s *= 0.20000000298023224D;
                    entity.u *= 0.20000000298023224D;
                    entity.f(d5 + d1, 0.0D, d6 + d2);
                }
            } else {
                f(-d1, 0.0D, -d2);
                entity.f(d1 / 4D, 0.0D, d2 / 4D);
            }
        }
    }

    public int h_() {
        return 27;
    }

    public ItemStack a(int i) {
        return al[i];
    }

    public ItemStack b(int i, int j) {
        if (al[i] != null) {
            if (al[i].a <= j) {
                ItemStack itemstack = al[i];

                al[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = al[i].a(j);

            if (al[i].a == 0) {
                al[i] = null;
            }
            return itemstack1;
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        al[i] = itemstack;
        if (itemstack != null && itemstack.a > c()) {
            itemstack.a = c();
        }
    }

    public String b() {
        return "Minecart";
    }

    public int c() {
        return 64;
    }

    public void d() {}

    public boolean a(EntityPlayer entityplayer) {
        if (d == 0) {
            if (j != null && (j instanceof EntityPlayer) && j != entityplayer) {
                return true;
            }
            if (!l.z) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) l).getServer();
                Type eventType = Type.VEHICLE_ENTER;
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity player = (entityplayer == null)?null:entityplayer.getBukkitEntity();
                
                VehicleEnterEvent event = new VehicleEnterEvent(eventType, vehicle, player);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
                // CraftBukkit end

                entityplayer.e(((Entity) (this)));
            }
        } else if (d == 1) {
            if (!l.z) {
                entityplayer.a(((IInventory) (this)));
            }
        } else if (d == 2) {
            ItemStack itemstack = entityplayer.an.e();

            if (itemstack != null && itemstack.c == Item.k.ba) {
                if (--itemstack.a == 0) {
                    entityplayer.an.a(entityplayer.an.c, ((ItemStack) (null)));
                }
                e += 1200;
            }
            f = p - entityplayer.p;
            ak = r - entityplayer.r;
        }
        return true;
    }

    public boolean a_(EntityPlayer entityplayer) {
        if (G) {
            return false;
        }
        return entityplayer.b(((Entity) (this))) <= 64D;
    }
    
    // CraftBukkit start
    @Override
    public org.bukkit.entity.Entity getBukkitEntity(){
        if (this.d == CraftMinecart.Type.StorageMinecart.getId()) {
            return this.bukkitStorageMinecart;
        } else if (this.d == CraftMinecart.Type.PoweredMinecart.getId()) {
            return this.bukkitPoweredMinecart;
        } else {
            return this.bukkitEntity;
        }
    }
    // CraftBukkit end
}
