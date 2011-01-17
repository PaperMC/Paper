package net.minecraft.server;

import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftAnimals;
import org.bukkit.craftbukkit.entity.CraftGhast;

public class EntityGhast extends EntityFlying implements IMobs {

    public int a;
    public double b;
    public double c;
    public double d;
    private Entity ak;
    private int al;
    public int e;
    public int f;

    public EntityGhast(World world) {
        super(world);
        a = 0;
        ak = null;
        al = 0;
        e = 0;
        f = 0;
        aP = "/mob/ghast.png";
        a(4F, 4F);
        ae = true;
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftGhast(server, this);
        //CraftBukkit end
    }

    protected void d() {
        if (l.k == 0) {
            q();
        }
        e = f;
        double d1 = b - p;
        double d2 = c - q;
        double d3 = d - r;
        double d4 = MathHelper.a(d1 * d1 + d2 * d2 + d3 * d3);

        if (d4 < 1.0D || d4 > 60D) {
            b = p + (double) ((W.nextFloat() * 2.0F - 1.0F) * 16F);
            c = q + (double) ((W.nextFloat() * 2.0F - 1.0F) * 16F);
            d = r + (double) ((W.nextFloat() * 2.0F - 1.0F) * 16F);
        }
        if (a-- <= 0) {
            a += W.nextInt(5) + 2;
            if (a(b, c, d, d4)) {
                s += (d1 / d4) * 0.10000000000000001D;
                t += (d2 / d4) * 0.10000000000000001D;
                u += (d3 / d4) * 0.10000000000000001D;
            } else {
                b = p;
                c = q;
                d = r;
            }
        }
        if (ak != null && ak.G) {
            ak = null;
        }
        if (ak == null || al-- <= 0) {
            ak = ((Entity) (l.a(((Entity) (this)), 100D)));
            if (ak != null) {
                al = 20;
            }
        }
        double d5 = 64D;

        if (ak != null && ak.b(((Entity) (this))) < d5 * d5) {
            double d6 = ak.p - p;
            double d7 = (ak.z.b + (double) (ak.J / 2.0F)) - (q + (double) (J / 2.0F));
            double d8 = ak.r - r;

            aI = v = (-(float) Math.atan2(d6, d8) * 180F) / 3.141593F;
            if (i(ak)) {
                if (f == 10) {
                    l.a(((Entity) (this)), "mob.ghast.charge", i(), (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
                }
                f++;
                if (f == 20) {
                    l.a(((Entity) (this)), "mob.ghast.fireball", i(), (W.nextFloat() - W.nextFloat()) * 0.2F + 1.0F);
                    EntityFireball entityfireball = new EntityFireball(l, ((EntityLiving) (this)), d6, d7, d8);
                    double d9 = 4D;
                    Vec3D vec3d = c(1.0F);

                    entityfireball.p = p + vec3d.a * d9;
                    entityfireball.q = q + (double) (J / 2.0F) + 0.5D;
                    entityfireball.r = r + vec3d.c * d9;
                    l.a(((Entity) (entityfireball)));
                    f = -40;
                }
            } else if (f > 0) {
                f--;
            }
        } else {
            aI = v = (-(float) Math.atan2(s, u) * 180F) / 3.141593F;
            if (f > 0) {
                f--;
            }
        }
        aP = f <= 10 ? "/mob/ghast.png" : "/mob/ghast_fire.png";
    }

    private boolean a(double d1, double d2, double d3, double d4) {
        double d5 = (b - p) / d4;
        double d6 = (c - q) / d4;
        double d7 = (d - r) / d4;
        AxisAlignedBB axisalignedbb = z.b();

        for (int k = 1; (double) k < d4; k++) {
            axisalignedbb.d(d5, d6, d7);
            if (l.a(((Entity) (this)), axisalignedbb).size() > 0) {
                return false;
            }
        }

        return true;
    }

    protected String e() {
        return "mob.ghast.moan";
    }

    protected String f() {
        return "mob.ghast.scream";
    }

    protected String g() {
        return "mob.ghast.death";
    }

    protected int h() {
        return Item.K.ba;
    }

    protected float i() {
        return 10F;
    }

    public boolean b() {
        return W.nextInt(20) == 0 && super.b() && l.k > 0;
    }

    public int j() {
        return 1;
    }
}
