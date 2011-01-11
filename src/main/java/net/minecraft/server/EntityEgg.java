package net.minecraft.server;

import java.util.List;
import java.util.Random;
import org.bukkit.MobType;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EntityEgg extends Entity {

    private int b;
    private int c;
    private int d;
    private int e;
    private boolean f;
    public int a;
    private EntityLiving aj;
    private int ak;
    private int al;

    public EntityEgg(World world) {
        super(world);
        b = -1;
        c = -1;
        d = -1;
        e = 0;
        f = false;
        a = 0;
        al = 0;
        a(0.25F, 0.25F);
    }

    public EntityEgg(World world, EntityLiving entityliving) {
        super(world);
        b = -1;
        c = -1;
        d = -1;
        e = 0;
        f = false;
        a = 0;
        al = 0;
        aj = entityliving;
        a(0.25F, 0.25F);
        c(entityliving.p, entityliving.q + (double) entityliving.s(), entityliving.r, entityliving.v, entityliving.w);
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

    public void a(double d1, double d2, double d3, float f1,
            float f2) {
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
        ak = 0;
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
                ak = 0;
                al = 0;
            } else {
                ak++;
                if (ak == 1200) {
                    l();
                }
                return;
            }
        } else {
            al++;
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
            List list = this.l.b(this, z.a(s, t, u).b(1.0D, 1.0D, 1.0D));
            double d1 = 0.0D;

            for (int l = 0; l < list.size(); l++) {
                Entity entity1 = (Entity) list.get(l);

                if (!entity1.c_() || entity1 == aj && al < 5) {
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
                if (!movingobjectposition.g.a(aj, 0)) {
                    ;
                }
            }

            // Craftbukkit start
            boolean hatching = !this.l.z && W.nextInt(8) == 0;
            byte numHatching = (hatching && W.nextInt(32) == 0) ? (byte) 4 : (byte) 1;
            if (!hatching) {
                numHatching = 0;
            }
            MobType type = MobType.CHICKEN;

            if (aj instanceof EntityPlayerMP) {
                CraftServer server = ((WorldServer) l).getServer();
                CraftPlayer player = new CraftPlayer(server, (EntityPlayerMP) aj);
                PlayerEggThrowEvent event = new PlayerEggThrowEvent(Type.PLAYER_EGG_THROW, player, hatching, numHatching, type);
                server.getPluginManager().callEvent(event);
                hatching = event.isHatching();
                numHatching = event.getNumHatches();
                type = event.getHatchType();
            }

            if (hatching) {
                for (int k = 0; k < numHatching; k++) {
                    Entity entity = null;
                    switch (type) {
                        case CHICKEN:
                            entity = new EntityChicken(this.l);
                            break;
                        case COW:
                            entity = new EntityCow(this.l);
                            break;
                        case CREEPER:
                            entity = new EntityCreeper(this.l);
                            break;
                        case GHAST:
                            entity = new EntityGhast(this.l);
                            break;
                        case PIG:
                            entity = new EntityPig(this.l);
                            break;
                        case PIG_ZOMBIE:
                            entity = new EntityPigZombie(this.l);
                            break;
                        case SHEEP:
                            entity = new EntitySheep(this.l);
                            break;
                        case SKELETON:
                            entity = new EntitySkeleton(this.l);
                            break;
                        case SPIDER:
                            entity = new EntitySpider(this.l);
                            break;
                        case ZOMBIE:
                            entity = new EntityZombie(this.l);
                            break;
                        default:
                            entity = new EntityChicken(this.l);
                            break;
                    }
                    entity.c(p, q, r, v, 0.0F);
                    this.l.a(entity);
                }
            }
            // Craftbukkit stop

            for (int j = 0; j < 8; j++) {
                this.l.a("snowballpoof", p, q, r, 0.0D, 0.0D, 0.0D);
            }

            l();
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

        if (r()) {
            for (int i1 = 0; i1 < 4; i1++) {
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
        b = nbttagcompound.c("xTile");
        c = nbttagcompound.c("yTile");
        d = nbttagcompound.c("zTile");
        e = nbttagcompound.b("inTile") & 0xff;
        a = nbttagcompound.b("shake") & 0xff;
        f = nbttagcompound.b("inGround") == 1;
    }

    public void b(EntityPlayer entityplayer) {
        if (f && aj == entityplayer && a <= 0 && entityplayer.an.a(new ItemStack(Item.j.aW, 1))) {
            l.a(this, "random.pop", 0.2F, ((W.nextFloat() - W.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.c(this, 1);
            l();
        }
    }
}
