package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public class PortalTravelAgent {

    private final WorldServer a;
    private final Random b;
    private final LongHashMap c = new LongHashMap();
    private final List d = new ArrayList();

    public PortalTravelAgent(WorldServer worldserver) {
        this.a = worldserver;
        this.b = new Random(worldserver.getSeed());
    }

    public void a(Entity entity, double d0, double d1, double d2, float f) {
        if (this.a.worldProvider.dimension != 1) {
            if (!this.b(entity, d0, d1, d2, f)) {
                this.a(entity);
                this.b(entity, d0, d1, d2, f);
            }
        } else {
            // CraftBukkit start - Modularize end portal creation
            ChunkCoordinates created = this.createEndPortal(d0, d1, d2);
            entity.setPositionRotation((double) created.x, (double) created.y, (double) created.z, entity.yaw, 0.0F);
            entity.motX = entity.motY = entity.motZ = 0.0D;
        }
    }

    // Split out from original a(Entity, double, double, double, float) method in order to enable being called from createPortal
    private ChunkCoordinates createEndPortal(double x, double y, double z) {
            int i = MathHelper.floor(x);
            int j = MathHelper.floor(y) - 1;
            int k = MathHelper.floor(z);
            // CraftBukkit end
            byte b0 = 1;
            byte b1 = 0;

            for (int l = -2; l <= 2; ++l) {
                for (int i1 = -2; i1 <= 2; ++i1) {
                    for (int j1 = -1; j1 < 3; ++j1) {
                        int k1 = i + i1 * b0 + l * b1;
                        int l1 = j + j1;
                        int i2 = k + i1 * b1 - l * b0;
                        boolean flag = j1 < 0;

                        this.a.setTypeIdUpdate(k1, l1, i2, flag ? Block.OBSIDIAN.id : 0);
                    }
                }
            }

        // CraftBukkit start
        return new ChunkCoordinates(i, j, k);
    }

    // use logic based on creation to verify end portal
    private ChunkCoordinates findEndPortal(ChunkCoordinates portal) {
        int i = portal.x;
        int j = portal.y - 1;
        int k = portal.z;
        byte b0 = 1;
        byte b1 = 0;

        for (int l = -2; l <= 2; ++l) {
            for (int i1 = -2; i1 <= 2; ++i1) {
                for (int j1 = -1; j1 < 3; ++j1) {
                    int k1 = i + i1 * b0 + l * b1;
                    int l1 = j + j1;
                    int i2 = k + i1 * b1 - l * b0;
                    boolean flag = j1 < 0;

                    if (this.a.getTypeId(k1, l1, i2) != (flag ? Block.OBSIDIAN.id : 0)) {
                        return null;
                    }
                }
            }
        }
        return new ChunkCoordinates(i, j, k);
    }
    // CraftBukkit end

    public boolean b(Entity entity, double d0, double d1, double d2, float f) {
        // CraftBukkit start - Modularize portal search process and entity teleportation
        ChunkCoordinates found = this.findPortal(entity.locX, entity.locY, entity.locZ, 128);
        if (found == null) {
            return false;
        }

        Location exit = new Location(this.a.getWorld(), found.x, found.y, found.z, f, entity.pitch);
        Vector velocity = entity.getBukkitEntity().getVelocity();
        this.adjustExit(entity, exit, velocity);
        entity.setPositionRotation(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
        if (entity.motX != velocity.getX() || entity.motY != velocity.getY() || entity.motZ != velocity.getZ()) {
            entity.getBukkitEntity().setVelocity(velocity);
        }
        return true;
    }

    public ChunkCoordinates findPortal(double x, double y, double z, int short1) {
        if (this.a.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
            return this.findEndPortal(this.a.worldProvider.h());
        }
        // CraftBukkit end
        double d3 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        // CraftBukkit start
        int l = MathHelper.floor(x);
        int i1 = MathHelper.floor(z);
        // CraftBukkit end
        long j1 = ChunkCoordIntPair.a(l, i1);
        boolean flag = true;
        double d4;
        int k1;

        if (this.c.contains(j1)) {
            ChunkCoordinatesPortal chunkcoordinatesportal = (ChunkCoordinatesPortal) this.c.getEntry(j1);

            d3 = 0.0D;
            i = chunkcoordinatesportal.x;
            j = chunkcoordinatesportal.y;
            k = chunkcoordinatesportal.z;
            chunkcoordinatesportal.d = this.a.getTime();
            flag = false;
        } else {
            for (k1 = l - short1; k1 <= l + short1; ++k1) {
                double d5 = (double) k1 + 0.5D - x; // CraftBukkit

                for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
                    double d6 = (double) l1 + 0.5D - z; // CraftBukkit

                    for (int i2 = this.a.S() - 1; i2 >= 0; --i2) {
                        if (this.a.getTypeId(k1, i2, l1) == Block.PORTAL.id) {
                            while (this.a.getTypeId(k1, i2 - 1, l1) == Block.PORTAL.id) {
                                --i2;
                            }

                            d4 = (double) i2 + 0.5D - y; // CraftBukkit
                            double d7 = d5 * d5 + d4 * d4 + d6 * d6;

                            if (d3 < 0.0D || d7 < d3) {
                                d3 = d7;
                                i = k1;
                                j = i2;
                                k = l1;
                            }
                        }
                    }
                }
            }
        }

        if (d3 >= 0.0D) {
            if (flag) {
                this.c.put(j1, new ChunkCoordinatesPortal(this, i, j, k, this.a.getTime()));
                this.d.add(Long.valueOf(j1));
            }
            // CraftBukkit start - Moved entity teleportation logic into exit
            return new ChunkCoordinates(i, j, k);
        } else {
            return null;
        }
    }
    // Entity repositioning logic split out from original b method and combined with repositioning logic for The End from original a method
    public void adjustExit(Entity entity, Location position, Vector velocity) {
        Location from = position.clone();
        Vector before = velocity.clone();
        int i = position.getBlockX();
        int j = position.getBlockY();
        int k = position.getBlockZ();
        float f = position.getYaw();

        if (this.a.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
            // entity.setPositionRotation((double) i, (double) j, (double) k, entity.yaw, 0.0F);
            // entity.motX = entity.motY = entity.motZ = 0.0D;
            position.setPitch(0.0F);
            velocity.setX(0);
            velocity.setY(0);
            velocity.setZ(0);
        } else {
            double d4;
            int k1;
            // CraftBukkit end

            double d8 = (double) i + 0.5D;
            double d9 = (double) j + 0.5D;

            d4 = (double) k + 0.5D;
            int j2 = -1;

            if (this.a.getTypeId(i - 1, j, k) == Block.PORTAL.id) {
                j2 = 2;
            }

            if (this.a.getTypeId(i + 1, j, k) == Block.PORTAL.id) {
                j2 = 0;
            }

            if (this.a.getTypeId(i, j, k - 1) == Block.PORTAL.id) {
                j2 = 3;
            }

            if (this.a.getTypeId(i, j, k + 1) == Block.PORTAL.id) {
                j2 = 1;
            }

            int k2 = entity.at();

            if (j2 > -1) {
                int l2 = Direction.h[j2];
                int i3 = Direction.a[j2];
                int j3 = Direction.b[j2];
                int k3 = Direction.a[l2];
                int l3 = Direction.b[l2];
                boolean flag1 = !this.a.isEmpty(i + i3 + k3, j, k + j3 + l3) || !this.a.isEmpty(i + i3 + k3, j + 1, k + j3 + l3);
                boolean flag2 = !this.a.isEmpty(i + i3, j, k + j3) || !this.a.isEmpty(i + i3, j + 1, k + j3);

                if (flag1 && flag2) {
                    j2 = Direction.f[j2];
                    l2 = Direction.f[l2];
                    i3 = Direction.a[j2];
                    j3 = Direction.b[j2];
                    k3 = Direction.a[l2];
                    l3 = Direction.b[l2];
                    k1 = i - k3;
                    d8 -= (double) k3;
                    int i4 = k - l3;

                    d4 -= (double) l3;
                    flag1 = !this.a.isEmpty(k1 + i3 + k3, j, i4 + j3 + l3) || !this.a.isEmpty(k1 + i3 + k3, j + 1, i4 + j3 + l3);
                    flag2 = !this.a.isEmpty(k1 + i3, j, i4 + j3) || !this.a.isEmpty(k1 + i3, j + 1, i4 + j3);
                }

                float f1 = 0.5F;
                float f2 = 0.5F;

                if (!flag1 && flag2) {
                    f1 = 1.0F;
                } else if (flag1 && !flag2) {
                    f1 = 0.0F;
                } else if (flag1 && flag2) {
                    f2 = 0.0F;
                }

                d8 += (double) ((float) k3 * f1 + f2 * (float) i3);
                d4 += (double) ((float) l3 * f1 + f2 * (float) j3);
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = 0.0F;

                if (j2 == k2) {
                    f3 = 1.0F;
                    f4 = 1.0F;
                } else if (j2 == Direction.f[k2]) {
                    f3 = -1.0F;
                    f4 = -1.0F;
                } else if (j2 == Direction.g[k2]) {
                    f5 = 1.0F;
                    f6 = -1.0F;
                } else {
                    f5 = -1.0F;
                    f6 = 1.0F;
                }

                // CraftBukkit start
                double d10 = velocity.getX();
                double d11 = velocity.getZ();
                // CraftBukkit end

                // CraftBukkit start - Adjust position and velocity instances instead of entity
                velocity.setX(d10 * (double) f3 + d11 * (double) f6);
                velocity.setZ(d10 * (double) f5 + d11 * (double) f4);
                f = f - (float) (k2 * 90) + (float) (j2 * 90);
            } else {
                // entity.motX = entity.motY = entity.motZ = 0.0D;
                velocity.setX(0);
                velocity.setY(0);
                velocity.setZ(0);
            }

            // entity.setPositionRotation(d8, d9, d4, entity.yaw, entity.pitch);
            position.setX(d8);
            position.setY(d9);
            position.setZ(d4);
            position.setYaw(f);
        }

        EntityPortalExitEvent event = new EntityPortalExitEvent(entity.getBukkitEntity(), from, position, before, velocity);
        this.a.getServer().getPluginManager().callEvent(event);
        Location to = event.getTo();
        if (event.isCancelled() || to == null || !entity.isAlive()) {
            position.setX(from.getX());
            position.setY(from.getY());
            position.setZ(from.getZ());
            position.setYaw(from.getYaw());
            position.setPitch(from.getPitch());
            velocity.copy(before);
        } else {
            position.setX(to.getX());
            position.setY(to.getY());
            position.setZ(to.getZ());
            position.setYaw(to.getYaw());
            position.setPitch(to.getPitch());
            velocity.copy(event.getAfter()); // event.getAfter() will never be null, as setAfter() will cause an NPE if null is passed in
        }
        // CraftBukkit end
    }

    public boolean a(Entity entity) {
        // CraftBukkit start - Allow for portal creation to be based on coordinates instead of entity
        return this.createPortal(entity.locX, entity.locY, entity.locZ, 16);
    }

    public boolean createPortal(double x, double y, double z, int b0) {
        if (this.a.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
            this.createEndPortal(x, y, z);
            return true;
        }
        // CraftBukkit end
        double d0 = -1.0D;
        // CraftBukkit start
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        // CraftBukkit end
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.b.nextInt(4);

        int i2;
        double d1;
        double d2;
        int j2;
        int k2;
        int l2;
        int i3;
        int j3;
        int k3;
        int l3;
        int i4;
        int j4;
        int k4;
        double d3;
        double d4;

        for (i2 = i - b0; i2 <= i + b0; ++i2) {
            d1 = (double) i2 + 0.5D - x; // CraftBukkit

            for (j2 = k - b0; j2 <= k + b0; ++j2) {
                d2 = (double) j2 + 0.5D - z; // CraftBukkit

                label274:
                for (k2 = this.a.S() - 1; k2 >= 0; --k2) {
                    if (this.a.isEmpty(i2, k2, j2)) {
                        while (k2 > 0 && this.a.isEmpty(i2, k2 - 1, j2)) {
                            --k2;
                        }

                        for (i3 = l1; i3 < l1 + 4; ++i3) {
                            l2 = i3 % 2;
                            k3 = 1 - l2;
                            if (i3 % 4 >= 2) {
                                l2 = -l2;
                                k3 = -k3;
                            }

                            for (j3 = 0; j3 < 3; ++j3) {
                                for (i4 = 0; i4 < 4; ++i4) {
                                    for (l3 = -1; l3 < 4; ++l3) {
                                        k4 = i2 + (i4 - 1) * l2 + j3 * k3;
                                        j4 = k2 + l3;
                                        int l4 = j2 + (i4 - 1) * k3 - j3 * l2;

                                        if (l3 < 0 && !this.a.getMaterial(k4, j4, l4).isBuildable() || l3 >= 0 && !this.a.isEmpty(k4, j4, l4)) {
                                            continue label274;
                                        }
                                    }
                                }
                            }

                            d3 = (double) k2 + 0.5D - y; // CraftBukkit
                            d4 = d1 * d1 + d3 * d3 + d2 * d2;
                            if (d0 < 0.0D || d4 < d0) {
                                d0 = d4;
                                l = i2;
                                i1 = k2;
                                j1 = j2;
                                k1 = i3 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D) {
            for (i2 = i - b0; i2 <= i + b0; ++i2) {
                d1 = (double) i2 + 0.5D - x; // CraftBukkit

                for (j2 = k - b0; j2 <= k + b0; ++j2) {
                    d2 = (double) j2 + 0.5D - z; // CraftBukkit

                    label222:
                    for (k2 = this.a.S() - 1; k2 >= 0; --k2) {
                        if (this.a.isEmpty(i2, k2, j2)) {
                            while (k2 > 0 && this.a.isEmpty(i2, k2 - 1, j2)) {
                                --k2;
                            }

                            for (i3 = l1; i3 < l1 + 2; ++i3) {
                                l2 = i3 % 2;
                                k3 = 1 - l2;

                                for (j3 = 0; j3 < 4; ++j3) {
                                    for (i4 = -1; i4 < 4; ++i4) {
                                        l3 = i2 + (j3 - 1) * l2;
                                        k4 = k2 + i4;
                                        j4 = j2 + (j3 - 1) * k3;
                                        if (i4 < 0 && !this.a.getMaterial(l3, k4, j4).isBuildable() || i4 >= 0 && !this.a.isEmpty(l3, k4, j4)) {
                                            continue label222;
                                        }
                                    }
                                }

                                d3 = (double) k2 + 0.5D - y; // CraftBukkit
                                d4 = d1 * d1 + d3 * d3 + d2 * d2;
                                if (d0 < 0.0D || d4 < d0) {
                                    d0 = d4;
                                    l = i2;
                                    i1 = k2;
                                    j1 = j2;
                                    k1 = i3 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int i5 = l;
        int j5 = i1;

        j2 = j1;
        int k5 = k1 % 2;
        int l5 = 1 - k5;

        if (k1 % 4 >= 2) {
            k5 = -k5;
            l5 = -l5;
        }

        boolean flag;

        if (d0 < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > this.a.S() - 10) {
                i1 = this.a.S() - 10;
            }

            j5 = i1;

            for (k2 = -1; k2 <= 1; ++k2) {
                for (i3 = 1; i3 < 3; ++i3) {
                    for (l2 = -1; l2 < 3; ++l2) {
                        k3 = i5 + (i3 - 1) * k5 + k2 * l5;
                        j3 = j5 + l2;
                        i4 = j2 + (i3 - 1) * l5 - k2 * k5;
                        flag = l2 < 0;
                        this.a.setTypeIdUpdate(k3, j3, i4, flag ? Block.OBSIDIAN.id : 0);
                    }
                }
            }
        }

        for (k2 = 0; k2 < 4; ++k2) {
            for (i3 = 0; i3 < 4; ++i3) {
                for (l2 = -1; l2 < 4; ++l2) {
                    k3 = i5 + (i3 - 1) * k5;
                    j3 = j5 + l2;
                    i4 = j2 + (i3 - 1) * l5;
                    flag = i3 == 0 || i3 == 3 || l2 == -1 || l2 == 3;
                    this.a.setTypeIdAndData(k3, j3, i4, flag ? Block.OBSIDIAN.id : Block.PORTAL.id, 0, 2);
                }
            }

            for (i3 = 0; i3 < 4; ++i3) {
                for (l2 = -1; l2 < 4; ++l2) {
                    k3 = i5 + (i3 - 1) * k5;
                    j3 = j5 + l2;
                    i4 = j2 + (i3 - 1) * l5;
                    this.a.applyPhysics(k3, j3, i4, this.a.getTypeId(k3, j3, i4));
                }
            }
        }

        return true;
    }

    public void a(long i) {
        if (i % 100L == 0L) {
            Iterator iterator = this.d.iterator();
            long j = i - 600L;

            while (iterator.hasNext()) {
                Long olong = (Long) iterator.next();
                ChunkCoordinatesPortal chunkcoordinatesportal = (ChunkCoordinatesPortal) this.c.getEntry(olong.longValue());

                if (chunkcoordinatesportal == null || chunkcoordinatesportal.d < j) {
                    iterator.remove();
                    this.c.remove(olong.longValue());
                }
            }
        }
    }
}
