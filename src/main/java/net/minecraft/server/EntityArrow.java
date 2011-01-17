package net.minecraft.server;

import java.util.List;
import java.util.Random;

//CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
//CraftBukkit end

public class EntityArrow extends Entity {

    private int c;
    private int d;
    private int e;
    private int f;
    private boolean ak;
    public int a;
    public EntityLiving b;
    private int al;
    private int am;
    
    public EntityArrow(World world) {
        super(world);
        c = -1;
        d = -1;
        e = -1;
        f = 0;
        ak = false;
        a = 0;
        am = 0;
        a(0.5F, 0.5F);
        
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftArrow(server, this);
        //CraftBukkit end
    }

    public EntityArrow(World world, double d1, double d2, double d3) {
        super(world);
        c = -1;
        d = -1;
        e = -1;
        f = 0;
        ak = false;
        a = 0;
        am = 0;
        a(0.5F, 0.5F);
        a(d1, d2, d3);
        H = 0.0F;
    }

    public EntityArrow(World world, EntityLiving entityliving) {
        super(world);
        c = -1;
        d = -1;
        e = -1;
        f = 0;
        ak = false;
        a = 0;
        am = 0;
        b = entityliving;
        a(0.5F, 0.5F);
        c(entityliving.p, entityliving.q + (double) entityliving.w(), entityliving.r, entityliving.v, entityliving.w);
        p -= MathHelper.b((v / 180F) * 3.141593F) * 0.16F;
        q -= 0.10000000149011612D;
        r -= MathHelper.a((v / 180F) * 3.141593F) * 0.16F;
        a(p, q, r);
        H = 0.0F;
        s = -MathHelper.a((v / 180F) * 3.141593F) * MathHelper.b((w / 180F) * 3.141593F);
        u = MathHelper.b((v / 180F) * 3.141593F) * MathHelper.b((w / 180F) * 3.141593F);
        t = -MathHelper.a((w / 180F) * 3.141593F);
        a(s, t, u, 1.5F, 1.0F);
    }

    protected void a() {}

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
        super.b_();
        if (y == 0.0F && x == 0.0F) {
            float f1 = MathHelper.a(s * s + u * u);

            x = v = (float) ((Math.atan2(s, u) * 180D) / 3.1415927410125732D);
            y = w = (float) ((Math.atan2(t, f1) * 180D) / 3.1415927410125732D);
        }
        if (a > 0) {
            a--;
        }
        if (ak) {
            int i = l.a(c, d, e);

            if (i != f) {
                ak = false;
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

            if (!entity1.c_() || entity1 == b && am < 5) {
                continue;
            }
            float f5 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.z.b(f5, f5, f5);
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
                if (entity instanceof EntityLiving) {
                    CraftServer server = ((WorldServer) this.l).getServer();

                    //TODO decide if we should create DamageCause.ARROW, DamageCause.PROJECTILE
                    // or leave as DamageCause.ENTITY_ATTACK
                    EntityDamageByProjectileEvent edbpe = new EntityDamageByProjectileEvent(b.getBukkitEntity(), entity.getBukkitEntity(), this.getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, 4);

                    server.getPluginManager().callEvent(edbpe);
                    if(!edbpe.isCancelled()) {
                        // this function returns if the arrow should stick in or not, i.e. !bounce
                        bounce = !movingobjectposition.g.a(((Entity) (b)), edbpe.getDamage());
                    } else {
                        // event was cancelled, get if the arrow should bounce or not
                        bounce = edbpe.getBounce();
                    }
                } else {
                    bounce = !movingobjectposition.g.a(((Entity) (b)), 4);
                }
                if (!bounce) {
                 // CraftBukkit end
                    l.a(((Entity) (this)), "random.drr", 1.0F, 1.2F / (W.nextFloat() * 0.2F + 0.9F));
                    q();
                } else {
                    s *= -0.10000000149011612D;
                    t *= -0.10000000149011612D;
                    u *= -0.10000000149011612D;
                    v += 180F;
                    x += 180F;
                    am = 0;
                }
            } else {
                c = movingobjectposition.b;
                d = movingobjectposition.c;
                e = movingobjectposition.d;
                f = l.a(c, d, e);
                s = (float) (movingobjectposition.f.a - p);
                t = (float) (movingobjectposition.f.b - q);
                u = (float) (movingobjectposition.f.c - r);
                float f2 = MathHelper.a(s * s + t * t + u * u);

                p -= (s / (double) f2) * 0.05000000074505806D;
                q -= (t / (double) f2) * 0.05000000074505806D;
                r -= (u / (double) f2) * 0.05000000074505806D;
                l.a(((Entity) (this)), "random.drr", 1.0F, 1.2F / (W.nextFloat() * 0.2F + 0.9F));
                ak = true;
                a = 7;
            }
        }
        p += s;
        q += t;
        r += u;
        float f3 = MathHelper.a(s * s + u * u);

        v = (float) ((Math.atan2(s, u) * 180D) / 3.1415927410125732D);
        for (w = (float) ((Math.atan2(t, f3) * 180D) / 3.1415927410125732D); w - y < -180F; y -= 360F) {
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
        float f4 = 0.99F;
        float f6 = 0.03F;

        if (v()) {
            for (int k = 0; k < 4; k++) {
                float f7 = 0.25F;

                l.a("bubble", p - s * (double) f7, q - t * (double) f7, r - u * (double) f7, s, t, u);
            }

            f4 = 0.8F;
        }
        s *= f4;
        t *= f4;
        u *= f4;
        t -= f6;
        a(p, q, r);
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) c);
        nbttagcompound.a("yTile", (short) d);
        nbttagcompound.a("zTile", (short) e);
        nbttagcompound.a("inTile", (byte) f);
        nbttagcompound.a("shake", (byte) a);
        nbttagcompound.a("inGround", (byte) (ak ? 1 : 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        c = ((int) (nbttagcompound.c("xTile")));
        d = ((int) (nbttagcompound.c("yTile")));
        e = ((int) (nbttagcompound.c("zTile")));
        f = nbttagcompound.b("inTile") & 0xff;
        a = nbttagcompound.b("shake") & 0xff;
        ak = nbttagcompound.b("inGround") == 1;
    }

    public void b(EntityPlayer entityplayer) {
        if (l.z) {
            return;
        }
        if (ak && b == entityplayer && a <= 0 && entityplayer.an.a(new ItemStack(Item.j, 1))) {
            l.a(((Entity) (this)), "random.pop", 0.2F, ((W.nextFloat() - W.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.c(((Entity) (this)), 1);
            q();
        }
    }
}
