package net.minecraft.server;

public class EntityFlying extends EntityLiving {

    public EntityFlying(World world) {
        super(world);
    }

    protected void a(float f) {}

    public void c(float f, float f1) {
        if (v()) {
            a(f, f1, 0.02F);
            c(s, t, u);
            s *= 0.80000001192092896D;
            t *= 0.80000001192092896D;
            u *= 0.80000001192092896D;
        } else if (x()) {
            a(f, f1, 0.02F);
            c(s, t, u);
            s *= 0.5D;
            t *= 0.5D;
            u *= 0.5D;
        } else {
            float f2 = 0.91F;

            if (A) {
                f2 = 0.5460001F;
                int i = l.a(MathHelper.b(p), MathHelper.b(z.b) - 1, MathHelper.b(r));

                if (i > 0) {
                    f2 = Block.m[i].bu * 0.91F;
                }
            }
            float f3 = 0.1627714F / (f2 * f2 * f2);

            a(f, f1, A ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (A) {
                f2 = 0.5460001F;
                int j = l.a(MathHelper.b(p), MathHelper.b(z.b) - 1, MathHelper.b(r));

                if (j > 0) {
                    f2 = Block.m[j].bu * 0.91F;
                }
            }
            c(s, t, u);
            s *= f2;
            t *= f2;
            u *= f2;
        }
        bl = bm;
        double d = p - m;
        double d1 = r - o;
        float f4 = MathHelper.a(d * d + d1 * d1) * 4F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }
        bm += (f4 - bm) * 0.4F;
        bn += bm;
    }

    public boolean m() {
        return false;
    }
}
