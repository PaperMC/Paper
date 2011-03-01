package net.minecraft.server;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public int l = 0;
    private double o;
    private double p;
    private double q;
    private boolean r = false;
    private boolean s;
    public boolean m = false;
    public Set n = new HashSet();

    public EntityTrackerEntry(Entity entity, int i, int j, boolean flag) {
        this.a = entity;
        this.b = i;
        this.c = j;
        this.s = flag;
        this.d = MathHelper.b(entity.locX * 32.0D);
        this.e = MathHelper.b(entity.locY * 32.0D);
        this.f = MathHelper.b(entity.locZ * 32.0D);
        this.g = MathHelper.d(entity.yaw * 256.0F / 360.0F);
        this.h = MathHelper.d(entity.pitch * 256.0F / 360.0F);
    }

    public boolean equals(Object object) {
        return object instanceof EntityTrackerEntry ? ((EntityTrackerEntry) object).a.id == this.a.id : false;
    }

    public int hashCode() {
        return this.a.id;
    }

    public void a(List list) {
        this.m = false;
        if (!this.r || this.a.d(this.o, this.p, this.q) > 16.0D) {
            this.o = this.a.locX;
            this.p = this.a.locY;
            this.q = this.a.locZ;
            this.r = true;
            this.m = true;
            this.b(list);
        }

        if (++this.l % this.c == 0) {
            int i = MathHelper.b(this.a.locX * 32.0D);
            int j = MathHelper.b(this.a.locY * 32.0D);
            int k = MathHelper.b(this.a.locZ * 32.0D);
            int l = MathHelper.d(this.a.yaw * 256.0F / 360.0F);
            int i1 = MathHelper.d(this.a.pitch * 256.0F / 360.0F);
            int j1 = i - this.d;
            int k1 = j - this.e;
            int l1 = k - this.f;
            Object object = null;
            boolean flag = Math.abs(i) >= 8 || Math.abs(j) >= 8 || Math.abs(k) >= 8;
            boolean flag1 = Math.abs(l - this.g) >= 8 || Math.abs(i1 - this.h) >= 8;
            
            //CraftBukkit - Create relative movement packet only if distance is greater than zero.
            int distanceSquared = j1*j1+k1*k1+l1*l1; 
            flag = (distanceSquared > 0) && flag;
            //CraftBukkit stop

            if (j1 >= -128 && j1 < 128 && k1 >= -128 && k1 < 128 && l1 >= -128 && l1 < 128) {
                if (flag && flag1) {
                    object = new Packet33RelEntityMoveLook(this.a.id, (byte) j1, (byte) k1, (byte) l1, (byte) l, (byte) i1);
                } else if (flag) {
                    object = new Packet31RelEntityMove(this.a.id, (byte) j1, (byte) k1, (byte) l1);
                } else if (flag1) {
                    object = new Packet32EntityLook(this.a.id, (byte) l, (byte) i1);
                }
            } else {
                object = new Packet34EntityTeleport(this.a.id, i, j, k, (byte) l, (byte) i1);
            }

            if (this.s) {
                double d0 = this.a.motX - this.i;
                double d1 = this.a.motY - this.j;
                double d2 = this.a.motZ - this.k;
                double d3 = 0.02D;
                double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d4 > d3 * d3 || d4 > 0.0D && this.a.motX == 0.0D && this.a.motY == 0.0D && this.a.motZ == 0.0D) {
                    this.i = this.a.motX;
                    this.j = this.a.motY;
                    this.k = this.a.motZ;
                    this.a((Packet) (new Packet28EntityVelocity(this.a.id, this.i, this.j, this.k)));
                }
            }

            if (object != null) {
                this.a((Packet) object);
            }

            DataWatcher datawatcher = this.a.O();

            if (datawatcher.a()) {
                this.b((Packet) (new Packet40EntityMetadata(this.a.id, datawatcher)));
            }

            if (flag) {
                this.d = i;
                this.e = j;
                this.f = k;
            }

            if (flag1) {
                this.g = l;
                this.h = i1;
            }
        }

        if (this.a.aY) {
            this.b((Packet) (new Packet28EntityVelocity(this.a)));
            this.a.aY = false;
        }
    }

    public void a(Packet packet) {
        Iterator iterator = this.n.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            entityplayer.a.b(packet);
        }
    }

    public void b(Packet packet) {
        this.a(packet);
        if (this.a instanceof EntityPlayer) {
            ((EntityPlayer) this.a).a.b(packet);
        }
    }

    public void a() {
        this.a((Packet) (new Packet29DestroyEntity(this.a.id)));
    }

    public void a(EntityPlayer entityplayer) {
        if (this.n.contains(entityplayer)) {
            this.n.remove(entityplayer);
        }
    }

    public void b(EntityPlayer entityplayer) {
        if (entityplayer != this.a) {
            double d0 = entityplayer.locX - (double) (this.d / 32);
            double d1 = entityplayer.locZ - (double) (this.f / 32);

            if (d0 >= (double) (-this.b) && d0 <= (double) this.b && d1 >= (double) (-this.b) && d1 <= (double) this.b) {
                // CraftBukkit
                if ((!this.n.contains(entityplayer)) && (this.a.world == entityplayer.world)) {
                    this.n.add(entityplayer);
                    entityplayer.a.b(this.b());
                    if (this.s) {
                        entityplayer.a.b((Packet) (new Packet28EntityVelocity(this.a.id, this.a.motX, this.a.motY, this.a.motZ)));
                    }

                    ItemStack[] aitemstack = this.a.k_();

                    if (aitemstack != null) {
                        for (int i = 0; i < aitemstack.length; ++i) {
                            entityplayer.a.b((Packet) (new Packet5EntityEquipment(this.a.id, i, aitemstack[i])));
                        }
                    }
                }
            } else if (this.n.contains(entityplayer)) {
                this.n.remove(entityplayer);
                entityplayer.a.b((Packet) (new Packet29DestroyEntity(this.a.id)));
            }
        }
    }

    public void b(List list) {
        for (int i = 0; i < list.size(); ++i) {
            this.b((EntityPlayer) list.get(i));
        }
    }

    private Packet b() {
        if (this.a instanceof EntityItem) {
            EntityItem entityitem = (EntityItem) this.a;
            Packet21PickupSpawn packet21pickupspawn = new Packet21PickupSpawn(entityitem);

            entityitem.locX = (double) packet21pickupspawn.b / 32.0D;
            entityitem.locY = (double) packet21pickupspawn.c / 32.0D;
            entityitem.locZ = (double) packet21pickupspawn.d / 32.0D;
            return packet21pickupspawn;
        } else if (this.a instanceof EntityPlayer) {
            return new Packet20NamedEntitySpawn((EntityHuman) this.a);
        } else {
            if (this.a instanceof EntityMinecart) {
                EntityMinecart entityminecart = (EntityMinecart) this.a;

                if (entityminecart.d == 0) {
                    return new Packet23VehicleSpawn(this.a, 10);
                }

                if (entityminecart.d == 1) {
                    return new Packet23VehicleSpawn(this.a, 11);
                }

                if (entityminecart.d == 2) {
                    return new Packet23VehicleSpawn(this.a, 12);
                }
            }

            if (this.a instanceof EntityBoat) {
                return new Packet23VehicleSpawn(this.a, 1);
            } else if (this.a instanceof IAnimal) {
                return new Packet24MobSpawn((EntityLiving) this.a);
            } else if (this.a instanceof EntityFish) {
                return new Packet23VehicleSpawn(this.a, 90);
            } else if (this.a instanceof EntityArrow) {
                return new Packet23VehicleSpawn(this.a, 60);
            } else if (this.a instanceof EntitySnowball) {
                return new Packet23VehicleSpawn(this.a, 61);
            } else if (this.a instanceof EntityEgg) {
                return new Packet23VehicleSpawn(this.a, 62);
            } else if (this.a instanceof EntityTNTPrimed) {
                return new Packet23VehicleSpawn(this.a, 50);
            } else {
                if (this.a instanceof EntityFallingSand) {
                    EntityFallingSand entityfallingsand = (EntityFallingSand) this.a;

                    if (entityfallingsand.a == Block.SAND.id) {
                        return new Packet23VehicleSpawn(this.a, 70);
                    }

                    if (entityfallingsand.a == Block.GRAVEL.id) {
                        return new Packet23VehicleSpawn(this.a, 71);
                    }
                }

                if (this.a instanceof EntityPainting) {
                    return new Packet25EntityPainting((EntityPainting) this.a);
                } else {
                    throw new IllegalArgumentException("Don\'t know how to add " + this.a.getClass() + "!");
                }
            }
        }
    }

    public void c(EntityPlayer entityplayer) {
        if (this.n.contains(entityplayer)) {
            this.n.remove(entityplayer);
            entityplayer.a.b((Packet) (new Packet29DestroyEntity(this.a.id)));
        }
    }
}
