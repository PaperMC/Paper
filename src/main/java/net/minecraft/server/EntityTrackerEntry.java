package net.minecraft.server;

import java.util.*;

public class EntityTrackerEntry {

    public Entity a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public double i;
    public double j;
    public double k;
    public int l;
    private double o;
    private double p;
    private double q;
    private boolean r;
    private boolean s;
    public boolean m;
    public Set n;

    public EntityTrackerEntry(Entity entity, int i1, int j1, boolean flag) {
        l = 0;
        r = false;
        m = false;
        n = ((Set) (new HashSet()));
        a = entity;
        b = i1;
        c = j1;
        s = flag;
        d = MathHelper.b(entity.p * 32D);
        e = MathHelper.b(entity.q * 32D);
        f = MathHelper.b(entity.r * 32D);
        g = MathHelper.d((entity.v * 256F) / 360F);
        h = MathHelper.d((entity.w * 256F) / 360F);
    }

    public boolean equals(Object obj) {
        if (obj instanceof EntityTrackerEntry) {
            return ((EntityTrackerEntry) obj).a.g == a.g;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return a.g;
    }

    public void a(List list) {
        m = false;
        if (!r || a.d(o, p, q) > 16D) {
            o = a.p;
            p = a.q;
            q = a.r;
            r = true;
            m = true;
            b(list);
        }
        if (++l % c == 0) {
            int i1 = MathHelper.b(a.p * 32D);
            int j1 = MathHelper.b(a.q * 32D);
            int k1 = MathHelper.b(a.r * 32D);
            int l1 = MathHelper.d((a.v * 256F) / 360F);
            int i2 = MathHelper.d((a.w * 256F) / 360F);
            boolean flag = i1 != d || j1 != e || k1 != f;
            boolean flag1 = l1 != g || i2 != h;
            int j2 = i1 - d;
            int k2 = j1 - e;
            int l2 = k1 - f;
            Object obj = null;

            if (j2 < -128 || j2 >= 128 || k2 < -128 || k2 >= 128 || l2 < -128 || l2 >= 128) {
                obj = ((new Packet34EntityTeleport(a.g, i1, j1, k1, (byte) l1, (byte) i2)));
            } else if (flag && flag1) {
                obj = ((new Packet33RelEntityMoveLook(a.g, (byte) j2, (byte) k2, (byte) l2, (byte) l1, (byte) i2)));
            } else if (flag) {
                obj = ((new Packet31RelEntityMove(a.g, (byte) j2, (byte) k2, (byte) l2)));
            } else if (flag1) {
                obj = ((new Packet32EntityLook(a.g, (byte) l1, (byte) i2)));
            } else {
                obj = ((new Packet30Entity(a.g)));
            }
            if (s) {
                double d1 = a.s - i;
                double d2 = a.t - j;
                double d3 = a.u - k;
                double d4 = 0.02D;
                double d5 = d1 * d1 + d2 * d2 + d3 * d3;

                if (d5 > d4 * d4 || d5 > 0.0D && a.s == 0.0D && a.t == 0.0D && a.u == 0.0D) {
                    i = a.s;
                    j = a.t;
                    k = a.u;
                    a(((Packet) (new Packet28(a.g, i, j, k))));
                }
            }
            if (obj != null) {
                a(((Packet) (obj)));
            }
            DataWatcher datawatcher = a.p();

            if (datawatcher.a()) {
                b(((Packet) (new Packet40(a.g, datawatcher))));
            }
            d = i1;
            e = j1;
            f = k1;
            g = l1;
            h = i2;
        }
        if (a.E) {
            b(((Packet) (new Packet28(a))));
            a.E = false;
        }
    }

    public void a(Packet packet) {
        EntityPlayerMP entityplayermp;

        for (Iterator iterator = n.iterator(); iterator.hasNext(); entityplayermp.a.b(packet)) {
            entityplayermp = (EntityPlayerMP) iterator.next();
        }
    }

    public void b(Packet packet) {
        a(packet);
        if (a instanceof EntityPlayerMP) {
            ((EntityPlayerMP) a).a.b(packet);
        }
    }

    public void a() {
        a(((Packet) (new Packet29DestroyEntity(a.g))));
    }

    public void a(EntityPlayerMP entityplayermp) {
        if (n.contains(((entityplayermp)))) {
            n.remove(((entityplayermp)));
        }
    }

    public void b(EntityPlayerMP entityplayermp) {
        if (entityplayermp == a) {
            return;
        }
        double d1 = entityplayermp.p - (double) (d / 32);
        double d2 = entityplayermp.r - (double) (f / 32);

        if (d1 >= (double) (-b) && d1 <= (double) b && d2 >= (double) (-b) && d2 <= (double) b) {
            if (!n.contains(((entityplayermp)))) {
                n.add(((entityplayermp)));
                entityplayermp.a.b(b());
                if (s) {
                    entityplayermp.a.b(((Packet) (new Packet28(a.g, a.s, a.t, a.u))));
                }
                ItemStack aitemstack[] = a.I();

                if (aitemstack != null) {
                    for (int i1 = 0; i1 < aitemstack.length; i1++) {
                        entityplayermp.a.b(((Packet) (new Packet5PlayerInventory(a.g, i1, aitemstack[i1]))));
                    }
                }
            }
        } else if (n.contains(((entityplayermp)))) {
            n.remove(((entityplayermp)));
            entityplayermp.a.b(((Packet) (new Packet29DestroyEntity(a.g))));
        }
    }

    public void b(List list) {
        for (int i1 = 0; i1 < list.size(); i1++) {
            b((EntityPlayerMP) list.get(i1));
        }
    }

    private Packet b() {
        if (a instanceof EntityItem) {
            EntityItem entityitem = (EntityItem) a;
            Packet21PickupSpawn packet21pickupspawn = new Packet21PickupSpawn(entityitem);

            entityitem.p = (double) packet21pickupspawn.b / 32D;
            entityitem.q = (double) packet21pickupspawn.c / 32D;
            entityitem.r = (double) packet21pickupspawn.d / 32D;
            return ((Packet) (packet21pickupspawn));
        }
        if (a instanceof EntityPlayerMP) {
            return ((Packet) (new Packet20NamedEntitySpawn((EntityPlayer) a)));
        }
        if (a instanceof EntityMinecart) {
            EntityMinecart entityminecart = (EntityMinecart) a;

            if (entityminecart.d == 0) {
                return ((Packet) (new Packet23VehicleSpawn(a, 10)));
            }
            if (entityminecart.d == 1) {
                return ((Packet) (new Packet23VehicleSpawn(a, 11)));
            }
            if (entityminecart.d == 2) {
                return ((Packet) (new Packet23VehicleSpawn(a, 12)));
            }
        }
        if (a instanceof EntityBoat) {
            return ((Packet) (new Packet23VehicleSpawn(a, 1)));
        }
        if (a instanceof IAnimals) {
            return ((Packet) (new Packet24MobSpawn((EntityLiving) a)));
        }
        if (a instanceof EntityFish) {
            return ((Packet) (new Packet23VehicleSpawn(a, 90)));
        }
        if (a instanceof EntityArrow) {
            return ((Packet) (new Packet23VehicleSpawn(a, 60)));
        }
        if (a instanceof EntitySnowball) {
            return ((Packet) (new Packet23VehicleSpawn(a, 61)));
        }
        if (a instanceof EntityEgg) {
            return ((Packet) (new Packet23VehicleSpawn(a, 62)));
        }
        if (a instanceof EntityTNTPrimed) {
            return ((Packet) (new Packet23VehicleSpawn(a, 50)));
        }
        if (a instanceof EntityFallingSand) {
            EntityFallingSand entityfallingsand = (EntityFallingSand) a;

            if (entityfallingsand.a == Block.E.bi) {
                return ((Packet) (new Packet23VehicleSpawn(a, 70)));
            }
            if (entityfallingsand.a == Block.F.bi) {
                return ((Packet) (new Packet23VehicleSpawn(a, 71)));
            }
        }
        if (a instanceof EntityPainting) {
            return ((Packet) (new Packet25((EntityPainting) a)));
        } else {
            throw new IllegalArgumentException((new StringBuilder()).append("Don't know how to add ").append(((((a)).getClass()))).append("!").toString());
        }
    }

    public void c(EntityPlayerMP entityplayermp) {
        if (n.contains(((entityplayermp)))) {
            n.remove(((entityplayermp)));
            entityplayermp.a.b(((Packet) (new Packet29DestroyEntity(a.g))));
        }
    }
}
