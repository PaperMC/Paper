package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
// CraftBukkit end

import java.util.List;
import java.util.Random;

public abstract class Entity {

    private static int a = 0;
    public int g;
    public double h;
    public boolean i;
    public Entity j;
    public Entity k;
    public World l;
    public double m;
    public double n;
    public double o;
    public double p;
    public double q;
    public double r;
    public double s;
    public double t;
    public double u;
    public float v;
    public float w;
    public float x;
    public float y;
    public final AxisAlignedBB z = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    public boolean A;
    public boolean B;
    public boolean C;
    public boolean D;
    public boolean E;
    public boolean F;
    public boolean G;
    public float H;
    public float I;
    public float J;
    public float K;
    public float L;
    protected boolean M;
    protected float N;
    private int b;
    public double O;
    public double P;
    public double Q;
    public float R;
    public float S;
    public boolean T;
    public float U;
    public boolean V;
    protected Random W;
    public int X;
    public int Y;
    public int Z;
    protected int aa;
    protected boolean ab;
    public int ac;
    public int ad;
    private boolean c;
    protected boolean ae;
    protected DataWatcher af;
    private double d;
    private double e;
    public boolean ag;
    public int ah;
    public int ai;
    public int aj;

    public Entity(World world) {
        g = a++;
        h = 1.0D;
        i = false;
        A = false;
        D = false;
        E = false;
        F = true;
        G = false;
        H = 0.0F;
        I = 0.6F;
        J = 1.8F;
        K = 0.0F;
        L = 0.0F;
        M = true;
        N = 0.0F;
        b = 1;
        R = 0.0F;
        S = 0.0F;
        T = false;
        U = 0.0F;
        V = false;
        W = new Random();
        X = 0;
        Y = 1;
        Z = 0;
        aa = 300;
        ab = false;
        ac = 0;
        ad = 300;
        c = true;
        ae = false;
        af = new DataWatcher();
        ag = false;
        l = world;
        a(0.0D, 0.0D, 0.0D);
        af.a(0, ((Byte.valueOf((byte) 0))));
        a();
    }

    protected abstract void a();

    public DataWatcher p() {
        return af;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            return ((Entity) obj).g == g;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return g;
    }

    public void q() {
        G = true;
    }

    protected void a(float f1, float f2) {
        I = f1;
        J = f2;
    }

    protected void b(float f1, float f2) {
        v = f1;
        w = f2;
    }

    public void a(double d1, double d2, double d3) {
        p = d1;
        q = d2;
        r = d3;
        float f1 = I / 2.0F;
        float f2 = J;

        z.c(d1 - (double) f1, (d2 - (double) H) + (double) R, d3 - (double) f1, d1 + (double) f1, (d2 - (double) H) + (double) R + (double) f2, d3 + (double) f1);
    }

    public void b_() {
        r();
    }

    public void r() {
        if (k != null && k.G) {
            k = null;
        }
        X++;
        K = L;
        m = p;
        n = q;
        o = r;
        y = w;
        x = v;
        if (v()) {
            if (!ab && !c) {
                float f1 = MathHelper.a(s * s * 0.20000000298023224D + t * t + u * u * 0.20000000298023224D) * 0.2F;

                if (f1 > 1.0F) {
                    f1 = 1.0F;
                }
                l.a(this, "random.splash", f1, 1.0F + (W.nextFloat() - W.nextFloat()) * 0.4F);
                float f2 = MathHelper.b(z.b);

                for (int i1 = 0; (float) i1 < 1.0F + I * 20F; i1++) {
                    float f3 = (W.nextFloat() * 2.0F - 1.0F) * I;
                    float f5 = (W.nextFloat() * 2.0F - 1.0F) * I;

                    l.a("bubble", p + (double) f3, f2 + 1.0F, r + (double) f5, s, t - (double) (W.nextFloat() * 0.2F), u);
                }

                for (int j1 = 0; (float) j1 < 1.0F + I * 20F; j1++) {
                    float f4 = (W.nextFloat() * 2.0F - 1.0F) * I;
                    float f6 = (W.nextFloat() * 2.0F - 1.0F) * I;

                    l.a("splash", p + (double) f4, f2 + 1.0F, r + (double) f6, s, t, u);
                }
            }
            N = 0.0F;
            ab = true;
            Z = 0;
        } else {
            ab = false;
        }
        if (l.z) {
            Z = 0;
        } else if (Z > 0) {
            if (ae) {
                Z -= 4;
                if (Z < 0) {
                    Z = 0;
                }
            } else {
                if (Z % 20 == 0) {
                    // CraftBukkit start
                    if(this instanceof EntityLiving) {
                        CraftServer server = ((WorldServer)l).getServer();
                        CraftEntity damagee = new CraftLivingEntity(server, (EntityLiving)this);

                        EntityDamageEvent ede = new EntityDamageEvent(damagee, EntityDamageEvent.DamageCause.DROWNING, 1);
                        server.getPluginManager().callEvent(ede);

                        if (!ede.isCancelled()){
                            a(((Entity) (null)), ede.getDamage());
                        }
                    } else {
                        a(((Entity) (null)), 1);
                    }
                    // CraftBukkit end
                }
                Z--;
            }
        }
        if (x()) {
            s();
        }
        if (q < -64D) {
            t();
        }
        if (!l.z) {
            a(0, Z > 0);
            a(2, k != null);
        }
        c = false;
    }

    protected void s() {
        if (!ae) {
            // CraftBukkit start
            if(this instanceof EntityLiving) {
                CraftServer server = ((WorldServer)l).getServer();
                CraftEntity defender = new CraftLivingEntity(server, (EntityLiving) this);

                EntityDamageByBlockEvent ede = new EntityDamageByBlockEvent(null, defender, EntityDamageEvent.DamageCause.LAVA, 4);
                server.getPluginManager().callEvent(ede);
                if (!ede.isCancelled()){
                    a(((Entity) (null)), ede.getDamage());
                }

                EntityCombustEvent ece = new EntityCombustEvent(Type.ENTITY_COMBUST, defender);
                server.getPluginManager().callEvent(ece);
                if (!ece.isCancelled()){
                    Z = 600;
                }
            } else {
                a(((Entity) (null)), 4);
                Z = 600;
            }
            // CraftBukkit end
        }
    }

    protected void t() {
        q();
    }

    public boolean b(double d1, double d2, double d3) {
        AxisAlignedBB axisalignedbb = z.c(d1, d2, d3);
        List list = l.a(this, axisalignedbb);

        if (list.size() > 0) {
            return false;
        }
        return !l.b(axisalignedbb);
    }

    public void c(double d1, double d2, double d3) {
        if (T) {
            z.d(d1, d2, d3);
            p = (z.a + z.d) / 2D;
            q = (z.b + (double) H) - (double) R;
            r = (z.c + z.f) / 2D;
            return;
        }
        double d4 = p;
        double d5 = r;
        double d6 = d1;
        double d7 = d2;
        double d8 = d3;
        AxisAlignedBB axisalignedbb = z.b();
        boolean flag = A && J();

        if (flag) {
            double d9 = 0.050000000000000003D;

            for (; d1 != 0.0D && l.a(this, z.c(d1, -1D, 0.0D)).size() == 0; d6 = d1) {
                if (d1 < d9 && d1 >= -d9) {
                    d1 = 0.0D;
                    continue;
                }
                if (d1 > 0.0D) {
                    d1 -= d9;
                } else {
                    d1 += d9;
                }
            }

            for (; d3 != 0.0D && l.a(this, z.c(0.0D, -1D, d3)).size() == 0; d8 = d3) {
                if (d3 < d9 && d3 >= -d9) {
                    d3 = 0.0D;
                    continue;
                }
                if (d3 > 0.0D) {
                    d3 -= d9;
                } else {
                    d3 += d9;
                }
            }
        }
        List list = l.a(this, z.a(d1, d2, d3));

        for (int i1 = 0; i1 < list.size(); i1++) {
            d2 = ((AxisAlignedBB) list.get(i1)).b(z, d2);
        }

        z.d(0.0D, d2, 0.0D);
        if (!F && d7 != d2) {
            d1 = d2 = d3 = 0.0D;
        }
        boolean flag1 = A || d7 != d2 && d7 < 0.0D;

        for (int j1 = 0; j1 < list.size(); j1++) {
            d1 = ((AxisAlignedBB) list.get(j1)).a(z, d1);
        }

        z.d(d1, 0.0D, 0.0D);
        if (!F && d6 != d1) {
            d1 = d2 = d3 = 0.0D;
        }
        for (int k1 = 0; k1 < list.size(); k1++) {
            d3 = ((AxisAlignedBB) list.get(k1)).c(z, d3);
        }

        z.d(0.0D, 0.0D, d3);
        if (!F && d8 != d3) {
            d1 = d2 = d3 = 0.0D;
        }
        if (S > 0.0F && flag1 && R < 0.05F && (d6 != d1 || d8 != d3)) {
            double d10 = d1;
            double d12 = d2;
            double d14 = d3;

            d1 = d6;
            d2 = S;
            d3 = d8;
            AxisAlignedBB axisalignedbb1 = z.b();

            z.b(axisalignedbb);
            List list1 = l.a(this, z.a(d1, d2, d3));

            for (int j2 = 0; j2 < list1.size(); j2++) {
                d2 = ((AxisAlignedBB) list1.get(j2)).b(z, d2);
            }

            z.d(0.0D, d2, 0.0D);
            if (!F && d7 != d2) {
                d1 = d2 = d3 = 0.0D;
            }
            for (int k2 = 0; k2 < list1.size(); k2++) {
                d1 = ((AxisAlignedBB) list1.get(k2)).a(z, d1);
            }

            z.d(d1, 0.0D, 0.0D);
            if (!F && d6 != d1) {
                d1 = d2 = d3 = 0.0D;
            }
            for (int l2 = 0; l2 < list1.size(); l2++) {
                d3 = ((AxisAlignedBB) list1.get(l2)).c(z, d3);
            }

            z.d(0.0D, 0.0D, d3);
            if (!F && d8 != d3) {
                d1 = d2 = d3 = 0.0D;
            }
            if (d10 * d10 + d14 * d14 >= d1 * d1 + d3 * d3) {
                d1 = d10;
                d2 = d12;
                d3 = d14;
                z.b(axisalignedbb1);
            } else {
                R += 0.5D;
            }
        }
        p = (z.a + z.d) / 2D;
        q = (z.b + (double) H) - (double) R;
        r = (z.c + z.f) / 2D;
        B = d6 != d1 || d8 != d3;
        C = d7 != d2;
        A = d7 != d2 && d7 < 0.0D;
        D = B || C;
        a(d2, A);
        if (d6 != d1) {
            s = 0.0D;
        }
        if (d7 != d2) {
            t = 0.0D;
        }
        if (d8 != d3) {
            u = 0.0D;
        }
        double d11 = p - d4;
        double d13 = r - d5;

        if (M && !flag) {
            L += ((float) ((double) MathHelper.a(d11 * d11 + d13 * d13) * 0.59999999999999998D));
            int k3 = MathHelper.b(p);
            int i4 = MathHelper.b(q - 0.20000000298023224D - (double) H);
            int l1 = MathHelper.b(r);
            int i3 = l.a(k3, i4, l1);

            if (L > (float) b && i3 > 0) {
                b++;
                StepSound stepsound = Block.m[i3].br;

                if (l.a(k3, i4 + 1, l1) == Block.aS.bi) {
                    stepsound = Block.aS.br;
                    l.a(this, stepsound.c(), stepsound.a() * 0.15F, stepsound.b());
                } else if (!Block.m[i3].bt.d()) {
                    l.a(this, stepsound.c(), stepsound.a() * 0.15F, stepsound.b());
                }
                Block.m[i3].b(l, k3, i4, l1, this);
            }
        }
        int l3 = MathHelper.b(z.a);
        int j4 = MathHelper.b(z.b);
        int i2 = MathHelper.b(z.c);
        int j3 = MathHelper.b(z.d);
        int k4 = MathHelper.b(z.e);
        int l4 = MathHelper.b(z.f);

        if (l.a(l3, j4, i2, j3, k4, l4)) {
            for (int i5 = l3; i5 <= j3; i5++) {
                for (int j5 = j4; j5 <= k4; j5++) {
                    for (int k5 = i2; k5 <= l4; k5++) {
                        int l5 = l.a(i5, j5, k5);

                        if (l5 > 0) {
                            Block.m[l5].a(l, i5, j5, k5, this);
                        }
                    }
                }
            }
        }
        R *= 0.4F;
        boolean flag2 = v();

        if (l.c(z)) {
            b(1);
            if (!flag2) {
                Z++;
                if (Z == 0) {
                    Z = 300;
                }
            }
        } else if (Z <= 0) {
            Z = -Y;
        }
        if (flag2 && Z > 0) {
            l.a(this, "random.fizz", 0.7F, 1.6F + (W.nextFloat() - W.nextFloat()) * 0.4F);
            Z = -Y;
        }
    }

    protected void a(double d1, boolean flag) {
        if (flag) {
            if (N > 0.0F) {
                a(N);
                N = 0.0F;
            }
        } else if (d1 < 0.0D) {
            N -= ((float) (d1));
        }
    }

    public AxisAlignedBB u() {
        return null;
    }

    protected void b(int i1) {
        if (!ae) {
            // CraftBukkit start
            if(this instanceof EntityLiving) {
                CraftEntity defender = null;
                CraftServer server = ((WorldServer)l).getServer();

                if (this instanceof EntityPlayerMP) {
                    defender = new CraftPlayer(server, (EntityPlayerMP)this);
                } else {
                    defender = new CraftLivingEntity(server, (EntityLiving)this);
                }

                EntityDamageEvent ede = new EntityDamageEvent(defender, EntityDamageEvent.DamageCause.FIRE, i1);
                server.getPluginManager().callEvent(ede);

                if (!ede.isCancelled()){
                    a(((Entity) (null)), ede.getDamage());
                }
                return;
            }
            // CraftBukkit end
            a(((Entity) (null)), i1);
        }
    }

    protected void a(float f1) {}

    public boolean v() {
        return l.a(z.b(0.0D, -0.40000000596046448D, 0.0D), Material.f, this);
    }

    public boolean a(Material material) {
        double d1 = q + (double) w();
        int i1 = MathHelper.b(p);
        int j1 = MathHelper.d(MathHelper.b(d1));
        int k1 = MathHelper.b(r);
        int l1 = l.a(i1, j1, k1);

        if (l1 != 0 && Block.m[l1].bt == material) {
            float f1 = BlockFluids.c(l.b(i1, j1, k1)) - 0.1111111F;
            float f2 = (float) (j1 + 1) - f1;

            return d1 < (double) f2;
        } else {
            return false;
        }
    }

    public float w() {
        return 0.0F;
    }

    public boolean x() {
        return l.a(z.b(-0.10000000149011612D, -0.40000000596046448D, -0.10000000149011612D), Material.g);
    }

    public void a(float f1, float f2, float f3) {
        float f4 = MathHelper.c(f1 * f1 + f2 * f2);

        if (f4 < 0.01F) {
            return;
        }
        if (f4 < 1.0F) {
            f4 = 1.0F;
        }
        f4 = f3 / f4;
        f1 *= f4;
        f2 *= f4;
        float f5 = MathHelper.a((v * 3.141593F) / 180F);
        float f6 = MathHelper.b((v * 3.141593F) / 180F);

        s += f1 * f6 - f2 * f5;
        u += f2 * f6 + f1 * f5;
    }

    public float b(float f1) {
        int i1 = MathHelper.b(p);
        double d1 = (z.e - z.b) * 0.66000000000000003D;
        int j1 = MathHelper.b((q - (double) H) + d1);
        int k1 = MathHelper.b(r);

        if (l.a(MathHelper.b(z.a), MathHelper.b(z.b), MathHelper.b(z.c), MathHelper.b(z.d), MathHelper.b(z.e), MathHelper.b(z.f))) {
            return l.l(i1, j1, k1);
        } else {
            return 0.0F;
        }
    }

    public void b(double d1, double d2, double d3, float f1, float f2) {
        m = p = d1;
        n = q = d2;
        o = r = d3;
        x = v = f1;
        y = w = f2;
        R = 0.0F;
        double d4 = x - f1;

        if (d4 < -180D) {
            x += 360F;
        }
        if (d4 >= 180D) {
            x -= 360F;
        }
        a(p, q, r);
        b(f1, f2);
    }

    public void c(double d1, double d2, double d3, float f1, float f2) {
        O = m = p = d1;
        P = n = q = d2 + (double) H;
        Q = o = r = d3;
        v = f1;
        w = f2;
        a(p, q, r);
    }

    public float a(Entity entity) {
        float f1 = (float) (p - entity.p);
        float f2 = (float) (q - entity.q);
        float f3 = (float) (r - entity.r);

        return MathHelper.c(f1 * f1 + f2 * f2 + f3 * f3);
    }

    public double d(double d1, double d2, double d3) {
        double d4 = p - d1;
        double d5 = q - d2;
        double d6 = r - d3;

        return d4 * d4 + d5 * d5 + d6 * d6;
    }

    public double e(double d1, double d2, double d3) {
        double d4 = p - d1;
        double d5 = q - d2;
        double d6 = r - d3;

        return (double) MathHelper.a(d4 * d4 + d5 * d5 + d6 * d6);
    }

    public double b(Entity entity) {
        double d1 = p - entity.p;
        double d2 = q - entity.q;
        double d3 = r - entity.r;

        return d1 * d1 + d2 * d2 + d3 * d3;
    }

    public void b(EntityPlayer entityplayer) {}

    public void c(Entity entity) {
        if (entity.j == this || entity.k == this) {
            return;
        }
        double d1 = entity.p - p;
        double d2 = entity.r - r;
        double d3 = MathHelper.a(d1, d2);

        if (d3 >= 0.0099999997764825821D) {
            d3 = MathHelper.a(d3);
            d1 /= d3;
            d2 /= d3;
            double d4 = 1.0D / d3;

            if (d4 > 1.0D) {
                d4 = 1.0D;
            }
            d1 *= d4;
            d2 *= d4;
            d1 *= 0.05000000074505806D;
            d2 *= 0.05000000074505806D;
            d1 *= 1.0F - U;
            d2 *= 1.0F - U;
            f(-d1, 0.0D, -d2);
            entity.f(d1, 0.0D, d2);
        }
    }

    public void f(double d1, double d2, double d3) {
        s += d1;
        t += d2;
        u += d3;
    }

    protected void y() {
        E = true;
    }

    public boolean a(Entity entity, int i1) {
        y();
        return false;
    }

    public boolean c_() {
        return false;
    }

    public boolean z() {
        return false;
    }

    public void b(Entity entity, int i1) {}

    public boolean c(NBTTagCompound nbttagcompound) {
        String s1 = A();

        if (G || s1 == null) {
            return false;
        } else {
            nbttagcompound.a("id", s1);
            d(nbttagcompound);
            return true;
        }
    }

    public void d(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Pos", ((NBTBase) (a(new double[] {
            p, q, r
        }))));
        nbttagcompound.a("Motion", ((NBTBase) (a(new double[] {
            s, t, u
        }))));
        nbttagcompound.a("Rotation", ((NBTBase) (a(new float[] {
            v, w
        }))));
        nbttagcompound.a("FallDistance", N);
        nbttagcompound.a("Fire", (short) Z);
        nbttagcompound.a("Air", (short) ad);
        nbttagcompound.a("OnGround", A);
        a(nbttagcompound);
    }

    public void e(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.k("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.k("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.k("Rotation");

        a(0.0D, 0.0D, 0.0D);
        s = ((NBTTagDouble) nbttaglist1.a(0)).a;
        t = ((NBTTagDouble) nbttaglist1.a(1)).a;
        u = ((NBTTagDouble) nbttaglist1.a(2)).a;
        m = O = p = ((NBTTagDouble) nbttaglist.a(0)).a;
        n = P = q = ((NBTTagDouble) nbttaglist.a(1)).a;
        o = Q = r = ((NBTTagDouble) nbttaglist.a(2)).a;
        x = v = ((NBTTagFloat) nbttaglist2.a(0)).a;
        y = w = ((NBTTagFloat) nbttaglist2.a(1)).a;
        N = nbttagcompound.f("FallDistance");
        Z = ((int) (nbttagcompound.c("Fire")));
        ad = ((int) (nbttagcompound.c("Air")));
        A = nbttagcompound.l("OnGround");
        a(p, q, r);
        b(nbttagcompound);
    }

    protected final String A() {
        return EntityList.b(this);
    }

    protected abstract void b(NBTTagCompound nbttagcompound);

    protected abstract void a(NBTTagCompound nbttagcompound);

    protected NBTTagList a(double ad1[]) {
        NBTTagList nbttaglist = new NBTTagList();
        double ad2[] = ad1;
        int i1 = ad2.length;

        for (int j1 = 0; j1 < i1; j1++) {
            double d1 = ad2[j1];

            nbttaglist.a(((NBTBase) (new NBTTagDouble(d1))));
        }

        return nbttaglist;
    }

    protected NBTTagList a(float af1[]) {
        NBTTagList nbttaglist = new NBTTagList();
        float af2[] = af1;
        int i1 = af2.length;

        for (int j1 = 0; j1 < i1; j1++) {
            float f1 = af2[j1];

            nbttaglist.a(((NBTBase) (new NBTTagFloat(f1))));
        }

        return nbttaglist;
    }

    public EntityItem a(int i1, int j1) {
        return a(i1, j1, 0.0F);
    }

    public EntityItem a(int i1, int j1, float f1) {
        return a(new ItemStack(i1, j1, 0), f1);
    }

    public EntityItem a(ItemStack itemstack, float f1) {
        EntityItem entityitem = new EntityItem(l, p, q + (double) f1, r, itemstack);

        entityitem.c = 10;
        l.a(((Entity) (entityitem)));
        return entityitem;
    }

    public boolean B() {
        return !G;
    }

    public boolean C() {
        int i1 = MathHelper.b(p);
        int j1 = MathHelper.b(q + (double) w());
        int k1 = MathHelper.b(r);

        return l.d(i1, j1, k1);
    }

    public boolean a(EntityPlayer entityplayer) {
        return false;
    }

    public AxisAlignedBB d(Entity entity) {
        return null;
    }

    public void D() {
        if (k.G) {
            k = null;
            return;
        }
        s = 0.0D;
        t = 0.0D;
        u = 0.0D;
        b_();
        k.E();
        e += k.v - k.x;
        d += k.w - k.y;
        for (; e >= 180D; e -= 360D) {
            ;
        }
        for (; e < -180D; e += 360D) {
            ;
        }
        for (; d >= 180D; d -= 360D) {
            ;
        }
        for (; d < -180D; d += 360D) {
            ;
        }
        double d1 = e * 0.5D;
        double d2 = d * 0.5D;
        float f1 = 10F;

        if (d1 > (double) f1) {
            d1 = f1;
        }
        if (d1 < (double) (-f1)) {
            d1 = -f1;
        }
        if (d2 > (double) f1) {
            d2 = f1;
        }
        if (d2 < (double) (-f1)) {
            d2 = -f1;
        }
        e -= d1;
        d -= d2;
        v += ((float) (d1));
        w += ((float) (d2));
    }

    public void E() {
        j.a(p, q + k() + j.F(), r);
    }

    public double F() {
        return (double) H;
    }

    public double k() {
        return (double) J * 0.75D;
    }

    public void e(Entity entity) {
        // CraftBukkit start
        setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // e(null) doesn't really fly for overloaded methods,
        // so this method is needed

        //CraftBukkit end

        d = 0.0D;
        e = 0.0D;
        if (entity == null) {
            if (k != null) {
                c(k.p, k.z.b + (double) k.J, k.r, v, w);
                k.j = null;
            }
            k = null;
            return;
        }
        if (k == entity) {
            k.j = null;
            k = null;
            c(entity.p, entity.z.b + (double) entity.J, entity.r, v, w);
            return;
        }
        if (k != null) {
            k.j = null;
        }
        if (entity.j != null) {
            entity.j.k = null;
        }
        k = entity;
        entity.j = this;
    }

    public Vec3D G() {
        return null;
    }

    public void H() {}

    public ItemStack[] I() {
        return null;
    }

    public boolean J() {
        return c(1);
    }

    public void b(boolean flag) {
        a(1, flag);
    }

    protected boolean c(int i1) {
        return (af.a(0) & 1 << i1) != 0;
    }

    protected void a(int i1, boolean flag) {
        byte byte0 = af.a(0);

        if (flag) {
            af.b(0, ((Byte.valueOf((byte) (byte0 | 1 << i1)))));
        } else {
            af.b(0, ((Byte.valueOf((byte) (byte0 & ~(1 << i1))))));
        }
    }
}
