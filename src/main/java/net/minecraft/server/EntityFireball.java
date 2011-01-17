package net.minecraft.server;

import java.util.List;
import java.util.Random;

//CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEgg;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftFireball;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
//CraftBukkit end

public class EntityFireball extends Entity {

    private int e;
    private int f;
    private int ak;
    private int al;
    private boolean am;
    public int a;
    private EntityLiving an;
    private int ao;
    private int ap;
    public double b;
    public double c;
    public double d;

    public EntityFireball(World world) {
        super(world);
        e = -1;
        f = -1;
        ak = -1;
        al = 0;
        am = false;
        a = 0;
        ap = 0;
        a(1.0F, 1.0F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftFireball(server, this);
        //CraftBukkit end
    }

    protected void a() {}

    public EntityFireball(World world, EntityLiving entityliving, double d1, double d2, double d3) {
        super(world);
        e = -1;
        f = -1;
        ak = -1;
        al = 0;
        am = false;
        a = 0;
        ap = 0;
        an = entityliving;
        a(1.0F, 1.0F);
        c(entityliving.p, entityliving.q, entityliving.r, entityliving.v, entityliving.w);
        a(p, q, r);
        H = 0.0F;
        s = t = u = 0.0D;
        d1 += W.nextGaussian() * 0.40000000000000002D;
        d2 += W.nextGaussian() * 0.40000000000000002D;
        d3 += W.nextGaussian() * 0.40000000000000002D;
        double d4 = MathHelper.a(d1 * d1 + d2 * d2 + d3 * d3);

        b = (d1 / d4) * 0.10000000000000001D;
        c = (d2 / d4) * 0.10000000000000001D;
        d = (d3 / d4) * 0.10000000000000001D;
    }

    public void b_() {
        super.b_();
        Z = 10;
        if (a > 0) {
            a--;
        }
        if (am) {
            int i = l.a(e, f, ak);

            if (i != al) {
                am = false;
                s *= W.nextFloat() * 0.2F;
                t *= W.nextFloat() * 0.2F;
                u *= W.nextFloat() * 0.2F;
                ao = 0;
                ap = 0;
            } else {
                ao++;
                if (ao == 1200) {
                    q();
                }
                return;
            }
        } else {
            ap++;
        }
        Vec3D vec3d = Vec3D.b(p, q, r);
        Vec3D vec3d1 = Vec3D.b(p + s, q + t, r + u);
        MovingObjectPosition movingobjectposition = l.a(vec3d, vec3d1);

        vec3d = Vec3D.b(p, q, r);
        vec3d1 = Vec3D.b(p + s, q + t, r + u);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.b(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }
        Entity entity = null;
        List list = l.b(((Entity) (this)), z.a(s, t, u).b(1.0D, 1.0D, 1.0D));
        double d1 = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.c_() || entity1 == an && ap < 25) {
                continue;
            }
            float f3 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.z.b(f3, f3, f3);
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
        if (movingobjectposition != null) {
            if (movingobjectposition.g != null) {
                // CraftBukkit start
                boolean bounce;
                if (movingobjectposition.g instanceof EntityLiving) {
                    CraftServer server = ((WorldServer) this.l).getServer();

                    //TODO @see EntityArrow#162
                    EntityDamageByProjectileEvent edbpe = new EntityDamageByProjectileEvent( an.getBukkitEntity(), movingobjectposition.g.getBukkitEntity(), this.getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);

                    server.getPluginManager().callEvent(edbpe);
                    if(!edbpe.isCancelled()) {
                        // this function returns if the fireball should stick or not, i.e. !bounce
                        bounce = !movingobjectposition.g.a(((Entity) (an)), edbpe.getDamage());
                    } else {
                        // event was cancelled, get if the fireball should bounce or not
                        bounce = edbpe.getBounce();
                    }
                } else {
                    bounce = !movingobjectposition.g.a(((Entity) (an)), 0);
                }
                if (!bounce) {
                    // CraftBukkit end
                    ;
                }
            }
            l.a(((Entity) (null)), p, q, r, 1.0F, true);
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
        float f2 = 0.95F;

        if (v()) {
            for (int k = 0; k < 4; k++) {
                float f4 = 0.25F;

                l.a("bubble", p - s * (double) f4, q - t * (double) f4, r - u * (double) f4, s, t, u);
            }

            f2 = 0.8F;
        }
        s += b;
        t += c;
        u += d;
        s *= f2;
        t *= f2;
        u *= f2;
        l.a("smoke", p, q + 0.5D, r, 0.0D, 0.0D, 0.0D);
        a(p, q, r);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) e);
        nbttagcompound.a("yTile", (short) f);
        nbttagcompound.a("zTile", (short) ak);
        nbttagcompound.a("inTile", (byte) al);
        nbttagcompound.a("shake", (byte) a);
        nbttagcompound.a("inGround", (byte) (am ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        e = ((int) (nbttagcompound.c("xTile")));
        f = ((int) (nbttagcompound.c("yTile")));
        ak = ((int) (nbttagcompound.c("zTile")));
        al = nbttagcompound.b("inTile") & 0xff;
        a = nbttagcompound.b("shake") & 0xff;
        am = nbttagcompound.b("inGround") == 1;
    }

    public boolean c_() {
        return true;
    }

    public boolean a(Entity entity, int i) {
        y();
        if (entity != null) {
            Vec3D vec3d = entity.G();

            if (vec3d != null) {
                s = vec3d.a;
                t = vec3d.b;
                u = vec3d.c;
                b = s * 0.10000000000000001D;
                c = t * 0.10000000000000001D;
                d = u * 0.10000000000000001D;
            }
            return true;
        } else {
            return false;
        }
    }
}
