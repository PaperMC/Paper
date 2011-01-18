package net.minecraft.server;

import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;

public abstract class EntityLiving extends Entity {

    public int aF;
    public float aG;
    public float aH;
    public float aI;
    public float aJ;
    protected float aK;
    protected float aL;
    protected float aM;
    protected float aN;
    protected boolean aO;
    protected String aP;
    protected boolean aQ;
    protected float aR;
    protected String aS;
    protected float aT;
    protected int aU;
    protected float aV;
    public boolean aW;
    public float aX;
    public float aY;
    public int aZ;
    public int ba;
    private int a;
    public int bb;
    public int bc;
    public float bd;
    public int be;
    public int bf;
    public float bg;
    public float bh;
    protected boolean bi;
    public int bj;
    public float bk;
    public float bl;
    public float bm;
    public float bn;
    protected int bo;
    protected double bp;
    protected double bq;
    protected double br;
    protected double bs;
    protected double bt;
    float bu;
    protected int bv;
    protected int bw;
    protected float bx;
    protected float by;
    protected float bz;
    protected boolean bA;
    protected float bB;
    protected float bC;
    private Entity b;
    private int c;

    public EntityLiving(World world) {
        super(world);
        aF = 20;
        aI = 0.0F;
        aJ = 0.0F;
        aO = true;
        aP = "/mob/char.png";
        aQ = true;
        aR = 0.0F;
        aS = null;
        aT = 1.0F;
        aU = 0;
        aV = 0.0F;
        aW = false;
        bd = 0.0F;
        be = 0;
        bf = 0;
        bi = false;
        bj = -1;
        bk = (float) (Math.random() * 0.89999997615814209D + 0.10000000149011612D);
        bu = 0.0F;
        bv = 0;
        bw = 0;
        bA = false;
        bB = 0.0F;
        bC = 0.7F;
        c = 0;
        aZ = 10;
        i = true;
        aH = (float) (Math.random() + 1.0D) * 0.01F;
        a(p, q, r);
        aG = (float) Math.random() * 12398F;
        v = (float) (Math.random() * 3.1415927410125732D * 2D);
        S = 0.5F;
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftLivingEntity(server, this);
        //CraftBukkit end
    }

    protected void a() {}

    public boolean i(Entity entity) {
        return l.a(Vec3D.b(p, q + (double) w(), r), Vec3D.b(entity.p, entity.q + (double) entity.w(), entity.r)) == null;
    }

    public boolean c_() {
        return !G;
    }

    public boolean z() {
        return !G;
    }

    public float w() {
        return J * 0.85F;
    }

    public int c() {
        return 80;
    }

    public void r() {
        aX = aY;
        super.r();
        if (W.nextInt(1000) < a++) {
            a = -c();
            String s = e();

            if (s != null) {
                this.l.a(((Entity) (this)), s, i(), (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
            }
        }
        if (B() && C()) {
            a(((Entity) (null)), 1);
        }
        if (ae || this.l.z) {
            Z = 0;
        }
        if (B() && a(Material.f) && !d_()) {
            ad--;
            if (ad == -20) {
                ad = 0;
                for (int k = 0; k < 8; k++) {
                    float f1 = W.nextFloat() - W.nextFloat();
                    float f2 = W.nextFloat() - W.nextFloat();
                    float f3 = W.nextFloat() - W.nextFloat();

                    this.l.a("bubble", p + (double) f1, q + (double) f2, r + (double) f3, this.s, t, u);
                }

                a(((Entity) (null)), 2);
            }
            Z = 0;
        } else {
            ad = aa;
        }
        bg = bh;
        if (bf > 0) {
            bf--;
        }
        if (bb > 0) {
            bb--;
        }
        if (ac > 0) {
            ac--;
        }
        if (aZ <= 0) {
            be++;
            if (be > 20) {
                T();
                q();
                for (int l = 0; l < 20; l++) {
                    double d1 = W.nextGaussian() * 0.02D;
                    double d2 = W.nextGaussian() * 0.02D;
                    double d3 = W.nextGaussian() * 0.02D;

                    this.l.a("explode", (p + (double) (W.nextFloat() * I * 2.0F)) - (double) I, q + (double) (W.nextFloat() * J), (r + (double) (W.nextFloat() * I * 2.0F)) - (double) I, d1, d2, d3);
                }
            }
        }
        aN = aM;
        aJ = aI;
        x = v;
        y = w;
    }

    public void R() {
        for (int k = 0; k < 20; k++) {
            double d1 = W.nextGaussian() * 0.02D;
            double d2 = W.nextGaussian() * 0.02D;
            double d3 = W.nextGaussian() * 0.02D;
            double d4 = 10D;

            l.a("explode", (p + (double) (W.nextFloat() * I * 2.0F)) - (double) I - d1 * d4, (q + (double) (W.nextFloat() * J)) - d2 * d4, (r + (double) (W.nextFloat() * I * 2.0F)) - (double) I - d3 * d4, d1, d2, d3);
        }
    }

    public void D() {
        super.D();
        aK = aL;
        aL = 0.0F;
    }

    public void b_() {
        super.b_();
        o();
        double d1 = p - m;
        double d2 = r - o;
        float f1 = MathHelper.a(d1 * d1 + d2 * d2);
        float f2 = aI;
        float f3 = 0.0F;

        aK = aL;
        float f4 = 0.0F;

        if (f1 > 0.05F) {
            f4 = 1.0F;
            f3 = f1 * 3F;
            f2 = ((float) Math.atan2(d2, d1) * 180F) / 3.141593F - 90F;
        }
        if (aY > 0.0F) {
            f2 = v;
        }
        if (!A) {
            f4 = 0.0F;
        }
        aL = aL + (f4 - aL) * 0.3F;
        float f5;

        for (f5 = f2 - aI; f5 < -180F; f5 += 360F) {
            ;
        }
        for (; f5 >= 180F; f5 -= 360F) {
            ;
        }
        aI += f5 * 0.3F;
        float f6;

        for (f6 = v - aI; f6 < -180F; f6 += 360F) {
            ;
        }
        for (; f6 >= 180F; f6 -= 360F) {
            ;
        }
        boolean flag = f6 < -90F || f6 >= 90F;

        if (f6 < -75F) {
            f6 = -75F;
        }
        if (f6 >= 75F) {
            f6 = 75F;
        }
        aI = v - f6;
        if (f6 * f6 > 2500F) {
            aI += f6 * 0.2F;
        }
        if (flag) {
            f3 *= -1F;
        }
        for (; v - x < -180F; x -= 360F) {
            ;
        }
        for (; v - x >= 180F; x += 360F) {
            ;
        }
        for (; aI - aJ < -180F; aJ -= 360F) {
            ;
        }
        for (; aI - aJ >= 180F; aJ += 360F) {
            ;
        }
        for (; w - y < -180F; y -= 360F) {
            ;
        }
        for (; w - y >= 180F; y += 360F) {
            ;
        }
        aM += f3;
    }

    protected void a(float f1, float f2) {
        super.a(f1, f2);
    }

    public void d(int k) {
        if (aZ <= 0) {
            return;
        }
        aZ += k;
        if (aZ > 20) {
            aZ = 20;
        }
        ac = aF / 2;
    }

    public boolean a(Entity entity, int k) {
        if (l.z) {
            return false;
        }
        bw = 0;
        if (aZ <= 0) {
            return false;
        }
        bm = 1.5F;
        boolean flag = true;

        if ((float) ac > (float) aF / 2.0F) {
            if (k <= bv) {
                return false;
            }
            e(k - bv);
            bv = k;
            flag = false;
        } else {
            bv = k;
            ba = aZ;
            ac = aF;
            e(k);
            bb = bc = 10;
        }
        bd = 0.0F;
        if (flag) {
            l.a(((Entity) (this)), (byte) 2);
            y();
            if (entity != null) {
                double d1 = entity.p - p;
                double d2;

                for (d2 = entity.r - r; d1 * d1 + d2 * d2 < 0.0001D; d2 = (Math.random() - Math.random()) * 0.01D) {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }

                bd = (float) ((Math.atan2(d2, d1) * 180D) / 3.1415927410125732D) - v;
                a(entity, k, d1, d2);
            } else {
                bd = (int) (Math.random() * 2D) * 180;
            }
        }
        if (aZ <= 0) {
            if (flag) {
                l.a(((Entity) (this)), g(), i(), (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
            }
            f(entity);
        } else if (flag) {
            l.a(((Entity) (this)), f(), i(), (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
        }
        return true;
    }

    protected void e(int k) {
        aZ -= k;
    }

    protected float i() {
        return 1.0F;
    }

    protected String e() {
        return null;
    }

    protected String f() {
        return "random.hurt";
    }

    protected String g() {
        return "random.hurt";
    }

    public void a(Entity entity, int k, double d1, double d2) {
        float f1 = MathHelper.a(d1 * d1 + d2 * d2);
        float f2 = 0.4F;

        s /= 2D;
        t /= 2D;
        u /= 2D;
        s -= (d1 / (double) f1) * (double) f2;
        t += 0.40000000596046448D;
        u -= (d2 / (double) f1) * (double) f2;
        if (t > 0.40000000596046448D) {
            t = 0.40000000596046448D;
        }
    }

    public void f(Entity entity) {
        if (aU > 0 && entity != null) {
            entity.b(((Entity) (this)), aU);
        }
        bi = true;
        if (!l.z) {
            g_();
        }
        l.a(((Entity) (this)), (byte) 3);
    }

    protected void g_() {
        int k = h();

        if (k > 0) {
            int l = W.nextInt(3);

            for (int i1 = 0; i1 < l; i1++) {
                a(k, 1);
            }
        }
    }

    protected int h() {
        return 0;
    }

    protected void a(float f1) {
        int k = (int) Math.ceil(f1 - 3F);

        if (k > 0) {
            a(((Entity) (null)), k);
            int l = this.l.a(MathHelper.b(p), MathHelper.b(q - 0.20000000298023224D - (double) H), MathHelper.b(r));

            if (l > 0) {
                StepSound stepsound = Block.m[l].br;

                this.l.a(((Entity) (this)), stepsound.c(), stepsound.a() * 0.5F, stepsound.b() * 0.75F);
            }
        }
    }

    public void c(float f1, float f2) {
        if (v()) {
            double d1 = q;

            a(f1, f2, 0.02F);
            c(s, t, u);
            s *= 0.80000001192092896D;
            t *= 0.80000001192092896D;
            u *= 0.80000001192092896D;
            t -= 0.02D;
            if (B && b(s, ((t + 0.60000002384185791D) - q) + d1, u)) {
                t = 0.30000001192092896D;
            }
        } else if (x()) {
            double d2 = q;

            a(f1, f2, 0.02F);
            c(s, t, u);
            s *= 0.5D;
            t *= 0.5D;
            u *= 0.5D;
            t -= 0.02D;
            if (B && b(s, ((t + 0.60000002384185791D) - q) + d2, u)) {
                t = 0.30000001192092896D;
            }
        } else {
            float f3 = 0.91F;

            if (A) {
                f3 = 0.5460001F;
                int k = this.l.a(MathHelper.b(p), MathHelper.b(z.b) - 1, MathHelper.b(r));

                if (k > 0) {
                    f3 = Block.m[k].bu * 0.91F;
                }
            }
            float f4 = 0.1627714F / (f3 * f3 * f3);

            a(f1, f2, A ? 0.1F * f4 : 0.02F);
            f3 = 0.91F;
            if (A) {
                f3 = 0.5460001F;
                int l = this.l.a(MathHelper.b(p), MathHelper.b(z.b) - 1, MathHelper.b(r));

                if (l > 0) {
                    f3 = Block.m[l].bu * 0.91F;
                }
            }
            if (m()) {
                N = 0.0F;
                if (t < -0.14999999999999999D) {
                    t = -0.14999999999999999D;
                }
            }
            c(s, t, u);
            if (B && m()) {
                t = 0.20000000000000001D;
            }
            t -= 0.080000000000000002D;
            t *= 0.98000001907348633D;
            s *= f3;
            u *= f3;
        }
        bl = bm;
        double d3 = p - m;
        double d4 = r - o;
        float f5 = MathHelper.a(d3 * d3 + d4 * d4) * 4F;

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }
        bm += (f5 - bm) * 0.4F;
        bn += bm;
    }

    public boolean m() {
        int k = MathHelper.b(p);
        int l = MathHelper.b(z.b);
        int i1 = MathHelper.b(r);

        return this.l.a(k, l, i1) == Block.aF.bi || this.l.a(k, l + 1, i1) == Block.aF.bi;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) aZ);
        nbttagcompound.a("HurtTime", (short) bb);
        nbttagcompound.a("DeathTime", (short) be);
        nbttagcompound.a("AttackTime", (short) bf);
    }

    public void b(NBTTagCompound nbttagcompound) {
        aZ = ((int) (nbttagcompound.c("Health")));
        if (!nbttagcompound.a("Health")) {
            aZ = 10;
        }
        bb = ((int) (nbttagcompound.c("HurtTime")));
        be = ((int) (nbttagcompound.c("DeathTime")));
        bf = ((int) (nbttagcompound.c("AttackTime")));
    }

    public boolean B() {
        return !G && aZ > 0;
    }

    public boolean d_() {
        return false;
    }

    public void o() {
        if (bo > 0) {
            double d1 = p + (bp - p) / (double) bo;
            double d2 = q + (bq - q) / (double) bo;
            double d3 = r + (br - r) / (double) bo;
            double d4;

            for (d4 = bs - (double) v; d4 < -180D; d4 += 360D) {
                ;
            }
            for (; d4 >= 180D; d4 -= 360D) {
                ;
            }
            v += ((float) (d4 / (double) bo));
            w += ((float) ((bt - (double) w) / (double) bo));
            bo--;
            a(d1, d2, d3);
            b(v, w);
        }
        if (aZ <= 0) {
            bA = false;
            bx = 0.0F;
            by = 0.0F;
            bz = 0.0F;
        } else if (!aW) {
            d();
        }
        boolean flag = v();
        boolean flag1 = x();

        if (bA) {
            if (flag) {
                t += 0.039999999105930328D;
            } else if (flag1) {
                t += 0.039999999105930328D;
            } else if (A) {
                S();
            }
        }
        bx *= 0.98F;
        by *= 0.98F;
        bz *= 0.9F;
        c(bx, by);
        List list = l.b(((Entity) (this)), z.b(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                Entity entity = (Entity) list.get(k);

                if (entity.z()) {
                    entity.c(((Entity) (this)));
                }
            }
        }
    }

    protected void S() {
        t = 0.41999998688697815D;
    }

    protected void d() {
        bw++;
        EntityPlayer entityplayer = l.a(((Entity) (this)), -1D);

        if (entityplayer != null) {
            double d1 = ((Entity) (entityplayer)).p - p;
            double d2 = ((Entity) (entityplayer)).q - q;
            double d3 = ((Entity) (entityplayer)).r - r;
            double d4 = d1 * d1 + d2 * d2 + d3 * d3;

            if (d4 > 16384D) {
                q();
            }
            if (bw > 600 && W.nextInt(800) == 0) {
                if (d4 < 1024D) {
                    bw = 0;
                } else {
                    q();
                }
            }
        }
        bx = 0.0F;
        by = 0.0F;
        float f1 = 8F;

        if (W.nextFloat() < 0.02F) {
            EntityPlayer entityplayer1 = l.a(((Entity) (this)), f1);

            if (entityplayer1 != null) {
                b = ((Entity) (entityplayer1));
                c = 10 + W.nextInt(20);
            } else {
                bz = (W.nextFloat() - 0.5F) * 20F;
            }
        }
        if (b != null) {
            b(b, 10F);
            if (c-- <= 0 || b.G || b.b(((Entity) (this))) > (double) (f1 * f1)) {
                b = null;
            }
        } else {
            if (W.nextFloat() < 0.05F) {
                bz = (W.nextFloat() - 0.5F) * 20F;
            }
            v += bz;
            w = bB;
        }
        boolean flag = v();
        boolean flag1 = x();

        if (flag || flag1) {
            bA = W.nextFloat() < 0.8F;
        }
    }

    public void b(Entity entity, float f1) {
        double d1 = entity.p - p;
        double d2 = entity.r - r;
        double d3;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d3 = (entityliving.q + (double) entityliving.w()) - (q + (double) w());
        } else {
            d3 = (entity.z.b + entity.z.e) / 2D - (q + (double) w());
        }
        double d4 = MathHelper.a(d1 * d1 + d2 * d2);
        float f2 = (float) ((Math.atan2(d2, d1) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float) ((Math.atan2(d3, d4) * 180D) / 3.1415927410125732D);

        w = -b(w, f3, f1);
        v = b(v, f2, f1);
    }

    private float b(float f1, float f2, float f3) {
        float f4;

        for (f4 = f2 - f1; f4 < -180F; f4 += 360F) {
            ;
        }
        for (; f4 >= 180F; f4 -= 360F) {
            ;
        }
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f1 + f4;
    }

    public void T() {}

    public boolean b() {
        return l.a(z) && l.a(((Entity) (this)), z).size() == 0 && !l.b(z);
    }

    protected void t() {
        a(((Entity) (null)), 4);
    }

    public Vec3D G() {
        return c(1.0F);
    }

    public Vec3D c(float f1) {
        if (f1 == 1.0F) {
            float f2 = MathHelper.b(-v * 0.01745329F - 3.141593F);
            float f4 = MathHelper.a(-v * 0.01745329F - 3.141593F);
            float f6 = -MathHelper.b(-w * 0.01745329F);
            float f8 = MathHelper.a(-w * 0.01745329F);

            return Vec3D.b(f4 * f6, f8, f2 * f6);
        } else {
            float f3 = y + (w - y) * f1;
            float f5 = x + (v - x) * f1;
            float f7 = MathHelper.b(-f5 * 0.01745329F - 3.141593F);
            float f9 = MathHelper.a(-f5 * 0.01745329F - 3.141593F);
            float f10 = -MathHelper.b(-f3 * 0.01745329F);
            float f11 = MathHelper.a(-f3 * 0.01745329F);

            return Vec3D.b(f9 * f10, f11, f7 * f10);
        }
    }

    public int j() {
        return 4;
    }
}
