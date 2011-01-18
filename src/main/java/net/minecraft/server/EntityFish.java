package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftFish;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
//CraftBukkit end

public class EntityFish extends Entity {

    private int d;
    private int e;
    private int f;
    private int ak;
    private boolean al;
    public int a;
    public EntityPlayer b;
    private int am;
    private int an;
    private int ao;
    public Entity c;
    private int ap;
    private double aq;
    private double ar;
    private double as;
    private double at;
    private double au;

    public EntityFish(World world) {
        super(world);
        d = -1;
        e = -1;
        f = -1;
        ak = 0;
        al = false;
        a = 0;
        an = 0;
        ao = 0;
        c = null;
        a(0.25F, 0.25F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftFish(server, this);
        //CraftBukkit end
    }

    protected void a() {}

    public EntityFish(World world, EntityPlayer entityplayer) {
        super(world);
        d = -1;
        e = -1;
        f = -1;
        ak = 0;
        al = false;
        a = 0;
        an = 0;
        ao = 0;
        c = null;
        b = entityplayer;
        b.aE = this;
        a(0.25F, 0.25F);
        c(entityplayer.p, (entityplayer.q + 1.6200000000000001D) - (double) entityplayer.H, entityplayer.r, entityplayer.v, entityplayer.w);
        p -= MathHelper.b((v / 180F) * 3.141593F) * 0.16F;
        q -= 0.10000000149011612D;
        r -= MathHelper.a((v / 180F) * 3.141593F) * 0.16F;
        a(p, q, r);
        H = 0.0F;
        float f1 = 0.4F;

        s = -MathHelper.a((v / 180F) * 3.141593F) * MathHelper.b((w / 180F) * 3.141593F) * f1;
        u = MathHelper.b((v / 180F) * 3.141593F) * MathHelper.b((w / 180F) * 3.141593F) * f1;
        t = -MathHelper.a((w / 180F) * 3.141593F) * f1;
        a(s, t, u, 1.5F, 1.0F);
    }

    public void a(double d1, double d2, double d3, float f1, float f2) {
        float f3 = MathHelper.a(d1 * d1 + d2 * d2 + d3 * d3);

        d1 /= f3;
        d2 /= f3;
        d3 /= f3;
        d1 += W.nextGaussian() * 0.0074999998323619366D * (double) f2;
        d2 += W.nextGaussian() * 0.0074999998323619366D * (double) f2;
        d3 += W.nextGaussian() * 0.0074999998323619366D * (double) f2;
        d1 *= f1;
        d2 *= f1;
        d3 *= f1;
        s = d1;
        t = d2;
        u = d3;
        float f4 = MathHelper.a(d1 * d1 + d3 * d3);

        x = v = (float) ((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D);
        y = w = (float) ((Math.atan2(d2, f4) * 180D) / 3.1415927410125732D);
        am = 0;
    }

    public void b_() {
        super.b_();
        if (ap > 0) {
            double d1 = p + (aq - p) / (double) ap;
            double d2 = q + (ar - q) / (double) ap;
            double d3 = r + (as - r) / (double) ap;
            double d4;

            for (d4 = at - (double) v; d4 < -180D; d4 += 360D) {
                ;
            }
            for (; d4 >= 180D; d4 -= 360D) {
                ;
            }
            v += ((float) (d4 / (double) ap));
            w += ((float) ((au - (double) w) / (double) ap));
            ap--;
            a(d1, d2, d3);
            b(v, w);
            return;
        }
        if (!this.l.z) {
            ItemStack itemstack = b.P();

            if (b.G || !b.B() || itemstack == null || itemstack.a() != Item.aP || b(((Entity) (b))) > 1024D) {
                q();
                b.aE = null;
                return;
            }
            if (c != null) {
                if (c.G) {
                    c = null;
                } else {
                    p = c.p;
                    q = c.z.b + (double) c.J * 0.80000000000000004D;
                    r = c.r;
                    return;
                }
            }
        }
        if (a > 0) {
            a--;
        }
        if (al) {
            int i = this.l.a(d, e, f);

            if (i != ak) {
                al = false;
                s *= W.nextFloat() * 0.2F;
                t *= W.nextFloat() * 0.2F;
                u *= W.nextFloat() * 0.2F;
                am = 0;
                an = 0;
            } else {
                am++;
                if (am == 1200) {
                    q();
                }
                return;
            }
        } else {
            an++;
        }
        Vec3D vec3d = Vec3D.b(p, q, r);
        Vec3D vec3d1 = Vec3D.b(p + s, q + t, r + u);
        MovingObjectPosition movingobjectposition = this.l.a(vec3d, vec3d1);

        vec3d = Vec3D.b(p, q, r);
        vec3d1 = Vec3D.b(p + s, q + t, r + u);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.b(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }
        Entity entity = null;
        List list = this.l.b(((Entity) (this)), z.a(s, t, u).b(1.0D, 1.0D, 1.0D));
        double d5 = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.c_() || entity1 == b && an < 5) {
                continue;
            }
            float f3 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.z.b(f3, f3, f3);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

            if (movingobjectposition1 == null) {
                continue;
            }
            double d6 = vec3d.a(movingobjectposition1.f);

            if (d6 < d5 || d5 == 0.0D) {
                entity = entity1;
                d5 = d6;
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        if (movingobjectposition != null) {
            if (movingobjectposition.g != null) {
                // CraftBukkit start
                //TODO add EntityDamagedByProjectileEvent : fishing hook?
                boolean bounce;
                if (movingobjectposition.g instanceof EntityLiving) {
                    CraftServer server = ((WorldServer) this.l).getServer();

                    //TODO @see EntityArrow#162
                    EntityDamageByProjectileEvent edbpe = new EntityDamageByProjectileEvent(b.getBukkitEntity(), movingobjectposition.g.getBukkitEntity(), this.getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);

                    server.getPluginManager().callEvent(edbpe);
                    if(!edbpe.isCancelled()) {
                        // this function returns if the fish should stick or not, i.e. !bounce
                        bounce = !movingobjectposition.g.a(((Entity) (b)), edbpe.getDamage());
                    } else {
                        // event was cancelled, get if the fish should bounce or not
                        bounce = edbpe.getBounce();
                    }
                } else {
                    bounce = !movingobjectposition.g.a(((Entity) (b)), 0);
                }
                if (!bounce) {
                    // CraftBukkit end
                    c = movingobjectposition.g;
                }
            } else {
                al = true;
            }
        }
        if (al) {
            return;
        }
        c(s, t, u);
        float f1 = MathHelper.a(s * s + u * u);

        v = (float) ((Math.atan2(s, u) * 180D) / 3.1415927410125732D);
        for (w = (float) ((Math.atan2(t, f1) * 180D) / 3.1415927410125732D); w - y < -180F; y -= 360F) {
            ;
        }
        for (; w - y >= 180F; y += 360F) {
            ;
        }
        for (; v - x < -180F; x -= 360F) {
            ;
        }
        for (; v - x >= 180F; x += 360F) {
            ;
        }
        w = y + (w - y) * 0.2F;
        v = x + (v - x) * 0.2F;
        float f2 = 0.92F;

        if (A || B) {
            f2 = 0.5F;
        }
        int k = 5;
        double d8 = 0.0D;

        for (int l = 0; l < k; l++) {
            double d9 = ((z.b + ((z.e - z.b) * (double) (l + 0)) / (double) k) - 0.125D) + 0.125D;
            double d10 = ((z.b + ((z.e - z.b) * (double) (l + 1)) / (double) k) - 0.125D) + 0.125D;
            AxisAlignedBB axisalignedbb1 = AxisAlignedBB.b(z.a, d9, z.c, z.d, d10, z.f);

            if (this.l.b(axisalignedbb1, Material.f)) {
                d8 += 1.0D / (double) k;
            }
        }

        if (d8 > 0.0D) {
            if (ao > 0) {
                ao--;
            } else if (W.nextInt(500) == 0) {
                ao = W.nextInt(30) + 10;
                t -= 0.20000000298023224D;
                this.l.a(((Entity) (this)), "random.splash", 0.25F, 1.0F + (W.nextFloat() - W.nextFloat()) * 0.4F);
                float f4 = MathHelper.b(z.b);

                for (int i1 = 0; (float) i1 < 1.0F + I * 20F; i1++) {
                    float f5 = (W.nextFloat() * 2.0F - 1.0F) * I;
                    float f7 = (W.nextFloat() * 2.0F - 1.0F) * I;

                    this.l.a("bubble", p + (double) f5, f4 + 1.0F, r + (double) f7, s, t - (double) (W.nextFloat() * 0.2F), u);
                }

                for (int j1 = 0; (float) j1 < 1.0F + I * 20F; j1++) {
                    float f6 = (W.nextFloat() * 2.0F - 1.0F) * I;
                    float f8 = (W.nextFloat() * 2.0F - 1.0F) * I;

                    this.l.a("splash", p + (double) f6, f4 + 1.0F, r + (double) f8, s, t, u);
                }
            }
        }
        if (ao > 0) {
            t -= (double) (W.nextFloat() * W.nextFloat() * W.nextFloat()) * 0.20000000000000001D;
        }
        double d7 = d8 * 2D - 1.0D;

        t += 0.039999999105930328D * d7;
        if (d8 > 0.0D) {
            f2 = (float) ((double) f2 * 0.90000000000000002D);
            t *= 0.80000000000000004D;
        }
        s *= f2;
        t *= f2;
        u *= f2;
        a(p, q, r);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) d);
        nbttagcompound.a("yTile", (short) e);
        nbttagcompound.a("zTile", (short) f);
        nbttagcompound.a("inTile", (byte) ak);
        nbttagcompound.a("shake", (byte) a);
        nbttagcompound.a("inGround", (byte) (al ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        d = ((int) (nbttagcompound.c("xTile")));
        e = ((int) (nbttagcompound.c("yTile")));
        f = ((int) (nbttagcompound.c("zTile")));
        ak = nbttagcompound.b("inTile") & 0xff;
        a = nbttagcompound.b("shake") & 0xff;
        al = nbttagcompound.b("inGround") == 1;
    }

    public int d() {
        byte byte0 = 0;

        if (c != null) {
            double d1 = b.p - p;
            double d2 = b.q - q;
            double d3 = b.r - r;
            double d4 = MathHelper.a(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 0.10000000000000001D;

            c.s += d1 * d5;
            c.t += d2 * d5 + (double) MathHelper.a(d4) * 0.080000000000000002D;
            c.u += d3 * d5;
            byte0 = 3;
        } else if (ao > 0) {
            EntityItem entityitem = new EntityItem(l, p, q, r, new ItemStack(Item.aS));
            double d6 = b.p - p;
            double d7 = b.q - q;
            double d8 = b.r - r;
            double d9 = MathHelper.a(d6 * d6 + d7 * d7 + d8 * d8);
            double d10 = 0.10000000000000001D;

            entityitem.s = d6 * d10;
            entityitem.t = d7 * d10 + (double) MathHelper.a(d9) * 0.080000000000000002D;
            entityitem.u = d8 * d10;
            l.a(((Entity) (entityitem)));
            byte0 = 1;
        }
        if (al) {
            byte0 = 2;
        }
        q();
        b.aE = null;
        return ((int) (byte0));
    }
}
