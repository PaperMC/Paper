package net.minecraft.server;

import java.util.Random;

public class EntityCreature extends EntityLiving {

    private PathEntity a;
    protected Entity d;
    protected boolean e;

    public EntityCreature(World world) {
        super(world);
        e = false;
    }

    protected void d() {
        e = false;
        float f = 16F;

        if (d == null) {
            d = l();
            if (d != null) {
                a = l.a(((Entity) (this)), d, f);
            }
        } else if (!d.B()) {
            d = null;
        } else {
            float f1 = d.a(((Entity) (this)));

            if (i(d)) {
                a(d, f1);
            }
        }
        if (!e && d != null && (a == null || W.nextInt(20) == 0)) {
            a = l.a(((Entity) (this)), d, f);
        } else if (a == null && W.nextInt(80) == 0 || W.nextInt(80) == 0) {
            boolean flag = false;
            int j = -1;
            int k = -1;
            int i1 = -1;
            float f2 = -99999F;

            for (int j1 = 0; j1 < 10; j1++) {
                int k1 = MathHelper.b((p + (double) W.nextInt(13)) - 6D);
                int l1 = MathHelper.b((q + (double) W.nextInt(7)) - 3D);
                int i2 = MathHelper.b((r + (double) W.nextInt(13)) - 6D);
                float f3 = a(k1, l1, i2);

                if (f3 > f2) {
                    f2 = f3;
                    j = k1;
                    k = l1;
                    i1 = i2;
                    flag = true;
                }
            }

            if (flag) {
                a = l.a(((Entity) (this)), j, k, i1, 10F);
            }
        }
        int i = MathHelper.b(z.b);
        boolean flag1 = v();
        boolean flag2 = x();

        w = 0.0F;
        if (a == null || W.nextInt(100) == 0) {
            super.d();
            a = null;
            return;
        }
        Vec3D vec3d = a.a(((Entity) (this)));

        for (double d1 = I * 2.0F; vec3d != null && vec3d.d(p, vec3d.b, r) < d1 * d1;) {
            a.a();
            if (a.b()) {
                vec3d = null;
                a = null;
            } else {
                vec3d = a.a(((Entity) (this)));
            }
        }

        bA = false;
        if (vec3d != null) {
            double d2 = vec3d.a - p;
            double d3 = vec3d.c - r;
            double d4 = vec3d.b - (double) i;
            float f4 = (float) ((Math.atan2(d3, d2) * 180D) / 3.1415927410125732D) - 90F;
            float f5 = f4 - v;

            by = bC;
            for (; f5 < -180F; f5 += 360F) {
                ;
            }
            for (; f5 >= 180F; f5 -= 360F) {
                ;
            }
            if (f5 > 30F) {
                f5 = 30F;
            }
            if (f5 < -30F) {
                f5 = -30F;
            }
            v += f5;
            if (e && d != null) {
                double d5 = d.p - p;
                double d6 = d.r - r;
                float f7 = v;

                v = (float) ((Math.atan2(d6, d5) * 180D) / 3.1415927410125732D) - 90F;
                float f6 = (((f7 - v) + 90F) * 3.141593F) / 180F;

                bx = -MathHelper.a(f6) * by * 1.0F;
                by = MathHelper.b(f6) * by * 1.0F;
            }
            if (d4 > 0.0D) {
                bA = true;
            }
        }
        if (d != null) {
            b(d, 30F);
        }
        if (B) {
            bA = true;
        }
        if (W.nextFloat() < 0.8F && (flag1 || flag2)) {
            bA = true;
        }
    }

    protected void a(Entity entity, float f) {}

    protected float a(int i, int j, int k) {
        return 0.0F;
    }

    protected Entity l() {
        return null;
    }

    public boolean b() {
        int i = MathHelper.b(p);
        int j = MathHelper.b(z.b);
        int k = MathHelper.b(r);

        return super.b() && a(i, j, k) >= 0.0F;
    }
}
