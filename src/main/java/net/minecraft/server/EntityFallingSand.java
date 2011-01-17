package net.minecraft.server;

public class EntityFallingSand extends Entity {

    public int a;
    public int b;

    public EntityFallingSand(World world) {
        super(world);
        b = 0;
    }

    public EntityFallingSand(World world, double d, double d1, double d2, int i) {
        super(world);
        b = 0;
        a = i;
        this.i = true;
        a(0.98F, 0.98F);
        H = J / 2.0F;
        a(d, d1, d2);
        s = 0.0D;
        t = 0.0D;
        u = 0.0D;
        M = false;
        m = d;
        n = d1;
        o = d2;
    }

    protected void a() {}

    public boolean c_() {
        return !G;
    }

    public void b_() {
        if (a == 0) {
            q();
            return;
        }
        m = p;
        n = q;
        o = r;
        b++;
        t -= 0.039999999105930328D;
        c(s, t, u);
        s *= 0.98000001907348633D;
        t *= 0.98000001907348633D;
        u *= 0.98000001907348633D;
        int i = MathHelper.b(p);
        int j = MathHelper.b(q);
        int k = MathHelper.b(r);

        if (l.a(i, j, k) == a) {
            l.e(i, j, k, 0);
        }
        if (A) {
            s *= 0.69999998807907104D;
            u *= 0.69999998807907104D;
            t *= -0.5D;
            q();
            if ((!l.a(a, i, j, k, true) || !l.e(i, j, k, a)) && !l.z) {
                a(a, 1);
            }
        } else if (b > 100 && !l.z) {
            a(a, 1);
            q();
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Tile", (byte) a);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        a = nbttagcompound.b("Tile") & 0xff;
    }
}
