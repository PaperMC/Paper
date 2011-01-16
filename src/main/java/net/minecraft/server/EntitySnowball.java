package net.minecraft.server;

import java.util.List;
import java.util.Random;

//CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSnowball;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
//CraftBukkit end

public class EntitySnowball extends Entity {

    private int b;
    private int c;
    private int d;
    private int e;
    private boolean f;
    public int a;
    private EntityLiving ak;
    private int al;
    private int am;

    public EntitySnowball(World world) {
        super(world);
        b = -1;
        c = -1;
        d = -1;
        e = 0;
        f = false;
        a = 0;
        am = 0;
        a(0.25F, 0.25F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftSnowball(server, this);
        //CraftBukkit end
    }

    protected void a() {}

    public EntitySnowball(World world, EntityLiving entityliving) {
        super(world);
        b = -1;
        c = -1;
        d = -1;
        e = 0;
        f = false;
        a = 0;
        am = 0;
        ak = entityliving;
        a(0.25F, 0.25F);
        c(entityliving.p, entityliving.q + (double) entityliving.w(), entityliving.r, entityliving.v, entityliving.w);
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

    public EntitySnowball(World world, double d1, double d2, double d3) {
        super(world);
        b = -1;
        c = -1;
        d = -1;
        e = 0;
        f = false;
        a = 0;
        am = 0;
        al = 0;
        a(0.25F, 0.25F);
        a(d1, d2, d3);
        H = 0.0F;
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
        al = 0;
    }

    public void b_() {
        O = p;
        P = q;
        Q = r;
        super.b_();
        if (a > 0) {
            a--;
        }
        if (f) {
            int i = this.l.a(b, c, d);

            if (i != e) {
                f = false;
                s *= W.nextFloat() * 0.2F;
                t *= W.nextFloat() * 0.2F;
                u *= W.nextFloat() * 0.2F;
                al = 0;
                am = 0;
            } else {
                al++;
                if (al == 1200) {
                    q();
                }
                return;
            }
        } else {
            am++;
        }
        Vec3D vec3d = Vec3D.b(p, q, r);
        Vec3D vec3d1 = Vec3D.b(p + s, q + t, r + u);
        MovingObjectPosition movingobjectposition = this.l.a(vec3d, vec3d1);

        vec3d = Vec3D.b(p, q, r);
        vec3d1 = Vec3D.b(p + s, q + t, r + u);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.b(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }
        if (!this.l.z) {
            Entity entity = null;
            List list = this.l.b(((Entity) (this)), z.a(s, t, u).b(1.0D, 1.0D, 1.0D));
            double d1 = 0.0D;

            for (int k = 0; k < list.size(); k++) {
                Entity entity1 = (Entity) list.get(k);

                if (!entity1.c_() || entity1 == ak && am < 5) {
                    continue;
                }
                float f4 = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.z.b(f4, f4, f4);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                if (movingobjectposition1 == null) {
                    continue;
                }
                double d2 = vec3d.a(movingobjectposition1.f);

                if (d2 < d1 || d1 == 0.0D) {
                    entity = entity1;
                    d1 = d2;
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }
        if (movingobjectposition != null) {
            if (movingobjectposition.g != null) {
                // CraftBukkit start
                boolean bounce;
                if (movingobjectposition.g instanceof EntityLiving) {
                    CraftServer server = ((WorldServer) this.l).getServer();
                    CraftEntity damagee = new CraftLivingEntity(server, (EntityLiving) movingobjectposition.g);
                    CraftEntity damager = new CraftLivingEntity(server, ak);
                    CraftEntity projectile = new CraftSnowball(server, (EntitySnowball) this);

                    //TODO @see EntityArrow#162
                    EntityDamageByProjectileEvent edbpe = new EntityDamageByProjectileEvent( damager, damagee, projectile, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);

                    server.getPluginManager().callEvent(edbpe);
                    if(!edbpe.isCancelled()) {
                        // this function returns if the snowball should stick or not, i.e. !bounce
                        bounce = !movingobjectposition.g.a(((Entity) (ak)), edbpe.getDamage());
                    } else {
                        // event was cancelled, get if the snowball should bounce or not
                        bounce = edbpe.getBounce();
                    }
                } else {
                    bounce = !movingobjectposition.g.a(((Entity) (ak)), 0);
                }
                if (!bounce) {
                    // CraftBukkit end
                    ;
                }
            }
            for (int j = 0; j < 8; j++) {
                this.l.a("snowballpoof", p, q, r, 0.0D, 0.0D, 0.0D);
            }

            q();
        }
        p += s;
        q += t;
        r += u;
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
        float f2 = 0.99F;
        float f5 = 0.03F;

        if (v()) {
            for (int l = 0; l < 4; l++) {
                float f3 = 0.25F;

                this.l.a("bubble", p - s * (double) f3, q - t * (double) f3, r - u * (double) f3, s, t, u);
            }

            f2 = 0.8F;
        }
        s *= f2;
        t *= f2;
        u *= f2;
        t -= f5;
        a(p, q, r);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) b);
        nbttagcompound.a("yTile", (short) c);
        nbttagcompound.a("zTile", (short) d);
        nbttagcompound.a("inTile", (byte) e);
        nbttagcompound.a("shake", (byte) a);
        nbttagcompound.a("inGround", (byte) (f ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        b = ((int) (nbttagcompound.c("xTile")));
        c = ((int) (nbttagcompound.c("yTile")));
        d = ((int) (nbttagcompound.c("zTile")));
        e = nbttagcompound.b("inTile") & 0xff;
        a = nbttagcompound.b("shake") & 0xff;
        f = nbttagcompound.b("inGround") == 1;
    }

    public void b(EntityPlayer entityplayer) {
        if (f && ak == entityplayer && a <= 0 && entityplayer.an.a(new ItemStack(Item.j, 1))) {
            l.a(((Entity) (this)), "random.pop", 0.2F, ((W.nextFloat() - W.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.c(((Entity) (this)), 1);
            q();
        }
    }
}
